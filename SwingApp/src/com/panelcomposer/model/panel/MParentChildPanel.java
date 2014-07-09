package com.panelcomposer.model.panel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MParentChildPanel extends MPanel {
	protected List<MStandardPanel> panels = new ArrayList<MStandardPanel>();

	public void add(MStandardPanel mspanel) {
		panels.add(mspanel);
		System.out.println("PARENT-CHILD PANEL ADDING: " + mspanel.getName());
	}

	
	public int getPanelCount() {
		return panels.size();
	}

	public MStandardPanel findByLevel(Integer level) {
		Iterator<MStandardPanel> it = panels.iterator();
		while (it.hasNext()) {
			MStandardPanel msp = it.next();
			if (msp.getLevel().intValue() == level.intValue()) {
				return msp;
			}
		}
		return null;
	}
	
	public List<MStandardPanel> getPanels() {
		return panels;
	}

	public void setPanels(List<MStandardPanel> panels) {
		this.panels = panels;
	}

}
