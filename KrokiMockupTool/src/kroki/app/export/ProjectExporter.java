package kroki.app.export;

import java.awt.Cursor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import kroki.app.KrokiMockupToolApp;
import kroki.app.analyzer.ConstraintAnalyzer;
import kroki.app.exceptions.NoZoomPanelException;
import kroki.app.generators.AdministrationSubsystemGenerator;
import kroki.app.generators.ApplicationRepositoryGenerator;
import kroki.app.generators.ConstraintGenerator;
import kroki.app.generators.DatabaseConfigGenerator;
import kroki.app.generators.EJBGenerator;
import kroki.app.generators.EnumerationGenerator;
import kroki.app.generators.MenuGenerator;
import kroki.app.generators.PanelGenerator;
import kroki.app.generators.WebResourceGenerator;
import kroki.app.generators.utils.Constraint;
import kroki.app.generators.utils.EJBAttribute;
import kroki.app.generators.utils.EJBClass;
import kroki.app.generators.utils.Enumeration;
import kroki.app.generators.utils.Menu;
import kroki.app.generators.utils.Submenu;
import kroki.app.gui.console.OutputPanel;
import kroki.app.menu.MenuItem;
import kroki.app.utils.RunAnt;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Zoom;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.property.Calculated;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;
import tudresden.ocl20.pivot.language.ocl.resource.ocl.Ocl22Parser;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.standalone.facade.StandaloneFacade;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;

/**
 * Class that provides methods for exporting KROKI projects as working Java
 * applications
 * 
 * @author KROKI Team
 *
 */
public class ProjectExporter {

	// true = swing export
	// false = web export
	private boolean swing;

	// project that is exported
	private BussinesSubsystem project;
	// list of EJB classes to be generated
	private ArrayList<EJBClass> classes;
	// list of menus to be generated
	private ArrayList<Menu> menus;
	// list of standard forms to be generated
	private ArrayList<VisibleElement> elements;
	// list of enumerations to be generated
	private ArrayList<Enumeration> enumerations;
	// configuration files generators
	private EJBGenerator ejbGenerator;
	private MenuGenerator menuGenerator;
	private PanelGenerator panelGenerator;
	private DatabaseConfigGenerator dbConfigGenerator;
	private EnumerationGenerator enumGenerator;
	private WebResourceGenerator webGenerator;
	private AdministrationSubsystemGenerator adminGenerator;
	private ApplicationRepositoryGenerator appRepoGenerator;
	private NamingUtil cc;
	private kroki.app.menu.Submenu rootMenu;
	private ConstraintGenerator constraintGenerator;
	private ArrayList<Constraint> constraints;
	private IModel model = null;

	public ProjectExporter(boolean swing) {
		classes = new ArrayList<EJBClass>();
		menus = new ArrayList<Menu>();
		elements = new ArrayList<VisibleElement>();
		enumerations = new ArrayList<Enumeration>();
		ejbGenerator = new EJBGenerator();
		menuGenerator = new MenuGenerator();
		panelGenerator = new PanelGenerator();
		enumGenerator = new EnumerationGenerator(swing);
		webGenerator = new WebResourceGenerator();
		adminGenerator = new AdministrationSubsystemGenerator();
		appRepoGenerator = new ApplicationRepositoryGenerator();
		rootMenu = new kroki.app.menu.Submenu("Menu");
		constraintGenerator = new ConstraintGenerator();
		cc = new NamingUtil();
		this.swing = swing;
	}

	/**
	 * Generates code and applicaion repository from the Kroki project and
	 * builds the exported application All other exprot methods are called from
	 * this point
	 * 
	 * @param file
	 *            export path
	 * @param proj
	 *            KROKI mockup project that needs to be exported
	 * @param message
	 */
	public void export(File file, String jarName, BussinesSubsystem proj, String message) {
		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 1);
		if (!KrokiMockupToolApp.getInstance().isBinaryRun()) {
			appPath = appPath.substring(0, appPath.length() - 16);
		}
		// File fileModel = new File(appPath +
		// "KrokiMockupTool/Temp/model1.uml");/********* putanja do modela
		// *****/

		// Kada proradi automatsko generisanje UML modela, ovo gore moze da se
		// zameni sa ovom linijom:
		File fileModel = new File(file.getAbsolutePath() + File.separator + jarName + ".uml");
		StandaloneFacade facade = StandaloneFacade.INSTANCE;
		try {
			facade.initialize(null);
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		try {
			// model = (EcoreModel) facade.loadEcoreModel(fileModel);
			model = facade.loadUMLModel(fileModel, getUMLResources());
		} catch (ModelAccessException e1) {
			e1.printStackTrace();
		}
		generateAppAndRepo(proj, message);
		writeProjectName(proj.getLabel(), proj.getProjectDescription());
		runAnt(file, proj, jarName, message);
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame()
				.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private File getUMLResources() {
		return new File("lib/org.eclipse.uml2.uml.resources_3.1.0.v201005031530.jar");
	}

	/**
	 * Gnerates application classes and repository without running the build
	 * script
	 */
	public void generateAppAndRepo(BussinesSubsystem proj, String message) {
		this.project = proj;
		// collecting the data from KROKI project
		getData(proj);
		// configuration files generation from collected data
		// separate generator classes are called for swing and web application
		if (swing) {
			menuGenerator.generateSWINGMenu(menus);
			panelGenerator.generate(elements, null);
			ejbGenerator.generateEJBXmlFiles(classes, null);
			ejbGenerator.generateEJBClasses(classes, true);
			ejbGenerator.generateXMLMappingFile(classes, null);
			constraintGenerator.generateConstraints(classes, true);
			dbConfigGenerator.generateFilesForDesktopApp();
			enumGenerator.generateXMLFiles(enumerations);
			enumGenerator.generateEnumFiles(enumerations);
		} else {
			appRepoGenerator.generate(classes, menus, elements, enumerations, rootMenu);
			// MainFrame.getInstance(); // If admin subsystem isn't started
			// adminGenerator.generate();
		}
	}

	/**
	 * Fetches project data into lists that are used by generators
	 * 
	 * @param proj
	 *            KROKI mockup project that is being exported
	 */
	public void getData(BussinesSubsystem proj) {
		dbConfigGenerator = new DatabaseConfigGenerator(proj.getDBConnectionProps());
		this.project = proj;

		// iteration trough project elements and retrieving usable data for
		// generators
		for (int i = 0; i < proj.ownedElementCount(); i++) {
			VisibleElement el = proj.getOwnedElementAt(i);
			if (el instanceof BussinesSubsystem) {
				getSubSystemData(el, i, null, null);
			} else if (el instanceof VisibleClass) {
				try {
					getClassData(el, "", null, null);
				} catch (NoZoomPanelException e) {
					KrokiMockupToolApp.getInstance().displayTextOutput(e.getMessage(), 2);
				}
			}
		}

		// Add one-to-many attributes to classes
		addReferences();
		// add default data
		if (swing) {
			addDefaultSwingData(proj);
		}
	}

	/**
	 * Fetches class (panel) data from the model
	 * 
	 * @param el
	 *            visible element that represents class in UI profile
	 * @param classPackage
	 * @param menu
	 * @throws NoZoomPanelException
	 */
	public void getClassData(VisibleElement el, String classPackage, Menu menu, kroki.app.menu.Submenu subMenuNew)
			throws NoZoomPanelException {
		if (el instanceof StandardPanel) {
			try {
				getStandardPanelData(el, classPackage, menu, subMenuNew);
			} catch (NoZoomPanelException e) {
				throw new NoZoomPanelException(e.getMessage());
			}
		} else if (el instanceof ParentChild) {
			getParentChildData(el, menu, subMenuNew);
		}
		// after data fetching is done, put current element in elements list
		elements.add(el);
	}

	/**
	 * Gets data from KROKI standard panels. This method iterates trough panel
	 * elements and collects data for EJB bean and menu generators.
	 * 
	 * @param el
	 *            visible element representing standard panel in UI profile
	 * @param classPackage
	 * @param menu
	 * @param subMenuNew
	 * @throws NoZoomPanelException
	 */
	public void getStandardPanelData(VisibleElement el, String classPackage, Menu menu,
			kroki.app.menu.Submenu subMenuNew) throws NoZoomPanelException {
		StandardPanel sp = (StandardPanel) el;
		VisibleClass vc = (VisibleClass) el;

		// EJB CLASS ATTRIBUTE LISTS
		ArrayList<EJBAttribute> attributes = new ArrayList<EJBAttribute>();

		// CONSTRAINTS
		ArrayList<Constraint> constraints = new ArrayList<Constraint>();
		List<tudresden.ocl20.pivot.pivotmodel.Constraint> consDres = new ArrayList<tudresden.ocl20.pivot.pivotmodel.Constraint>();
		List<String> importedPackages = new ArrayList<String>();
		importedPackages.add("import adapt.exceptions.InvariantException;");

		// DATA USED FOR EJB CLASS GENERATION
		// for each panel element, one EJB attribute object is created and added
		// to attributes list for that panel
		for (VisibleElement element : sp.getVisibleElementList()) {
			if (element instanceof VisibleProperty) {
				VisibleProperty vp = (VisibleProperty) element;
				EJBAttribute attribute = getVisiblePropertyData(vp);
				attributes.add(attribute);
				if (element instanceof Calculated) {
					String expression = ((Calculated) element).getExpression();
					try {
						consDres = Ocl22Parser.INSTANCE.parseOclString(expression, model);
					} catch (tudresden.ocl20.pivot.parser.ParseException e) {
						e.printStackTrace();
					}
				}
			} else if (element instanceof Zoom) {
				Zoom z = (Zoom) element;
				EJBAttribute attribute = getZoomData(z, sp.getPersistentClass().name());
				if (attribute != null) {
					attributes.add(attribute);
				} else {
					KrokiMockupToolApp.getInstance().displayTextOutput("Target panel not set for combozoom '"
							+ z.getLabel() + "' in '" + vc.getLabel() + "'. Skipping that file.", 3);
					throw new NoZoomPanelException("Target panel not set for combozoom '" + z.getLabel() + "' in '"
							+ vc.getLabel() + "'. Skipping that file.");
				}
			}
		}

		String tableName = cc.toDatabaseFormat(this.project.getLabel(), sp.getLabel());

		String sys = cc.toCamelCase(project.getLabel(), true);
		if (menu != null) {
			sys = cc.toCamelCase(menu.getLabel(), true);
		}

		if (subMenuNew != null) {
			sys = cc.toCamelCase(subMenuNew.getName(), true);
		}
		ConstraintAnalyzer ca = null;
		if (consDres != null && !consDres.isEmpty()) {
			ca = new ConstraintAnalyzer(consDres.get(0), importedPackages);
			constraints.add(ca.process());
		}

		// EJB class instance for panel is created and passed to generator
		String pack = "ejb";
		if (!swing) {
			pack = "ejb_generated";
		}
		EJBClass ejb = new EJBClass(pack, sys, sp.getPersistentClass().name(), tableName, sp.getLabel(), attributes,
				constraints);
		ejb.setImportedPackages(importedPackages);
		System.out.println("DODAJEM KLASU: " + ejb.getName());
		classes.add(ejb);

		// SUBMENU GENERATION DATA
		// each panel get it's own menu item
		String activate = ejb.getName().toLowerCase() + "_st";
		String label = ejb.getLabel();
		String panel_type = "standard-panel";
		/*
		 * if(!swing) { activate = "/resources/" + ejb.getName(); }
		 */
		Submenu sub = new Submenu(activate, label, panel_type);
		// if it is in a subsystem, it is added as sub-menu item

		/****************************** NEW PART ******************************/
		kroki.app.generators.utils.Submenu subOld = new kroki.app.generators.utils.Submenu(activate, label, panel_type);
		MenuItem menuItem = new MenuItem();
		menuItem.setActivate(activate);
		menuItem.setFormName(label);
		menuItem.setMenuName(label);
		menuItem.setPanelType(panel_type);
		/********************************************************************/
		if (menu != null) {
			menu.addSubmenu(sub);

			// New
			subMenuNew.getChildren().add(menuItem);
			menuItem.setParent(subMenuNew);
		} else {
			// if panel is in root of workspace, it gets it's item in main menu
			Menu men = new Menu("menu" + activate, label, new ArrayList<Submenu>(), new ArrayList<Menu>());
			men.addSubmenu(sub);
			menus.add(men);

			kroki.app.menu.Submenu subMenuTemp = new kroki.app.menu.Submenu(label);
			subMenuTemp.getChildren().add(menuItem);
			menuItem.setParent(subMenuTemp);
			rootMenu.getChildren().add(subMenuTemp);
			subMenuTemp.setParent(rootMenu);
		}
	}

	/**
	 * Method used to collect the data from parent-child panels
	 * 
	 * @param el
	 * @param menu
	 * @param subMenuNew
	 */
	public void getParentChildData(VisibleElement el, Menu menu, kroki.app.menu.Submenu subMenuNew) {
		ParentChild pcPanel = (ParentChild) el;
		String activate = cc.toCamelCase(pcPanel.name(), false) + "_pc";
		String label = pcPanel.getLabel();
		String panel_type = "parent-child";
		/*
		 * if(!swing) { activate = "/resources/" +
		 * cc.toCamelCase(pcPanel.name(), false);
		 * 
		 * //add list to contained panels enclosed in square brackets panel_type
		 * += "["; for(Hierarchy hierarchy: pcPanel.containedHierarchies()) {
		 * panel_type +=
		 * cc.toCamelCase(hierarchy.getTargetPanel().getComponent().getName(),
		 * false) + ":"; } panel_type = panel_type.substring(0,
		 * panel_type.length()-1) + "]"; }
		 */
		Submenu sub = new Submenu(activate, label, panel_type);
		/****************************** NEW PART ******************************/
		MenuItem menuItem = new MenuItem();
		menuItem.setActivate(activate);
		menuItem.setFormName(label);
		menuItem.setMenuName(label);
		menuItem.setPanelType(panel_type);
		/********************************************************************/

		if (menu != null) {
			menu.addSubmenu(sub);

			// New
			subMenuNew.getChildren().add(menuItem);
			menuItem.setParent(subMenuNew);
		} else {
			Menu men = new Menu("menu" + activate, label, new ArrayList<Submenu>(), new ArrayList<Menu>());
			men.addSubmenu(sub);
			menus.add(men);

			kroki.app.menu.Submenu subMenuTemp = new kroki.app.menu.Submenu(label);
			subMenuTemp.getChildren().add(menuItem);
			menuItem.setParent(subMenuTemp);
			rootMenu.getChildren().add(subMenuTemp);
			subMenuTemp.setParent(rootMenu);
		}
	}

	/**
	 * Gets data form VisibleProperty and constructs corresponding EJBAttribute
	 * object
	 * 
	 * @param vp
	 * @return
	 */
	public EJBAttribute getVisiblePropertyData(VisibleProperty vp) {
		String type = "java.lang.String";
		Enumeration enumeration = null;
		if (vp.getComponentType() == ComponentType.TEXT_FIELD) {
			if (vp.getDataType().equals("Integer")) {
				type = "java.lang.Integer";
			} else if (vp.getDataType().equals("BigDecimal")) {
				type = "java.math.BigDecimal";
			} else if (vp.getDataType().equals("Date")) {
				type = "java.util.Date";
			}
		} else if (vp.getComponentType() == ComponentType.CHECK_BOX) {
			type = "java.lang.Boolean";
		} else if (vp.getComponentType() == ComponentType.COMBO_BOX) {
			// DATA USED FOR ENUMERATION GENERATION
			String enumName = cc.toCamelCase(vp.getLabel(), false);
			enumName += cc.toCamelCase(vp.umlClass().name(), false) + "Enum";
			String enumClass = vp.umlClass().name();
			String enumProp = cc.toCamelCase(vp.getLabel(), true);
			String[] enumValues = { "None" };
			if (vp.getEnumeration() != null) {
				enumValues = vp.getEnumeration().split(";");
			}
			enumeration = new Enumeration(enumName, vp.getLabel(), enumClass, enumProp, enumValues);
			enumerations.add(enumeration);
		} else if (vp.getComponentType() == ComponentType.TEXT_AREA) {
			// Since both text areas and text fields contain string data,
			// we need to add suffix to generate appropriate UI control
			// suffix needs to be skipped when generating java classes
			type = "java.lang.String:TextArea";
		}

		ArrayList<String> anotations = new ArrayList<String>();
		// String name = cc.toCamelCase(vp.name(), true);
		/*
		 * TODO Prefiks se za sada koristi jer puca ako je ime nekog polja
		 * rezervisana rec u programskom jeziku.
		 */
		String name = "ka_" + cc.toCamelCase(vp.name(), true);
		String label = vp.getLabel();
		String columnLabel = vp.getColumnLabel();
		int length = vp.getLength();
		int precision = vp.getPrecision();

		String lenPrecAnnotation = "";
		// Skip length and precision specification if length = 0
		if (length > 0) {
			lenPrecAnnotation += ", length = " + length + ", precision = " + precision;
		}
		if (vp.getPersistentType() == null) {
			vp.setPersistentType("varchar");
		}
		anotations.add("@Column(name = \"" + columnLabel + "\", unique = false, nullable = false " + lenPrecAnnotation
				+ ",columnDefinition = \"" + vp.getPersistentType().toUpperCase() + "\")");
		EJBAttribute attribute = new EJBAttribute(anotations, type, name, label, columnLabel, length, precision, true,
				false, vp.isRepresentative(), enumeration);
		return attribute;
	}

	/**
	 * Gets data from Zoom and constructs corresponding EJBAttribute object
	 * 
	 * @param z
	 * @return
	 */
	public EJBAttribute getZoomData(Zoom z, String className) {
		// KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("GET
		// ZOOM DATA: " + z.getLabel() + " in " + className, 0);
		if (z.getTargetPanel() != null) {
			VisibleClass vc = z.getTargetPanel();
			if (vc instanceof StandardPanel) {
				StandardPanel spanel = (StandardPanel) vc;
				String type = spanel.getPersistentClass().name();

				ArrayList<String> anotations = new ArrayList<String>();
				// String name =
				// cc.toCamelCase(z.getTargetPanel().getComponent().getName(),
				// true);//OVO JE BILO ORIGINALNO
				String name = cc.toCamelCase(z.getLabel(), true);
				//////////////////////////////////////////////////////////////////////////////////////////////////////
				String propName = cc.toCamelCase(className, true) + "_" + name;
				String databaseName = z.getLabel().substring(0, 1).toLowerCase() + z.getLabel().substring(1);
				String label = z.getLabel();
				Boolean mandatory = z.lower() != 0;

				anotations.add("@ManyToOne");
				anotations.add("@JoinColumn(name=\"" + propName + "\", referencedColumnName=\"ID\",  nullable = "
						+ !mandatory + ")");

				// Length and precision for zoom fields are left 0, which
				// indicates they are ignored on the template
				EJBAttribute attribute = new EJBAttribute(anotations, type, propName, label, databaseName, 0, 0,
						mandatory, false, false, null);
				return attribute;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Fetches data from KROKI BusinessSubSystem and constructs Menu instances.
	 * 
	 * @param el
	 * @param index
	 * @param mmenu
	 */
	public void getSubSystemData(VisibleElement el, int index, Menu mmenu, kroki.app.menu.Submenu subMenuNew) {
		// MENU GENERATION DATA
		String n = el.name();
		if (n == null) {
			n = el.getLabel();
		}
		String name = "menu" + index + "_" + n;
		String label = n.replace("_", " ");
		Menu menu = new Menu(name, label, new ArrayList<Submenu>(), new ArrayList<Menu>());

		// NEW
		kroki.app.menu.Submenu subMenuTemp = new kroki.app.menu.Submenu(label);
		BussinesSubsystem bs = (BussinesSubsystem) el;

		for (int m = 0; m < bs.ownedElementCount(); m++) {
			VisibleElement e = bs.getOwnedElementAt(m);
			if (e instanceof VisibleClass) {
				try {
					getClassData(e, el.name(), menu, subMenuTemp);
				} catch (NoZoomPanelException e1) {
					e1.printStackTrace();
				}
			} else if (e instanceof BussinesSubsystem) {
				getSubSystemData(e, index + 1, menu, subMenuTemp);
			}
		}
		if (mmenu != null) {
			mmenu.addMenu(menu);

			subMenuNew.getChildren().add(subMenuTemp);
			subMenuTemp.setParent(subMenuNew);
		} else {
			menus.add(menu);

			rootMenu.getChildren().add(subMenuTemp);
			subMenuTemp.setParent(rootMenu);
		}
	}

	/**
	 * Adding OneToMany attributes to classes on opposite sides of zoom
	 * attributes this is done after all panels have been processed and added to
	 * classes list which allows child panel to be drawn after parent
	 */
	public void addReferences() {
		for (int i = 0; i < classes.size(); i++) {
			EJBClass ejbClass = classes.get(i);
			for (int j = 0; j < ejbClass.getAttributes().size(); j++) {
				EJBAttribute attribute = ejbClass.getAttributes().get(j);
				if (getAttributeType(attribute).equals("ManyToOne")) {
					EJBClass oppositeCLass = getClass(attribute.getType());
					if (oppositeCLass != null) {
						// String name = ejbClass.getName() + "Set";//OVO JE
						// BILO ORIGINALNO
						String name = attribute.getName() + "Set";
						//////////////////////////////////////////////////////////////////
						String type = "Set<" + ejbClass.getName() + ">";
						String label = attribute.getLabel();
						String mappedBy = attribute.getName();
						ArrayList<String> annotations = new ArrayList<String>();
						annotations.add("@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = \"" + mappedBy
								+ "\")");

						EJBAttribute attr = new EJBAttribute(annotations, type, name, label, name, 0, 0, true, false,
								false, null);
						oppositeCLass.getAttributes().add(attr);

						KrokiMockupToolApp.getInstance().displayTextOutput("[PROJECT EXPORTER] Adding refference: "
								+ attr.getName() + " --> " + oppositeCLass.getName(), 0);

						for (int k = 0; k < oppositeCLass.getAttributes().size(); k++) {
							EJBAttribute att = oppositeCLass.getAttributes().get(k);
							if (att.getRepresentative()) {
								attribute.getColumnRefs().add(att);
							}
						}

					} else {
						KrokiMockupToolApp.getInstance().displayTextOutput("Class '" + ejbClass.getLabel()
								+ "' references non-exsisting class '" + attribute.getType() + "'", 3);
						throw new NullPointerException("Refferenced file " + attribute.getType() + " not found!");
					}
				}
			}
		}
	}

	/**
	 * Gets reference to EJB class from model based on name
	 * 
	 * @param name
	 * @return
	 */
	public EJBClass getClass(String name) {
		EJBClass clas = null;
		for (int i = 0; i < classes.size(); i++) {
			EJBClass cl = classes.get(i);
			if (cl.getName().equalsIgnoreCase(name)) {
				clas = cl;
			}
		}
		return clas;
	}

	/**
	 * Determines attribute type based on it's annotation
	 * 
	 * @param attribute
	 * @return Column, OneToMany or ManyToOne
	 */
	public String getAttributeType(EJBAttribute attribute) {
		String annotation = attribute.getAnnotations().get(0);
		if (annotation.startsWith("@Column")) {
			return "Column";
		} else if (annotation.startsWith("@ManyToOne")) {
			return "ManyToOne";
		} else {
			return "OneToMany";
		}

	}

	/**
	 * Writes name of current project to corresponding configuration file
	 * 
	 * @param name
	 */
	public void writeProjectName(String name, String description) {
		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 1);
		if (!KrokiMockupToolApp.getInstance().isBinaryRun()) {
			appPath = appPath.substring(0, appPath.length() - 16);
		}

		if (description == null) {
			description = "Please log in to continue.";
		}

		// Configuration file
		File propertiesFile = new File(
				appPath + "SwingApp" + File.separator + "props" + File.separator + "main-generated.properties");
		String toAppendName = "main.form.name";
		String toAppendDescription = "app.description";

		if (!swing) {
			propertiesFile = new File(appPath + "ApplicationRepository" + File.separator + "generated" + File.separator
					+ "props" + File.separator + "main.properties");
			toAppendName = "app.title";
		}

		// read properties file
		// and append first line which contains main form title
		Scanner scan;
		ArrayList<String> lines = new ArrayList<String>();
		lines.add(toAppendName + " = " + name);
		if (!swing) {
			lines.add(toAppendDescription + " = " + description);
		}
		try {
			scan = new Scanner(propertiesFile);
			while (scan.hasNext()) {
				String line = scan.nextLine();
				if (!line.startsWith(toAppendName)) {
					if (!line.startsWith(toAppendDescription)) {
						lines.add(line);
					}
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("[PROJECT EXPORTER] Pproperties file not found. Creating a new one.");
		}

		// write main.properties file with main form title
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(propertiesFile));
			for (int i = 0; i < lines.size(); i++) {
				pw.println(lines.get(i));
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Runs ant script file which compiles generated application
	 * 
	 * @param file
	 * @param proj
	 * @param message
	 */
	public void runAnt(File file, BussinesSubsystem proj, String jarName, String message) {
		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 1);
		if (!KrokiMockupToolApp.getInstance().isBinaryRun()) {
			appPath = appPath.substring(0, appPath.length() - 16);
		}
		String appFolderName = "SwingApp";
		if (!swing) {
			appFolderName = "WebApp";
		}

		File buildFile = new File(appPath + appFolderName + File.separator + "build.xml");

		if (proj.getEclipseProjectPath() != null) {
			if (!swing) {
				buildFile = new File(proj.getEclipseProjectPath().getAbsolutePath() + File.separator + appFolderName
						+ File.separator + "kroki-build.xml");
			}
		} else {
			if (!swing) {
				buildFile = new File(appPath + appFolderName + File.separator + "kroki-build.xml");
			}
		}

		File outputFile = new File(file.getAbsolutePath() + File.separator + jarName);

		RunAnt runner = new RunAnt();
		runner.runBuild(jarName + ".jar", buildFile, outputFile);
		if (message != null) {
			KrokiMockupToolApp.getInstance().displayTextOutput(message, OutputPanel.KROKI_FINISHED);
		}
	}

	/**
	 * Adds default data for SWING application
	 * 
	 * @param proj
	 */
	public void addDefaultSwingData(BussinesSubsystem proj) {
		// Add User class to classes list
		ArrayList<String> usernameAnnotations = new ArrayList<String>();
		usernameAnnotations.add("@Column(name = \"username\", unique = false, nullable = false)");
		EJBAttribute usernameAttribute = new EJBAttribute(usernameAnnotations, "java.lang.String", "username",
				"User name", "username", 20, 0, true, false, true, null);

		ArrayList<String> passwordAnnotations = new ArrayList<String>();
		passwordAnnotations.add("@Column(name = \"password\", unique = false, nullable = false)");
		EJBAttribute passwordAttribute = new EJBAttribute(passwordAnnotations, "java.lang.String", "password",
				"Password", "password", 20, 0, true, false, false, null);

		String userTableName = cc.toDatabaseFormat(proj.getLabel(), "User");
		ArrayList<EJBAttribute> userAttributes = new ArrayList<EJBAttribute>();
		userAttributes.add(usernameAttribute);
		userAttributes.add(passwordAttribute);

		EJBClass user = new EJBClass("ejb", "default", "User", userTableName, "User", userAttributes);
		classes.add(user);

		// add default enumerations
		// OpenedAs
		Enumeration openedAsEnum = new Enumeration("OpenedAs", "Opened as", null, null,
				new String[] { "DEFAULT", "ZOOM", "NEXT" });
		// OperationType
		Enumeration operationTypeEnum = new Enumeration("OperationType", "Operation type", null, null,
				new String[] { "BussinesTransaction", "ViewReport", "JavaOperation" });
		// PanelType
		Enumeration panelTypeEnum = new Enumeration("PanelType", "Panel type", null, null,
				new String[] { "StandardPanel", "ParentChildPanel", "ManyToManyPanel" });
		// StateMode
		Enumeration stateModeEnum = new Enumeration("StateMode", "State mode", null, null,
				new String[] { "UPDATE", "ADD", "SEARCH" });
		// ViewMode
		Enumeration viewModeEnum = new Enumeration("ViewMode", "View mode", null, null,
				new String[] { "TABLEVIEW", "INPUTPANELVIEW" });

		enumerations.add(openedAsEnum);
		enumerations.add(operationTypeEnum);
		enumerations.add(panelTypeEnum);
		enumerations.add(stateModeEnum);
		enumerations.add(viewModeEnum);
	}

	/**
	 * Adds default classes for web application to classes list.
	 * 
	 * @param classes
	 */
	public void addDefaultClasses(ArrayList<EJBClass> classes) {
		// ACTION
		ArrayList<String> annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"ACT_NAME\", unique = false, nullable = false)");
		EJBAttribute actionName = new EJBAttribute(annotations, "java.lang.String", "name", "Name", "ACT_NAMER", 20, 0,
				true, false, true, null);

		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"ACT_LINK\", unique = false, nullable = false)");
		EJBAttribute actionLink = new EJBAttribute(annotations, "java.lang.String", "link", "Link", "ACT_LINK", 20, 0,
				true, false, false, null);

		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"ACT_IMG_PATH\", unique = false, nullable = false)");
		EJBAttribute actionImagePath = new EJBAttribute(annotations, "java.lang.String", "imagePath", "Image path",
				"ACT_IMAGE_PATH", 20, 0, false, false, false, null);

		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"ACT_TYPE\", unique = false, nullable = false)");
		EJBAttribute actionType = new EJBAttribute(annotations, "java.lang.String", "type", "Type", "ACT_TYPE", 20, 0,
				true, false, false, null);

		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"ACT_TIP\", unique = false, nullable = false)");
		EJBAttribute actionTip = new EJBAttribute(annotations, "java.lang.String", "tip", "Tip", "ACT_TIP", 20, 0,
				false, false, false, null);

		ArrayList<EJBAttribute> actionAttributes = new ArrayList<EJBAttribute>();
		actionAttributes.add(actionName);
		actionAttributes.add(actionLink);
		actionAttributes.add(actionImagePath);
		actionAttributes.add(actionType);
		actionAttributes.add(actionTip);

		EJBClass action = new EJBClass("adapt.entities", "default", "Action", "ADAPT_ACTION", "Action",
				actionAttributes);

		// USER
		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"USER_USERNAME\", unique = true, nullable = false)");
		EJBAttribute userName = new EJBAttribute(annotations, "java.lang.String", "username", "Username",
				"USER_USERNAME", 20, 0, true, true, true, null);

		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"USER_PASSWORD\", unique = false, nullable = false)");
		EJBAttribute userPassword = new EJBAttribute(annotations, "String", "password", "Password", "USER_PASSWORD", 20,
				0, true, false, false, null);

		annotations = new ArrayList<String>();
		annotations.add("@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = \"user\")");
		EJBAttribute userRights = new EJBAttribute(annotations, "Set<UserRights>", "rights", "Rights", "USER_RIGHTS",
				20, 0, false, false, false, null);

		ArrayList<EJBAttribute> userAttributes = new ArrayList<EJBAttribute>();
		userAttributes.add(userName);
		userAttributes.add(userPassword);
		userAttributes.add(userRights);

		EJBClass user = new EJBClass("adapt.entities", "default", "User", "ADAPT_USER", "User", userAttributes);

		// MYRESOURCE
		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"MYRES_ENT_ID\", unique = false, nullable = false)");
		EJBAttribute myResourceEntityID = new EJBAttribute(annotations, "java.lang.Long", "entityId", "Entity ID",
				"MYERS_ENT_ID", 20, 0, true, false, false, null);

		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"MYRES_TABLE\", unique = false, nullable = false)");
		EJBAttribute myResourceTable = new EJBAttribute(annotations, "java.lang.String", "table", "Table",
				"MYRES_TABLE", 20, 0, true, false, false, null);

		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"MYRES_ENT_LABEL\", unique = false, nullable = false)");
		EJBAttribute myResourceEntityLabel = new EJBAttribute(annotations, "java.lang.String", "entityLabel",
				"Entity label", "MYRES_ENT_LABEL", 20, 0, true, false, false, null);

		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"MYRES_TABLE_LABEL\", unique = false, nullable = false)");
		EJBAttribute myResourceTableLabel = new EJBAttribute(annotations, "java.lang.String", "tableLabel", "Table",
				"MYRES_TABLE_LABEL", 20, 0, true, false, false, null);

		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"MYRES_RESLINK\", unique = false, nullable = false)");
		EJBAttribute myResourceResourceLink = new EJBAttribute(annotations, "java.lang.String", "resLink",
				"Resource link", "MYRES_RESLINK", 20, 0, true, false, false, null);

		annotations = new ArrayList<String>();
		annotations.add("@ManyToOne");
		annotations.add("@JoinColumn(name=\"user\", referencedColumnName=\"ID\",  nullable = false)");
		EJBAttribute myResourceUser = new EJBAttribute(annotations, "User", "user", "User", "MYRES_USER", 20, 0, true,
				false, false, null);

		ArrayList<EJBAttribute> myResourceAttributes = new ArrayList<EJBAttribute>();
		myResourceAttributes.add(myResourceEntityID);
		myResourceAttributes.add(myResourceTable);
		myResourceAttributes.add(myResourceEntityLabel);
		myResourceAttributes.add(myResourceTableLabel);
		myResourceAttributes.add(myResourceResourceLink);
		myResourceAttributes.add(myResourceUser);

		EJBClass myResource = new EJBClass("adapt.entities", "default", "MyResource", "ADAPT_MY_RESOURCE",
				"My Resources", myResourceAttributes);

		// RESOURCE
		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"RES_NAME\", unique = false, nullable = false)");
		EJBAttribute resourceName = new EJBAttribute(annotations, "java.lang.String", "name", "Name", "RES_NAME", 20, 0,
				true, true, true, null);

		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"RES_LINK\", unique = false, nullable = false)");
		EJBAttribute resourceLink = new EJBAttribute(annotations, "java.lang.String", "link", "Link", "RES_LINK", 50, 0,
				true, true, false, null);

		ArrayList<EJBAttribute> resourceAttributes = new ArrayList<EJBAttribute>();
		resourceAttributes.add(resourceName);
		resourceAttributes.add(resourceLink);

		EJBClass resource = new EJBClass("adapt.entities", "default", "Resource", "ADAPT_RESOURCE", "Resources",
				resourceAttributes);

		// USERRIGHTS
		annotations = new ArrayList<String>();
		annotations.add("@Column(name = \"UR_ALLOWED\", unique = false, nullable = false)");
		EJBAttribute userRightsAllowed = new EJBAttribute(annotations, "java.lang.Boolean", "allowed", "Allowed",
				"UR_ALLOWED", 20, 0, true, false, false, null);

		annotations = new ArrayList<String>();
		annotations.add("@ManyToOne");
		annotations.add("@JoinColumn(name=\"user\", referencedColumnName=\"ID\",  nullable = false)");
		EJBAttribute userRightsUser = new EJBAttribute(annotations, "User", "user", "User", "UR_USER", 20, 0, true,
				false, false, null);

		annotations = new ArrayList<String>();
		annotations.add("@ManyToOne");
		annotations.add("@JoinColumn(name=\"action\", referencedColumnName=\"ID\",  nullable = false)");
		EJBAttribute userRightsAction = new EJBAttribute(annotations, "Action", "action", "Action", "UR_ACTION", 20, 0,
				true, false, false, null);

		annotations = new ArrayList<String>();
		annotations.add("@ManyToOne");
		annotations.add("@JoinColumn(name=\"resource\", referencedColumnName=\"ID\",  nullable = false)");
		EJBAttribute userRightsResource = new EJBAttribute(annotations, "Resource", "resource", "resource",
				"UR_RESOURCE", 20, 0, true, false, false, null);

		ArrayList<EJBAttribute> userRightsAttributes = new ArrayList<EJBAttribute>();
		userRightsAttributes.add(userRightsAllowed);
		userRightsAttributes.add(userRightsUser);
		userRightsAttributes.add(userRightsAction);
		userRightsAttributes.add(userRightsResource);

		EJBClass uRights = new EJBClass("adapt.entities", "default", "UserRights", "ADAPT_USER_RIGHTS", "User rights",
				userRightsAttributes);

		classes.add(action);
		classes.add(user);
		classes.add(myResource);
		classes.add(resource);
		classes.add(uRights);
	}

	// ------------------------------------------------------------------------------------------------------------------
	// || GETTERS AND SETTERS

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

	public ArrayList<Enumeration> getEnumerations() {
		return enumerations;
	}

	public void setEnumerations(ArrayList<Enumeration> enumerations) {
		this.enumerations = enumerations;
	}

	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(ArrayList<Constraint> constraints) {
		this.constraints = constraints;
	}
}