package kroki.profil.utils;

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

/**
 * Class contains <code>ElementsGroup</code> util methods 
 * @author Kroki Team
 */
public class ElementsGroupUtil {


	/**
	 * Adds a new visible element to the group's list of visible element
	 * @param group Element group
	 * @param visibleElement Element to be added
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
			//add drawing component
			((Composite) group.getComponent()).addChild(visibleElement.getComponent());
		}
	}

	/**
	 * Adds a visible element to the group at the position specified with the index
	 * @param group Elements group
	 * @param index Position where the element should be added
	 * @param visibleElement Visible element to be added
	 */
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
			//add drawing component
			((Composite) group.getComponent()).addChild(index, visibleElement.getComponent());
		}
	}

	/**
	 * Returns index of the visible element in the element's group contained elements list
	 * @param group Elements group
	 * @param visibleElement Visible element
	 * @return Element index
	 */
	public static int indexOf(ElementsGroup group, VisibleElement visibleElement) {
		return group.getVisibleElementList().indexOf(visibleElement);
	}

	/**
	 * Removes the element from the group's list of visible elements
	 * @param group Elements group
	 * @param visibleElement Visible element to be removed
	 */
	public static void removeVisibleElement(ElementsGroup group, VisibleElement visibleElement) {
		if (group.getVisibleElementList().contains(visibleElement)) {
			group.getVisibleElementList().remove(visibleElement);
			((Composite) group.getComponent()).removeChild(visibleElement.getComponent());
		}
	}

	/**
	 * Finds the visible element whose graphical component is located at the point position
	 * @param group Elements group
	 * @param point Location
	 * @return Found visible element or null if no element can be found
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

	/**
	 * Finds the element group whose graphical component is located at the point position
	 * @param group Parent elements group 
	 * @param point Location
	 * @return Found elements group or null if no group can be found
	 */
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

	/**
	 * Checks if an visible element can be added to the given elements group
	 * @param group Elements group
	 * @param visibleElement Visible element to be added
	 * @return true if element can be added, false otherwise
	 */
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

	/**
	 * Return number of visible element contained by the elements gruop
	 * @param group Elements group
	 * @return Number of visible elements
	 */
	public static int getVisibleElementsNum(ElementsGroup group) {
		return group.getVisibleElementList().size();
	}

	/**
	 * Returns visible element at the given index in the group's visible elements list
	 * @param group Elements group
	 * @param index Index
	 * @return Visible element at index position
	 */
	public static VisibleElement getVisibleElementAt(ElementsGroup group, int index) {
		return group.getVisibleElementList().get(index);
	}
}
