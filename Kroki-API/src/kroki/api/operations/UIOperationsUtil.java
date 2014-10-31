package kroki.api.operations;

import kroki.profil.ComponentType;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;

public class UIOperationsUtil {

	public static final int STANDARD_PANEL_OPERATIONS = 2;
	public static final int PARENTCHILD_PANEL_OPERATIONS = 1;

	
	public static VisibleOperation makeVisibleOperation(String label, boolean visible, ComponentType componentType, VisibleClass panel, OperationType operationType, int indexClass, int indexGroup){
		VisibleOperation operation;
		int operationsGroup = getOperationGroup(panel);
		if (operationsGroup == -1)
			return null;
		if (operationType == OperationType.REPORT)
			operation = new Report(label, visible, componentType);
		else
			operation = new Transaction(label, visible, componentType);
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(operationsGroup);
		if (indexGroup != -1)
			gr.addVisibleElement(indexGroup, operation);
		else
			gr.addVisibleElement(operation);
		if (indexClass != -1)
			panel.addVisibleElement(indexClass, operation);
		else
			panel.addVisibleElement(operation);
		return operation;

	}
	
	public static VisibleOperation makeVisibleOperation(String label, boolean visible, ComponentType componentType, VisibleClass panel, OperationType operationType){
		return makeVisibleOperation(label, visible, componentType, panel, operationType, -1, -1);
	}
	
	private static int getOperationGroup(VisibleClass panel){
		if (panel instanceof StandardPanel)
			return STANDARD_PANEL_OPERATIONS;
		if (panel instanceof ParentChild)
			return PARENTCHILD_PANEL_OPERATIONS;
		return -1; 
	}
}
