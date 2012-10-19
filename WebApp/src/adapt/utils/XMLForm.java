package adapt.utils;

public class XMLForm {

	String name;
	String fileName;
	Boolean allowActions;
	Boolean allowControls;
	
	public XMLForm(String name, String fileName, Boolean allowActions,
			Boolean allowControls) {
		super();
		this.name = name;
		this.fileName = fileName;
		this.allowActions = allowActions;
		this.allowControls = allowControls;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Boolean getAllowActions() {
		return allowActions;
	}

	public void setAllowActions(Boolean allowActions) {
		this.allowActions = allowActions;
	}

	public Boolean getAllowControls() {
		return allowControls;
	}

	public void setAllowControls(Boolean allowControls) {
		this.allowControls = allowControls;
	}
}
