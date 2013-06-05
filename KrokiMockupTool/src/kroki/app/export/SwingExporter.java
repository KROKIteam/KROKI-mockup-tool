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
import kroki.app.generators.EnumerationGenerator;
import kroki.app.generators.MenuGenerator;
import kroki.app.generators.PanelGenerator;
import kroki.app.generators.utils.EJBAttribute;
import kroki.app.generators.utils.EJBClass;
import kroki.app.generators.utils.Enumeration;
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
	//list of standard forms to be generated
	private ArrayList<VisibleElement> elements;
	//list of enumerations to be generated
	private ArrayList<Enumeration> enumerations;
	//configuration files generators
	private EJBGenerator ejbGenerator;
	private MenuGenerator menuGenerator;
	private PanelGenerator panelGenerator;
	private DatabaseConfigGenerator dbConfigGenerator;
	private EnumerationGenerator enumGenerator;

	public SwingExporter() {
		classes = new ArrayList<EJBClass>();
		menus = new ArrayList<Menu>();
		elements = new ArrayList<VisibleElement>();
		enumerations = new ArrayList<Enumeration>();
		ejbGenerator = new EJBGenerator();
		menuGenerator = new MenuGenerator();
		panelGenerator = new PanelGenerator();
		enumGenerator = new EnumerationGenerator(true);
	}

	/**
	 * Project is exported as runnable jar file
	 * on given location
	 * @param file location to which project needs to be exported
	 * @param proj project that is exported
	 * @message message that is dispayed on finish
	 */
	public void export(File file, BussinesSubsystem proj, String message) {
		getData(proj);

		//CONFIGURATION FILES GENERATION
		menuGenerator.generateSWINGMenu(menus);
		panelGenerator.generate(elements);
		ejbGenerator.generateEJBXmlFiles(classes);
		ejbGenerator.generateEJBClasses(classes, true);
		ejbGenerator.generateXMLMappingFile(classes);
		dbConfigGenerator.generateFilesForDesktopApp();
		enumGenerator.generateXMLFiles(enumerations);
		enumGenerator.generateEnumFiles(enumerations);

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

	public void getData(BussinesSubsystem proj) {
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

		//add default data
		addDefaultData(proj);
		//Add one-to-many attributes to classes
		addReferences();
	}

	//fetches class (panel) data from the model
	public void getClassData(VisibleElement el, String classPackage, Menu menu) {
		NamingUtil cc = new NamingUtil();

		if(el instanceof StandardPanel) {
			StandardPanel sp = (StandardPanel)el;
			VisibleClass vc = (VisibleClass)el;

			//EJB CLASS ATTRIBUTE LISTS
			ArrayList<EJBAttribute> attributes = new ArrayList<EJBAttribute>();
			
			/***********************************************/
			/*    DATA USED FOR EJB CLASS GENERATION       */
			/* one ejb class is generated for every panel  */
			/***********************************************/
			for (VisibleElement element : sp.getVisibleElementList()) {
				if(element instanceof VisibleProperty) {
					VisibleProperty vp = (VisibleProperty) element;
					
					String type = "java.lang.String";
					Enumeration enumeration = null;
					if(vp.getComponentType() == ComponentType.TEXT_FIELD) {
						if(vp.getDataType().equals("BigDecimal")) {
							type = "java.math.BigDecimal";
						}else if(vp.getDataType().equals("Date")) {
							type = "java.util.Date";
						}
					}else if(vp.getComponentType() == ComponentType.CHECK_BOX) {
						type =  "java.lang.Boolean";
					}else if (vp.getComponentType() == ComponentType.COMBO_BOX) {
						/***********************************************/
						/*    DATA USED FOR ENUMERATION GENERATION     */
						/* one enum is generated for every combo-box   */
						/***********************************************/
						String enumName = cc.toCamelCase(vp.getLabel(), false);
						enumName += cc.toCamelCase(vp.umlClass().name(), false) + "Enum";
						String enumClass = vp.umlClass().name();
						String enumProp = cc.toCamelCase(vp.getLabel(), true);
						String[] enumValues = vp.getEnumeration().split(";");
						enumeration = new Enumeration(enumName, vp.getLabel(), enumClass, enumProp, enumValues);
						enumerations.add(enumeration);
					}

					ArrayList<String> anotations = new ArrayList<String>();
					String name = cc.toCamelCase(vp.getLabel(), true);
					String label = vp.getLabel();
					String columnLabel = vp.getColumnLabel();

					anotations.add("@Column(name = \"" + columnLabel + "\", unique = false, nullable = false)");
					EJBAttribute attribute = new EJBAttribute(anotations, type, name, label, columnLabel, true, false, vp.isRepresentative(), enumeration);
					attributes.add(attribute);
					
				}else if(element instanceof Zoom) {
					Zoom z = (Zoom)element;
					if(z.getTargetPanel() != null) {
						String n = z.getLabel().substring(0, 1).toLowerCase() + z.getLabel().substring(1);
						String type = cc.toCamelCase(z.getTargetPanel().getComponent().getName(), false);

						ArrayList<String> anotations = new ArrayList<String>();
						String name = cc.toCamelCase(z.getTargetPanel().getComponent().getName(), true);
						String databaseName = z.getLabel().substring(0, 1).toLowerCase() + z.getLabel().substring(1);
						String label = z.getLabel();
						Boolean mandatory = z.lower() != 0;

						anotations.add("@ManyToOne");
						anotations.add("@JoinColumn(name=\"" + name + "\", referencedColumnName=\"ID\",  nullable = " + !mandatory + ")");

						EJBAttribute attribute = new EJBAttribute(anotations, type, name, label, databaseName, mandatory, false, false, null);
						attributes.add(attribute);

					}else {
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Target panel not set for combozoom '" + z.getLabel() + "' in '" + vc.getLabel() + "'. Skipping that file.", 2);
						return;
					}
				}
			}
			
			String tableName = cc.toDatabaseFormat(this.project.getLabel(), sp.getLabel());

			String sys = cc.toCamelCase(project.getLabel(), true);
			if(menu != null) {
				sys = cc.toCamelCase(menu.getLabel(), true);
			}
			//EJB class instance for panel is created and passed to generator
			EJBClass ejb = new EJBClass("ejb", sys, sp.getPersistentClass().name(), tableName, sp.getLabel(), attributes);
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
			for(int j=0; j<ejbClass.getAttributes().size(); j++) {
				EJBAttribute attribute = ejbClass.getAttributes().get(j);
				if(getAttributeType(attribute).equals("ManyToOne")) {
					EJBClass oppositeCLass = getClass(attribute.getType());
					if(oppositeCLass != null) {
						String name = ejbClass.getName() + "Set";
						String type = "Set<" + ejbClass.getName() + ">";
						String label = attribute.getLabel();
						String mappedBy = attribute.getName();
						ArrayList<String> annotations = new ArrayList<String>();
						annotations.add("@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = \"" + mappedBy + "\")");

						EJBAttribute attr = new EJBAttribute(annotations, type, name, label, name, true, false, false, null);
						oppositeCLass.getAttributes().add(attr);
						
						for(int k=0; k<oppositeCLass.getAttributes().size(); k++) {
							EJBAttribute att = oppositeCLass.getAttributes().get(k);
							if(att.getRepresentative()) {
								attribute.getColumnRefs().add(att);
							}
						}

					}else {
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Class '" + ejbClass.getLabel() + "' references non-exsisting class '" + attribute.getType()  + "'", 3);
						throw new NullPointerException("Refferenced file " + attribute.getType() + " not found!");
					}
				}
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
			PrintWriter pw = new PrintWriter(new FileOutputStream(propertiesFile));
			for(int i=0; i<lines.size(); i++) {
				pw.println(lines.get(i));
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Adds default data that needs to be generated in order to make application run correctly
	 * @param proj
	 */
	public void addDefaultData(BussinesSubsystem proj) {
		NamingUtil namer = new NamingUtil();

		//Add User class to classes list
		ArrayList<String> usernameAnnotations = new ArrayList<String>();
		usernameAnnotations.add("@Column(name = \"username\", unique = false, nullable = false)");
		EJBAttribute usernameAttribute = new EJBAttribute(usernameAnnotations, "java.lang.String", "username", "User name", "username", true, false, true, null);

		ArrayList<String> passwordAnnotations = new ArrayList<String>();
		passwordAnnotations.add("@Column(name = \"password\", unique = false, nullable = false)");
		EJBAttribute passwordAttribute = new EJBAttribute(passwordAnnotations, "java.lang.String", "password", "Password", "password", true, false, false, null);

		String userTableName = namer.toDatabaseFormat(proj.getLabel(), "User");
		ArrayList<EJBAttribute> userAttributes = new ArrayList<EJBAttribute>();
		userAttributes.add(usernameAttribute);
		userAttributes.add(passwordAttribute);

		EJBClass user = new EJBClass("ejb", "default", "User", userTableName, "User", userAttributes);
		classes.add(user);

		//add default enumerations
		//OpenedAs
		Enumeration openedAsEnum = new Enumeration("OpenedAs", "Opened as", null, null, new String[]{"DEFAULT", "ZOOM", "NEXT"});
		//OperationType
		Enumeration operationTypeEnum = new Enumeration("OperationType", "Operation type", null, null, new String[]{"BussinesTransaction", "ViewReport", "JavaOperation"});
		//PanelType
		Enumeration panelTypeEnum = new Enumeration("PanelType", "Panel type", null, null, new String[]{"StandardPanel", "ParentChildPanel", "ManyToManyPanel"});
		//StateMode
		Enumeration stateModeEnum = new Enumeration("StateMode", "State mode", null, null, new String[]{"UPDATE", "ADD", "SEARCH"});
		//ViewMode
		Enumeration viewModeEnum = new Enumeration("ViewMode", "View mode", null, null, new String[]{"TABLEVIEW", "INPUTPANELVIEW"});

		enumerations.add(openedAsEnum);
		enumerations.add(operationTypeEnum);
		enumerations.add(panelTypeEnum);
		enumerations.add(stateModeEnum);
		enumerations.add(viewModeEnum);
	}

	/**
	 * Determines attribute type based on it's annotation
	 * @param attribute
	 * @return Column, OneToMany or ManyToOne
	 */
	public String getAttributeType(EJBAttribute attribute) {
		String annotation = attribute.getAnnotations().get(0);
		if(annotation.startsWith("@Column")) {
			return "Column";
		}else if (annotation.startsWith("@ManyToOne")) {
			return "ManyToOne";
		}else {
			return "OneToMany";
		}

	}

	public ArrayList<EJBClass> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<EJBClass> classes) {
		this.classes = classes;
	}

	public ArrayList<Menu> getMenus() {
		return menus;
	}

	public void setMenus(ArrayList<Menu> menus) {
		this.menus = menus;
	}

	public ArrayList<VisibleElement> getElements() {
		return elements;
	}

	public void setElements(ArrayList<VisibleElement> elements) {
		this.elements = elements;
	}

}
