package adapt.util.ejb;

import java.lang.reflect.Field;
import java.util.Iterator;

import adapt.exceptions.EntityAttributeNotFoundException;
import adapt.model.ejb.AbstractAttribute;
import adapt.model.ejb.ColumnAttribute;
import adapt.model.ejb.EntityBean;
import adapt.model.ejb.JoinColumnAttribute;

public class EntityHelper {
	/***
	 * For fetching attribute with given name
	 * 
	 * @param name
	 * @return
	 * @throws EntityAttributeNotFoundException
	 */
	public static AbstractAttribute getAttribute(EntityBean bean, String name)
			throws EntityAttributeNotFoundException {
		Iterator<AbstractAttribute> it = bean.getAttributes().iterator();
		AbstractAttribute attr = null;
		while (it.hasNext()) {
			attr = it.next();
			if (attr.getName().equals(name)) {
				return attr;
			}
		}
		throw new EntityAttributeNotFoundException(
				"Entity attribute not found by the name '" + name + "'");
	}

	public static JoinColumnAttribute getJoinByFieldName(EntityBean bean, String fieldName) throws EntityAttributeNotFoundException {
		Iterator<AbstractAttribute> it = bean.getAttributes().iterator();
		AbstractAttribute attr = null;
		while (it.hasNext()) {
			attr = it.next();
			if (attr instanceof JoinColumnAttribute) {
				System.out.println("ATTR: " + attr.getLabel() + " | " + attr.getName() + " | " + attr.getFieldName() + "[" + fieldName + "]");
				//TODO compare actual field name, not label
				String label = attr.getLabel();
				String labelNoSpace = attr.getLabel().replace(" ", "_");
				if (label.equals(fieldName) || labelNoSpace.equals(fieldName)) {
					return (JoinColumnAttribute) attr;
				}
			}
		}
		throw new EntityAttributeNotFoundException(
				"Entity attribute not found with the field name '"
						+ fieldName + "' in entity class '"
						+ bean.getEntityClass().getName() + "'");
	}
	
	public static JoinColumnAttribute getJoinByLookup(EntityBean bean, Class<?> lookupClass) throws EntityAttributeNotFoundException {
		Iterator<AbstractAttribute> it = bean.getAttributes().iterator();
		AbstractAttribute attr = null;
		while (it.hasNext()) {
			attr = it.next();
			if (attr instanceof JoinColumnAttribute) {
				if (((JoinColumnAttribute) attr).getLookupClass().equals(
						lookupClass)) {
					return (JoinColumnAttribute) attr;
				}
			}
		}
		throw new EntityAttributeNotFoundException(
				"Entity attribute not found with the lookup class name '"
						+ lookupClass.getName() + "' in entity class '"
						+ bean.getEntityClass().getName() + "'");
	}

	public static int getIndexOfJoinByLookup(EntityBean bean,
			Class<?> lookupClass) throws EntityAttributeNotFoundException {
		int counter = 0;
		Iterator<AbstractAttribute> it = bean.getAttributes().iterator();
		AbstractAttribute attr = null;
		while (it.hasNext()) {
			attr = it.next();
			if (attr instanceof JoinColumnAttribute) {
				if (((JoinColumnAttribute) attr).getLookupClass().getName()
						.equals(lookupClass.getName())) {
					return counter;
				} else {
					counter += ((JoinColumnAttribute) attr).getColumns().size();
				}
			} else {
				counter++;
			}
		}
		throw new EntityAttributeNotFoundException(
				"Entity attribute not found with the lookup class name '"
						+ lookupClass.getName() + "'");
	}

	public static ColumnAttribute getColumnForPosition(EntityBean bean,
			int position) throws EntityAttributeNotFoundException {
		int counter = 0;
		for (int i = 0; i < bean.getAttributes().size(); i++) {
			if (bean.getAttributes().get(i) instanceof ColumnAttribute) {
				if (position == counter) {
					return (ColumnAttribute) bean.getAttributes().get(i);
				}
				counter++;
			} else if (bean.getAttributes().get(i) instanceof JoinColumnAttribute) {
				JoinColumnAttribute jca = (JoinColumnAttribute) bean
						.getAttributes().get(i);
				for (int j = 0; j < jca.getColumns().size(); j++) {
					if (position == counter) {
						return jca.getColumns().get(j);
					}
					counter++;
				}
			}
		}
		throw new EntityAttributeNotFoundException(
				"Entity attribute not found for the '" + position + "'");
	}

	public static int getAllAttributesColumnCount(EntityBean bean) {
		int counter = 0;
		for (int i = 0; i < bean.getAttributes().size(); i++) {
			if (bean.getAttributes().get(i) instanceof JoinColumnAttribute) {
				counter += ((JoinColumnAttribute) bean.getAttributes().get(i))
						.getColumns().size();
			} else {
				counter++;
			}
		}
		return counter;
	}

	public static int getColumnAttributesColumnCount(EntityBean bean) {
		int counter = 0;
		for(int i=0; i<bean.getAttributes().size(); i++) {
			if(bean.getAttributes().get(i) instanceof ColumnAttribute) {
				counter++;
			}
		}
		return counter;
	}

	public static JoinColumnAttribute getJoinByColumnPosition(EntityBean bean,
			int position) {
		int counter = 0;
		for (int i = 0; i < bean.getAttributes().size(); i++) {
			if (bean.getAttributes().get(i) instanceof ColumnAttribute) {
				counter++;
			} else if (bean.getAttributes().get(i) instanceof JoinColumnAttribute) {
				JoinColumnAttribute jca = (JoinColumnAttribute) bean
						.getAttributes().get(i);
				for (int j = 0; j < jca.getColumns().size(); j++) {
					if (position == counter) {
						return jca;
					}
					counter++;
				}
			}
		}
		return null;
	}

	public static int getIndexOf(EntityBean bean,
			ColumnAttribute columnAttribute)
					throws EntityAttributeNotFoundException {
		int counter = 0;
		ColumnAttribute colAttr = null;
		JoinColumnAttribute jcAttr = null;
		for (int i = 0; i < bean.getAttributes().size(); i++) {
			if (bean.getAttributes().get(i) instanceof ColumnAttribute) {
				colAttr = (ColumnAttribute) bean.getAttributes().get(i);
				if (colAttr.getName().equals(columnAttribute.getName())) {
					return counter;
				}
				counter++;
			} else if (bean.getAttributes().get(i) instanceof JoinColumnAttribute) {
				jcAttr = (JoinColumnAttribute) bean.getAttributes().get(i);
				counter += jcAttr.getColumns().size();
			}
		}
		return -1;
	}

	public static int getIndexOfForJoin(EntityBean bean,
			ColumnAttribute columnAttribute,
			JoinColumnAttribute joinColumnAttribute)
					throws EntityAttributeNotFoundException {
		int counter = 0;
		ColumnAttribute colAttr = null;
		JoinColumnAttribute jcAttr = null;
		for (int i = 0; i < bean.getAttributes().size(); i++) {
			if (bean.getAttributes().get(i) instanceof JoinColumnAttribute) {
				jcAttr = (JoinColumnAttribute) bean.getAttributes().get(i);
				String jcaName = joinColumnAttribute.getLookupClass().getName();
				if (jcAttr.getLookupClass().getName().equals(jcaName)) {
					for (int j = 0; j < jcAttr.getColumns().size(); j++) {
						colAttr = jcAttr.getColumns().get(j);
						if (colAttr.getName().equals(columnAttribute.getName())) {
							return counter;
						}
						counter++;
					}
				} else {
					counter += jcAttr.getColumns().size();
				}
			} else if (bean.getAttributes().get(i) instanceof ColumnAttribute) {
				counter++;
			}
		}
		throw new EntityAttributeNotFoundException(
				"Entity attribute not found for the '"
						+ columnAttribute.getName() + "'");
	}

	public static ColumnAttribute getKeyAsColumn(EntityBean bean)
			throws EntityAttributeNotFoundException {
		ColumnAttribute ca = null;
		for (int i = 0; i < bean.getAttributes().size(); i++) {
			if (bean.getAttributes().get(i) instanceof ColumnAttribute) {
				if ((ca = (ColumnAttribute) bean.getAttributes().get(i))
						.getKey().booleanValue() == true) {
					return ca;
				}
			}
		}
		throw new EntityAttributeNotFoundException(
				"Entity attribute not found for KEY");
	}

	public static ColumnAttribute getColumnInJoinByName(EntityBean bean,
			JoinColumnAttribute jca, String name)
					throws EntityAttributeNotFoundException {

		ColumnAttribute ca = null;
		for (int i = 0; i < jca.getColumns().size(); i++) {
			ca = jca.getColumns().get(i);
			if(ca.getName().equals(name)) {
				return ca;
			}
		}

		throw new EntityAttributeNotFoundException(
				"Entity attribute not found for KEY");
	}

	public static String getIDValue(Object object) throws ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String id = "-1";
		Class objectClass = object.getClass();
		Field idField = objectClass.getDeclaredField("id");
		idField.setAccessible(true);
		id = idField.get(object).toString();
		return id;
	}
	
}
