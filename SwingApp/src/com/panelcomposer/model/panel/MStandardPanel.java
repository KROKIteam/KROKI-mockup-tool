package com.panelcomposer.model.panel;

import java.util.ArrayList;
import java.util.List;

import com.panelcomposer.model.ejb.EntityBean;
import com.panelcomposer.model.panel.configuration.DataSettings;
import com.panelcomposer.model.panel.configuration.Next;
import com.panelcomposer.model.panel.configuration.PanelSettings;
import com.panelcomposer.model.panel.configuration.Zoom;
import com.panelcomposer.model.panel.configuration.operation.SpecificOperations;


public class MStandardPanel extends MPanel {

	protected EntityBean entityBean;
	protected Integer level;
	protected PanelSettings panelSettings;
	protected SpecificOperations standardOperations;
	protected DataSettings dataSettings;
	protected List<Next> nextPanels = new ArrayList<Next>();
	protected List<Zoom> zoomPanels = new ArrayList<Zoom>();

	public EntityBean getEntityBean() {
		return entityBean;
	}

	public void setEntityBean(EntityBean entityBean) {
		this.entityBean = entityBean;
	}

	public PanelSettings getPanelSettings() {
		return panelSettings;
	}

	public void setPanelSettings(PanelSettings panelSettings) {
		this.panelSettings = panelSettings;
	}

	public SpecificOperations getStandardOperations() {
		return standardOperations;
	}

	public void setStandardOperations(SpecificOperations standardOperations) {
		this.standardOperations = standardOperations;
	}

	public DataSettings getDataSettings() {
		return dataSettings;
	}

	public void setDataSettings(DataSettings dataSettings) {
		this.dataSettings = dataSettings;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<Next> getNextPanels() {
		return nextPanels;
	}

	public void setNextPanels(List<Next> nextPanels) {
		this.nextPanels = nextPanels;
	}

	public List<Zoom> getZoomPanels() {
		return zoomPanels;
	}

	public void setZoomPanels(List<Zoom> zoomPanels) {
		this.zoomPanels = zoomPanels;
	}

}
