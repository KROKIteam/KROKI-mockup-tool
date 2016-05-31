package com.panelcomposer.model.attribute;

import javax.swing.JComponent;

import com.panelcomposer.model.enumeration.Enumeration;

public class ColumnAttribute extends AbsAttribute {

	protected Boolean key = false;
	protected Integer scale;
	protected String dataType;
	protected Boolean disabled = false;
	protected Boolean editableInTable = false;
	protected Boolean derived = false;
	protected String formula;
	protected Object defaultValue;
	protected Enumeration enumeration;
	protected JComponent component;
	protected Boolean readOnly = false;
	protected Boolean autoGo = false;
	
	public Boolean getKey() {
		return key;
	}

	public void setKey(Boolean key) {
		this.key = key;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getEditableInTable() {
		return editableInTable;
	}

	public void setEditableInTable(Boolean editableInTable) {
		this.editableInTable = editableInTable;
	}

	public Boolean getDerived() {
		return derived;
	}

	public void setDerived(Boolean derived) {
		this.derived = derived;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public JComponent getComponent() {
		return component;
	}

	public void setComponent(JComponent component) {
		this.component = component;
	}

	public Enumeration getEnumeration() {
		return enumeration;
	}

	public void setEnumeration(Enumeration enumeration) {
		this.enumeration = enumeration;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Boolean getAutoGo() {
		return autoGo;
	}

	public void setAutoGo(Boolean autoGo) {
		this.autoGo = autoGo;
	}

}
