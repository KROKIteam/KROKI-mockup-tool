package kroki.app.generators.utils;

/**
 * Class is used to define mapping of attributes of persistent classes of UI profile to an attribute
 * inside a freemarker template. Helper class to make generation of persistent layer easier and
 * is not a part of the UI profile.
 * @author Milorad Filipović
 */
public class Attribute {
	
	private String name;
	private String databaseName;
	private String label;
	private String type;
	private Boolean unique;
	private Boolean mandatory;
	private Boolean representative;
	private Enumeration enumeration;
	
	public Attribute(String name, String databaseName, String label,
			String type, Boolean unique, Boolean mandatory, Boolean representative, Enumeration enumeration) {
		super();
		this.name = name;
		this.databaseName = databaseName;
		this.label = label;
		this.type = type;
		this.unique = unique;
		this.mandatory = mandatory;
		this.representative = representative;
		this.enumeration = enumeration;
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

	public Enumeration getEnumeration() {
		return enumeration;
	}

	public void setEnumeration(Enumeration enumeration) {
		this.enumeration = enumeration;
	}
}
