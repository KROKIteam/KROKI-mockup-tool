package adapt.model.menu;

import adapt.enumerations.PanelType;
import adapt.model.AbstractElement;

public class AdaptSubMenu extends AbstractElement {

	protected String activate;
	protected PanelType panelType;
	
	public String getActivate() {
		return activate;
	}
	public void setActivate(String activate) {
		this.activate = activate;
	}
	public PanelType getPanelType() {
		return panelType;
	}
	public void setPanelType(PanelType panelType) {
		this.panelType = panelType;
	}
}
