package com.panelcomposer.business.procedure;

import com.panelcomposer.model.panel.configuration.operation.ParameterType;

public class ProcedureParameter {
	protected ParameterType type;
	protected Object value;

	public ProcedureParameter() {

	}

	public ProcedureParameter(ParameterType type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}

	public ParameterType getType() {
		return type;
	}

	public void setType(ParameterType type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
