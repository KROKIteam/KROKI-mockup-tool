package com.panelcomposer.model.panel.configuration;

import com.panelcomposer.enumerations.PanelType;
import com.panelcomposer.model.AbstractElement;

public class Zoom extends AbstractElement {

	/**
	 * Original panel id
	 */
	protected String panelId;
	/***
	 * Type of panel
	 */
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
