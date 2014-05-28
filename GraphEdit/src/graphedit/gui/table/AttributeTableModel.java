package graphedit.gui.table;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.command.AddAttributeCommand;
import graphedit.command.ChangeAttributeOwnerCommand;
import graphedit.command.ChangeAttributeTypeCommand;
import graphedit.command.RemoveAttributeCommand;
import graphedit.command.RenameAttributeCommand;
import graphedit.gui.utils.Dialogs;
import graphedit.model.components.Attribute;
import graphedit.model.components.AttributeTypeUI;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Interface;
import graphedit.model.components.Modifier;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.util.Validator;
import graphedit.view.ClassPainter;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class AttributeTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;


	private GraphElement element; 
	private List<Attribute> attributes;

	public AttributeTableModel(GraphElement element, List<Attribute> attributes) { 
		this.element = element;
		this.attributes = attributes;
		
	}

	private static final String[] COLUMN_NAMES = {
		"", "Owner","Name", "Type", "Modifier", "Static", "Final", "Display"
	};

	@Override
	public int getRowCount() {
		return attributes.size() + 1;
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
		if (rowIndex == attributes.size())
			return false;
		if (columnIndex == 0)
			return false;
		if (columnIndex == COLUMN_NAMES.length - 1)
			return true;
		if (element instanceof Interface || currentApplicationMode() == ApplicationMode.USER_INTERFACE) {
			return columnIndex < 4;
		} 
		return true;
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
			return Modifier.class;
		case 5:
			return Boolean.class;
		case 6: 
			return Boolean.class;
		case 7:
			return Boolean.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex > attributes.size() - 1)
			return null;
		Attribute attribute = attributes.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return rowIndex + 1;
		case 1:
			return element.getProperty(GraphElementProperties.NAME);
		case 2:
			return attribute.getName();
		case 3:
			return attribute.getType();
		case 4:
			return attribute.getModifier();
		case 5:
			return attribute.isStaticAttribute();
		case 6: 
			return attribute.isFinalAttribute();
		case 7:
			return attribute.isVisible();
		}
		return null;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (getValueAt(rowIndex, columnIndex).equals(value)) {
			return;
		}
		Attribute attribute = attributes.get(rowIndex);
		switch (columnIndex) {
		case 1:
			if (element != (GraphElement)value){
				ChangeAttributeOwnerCommand command1 = new ChangeAttributeOwnerCommand(MainFrame.getInstance().getCurrentView(), element, (GraphElement) value,  attribute);
				MainFrame.getInstance().getCommandManager().executeCommand(command1);
				fireTableDataChanged();
			}
			break;
		case 2:
			if (Validator.attributeHasName(attributes, (String) value)) {
				Dialogs.showErrorMessage("Name already exists!", "Error");
			} else if (!Validator.isJavaIdentifier((String) value)) {
				Dialogs.showErrorMessage("Not a valid identifier name!", "Error");
			} else {
				RenameAttributeCommand command = new RenameAttributeCommand(MainFrame.getInstance().getCurrentView(), element, attribute, (String) value);
				MainFrame.getInstance().getCommandManager().executeCommand(command);
			}
			break;
		case 3:
			String valueStr;
			if (value instanceof AttributeTypeUI)
				valueStr = ((AttributeTypeUI)value).toString();
			else
				valueStr = (String) value;
			ChangeAttributeTypeCommand command = new ChangeAttributeTypeCommand(MainFrame.getInstance().getCurrentView(), element, attribute, valueStr);
			MainFrame.getInstance().getCommandManager().executeCommand(command);
			break;
		case 4:
			attribute.setModifier((Modifier) value);
			break;
		case 5:
			attribute.setStaticAttribute((Boolean) value);
			break;
		case 6: 
			attribute.setFinalAttribute((Boolean) value);
			break;
		case 7:
			attribute.setVisible((Boolean)value);
			((ClassPainter)MainFrame.getInstance().getCurrentView().getElementPainter(element)).setUpdated(true);
			((ClassPainter)MainFrame.getInstance().getCurrentView().getElementPainter(element)).setAttributesOrMethodsUpdated(true);
			break;
		}
		MainFrame.getInstance().getCurrentView().repaint();
	}

	public void removeAttribute(int rowIndex) {
		String name = (String) getValueAt(rowIndex, 2);
		for (int i = 0; i < attributes.size(); i++) {
			if (attributes.get(i).getName().equals(name)) {
				RemoveAttributeCommand command = new RemoveAttributeCommand(
						MainFrame.getInstance().getCurrentView(), 
						element, 
						attributes.get(i));
				MainFrame.getInstance().getCommandManager().executeCommand(command);

				fireTableDataChanged();
				return;
			}
		}
	}

	public void addAttribute(Attribute attribute) {
		AddAttributeCommand command = new AddAttributeCommand(
				MainFrame.getInstance().getCurrentView(),
				element, 
				attribute);
		MainFrame.getInstance().getCommandManager().executeCommand(command);

		fireTableDataChanged();
	}

	private ApplicationMode currentApplicationMode(){
		return MainFrame.getInstance().getAppMode();
	}
}
