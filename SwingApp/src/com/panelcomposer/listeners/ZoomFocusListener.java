package com.panelcomposer.listeners;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JComponent;
import javax.swing.JTextField;

import util.PersistenceHelper;
import util.StringUtil;
import util.staticnames.Messages;

import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.attribute.JoinColumnAttribute;

public class ZoomFocusListener extends FocusAdapter {

	protected JoinColumnAttribute joinColAttr;
	protected JComponent zoomedBy;
	protected JComponent lookup;

	public ZoomFocusListener(JoinColumnAttribute joinColAttr,
			JComponent zoomedBy, JComponent lookup) {
		this.joinColAttr = joinColAttr;
		this.zoomedBy = zoomedBy;
		this.lookup = lookup;
	}

	@Override
	public void focusLost(FocusEvent event) {
		EntityManager em = PersistenceHelper.createEntityManager();
		try {
			String zoomedValue = ((JTextField) zoomedBy).getText().trim();
			Class<?> lookupClass = joinColAttr.getLookupClass();
			String className = joinColAttr.getLookupClass().getName();
			ColumnAttribute zoomedByCol = joinColAttr.getZoomedByAsColumn();
			if(zoomedByCol == null) 
				return;			
			String fieldName = zoomedByCol.getFieldName();
			if (zoomedValue == null || zoomedValue.trim().equals(""))
				((JTextField) lookup).setText(Messages.LOOKUP_NOT_FOUND);
			String query = "SELECT x FROM " + className + " x  WHERE x."
				+ fieldName + " = '" + zoomedValue + "'";
			System.out.println(query);
			Query q = em.createQuery(query);
			Object entity = lookupClass.cast(q.getSingleResult());			
			String methodGet = null;
			Method method = null;
			for (int i = 0; i < joinColAttr.getColumns().size(); i++) {
				// if it's not zoomed-by attribute
				if(!joinColAttr.getColumns().get(i).getName().equals(joinColAttr.getZoomedBy())) {
					String fname = joinColAttr.getColumns().get(i).getFieldName();
					methodGet = "get" + StringUtil.capitalize(fname);
					method = lookupClass.cast(entity).getClass().getMethod(methodGet);
					String s = method.invoke(
							joinColAttr.getLookupClass().cast(entity)).toString();
					((JTextField) lookup).setText(s);
				}
			}
		} catch (Exception e) {
			System.out.println("nije dobavljena vrednost zumom");
		} finally {
			em.close();
		}
	}
}
