package com.panelcomposer.listeners;

import java.math.BigDecimal;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import util.EntityHelper;
import util.ExpressionHelper;

import com.eteks.parser.CalculatorParser;
import com.panelcomposer.converters.ConverterUtil;
import com.panelcomposer.elements.STable;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;

public class TableCellEditorListener implements CellEditorListener {

	protected EntityBean bean;
	protected ColumnAttribute column;
	protected STable table;

	public TableCellEditorListener(EntityBean bean, ColumnAttribute column,
			STable table) {
		this.bean = bean;
		this.column = column;
		this.table = table;
	}

	@Override
	public void editingCanceled(ChangeEvent ce) {
	}

	@Override
	public void editingStopped(ChangeEvent ce) {
		try {
			int selectedRow = table.getSelectedRow();
			int colind = EntityHelper.getIndexOf(bean, column);
			
			String expression = ExpressionHelper.turnIntoExpression(bean, column, table.getPanel(), true);
			System.out.println(expression);
			CalculatorParser calcParser = new CalculatorParser();
			String retVal = new BigDecimal(
					calcParser.computeExpression(expression)).toString();
			
			Object objVal = ConverterUtil.convert(retVal, column);
			table.getTableModel().setValueAt(objVal, selectedRow, colind);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
