package kroki.profil.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import kroki.mockup.model.Composite;
import kroki.mockup.model.layout.BorderLayoutManager;
import kroki.mockup.model.layout.FlowLayoutManager;
import kroki.mockup.model.layout.FreeLayoutManager;
import kroki.mockup.model.layout.LayoutManager;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.group.GroupAlignment;
import kroki.profil.group.GroupLocation;
import kroki.profil.group.GroupOrientation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;

/**
 * Class contains <code>Hierarchy</code> util methods 
 * @author Kroki Team
 */
public class ParentChildUtil {


	/**
	 * Sets default values of gui properties of the given parent-child panel
	 * (properties panel, operations panel, layouts etc.)
	 * @param panel
	 */
	public static void defaultGuiSettings(ParentChild panel) {
		Composite root = ((Composite) panel.getComponent());
		root.setLayoutManager(new BorderLayoutManager());
		panel.setPropertiesPanel(new ElementsGroup("properties", ComponentType.PANEL));
		panel.getPropertiesPanel().setGroupLocation(GroupLocation.componentPanel);
		panel.getPropertiesPanel().setGroupOrientation(GroupOrientation.area);
		LayoutManager propertiesLayout = new FreeLayoutManager();
		((Composite) panel.getPropertiesPanel().getComponent()).setLayoutManager(propertiesLayout);
		((Composite) panel.getPropertiesPanel().getComponent()).setLocked(true);
		panel.setOperationsPanel(new ElementsGroup("operations", ComponentType.PANEL));
		panel.getOperationsPanel().setGroupLocation(GroupLocation.operationPanel);
		panel.getOperationsPanel().setGroupOrientation(GroupOrientation.horizontal);
		panel.getOperationsPanel().setGroupAlignment(GroupAlignment.left);
		LayoutManager operationsLayout = new FlowLayoutManager();
		operationsLayout.setAlign(LayoutManager.LEFT);
		((Composite) panel.getOperationsPanel().getComponent()).setLayoutManager(operationsLayout);
		((Composite) panel.getOperationsPanel().getComponent()).setLocked(true);
		UIPropertyUtil.addVisibleElement(panel, panel.getPropertiesPanel());
		UIPropertyUtil.addVisibleElement(panel, panel.getOperationsPanel());
		root.addChild(panel.getPropertiesPanel().getComponent(), BorderLayoutManager.CENTER);
		root.addChild(panel.getOperationsPanel().getComponent(), BorderLayoutManager.SOUTH);
		panel.update();
	}
	
	

	/**
	 * Return all hierarchies contained by the panel
	 * @param panel Parent-child panel
	 * @return All contained hierarchies
	 */
	public static List<Hierarchy> allContainedHierarchies(ParentChild panel) {
		List<Hierarchy> allContainedHierarchies = new ArrayList<Hierarchy>();
		for (VisibleElement visibleElement : panel.getVisibleElementList()) {
			if (visibleElement instanceof Hierarchy) {
				allContainedHierarchies.add((Hierarchy) visibleElement);
			}
		}
		return allContainedHierarchies;
	}

	/**
	 * Return all panels contained by the panel (all or only standard)
	 * @param panel Parent-child panel
	 * @param onlyStandard Indicates if only standard or all panels are considered
	 * @return All contained panels
	 */
	public static List<VisibleClass> allContainedPanels(ParentChild panel, boolean onlyStandard){
		List<VisibleClass> allContainedPanels = new ArrayList<VisibleClass>();
		for (VisibleElement visibleElement : panel.getVisibleElementList()) {
			if (visibleElement instanceof Hierarchy) {
				if ((onlyStandard && ((Hierarchy)visibleElement).getTargetPanel() instanceof StandardPanel) 
						|| !onlyStandard)
					allContainedPanels.add(((Hierarchy)visibleElement).getTargetPanel());
			}
		}
		return allContainedPanels;
 	}

	/**
	 * Returns number of the hierarchies contained by the panel
	 * @param panel Parent-child panel
	 * @return Number of contained hierarchies
	 */
	public static int getHierarchyCount(ParentChild panel) {
		int i = 0;
		for (VisibleElement visibleElement : panel.getVisibleElementList()) {
			if (visibleElement instanceof Hierarchy) {
				i++;
			}
		}
		return i;
	}

	/**
	 * Return the root of the hierarchy (or null if there is none)
	 * @param panel Parent-child panel
	 * @return Root of the hierarchy or null if there is none
	 */
	public static Hierarchy getHierarchyRoot(ParentChild panel) {
		Hierarchy root = null;
		for (VisibleElement visibleElement : panel.getVisibleElementList()) {
			if (visibleElement instanceof Hierarchy) {
				if (((Hierarchy) visibleElement).getLevel() == 1) {
					root = (Hierarchy) visibleElement;
					break;
				}
			}
		}
		return root;
	}

	/**
	 * Returns all hierarchies at the given level
 	 * @param panel Parent-child panel
 	 * @param level Level
	 * @return All hierarchies at the specified level
	 */
	public static List<Hierarchy> allHierarchiesByLevel(ParentChild panel, int level) {
		List<Hierarchy> allHierarchiesByLevel = new ArrayList<Hierarchy>();
		for (VisibleElement visibleElement : panel.getVisibleElementList()) {
			if (visibleElement instanceof Hierarchy) {
				Hierarchy hierarchy = (Hierarchy) visibleElement;
				if (hierarchy.getLevel() == level) {
					allHierarchiesByLevel.add((Hierarchy) visibleElement);
				}
			}
		}
		return allHierarchiesByLevel;
	}

	//----------Target panel--------------------------------

	/**
	 * Return all panels which can become target panels.
	 * That is, all suitable standard panels (found when examining next association ends of existing target panels) 
	 * and parent-child panels which contain those standard panels (between which and the current parent-child panels
	 * are no references at the time)
	 * @param panel Parent-child panel
	 * @return All possible target panels
	 */
	public static List<VisibleClass> getAllPosibleTargetPanels(ParentChild panel){
		List<VisibleClass> ret = new ArrayList<VisibleClass>();

		List<VisibleClass> standardPanels = possibleTargetStandardPanels(panel);
		ret.addAll(standardPanels);

		//add parent child panels that contain found standard panels

		ret.addAll(allParentChildPanelsContainingPanels(panel, standardPanels));
		return ret;

	}

	/**
	 * Finds all possible target standard panels
	 * @param panel Parent-child panel
	 * @return All possible target standard panels
	 */
	private static List<VisibleClass> possibleTargetStandardPanels(ParentChild panel){
		List<VisibleClass> ret = new ArrayList<VisibleClass>();

		//find all next association ends

		for (Hierarchy hierarchy :  VisibleClassUtil.containedHierarchies(panel)){
			VisibleClass targetPanel = hierarchy.getTargetPanel();
			if (targetPanel != null && targetPanel instanceof ParentChild)
				targetPanel = hierarchy.getAppliedToPanel();

			if (targetPanel != null){
				for (Next next : VisibleClassUtil.containedNexts(targetPanel)){
					if (!ret.contains(next.getTargetPanel()))
						ret.add((StandardPanel) next.getTargetPanel());
				}
			}
		}
		return ret;
	}

	/**
	 * Finds all parent-child panels containing the given list of panels
	 * Checks for mutual references between parent-child panels (which are prohibited)
	 * @param panel Parent-child panel
	 * @param linkedPanels List of panels needed to be contained
	 * @return List of parent-child panels containing the given list of panels
	 */
	private static List<ParentChild> allParentChildPanelsContainingPanels(
							ParentChild panel, List<VisibleClass> linkedPanels){
		
		List<ParentChild> ret = new ArrayList<ParentChild>();

		List<ParentChild> allParentChildPanels = new ArrayList<ParentChild>();
		getAllParentChildPanels(BusinessSubsystemUtil.getProject(panel.umlPackage()), allParentChildPanels);
		allParentChildPanels.remove(panel);

		for (ParentChild parentChild : allParentChildPanels){
			//parent child panels cannot reference each other 
			boolean add = false;
			for (Hierarchy hierarchy : VisibleClassUtil.containedHierarchies(parentChild)){
				if (hierarchy.getTargetPanel() == panel){
					add = false;
					break;
				}
				if (linkedPanels.contains(hierarchy.getTargetPanel())){
					add = true;
				}
			}
			if (add)
				ret.add(parentChild);
		}
		return ret;
	}

	/**
	 * Finds all parent-child panels contained by a package and its nested packages
	 * @param umlPackage Package
	 * @param ret All contained parent-child panels
	 */
	private static void getAllParentChildPanels(UmlPackage umlPackage, List<ParentChild> ret){
		for (UmlType umlType : umlPackage.ownedType())
			if (umlType instanceof ParentChild)
				ret.add((ParentChild)umlType);
		for (UmlPackage containedPackage : umlPackage.nestedPackage())
			getAllParentChildPanels(containedPackage, ret);
	}

	/**
	 * Finds all panels which can be used to set values of the applied to property
	 * @param panel Parent-child panel
	 * @param parentChild Parent parent-child panel
	 * @return All possible applied to panels
	 */
	public static List<VisibleClass> getPossibleAppliedToPanels(ParentChild panel, ParentChild parentChild){
		List<VisibleClass> ret = new ArrayList<VisibleClass>();
		List<VisibleClass> containedPanels = allContainedPanels(parentChild, true);
		for (VisibleClass sp : possibleTargetStandardPanels(panel)){
			if (containedPanels.contains(sp))
				ret.add(sp);
		}
		return ret;
	}	

	/**
	 * Checks which hierarchies can be the given hierarchy's parent
	 * @param hierarchy Hierarchy
	 * @param level Hierarchy level
	 * @param thisPanel Parent-child panel
	 * @return All possible parents
	 */
	public static List<Hierarchy> possibleParents(ParentChild thisPanel, Hierarchy hierarchy, int level){
		if (hierarchy.getTargetPanel() == null)
			return null;
		List<Hierarchy> ret = new ArrayList<Hierarchy>();
		List<Hierarchy> successors = HierarchyUtil.allSuccessors(hierarchy);


		//if it is a parent-child panel, check for applied to
		VisibleClass panel = hierarchy.getTargetPanel();

		if (panel instanceof ParentChild)
			panel = hierarchy.getAppliedToPanel();

		List<Zoom> associations = VisibleClassUtil.containedZooms(panel);
		List<VisibleClass> linkedPanels = new ArrayList<VisibleClass>();
		for (Zoom zoom : associations){
			linkedPanels.add(zoom.getTargetPanel());
		}

		for (Hierarchy containedHierarchy : VisibleClassUtil.containedHierarchies(thisPanel)){
			if (successors.contains(containedHierarchy))
				continue;
			if (containedHierarchy != hierarchy && containedHierarchy.getLevel()!=-1 && (level == -1 || containedHierarchy.getLevel() == level)){
				if (containedHierarchy.getTargetPanel() instanceof ParentChild){
					if (containedHierarchy.getAppliedToPanel() == null)
						continue;
					if (linkedPanels.contains(containedHierarchy.getAppliedToPanel()))
						ret.add(containedHierarchy);
				}

				else if (linkedPanels.contains(containedHierarchy.getTargetPanel()))
					ret.add(containedHierarchy);
			}
		}
		return ret;
	}

	/**
	 * Finds all possible association ends for the given hierarchy at the given level
	 * @param thisPanel Parent-child panel
	 * @param hierarchy Hierarchy
	 * @param level Hierarchy's level
	 * @return All possible association ends
	 */
	public static List<VisibleAssociationEnd> possibleAssociationEnds(ParentChild thisPanel, Hierarchy hierarchy, int level){
		List<VisibleAssociationEnd> ret = new ArrayList<VisibleAssociationEnd>();

		if (hierarchy.getTargetPanel() == null)
			return null;

		VisibleClass panel = hierarchy.getTargetPanel();
		List<VisibleClass> possiblePanelParents = possiblePanelParents(thisPanel, hierarchy, level);

		//if there is a navigable association whose cardinality is 1 or less
		//=> these is a zoom to that panel

		List<Zoom> associations = VisibleClassUtil.containedZooms(panel);
		for (Zoom zoom : associations){
			if (possiblePanelParents.contains(zoom.getTargetPanel()))
				ret.add(zoom);
		}
		return ret;
	}

	/**
	 * Fins all possible association ends for the given hierarchy, whose parent and target panels are not null
	 * @param hierarchy Hierarchy
	 * @param panel Parent-child panel
	 * @return All possible association ends
	 */
	public static List<VisibleAssociationEnd> possibleAssociationEnds(ParentChild panel, Hierarchy hierarchy){
		if (hierarchy.getTargetPanel() == null || hierarchy.getHierarchyParent() == null)
			return null;


		VisibleClass childPanel = hierarchy.getTargetPanel();
		//if it is a parent-child panel, use applied to values
		if (childPanel instanceof ParentChild)
			childPanel = hierarchy.getAppliedToPanel();
		if (childPanel == null)
			return null;

		VisibleClass parentPanel = hierarchy.getHierarchyParent().getTargetPanel();
		if (parentPanel instanceof ParentChild)
			parentPanel = hierarchy.getHierarchyParent().getAppliedToPanel();
		if (parentPanel == null)
			return null;


		List<VisibleAssociationEnd> ret = new ArrayList<VisibleAssociationEnd>();


		List<Zoom> associations = VisibleClassUtil.containedZooms(childPanel);

		for (Zoom zoom : associations){
			if (zoom.getTargetPanel() == parentPanel)
				ret.add(zoom);
		}
		return ret;

	}



	/**
	 * Finds all possible parent for the given hierarchy at the given level
	 * @param panel Parent-child panel
	 * @param hierarchy Hierarchy
	 * @param level Hierarchy's level
	 * @return All possible hierarchy parent
	 */
	public static List<VisibleClass> possiblePanelParents(ParentChild panel, Hierarchy hierarchy, int level){
		if (hierarchy.getTargetPanel() == null)
			return null;
		List<VisibleClass> ret = new ArrayList<VisibleClass>();

		for (Hierarchy h : possibleParents(panel, hierarchy, level))
			ret.add(h.getTargetPanel());

		return ret;
	}



	/** 
	 * Check at which level a hierarchy can be located having in mind which panel is its target panel
	 * @param hierarchy Hierarchy
	 * @param panel Parent-child panel
	 * @return Possible levels
	 */
	public static Vector<Integer> possibleLevels(ParentChild panel, Hierarchy hierarchy){

		List<Hierarchy> possibleParents = possibleParents(panel, hierarchy, -1);

		if (possibleParents == null)
			return null;
		Vector<Integer> ret = new Vector<Integer>();

		for (Hierarchy containedHierarchy : possibleParents){
			if (containedHierarchy == hierarchy)
				continue;
			boolean contains = false;
			for (Integer i : ret)
				if ( i == containedHierarchy.getLevel()+1){
					contains = true;
					break;
				}
			if (!contains){
				ret.add(containedHierarchy.getLevel()+1);
			}
		}
		return ret;
	}

	
	/**
	 * Updates panel's hierarchies once the value of the target panel property
	 *  of the given hierarchy has been updated
	 * @param panel Parent-child panel
	 * @param h Hierarchy
	 * @param newTarget New target panel
	 */
	public static void updateTargetPanel(ParentChild panel, Hierarchy h, VisibleClass newTarget){
		
		List<ParentChild> parentChildList = new ArrayList<ParentChild>(); 
		getAllParentChildPanels(BusinessSubsystemUtil.getProject(panel.umlPackage()), parentChildList);
		for (ParentChild parentChild : parentChildList)
			for (Hierarchy containedHierarchy : VisibleClassUtil.containedHierarchies(parentChild))
				if (containedHierarchy.getTargetPanel() == panel && containedHierarchy.getAppliedToPanel() == h.getTargetPanel()){
					HierarchyUtil.updateTargetPanel(containedHierarchy, null);
				}
		
		HierarchyUtil.updateTargetPanel(h, newTarget);
	}


}
