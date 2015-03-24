package kroki.profil.utils;

import java.util.ArrayList;
import java.util.List;

import kroki.common.copy.DeepCopy;
import kroki.mockup.model.Composite;
import kroki.mockup.model.components.NullComponent;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;

/**
 * Class contains <code>Hierarchy</code> util methods 
 * @author Kroki Team
 */
public class HierarchyUtil {

	/**
	 * Updates the hierarchy and its contained (child) hierarchies when the applied property is changed
	 * @param hierarchy Hierarchy
	 * @param newAppliedTo The new value of applied to property 
	 */
	public static void updateAppliedTo (Hierarchy hierarchy, VisibleClass newAppliedTo){

		hierarchy.setAppliedToPanel(newAppliedTo);

		//check children, leave everything as is in cases when child's target panel is linked to newAppliedTo
		//in other cases, reset hierarchies

		VisibleClass panel;
		for (Hierarchy h : childHierarchies(hierarchy)){
			boolean reset = false;
			if (newAppliedTo != null){
				panel = h.getTargetPanel();
				if (panel instanceof ParentChild)
					panel = h.getAppliedToPanel();
				Zoom associationEnd = null;
				boolean contains = false;
				for (Zoom zoom : VisibleClassUtil.containedZooms(panel))
					if (zoom.getTargetPanel() == newAppliedTo){
						contains = true;
						if (associationEnd == null)
							associationEnd = zoom;
						else{
							associationEnd = null;
							break;
						}
					}
				if (contains){
					h.setViaAssociationEnd(associationEnd);
				}
				else
					reset = true;
			}
			else reset = true;

			if (reset){
				reset(h);
				for (Hierarchy child : allSuccessors(h))
					reset(child);
			}
		}
	}

	/**
	 * Updates the hierarchy and its contained (child) hierarchies when the target panel property is changed
	 * @param hierarchy Hierarchy
	 * @param newTarget New value of target panel property
	 */
	public static void updateTargetPanel(Hierarchy hierarchy, VisibleClass newTarget){

		hierarchy.setTargetPanel(newTarget);

		//check children, leave everything as is in cases when child's target panel is linked to newTarget
		//in other cases, reset hierarchies

		VisibleClass panel;
		for (Hierarchy h : childHierarchies(hierarchy)){
			boolean reset = false;

			if (newTarget != null && newTarget instanceof StandardPanel){
				panel = h.getTargetPanel();
				if (panel instanceof ParentChild)
					panel = h.getAppliedToPanel();
				Zoom associationEnd = null;
				boolean contains = false;
				for (Zoom zoom : VisibleClassUtil.containedZooms(panel))
					if (zoom.getTargetPanel() == newTarget){
						contains = true;
						if (associationEnd == null)
							associationEnd = zoom;
						else{
							associationEnd = null;
							break;
						}
					}
				if (contains){
					h.setViaAssociationEnd(associationEnd);
				}
				else{
					reset = true;
				}
			}
			else{
				reset = true;
			}
			if (reset){
				reset(h);
				for (Hierarchy child : allSuccessors(h))
					reset(child);
			}
		}
		if (newTarget == null)
			reset(hierarchy);
	}
	
	/**
	 * Resets a hierarchy - set properties such as parent, association end, target and applied to panel to null
	 * and level to -1
	 * @param hierarchy Hierarchy
	 */
	public static void reset(Hierarchy hierarchy){
		hierarchy.setHierarchyParent(null);
		hierarchy.setLevel(-1);
		hierarchy.setViaAssociationEnd(null);
		hierarchy.setTargetPanel(null);
		hierarchy.setAppliedToPanel(null);
		forceUpdateComponent(hierarchy);
	}
	
	/**
	 * Return all child hierarchies of the given hierarchy
	 * @param hierarchy Hierarchy
	 * @return List of child hierarchies
	 */
	public static List<Hierarchy> childHierarchies(Hierarchy hierarchy){
		List<Hierarchy> ret = new ArrayList<Hierarchy>();
		ParentChild panel = (ParentChild)hierarchy.umlClass();
		for (Hierarchy h : VisibleClassUtil.containedHierarchies(panel))
			if (h.getHierarchyParent() == hierarchy)
				ret.add(h);
		return ret;
	}
	
	/**
	 * Return all hierarchies directly or indirectly contained by the hierarchy 
	 * i.e. all successors 
	 * @param hierarchy Hierarchy
	 * @return List of successor hierarchies
	 */
	public static List<Hierarchy> allSuccessors(Hierarchy hierarchy){
		List<Hierarchy> ret = new ArrayList<Hierarchy>();
		allSuccessors(ret, hierarchy);
		return ret;
	}

	/**
	 * Return all hierarchies directly or indirectly contained by the hierarchy 
	 * i.e. all successors 
	 * @param hierarchy Hierarchy
	 * @param ret List containing the results
	 */
	public static void allSuccessors(List<Hierarchy> ret, Hierarchy hierarchy){
		List<Hierarchy> childHierarcies = childHierarchies(hierarchy);
		ret.addAll(childHierarcies);
		for (Hierarchy h : childHierarcies)
			allSuccessors(ret, h);
	}

	/**
	 * Changes level of the given hierarchy
	 * @param hierarchy Hierarchy
	 * @param newLevel New level
	 */
	public static void changeLevel (Hierarchy hierarchy, int newLevel){
		int oldLevel = hierarchy.getLevel();
		int diff = oldLevel - newLevel;

		hierarchy.setLevel(newLevel);
		List<Hierarchy> successors = allSuccessors(hierarchy);

		for (Hierarchy h : successors)
			h.setLevel(h.getLevel() - diff);
	} 	
	
	/**
	 * Updates the hierarchy parent property of the given hierarchy 
	 * @param hierarchy Hierarchy 
	 * @param hierarchyParent New hierarchy parent
	 */
	public static void updateParent(Hierarchy hierarchy, Hierarchy hierarchyParent) {
		hierarchy.setHierarchyParent(hierarchyParent);
		if (hierarchyParent != null) {
			hierarchy.setLevel(hierarchy.getHierarchyParent().getLevel() + 1);
			ParentChild panel = (ParentChild) hierarchy.umlClass();
			List<VisibleAssociationEnd> viaAssociationEnd = ParentChildUtil.possibleAssociationEnds(panel, hierarchy);
			if (viaAssociationEnd != null && viaAssociationEnd.size() == 1)
				hierarchy.setViaAssociationEnd(viaAssociationEnd.get(0));
		}
		else{
			hierarchy.setLevel(-1);
			hierarchy.setViaAssociationEnd(null);
		}
	}

	/**
	 * Set level and hierarchy parent of the hierarchy
	 * @param panel Parent-child panel containing the hierarchy
	 * @param hierarchy Hierarchy
	 */
	public static void setHierarchhyAttributes(ParentChild panel, Hierarchy hierarchy){
		if (hierarchy.getLevel() != 0)
			return;
		int count = ParentChildUtil.getHierarchyCount(panel);
		if (count == 0) {
			hierarchy.setLevel(1);
			hierarchy.setHierarchyParent(null);
		} else if (count == 1) {
			hierarchy.setLevel(2);
			hierarchy.setHierarchyParent(ParentChildUtil.getHierarchyRoot(panel));
		} else {
			//if the panel has two or more element
			//hierarchy panel and level will stay undefined until target panel is picked
			//(these attributes will then be determined based on relationships between elements
			//of the hierarchy)
			hierarchy.setLevel(-1);
		}
	}
	
	/**
	 * Updates graphical component connected with the hierarchy
	 * @param hierarchy Hierarchy
	 */
	public static void forceUpdateComponent(Hierarchy hierarchy) {

		int relX = hierarchy.getComponent().getRelativePosition().x;
		int relY = hierarchy.getComponent().getRelativePosition().y;
		int absX = hierarchy.getComponent().getAbsolutePosition().x;
		int absY = hierarchy.getComponent().getAbsolutePosition().y;
		((Composite) hierarchy.getParentGroup().getComponent()).removeChild(hierarchy.getComponent());

		if (hierarchy.getTargetPanel() != null) {
			//cloning the whole panel in order to visualize it
			 hierarchy.setTargetPanelClone((VisibleClass) DeepCopy.copy(hierarchy.getTargetPanel()));

			 //if some of the actions are forbidden within the target panel hey cannot be permitted
			 //by the assocation end
			if (hierarchy.getTargetPanelClone() instanceof StandardPanel) {
				StandardPanel panel = (StandardPanel)hierarchy.getTargetPanel();
				hierarchy.setAdd(panel.isAdd());
				hierarchy.setChangeMode(panel.isChangeMode());
				hierarchy.setCopy(panel.isCopy());
				hierarchy.setDataNavigation(panel.isDataNavigation());
				hierarchy.setDelete(panel.isDelete());
				hierarchy.setSearch(panel.isSearch());
				hierarchy.setUpdate(panel.isUpdate());
			}
			hierarchy.setComponent(hierarchy.getTargetPanelClone().getComponent());
			hierarchy.setLabel(hierarchy.getTargetPanel().getLabel());
		}
		else{
			hierarchy.setLabel("Hierarchy");
			hierarchy.setComponent(new NullComponent(hierarchy.getLabel()));
			hierarchy.setTargetPanelClone(null);
		}


		hierarchy.getComponent().getAbsolutePosition().setLocation(absX, absY);
		hierarchy.getComponent().getRelativePosition().setLocation(relX, relY);
		((Composite) hierarchy.getParentGroup().getComponent()).addChild(hierarchy.getComponent());


	}


}
