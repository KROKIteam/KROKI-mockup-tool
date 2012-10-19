/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.command;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;

/**
 * Komanda pomeranja elemenata.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class MoveCommand implements Command {

    /**Lista elementata koja je pomerena*/
    private List<VisibleElement> visibleElementList;
    /**Pomeraj po x osi*/
    private int dx;
    /**Pomeraj po y osi*/
    private int dy;

    public MoveCommand(List<VisibleElement> visibleElementList, int dx, int dy) {
        this.visibleElementList = new ArrayList<VisibleElement>();
        this.visibleElementList.addAll(visibleElementList);
        this.dx = dx;
        this.dy = dy;
    }

    /***********************/
    /*IMPLEMENTIRANE METODE*/
    /***********************/
    public void doCommand() {
        for (int i = 0; i < visibleElementList.size(); i++) {
            VisibleElement visibleElement = visibleElementList.get(i);
            Point elemAbsPos = (Point) visibleElement.getComponent().getAbsolutePosition().clone();
            Point newPos = new Point(elemAbsPos.x + dx, elemAbsPos.y + dy);
            visibleElement.getComponent().setAbsolutePosition(newPos);
            if (visibleElement instanceof ElementsGroup) {
                if (((ElementsGroup) visibleElement).getVisibleElementsNum() > 0) {
                    dragElementsGroup((ElementsGroup) visibleElement, dx, dy);
                }
            }
            visibleElement.update();
        }
    }

    public void undoCommand() {
        for (int i = 0; i < visibleElementList.size(); i++) {
            VisibleElement visibleElement = visibleElementList.get(i);
            Point elemAbsPos = (Point) visibleElement.getComponent().getAbsolutePosition().clone();
            Point newPos = new Point(elemAbsPos.x - dx, elemAbsPos.y - dy);
            visibleElement.getComponent().setAbsolutePosition(newPos);
            if (visibleElement instanceof ElementsGroup) {
                if (((ElementsGroup) visibleElement).getVisibleElementsNum() > 0) {
                    dragElementsGroup((ElementsGroup) visibleElement, -dx, -dy);
                }
            }
            visibleElement.update();
        }
    }

    /*****************/
    /*PRIVATNE METODE*/
    /*****************/
    private void dragElementsGroup(ElementsGroup elementsGroup, int dx, int dy) {
        for (int i = 0; i < elementsGroup.getVisibleElementsNum(); i++) {
            VisibleElement visibleElement = elementsGroup.getVisibleElementAt(i);
            Point elemAbsPos = (Point) visibleElement.getComponent().getAbsolutePosition().clone();
            Point newPos = new Point(elemAbsPos.x + dx, elemAbsPos.y + dy);
            visibleElement.getComponent().setAbsolutePosition(newPos);
            if (visibleElement instanceof ElementsGroup) {
                dragElementsGroup((ElementsGroup) visibleElement, dx, dy);
            }
            visibleElement.update();
        }
    }
}
