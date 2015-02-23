package adapt.model.panel.configuration.operation;

import adapt.model.AbstractElement;

public class Parameter extends AbstractElement {
	protected Class<?> type;
	protected Object value;
	protected Object defaultValue;
	protected Boolean hidden = false;
	protected Boolean disabled = true;
	protected ParameterType parameterType;
	
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Boolean getHidden() {
		return hidden;
	}
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	public ParameterType getParameterType() {
		return parameterType;
	}
	public void setParameterType(ParameterType parameterType) {
		this.parameterType = parameterType;
	}
}
