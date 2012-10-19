package kroki.app.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
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
 * Akcija za export konfiguracionih xml datoteka potrebnih za genericku swing aplikaciju
 */
public class ExportSwingAction extends AbstractAction {

	//lista na osnovu koje se generisu ejb klase
	ArrayList<EJBClass> classes;
	//lista na osnovu koje se generise xml datoteka
	//za konfigurisanje menija
	ArrayList<Menu> menus;
	//lista na osnovu koje se generise xml datoteka za panele
	ArrayList<VisibleElement> elements;
	//TypeComponentMapper tcm = new TypeComponentMapper();
	
	public ExportSwingAction() {
		putValue(NAME, "Desktop Application");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//nadjem selektovani projekat iz workspace-a
		BussinesSubsystem proj = null;
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
        
        //ITERACIJA KROZ ELEMENTE SELEKTOVANOG PROJEKTA
        if(proj != null) {
        	for(int i=0; i<proj.ownedElementCount(); i++) {
        		VisibleElement el = proj.getOwnedElementAt(i);
        		if(el instanceof BussinesSubsystem) {
        			getSubSystemData(el, i);
        		}else if(el instanceof VisibleClass) {
        			getClassData(el, "", null);
        		}
        	}
        	
            //GENERISANJE DATOTEKA
            menuGenerator.generate(menus);
            panelGenerator.generate(elements);
            ejbGenerator.generateEJBXmlFiles(classes);
            ejbGenerator.generateEJBClasses(classes, true);
            ejbGenerator.generateXMLMappingFile(classes);
            //pokreni pejinu aplikaciju
            MainApp mapp = new MainApp();
            //mapp.main(null);

        	
        }else {
        	//ako nista nije selektovano, prikazem poruku
        	JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "Morate selektovati projekat!");
        }
        
	}

	//KUPI PODATKE KLASA (PANELA) IZ MODELA NA OSNOVU KOJIH SE VRSI GENERISANJE DATOTEKA
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
			
			/*****************************************/
			/*    PODACI ZA GENERISANJE EJB KLASA    */
			/*za svaki panel generise se jedna klasa */
			/*****************************************/
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
				//dodajem ManyToOne (zoom) atribut
				String n = z.getLabel().substring(0, 1).toLowerCase() + z.getLabel().substring(1);
				String reffColumn = "id";
				String type = cc.toCamelCase(z.getTargetPanel().getComponent().getName(), false);
				
				ManyToOneAttribute mto = new ManyToOneAttribute(cc.toCamelCase(z.getTargetPanel().getComponent().getName(), true), n, z.getLabel(), type, true);
				mtoAttributes.add(mto);
				
				//u suprotni kraj asocijacije dodam OneToMany atribut
				if(zcl != null) {
					String name = sp.getPersistentClass().name().substring(0, 1).toLowerCase() + sp.getPersistentClass().name().substring(1) + "Set";
					String label = z.getLabel();
					String reffTable = sp.getPersistentClass().name();
					String mappedBy = cc.toCamelCase(z.getLabel(), true);
					
					//pokupim sve representative atribute kako bi bili prikazani u zoom polju
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
			
			//ZA SVAKI PANEL U MODELU GENERISE SE JEDNA EJB KLASA I PROSLEDJUJE GENERATORU
			EJBClass ejb = new EJBClass("ejb", sp.getPersistentClass().name(), sp.getLabel(), attributes, mtoAttributes, otmAttributes);
			classes.add(ejb);
			
			
			/*****************************************/
			/*    PODACI ZA GENERISANJE PODMENIJA    */
			/*   za svaki panel generise se stavka   */
			/*****************************************/
			//SVAKI PANEL DOBIJE SVOJU STAVKU U MENIJU
			String activate = ejb.getName().toLowerCase() + "_st";
			String label = ejb.getLabel();
			String panel_type = "standard-panel";
			Submenu sub = new Submenu(activate, label, panel_type);
			//ako je u podsistemu, dodaje se u njegove stavke
			if(menu != null) {
				menu.add(sub);
			}else {
				//ako je u rootu dobija svoj meni
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
			//ako je u podsistemu, dodaje se u njegove stavke
			if(menu != null) {
				menu.add(sub);
			}else {
				//ako je u rootu dobija svoj meni
				Menu men = new Menu("menu" + activate, label, new ArrayList<Submenu>());
				men.add(sub);
				menus.add(men);
			}
		}
		elements.add(el);
	}
	
	
	//KUPI PODATKE PODSISTEMA IZ MODELA NA OSNOVU KOJIH SE VRSI GENERISANJE
	public void getSubSystemData(VisibleElement el, int index) {
		/*****************************************/
		/*    PODACI ZA GENERISANJE MENIJA       */
		/*  za svaki subsystem generise se meni  */
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

	//na osnovu imena vraca referencu na ejb klasu iz modela
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
	
}
