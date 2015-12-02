package kroki.app.generators.utils;

public class DerivedValue {
	
	private String propertyName;
	private String type;
	private String returnValue;
	public DerivedValue(String propertyName, String type, String returnValue) {
		super();
		this.propertyName = propertyName;
		this.type = type;
		this.returnValue = returnValue;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

}
