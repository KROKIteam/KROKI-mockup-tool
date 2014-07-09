package com.panelcomposer.aspects;

import com.panelcomposer.elements.STable;
import com.panelcomposer.observe.ObserverPanel;


public aspect STableAspect {
	
	/***
	 * pointcut
	 * @param table
	 */
	public pointcut goToRow(STable table) : execution (* STable.goTo*(..)) && target(table);

	before(STable table) : goToRow(table) {
		if (table.getPanel().getTable().getTableModel().getRowCount() <= 0) {
			return;
		}
	}
	
	after(STable table) : goToRow(table) {
		java.util.Iterator<ObserverPanel> i = table.getPanel().observers.iterator();
		while (i.hasNext()) {
			((ObserverPanel) i.next()).updateTable(table.getPanel());
		}
		table.getPanel().refreshInputPanel();
	}
	
}
