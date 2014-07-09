package com.panelcomposer.aspects.sform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;

import com.panelcomposer.elements.SForm;
import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.model.panel.MParentChildPanel;
import com.panelcomposer.model.panel.MStandardPanel;

public aspect ParentChildAspect {

	public pointcut initParentChild(SForm form) : 
		initialization(SForm.new(MParentChildPanel, ..)) && target(form);

	after(SForm form) : initParentChild(form) {
		MParentChildPanel mpcp = (MParentChildPanel) form.getMpanel();
		List<SPanel> panelList = new ArrayList<SPanel>();
		for (int i = 0; i < mpcp.getPanelCount(); i++) {
			MStandardPanel msp = mpcp.getPanels().get(i);
			String label = msp.getEntityBean().getLabel();
			SPanel sp = new SPanel(msp, form);
			if (msp.getLevel() != 1) {
				Iterator<SPanel> pIt = panelList.iterator();
				while (pIt.hasNext()) {
					SPanel spParent = pIt.next();
					if (spParent.getModelPanel().getLevel().intValue() == msp
							.getLevel() - 1) {
						spParent.addObserver(sp);
					}
				}
			}
			panelList.add(sp);
			form.getPanels().add(sp);
			form.addToPane(new JLabel(label));
			form.addToPane(sp);
			form.setTitle(mpcp.getLabel());
		}
	}
}
