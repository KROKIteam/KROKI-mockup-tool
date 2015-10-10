package kroki.app.command;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.utils.ElementsGroupUtil;

/**
 * Command for moving the elements
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class MoveCommand implements Command {

    /**List of element which are to be moved*/
    private List<VisibleElement> visibleElementList;
    /**x axis distance*/
    private int dx;
    /**y axis distance*/
    private int dy;

    public MoveCommand(List<VisibleElement> visibleElementList, int dx, int dy) {
        this.visibleElementList = new ArrayList<VisibleElement>();
        this.visibleElementList.addAll(visibleElementList);
        this.dx = dx;
        this.dy = dy;
    }

    public void doCommand() {
        for (int i = 0; i < visibleElementList.size(); i++) {
            VisibleElement visibleElement = visibleElementList.get(i);
            Point elemAbsPos = (Point) visibleElement.getComponent().getAbsolutePosition().clone();
            Point newPos = new Point(elemAbsPos.x + dx, elemAbsPos.y + dy);
            visibleElement.getComponent().setAbsolutePosition(newPos);
            if (visibleElement instanceof ElementsGroup) {
                if (ElementsGroupUtil.getVisibleElementsNum((ElementsGroup) visibleElement) > 0) {
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
                if (ElementsGroupUtil.getVisibleElementsNum((ElementsGroup) visibleElement) > 0) {
                    dragElementsGroup((ElementsGroup) visibleElement, -dx, -dy);
                }
            }
            visibleElement.update();
        }
    }

    private void dragElementsGroup(ElementsGroup elementsGroup, int dx, int dy) {
        for (int i = 0; i < ElementsGroupUtil.getVisibleElementsNum(elementsGroup); i++) {
            VisibleElement visibleElement = ElementsGroupUtil.getVisibleElementAt(elementsGroup, i);
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
