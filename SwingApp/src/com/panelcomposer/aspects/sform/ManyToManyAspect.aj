package com.panelcomposer.aspects.sform;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.EntityHelper;
import util.resolvers.ComponentResolver;
import util.staticnames.Settings;

import com.panelcomposer.elements.SForm;
import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.enumerations.ViewMode;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.attribute.JoinColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;
import com.panelcomposer.model.panel.MManyToManyPanel;
import com.panelcomposer.model.panel.MStandardPanel;

public aspect ManyToManyAspect {

	public pointcut initManyToMany(SForm form) : 
		initialization(SForm.new(MManyToManyPanel, ..)) && target(form);

	after(final SForm form) : initManyToMany(form) {
		MManyToManyPanel mtmp = (MManyToManyPanel) form.getMpanel();
		List<SPanel> panelList = new ArrayList<SPanel>();

		MStandardPanel msp = mtmp.findByLevel(1);
		msp.getPanelSettings().setHideToolbar(true);
		msp.getPanelSettings().setViewMode(ViewMode.INPUTPANELVIEW);
		SPanel sp = new SPanel(msp, form);
		sp.getLockableUI().setLocked(true);
		panelList.add(sp);
		String label = msp.getEntityBean().getLabel();
		form.addToPane(new JLabel(label));
		form.addToPane(sp);
		form.getPanels().add(sp);
		msp = mtmp.findByLevel(2);
		sp = new SPanel(msp, form);
		SPanel ownerPanel = (SPanel) thisJoinPoint.getArgs()[1];
		int index = 0;
		if (ownerPanel != null) {
			System.out.println("ownerpanel is not null");
			index = ownerPanel.getTable().getSelectedRow();
		}
		sp.getTable().setRowSelectionInterval(index, index);
		System.out.println("ManyToManyAspect.after() index = " + index);
		panelList.add(sp);
		label = msp.getEntityBean().getLabel();
		form.addToPane(new JLabel(label));
		form.addToPane(sp);
		form.getPanels().add(sp);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JButton btnInsert = new JButton(new ImageIcon(getClass().getResource(
				Settings.iconsDirectory + "insert_to.gif")));
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					SPanel upPanel = form.getPanels().get(0);
					SPanel middlePanel = form.getPanels().get(1);
					SPanel downPanel = form.getPanels().get(2);
					EntityBean downBean = downPanel.getModelPanel()
							.getEntityBean();
					JoinColumnAttribute jca = null;
					EntityBean bean = upPanel.getModelPanel().getEntityBean();
					jca = EntityHelper.getJoinByLookup(downBean,
							bean.getEntityClass());
					Object downObject = null;
					if (jca != null) {
						ColumnAttribute ca = jca.getZoomedByAsColumn();
						int index = EntityHelper.getIndexOf(bean, ca);
						downObject = downBean.getEntityClass().getConstructor()
								.newInstance();
						JComponent comp = upPanel.getInputPanel()
								.getPanelComponents().get(index);
						ComponentResolver.invokeMethod(comp, jca, ca,
								downObject, downBean);
						downBean = downPanel.getModelPanel().getEntityBean();
						bean = middlePanel.getModelPanel().getEntityBean();
						jca = EntityHelper.getJoinByLookup(downBean,
								bean.getEntityClass());
						if (jca != null) {
							ca = jca.getZoomedByAsColumn();
							index = EntityHelper.getIndexOf(bean, ca);
							comp = middlePanel.getInputPanel()
									.getPanelComponents().get(index);
							ComponentResolver.invokeMethod(comp, jca, ca,
									downObject, downBean);
							downPanel.getTable().getTableModel()
									.add(downObject);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		JButton btnRemove = new JButton(new ImageIcon(getClass().getResource(
				Settings.iconsDirectory + "remove.gif")));
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				form.getPanels().get(2).getTable().handleDelete();
			}
		});

		panel.add(btnInsert);
		panel.add(btnRemove);
		form.addToPane(panel);

		msp = mtmp.findByLevel(3);
		msp.getPanelSettings().setViewMode(ViewMode.TABLEVIEW);
		msp.getPanelSettings().setHideToolbar(true);
		sp = new SPanel(msp, form);
		panelList.add(sp);
		label = msp.getEntityBean().getLabel();

		form.addToPane(new JLabel(label));
		form.addToPane(sp);
		form.getPanels().add(sp);
		form.setTitle(mtmp.getLabel());
	}

}
