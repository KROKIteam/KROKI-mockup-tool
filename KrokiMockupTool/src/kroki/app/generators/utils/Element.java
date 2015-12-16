package kroki.app.generators.utils;

public class Element {
	
	private String name;
	
	public Element(String name) {
		this.name = name;
	}
	public Element(){}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean hasName() {
		return name != null;
	}
}
