package adapt.utils;

import java.util.ArrayList;

public class XMLAttribute {

	private String name;
	private String databaseName;
	private String label;
	private String type;
	private Boolean unique;
	private Boolean mandatory;
	private Boolean representative;
	private ArrayList<String> values;
	
	public XMLAttribute(String name, String databaseName, String label,
			String type, Boolean unique, Boolean mandatory, Boolean representative) {
		super();
		this.name = name;
		this.databaseName = databaseName;
		this.label = label;
		this.type = type;
		this.unique = unique;
		this.mandatory = mandatory;
		this.representative = representative;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getUnique() {
		return unique;
	}

	public void setUnique(Boolean unique) {
		this.unique = unique;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public Boolean getRepresentative() {
		return representative;
	}

	public void setRepresentative(Boolean representative) {
		this.representative = representative;
	}

	public ArrayList<String> getValues() {
		return values;
	}

	public void setValues(ArrayList<String> values) {
		this.values = values;
	}
}
