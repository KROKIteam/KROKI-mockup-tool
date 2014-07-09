package util.resolvers;

import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.h2.constant.SysProperties;

import util.PersistenceHelper;
import util.StringUtil;

import com.panelcomposer.converters.ConverterUtil;
import com.panelcomposer.core.AppCache;
import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.attribute.JoinColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;

/***
 * Utility class for resolving all the panel's component issues
 * 
 * @author Darko
 * 
 */
public class ComponentResolver {

	/***
	 * Finds respective component type for the value type
	 * 
	 * @param type
	 *            - Value type
	 * @return
	 */
	public static JComponent getComponentForType(String type) {
		String compType = AppCache.getInstance().getComponentType(type);
		try {
			return (JComponent) Class.forName(compType).getConstructor()
					.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/****
	 * Returns component for ColumnAttribute and it's type
	 * 
	 * @param ca
	 * @return
	 */
	public static JComponent getComponent(ColumnAttribute ca) {
		String type = ca.getDataType();
		String compType = AppCache.getInstance().getComponentType(type);
		if (ca.getEnumeration() != null) {
			return new JComboBox(ca.getEnumeration().getLabels().toArray());
		}
		try {
			return (JComponent) Class.forName(compType).getConstructor()
					.newInstance();
		} catch (Exception e) {
			System.out.println("COMPONENT RESOLVER ERROR: " + e.getMessage());
			return null;
		}
	}

	/***
	 * Calculates maximum character count for value of column's attribute length
	 * and scale.
	 * 
	 * @param length
	 * @param scale
	 * @return
	 */
	public static int charactersCount(Integer length, Integer scale) {
		if (length == null)
			return 0;
		int retVal = 0;
		if (scale == null || scale.intValue() <= 0) {
			if (length != null)
				retVal = length.intValue();
		} else if (scale != null && scale.intValue() > 0) {
			retVal += scale + 1;
		}
		return retVal;
	}

	public static Object getDataFromComponent(EntityBean bean,
			ColumnAttribute colAttr, SPanel panel, JComponent component) {
		Object retVal = null;
		try {
			if (component instanceof JTextField) {
				String s = ((JTextField) component).getText();
				if (s != null && !s.equals("")) {
					System.out
							.println("ComponentResolver.getDataFromComponent() s: "
									+ s);
					retVal = ConverterUtil.convert(s, colAttr);
				}
				System.out.println("retVal = " + retVal);
			}
			/*
			 * } else if (component instanceof JComboBox) { if (value instanceof
			 * Integer) { ((JComboBox) component).setSelectedIndex((Integer)
			 * value); } } else if (component instanceof JCheckBox) {
			 * ((JCheckBox) component).setSelected((Boolean)
			 * ConverterUtil.convert(value.toString(), column)); }
			 */

		} catch (Exception e) {
			return null;
		}
		return retVal;
	}

	public static Object getDataFromComponents(EntityBean entity,
			List<JComponent> components, Object retVal) {
		JComponent comp = null;
		try {
			if(retVal == null) {
				retVal = entity.getEntityClass().getConstructor().newInstance();
			}
			int counter = 0;
			ColumnAttribute colAttr = null;
			JoinColumnAttribute jcAttr = null;
			for (int i = 0; i < entity.getAttributes().size(); i++) {
				colAttr = null;
				jcAttr = null;
				if (entity.getAttributes().get(i) instanceof ColumnAttribute) {
					colAttr = (ColumnAttribute) entity.getAttributes().get(i);
					comp = components.get(counter);
					counter++;
				} else if (entity.getAttributes().get(i) instanceof JoinColumnAttribute) {
					jcAttr = (JoinColumnAttribute) entity.getAttributes()
							.get(i);
					for (int j = 0; j < jcAttr.getColumns().size(); j++) {
						if (jcAttr.getColumns().get(j).getKey().booleanValue() == true) {
							colAttr = jcAttr.getColumns().get(j);
							comp = components.get(counter);
							counter++;
						} else {
							counter++;
							continue;
						}
					}
				}
				if (jcAttr != null) {
					invokeMethod(comp, jcAttr, colAttr, retVal, entity);
				} else {
					String setterName = "set"
							+ StringUtil.capitalize(colAttr.getName());
					String dataType = colAttr.getDataType().split(":")[0];

					
					System.out.println("METODA: " + setterName);
					System.out.println("DATA TYPE: " + dataType);
					
					Class<?> parameter = Class.forName(dataType);
					Method method = entity.getEntityClass().getMethod(
							setterName, parameter);
					invokeMethod(method, retVal, parameter, null, comp, colAttr);
				}
			}
		} catch (Exception e) {
			/* System.err.println(e.getMessage()); */
			e.printStackTrace();
		}
		return retVal;
	}

	/***
	 * Invoke method with parameters
	 * 
	 * @param method
	 *            Method
	 * @param value
	 *            Value of object
	 * @param parameter
	 * @param argumentType
	 * @param component
	 * @throws Exception
	 */
	public static void invokeMethod(Method method, Object value,
			Class<?> parameter, Class<?> argumentType, JComponent component,
			ColumnAttribute column) throws Exception {
		Object argument = null;
		if (component instanceof JTextField) {
			String s = ((JTextField) component).getText();
			System.out.println("CONVERT: " + s);
			argument = ConverterUtil.convert(s, column);
		} else if (component instanceof JComboBox) {
			JComboBox cb = (JComboBox)component;
			String val = cb.getSelectedItem().toString();
			argument = parameter.getConstructor(String.class).newInstance(val);
			System.out.println("ARGUMENT: " + val + " za " + argument.getClass().getCanonicalName());
		} else if (component instanceof JCheckBox) {
			argument = parameter.getConstructor(boolean.class).newInstance(
					((JCheckBox) component).isSelected());
		} else if (component instanceof JTextArea) {
			String s = ((JTextArea)component).getText();
			argument = ConverterUtil.convert(s, column);
		}
		method.invoke(value, argument);
	}

	public static void invokeMethod(JComponent comp,
			JoinColumnAttribute jcAttr, ColumnAttribute colAttr, Object retVal,
			EntityBean entity) {
		String value = ((JTextField) comp).getText().trim();
		Query q;
		EntityManager em = PersistenceHelper.createEntityManager();
		Object obj = null;
		try {
			String sql = "SELECT x FROM " + jcAttr.getLookupClass().getName()
					+ " x WHERE x." + colAttr.getFieldName() + " = '" + value
					+ "'";
			System.out.println("SQL: : : " + sql);
			q = em.createQuery(sql);
			obj = jcAttr.getLookupClass().cast(q.getSingleResult());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			em.close();
		}
		if (obj != null) {
			try {
				String mName = "set"
						+ StringUtil.capitalize(jcAttr.getFieldName());
				System.out.println("METODA: " + mName);
				Method m = entity.getEntityClass().getMethod(mName,
						jcAttr.getLookupClass());
				m.invoke(entity.getEntityClass().cast(retVal), obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets value on the component.
	 * 
	 * @param component
	 *            Component
	 * @param value
	 *            Value
	 * @param column
	 *            Column attribute
	 * @return
	 */
	public static JComponent setValue(JComponent component, Object value,
			ColumnAttribute column) {
		if (component instanceof JTextField) {
			((JTextField) component).setText(ConverterUtil.convertForViewing(
					value, column));
		} else if (component instanceof JComboBox) {
			if (value instanceof Integer) {
				((JComboBox) component).setSelectedIndex((Integer) value);
			}
		} else if (component instanceof JCheckBox) {
			((JCheckBox) component).setSelected((Boolean) ConverterUtil
					.convert(value.toString(), column));
		}
		return component;
	}

	public static void fillPanel(EntityBean ejb, SPanel panel, int rowIndex) {
		int counter = 0;
		JComponent comp = null;
		ColumnAttribute column = null;
		Object value = null;
		for (int i = 0; i < ejb.getAttributes().size(); i++) {
			if (ejb.getAttributes().get(i) instanceof JoinColumnAttribute) {
				JoinColumnAttribute jca = (JoinColumnAttribute) ejb
						.getAttributes().get(i);
				for (int j = 0; j < jca.getColumns().size(); j++) {
					column = (ColumnAttribute) jca.getColumns().get(j);
					comp = panel.getInputPanel().getPanelComponents()
							.get(counter);
					value = panel.getTable().getTableModel()
							.getValueAt(rowIndex, counter);
					ComponentResolver.setValue(comp, value, column);
					counter++;
				}
			} else if (ejb.getAttributes().get(i) instanceof ColumnAttribute) {
				column = (ColumnAttribute) ejb.getAttributes().get(i);
				comp = panel.getInputPanel().getPanelComponents().get(counter);
				value = panel.getTable().getTableModel()
						.getValueAt(rowIndex, counter);
				ComponentResolver.setValue(comp, value, column);
				counter++;
			}
		}
	}

}
