/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.command;

import java.awt.Point;
import kroki.app.KrokiMockupToolApp;
import kroki.app.model.SelectionModel;
import kroki.app.view.Canvas;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;

/**
 * Komanda dodavanja elementa
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AddCommand implements Command {

    VisibleClass visibleClass;
    ElementsGroup elementsGroup;
    VisibleElement element;
    Point point;

    public AddCommand(VisibleClass visibleClass, ElementsGroup elementsGroup, VisibleElement element, Point point) {
        this.visibleClass = visibleClass;
        this.elementsGroup = elementsGroup;
        this.element = element;
        this.point = point;
    }

    public void doCommand() {
        visibleClass.addVisibleElement(element);
        elementsGroup.addVisibleElement(element);
        element.getComponent().setAbsolutePosition(point);
        elementsGroup.update();
        visibleClass.update();
    }

    public void undoCommand() {
        Canvas c = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent();
        SelectionModel selectionModel = c.getSelectionModel();
        if (selectionModel.isSelected(element)) {
            selectionModel.removeFromSelection(element);
        }
        visibleClass.removeVisibleElement(element);
        elementsGroup.removeVisibleElement(element);
        elementsGroup.update();
        visibleClass.update();
    }
}
