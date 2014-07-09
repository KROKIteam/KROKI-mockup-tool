package com.panelcomposer.aspects;

import javax.swing.JToolBar;

import util.staticnames.Settings;

import com.panelcomposer.elements.STModel;
import com.panelcomposer.elements.STable;
import com.panelcomposer.elements.SToolBar;
import com.panelcomposer.elements.spanel.InputPanel;
import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.enumerations.StateMode;
import com.panelcomposer.enumerations.ViewMode;

public aspect SPanelAspect {
	
	public void SPanel.addToolBar() {
		if(getModelPanel().getPanelSettings().getHideToolbar() == false) {
			setToolbar(new SToolBar(JToolBar.HORIZONTAL, this));
			add(getToolbar(), "span, wrap");
		}
	}

	/***
	 * Adds input panel and table on the card panel 
	 * in order that is depending on <i>gridView</i> variable value
	 */
	public void SPanel.addPanelAndTable() {
		setTable(new STable(new STModel(getModelPanel().getEntityBean()), this));
		setInputPanel(new InputPanel(this));
		if (getModelPanel().getPanelSettings().getViewMode().equals(ViewMode.TABLEVIEW)) {
			getCardPanel().add(getTable().getScrollPane(), Settings.TABLE);
			getCardPanel().add(getInputPanel(), Settings.INPUT);
		} else {
			getCardPanel().add(getInputPanel(), Settings.INPUT);
			getCardPanel().add(getTable().getScrollPane(), Settings.TABLE);
		}
	}
	
	public pointcut load(SPanel spanel) : execution (void SPanel.loadData(..)) && target(spanel);
	
	after(SPanel spanel) : load(spanel) {
		if (spanel.getTable().getTableModel().getRowCount() > 0) {
			spanel.getTable().getTableModel().setCurrentRow(0);
		}
		if(spanel.getTable().getRowCount() > 0) {
			spanel.getTable().setRowSelectionInterval(0, 0);
		}
		if (spanel.getModelPanel().getPanelSettings().getStateMode() != StateMode.SEARCH) {
			spanel.refreshInputPanel();
		}
	}
	
	after(SPanel spanel) : call (* SPanel.handleAdd()) && target(spanel) {
		if(spanel.getToolbar() != null) {
			spanel.getToolbar().setEnablingOnToolBar(false);
		}	
		spanel.getModelPanel().getPanelSettings().setViewMode(ViewMode.INPUTPANELVIEW);
		spanel.getCardLayout().show(spanel.getCardPanel(), Settings.INPUT);
		java.util.Iterator<SPanel> i = spanel.getForm().getPanels().iterator();
		while (i.hasNext()) {
			SPanel sp = (SPanel) i.next();
			if(sp != spanel) {
				sp.getLockableUI().setLocked(true);
				sp.getToolbar().setEnablingOnToolBar(false);
			}
		}
		
	}
	
	after(SPanel spanel) : call (* SPanel.handleSearch()) && target(spanel) {
		if(spanel.getToolbar() != null) {
			spanel.getToolbar().setEnablingOnToolBar(false);
		}
	}
	
	before(SPanel spanel) : call (* SPanel.handleCancel()) && target(spanel) {
		if(spanel.getToolbar() != null) {
			spanel.getToolbar().setEnablingOnToolBar(true);
		}
		java.util.Iterator<SPanel> i = spanel.getForm().getPanels().iterator();
		while (i.hasNext()) {
			SPanel sp = (SPanel) i.next();
			if(sp != spanel) {
				System.out.println("lock" + sp.getModelPanel().getName());
				sp.getLockableUI().setLocked(false);
				sp.getToolbar().setEnablingOnToolBar(true);
			}
		}
	}
	
	
	
}
