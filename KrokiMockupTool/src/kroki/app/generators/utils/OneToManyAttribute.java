package kroki.app.generators.utils;

/**
 * Class is used to define mapping of OneToMany associations from the UI profile to ManyToMany 
 * attributes inside the freemarker template. 
 * Helper class to make generation of persistent layer easier and is not a part of the UI profile.
 * @author Milorad Filipović
 */
public class OneToManyAttribute {

	private String name;
	private String label;
	private String refferencedTable;
	private String mappedBy;
	
	public OneToManyAttribute(String name, String label,
			String refferencedTable, String mappedBy) {
		super();
		this.name = name;
		this.label = label;
		this.refferencedTable = refferencedTable;
		this.mappedBy = mappedBy;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRefferencedTable() {
		return refferencedTable;
	}

	public void setRefferencedTable(String refferencedTable) {
		this.refferencedTable = refferencedTable;
	}

	public String getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
	}
	
}
