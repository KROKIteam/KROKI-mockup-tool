package adapt.model.panel.configuration;

import adapt.enumerations.PanelType;
import adapt.model.AbstractElement;

public class Zoom extends AbstractElement {

	protected String panelId;
	protected PanelType panelType;
	
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
}
