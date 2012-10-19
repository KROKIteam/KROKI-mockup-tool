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
 */
public class RemoveCommand implements Command {

    List<VisibleElement> visibleElementList;

    public RemoveCommand(List<VisibleElement> visibleElementList) {
        this.visibleElementList = new ArrayList<VisibleElement>();
        this.visibleElementList.addAll(visibleElementList);
    }

    public void doCommand() {
        for (VisibleElement visibleElement : visibleElementList) {
            //NEW:
            VisibleClass visibleClass = null;
            if (visibleElement instanceof UmlProperty) {
                visibleClass = (VisibleClass) ((UmlProperty) visibleElement).umlClass();
            } else if (visibleElement instanceof UmlOperation) {
                visibleClass = (VisibleClass) ((UmlOperation) visibleElement).umlClass();
            } else {
                return;
            }
            //VisibleClass visibleClass = visibleElement.getParentPanel();
            ElementsGroup elementsGroup = visibleElement.getParentGroup();
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
        for (VisibleElement visibleElement : visibleElementList) {
            //NEW:
            VisibleClass visibleClass = null;
            if (visibleElement instanceof UmlProperty) {
                visibleClass = (VisibleClass) ((UmlProperty) visibleElement).umlClass();
            } else if (visibleElement instanceof UmlOperation) {
                visibleClass = (VisibleClass) ((UmlOperation) visibleElement).umlClass();
            } else {
                return;
            }
            //VisibleClass visibleClass = visibleElement.getParentPanel();
            ElementsGroup elementsGroup = visibleElement.getParentGroup();
            if (elementsGroup != null) {
                elementsGroup.addVisibleElement(visibleElement);
                elementsGroup.update();
            }
            if (visibleClass != null) {
                visibleClass.addVisibleElement(visibleElement);
                visibleClass.update();
            }
        }
    }
}
