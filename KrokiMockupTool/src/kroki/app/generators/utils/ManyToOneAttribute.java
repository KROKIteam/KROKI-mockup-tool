package kroki.app.generators.utils;

import java.util.ArrayList;


/**
 * Class is used to define mapping of ManyToOne associations from the UI profile to ManyToOne 
 * attributes inside the freemarker template. 
 * Helper class to make generation of persistent layer easier and is not a part of the UI profile.
 * @author Milorad Filipović
 */
public class ManyToOneAttribute {
	
	private String name;
	private String databaseName;
	private String label;
	private String type;
	private Boolean mandatory;
	private ArrayList<Attribute> columnRefs;
	
	public ManyToOneAttribute(String name, String databaseName,
			String label, String type, Boolean mandatory) {
		super();
		this.name = name;
		this.databaseName = databaseName;
		this.label = label;
		this.type = type;
		this.mandatory = mandatory;
		columnRefs = new ArrayList<Attribute>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public ArrayList<Attribute> getColumnRefs() {
		return columnRefs;
	}

	public void setColumnRefs(ArrayList<Attribute> columnRefs) {
		this.columnRefs = columnRefs;
	}
}
