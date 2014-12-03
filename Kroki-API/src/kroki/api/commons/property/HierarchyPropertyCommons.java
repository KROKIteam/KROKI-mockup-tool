package kroki.api.commons.property;

import java.util.List;

import kroki.api.profil.group.ElementsGroupUtil;
import kroki.api.profil.panel.ParentChildUtil;
import kroki.api.profil.property.HierarchyUtil;
import kroki.api.profil.property.UIPropertyUtil;
import kroki.api.util.Util;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;

public class HierarchyPropertyCommons {

	
	public static void addHierarchyElement(VisibleClass visibleClass, Hierarchy hierarchy, VisibleClass targetPanel){
		hierarchy.setActivationPanel(visibleClass);
		UIPropertyUtil.addVisibleElement(visibleClass, hierarchy);
		hierarchy.setTargetPanel(targetPanel);


		if (!(targetPanel instanceof ParentChild)){
			List<Hierarchy> possibleParents = ParentChildUtil.possibleParents((ParentChild)visibleClass, hierarchy, hierarchy.getLevel() - 1);
			if (possibleParents != null  && possibleParents.size() >= 1){ 
				hierarchy.setHierarchyParent(possibleParents.get(0)); //set the first one by default, users can change it in mockup editor
				hierarchy.setLevel(possibleParents.get(0).getLevel() + 1);
				List<VisibleAssociationEnd> possibleEnds = ParentChildUtil.possibleAssociationEnds((ParentChild)visibleClass, hierarchy);
				if (possibleEnds != null  && possibleEnds.size() >= 1)
					hierarchy.setViaAssociationEnd(possibleEnds.get(0));
			}
		}

		
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.PARENTCHILD_PANEL_PROPERTIES);
		ElementsGroupUtil.addVisibleElement(gr, hierarchy);

		hierarchy.setParentGroup(gr);
	     //  element.getComponent().setAbsolutePosition(point);
	        
		gr.update();
		visibleClass.update();
		HierarchyUtil.forceUpdateComponent(hierarchy);
	}

}
