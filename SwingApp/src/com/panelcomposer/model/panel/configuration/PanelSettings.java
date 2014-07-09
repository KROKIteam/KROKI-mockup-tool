package com.panelcomposer.model.panel.configuration;

import com.panelcomposer.enumerations.OpenedAs;
import com.panelcomposer.enumerations.StateMode;
import com.panelcomposer.enumerations.ViewMode;

public class PanelSettings {

	protected Boolean add = true;
	protected Boolean update = true;
	protected Boolean copy = true;
	protected Boolean delete = true;
	protected Boolean changeMode = true;
	protected Boolean dataNavigation = true;
	protected ViewMode viewMode = ViewMode.TABLEVIEW;
	protected Boolean hideToolbar = false;
	protected StateMode stateMode = StateMode.UPDATE;
	protected OpenedAs openedAs = OpenedAs.DEFAULT;

	public PanelSettings() {
		
	}

	public Boolean getAdd() {
		return add;
	}

	public void setAdd(Boolean add) {
		this.add = add;
	}

	public Boolean getUpdate() {
		return update;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}

	public Boolean getCopy() {
		return copy;
	}

	public void setCopy(Boolean copy) {
		this.copy = copy;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public Boolean getChangeMode() {
		return changeMode;
	}

	public void setChangeMode(Boolean changeMode) {
		this.changeMode = changeMode;
	}

	public Boolean getDataNavigation() {
		return dataNavigation;
	}

	public void setDataNavigation(Boolean dataNavigation) {
		this.dataNavigation = dataNavigation;
	}

	public ViewMode getViewMode() {
		return viewMode;
	}

	public void setViewMode(ViewMode gridView) {
		this.viewMode = gridView;
	}

	public Boolean getHideToolbar() {
		return hideToolbar;
	}

	public void setHideToolbar(Boolean hideToolbar) {
		this.hideToolbar = hideToolbar;
	}

	public StateMode getStateMode() {
		return stateMode;
	}

	public void setStateMode(StateMode stateMode) {
		this.stateMode = stateMode;
	}

	public OpenedAs getOpenedAs() {
		return openedAs;
	}

	public void setOpenedAs(OpenedAs openedAs) {
		this.openedAs = openedAs;
	}

}
