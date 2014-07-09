package com.panelcomposer.aspects.sform;

import javax.swing.JFrame;

import com.panelcomposer.elements.SForm;
import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.enumerations.OpenedAs;
import com.panelcomposer.model.panel.MStandardPanel;

public aspect StandardPanelAspect {

	public pointcut initStandard(SForm form) : 
		(initialization(SForm.new(MStandardPanel, SPanel, OpenedAs, String, JFrame)) 
				|| initialization(SForm.new(MStandardPanel, SPanel, OpenedAs, String))) 
				&& target(form);

	after(SForm form) : initStandard(form) {
		SPanel ownerPanel = (SPanel) thisJoinPoint.getArgs()[1];
		OpenedAs oa = (OpenedAs) thisJoinPoint.getArgs()[2];
		String filter = (String) thisJoinPoint.getArgs()[3];
		SPanel sp = new SPanel(form.getMpanel(), form, ownerPanel, oa, filter);
		form.getPanels().add(sp);
		form.addToPane(sp);
		form.setTitle(((MStandardPanel) form.getMpanel()).getEntityBean()
				.getLabel());
	}
}
