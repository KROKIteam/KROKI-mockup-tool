package com.panelcomposer.model.type;

public class Type {
	protected String languageType;
	protected String componentType;
	protected Class<?> argument;

	public Type() {
	}
	
	public Type(String languageType, String componentType, Class<?> argument) {
		super();
		this.languageType = languageType;
		this.componentType = componentType;
		this.argument = argument;
	}

	public String getLanguageType() {
		return languageType;
	}

	public void setLanguageType(String languageType) {
		this.languageType = languageType;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public Class<?> getArgument() {
		return argument;
	}

	public void setArgument(Class<?> argument) {
		this.argument = argument;
	}

}
