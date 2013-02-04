package kroki.app.action;

import java.awt.event.ActionEvent;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
import kroki.app.generators.DatabaseConfigGenerator;
import kroki.app.generators.EJBGenerator;
import kroki.app.generators.MenuGenerator;
import kroki.app.generators.PanelGenerator;
import kroki.app.generators.utils.Attribute;
import kroki.app.generators.utils.EJBClass;
import kroki.app.generators.utils.ManyToOneAttribute;
import kroki.app.generators.utils.Menu;
import kroki.app.generators.utils.OneToManyAttribute;
import kroki.app.generators.utils.Submenu;
import kroki.app.utils.TypeComponentMapper;
import kroki.app.view.Canvas;
import kroki.commons.camelcase.CamelCaser;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Zoom;
import kroki.profil.operation.BussinessOperation;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.panel.mode.ViewMode;
import kroki.profil.panel.std.StdPanelSettings;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;

import com.panelcomposer.core.MainApp;

/**
 * Action that generates configuration xml files for web app 
 */
public class ExportSwingAction extends AbstractAction {

	//list of EJB classes to be generated
	ArrayList<EJBClass> classes;
	//list of menus to be generated
	ArrayList<Menu> menus;
	//list of standard forms to be nerated
	ArrayList<VisibleElement> elements;
	//TypeComponentMapper tcm = new TypeComponentMapper();

	public ExportSwingAction() {
		putValue(NAME, "Desktop Application");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		//find selected project from workspace
		BussinesSubsystem proj = null;
		try {
			String selectedNoded = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath().getLastPathComponent().toString();
			for(int j=0; j<KrokiMockupToolApp.getInstance().getWorkspace().getPackageCount(); j++) {
				BussinesSubsystem pack = (BussinesSubsystem)KrokiMockupToolApp.getInstance().getWorkspace().getPackageAt(j);
				if(pack.getLabel().equals(selectedNoded)) {
					proj = pack;
				}
			}

			classes = new ArrayList<EJBClass>();
			menus = new ArrayList<Menu>();
			elements = new ArrayList<VisibleElement>();
			EJBGenerator ejbGenerator = new EJBGenerator();
			MenuGenerator menuGenerator = new MenuGenerator();
			PanelGenerator panelGenerator = new PanelGenerator();
			DatabaseConfigGenerator dbConfigGenerator = new DatabaseConfigGenerator(proj.getDBConnectionProps());


			//iteration trough project elements and retrieving of usable data for generators
			for(int i=0; i<proj.ownedElementCount(); i++) {
				VisibleElement el = proj.getOwnedElementAt(i);
				if(el instanceof BussinesSubsystem) {
					getSubSystemData(el, i);
				}else if(el instanceof VisibleClass) {
					getClassData(el, "", null);
				}
			}

			//CONFIGURATION FILES GENERATION
			menuGenerator.generate(menus);
			panelGenerator.generate(elements);
			ejbGenerator.generateEJBXmlFiles(classes);
			ejbGenerator.generateEJBClasses(classes, true);
			ejbGenerator.generateXMLMappingFile(classes);
			dbConfigGenerator.generateFilesForDesktopApp();

			//write project label as mainframe title in main.properties file
			writeProjectName(proj.getLabel());

			//start app
			//MainApp mapp = new MainApp();
			//mapp.main(null);

		} catch (NullPointerException e2) {
			//if no project is selected, inform user to select one
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
		}
	}

	//fetches class (panel) data from the model
	public void getClassData(VisibleElement el, String classPackage, Menu menu) {
		CamelCaser cc = new CamelCaser();

		if(el instanceof StandardPanel) {
			StandardPanel sp = (StandardPanel)el;
			StdPanelSettings sps = sp.getStdPanelSettings();
			VisibleClass vc = (VisibleClass)el;

			//LISTE ATRIBUTA ZA EJB KLASU
			ArrayList<Attribute> attributes = new ArrayList<Attribute>();
			ArrayList<ManyToOneAttribute> mtoAttributes = new ArrayList<ManyToOneAttribute>();
			ArrayList<OneToManyAttribute> otmAttributes = new ArrayList<OneToManyAttribute>();

			/***********************************************/
			/*    DATA USED FOR EJB CLASS GENERATION      */
			/*one ejb class is generated for every panel */
			/********************************************/
			for(int j=0; j<vc.containedProperties().size(); j++) {
				VisibleProperty vp = vc.containedProperties().get(j);

				String type = "java.lang.String";
				if(vp.getComponentType() == ComponentType.TEXT_FIELD) {
					if(vp.getDataType().equals("BigDecimal")) {
						type = "java.math.BigDecimal";
					}else if(vp.getDataType().equals("Date")) {
						type = "java.util.Date";
					}
				}else if(vp.getComponentType() == ComponentType.CHECK_BOX) {
					type =  "java.lang.Boolean";
				} 


				Attribute attr = new Attribute(cc.toCamelCase(vp.getLabel(), true), vp.getColumnLabel(), vp.getLabel(), type, false, true);
				attr.setRepresentative(vp.isRepresentative());
				attributes.add(attr);
			}

			for(int l=0; l<vc.containedZooms().size(); l++) {
				Zoom z = vc.containedZooms().get(l);
				StandardPanel zsp = (StandardPanel) z.getTargetPanel();
				EJBClass zcl = getClass(zsp.getPersistentClass().name());
				//adding ManyToOne (zoom) attribute
				String n = z.getLabel().substring(0, 1).toLowerCase() + z.getLabel().substring(1);
				String reffColumn = "id";
				String type = cc.toCamelCase(z.getTargetPanel().getComponent().getName(), false);

				ManyToOneAttribute mto = new ManyToOneAttribute(cc.toCamelCase(z.getTargetPanel().getComponent().getName(), true), n, z.getLabel(), type, true);
				mtoAttributes.add(mto);

				//add OneToMany attribute to opposite and of association
				if(zcl != null) {
					String name = sp.getPersistentClass().name().substring(0, 1).toLowerCase() + sp.getPersistentClass().name().substring(1) + "Set";
					String label = z.getLabel();
					String reffTable = sp.getPersistentClass().name();
					String mappedBy = cc.toCamelCase(z.getLabel(), true);

					//fetching of all representative attributes that will be displayed in zoom field
					for(int m=0; m<zcl.getAttributes().size(); m++) {
						Attribute a = zcl.getAttributes().get(m);

						if(a.getRepresentative()) {
							System.out.println("atribut" + a.getLabel() + "JE reprezentativan");
							mto.getColumnRefs().add(a);
						}else {
							System.out.println("atribut" + a.getLabel() + "NIJE reprezentativan");
						}
					}

					OneToManyAttribute otm = new OneToManyAttribute(name, label, reffTable, mappedBy);
					zcl.getOneToManyAttributes().add(otm);

				}else {
					System.out.println("NULL majku mu");
				}

			}

			//EJB class instance for panel is created and passed to generator
			EJBClass ejb = new EJBClass("ejb", sp.getPersistentClass().name(), sp.getLabel(), attributes, mtoAttributes, otmAttributes);
			classes.add(ejb);


			   /**************************************/
			  /*        SUBMENU GENERATION DATA     */
			 /*        one menu item per panel     */
			/**************************************/
			String activate = ejb.getName().toLowerCase() + "_st";
			String label = ejb.getLabel();
			String panel_type = "standard-panel";
			Submenu sub = new Submenu(activate, label, panel_type);
			//if it is in a subsystem, it is aded as submenu item
			if(menu != null) {
				menu.add(sub);
			}else {
				//if panel is in root of workspace, it gets it's item in main menu
				Menu men = new Menu("menu" + activate, label, new ArrayList<Submenu>());
				men.add(sub);
				menus.add(men);
			}
		}else if (el instanceof ParentChild) {
			ParentChild pcPanel = (ParentChild)el;
			String activate = cc.toCamelCase(pcPanel.name(), false) + "_pc";
			String label = pcPanel.getLabel();
			String panel_type = "parent-child";
			Submenu sub = new Submenu(activate, label, panel_type);
			if(menu != null) {
				menu.add(sub);
			}else {
				Menu men = new Menu("menu" + activate, label, new ArrayList<Submenu>());
				men.add(sub);
				menus.add(men);
			}
		}
		elements.add(el);
	}


	//FETCHING SUBSYSTEM DATA USED FOR FILES GENERATION
	public void getSubSystemData(VisibleElement el, int index) {
		   /*****************************************/
		  /*          MENU GENERATION DATA         */
		 /*          one menu per package         */
		/*****************************************/
		String name = "menu" + index;
		String label = el.name().replace("_", " ");
		Menu menu = new Menu(name, label, new ArrayList<Submenu>());
		menus.add(menu);

		BussinesSubsystem bs = (BussinesSubsystem) el;

		for(int m=0; m<bs.ownedElementCount(); m++) {
			VisibleElement e = bs.getOwnedElementAt(m);
			if(e instanceof VisibleClass) {
				getClassData(e, el.name(), menu);
			}else if (e instanceof BussinesSubsystem) {
				getSubSystemData(e, index+1);
			}
		}

	}

	//gets refference to ejb class from model based on name
	public EJBClass getClass(String name) {
		System.out.println("trazim klasu sa imenom " + name);
		EJBClass clas = null;
		for(int i=0; i<classes.size(); i++) {
			EJBClass cl = classes.get(i);
			System.out.println("imam " + cl.getName());
			if(cl.getName().equalsIgnoreCase(name)) {
				clas = cl;
			}
		}
		return clas;
	}

	public void writeProjectName(String name) {
		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
		File propertiesFile = new File(appPath.substring(0, appPath.length()-16) + "SwingApp" + File.separator + "props" + File.separator + "main.properties");

		//read main.properties file
		//and append first line which contains main form title
		Scanner scan;
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("main.form.name=" + name);
		try {
			scan = new Scanner(propertiesFile);
			while(scan.hasNext()){
				String line = scan.nextLine();
				if(!line.startsWith("main.form.name")) {
					lines.add(line);
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("[ERROR] main.properties file not found");
		}

		//write main.properties file with main form title
		try {
			PrintWriter pw=new PrintWriter(new FileOutputStream(propertiesFile));
			for(int i=0; i<lines.size(); i++) {
				pw.println(lines.get(i));
			}
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
