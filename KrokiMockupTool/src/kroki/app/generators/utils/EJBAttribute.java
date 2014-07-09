package kroki.app.generators.utils;

import java.util.ArrayList;

public class EJBAttribute {

	private ArrayList<String> annotations;
	private String type;
	private String name;
	private String label;
	private String databaseName;
	private Boolean mandatory;
	private Boolean unique;
	private Boolean representative;
	private Enumeration enumeration;
	private ArrayList<EJBAttribute> columnRefs;
	
	public EJBAttribute(ArrayList<String> annotations, String type, String name,
			String label, String databaseName, Boolean mandatory,
			Boolean unique, Boolean representative, Enumeration enumeration) {
		super();
		this.annotations = annotations;
		this.type = type;
		this.name = name;
		this.label = label;
		this.databaseName = databaseName;
		this.mandatory = mandatory;
		this.unique = unique;
		this.representative = representative;
		this.enumeration = enumeration;
		columnRefs = new ArrayList<EJBAttribute>();
	}

	public ArrayList<String> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(ArrayList<String> annotations) {
		this.annotations = annotations;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public Boolean getUnique() {
		return unique;
	}

	public void setUnique(Boolean unique) {
		this.unique = unique;
	}

	public Boolean getRepresentative() {
		return representative;
	}

	public void setRepresentative(Boolean representative) {
		this.representative = representative;
	}

	public Enumeration getEnumeration() {
		return enumeration;
	}

	public void setEnumeration(Enumeration enumeration) {
		this.enumeration = enumeration;
	}

	public ArrayList<EJBAttribute> getColumnRefs() {
		return columnRefs;
	}

	public void setColumnRefs(ArrayList<EJBAttribute> columnRefs) {
		this.columnRefs = columnRefs;
	}
}
