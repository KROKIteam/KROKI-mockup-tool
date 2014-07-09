package util;

import javax.swing.JComponent;

import util.resolvers.ComponentResolver;

import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.model.attribute.AbsAttribute;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.attribute.JoinColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;

public class ExpressionHelper {

	public static String operands = "[-*/()%+]+";
	public static String separator = "[.]";

	public static String turnIntoExpression(EntityBean bean,
			ColumnAttribute column, SPanel panel, boolean thruTable) {

		System.out.println("column formula: " + column.getFormula());
		String expression = column.getFormula();

		String[] values = column.getFormula().split(operands);
		JoinColumnAttribute jca = null;
		ColumnAttribute ca = null;
		int index = 0;
		try {
			for (int i = 0; i < values.length; i++) {
				if (values[i].indexOf(".") != -1) {
					String[] subvalues = values[i].split(separator);
					String s1 = subvalues[0].trim();
					String s2 = subvalues[1].trim();
					AbsAttribute attr = EntityHelper.getAttribute(bean, s1);
					if (attr != null && attr instanceof JoinColumnAttribute) {
						jca = (JoinColumnAttribute) attr;
						ca = EntityHelper.getColumnInJoinByName(bean, jca, s2);
						index = EntityHelper.getIndexOfForJoin(bean, ca, jca);
					}
				} else {
					ca = (ColumnAttribute) EntityHelper.getAttribute(bean,
							values[i].trim());
					index = EntityHelper.getIndexOf(bean, ca);
				}
				Object value = null;
				if(thruTable) {
					int selectedRow = panel.getTable().getSelectedRow();
					value = panel.getTable().getTableModel().getValueAt(selectedRow, index);
				} else {
	
					System.out.println("index: " + index);
					JComponent comp = panel.getInputPanel().getPanelComponents().get(index);
					value = ComponentResolver.getDataFromComponent(bean, ca, panel, comp);
					System.out.println("value: " + value);
				}
				
				String val = "";
				if (value != null) {
					val = value.toString();
				}
				expression = expression.replace(values[i].trim(), val);
			}
		} catch (Exception e) {
			return null;
		}
		return expression;
	}
}
