package com.panelcomposer.aspects;

import javax.swing.JButton;

import util.EntityHelper;

import com.panelcomposer.elements.spanel.InputPanel;
import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.enumerations.OpenedAs;
import com.panelcomposer.listeners.ComponentFocusAdapter;
import com.panelcomposer.model.attribute.AbsAttribute;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.attribute.JoinColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;

public aspect InputPanelAspect {

	public pointcut createButonComp(InputPanel inputPanel) : execution (* InputPanel.createButtonListener(..)) && target(inputPanel);

	after(InputPanel inputPanel) : createButonComp(inputPanel) {
		OpenedAs op = inputPanel.getPanel().getModelPanel().getPanelSettings()
				.getOpenedAs();
		JoinColumnAttribute jca = (JoinColumnAttribute) thisJoinPoint.getArgs()[0];
		if (op == OpenedAs.NEXT) {
			SPanel owner = inputPanel.getPanel().getOwnerPanel();
			
			String lookupName = jca.getLookupClass().getName();
			String ownerName = owner.getModelPanel().getEntityBean()
					.getEntityClass().getName();
			if (ownerName.equals(lookupName)) {
				((JButton) thisJoinPoint.getArgs()[1]).setVisible(false);
			}
		}
		if(jca.getHidden()) {
			((JButton) thisJoinPoint.getArgs()[1]).setVisible(false);
		}
	}

	public pointcut initMethod(InputPanel inputPanel) : execution (* InputPanel.init(..)) && target(inputPanel);

	/***
	 * For marking columns that are not supposed to change if panel is opened as Next  
	 * @param inputPanel
	 */
	before(InputPanel inputPanel) : initMethod(inputPanel) {
		JoinColumnAttribute jca = null;
		OpenedAs oa = inputPanel.getPanel().getModelPanel().getPanelSettings()
				.getOpenedAs();
		SPanel owner = inputPanel.getPanel().getOwnerPanel();
		try {
			if (oa == OpenedAs.NEXT && owner != null) {
				Class<?> ownerBeanClass = owner.getModelPanel().getEntityBean()
						.getEntityClass();
				EntityBean bean = inputPanel.getPanel().getModelPanel()
						.getEntityBean();
				jca = EntityHelper.getJoinByLookup(
						bean, ownerBeanClass);
				for (ColumnAttribute ca : jca.getColumns()) {
					ca.setDisabled(true);
					int column = EntityHelper.getIndexOfForJoin(bean,
							ca, jca);
					int rowSelected = owner.getTable().getSelectedRow();
					Object value = owner.getTable().getTableModel()
							.getValueAt(rowSelected, column);
					ca.setDefaultValue(value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void InputPanel.setDerivedFormulas() {
		ColumnAttribute colAttr = null;
		String operands = "[-/()%+*]+";
		String regex = "[.]";
		EntityBean bean = getPanel().getModelPanel().getEntityBean();
		for (int j = 0; j < bean.getAttributes().size(); j++) {
			if (bean.getAttributes().get(j) instanceof ColumnAttribute) {
				colAttr = (ColumnAttribute) bean.getAttributes().get(j);
				if (colAttr.getDerived()) {
					String[] values = colAttr.getFormula().split(operands);
					System.out.println(colAttr.getFormula());
					JoinColumnAttribute jca = null;
					ColumnAttribute ca = null;
					int index = 0;
					try {
						for (int i = 0; i < values.length; i++) {
							System.out.println(i + ") " + values[i]);
							if (values[i].indexOf(".") != -1) {
								String[] subvalues = values[i].split(regex);
								String s1 = subvalues[0].trim();
								String s2 = subvalues[1].trim();
								AbsAttribute attr = EntityHelper.getAttribute(
										bean, s1);
								if (attr != null
										&& attr instanceof JoinColumnAttribute) {
									jca = (JoinColumnAttribute) attr;
									ca = EntityHelper.getColumnInJoinByName(
											bean, jca, s2);
									index = EntityHelper.getIndexOfForJoin(
											bean, ca, jca);
								}
							} else {
								ca = (ColumnAttribute) EntityHelper
										.getAttribute(bean, values[i].trim());
								index = EntityHelper.getIndexOf(bean, ca);
							}
							getPanelComponents().get(index).addFocusListener(
									new ComponentFocusAdapter(
											bean, colAttr,getPanel()));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

}
