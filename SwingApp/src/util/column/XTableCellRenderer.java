package util.column;

import java.awt.Component;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.panelcomposer.converters.ConverterUtil;

public class XTableCellRenderer implements TableCellRenderer {

	DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();

	private void configureRenderer(JLabel renderer, Object value) {
		if (value != null) {
			if(value instanceof Date) {
				renderer.setText(ConverterUtil.convertForViewing(value, Date.class));
			} else if(value instanceof Boolean) {
				renderer.setText(ConverterUtil.convertForViewing(value, Boolean.class));
			}
		}
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		tableRenderer = (DefaultTableCellRenderer) tableRenderer
				.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
		configureRenderer(tableRenderer, value);
		return tableRenderer;
	}

}
