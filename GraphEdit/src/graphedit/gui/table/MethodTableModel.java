package graphedit.gui.table;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.command.AddMethodCommand;
import graphedit.command.ChangeMethodOwnerCommand;
import graphedit.command.ChangeMethodReturnTypeCommand;
import graphedit.command.ChangeMethodStereotypeCommand;
import graphedit.command.MoveMethodCommand;
import graphedit.command.RemoveMethodCommand;
import graphedit.command.RenameMethodCommand;
import graphedit.gui.utils.Dialogs;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Interface;
import graphedit.model.components.Method;
import graphedit.model.components.MethodStereotypeUI;
import graphedit.model.components.Modifier;
import graphedit.util.Validator;
import graphedit.view.ClassPainter;

import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

public class MethodTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private GraphElement element;
	private List<Method> methods;

	public MethodTableModel(GraphElement element, List<Method> methods) {
		this.element = element;
		this.methods = methods;
	}

	private static final String[] COLUMN_NAMES = {
		"", "Owner", "Name", "Stereotype", "Return Type", "Modifier", "Constructor", "Static", "Final", "Abstract", "Parameters", "Display"
	};

	@Override
	public int getRowCount() {
		return methods.size() + 1;
	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (rowIndex == methods.size())
			return false;
		if (columnIndex == 0)
			return false;
		if (columnIndex == COLUMN_NAMES.length - 1)
			return true;
		if (element instanceof Interface) {
			if (columnIndex == 5 || columnIndex == 6 || columnIndex == 9) {
				return false;
			}
		}
		else if (currentApplicationMode() == ApplicationMode.USER_INTERFACE){
			return columnIndex < 4;
		}
		return columnIndex != 10;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Integer.class;
		case 1:
			return GraphElement.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return String.class;
		case 5:
			return Modifier.class;
		case 6:
			return Boolean.class;
		case 7: 
			return Boolean.class;
		case 8:
			return Boolean.class;
		case 9:
			return Boolean.class;
		case 10:
			return JButton.class;
		case 11:
			return Boolean.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex > methods.size() - 1)
			return null;
		Method method = methods.get(rowIndex);
		switch (columnIndex) {
		case -1:
			return method;
		case 0:
			return rowIndex + 1;
		case 1:
			return element;
		case 2:
			return method.getName();
		case 3:
			return method.getStereotype();
		case 4:
			return method.getReturnType();
		case 5:
			return method.getModifier();
		case 6:
			return method.isConstructorMethod();
		case 7: 
			return method.isStaticMethod();
		case 8:
			return method.isFinalMethod();
		case 9:
			return method.isAbstractMethod();
		case 10:
			return method.getParameters();
		case 11:
			return method.isVisible();
		}
		return null;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (getValueAt(rowIndex, columnIndex).equals(value)) {
			return;
		}
		Method method = methods.get(rowIndex);
		switch (columnIndex) {
		case 1:
			if (element != (GraphElement)value){
				ChangeMethodOwnerCommand command1 = new ChangeMethodOwnerCommand(MainFrame.getInstance().getCurrentView(), element, (GraphElement) value,  method);
				MainFrame.getInstance().getCommandManager().executeCommand(command1);
				fireTableDataChanged();
			}
			break;
		case 2:
			if (Validator.methodHasName(methods, (String) value)) {
				Dialogs.showErrorMessage("Name already exists!", "Error");
			} else if (!Validator.isJavaIdentifier((String) value)) {
				Dialogs.showErrorMessage("Not a valid identifier name!", "Error");
			} else {
				RenameMethodCommand command = new RenameMethodCommand(MainFrame.getInstance().getCurrentView(), element, method, (String) value);
				MainFrame.getInstance().getCommandManager().executeCommand(command);
			}
			break;
		case 3:
			if (value instanceof MethodStereotypeUI){
				String newStereotype = ((MethodStereotypeUI) value).toString();
				ChangeMethodStereotypeCommand command = new ChangeMethodStereotypeCommand(MainFrame.getInstance().getCurrentView(), element, method, newStereotype);
				MainFrame.getInstance().getCommandManager().executeCommand(command);
			}
			else
				method.setStereotype((String) value);
			break;
		case 4:
			ChangeMethodReturnTypeCommand command = new ChangeMethodReturnTypeCommand(MainFrame.getInstance().getCurrentView(), element, method, (String) value);
			MainFrame.getInstance().getCommandManager().executeCommand(command);
			break;
		case 5:
			method.setModifier((Modifier) value);
			break;
		case 6:
			method.setConstructorMethod((Boolean) value);
			break;
		case 7: 
			method.setStaticMethod((Boolean) value);
			break;
		case 8:
			method.setFinalMethod((Boolean) value);
			break;
		case 9:
			method.setAbstractMethod((Boolean) value);
			break;
		case 11:
			method.setVisible((Boolean)value);
			((ClassPainter)MainFrame.getInstance().getCurrentView().getElementPainter(element)).setUpdated(true);
			((ClassPainter)MainFrame.getInstance().getCurrentView().getElementPainter(element)).setAttributesOrMethodsUpdated(true);
			break;
		}
		MainFrame.getInstance().getCurrentView().repaint();
	}

	public Method getMethod(int index) {
		if (index < methods.size())
			return methods.get(index);
		return null;
	}

	
	public void moveMethodUp(int rowIndex) {
		MoveMethodCommand command = new MoveMethodCommand(MainFrame.getInstance().getCurrentView(),
				element, (Method)getValueAt(rowIndex, -1), rowIndex, MoveMethodCommand.DIRECTION.UP);
		MainFrame.getInstance().getCommandManager().executeCommand(command);
		fireTableDataChanged();
	}

	public void moveMethodDown(int rowIndex) {
		MoveMethodCommand command = new MoveMethodCommand(MainFrame.getInstance().getCurrentView(),
				element, (Method)getValueAt(rowIndex, -1), rowIndex, MoveMethodCommand.DIRECTION.DOWN);
		MainFrame.getInstance().getCommandManager().executeCommand(command);
		fireTableDataChanged();
	}

	
	
	public void removeMethod(int rowIndex) {
		String name = (String) getValueAt(rowIndex, 2);
		for (int i = 0; i < methods.size(); i++) {
			if (methods.get(i).getName().equals(name)) {
				RemoveMethodCommand command = new RemoveMethodCommand(
						MainFrame.getInstance().getCurrentView(), 
						element, 
						methods.get(i));
				MainFrame.getInstance().getCommandManager().executeCommand(command);

				fireTableDataChanged();
				return;
			}
		}
	}

	public void addMethod(Method method) {
		AddMethodCommand command = new AddMethodCommand(
				MainFrame.getInstance().getCurrentView(),
				element, 
				method);
		MainFrame.getInstance().getCommandManager().executeCommand(command);

		fireTableDataChanged();
	}

	private ApplicationMode currentApplicationMode(){
		return MainFrame.getInstance().getAppMode();
	}

}
