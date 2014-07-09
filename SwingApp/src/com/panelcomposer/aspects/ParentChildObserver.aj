package com.panelcomposer.aspects;

import util.EntityHelper;

import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.exceptions.EntityAttributeNotFoundException;
import com.panelcomposer.model.attribute.JoinColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;
import com.panelcomposer.observe.ObserverPanel;

public aspect ParentChildObserver {
	
	public void SPanel.doOnUpdateTable(SPanel panel) {
		EntityBean ebParent = panel.getTable().getTableModel().getEntityBean();
		EntityBean ebChild = this.getTable().getTableModel().getEntityBean();
		String where = " WHERE x.";
		int column = 0;
		try {
			JoinColumnAttribute jcAttr = null;
			jcAttr = (JoinColumnAttribute) EntityHelper
					.getJoinByLookup(ebChild,
							ebParent.getEntityClass());
			where += jcAttr.getFieldName() + "." 
				+ jcAttr.getZoomedBy() + " = '";
			column = EntityHelper.getIndexOf(ebParent, jcAttr.getZoomedByAsColumn());
		} catch (EntityAttributeNotFoundException e) {
			e.printStackTrace();
			return;
		}
		int selRow = panel.getTable().getSelectedRow();
		Object value = panel.getTable().getTableModel()
				.getValueAt(selRow, column);
		if (value != null) {
			where += value.toString() + "'";
		} else {
			where += "'";
		}
		this.getModelPanel().getDataSettings().setDataFilter(where);
		this.loadData(null);
		java.util.Iterator<ObserverPanel> it = this.observers.iterator();
		while (it.hasNext()) {
			((ObserverPanel) it.next()).updateTable(this);
		}
	}
}
