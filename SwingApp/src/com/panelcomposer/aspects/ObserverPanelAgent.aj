package com.panelcomposer.aspects;

import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.listeners.TableKeyAdapter;
import com.panelcomposer.listeners.TableMouseAdapter;
import com.panelcomposer.observe.ObserverPanel;

public aspect ObserverPanelAgent {

	java.util.List<ObserverPanel> SPanel.observers = new java.util.ArrayList<ObserverPanel>();

	public void ObserverPanel.updateTable(SPanel panel) {
		doOnUpdateTable(panel);
	}

	public void SPanel.addObserver(ObserverPanel observer) {
		observers.add(observer);
	}

	public void SPanel.removeObserver(ObserverPanel observer) {
		observers.remove(observer);
	}

	/***
	 * Listening on mouse event on table
	 * 
	 * @param adapter
	 *            TableMouseAdapter
	 */
	after(TableMouseAdapter adapter) : execution (* TableMouseAdapter.mouseReleased(..)) && target(adapter) {
		java.util.Iterator<ObserverPanel> i = adapter.getTable().getPanel().observers
				.iterator();
		while (i.hasNext()) {
			((ObserverPanel) i.next()).updateTable(adapter.getTable()
					.getPanel());
		}
	}

	/***
	 * Listening on key event on table
	 * 
	 * @param adapter
	 *            TableMouseAdapter
	 */
	after(TableKeyAdapter adapter) : execution (* TableKeyAdapter.keyReleased(..)) && target(adapter) {
		java.util.Iterator<ObserverPanel> i = adapter.getTable().getPanel().observers
				.iterator();
		while (i.hasNext()) {
			((ObserverPanel) i.next()).updateTable(adapter.getTable()
					.getPanel());
		}
	}

}
