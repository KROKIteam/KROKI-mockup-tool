/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.command;

import java.awt.Point;
import kroki.app.KrokiMockupToolApp;
import kroki.app.model.SelectionModel;
import kroki.app.view.Canvas;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.VisibleProperty;

/**
 * Komanda dodavanja elementa
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 * @author Renata
 */
public class AddCommand implements Command {

    private VisibleClass visibleClass;
    private ElementsGroup elementsGroup;
    private VisibleElement element;
    private Point point;
    private int classIndex, groupIndex;

    public AddCommand(VisibleClass visibleClass, ElementsGroup elementsGroup, VisibleElement element, Point point) {
        this.visibleClass = visibleClass;
        this.elementsGroup = elementsGroup;
        this.element = element;
        this.point = point;
        classIndex = visibleClass.getVisibleElementList().size();
        groupIndex = elementsGroup.getVisibleElementList().size();
    }

    public void doCommand() {
    	
        visibleClass.addVisibleElement(classIndex, element);
        elementsGroup.addVisibleElement(groupIndex, element);
        element.getComponent().setAbsolutePosition(point);
        if(element instanceof VisibleProperty) {
        	VisibleProperty prop = (VisibleProperty) element;
        	NamingUtil namer = new NamingUtil();
        	prop.setColumnLabel(namer.toDatabaseFormat(visibleClass.getLabel(), element.getLabel()));
        }
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
