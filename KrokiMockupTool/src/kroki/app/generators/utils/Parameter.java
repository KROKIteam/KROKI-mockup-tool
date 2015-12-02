package kroki.app.generators.utils;

public class Parameter{
	
	private String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Parameter(String name, String type) {
		this.name = name;
		this.type = type;
	}

}
