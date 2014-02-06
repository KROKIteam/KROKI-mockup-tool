/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.command;

import java.util.ArrayList;
import java.util.List;

import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlProperty;

/**
 * Komanda brisanja elementa
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 * @author Renata
 */
public class RemoveCommand implements Command {

	private List<VisibleElement> visibleElementList;
	private List<Integer> classIndexes, groupIndexes;

	public RemoveCommand(List<VisibleElement> visibleElementList) {
		this.visibleElementList = new ArrayList<VisibleElement>();
		this.visibleElementList.addAll(visibleElementList);
		classIndexes = new ArrayList<Integer>();
		groupIndexes = new ArrayList<Integer>();
		VisibleClass visibleClass;
		ElementsGroup elementsGroup;
		for (VisibleElement visibleElement : visibleElementList){
			visibleClass = getVisibleClass(visibleElement);
			if (visibleClass == null)
				continue;
			classIndexes.add(visibleClass.getVisibleElementList().indexOf(visibleElement));
			elementsGroup = visibleElement.getParentGroup();
			groupIndexes.add(elementsGroup.getVisibleElementList().indexOf(visibleElement));
		}
	}

	public void doCommand() {
		VisibleClass visibleClass;
		ElementsGroup elementsGroup;
		for (VisibleElement visibleElement : visibleElementList){
			visibleClass = getVisibleClass(visibleElement);
			if (visibleClass == null)
				continue;
			elementsGroup = visibleElement.getParentGroup();
			if (elementsGroup != null) {
				elementsGroup.removeVisibleElement(visibleElement);
				elementsGroup.update();
			}
			if (visibleClass != null) {
				visibleClass.removeVisibleElement(visibleElement);
				visibleClass.update();
			}
		}
	}

	public void undoCommand() {
		
		//Should add elements at their original positions
		VisibleClass visibleClass;
		ElementsGroup elementsGroup;
		int index = 0;
		
		for (VisibleElement visibleElement : visibleElementList){
			visibleClass = getVisibleClass(visibleElement);
			if (visibleClass == null)
				continue;
			elementsGroup = visibleElement.getParentGroup();
			int classIndex = classIndexes.get(index);
			int groupIndex = groupIndexes.get(index ++);
			
			if (elementsGroup != null) {
				elementsGroup.addVisibleElement(groupIndex, visibleElement);
				elementsGroup.update();
			}
			if (visibleClass != null) {
				visibleClass.addVisibleElement(classIndex, visibleElement);
				visibleClass.update();
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
