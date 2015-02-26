package adapt.model.ejb;

import adapt.model.enumeration.Enumeration;

public class ColumnAttribute extends AbstractAttribute {
	
	protected Boolean key = false;
	protected Integer length;
	protected Integer scale;
	protected Integer precision;
	protected String dataType;
	protected Boolean disabled = false;
	protected Boolean editableInTable = false;
	protected Boolean derived = false;
	protected String formula;
	protected Object defaultValue;
	protected Enumeration enumeration;
	
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
	public Enumeration getEnumeration() {
		return enumeration;
	}
	public void setEnumeration(Enumeration enumeration) {
		this.enumeration = enumeration;
	}
	public Integer getPrecision() {
		return precision;
	}
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
	@Override
	public String toString() {
		return "ColumnAttribute [key=" + key + ", length=" + length
				+ ", scale=" + scale + ", dataType=" + dataType + ", disabled="
				+ disabled + ", editableInTable=" + editableInTable
				+ ", derived=" + derived + ", formula=" + formula
				+ ", defaultValue=" + defaultValue + ", enumeration="
				+ enumeration + "]";
	}
}
