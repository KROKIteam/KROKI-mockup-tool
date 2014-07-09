package com.panelcomposer.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.panelcomposer.elements.STable;
import com.panelcomposer.enumerations.StateMode;


public class TableMouseAdapter extends MouseAdapter {

	protected STable table;
	
	public TableMouseAdapter(STable table) {
		this.table = table;
	}

	@Override
	public void mouseReleased(MouseEvent ev) {
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
