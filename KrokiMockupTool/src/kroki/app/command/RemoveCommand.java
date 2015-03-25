package kroki.app.command;

import java.util.ArrayList;
import java.util.List;

import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.utils.ElementsGroupUtil;
import kroki.profil.utils.HierarchyUtil;
import kroki.profil.utils.UIPropertyUtil;
import kroki.profil.utils.VisibleClassUtil;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlProperty;

/**
 * Command for removing elements
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 * @author Renata
 */
public class RemoveCommand implements Command {

	private List<VisibleElement> visibleElementList;
	private List<Integer> groupIndexes;
	private List<Hierarchy> hierarchies = new ArrayList<Hierarchy>();

	public RemoveCommand(List<VisibleElement> visibleElementList) {
		this.visibleElementList = new ArrayList<VisibleElement>();
		this.visibleElementList.addAll(visibleElementList);

		groupIndexes = new ArrayList<Integer>();
		VisibleClass visibleClass;
		ElementsGroup elementsGroup;
		for (VisibleElement visibleElement : visibleElementList){
			visibleClass = getVisibleClass(visibleElement);
			if (visibleClass == null)
				continue;
			if (visibleElement instanceof Hierarchy){
				hierarchies.add((Hierarchy)visibleElement);
				continue;
			}
			elementsGroup = visibleElement.getParentGroup();
			groupIndexes.add(elementsGroup.getVisibleElementList().indexOf(visibleElement));
		}

		ParentChild panel = null;
		List<Hierarchy> currentSuccessors = new ArrayList<Hierarchy>();
		this.visibleElementList.removeAll(hierarchies);

		for (Hierarchy hierarchy : hierarchies){
			//delete hierarchy and all successors

			if (panel == null)
				panel = (ParentChild)hierarchy.umlClass();
			currentSuccessors.clear();
			currentSuccessors = HierarchyUtil.allSuccessors(hierarchy);
			currentSuccessors.add(0,hierarchy);

			for (Hierarchy h : currentSuccessors)
				if (!this.visibleElementList.contains(h)){
					this.visibleElementList.add(h);
					elementsGroup = hierarchy.getParentGroup();
					groupIndexes.add(elementsGroup.getVisibleElementList().indexOf(hierarchy));
				}
		}

	}

	public void doCommand() {
		VisibleClass visibleClass;
		ElementsGroup elementsGroup;
		for (VisibleElement visibleElement : visibleElementList){
			visibleClass = getVisibleClass(visibleElement);
			if (visibleClass == null)
				continue;

			if (visibleElement instanceof VisibleAssociationEnd){
				VisibleAssociationEnd end = (VisibleAssociationEnd)visibleElement;
				if (end.opposite() != null){
					//change opposite
					VisibleAssociationEnd oppositeEnd = (VisibleAssociationEnd) end.opposite();
					oppositeEnd.setOpposite(null);
					if (end instanceof Zoom){
						oppositeEnd.setTargetPanel(null);
					}

				}
			}
			elementsGroup = visibleElement.getParentGroup();
			if (elementsGroup != null) {
				ElementsGroupUtil.removeVisibleElement(elementsGroup, visibleElement);
				elementsGroup.update();
			}
			if (visibleClass != null) {
				UIPropertyUtil.removeVisibleElement(visibleClass, visibleElement);
				visibleClass.update();
			}
			
			
            if (visibleElement.getComponentType() != null){
				VisibleClassUtil.decrementCount(visibleClass, visibleElement.getComponentType());
			}
		}
	}

	public void undoCommand() {
		VisibleClass visibleClass;
		ElementsGroup elementsGroup;
		int index = 0;

		//Should add elements at their original positions
		//add all elements first, to avoid exceptions


		for (VisibleElement  visibleElement : visibleElementList){

			//update opposite
			if (visibleElement instanceof VisibleAssociationEnd){
				VisibleAssociationEnd end = (VisibleAssociationEnd)visibleElement;
				if (end.opposite() != null){
					//change opposite
					VisibleAssociationEnd oppositeEnd = (VisibleAssociationEnd) end.opposite();
					oppositeEnd.setOpposite(end);
					oppositeEnd.setTargetPanel(end.getActivationPanel());

				}
			}

			elementsGroup = visibleElement.getParentGroup();
			visibleClass = getVisibleClass(visibleElement);
			if (visibleClass == null)
				continue;
			if (elementsGroup != null) 
				ElementsGroupUtil.addVisibleElement(elementsGroup, visibleElement);

			if (visibleClass != null) 
				UIPropertyUtil.addVisibleElement(visibleClass, visibleElement);
		}


		for (VisibleElement visibleElement : visibleElementList){
			visibleClass = getVisibleClass(visibleElement);
			if (visibleClass == null)
				continue;

			elementsGroup = visibleElement.getParentGroup();
			int groupIndex = groupIndexes.get(index ++);

			if (elementsGroup != null) {
				ElementsGroupUtil.removeVisibleElement(elementsGroup, visibleElement);
				ElementsGroupUtil.addVisibleElement(elementsGroup, groupIndex, visibleElement);
				elementsGroup.update();
			}
			

            if (visibleElement.getComponentType() != null){
				VisibleClassUtil.incrementCount(visibleClass, visibleElement.getComponentType());
			}

		}

	}

	private VisibleClass getVisibleClass(VisibleElement visibleElement){
		if (visibleElement instanceof UmlProperty) 
			return (VisibleClass) ((UmlProperty) visibleElement).umlClass();
		else if (visibleElement instanceof UmlOperation) 
			return (VisibleClass) ((UmlOperation) visibleElement).umlClass();
		else
			return null;
	}
}
