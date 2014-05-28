package graphedit.gui.table;

import graphedit.app.MainFrame;
import graphedit.command.AddParameterCommand;
import graphedit.command.ChangeParameterCommand;
import graphedit.command.RemoveParameterCommand;
import graphedit.gui.utils.Dialogs;
import graphedit.model.components.Method;
import graphedit.model.components.Parameter;
import graphedit.util.Validator;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ParameterTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private Method method;
	private List<Parameter> parameters;
	
	public ParameterTableModel(Method method, List<Parameter> parameters) { 
		this.method = method;
		this.parameters = parameters;
	}
	
	private static final String[] COLUMN_NAMES = {
		"","Name", "Type", "Final"
	};

	@Override
	public int getRowCount() {
		return parameters.size() + 1;
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
		if (columnIndex == 0)
			return false;
		return true;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Integer.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3: 
			return Boolean.class;
		}
		return super.getColumnClass(columnIndex);
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex > parameters.size() - 1)
			return null;
		Parameter parameter = parameters.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return rowIndex + 1;
		case 1:
			return parameter.getName();
		case 2:
			return parameter.getType();
		case 3: 
			return parameter.isFinalParameter();
		}
		return null;
	}
	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (getValueAt(rowIndex, columnIndex).equals(value)) {
			return;
		}
		Parameter parameter = parameters.get(rowIndex);
		String name = parameter.getName();
		String type = parameter.getType();
		Boolean finalParameter = parameter.isFinalParameter();
		switch (columnIndex) {
		case 1:
			if (Validator.parameterHasName(parameters, (String) value)) {
				Dialogs.showErrorMessage("Name already exists!", "Error");
			} else if (!Validator.isJavaIdentifier((String) value)) {
				Dialogs.showErrorMessage("Not a valid identifier name!", "Error");
			} else {
				name = (String) value;
			}
			break;
		case 2:
			type = (String) value;
			break;
		case 3: 
			finalParameter = (Boolean) value;
			break;
		}
		ChangeParameterCommand command = new ChangeParameterCommand(MainFrame.getInstance().getCurrentView(), MainFrame.getInstance().getCurrentView().getSelectionModel().getSelectedElements().get(0), parameter, name, type, finalParameter);
		MainFrame.getInstance().getCommandManager().executeCommand(command);
		MainFrame.getInstance().getCurrentView().repaint();
	}
	
	public void removeParameter(int rowIndex) {
		String name = (String) getValueAt(rowIndex, 1);
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getName().equals(name)) {
				RemoveParameterCommand command = new RemoveParameterCommand(
						MainFrame.getInstance().getCurrentView(), 
						MainFrame.getInstance().getCurrentView().getSelectionModel().getSelectedElements().get(0),
						method,
						parameters.get(i));
				MainFrame.getInstance().getCommandManager().executeCommand(command);
				
				fireTableDataChanged();
				return;
			}
		}
	}
	
	public void addParameter(Parameter parameter) {
		AddParameterCommand command = new AddParameterCommand(
				MainFrame.getInstance().getCurrentView(),
				MainFrame.getInstance().getCurrentView().getSelectionModel().getSelectedElements().get(0),
				method,
				parameter);
		MainFrame.getInstance().getCommandManager().executeCommand(command);
		
		fireTableDataChanged();
	}

}
