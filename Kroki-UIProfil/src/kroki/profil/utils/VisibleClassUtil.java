
package kroki.profil.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.Persistent;
import kroki.profil.property.VisibleProperty;

/**
 * Class contains <code>VisibleClass</code> util methods 
 * @author Kroki Team
 */
public class VisibleClassUtil {

	/**
	 * Finds visible element whose graphical component is located at the point's position
	 * @param visibleClass Visible class
	 * @param point Location
	 * @return Found visible element or null if there is no element at the given position
	 */
	public static VisibleElement getVisibleElementAtPoint(VisibleClass visibleClass, Point point) {
		VisibleElement returnElement = null;
		for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			if (visibleElement instanceof ElementsGroup) {
				ElementsGroup elementsGroup = (ElementsGroup) visibleElement;
				if (elementsGroup.getComponent().contains(point)) {
					returnElement = ElementsGroupUtil.getVisibleElementAtPoint(elementsGroup, point);
					if (returnElement == null) {
						returnElement = elementsGroup;
					}
					break;
				}
			} else {
				if (visibleElement.getComponent().contains(point)) {
					returnElement = visibleElement;
					break;
				}
			}
		}
		if (returnElement == null) {
			if (visibleClass.getComponent().contains(point)) {
				returnElement = visibleClass;
			}
		}
		return returnElement;
	}

	/**
	 * Finds elements group whose graphical component is located at the point's position
	 * @param visibleClass Visible class
	 * @param point Location
	 * @return Found elements group or null if there is no group at the given position
	 */
	public static ElementsGroup getElementsGroupAtPoint(VisibleClass visibleClass, Point point) {
		ElementsGroup returnElement = null;
		for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			if (visibleElement instanceof ElementsGroup) {
				ElementsGroup elementsGroup = (ElementsGroup) visibleElement;
				if (elementsGroup.getComponent().contains(point)) {
					returnElement = ElementsGroupUtil.getElementsGroupAtPoint(elementsGroup, point);
					if (returnElement == null) {
						returnElement = elementsGroup;
					}
					break;
				}
			}
		}
		return returnElement;
	}

	/**
	 * Returns all visible properties contained by the visible class
	 * @param visibleClass Visible class 
	 * @return All contained visible properties
	 */
	public static List<VisibleProperty> containedProperties(VisibleClass visibleClass) {
		List<VisibleProperty> visiblePropertyList = new ArrayList<VisibleProperty>();
		for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			if (visibleElement instanceof VisibleProperty) {
				visiblePropertyList.add((VisibleProperty) visibleElement);
			}
		}
		return visiblePropertyList;
	}

	/**
	 * Returns all persistent elements contained by the visible class
	 * @param visibleClass Visible class 
	 * @return All contained persistent elements
	 */
	public static List<Persistent> containedPersistents(VisibleClass visibleClass) {
		List<Persistent> persistentList = new ArrayList<Persistent>();
		for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			if (visibleElement instanceof Persistent) {
				persistentList.add((Persistent) visibleElement);
			}
		}
		return persistentList;
	}

	/**
	 * Returns all operations contained by the visible class
	 * @param visibleClass Visible class 
	 * @return All contained operations
	 */
	public static List<VisibleOperation> containedOperations(VisibleClass visibleClass) {
		List<VisibleOperation> visibleOperationList = new ArrayList<VisibleOperation>();
		for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			if (visibleElement instanceof VisibleOperation) {
				visibleOperationList.add((VisibleOperation) visibleElement);
			}
		}
		return visibleOperationList;
	}

	/**
	 * Returns all reports contained by the visible class
	 * @param visibleClass Visible class 
	 * @return All contained reports
	 */
	public static List<Report> containedReports(VisibleClass visibleClass) {
		List<Report> reportList = new ArrayList<Report>();
		for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			if (visibleElement instanceof Report) {
				reportList.add((Report) visibleElement);
			}
		}
		return reportList;
	}

	/**
	 * Returns all transactions contained by the visible class
	 * @param visibleClass Visible class 
	 * @return All contained transactions
	 */
	public static List<Transaction> containedTransactions(VisibleClass visibleClass) {
		List<Transaction> transactionList = new ArrayList<Transaction>();
		for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			if (visibleElement instanceof Transaction) {
				transactionList.add((Transaction) visibleElement);
			}
		}
		return transactionList;
	}

	/**
	 * Returns all association ends contained by the visible class
	 * @param visibleClass Visible class 
	 * @return All contained association ends
	 */
	public static List<VisibleAssociationEnd> containedAssociationEnds(VisibleClass visibleClass) {
		List<VisibleAssociationEnd> visibleAssociationEndList = new ArrayList<VisibleAssociationEnd>();
		for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			if (visibleElement instanceof VisibleAssociationEnd) {
				visibleAssociationEndList.add((VisibleAssociationEnd) visibleElement);
			}
		}
		return visibleAssociationEndList;
	}

	/**
	 * Returns all zooms contained by the visible class
	 * @param visibleClass Visible class 
	 * @return All contained zooms
	 */
	public static List<Zoom> containedZooms(VisibleClass visibleClass) {
		List<Zoom> zoomList = new ArrayList<Zoom>();
		for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			if (visibleElement instanceof Zoom) {
				zoomList.add((Zoom) visibleElement);
			}
		}
		return zoomList;
	}

	/**
	 * Returns all nexts contained by the visible class
	 * @param visibleClass Visible class 
	 * @return All contained nexts
	 */
	public static List<Next> containedNexts(VisibleClass visibleClass) {
		List<Next> nextList = new ArrayList<Next>();
		for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			if (visibleElement instanceof Next) {
				nextList.add((Next) visibleElement);
			}
		}
		return nextList;
	}

	/**
	 * Returns all hierarchies contained by the visible class
	 * @param visibleClass Visible class 
	 * @return All contained hierarchies
	 */
	public static List<Hierarchy> containedHierarchies(VisibleClass visibleClass) {
		List<Hierarchy> hierarchyList = new ArrayList<Hierarchy>();
		for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			if (visibleElement instanceof Hierarchy) {
				hierarchyList.add((Hierarchy) visibleElement);
			}
		}
		return hierarchyList;
	}

	/**
	 * Gets number of components with specified type
	 * @param panel Panel
	 * @param type Component type
	 * @param Number of components with specified type
	 */
	public static Integer getComponentCount(VisibleClass panel, ComponentType type) {
		Integer count = 0;
		if(panel.getComponentCounts().get(type) != null) {
			count = panel.getComponentCounts().get(type);
		}
		return count;
	}

	/**
	 * Increments the number of components with the specified type and returns incremented count
	 * If that components type is not found in count map, it is added with count set to 1
	 * @param panel Panel
	 * @param type Component type
	 * @return Incremented components count
	 */
	public static Integer incrementCount(VisibleClass panel, ComponentType type) {
		Integer count = panel.getComponentCounts().get(type);
		if(count != null) {
			count++;
			panel.getComponentCounts().put(type, count);
			return count;
		}else {
			panel.getComponentCounts().put(type, 1);
			return 1;
		}
	}

	/**
	 * Decrements the number of components with the specified type
	 * @param panel Panel
	 * @param type Component type
	 */
	public static void decrementCount(VisibleClass panel, ComponentType type) {
		Integer count = panel.getComponentCounts().get(type);
		if(count != null && count > 0) {
			count--;
			panel.getComponentCounts().put(type, count);
		}
	}

}
