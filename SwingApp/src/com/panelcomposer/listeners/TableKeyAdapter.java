package com.panelcomposer.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.panelcomposer.elements.STable;
import com.panelcomposer.enumerations.StateMode;


public class TableKeyAdapter extends KeyAdapter {
	
	protected STable table;
	
	public TableKeyAdapter(STable table) {
		this.table = table;
	}
	
	@Override
	public void keyReleased(KeyEvent event) {
		StateMode state = table.getPanel().getModelPanel().getPanelSettings().getStateMode();
		if (table.isEnabled() && state != StateMode.SEARCH) {
			if (table.getTableModel().getRowCount() > 0) {
				table.getPanel().refreshInputPanel();
			}
		}
	}

	public STable getTable() {
		return table;
	}

	public void setTable(STable table) {
		this.table = table;
	}
	
	
}
