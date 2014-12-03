package kroki.api.profil.group;

import java.awt.Point;

import kroki.mockup.model.Composite;
import kroki.mockup.model.layout.FreeLayoutManager;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.group.GroupLocation;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.property.VisibleProperty;

public class ElementsGroupUtil {

	
	  /**
     * Dodaje novi vidljivi element u listu.
     * @param visibleElement vidljivi element
     */
    public static void addVisibleElement(ElementsGroup group,  VisibleElement visibleElement) {
        if (!group.getVisibleElementList().contains(visibleElement)) {
        	group.getVisibleElementList().add(visibleElement);
            visibleElement.setParentGroup(group);
            //NEW:
            //visibleElement.setParentPanel(parentPanel);
            if (visibleElement instanceof ElementsGroup) {
                ((ElementsGroup) visibleElement).setGroupLocation(group.getGroupLocation());
                ((Composite) visibleElement.getComponent()).setLayoutManager(new FreeLayoutManager());
            }
            //na kraju ide dodavanje komponente za iscrtavanje
            ((Composite) group.getComponent()).addChild(visibleElement.getComponent());
        }
    }

    public static void addVisibleElement(ElementsGroup group, int index, VisibleElement visibleElement) {
        if (!group.getVisibleElementList().contains(visibleElement)) {
        	group.getVisibleElementList().add(index, visibleElement);
            visibleElement.setParentGroup(group);
            //NEW:
            //visibleElement.setParentPanel(parentPanel);
            if (visibleElement instanceof ElementsGroup) {
                ((ElementsGroup) visibleElement).setGroupLocation(group.getGroupLocation());
                ((Composite) visibleElement.getComponent()).setLayoutManager(new FreeLayoutManager());
            }
            //na kraju ide dodavanje komponente za iscrtavanje
            ((Composite) group.getComponent()).addChild(index, visibleElement.getComponent());
        }
    }

    public static int indexOf(ElementsGroup group, VisibleElement visibleElement) {
        return group.getVisibleElementList().indexOf(visibleElement);
    }

    /**
     * Brisanje vidljivog elementa iz liste vidljivih elemenata
     * @param visibleElement vidljivi element
     */
    public static void removeVisibleElement(ElementsGroup group, VisibleElement visibleElement) {
        if (group.getVisibleElementList().contains(visibleElement)) {
        	group.getVisibleElementList().remove(visibleElement);
            ((Composite) group.getComponent()).removeChild(visibleElement.getComponent());
        }
    }

    /**
     * Pronalazi vidljiv element čija se grafička komponenta nalazi na prosleđenoj lokaciji
     * @param point lokacija
     * @return vidljivi element
     */
    public static VisibleElement getVisibleElementAtPoint(ElementsGroup group, Point point) {
        for (VisibleElement visibleElement : group.getVisibleElementList()) {
            if (visibleElement.getComponent().contains(point)) {
                if (visibleElement instanceof ElementsGroup) {
                    VisibleElement retElem = getVisibleElementAtPoint(((ElementsGroup) visibleElement), point);
                    if (retElem != null) {
                        return retElem;
                    } else {
                        return visibleElement;
                    }
                } else {
                    return visibleElement;
                }
            }
        }
        return null;
    }

    public static ElementsGroup getElementsGroupAtPoint(ElementsGroup group, Point point) {
        for (VisibleElement visibleElement : group.getVisibleElementList()) {
            if (visibleElement.getComponent().contains(point)) {
                if (visibleElement instanceof ElementsGroup) {
                    ElementsGroup retElem = getElementsGroupAtPoint(((ElementsGroup) visibleElement), point);
                    if (retElem != null) {
                        return retElem;
                    } else {
                        return (ElementsGroup) visibleElement;
                    }
                }
            }
        }
        return null;
    }

    public static boolean checkIfCanAdd(ElementsGroup group, VisibleElement visibleElement) {
        boolean flag = false;
        if (visibleElement instanceof VisibleOperation && group.getGroupLocation() == GroupLocation.operationPanel) {
            flag = true;
        } else if (visibleElement instanceof VisibleProperty && group.getGroupLocation() == GroupLocation.componentPanel) {
            flag = true;
        } else if (visibleElement instanceof ElementsGroup && (group.getGroupLocation() == GroupLocation.componentPanel || group.getGroupLocation() == GroupLocation.operationPanel)) {
            //TODO: dodati opciju da ne moze radio group da se doda u OPERATION deo!!!
            flag = true;
        } else if (visibleElement instanceof VisibleAssociationEnd) {
            if (visibleElement instanceof Zoom && group.getGroupLocation() == GroupLocation.componentPanel) {
                flag = true;
            } else if (visibleElement instanceof Next && group.getGroupLocation() == GroupLocation.operationPanel) {
                flag = true;
            } else if (visibleElement instanceof Hierarchy && group.getGroupLocation() == GroupLocation.componentPanel) {
                //NEW:
                if (group.umlClass() instanceof ParentChild) {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    public static int getVisibleElementsNum(ElementsGroup group) {
        return group.getVisibleElementList().size();
    }

    public static VisibleElement getVisibleElementAt(ElementsGroup group, int index) {
        return group.getVisibleElementList().get(index);
    }
}
