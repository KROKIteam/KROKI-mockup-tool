package com.panelcomposer.elements;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.table.AbstractTableModel;

import util.EntityHelper;
import util.PersistenceHelper;
import util.StringUtil;

import com.panelcomposer.converters.ConverterUtil;
import com.panelcomposer.exceptions.EntityAttributeNotFoundException;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.attribute.JoinColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;

@SuppressWarnings("serial")
public class STModel extends AbstractTableModel {

	protected EntityBean entityBean;
	protected List<Object> queryList = new ArrayList<Object>();
	protected int currentRow;
	
	public STModel(EntityBean eb) {
		this.entityBean = eb;
	}

	public int getCurrentRow() {
		if (queryList.size() == 0)
			return -1;
		return currentRow;
	}

	public void setCurrentRow(int rowIndex) {
		currentRow = rowIndex;
	}

	public int getRowCount() {
		return queryList == null ? 0 : queryList.size();
	}

	public Object get(int rowIndex) {
		return queryList.get(rowIndex);
	}

	public void dispose() {
		queryList.clear();
	}

	@SuppressWarnings("unchecked")
	public void refreshData(String where) throws Exception {
		EntityManager em = PersistenceHelper.createEntityManager();
		try {
			String query = "SELECT x FROM "
					+ entityBean.getEntityClass().getName() + " x " + where;
			Query q = em.createQuery(query);
			queryList = q.getResultList();
			fireTableDataChanged();
		} catch (Exception e) {
			if (queryList != null)
				queryList.clear();
			throw e;
		} finally {
			em.close();
		}
	}

	public void add(Object entity) throws Exception {
		EntityManager em = PersistenceHelper.createEntityManager();
		em.getTransaction().begin();
		try {
			em.persist(entity);
			em.getTransaction().commit();
			queryList.add(entity);
			fireTableRowsInserted(queryList.size() - 1, queryList.size() - 1);
			setCurrentRow(queryList.size() - 1);
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	public void remove(Object entity) throws Exception {
		EntityManager em = PersistenceHelper.createEntityManager();
		em.getTransaction().begin();
		try {
			Object newEntity = em.merge(entity);
			em.remove(newEntity);
			em.getTransaction().commit();
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		queryList.remove(entity);
		fireTableRowsDeleted(currentRow, currentRow);
		if (currentRow == 0 && queryList.size() > 0)
			setCurrentRow(0);
		else if (currentRow > 0 && queryList.size() > currentRow)
			setCurrentRow(currentRow);
		else if (currentRow > 0 && queryList.size() == currentRow)
			setCurrentRow(currentRow - 1);
		else if (queryList.size() == 0)
			setCurrentRow(-1);
	}

	public void update(Object entity) throws Exception {
		EntityManager em = PersistenceHelper.createEntityManager();
		em.getTransaction().begin();
		Object newEntity;
		try {
			newEntity = em.merge(entity);
			em.getTransaction().commit();
			queryList.set(currentRow, newEntity);
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			throw e;
		} finally {
			em.close();
		}
		fireTableRowsUpdated(currentRow, currentRow);
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (row < 0 || col < 0)
			return null;
		Object retVal = null;
		ColumnAttribute colAttr = null;
		JoinColumnAttribute jcAttr = null;
		Method method = null;
		try {
			colAttr = EntityHelper.getColumnForPosition(entityBean, col);
			jcAttr = EntityHelper.getJoinByColumnPosition(entityBean, col);
			String attrName = StringUtil.capitalize(colAttr.getName());
			Class<?> entityClazz = Class.forName(entityBean.getEntityClass()
					.getName());
			String methodGet = "get" + attrName;
			if (jcAttr != null) {
				Method m = entityClazz.getMethod("get"
						+ jcAttr.getName().substring(0,1).toUpperCase() + jcAttr.getName().substring(1));
				Object lookupObject = m.invoke(entityClazz.cast(queryList
						.get(row)));
				method = jcAttr.getLookupClass().getMethod(methodGet);
				retVal = method.invoke(lookupObject);
			} else {
				method = entityClazz.cast(queryList.get(row)).getClass()
						.getMethod(methodGet);
				retVal = method.invoke(entityClazz.cast(queryList.get(row)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return ConverterUtil.convertBack(retVal, colAttr);
		// return ConverterUtil.convertForViewing(retVal, colAttr);
		return method.getReturnType().cast(retVal);
	}

	@Override
	public int getColumnCount() {
		//System.out.println("GET COLUMN COUNT IZ MODELA VRACA: " + EntityHelper.getColumnAttributesColumnCount(entityBean));
		return EntityHelper.getAllAttributesColumnCount(entityBean);
	}

	public EntityBean getEntityBean() {
		return entityBean;
	}

	public void setEntityBean(EntityBean entity) {
		this.entityBean = entity;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		try {
			ColumnAttribute ca = EntityHelper.getColumnForPosition(entityBean,
					col);
			return ca.getEditableInTable();
		} catch (EntityAttributeNotFoundException e) {
			return false;
		}
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		try {
			ColumnAttribute colAttr = null;
			colAttr = EntityHelper.getColumnForPosition(entityBean, col);

			Object obj = entityBean.getEntityClass().cast(queryList.get(row));
			String setterName = "set"
					+ StringUtil.capitalize(colAttr.getFieldName());
			String dataType = colAttr.getDataType();
			if ((dataType == null || dataType.equals(""))
					&& colAttr.getEnumeration() != null) {
				dataType = "java.lang.Integer";
			}
			Class<?> parameter = Class.forName(dataType);
			Method method = entityBean.getEntityClass().getMethod(setterName,
					parameter);
			Object convertedValue = ConverterUtil.convert(value.toString(),
					colAttr);
			method.invoke(entityBean.getEntityClass().cast(obj), convertedValue);
			fireTableCellUpdated(row, col);
			if (!colAttr.getDerived())
				update(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Class<?> getColumnClass(int column) {
		return (getValueAt(0, column).getClass());
	}
	
	public Object getObject(int row) {
		return queryList.get(row);
	}

}
