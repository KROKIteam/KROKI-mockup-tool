package com.panelcomposer.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import util.EntityHelper;
import util.staticnames.Messages;
import util.xml_readers.PanelReader;

import com.panelcomposer.elements.SForm;
import com.panelcomposer.elements.STModel;
import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.enumerations.OpenedAs;
import com.panelcomposer.enumerations.PanelType;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.attribute.JoinColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;
import com.panelcomposer.model.panel.MManyToManyPanel;
import com.panelcomposer.model.panel.MStandardPanel;
import com.panelcomposer.model.panel.configuration.Next;

public class NextActionListener implements ActionListener {

	private Next next;
	private SPanel panel;

	public NextActionListener(Next next, SPanel panel) {
		this.next = next;
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String where = " ";
		PanelType panelType = next.getPanelType();
		STModel tableModel = panel.getTable().getTableModel();
		String idNext = next.getName();
		String panelId = next.getPanelId();
		int rowIndex = panel.getTable().getSelectedRow();
		if (rowIndex != -1) {
			try {
				panel.getTable().getTableModel().setCurrentRow(rowIndex);
				if (panelType.equals(PanelType.MANYTOMANYPANEL)) {
					System.out.println("nextaction : many2many ");
					MManyToManyPanel mtmp = (MManyToManyPanel) PanelReader.loadPanel(
							panelId, PanelType.MANYTOMANYPANEL, idNext, OpenedAs.NEXT);
					SForm sf = new SForm(mtmp, panel);
					sf.setVisible(true);
					
				} else if (panelType.equals(PanelType.STANDARDPANEL)) {
					MStandardPanel msp = (MStandardPanel) PanelReader.loadPanel(
							panelId, PanelType.STANDARDPANEL, idNext,  OpenedAs.NEXT);
					Class<?> lookup = tableModel.getEntityBean().getEntityClass();
					JoinColumnAttribute jca = null;
					jca = EntityHelper.getJoinByLookup(msp.getEntityBean(), lookup);
					String value = "";
					EntityBean ejb = tableModel.getEntityBean();
					ColumnAttribute keyAttr = EntityHelper.getKeyAsColumn(
							tableModel.getEntityBean());
					String fieldName = jca.getZoomedByAsColumn().getFieldName();
					value = tableModel.getValueAt(rowIndex,
									EntityHelper.getIndexOf(ejb, keyAttr)).toString();
					where += "WHERE x." + jca.getFieldName() 
						+ "." + fieldName + "= '" + value + "'";
					SForm sf = new SForm(msp, panel, OpenedAs.NEXT, where);
					sf.setVisible(true);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(panel.getForm(), Messages.NEXT_ERROR, 
						Messages.ERROR, JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
