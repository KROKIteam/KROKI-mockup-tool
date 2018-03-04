package graphedit.gui.table;

import java.util.Collections;

import javax.swing.table.AbstractTableModel;

import graphedit.model.components.Attribute;
import graphedit.util.Utility;
import kroki.profil.property.VisibleProperty;

public class EnumValuesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private Attribute attribute;
	private VisibleProperty visibleProprty;

	public EnumValuesTableModel(Attribute attribute) { 
		this.attribute = attribute;
		if (attribute.getUmlProperty() instanceof VisibleProperty)
			this.visibleProprty = (VisibleProperty) attribute.getUmlProperty();
	}

	private static final String[] COLUMN_NAMES = {
		"","Name"
	};

	@Override
	public int getRowCount() {
		return attribute.getPossibleValues().size() + 1;
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
		if (rowIndex == attribute.getPossibleValues().size())
			return false;
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
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex > attribute.getPossibleValues().size() - 1)
			return null;
		switch (columnIndex) {
		case 0:
			return rowIndex + 1;
		case 1:
			return attribute.getPossibleValues().get(rowIndex);
		}
		return null;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (getValueAt(rowIndex, columnIndex).equals(value)) {
			return;
		}
		switch (columnIndex) {
		case 1:
			attribute.getPossibleValues().set(rowIndex, (String)value);
			if (visibleProprty != null)
				visibleProprty.setEnumeration(Utility.parsePossibleEnumValues(attribute.getPossibleValues()));
			break;
		}

	}

	public void removeValue(int rowIndex) {
		attribute.getPossibleValues().remove(rowIndex);
		if (visibleProprty != null)
			visibleProprty.setEnumeration(Utility.parsePossibleEnumValues(attribute.getPossibleValues()));

		fireTableDataChanged();
	}

	public void addPossibleValue(String value) {
		attribute.getPossibleValues().add(value);
		if (visibleProprty != null)
			visibleProprty.setEnumeration(Utility.parsePossibleEnumValues(attribute.getPossibleValues()));
		fireTableDataChanged();
	}

	public void moveValueUp(int rowIndex) {
		Collections.swap(attribute.getPossibleValues(), rowIndex, rowIndex - 1);
		if (visibleProprty != null)
			visibleProprty.setEnumeration(Utility.parsePossibleEnumValues(attribute.getPossibleValues()));
		fireTableDataChanged();
	}

	public void moveValueDown(int rowIndex) {
		Collections.swap(attribute.getPossibleValues(), rowIndex, rowIndex + 1);
		if (visibleProprty != null)
			visibleProprty.setEnumeration(Utility.parsePossibleEnumValues(attribute.getPossibleValues()));
		fireTableDataChanged();
	}


}