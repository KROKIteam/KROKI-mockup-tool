package com.panelcomposer.listeners;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;

import javax.swing.JComponent;

import util.EntityHelper;
import util.ExpressionHelper;
import util.resolvers.ComponentResolver;

import com.eteks.parser.CalculatorParser;
import com.panelcomposer.converters.ConverterUtil;
import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;

public class ComponentFocusAdapter extends FocusAdapter{

	protected EntityBean bean;
	protected ColumnAttribute column;
	protected SPanel panel;
	
	public ComponentFocusAdapter(EntityBean bean, ColumnAttribute column, SPanel panel) {
		this.bean = bean;
		this.column = column;
		this.panel = panel;
	}
	
	@Override
	public void focusLost(FocusEvent fe) {
		try {
			//int selectedRow = panel.getTable().getSelectedRow();
			String expression = ExpressionHelper.turnIntoExpression(bean, column, panel, false);
			System.out.println("ComponentFocusAdapter.focusLost() expression: " +expression);
			CalculatorParser calcParser = new CalculatorParser();
			String retVal = new BigDecimal(
					calcParser.computeExpression(expression)).toString();
			int colind = EntityHelper.getIndexOf(bean, column);
			Object objVal = ConverterUtil.convert(retVal, column);
			//panel.getTable().getTableModel().setValueAt(objVal, selectedRow, colind);
			JComponent component = panel.getInputPanel().getPanelComponents().get(colind);
			ComponentResolver.setValue(component, objVal, column);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
