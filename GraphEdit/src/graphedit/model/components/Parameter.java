package graphedit.model.components;

import graphedit.app.MainFrame;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.properties.ApplicationModeProperties;
import graphedit.util.Validator;

import java.io.Serializable;
import java.util.List;

import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlParameter;

public class Parameter implements Serializable {

	private static final long serialVersionUID = 1L;

	private static int instanceCounter = 0;
	
	private String type;
	
	private String name;
	
	private boolean finalParameter;
	
	private UmlParameter umlParameter;
	
	private ApplicationModeProperties appModeProperties;
	
	
	public Parameter() { 
		
		appModeProperties = ApplicationModeProperties.getInstance();
		
		
		name = (String) appModeProperties.getPropertyValue("parameterName") + instanceCounter++;
		if (MainFrame.getInstance().getCurrentView() != null){
			@SuppressWarnings("unchecked")
			List<Attribute> attributes = (List<Attribute>) MainFrame.getInstance().getCurrentView().getSelectionModel().getSelectedElements().get(0).getProperty(GraphElementProperties.ATTRIBUTES);
			while (Validator.attributeHasName(attributes, name))
				name = (String) appModeProperties.getPropertyValue("parameterName") + instanceCounter++;
		}
		type = (String) appModeProperties.getPropertyValue("parameterType");
		
	}
	
	public Parameter(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public void setOperation(UmlOperation umlOperation){
		if (umlParameter != null)
			umlParameter.setOperation(umlOperation);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isFinalParameter() {
		return finalParameter;
	}

	public void setFinalParameter(boolean finalParameter) {
		this.finalParameter = finalParameter;
	}

	public UmlParameter getUmlParameter() {
		return umlParameter;
	}

	public void setUmlParameter(UmlParameter umlParameter) {
		this.umlParameter = umlParameter;
	}


}
