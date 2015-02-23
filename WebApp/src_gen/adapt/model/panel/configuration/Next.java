package adapt.model.panel.configuration;

import adapt.enumerations.PanelType;
import adapt.model.AbstractElement;

public class Next extends AbstractElement {
	
	// Original panel ID
	protected String panelId;
	protected PanelType panelType;
	protected String parentGroup;
	protected String opposite;
	
	public String getPanelId() {
		return panelId;
	}
	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}
	public PanelType getPanelType() {
		return panelType;
	}
	public void setPanelType(PanelType panelType) {
		this.panelType = panelType;
	}
	public String getParentGroup() {
		return parentGroup;
	}
	public void setParentGroup(String parentGroup) {
		this.parentGroup = parentGroup;
	}
	public String getOpposite() {
		return opposite;
	}
	public void setOpposite(String opposite) {
		this.opposite = opposite;
	}
}