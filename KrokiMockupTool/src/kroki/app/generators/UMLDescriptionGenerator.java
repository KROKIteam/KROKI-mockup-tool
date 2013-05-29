package kroki.app.generators;

import java.util.ArrayList;
import java.util.List;

import kroki.app.KrokiMockupToolApp;
import kroki.app.export.SwingExporter;
import kroki.app.generators.utils.Attribute;
import kroki.app.generators.utils.EJBClass;
import kroki.app.generators.utils.ManyToOneAttribute;
import kroki.app.generators.utils.Menu;
import kroki.app.generators.utils.OneToManyAttribute;
import kroki.app.utils.DiagramProfile;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Zoom;
import kroki.profil.operation.Report;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ManyToMany;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;

public class UMLDescriptionGenerator {
	
	NamingUtil namer;
	SwingExporter exporter;
	DiagramProfile profile;
	String connections;
	String parentChildDescriptions;
	
	/**
	 * Generates UML description for PlantUML
	 * @param project
	 * @return
	 */
	public String generateDescription(BussinesSubsystem project, DiagramProfile profile) {
		namer = new NamingUtil();
		exporter = new SwingExporter();
		String desc = "@startuml \n";
		connections = "\n";
		parentChildDescriptions = "";
		this.profile = profile;
		
		if(isProject(project)) {
			exporter.getData(project);
			//generate package data
			for (Menu menu : exporter.getMenus()) {
				desc += getPackageDescription(menu);
			}
			getClassConnections(exporter.getElements());
			desc += connections;
			desc += parentChildDescriptions + "\n";
		}else {
			//if package is selected, first find project it belongs to
			BussinesSubsystem p = findProject(project);
			//then fetch project data
			exporter.getData(p);
			for (Menu menu : exporter.getMenus()) {
				if(menu.getLabel().equals(project.getLabel())) {
					//display only data for selected package
					String packName = namer.toCamelCase(menu.getLabel(), true);
					desc += getPackageDescription(menu);
					ArrayList<EJBClass> packClasses = getSubsystemClasses(packName, exporter.getClasses());
					ArrayList<VisibleElement> packElements = getElementsFromClasses(packClasses, exporter.getElements());
					ArrayList<VisibleElement> packPCElements = getParentChildElements(exporter.getElements(), packName);
					packElements.addAll(packPCElements);
					getClassConnections(packElements);
					desc += connections;
					desc += parentChildDescriptions;
				}
			}
			
			
		}
		return desc + "\n@enduml";
	}
	
	
	/************************************************************
	 * Generates PlantUML package description for KROKI packages
	 * @param pack
	 * @return
	 ***********************************************************/
	public String getPackageDescription(Menu pack) {
		String packName = namer.toCamelCase(pack.getLabel(), true);
		String desc = "\npackage " + packName + " <<Frame>> {";;
		ArrayList<EJBClass> subsysClasses = getSubsystemClasses(packName, exporter.getClasses());
		//first find all standard panel elements for current package
		ArrayList<VisibleElement> subsystemElements = getElementsFromClasses(subsysClasses, exporter.getElements());
		//then all parent-child elements
		ArrayList<VisibleElement> subsysPCElements = getParentChildElements(exporter.getElements(), packName);
		//and then concatenate these lists
		subsystemElements.addAll(subsysPCElements);
		if(profile == DiagramProfile.PERSISTENT_PROFILE) {
			if(!subsysClasses.isEmpty()) {
				for (EJBClass ejbClass : subsysClasses) {
					desc += "\n\t" + getPersistentClassDescription(ejbClass);
				}
			}
		}else if (profile == DiagramProfile.UI_PROFILE) {
			if(!subsystemElements.isEmpty()) {
				for (VisibleElement visibleElement : subsystemElements) {
					desc += "\n\t" + getUIClassDescription(visibleElement);
				}
			}
		}
		for (Menu submenu : pack.getMenus()) {
			desc += getPackageDescription(submenu);
		}
		return desc + "\n}";
	}
	
	
	/***************************************************************
	 * Generates PlantUML description for KROKI persistent diagram
	 * @param clas
	 * @return
	 ***************************************************************/
	public String getPersistentClassDescription(EJBClass clas) {
		String className = clas.getName();
		String stereotype = " <<PersistentClass>> ";
		String classDescription = "\nclass " + className + stereotype;
		
		ArrayList<Attribute> classAttributes = clas.getAttributes();
		if(!classAttributes.isEmpty()) {
			classDescription += "{";
			for (Attribute attribute : classAttributes) {
				String[] splitedType = attribute.getType().split("\\.");
				int index = splitedType.length-1;
				String simpleType = splitedType[index];
				classDescription += "\n\t\t-" + attribute.getName() + " : " + simpleType;
			}
//			for (ManyToOneAttribute mtoAttr : clas.getManyToOneAttributes()) {
//				classDescription += "\n\t\t-" + mtoAttr.getName() + " : " + mtoAttr.getType();
//			}
//			for (OneToManyAttribute otmAttr : clas.getOneToManyAttributes()) {
//				classDescription += "\n\t\t-" + otmAttr.getName() + " : HashSet<" + otmAttr.getRefferencedTable() + ">";
//			}
			classDescription += "\n\t}";
		}
		return "\n" + classDescription;
	}
	
	
	/************************************************************
	 * Generates PlantUML description for KROKI UI diagram
	 * @param clas
	 * @return
	 ************************************************************/
	public String getUIClassDescription(VisibleElement element) {
		VisibleClass clas = (VisibleClass) element;
		String stereotype = "<<StandardPanel>>";
		String className = namer.toCamelCase(element.getLabel(), false);
		if(element instanceof ParentChild) {
			stereotype = "<<ParentChild>>";
		}else if(element instanceof ManyToMany) {
			stereotype = "<<ManyToMany>>";
		}
		String classDesc = "\nclass " + className + stereotype;
		
		List<VisibleProperty> props = clas.containedProperties();
		List<VisibleOperation> ops = clas.containedOperations();
		if(!props.isEmpty()) {
			classDesc += " {";
			for (VisibleProperty prop : props) {
				classDesc += "\n\t\t-" + prop.getLabel() + ":" + prop.getComponentType();
			}
			for (VisibleOperation op : ops) {
				String opName = op.name();
				String opStereotype = "<<Transaction>>";
				if(op instanceof Report) {
					opStereotype = "<<Report>>";
				}
				classDesc += "\n\t\t " + opName + "() " + opStereotype;
			}
			classDesc += "\n\t}";
		}
		
		return classDesc;
	}
	
	
	public void getClassConnections(ArrayList<VisibleElement> elements) {
		if(profile == DiagramProfile.UI_PROFILE) {
			getUIClassConnections(elements);
		}else {
			getPersistentClassConnections(elements);
		}
		
	}
	
	public void getUIClassConnections(ArrayList<VisibleElement> elements) {
		for (VisibleElement element : elements) {
			String elName = namer.toCamelCase(element.getLabel(), false);
			if(element instanceof StandardPanel) {
				VisibleClass vClass = (VisibleClass) element;
				for (Zoom zoom : vClass.containedZooms()) {
					String zoomed = namer.toCamelCase(zoom.getTargetPanel().getComponent().getName(), false);
					connections += "\n" + elName + " \"*\" -- \"1 <<zoom>>\" " + zoomed;
				}
			}else if (element instanceof ParentChild) {
				ParentChild pc = (ParentChild) element;
				for (Hierarchy h : pc.allContainedHierarchies()) {
					String hName = namer.toCamelCase(h.getTargetPanel().getComponent().getName(), false);
					connections += "\n" + elName + " \"1\" -- \"1\" " + hName + ":<<hierarchy : level " + h.getLevel() +">>"; 
				}
			}
		}
	}
	
	public void getPersistentClassConnections(ArrayList<VisibleElement> elements) {
		for (VisibleElement element : elements) {
			String elName = namer.toCamelCase(element.getLabel(), false);
			if(element instanceof StandardPanel) {
				VisibleClass vClass = (VisibleClass) element;
				for (Zoom zoom : vClass.containedZooms()) {
					String zoomed = namer.toCamelCase(zoom.getTargetPanel().getComponent().getName(), false);
					String zoomLabel = namer.toCamelCase(zoom.getLabel(), false);
					connections += "\n" + elName + " \"*\" -- \"1\" " + zoomed + " : " + zoomLabel;
				}
			}
		}
	}
	
	//-------------------------UTIL FUNCTIONS-------------------------------------
	public EJBClass getClassByName(String name, ArrayList<EJBClass> classes) {
		for (EJBClass cl : classes) {
			if(cl.getName().equals(name)) {
				return cl;
			}
		}
		
		return null;
	}
	
	public ArrayList<EJBClass> getSubsystemClasses(String subsystemName, ArrayList<EJBClass> clazzez) {
		ArrayList<EJBClass> classes = new ArrayList<EJBClass>();
		for (EJBClass ejbClass : clazzez) {
			if(ejbClass.getSubsystem().equalsIgnoreCase(subsystemName)) {
				classes.add(ejbClass);
			}
		}
		return classes;
	}
	

	/*
	 * Gets list of visible elements based on list of ejb classes
	 */
	public ArrayList<VisibleElement> getElementsFromClasses(ArrayList<EJBClass> clazzez, ArrayList<VisibleElement> elements) {
		ArrayList<VisibleElement> els = new ArrayList<>();
		for (VisibleElement element : elements) {
			if (element instanceof StandardPanel) {
				for (EJBClass ejbClass : clazzez) {
					if(namer.toCamelCase(element.getLabel(), false).equalsIgnoreCase(ejbClass.getName())) {
						els.add(element);
					}
				}
			}
		}
		return els;
	}
	
	/*
	 * Gets list of parent-child elements that belong to specified package
	 */
	public ArrayList<VisibleElement> getParentChildElements(ArrayList<VisibleElement> elements, String packageName) {
		ArrayList<VisibleElement> els = new ArrayList<VisibleElement>();
		for (VisibleElement element : elements) {
			//since parent-child elements don't have ejb class accociated, package is determined by their child panels
			if(element instanceof ParentChild) {
				ParentChild pc = (ParentChild) element;
				//if all child panels are in the same package, place parent panel in that package,
				//else, place it in separate package called 'parent-child panels'
				boolean samePackage = true;
				for (Hierarchy hier : pc.allContainedHierarchies()) {
					String targetPanel = namer.toCamelCase(hier.getTargetPanel().getComponent().getName(), false);
					EJBClass panelClazz = getClassByName(targetPanel, exporter.getClasses());
					if(!panelClazz.getSubsystem().equalsIgnoreCase(packageName)) {
						samePackage = false;
					}
				}
				if(samePackage) {
					els.add(element);
				}
//				else {
//					parentChildDescriptions += "\nclass " + pcName + " <<ParentChild>>\n";
//				}
			}
		}
		return els;
	}
	
	/*
	 * Checks if package is project or package;
	 */
	public boolean isProject(BussinesSubsystem pack) {
		String name = pack.getLabel();
		for(int i=0; i<KrokiMockupToolApp.getInstance().getWorkspace().getPackageCount(); i++) {
			BussinesSubsystem sys = (BussinesSubsystem)KrokiMockupToolApp.getInstance().getWorkspace().getPackageAt(i);
			if(sys.getLabel().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Finds project based on belonging package
	 */
	public BussinesSubsystem findProject(BussinesSubsystem pack) {
		BussinesSubsystem project = null;
		for(int i=0; i<KrokiMockupToolApp.getInstance().getWorkspace().getPackageCount(); i++) {
			BussinesSubsystem proj = (BussinesSubsystem)KrokiMockupToolApp.getInstance().getWorkspace().getPackageAt(i);
			for (int j=0; j<proj.ownedElementCount(); j++) {
				BussinesSubsystem subsys = (BussinesSubsystem) proj.getOwnedElementAt(j);
				if(subsys.name().equals(pack.name())){
					project = proj;
				}
			}
		}
		return project;
	}
	
}
