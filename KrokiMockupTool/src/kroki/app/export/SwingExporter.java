package kroki.app.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

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
import kroki.app.utils.RunAnt;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Zoom;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.panel.std.StdPanelSettings;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Class that provides methods used for exporting KROKI projects
 * as working Java Swing application
 * @author Milorad Filipovic
 */
public class SwingExporter {
	
	//project that is exported
	private BussinesSubsystem project;
	//list of EJB classes to be generated
	private ArrayList<EJBClass> classes;
	//list of menus to be generated
	private ArrayList<Menu> menus;
	//list of standard forms to be nerated
	private ArrayList<VisibleElement> elements;
	//configuration files generators
	private EJBGenerator ejbGenerator;
	private MenuGenerator menuGenerator;
	private PanelGenerator panelGenerator;
	private DatabaseConfigGenerator dbConfigGenerator;

	public SwingExporter() {
		classes = new ArrayList<EJBClass>();
		menus = new ArrayList<Menu>();
		elements = new ArrayList<VisibleElement>();
		ejbGenerator = new EJBGenerator();
		menuGenerator = new MenuGenerator();
		panelGenerator = new PanelGenerator();
	}

	/**
	 * Project is exported as runnable jar file
	 * on given location
	 * @param file location to which project needs to be exported
	 * @param proj project that is exported
	 * @message message that is dispayed on finish
	 */
	public void export(File file, BussinesSubsystem proj, String message) {
		dbConfigGenerator = new DatabaseConfigGenerator(proj.getDBConnectionProps());
		this.project = proj;
		
		//iteration trough project elements and retrieving of usable data for generators
		for(int i=0; i<proj.ownedElementCount(); i++) {
			VisibleElement el = proj.getOwnedElementAt(i);
			if(el instanceof BussinesSubsystem) {
				getSubSystemData(el, i, null);
			}else if(el instanceof VisibleClass) {
				getClassData(el, "", null);
			}
		}

		NamingUtil namer = new NamingUtil();
		
		//Add User class to classes list
		Attribute usernameAttribute = new Attribute("username", "username", "User name", "java.lang.String", true, true, true);
		Attribute passwordAttribute = new Attribute("password", "password", "Password", "java.lang.String", true, true, false);
		ArrayList<Attribute> userAttributes = new ArrayList<Attribute>();
		userAttributes.add(usernameAttribute);
		userAttributes.add(passwordAttribute);
		String userTableName = namer.toDatabaseFormat(proj.getLabel(), "User");
		
		EJBClass user = new EJBClass("ejb", "User", userTableName, "User", userAttributes, new ArrayList<ManyToOneAttribute>(), new ArrayList<OneToManyAttribute>());
		classes.add(user);

		//Add one-to-many attributes to classes
		addReferences();
		
		//CONFIGURATION FILES GENERATION
		menuGenerator.generate(menus);
		panelGenerator.generate(elements);
		ejbGenerator.generateEJBXmlFiles(classes);
		ejbGenerator.generateEJBClasses(classes, true);
		ejbGenerator.generateXMLMappingFile(classes);
		dbConfigGenerator.generateFilesForDesktopApp();

		//write project label as mainframe title in main.properties file
		writeProjectName(proj.getLabel());

		//run ant script that compiles generated code and makes runnable jar
		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);

		String jarName = proj.getLabel().replace(" ", "_");
		File buildFile = new File(appPath.substring(0, appPath.length()-16) + "SwingApp" + File.separator + "build.xml");
		File outputFile = new File(file.getAbsolutePath() + File.separator + jarName);

		RunAnt runner = new RunAnt();
		runner.runBuild(jarName + ".jar", buildFile, outputFile);
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText(message, 0);
	}

	//fetches class (panel) data from the model
	public void getClassData(VisibleElement el, String classPackage, Menu menu) {
		NamingUtil cc = new NamingUtil();
		System.out.println("OBRADJUJEM: " + el.getLabel());

		if(el instanceof StandardPanel) {
			StandardPanel sp = (StandardPanel)el;
			StdPanelSettings sps = sp.getStdPanelSettings();
			VisibleClass vc = (VisibleClass)el;

			//EJB CLASS ATTRIBUTE LISTS
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

				Attribute attr = new Attribute(cc.toCamelCase(vp.getLabel(), true), vp.getColumnLabel(), vp.getLabel(), type, false, true, vp.isRepresentative());
				attributes.add(attr);
			}

			
			for(int l=0; l<vc.containedZooms().size(); l++) {
			Zoom z = vc.containedZooms().get(l);
			if(z.getTargetPanel() != null) {
				//adding ManyToOne (zoom) attribute
				String n = z.getLabel().substring(0, 1).toLowerCase() + z.getLabel().substring(1);
				String type = cc.toCamelCase(z.getTargetPanel().getComponent().getName(), false);

				ManyToOneAttribute mto = new ManyToOneAttribute(cc.toCamelCase(z.getTargetPanel().getComponent().getName(), true), n, z.getLabel(), type, true);
				mtoAttributes.add(mto);
			}else {
				KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Target panel not set for combozoom '" + z.getLabel() + "' in '" + vc.getLabel() + "'. Skipping that file.", 2);
				return;
			}
		}
			String tableName = cc.toDatabaseFormat(this.project.getLabel(), sp.getLabel());
			
			//EJB class instance for panel is created and passed to generator
			EJBClass ejb = new EJBClass("ejb", sp.getPersistentClass().name(), tableName, sp.getLabel(), attributes, mtoAttributes, otmAttributes);
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
				menu.addSubmenu(sub);
			}else {
				//if panel is in root of workspace, it gets it's item in main menu
				Menu men = new Menu("menu" + activate, label, new ArrayList<Submenu>(), new ArrayList<Menu>());
				men.addSubmenu(sub);
				menus.add(men);
			}
		}else if (el instanceof ParentChild) {
			ParentChild pcPanel = (ParentChild)el;
			String activate = cc.toCamelCase(pcPanel.name(), false) + "_pc";
			String label = pcPanel.getLabel();
			String panel_type = "parent-child";
			Submenu sub = new Submenu(activate, label, panel_type);
			if(menu != null) {
				menu.addSubmenu(sub);
			}else {
				Menu men = new Menu("menu" + activate, label, new ArrayList<Submenu>(), new ArrayList<Menu>());
				men.addSubmenu(sub);
				menus.add(men);
			}
		}
		elements.add(el);
	}


	//FETCHING SUBSYSTEM DATA USED FOR FILES GENERATION
	public void getSubSystemData(VisibleElement el, int index, Menu mmenu) {
		/*****************************************/
		/*          MENU GENERATION DATA         */
		/*          one menu per package         */
		/*****************************************/
		String name = "menu" + index + "_" + el.name();
		String label = el.name().replace("_", " ");
		Menu menu = new Menu(name, label, new ArrayList<Submenu>(), new ArrayList<Menu>());

		BussinesSubsystem bs = (BussinesSubsystem) el;

		for(int m=0; m<bs.ownedElementCount(); m++) {
			VisibleElement e = bs.getOwnedElementAt(m);
			if(e instanceof VisibleClass) {
				getClassData(e, el.name(), menu);
			}else if (e instanceof BussinesSubsystem) {
				getSubSystemData(e, index+1, menu);
			}
		}
		if(mmenu != null) {
			mmenu.addMenu(menu);
		}else {
			menus.add(menu);
		}
	}

	//adding OneToMany to classes on opposite sides of zoom attributes
	//this is done after all panels have been processed and added to classes list
	//which allows child panel to be drawn after parent
	public void addReferences() {
		for(int i=0; i<classes.size(); i++) {
			EJBClass ejbClass = classes.get(i);
			if(!ejbClass.getManyToOneAttributes().isEmpty()) {
				for(int j=0; j<ejbClass.getManyToOneAttributes().size(); j++) {
					ManyToOneAttribute mtrAttribute = ejbClass.getManyToOneAttributes().get(j);
					EJBClass oppositeClass = getClass(mtrAttribute.getType());
					
					if(oppositeClass != null) {
						String name = ejbClass.getName() + "Set";
						String label = mtrAttribute.getLabel();
						String reffTable = ejbClass.getName();
						String mappedBy = mtrAttribute.getName();
						
						OneToManyAttribute otmAttr = new OneToManyAttribute(name, label, reffTable, mappedBy);
						oppositeClass.getOneToManyAttributes().add(otmAttr);
						
						for(int l=0; l<oppositeClass.getAttributes().size(); l++) {
							Attribute atr = oppositeClass.getAttributes().get(l);
							if(atr.getRepresentative()) {
								mtrAttribute.getColumnRefs().add(atr);
							}
						}
					}else {
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Class '" + ejbClass.getLabel() + "' references non-exsisting class '" + mtrAttribute.getType()  + "'", 3);
						throw new NullPointerException("Refferenced file + " + mtrAttribute.getType() + " not found!");
					}
				}
			}
		}
	}
	
	//gets refference to ejb class from model based on name
	public EJBClass getClass(String name) {
		System.out.println("trazim klasu sa imenom " + name);
		EJBClass clas = null;
		for(int i=0; i<classes.size(); i++) {
			EJBClass cl = classes.get(i);
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
			e.printStackTrace();
		}

	}

}
