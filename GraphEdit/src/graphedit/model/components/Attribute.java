package graphedit.model.components;

import graphedit.app.MainFrame;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.properties.ApplicationModeProperties;
import graphedit.util.Validator;

import java.io.Serializable;
import java.util.List;

import kroki.uml_core_basic.UmlProperty;


public class Attribute implements Serializable {

	private static final long serialVersionUID = 1L;

	private static int instanceCounter = 1;
	
	private String name;
	
	private String type;
	
	private boolean staticAttribute = false;
	
	private boolean finalAttribute = false;
	
	private Modifier modifier = Modifier.PRIVATE;
	
	private boolean visible = true;
	
	private UmlProperty umlProperty;
	
	private ApplicationModeProperties appModeProperties;
	
	/*
	 * UmlProperty nema modifier, static, final, pa ne moze za sada drugacije
	 */

	public Attribute() { 
		
		appModeProperties = ApplicationModeProperties.getInstance();
		
		name = (String) appModeProperties.getPropertyValue("attributeName") + instanceCounter++;
		if (MainFrame.getInstance().getCurrentView() != null){
			@SuppressWarnings("unchecked")
			List<Attribute> attributes = (List<Attribute>) MainFrame.getInstance().getCurrentView().getSelectionModel().getSelectedElements().get(0).getProperty(GraphElementProperties.ATTRIBUTES);
			while (Validator.attributeHasName(attributes, name))
				name = (String) appModeProperties.getPropertyValue("attributeName") + MainFrame.getInstance().incrementClassCounter();
		}
		type = (String) appModeProperties.getPropertyValue("attributeType");
		
	}
	
	public Attribute(String name, String type) {
		this.name = name;
		this.type = type;
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

	public boolean isStaticAttribute() {
		return staticAttribute;
	}

	public void setStaticAttribute(boolean staticAttribute) {
		this.staticAttribute = staticAttribute;
	}

	public boolean isFinalAttribute() {
		return finalAttribute;
	}

	public void setFinalAttribute(boolean finalAttribute) {
		this.finalAttribute = finalAttribute;
	}

	public Modifier getModifier() {
		return modifier;
	}

	public void setModifier(Modifier modifier) {
		this.modifier = modifier;
	}

	@Override
	public String toString() {
		return modifier + " " + name + " : " + type;
	}

	public UmlProperty getUmlProperty() {
		return umlProperty;
	}

	public void setUmlProperty(UmlProperty umlProperty) {
		this.umlProperty = umlProperty;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	
}
