/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.panel.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import kroki.mockup.model.Composite;
import kroki.mockup.model.components.TitledContainer;
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
import kroki.profil.panel.ContainerPanel;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;

/**
 * <code>ParentChild</code> modeluje slozeni panel  ciji su
 * sastavni paneli organizovani u stablo, na nacin definisan HCI standardom. 
 * @author Vladan Marsenic (vladan.marsenic@gmail.com)
 * @author Renata
 */
public class ParentChild extends ContainerPanel {
	
	
	private static final long serialVersionUID = 1L;

	private ElementsGroup propertiesPanel;
	private ElementsGroup operationsPanel;

	public ParentChild() {
		super();
		component = new TitledContainer("Parent child");
		component.getRelativePosition().setLocation(5, 5);
		component.getAbsolutePosition().setLocation(5, 5);
		component.getDimension().setSize(800, 500);
	}

	

	@Override
	public void update() {
		component.updateComponent();
		((Composite) component).layout();
	}

	@Override
	public String toString() {
		return label;
	}

	/**************/
	/*JAVNE METODE*/
	/**************/
	/**VraÄ‡a sve elemente koji predstavljaju deo hijerarhijske strukture*/
	public List<Hierarchy> allContainedHierarchies() {
		List<Hierarchy> allContainedHierarchies = new ArrayList<Hierarchy>();
		for (VisibleElement visibleElement : visibleElementList) {
			if (visibleElement instanceof Hierarchy) {
				allContainedHierarchies.add((Hierarchy) visibleElement);
			}
		}
		return allContainedHierarchies;
	}

	public List<VisibleClass> allContainedPanels(boolean onlyStandard){
		List<VisibleClass> allContainedPanels = new ArrayList<VisibleClass>();
		for (VisibleElement visibleElement : visibleElementList) {
			if (visibleElement instanceof Hierarchy) {
				if ((onlyStandard && ((Hierarchy)visibleElement).getTargetPanel() instanceof StandardPanel) 
						|| !onlyStandard)
					allContainedPanels.add(((Hierarchy)visibleElement).getTargetPanel());
			}
		}
		return allContainedPanels;
	}

	/**VraÄ‡a broj elemenata hijerarhijske strukture*/
	public int getHierarchyCount() {
		int i = 0;
		for (VisibleElement visibleElement : visibleElementList) {
			if (visibleElement instanceof Hierarchy) {
				i++;
			}
		}
		return i;
	}

	/**VraÄ‡a koren hijerarhije. Ukoliko ga nema vraÄ‡a null.*/
	public Hierarchy getHierarchyRoot() {
		Hierarchy root = null;
		for (VisibleElement visibleElement : visibleElementList) {
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
	public List<Hierarchy> allHierarchiesByLevel(int level) {
		List<Hierarchy> allHierarchiesByLevel = new ArrayList<Hierarchy>();
		for (VisibleElement visibleElement : visibleElementList) {
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

	public List<VisibleClass> getAllPosibleTargetPanels(){
		List<VisibleClass> ret = new ArrayList<VisibleClass>();

		List<VisibleClass> standardPanels = possibleTargetStandardPanels();
		ret.addAll(standardPanels);

		//add parent child panels that contain found standard panels

		ret.addAll(allParentChildPanelsContainingPanels(standardPanels));
		return ret;

	}

	private List<VisibleClass> possibleTargetStandardPanels(){
		List<VisibleClass> ret = new ArrayList<VisibleClass>();

		//find all next association ends

		for (Hierarchy hierarchy : containedHierarchies()){
			VisibleClass panel = hierarchy.getTargetPanel();
			if (panel != null && panel instanceof ParentChild)
				panel = hierarchy.getAppliedToPanel();

			if (panel != null){
				for (Next next : panel.containedNexts()){
					if (!ret.contains(next.getTargetPanel()))
						ret.add((StandardPanel) next.getTargetPanel());
				}
			}
		}
		return ret;
	}

	private List<ParentChild> allParentChildPanelsContainingPanels(List<VisibleClass> linkedPanels){
		List<ParentChild> ret = new ArrayList<ParentChild>();

		List<ParentChild> allParentChildPanels = new ArrayList<ParentChild>();
		getAllParentChildPanels(getProject(umlPackage), allParentChildPanels);
		allParentChildPanels.remove(this);

		for (ParentChild parentChild : allParentChildPanels){
			//parent child panels cannot reference each other 
			boolean add = false;
			for (Hierarchy hierarchy : parentChild.containedHierarchies()){
				if (hierarchy.getTargetPanel() == this){
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


	private void getAllParentChildPanels(UmlPackage umlPackage, List<ParentChild> ret){
		for (UmlType umlType : umlPackage.ownedType())
			if (umlType instanceof ParentChild)
				ret.add((ParentChild)umlType);
		for (UmlPackage containedPackage : umlPackage.nestedPackage())
			getAllParentChildPanels(containedPackage, ret);
	}

	private UmlPackage getProject(UmlPackage umlPackage){
		if (umlPackage.nestingPackage() == null)
			return umlPackage;
		return getProject(umlPackage.nestingPackage());
	}

	public List<VisibleClass> getPossibleAppliedToPanels(ParentChild parentChild){
		List<VisibleClass> ret = new ArrayList<VisibleClass>();
		List<VisibleClass> containedPanels = parentChild.allContainedPanels(true);
		for (VisibleClass sp : possibleTargetStandardPanels()){
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
	public List<Hierarchy> possibleParents(Hierarchy hierarchy, int level){
		if (hierarchy.getTargetPanel() == null)
			return null;
		List<Hierarchy> ret = new ArrayList<Hierarchy>();
		List<Hierarchy> successors = hierarchy.allSuccessors();


		//ako je parent child, proveriti za applied to
		VisibleClass panel = hierarchy.getTargetPanel();

		if (panel instanceof ParentChild)
			panel = hierarchy.getAppliedToPanel();

		List<Zoom> associations = panel.containedZooms();
		List<VisibleClass> linkedPanels = new ArrayList<VisibleClass>();
		for (Zoom zoom : associations){
			linkedPanels.add(zoom.getTargetPanel());
		}

		for (Hierarchy containedHierarchy : containedHierarchies()){
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

	public List<VisibleAssociationEnd> possibleAssociationEnds(Hierarchy hierarchy, int level){
		List<VisibleAssociationEnd> ret = new ArrayList<VisibleAssociationEnd>();

		if (hierarchy.getTargetPanel() == null)
			return null;

		VisibleClass panel = hierarchy.getTargetPanel();
		List<VisibleClass> possiblePanelParents = possiblePanelParents(hierarchy, level);

		//postoji navigabilna asocijacija gde je kardinalitet tog panela ne veci od 1
		//=> ima  zoom ka tom panelu

		List<Zoom> associations = panel.containedZooms();
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
	public List<VisibleAssociationEnd> possibleAssociationEnds(Hierarchy hierarchy){
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


		List<Zoom> associations = childPanel.containedZooms();

		for (Zoom zoom : associations){
			if (zoom.getTargetPanel() == parentPanel)
				ret.add(zoom);
		}
		return ret;

	}



	public List<VisibleClass> possiblePanelParents(Hierarchy hierarchy, int level){
		if (hierarchy.getTargetPanel() == null)
			return null;
		List<VisibleClass> ret = new ArrayList<VisibleClass>();

		for (Hierarchy h : possibleParents(hierarchy, level))
			ret.add(h.getTargetPanel());

		return ret;
	}



	/** 
	 * Proverava na kom se nivoi moze nalaziti hijerarhija imajuci u vidu koji je target panel
	 * @param hierarchy
	 * @return
	 */
	public Vector<Integer> possibleLevels(Hierarchy hierarchy){

		List<Hierarchy> possibleParents = possibleParents(hierarchy, -1);

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

	

	public void updateTargetPanel(Hierarchy h, VisibleClass newTarget){
		
		List<ParentChild> parentChildList = new ArrayList<ParentChild>(); 
		getAllParentChildPanels(getProject(umlPackage), parentChildList);
		for (ParentChild parentChild : parentChildList)
			for (Hierarchy containedHierarchy : parentChild.containedHierarchies())
				if (containedHierarchy.getTargetPanel() == this && containedHierarchy.getAppliedToPanel() == h.getTargetPanel()){
					containedHierarchy.updateTargetPanel(null);
				}
		
		h.updateTargetPanel(newTarget);
	}

	


	/*******************/
	/**GETERI I SETERI**/
	/*******************/
	public ElementsGroup getOperationsPanel() {
		return operationsPanel;
	}

	public void setOperationsPanel(ElementsGroup operationsPanel) {
		this.operationsPanel = operationsPanel;
	}

	public ElementsGroup getPropertiesPanel() {
		return propertiesPanel;
	}

	public void setPropertiesPanel(ElementsGroup propertiesPanel) {
		this.propertiesPanel = propertiesPanel;
	}
}
