package kroki.api.commons;

import java.util.List;

import kroki.api.enums.OperationType;
import kroki.api.util.Util;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.property.VisibleProperty;
import kroki.profil.utils.ElementsGroupUtil;
import kroki.profil.utils.HierarchyUtil;
import kroki.profil.utils.ParentChildUtil;
import kroki.profil.utils.UIPropertyUtil;

/**
 * Class containing methods for creating new operations and properties,
 * as well as for adding and removing them from panels
 * @author KROKI Team
 *
 */
public class ApiCommons {

	/**
	 * Creates a new visible operations and adds in to the given panel
	 * @param label Label of the visible operation
	 * @param visible Is operation visible
	 * @param componentType Component type (Button)
	 * @param operationType Operation type (report or transaction)
	 * @param panel Visible panel to which the property is being added
	 * @param indexClass Class index of the operation (-1 if the visible property is being created for the first time, old index in case of undo operation)
	 * @param indexGroup Group index of the operation (-1 if the visible property is being created for the first time, old index in case of undo operation)
	 * @return Created visible operation
	 */
	public static VisibleOperation makeVisibleOperation(String label, boolean visible, ComponentType componentType,
			VisibleClass panel, OperationType operationType, int indexClass, int indexGroup){
		
		VisibleOperation operation;
		int operationsGroup = Util.getOperationGroupIndex(panel);
		if (operationsGroup == -1)
			return null;
		if (operationType == OperationType.REPORT)
			operation = new Report(label, visible, componentType);
		else
			operation = new Transaction(label, visible, componentType);
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(operationsGroup);
		if (indexGroup != -1)
			ElementsGroupUtil.addVisibleElement(gr, indexGroup, operation);
		else
			ElementsGroupUtil.addVisibleElement(gr, operation);
		if (indexClass != -1)
			UIPropertyUtil.addVisibleElement(panel, indexClass, operation);
		else
			UIPropertyUtil.addVisibleElement(panel, operation);
		return operation;

	}
	
	/**
	 * 
	 * @param label Label of the visible operation
	 * @param visible Is operation visible
	 * @param type Component type (Button)
	 * @param panel Visible panel to which the property is being added
	 * @param operationType Operation type (report or transaction)
	 * @return Created visible operation
	 */
	public static VisibleOperation makeVisibleOperation(String label, boolean visible, ComponentType componentType, 
			VisibleClass panel, OperationType operationType){
		
		return makeVisibleOperation(label, visible, componentType, panel, operationType, -1, -1);
	}
	
	/**
	 * Removes operation with give class index from visible class
	 * @param visibleClass Class containing operation which is to be removed
	 * @param classIndex Class index of the operation
	 */
	public static void removeOperation(VisibleClass visibleClass, int classIndex){
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getOperationGroupIndex(visibleClass));
		ElementsGroupUtil.removeVisibleElement(gr, UIPropertyUtil.getVisibleElementAt(visibleClass, classIndex));
		UIPropertyUtil.removeVisibleElement(visibleClass, classIndex);
	}
	
	/**
	 * Removes given visible element from the specified visible class
	 * @param visibleClass Visible class
	 * @param visibleElement Element to be removed
	 */
	public static void removeVisibleElement(VisibleClass visibleClass, VisibleElement visibleElement){
		
		ElementsGroup gr = null;
		if (visibleElement instanceof VisibleOperation)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getOperationGroupIndex(visibleClass));
		else if (visibleElement instanceof VisibleProperty)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getPropertiesGroupIndex(visibleClass));
		else if (visibleElement instanceof Hierarchy)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getPropertiesGroupIndex(visibleClass));
		else if (visibleElement instanceof VisibleAssociationEnd)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getOperationGroupIndex(visibleClass));
		else
			return;
		
		ElementsGroupUtil.removeVisibleElement(gr, visibleElement);
		UIPropertyUtil.removeVisibleElement(visibleClass, visibleElement);
	}
	
	/**
	 * Adds hierarchy to the given panel. Sets properties of the hierarchy
	 * @param visibleClass Visible panel to which the property is being added
	 * @param hierarchy Hierarchy
	 * @param targetPanel Hierarchy's target panel
	 */
	public static void addHierarchyElement(VisibleClass visibleClass, Hierarchy hierarchy, VisibleClass targetPanel){
		hierarchy.setActivationPanel(visibleClass);
		UIPropertyUtil.addVisibleElement(visibleClass, hierarchy);
		hierarchy.setTargetPanel(targetPanel);


		if (!(targetPanel instanceof ParentChild)){
			List<Hierarchy> possibleParents = ParentChildUtil.possibleParents((ParentChild)visibleClass, hierarchy, hierarchy.getLevel() - 1);
			if (possibleParents != null  && possibleParents.size() >= 1){ 
				hierarchy.setHierarchyParent(possibleParents.get(0)); //set the first one by default, users can change it in mockup editor
				hierarchy.setLevel(possibleParents.get(0).getLevel() + 1);
				List<VisibleAssociationEnd> possibleEnds = ParentChildUtil.possibleAssociationEnds((ParentChild)visibleClass, hierarchy);
				if (possibleEnds != null  && possibleEnds.size() >= 1)
					hierarchy.setViaAssociationEnd(possibleEnds.get(0));
			}
		}

		
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.PARENTCHILD_PANEL_PROPERTIES);
		ElementsGroupUtil.addVisibleElement(gr, hierarchy);

		hierarchy.setParentGroup(gr);
	     //  element.getComponent().setAbsolutePosition(point);
	        
		gr.update();
		visibleClass.update();
		HierarchyUtil.forceUpdateComponent(hierarchy);
	}
	

	/**
	 * Creates a new visible property and adds in to the given panel
	 * @param label Label of the visible property
	 * @param visible Is property visible
	 * @param type Component type (text field, text area, check box etc.)
	 * @param panel Visible panel to which the property is being added
	 * @param indexClass Class index of the property (-1 if the visible property is being created for the first time, old index in case of undo operation)
	 * @param indexGroup Group index of the property (-1 if the visible property is being created for the first time, old index in case of undo operation)
	 * @return Created visible property
	 */
	public static VisibleProperty makeVisiblePropertyAt(String label, boolean visible, ComponentType type, VisibleClass panel, int indexClass, int indexGroup) {
		NamingUtil namer = new NamingUtil();
		int propertiesGroup = Util.getPropertiesGroupIndex(panel);
		if (propertiesGroup == -1)
			return null;
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(propertiesGroup);
		VisibleProperty property = new VisibleProperty(label, visible, type);	
		property.setName(label);
		if(type == ComponentType.TEXT_FIELD) 
			property.setDataType("String");
		property.setColumnLabel(namer.toDatabaseFormat(panel.getLabel(), label));

		if (indexGroup != -1)
			ElementsGroupUtil.addVisibleElement(gr, indexGroup, property);
		else
			ElementsGroupUtil.addVisibleElement(gr, property);
		if (indexClass != -1)
			UIPropertyUtil.addVisibleElement(panel, indexClass, property);
		else
			UIPropertyUtil.addVisibleElement(panel, property);
		return property;
	}


	/**
	 * Creates visible property by calling {@link #makeVisiblePropertyAt()} and passing -1 as class and group index
	 * @param label Label of the visible property
	 * @param visible Is property visible
	 * @param type Component type (text field, text area, check boc etc.)
	 * @param panel Visible panel to which the property is being added
	 * @return Created visible property
	 */
	public static VisibleProperty makeVisibleProperty(String label, boolean visible, ComponentType type, VisibleClass panel){
		return makeVisiblePropertyAt(label, visible, type, panel, -1, -1);
	}
	
	
	/**
	 * Removes property with given class index from visible class
	 * @param visibleClass Visible class containing the property
	 * @param classIndex Class index of the property
	 */
	public static void removeProperty(VisibleClass visibleClass, int classIndex){
		ElementsGroup gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getPropertiesGroupIndex(visibleClass));
		ElementsGroupUtil.removeVisibleElement(gr, UIPropertyUtil.getVisibleElementAt(visibleClass, classIndex));
		UIPropertyUtil.removeVisibleElement(visibleClass, classIndex);
	}
	
	/**
	 * Moves element down
	 * @param visibleClass Visible class containing the element
	 * @param classIndex Class index of the element
	 * @param groupIndex Group index of the element
	 * @param type 1 for swapping properties, 2 for swapping operations, 
	 */
	public static void moveElementDown(VisibleClass visibleClass, int classIndex, int groupIndex, int type) {

		
		ElementsGroup gr;
		if (type == 1)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getPropertiesGroupIndex(visibleClass));
		else if (type == 2)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getOperationGroupIndex(visibleClass));
		else 
			return;
		
		if (groupIndex == gr.getVisibleElementList().size() - 1)
			return;

		//remove and add again, to each other's position
		VisibleElement thisProp = (VisibleElement) ElementsGroupUtil.getVisibleElementAt(gr, groupIndex);
		VisibleElement otherProp = (VisibleElement) ElementsGroupUtil.getVisibleElementAt(gr, groupIndex + 1);

		swapProperties(visibleClass, thisProp, otherProp, groupIndex + 1, groupIndex, classIndex + 1, classIndex, gr);

	}
	
	/**
	 * Moves element up 
	 * @param visibleClass Visible class containing the element
	 * @param classIndex Class index of the element
	 * @param groupIndex Group index of the element
	 * @param type 1 for swapping properties, 2 for swapping operations, 
	 */
	public static void moveElementUp(VisibleClass visibleClass, int classIndex, int groupIndex, int type) {
		
		if (groupIndex == 0)
			return;
		
		ElementsGroup gr;
		if (type == 1)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getPropertiesGroupIndex(visibleClass));
		else if (type == 2)
			gr = (ElementsGroup) visibleClass.getVisibleElementList().get(Util.getPropertiesGroupIndex(visibleClass));
		else 
			return;

		//remove and add again, to each other's position
		VisibleElement thisProp = (VisibleElement) ElementsGroupUtil.getVisibleElementAt(gr, groupIndex);
		VisibleElement otherProp = (VisibleElement) ElementsGroupUtil.getVisibleElementAt(gr, groupIndex - 1);

		swapProperties(visibleClass, thisProp, otherProp, groupIndex - 1, groupIndex, classIndex - 1, classIndex, gr);

	}
	
	/**
	 * Swaps positions of two visible elements
	 * @param visibleClass Class containing the elements
	 * @param p1 The first visible element
	 * @param p2 The second visible element
	 * @param firstIndexGr First element's group index
	 * @param secondIndexGr Second element's group index
	 * @param firstIndexCl First element's class index
	 * @param secondIndexCl Second element's class index
	 * @param gr Element group containing the elements
	 */
	public static void swapProperties(VisibleClass visibleClass, VisibleElement p1, VisibleElement p2, int firstIndexGr, int secondIndexGr,
			int firstIndexCl, int secondIndexCl, ElementsGroup gr){


		ElementsGroupUtil.removeVisibleElement(gr, p1);
		ElementsGroupUtil.removeVisibleElement(gr, p2);
		if (firstIndexGr < secondIndexGr){
			ElementsGroupUtil.addVisibleElement(gr, firstIndexGr, p1);
			ElementsGroupUtil.addVisibleElement(gr, secondIndexGr, p2);
		}
		else{
			ElementsGroupUtil.addVisibleElement(gr, secondIndexGr, p2);
			ElementsGroupUtil.addVisibleElement(gr, firstIndexGr, p1);
		}

		UIPropertyUtil.removeVisibleElement(visibleClass, p1);
		UIPropertyUtil.removeVisibleElement(visibleClass, p2);
		if (firstIndexCl < secondIndexCl){
			UIPropertyUtil.addVisibleElement(visibleClass, firstIndexCl, p1);
			UIPropertyUtil.addVisibleElement(visibleClass, secondIndexCl, p2);
		}
		else{
			UIPropertyUtil.addVisibleElement(visibleClass, secondIndexCl, p2);
			UIPropertyUtil.addVisibleElement(visibleClass, firstIndexCl, p1);
		}
	}
	
}
