package graphedit.gui.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class PropertiesTableRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	private JPanel container = new JPanel(new BorderLayout());
	private JButton button = new JButton("...");
	private JLabel label = new JLabel("Expand");

	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value,
			boolean isSelected, boolean hasFocus, final int row, final int column) {

		if (value instanceof List) {
			button.setPreferredSize(new Dimension(20, 15));
			container.setBackground(Color.WHITE);
			container.add(label, BorderLayout.WEST);
			container.add(button, BorderLayout.EAST);
			return container;
		}
	
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

}
