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

public class ParentChildUtil {


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
	
	

	/**************/
	/*JAVNE METODE*/
	/**************/
	/**VraÄ‡a sve elemente koji predstavljaju deo hijerarhijske strukture*/
	public static List<Hierarchy> allContainedHierarchies(ParentChild panel) {
		List<Hierarchy> allContainedHierarchies = new ArrayList<Hierarchy>();
		for (VisibleElement visibleElement : panel.getVisibleElementList()) {
			if (visibleElement instanceof Hierarchy) {
				allContainedHierarchies.add((Hierarchy) visibleElement);
			}
		}
		return allContainedHierarchies;
	}

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

	/**VraÄ‡a broj elemenata hijerarhijske strukture*/
	public static int getHierarchyCount(ParentChild panel) {
		int i = 0;
		for (VisibleElement visibleElement : panel.getVisibleElementList()) {
			if (visibleElement instanceof Hierarchy) {
				i++;
			}
		}
		return i;
	}

	/**VraÄ‡a koren hijerarhije. Ukoliko ga nema vraÄ‡a null.*/
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

	/**Pronalazi sve hijerarhije Ä�iji nivo je jednak prosleÄ‘enom parametru <code>level</code>*/
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

	public static List<VisibleClass> getAllPosibleTargetPanels(ParentChild panel){
		List<VisibleClass> ret = new ArrayList<VisibleClass>();

		List<VisibleClass> standardPanels = possibleTargetStandardPanels(panel);
		ret.addAll(standardPanels);

		//add parent child panels that contain found standard panels

		ret.addAll(allParentChildPanelsContainingPanels(panel, standardPanels));
		return ret;

	}

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

	private static List<ParentChild> allParentChildPanelsContainingPanels(
							ParentChild panel, List<VisibleClass> linkedPanels){
		
		List<ParentChild> ret = new ArrayList<ParentChild>();

		List<ParentChild> allParentChildPanels = new ArrayList<ParentChild>();
		getAllParentChildPanels(panel, getProject(panel, panel.umlPackage()), allParentChildPanels);
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


	private static void getAllParentChildPanels(ParentChild panel, UmlPackage umlPackage, List<ParentChild> ret){
		for (UmlType umlType : umlPackage.ownedType())
			if (umlType instanceof ParentChild)
				ret.add((ParentChild)umlType);
		for (UmlPackage containedPackage : umlPackage.nestedPackage())
			getAllParentChildPanels(panel, containedPackage, ret);
	}

	private static UmlPackage getProject(ParentChild panel, UmlPackage umlPackage){
		if (umlPackage.nestingPackage() == null)
			return umlPackage;
		return getProject(panel, umlPackage.nestingPackage());
	}

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
	 * Proverava koje hijerarhije mogu biti parent za prosledjenu
	 * @param hierarchy
	 * @return
	 */
	public static List<Hierarchy> possibleParents(ParentChild thisPanel, Hierarchy hierarchy, int level){
		if (hierarchy.getTargetPanel() == null)
			return null;
		List<Hierarchy> ret = new ArrayList<Hierarchy>();
		List<Hierarchy> successors = HierarchyUtil.allSuccessors(hierarchy);


		//ako je parent child, proveriti za applied to
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

	public static List<VisibleAssociationEnd> possibleAssociationEnds(ParentChild thisPanel, Hierarchy hierarchy, int level){
		List<VisibleAssociationEnd> ret = new ArrayList<VisibleAssociationEnd>();

		if (hierarchy.getTargetPanel() == null)
			return null;

		VisibleClass panel = hierarchy.getTargetPanel();
		List<VisibleClass> possiblePanelParents = possiblePanelParents(thisPanel, hierarchy, level);

		//postoji navigabilna asocijacija gde je kardinalitet tog panela ne veci od 1
		//=> ima  zoom ka tom panelu

		List<Zoom> associations = VisibleClassUtil.containedZooms(panel);
		for (Zoom zoom : associations){
			if (possiblePanelParents.contains(zoom.getTargetPanel()))
				ret.add(zoom);
		}
		return ret;
	}

	/**
	 * Nalazi mogu ce krajeve asocijacije, kada su poznati i targeg i hierarchy parent
	 * @param hierarchy
	 * @return
	 */
	public static List<VisibleAssociationEnd> possibleAssociationEnds(ParentChild panel, Hierarchy hierarchy){
		if (hierarchy.getTargetPanel() == null || hierarchy.getHierarchyParent() == null)
			return null;


		VisibleClass childPanel = hierarchy.getTargetPanel();
		//ako je parent child, uzmi applied to 
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



	public static List<VisibleClass> possiblePanelParents(ParentChild panel, Hierarchy hierarchy, int level){
		if (hierarchy.getTargetPanel() == null)
			return null;
		List<VisibleClass> ret = new ArrayList<VisibleClass>();

		for (Hierarchy h : possibleParents(panel, hierarchy, level))
			ret.add(h.getTargetPanel());

		return ret;
	}



	/** 
	 * Proverava na kom se nivoi moze nalaziti hijerarhija imajuci u vidu koji je target panel
	 * @param hierarchy
	 * @return
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

	

	public static void updateTargetPanel(ParentChild panel, Hierarchy h, VisibleClass newTarget){
		
		List<ParentChild> parentChildList = new ArrayList<ParentChild>(); 
		getAllParentChildPanels(panel, getProject(panel, panel.umlPackage()), parentChildList);
		for (ParentChild parentChild : parentChildList)
			for (Hierarchy containedHierarchy : VisibleClassUtil.containedHierarchies(parentChild))
				if (containedHierarchy.getTargetPanel() == panel && containedHierarchy.getAppliedToPanel() == h.getTargetPanel()){
					HierarchyUtil.updateTargetPanel(containedHierarchy, null);
				}
		
		HierarchyUtil.updateTargetPanel(h, newTarget);
	}


}
