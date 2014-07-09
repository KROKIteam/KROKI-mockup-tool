package adapt.utils;

import java.util.ArrayList;

public class EntityClass {
	
	private String name;
	private ArrayList<EntityProperty> properties;
	
	public EntityClass(String name, ArrayList<EntityProperty> properties) {
		this.name = name;
		this.properties = properties;
	}

	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<EntityProperty> getProperties() {
		return properties;
	}
	
	public void setProperties(ArrayList<EntityProperty> properties) {
		this.properties = properties;
	}
}