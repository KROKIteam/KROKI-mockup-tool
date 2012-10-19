package adapt.utils;

public class XMLManyToManyAttribute {

	private String name;
	private String databaseName;
	private String label;
	private String type;
	private String joinTable;
	private String joinColumns;
	private String inverseJoinColumns;
	
	public XMLManyToManyAttribute(String name, String databaseName,
			String label, String type, String joinTable, String joinColumns,
			String inverseJoinColumns) {
		super();
		this.name = name;
		this.databaseName = databaseName;
		this.label = label;
		this.type = type;
		this.joinTable = joinTable;
		this.joinColumns = joinColumns;
		this.inverseJoinColumns = inverseJoinColumns;
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

	public String getJoinTable() {
		return joinTable;
	}

	public void setJoinTable(String joinTable) {
		this.joinTable = joinTable;
	}

	public String getJoinColumns() {
		return joinColumns;
	}

	public void setJoinColumns(String joinColumns) {
		this.joinColumns = joinColumns;
	}

	public String getInverseJoinColumns() {
		return inverseJoinColumns;
	}

	public void setInverseJoinColumns(String inverseJoinColumns) {
		this.inverseJoinColumns = inverseJoinColumns;
	}
}
