package kroki.app.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.w3c.dom.Attr;

import kroki.app.KrokiMockupToolApp;
import kroki.app.generators.DatabaseConfigGenerator;
import kroki.app.generators.EJBGenerator;
import kroki.app.generators.MenuGenerator;
import kroki.app.generators.WebResourceGenerator;
import kroki.app.generators.utils.Attribute;
import kroki.app.generators.utils.EJBClass;
import kroki.app.generators.utils.ManyToOneAttribute;
import kroki.app.generators.utils.Menu;
import kroki.app.generators.utils.OneToManyAttribute;
import kroki.app.generators.utils.Submenu;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Zoom;
import kroki.profil.operation.BussinessOperation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.std.StdPanelSettings;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;

public class WebExporter {

	private BussinesSubsystem project;
	private ArrayList<VisibleElement> elements;
	private ArrayList<EJBClass> classes;
	//list of menus to be generated
	private ArrayList<Menu> menus;
	WebResourceGenerator webGenerator;
	EJBGenerator ejbGenerator;
	DatabaseConfigGenerator dbConfigGen;
	MenuGenerator menuGenerator;

	public WebExporter() {
		elements = new ArrayList<VisibleElement>();
		classes = new ArrayList<EJBClass>();
		menus = new ArrayList<Menu>();
		webGenerator = new WebResourceGenerator();
		ejbGenerator = new EJBGenerator();
		menuGenerator = new MenuGenerator();
	}

	public void export(File file, BussinesSubsystem proj) {
		this.project = proj;
		dbConfigGen = new DatabaseConfigGenerator(proj.getDBConnectionProps());

		for(int i=0; i<proj.ownedElementCount(); i++) {
			VisibleElement el = proj.getOwnedElementAt(i);
			if(el instanceof BussinesSubsystem) {
				getSubSystemData(el, null);
				getSubSystemClasses(el);
			}else if(el instanceof VisibleClass) {
				getClassData(el, "", null);
				elements.add(el);
			}
		}

		//Add infrastructural classes (for user rights management)
		addDefaultClasses(classes);

		//Add one-to-many attributes to classes
		addReferences();
		
		//CONFIGURATION FILES GENERATION
		webGenerator.generate(elements);
		ejbGenerator.generateEJBClasses(classes, false);
		dbConfigGen.generatePersistenceXMl(true);
		menuGenerator.generateWEBMenu(menus);

		//write project label as mainframe title in main.properties file
		writeProjectName(proj.getLabel());
	}

	//fetches class (panel) data from the model
	public void getClassData(VisibleElement el, String classPackage,  Menu menu) {
		NamingUtil cc = new NamingUtil();

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
				String values = null;
				if(vp.getComponentType() == ComponentType.TEXT_FIELD) {
					if(vp.getDataType().equals("BigDecimal")) {
						type = "java.math.BigDecimal";
					}else if(vp.getDataType().equals("Date")) {
						type = "java.util.Date";
					}
				}else if(vp.getComponentType() == ComponentType.CHECK_BOX) {
					type =  "java.lang.Boolean";
				} 


				Attribute attr = new Attribute(cc.toCamelCase(vp.getLabel(), true), vp.getColumnLabel(), vp.getLabel(), type, false, true, vp.isRepresentative(), null);
				attr.setRepresentative(vp.isRepresentative());
				attr.setMandatory(vp.lower() != 0);
				attributes.add(attr);
			}

			for(int l=0; l<vc.containedZooms().size(); l++) {
				Zoom z = vc.containedZooms().get(l);
				if(z.getTargetPanel() != null) {
				//adding ManyToOne (zoom) attribute
				String n = z.getLabel().substring(0, 1).toLowerCase() + z.getLabel().substring(1);
				//String reffColumn = "id";
				String type = cc.toCamelCase(z.getTargetPanel().getComponent().getName(), false);

				ManyToOneAttribute mto = new ManyToOneAttribute(cc.toCamelCase(z.getTargetPanel().getComponent().getName(), true), n, z.getLabel(), type, true);
				mto.setMandatory(z.lower() != 0);
				mtoAttributes.add(mto);
				}else {
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Target panel not set for combozoom '" + z.getLabel() + "' in '" + vc.getLabel() + "'. Skipping that file.", 2);
					return;
				}
			}

			String tableName = cc.toDatabaseFormat(this.project.getLabel(), sp.getLabel());
			//EJB class instance for panel is created and passed to generator
			EJBClass ejb = new EJBClass("adapt.entities", sp.getPersistentClass().name(), tableName, sp.getLabel(), attributes, mtoAttributes, otmAttributes);
			classes.add(ejb);
			

			/**************************************/
			/*        SUBMENU GENERATION DATA     */
			/*        one menu item per panel     */
			/**************************************/
			String activate = "/resources/" + ejb.getName();
			String label = ejb.getLabel();
			Submenu sub = new Submenu(activate, label, "");
			//if it is in a subsystem, it is aded as submenu item
			if(menu != null) {
				menu.addSubmenu(sub);
			}else {
				//if panel is in root of workspace, it gets it's item in main menu
				Menu men = new Menu("menu" + activate, label, new ArrayList<Submenu>(), new ArrayList<Menu>());
				men.addSubmenu(sub);
				menus.add(men);
			}
		}
	}

	//FETCHING SUBSYSTEM DATA USED FOR FILES GENERATION
	public void getSubSystemData(VisibleElement el, Menu mmenu) {
		
		String label = el.name().replace("_", " ");
		Menu menu = new Menu("", label, new ArrayList<Submenu>(), new ArrayList<Menu>());
		BussinesSubsystem bs = (BussinesSubsystem) el;
		
		for(int m=0; m<bs.ownedElementCount(); m++) {
			VisibleElement e = bs.getOwnedElementAt(m);
			if(e instanceof VisibleClass) {
				getClassData(e, el.name(), menu);
			}else if (e instanceof BussinesSubsystem) {
				getSubSystemData(e, menu);
			}
		}
		
		if(mmenu != null) {
			mmenu.addMenu(menu);
		}else {
			menus.add(menu);
		}

	}

	//puts all classes from given package into elements list
	public void getSubSystemClasses(VisibleElement el) {
		BussinesSubsystem bs = (BussinesSubsystem) el;
		for(int m=0; m<bs.ownedElementCount(); m++) {
			VisibleElement e = bs.getOwnedElementAt(m);
			if(e instanceof VisibleClass) {
				elements.add(e);
			}else if (e instanceof BussinesSubsystem) {
				getSubSystemClasses(e);
			}
		}
	}

	//gets refference to ejb class from model based on name
	public EJBClass getClass(String name) {
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
		File propertiesFile = new File(appPath.substring(0, appPath.length()-16) + "WebApp" + File.separator + "props" + File.separator + "app.properties");

		//read app.properties file
		//and append first line which contains main form title
		Scanner scan;
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("app.title = " + name);
		try {
			scan = new Scanner(propertiesFile);
			while(scan.hasNext()){
				String line = scan.nextLine();
				if(!line.startsWith("app.title")) {
					lines.add(line);
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("[ERROR] app.properties file not found");
		}

		//write app.properties file with main form title
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

	public void addDefaultClasses(ArrayList<EJBClass> classes) {
		//ACTION
		Attribute actionName = new Attribute("name", "ACT_NAME", "Name", "java.lang.String", true, true, true, null);
		Attribute actionLink = new Attribute("link", "ACT_LINK", "Link", "java.lang.String", false, true, false, null);
		Attribute actionImagePath = new Attribute("imagePath", "ACT_IMG_PATH", "Image path", "java.lang.String", false, false, false, null);
		Attribute actonType = new Attribute("type", "ACT_TYPE", "Type", "java.lang.String", false, true, false, null);
		Attribute actionTip = new Attribute("tip", "ACT_TIP", "Tip", "java.lang.String", false, false, false, null);

		ArrayList<Attribute> actionAttributes = new ArrayList<Attribute>();
		actionAttributes.add(actionName);
		actionAttributes.add(actionLink);
		actionAttributes.add(actionImagePath);
		actionAttributes.add(actonType);
		actionAttributes.add(actionTip);

		EJBClass action = new EJBClass("adapt.entities", "Action", "ADAPT_ACTION", "Action", actionAttributes, new ArrayList<ManyToOneAttribute>(), new ArrayList<OneToManyAttribute>());

		//USER
		Attribute userName = new Attribute("name", "USER_USERNAME", "Username", "java.lang.String", true, true, true, null);
		Attribute userPassword = new Attribute("password", "USER_PASSWORD", "Password", "java.lang.String", false, true, false, null);
		OneToManyAttribute userRights = new OneToManyAttribute("rights", "Rights", "UserRights", "user");

		ArrayList<Attribute> userAttributes = new ArrayList<Attribute>();
		userAttributes.add(userName);
		userAttributes.add(userPassword);

		ArrayList<OneToManyAttribute> userOTMAttributes = new ArrayList<OneToManyAttribute>();
		userOTMAttributes.add(userRights);

		EJBClass user = new EJBClass("adapt.entities", "User", "ADAPT_USER", "User", userAttributes, new ArrayList<ManyToOneAttribute>(), userOTMAttributes);

		//MYRESOURCE
		Attribute myResourceEntId = new Attribute("entId", "MYRES_ENT_ID", "Entity ID", "java.lang.Long", false, true, false, null);
		Attribute myResourceTable = new Attribute("table", "MYRES_TABLE", "Table", "java.lang.String", false, true, false, null);
		Attribute myResourceEntLabel = new Attribute("entLabel", "MYRES_ENT_LABEL", "Entity label", "java.lang.String", false, true, false, null);
		Attribute myResourceTableLabel = new Attribute("tableLabel", "MYRES_TABLE_LABEL", "Table", "java.lang.String", false, true, false, null);
		Attribute myResourceResLink = new Attribute("ResLink", "MYRES_RESLINK", "Resource link", "java.lang.String", false, true, false, null);
		ManyToOneAttribute myResourceUser = new ManyToOneAttribute("user", "MYRES_USER", "User", "User", true);
		
		ArrayList<Attribute> myResourceAttributes = new ArrayList<Attribute>();
		myResourceAttributes.add(myResourceEntId);
		myResourceAttributes.add(myResourceTable);
		myResourceAttributes.add(myResourceTableLabel);
		myResourceAttributes.add(myResourceEntLabel);
		myResourceAttributes.add(myResourceResLink);

		ArrayList<ManyToOneAttribute> myResourceMTOAttributes = new ArrayList<ManyToOneAttribute>();
		myResourceMTOAttributes.add(myResourceUser);
		
		EJBClass myResource = new EJBClass("adapt.entities", "MyResource", "ADAPT_MY_RESOURCE", "My Resources", myResourceAttributes, myResourceMTOAttributes, new ArrayList<OneToManyAttribute>());
		
		//RESOURCE
		Attribute resourceName = new Attribute("name", "RES_NAME", "Name", "java.lang.String", true, true, true, null);
		Attribute resourceLink = new Attribute("link", "RES_LINK", "Link", "java.lang.String", true, true, false, null);
		
		ArrayList<Attribute> resourceAttributes = new ArrayList<Attribute>();
		resourceAttributes.add(resourceName);
		resourceAttributes.add(resourceLink);
		
		EJBClass resource = new EJBClass("adapt.entities", "Resource", "ADAPT_RESOURCE", "Resources", resourceAttributes, new ArrayList<ManyToOneAttribute>(), new ArrayList<OneToManyAttribute>());
		
		//USERRIGHTS
		Attribute urightsAllowed = new Attribute("allowed", "UR_ALLOWED", "Allowed", "java.lang.Boolean", false, true, true, null);
		ManyToOneAttribute urightsUser = new ManyToOneAttribute("user", "UR_USER", "User", "User", true);
		ManyToOneAttribute urightsAction = new ManyToOneAttribute("action", "UR_ACTION", "Action", "Action", true);
		ManyToOneAttribute urightsResource = new ManyToOneAttribute("resource", "UR_RESOURCE", "Resource", "Resource", true);
		
		ArrayList<Attribute> urightsAttributes = new ArrayList<Attribute>();
		urightsAttributes.add(urightsAllowed);
		
		ArrayList<ManyToOneAttribute> urightsMTOAttributes = new ArrayList<ManyToOneAttribute>();
		urightsMTOAttributes.add(urightsUser);
		urightsMTOAttributes.add(urightsAction);
		urightsMTOAttributes.add(urightsResource);
		
		EJBClass uRights = new EJBClass("adapt.entities", "UserRights", "ADAPT_USER_RIGHTS", "User rights", urightsAttributes, urightsMTOAttributes, new ArrayList<OneToManyAttribute>());
	
		classes.add(action);
		classes.add(user);
		classes.add(myResource);
		classes.add(resource);
		classes.add(uRights);
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
	
}
