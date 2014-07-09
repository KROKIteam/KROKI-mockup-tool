package kroki.app.generators.utils;

/**
 * Class that represents enumeration which holds values for combo boxes.
 * @author Milorad Filipovic
 *
 */
public class Enumeration {

	/**
	 * Enumeration name and label.
	 */
	private String name;
	private String label;
	/**
	 * Class containing property with enumerated values.
	 */
	private String refferencedClass;
	/**
	 *Name of the property that needs combo box.
	 *Property is identified using property name and class name.
	 **/
	private String refferencedProp;
	/**
	 * Enumeration values.
	 */
	private String[] values;
	
	public Enumeration() {
	}

	public Enumeration(String name, String label, String refferencedClass, String refferencedProp, String[] values) {
		this.name = name;
		this.label = label;
		this.refferencedClass = refferencedClass;
		this.refferencedProp = refferencedProp;
		this.values = values;
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

	public String getRefferencedClass() {
		return refferencedClass;
	}
	
	public void setRefferencedClass(String refferencedClass) {
		this.refferencedClass = refferencedClass;
	}
	
	public String getRefferencedProp() {
		return refferencedProp;
	}
	
	public void setRefferencedProp(String refferencedProp) {
		this.refferencedProp = refferencedProp;
	}
	
	public String[] getValues() {
		return values;
	}
	
	public void setValues(String[] values) {
		this.values = values;
	}
}
