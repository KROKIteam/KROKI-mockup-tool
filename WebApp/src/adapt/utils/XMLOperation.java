package adapt.utils;

public class XMLOperation {
	
	boolean allowed;
	String label;
	String name;
	String target;
	String type;
	String confirmText;
	
	public XMLOperation(boolean allowed, String label, String name,
			String target, String type, String confirmText) {
		super();
		this.allowed = allowed;
		this.label = label;
		this.name = name;
		this.target = target;
		this.type = type;
		this.confirmText = confirmText;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
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

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConfirmText() {
		return confirmText;
	}

	public void setConfirmText(String confirmText) {
		this.confirmText = confirmText;
	}
}
