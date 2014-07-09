package com.panelcomposer.model.menu;

import com.panelcomposer.enumerations.PanelType;
import com.panelcomposer.model.AbstractElement;

public class MySubMenu extends AbstractElement {

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
