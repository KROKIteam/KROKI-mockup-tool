package kroki.api.commons.operation;

import kroki.api.profil.group.ElementsGroupUtil;
import kroki.api.profil.property.UIPropertyUtil;
import kroki.api.util.Util;
import kroki.profil.ComponentType;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.VisibleClass;

public class UIOperationsCommons {

	

	/**
	 * Creates a new visible operations and adds in to the given panel
	 * @param label Label of visible operation
	 * @param visible Is operation visible
	 * @param componentType Component type (Button)
	 * @param operationType Operation type (report or transaction)
	 * @param panel Visible panel to which the property is being added
	 * @param indexClass Class index of the operation (-1 if the visible property is being created for the first time, old index in case of undo operation)
	 * @param indexGroup Group index of the operation (-1 if the visible property is being created for the first time, old index in case of undo operation)
	 * @return Created visible operation
	 */
	public static VisibleOperation makeVisibleOperation(String label, boolean visible, ComponentType componentType, VisibleClass panel, OperationType operationType, int indexClass, int indexGroup){
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
	 * @param label Label of visible operation
	 * @param visible Is operation visible
	 * @param type Component type (Button)
	 * @param panel Visible panel to which the property is being added
	 * @param operationType Operation type (report or transaction)
	 * @return Created visible operation
	 */
	public static VisibleOperation makeVisibleOperation(String label, boolean visible, ComponentType componentType, VisibleClass panel, OperationType operationType){
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

}
