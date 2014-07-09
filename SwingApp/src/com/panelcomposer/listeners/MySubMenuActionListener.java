package com.panelcomposer.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import util.xml_readers.PanelReader;

import com.panelcomposer.elements.SForm;
import com.panelcomposer.enumerations.OpenedAs;
import com.panelcomposer.enumerations.PanelType;
import com.panelcomposer.model.panel.MManyToManyPanel;
import com.panelcomposer.model.panel.MPanel;
import com.panelcomposer.model.panel.MParentChildPanel;
import com.panelcomposer.model.panel.MStandardPanel;

public class MySubMenuActionListener implements ActionListener {

	protected String act;
	protected PanelType panelType;
	protected JFrame frame;

	public MySubMenuActionListener(String act, PanelType panelType, JFrame frame) {
		this.act = act;
		this.panelType = panelType;
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent event) {
		MPanel mp = PanelReader.loadPanel(new String(act),  
				panelType, null, OpenedAs.DEFAULT);
		SForm sf = null;
		if(panelType.equals(PanelType.STANDARDPANEL)) {
			MStandardPanel msp = (MStandardPanel) mp;
			sf = new SForm(msp, null, OpenedAs.DEFAULT, null);
		} if(panelType.equals(PanelType.PARENTCHILDPANEL)) {
			MParentChildPanel mpcp = (MParentChildPanel) mp;
			sf = new SForm(mpcp, null);
		} else if(panelType.equals(PanelType.MANYTOMANYPANEL)) {
			MManyToManyPanel mmtmp = (MManyToManyPanel) mp;
			sf = new SForm(mmtmp, null);
		}
		sf.setVisible(true);
	}

}
