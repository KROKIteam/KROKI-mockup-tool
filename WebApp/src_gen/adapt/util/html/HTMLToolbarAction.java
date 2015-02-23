package adapt.util.html;

/**
 * Class that represents one action button on standard form toolbar
 * @author Milorad Filipovic
 */
public class HTMLToolbarAction {

	String id;
	String toolTip;
	String imgPath;
	Boolean initiallyDiabled;
	
	public HTMLToolbarAction(String id, String toolTip, String imgPath, Boolean initiallyDisabled) {
		super();
		this.id = id;
		this.toolTip = toolTip;
		this.imgPath = imgPath;
		this.initiallyDiabled = initiallyDisabled;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public Boolean getInitiallyDiabled() {
		return initiallyDiabled;
	}

	public void setInitiallyDiabled(Boolean initiallyDiabled) {
		this.initiallyDiabled = initiallyDiabled;
	}
}
