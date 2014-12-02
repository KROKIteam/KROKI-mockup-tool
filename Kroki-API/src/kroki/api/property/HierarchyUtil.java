package kroki.api.property;

import java.util.ArrayList;
import java.util.List;

import kroki.api.panel.ParentChildUtil;
import kroki.api.panel.VisibleClassUtil;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;

public class HierarchyUtil {

	public static void updateAppliedTo (Hierarchy hierarchy, VisibleClass newAppliedTo){

		hierarchy.setAppliedToPanel(newAppliedTo);

		//check children, leave everything as is in cases when child's target panel is linked to neAppliedTo
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
	

	public static void reset(Hierarchy hierarchy){
		hierarchy.setHierarchyParent(null);
		hierarchy.setLevel(-1);
		hierarchy.setViaAssociationEnd(null);
		hierarchy.setTargetPanel(null);
		hierarchy.setAppliedToPanel(null);
		hierarchy.forceUpdateComponent();
	}
	
	public static List<Hierarchy> childHierarchies(Hierarchy hierarchy){
		List<Hierarchy> ret = new ArrayList<Hierarchy>();
		ParentChild panel = (ParentChild)hierarchy.umlClass();
		for (Hierarchy h : VisibleClassUtil.containedHierarchies(panel))
			if (h.getHierarchyParent() == hierarchy)
				ret.add(h);
		return ret;
	}
	
	public static List<Hierarchy> allSuccessors(Hierarchy hierarchy){
		List<Hierarchy> ret = new ArrayList<Hierarchy>();
		allSuccessors(ret, hierarchy);
		return ret;
	}

	public static void allSuccessors(List<Hierarchy> ret, Hierarchy hierarchy){
		List<Hierarchy> childHierarcies = childHierarchies(hierarchy);
		ret.addAll(childHierarcies);
		for (Hierarchy h : childHierarcies)
			allSuccessors(ret, h);
	}

	public static void changeLevel (Hierarchy hierarchy, int newLevel){
		int oldLevel = hierarchy.getLevel();
		int diff = oldLevel - newLevel;

		hierarchy.setLevel(newLevel);
		List<Hierarchy> successors = allSuccessors(hierarchy);

		for (Hierarchy h : successors)
			h.setLevel(h.getLevel() - diff);
	} 	
	
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
			//ako ima dva ili vise elemenata
			//u pocetku ce biti nedefinisan dok se ne izabere targetPanel 
			//(prikikom cega ce se odrediti level na osnovu uzajamnih veza izmedju elemenata hijerarhije
			hierarchy.setLevel(-1);
		}
	}

}
