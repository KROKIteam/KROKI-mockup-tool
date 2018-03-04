package graphedit.model.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.properties.ApplicationModeProperties;
import graphedit.util.Validator;
import kroki.uml_core_basic.UmlProperty;


public class Attribute implements Serializable {

	private static final long serialVersionUID = 1L;

	private static int instanceCounter = 1;
	
	private String name;
	
	/**
	 * Component type in UI modes
	 */
	private String type;
	
	private boolean staticAttribute = false;
	
	private boolean finalAttribute = false;
	
	private Modifier modifier = Modifier.PRIVATE;
	
	private boolean visible = true;
	
	private UmlProperty umlProperty;
	
	private ApplicationModeProperties appModeProperties;
	
	private List<String> possibleValues;
	
	private String dataType;
	
	/*
	 * UmlProperty nema modifier, static, final, pa ne moze za sada drugacije
	 */

	public Attribute() { 
		
		appModeProperties = ApplicationModeProperties.getInstance();
		possibleValues = new ArrayList<String>();
		
		name = (String) appModeProperties.getPropertyValue("attributeName") + instanceCounter++;
		if (MainFrame.getInstance().getCurrentView() != null){
			@SuppressWarnings("unchecked")
			List<Attribute> attributes = (List<Attribute>) MainFrame.getInstance().getCurrentView().getSelectionModel().getSelectedElements().get(0).getProperty(GraphElementProperties.ATTRIBUTES);
			while (Validator.attributeHasName(attributes, name))
				name = (String) appModeProperties.getPropertyValue("attributeName") + instanceCounter++;
		}
		type = (String) appModeProperties.getPropertyValue("attributeType");
		
		dataType = (String) appModeProperties.getPropertyValue("attributeDataType");
		
	}
	
	public Attribute(String name, String type) {
		possibleValues = new ArrayList<String>();
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
		if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE ||
				MainFrame.getInstance().getAppMode() == ApplicationMode.PERSISTENT)
			return modifier + " " + name + " : " + type;
		else if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE_MIXED)
			return modifier + " " + name + " : " + type + " (" + getDataType() + ")";
		else
			return modifier + " " + name + " : "  + getDataType();
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

	public List<String> getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(List<String> possibleValues) {
		this.possibleValues = possibleValues;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	
}
