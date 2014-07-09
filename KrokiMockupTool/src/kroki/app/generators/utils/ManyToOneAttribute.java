package kroki.app.generators.utils;

import java.util.ArrayList;


/**
 * Klasa koja sluzi za mapiranje ManyToOne veze iz UI profila na ManyToOne atribut u freemarker sablonu.
 * Ova klasa sluzi samo kao pomocna klasa za lakse generisanje perzistentnog sloja i nije deo UI profila.
 * @author mrd
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
