package kroki.api.profil.panel;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import kroki.api.profil.group.ElementsGroupUtil;
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

public class VisibleClassUtil {

	/**
	 * Pronalazi vidljiv element Ä�ija se grafiÄ�ka komponenta nalazi na prosleÄ‘enoj lokaciji
	 * @param point lokacija
	 * @return vidljivi element
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
	 * VraÄ‡a sve vidljiva polja
	 * @return
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

	 /**VraÄ‡a listu svih perzistentnih obelezja klase*/
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
	  * VraÄ‡a sve operacije
	  * @return
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

	 /**VraÄ‡a listu svih izvesstaja*/
	 public static List<Report> containedReports(VisibleClass visibleClass) {
		 List<Report> reportList = new ArrayList<Report>();
		 for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			 if (visibleElement instanceof Report) {
				 reportList.add((Report) visibleElement);
			 }
		 }
		 return reportList;
	 }

	 /**VraÄ‡a listu svih transakcija*/
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
	  * VraÄ‡a sve krajeve asocijacije
	  * @return
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

	 /**VraÄ‡a listu svih zumova*/
	 public static List<Zoom> containedZooms(VisibleClass visibleClass) {
		 List<Zoom> zoomList = new ArrayList<Zoom>();
		 for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			 if (visibleElement instanceof Zoom) {
				 zoomList.add((Zoom) visibleElement);
			 }
		 }
		 return zoomList;
	 }

	 /**VraÄ‡a listu sih nekstova*/
	 public static List<Next> containedNexts(VisibleClass visibleClass) {
		 List<Next> nextList = new ArrayList<Next>();
		 for (VisibleElement visibleElement : visibleClass.getVisibleElementList()) {
			 if (visibleElement instanceof Next) {
				 nextList.add((Next) visibleElement);
			 }
		 }
		 return nextList;
	 }

	 /**Vraca listu hijerarhija*/
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
	  */
	 public static Integer getComponentCount(VisibleClass panel, ComponentType type) {
		 Integer count = 0;
		 if(panel.getComponentCounts().get(type) != null) {
			 count = panel.getComponentCounts().get(type);
		 }
		 return count;
	 }

	 /**
	  * Increments number of components with specfied type and returns incremented count
	  * If that components type is not found in count map, it is added with count set to 1
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

	 public static void decrementCount(VisibleClass panel, ComponentType type) {
		 Integer count = panel.getComponentCounts().get(type);
		 if(count != null && count > 0) {
			 count--;
			 panel.getComponentCounts().put(type, count);
		 }
	 }
	
}
