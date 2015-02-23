package adapt.model;

public abstract class AbstractElement {
	
	protected String name;
	protected String label;
	
	public AbstractElement() {
		name = "";
		label = "";
	}
	
	public AbstractElement(String name, String label) {
		this.name = name;
		this.label = label;
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

}
