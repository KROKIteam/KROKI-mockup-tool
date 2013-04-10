package kroki.app.generators.utils;

/**
 * Klasa koja sluzi za mapiranje atributa perzistentih klasa iz UI profila na atribut u freemarker sablonu.
 * Ova klasa sluzi samo kao pomocna klasa za lakse generisanje perzistentnog sloja i nije deo UI profila.
 * @author mrd
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
