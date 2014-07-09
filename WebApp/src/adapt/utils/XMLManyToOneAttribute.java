package adapt.utils;

public class XMLManyToOneAttribute {
	
	private String name;
	private String databaseName;
	private String label;
	private String type;
	private Boolean mandatory;
	
	public XMLManyToOneAttribute(String name, String databaseName,
			String label, String type, Boolean mandatory) {
		super();
		this.name = name;
		this.databaseName = databaseName;
		this.label = label;
		this.type = type;
		this.mandatory = mandatory;
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
}
