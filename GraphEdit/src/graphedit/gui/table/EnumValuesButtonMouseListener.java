package graphedit.gui.table;

import graphedit.model.components.Attribute;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

public class EnumValuesButtonMouseListener implements MouseListener {

	private JTable table;

	public EnumValuesButtonMouseListener(JTable table) {
		this.table = table;
	}

	private void forwardEventToButton(MouseEvent e) {
		TableColumnModel columnModel = table.getColumnModel();
		int column = columnModel.getColumnIndexAtX(e.getX());
		int row = e.getY() / table.getRowHeight();
		Object value;

		if (row >= table.getRowCount() || row < 0
				|| column >= table.getColumnCount() || column < 0)
			return;

		Attribute att = (Attribute) table.getValueAt(row, -1);
		if (!att.getType().equals("ComboBox"))
			return;
		
		value = table.getValueAt(row, column);

		if (!(value instanceof List)) {
			return;
		}
		ClassDialog dialog = new ClassDialog();
		
		if (table.getModel() instanceof AttributeTableModel) {
			Attribute attribute = (Attribute) ((AttributeTableModel)table.getModel()).getValueAt(table.getSelectedRow(), -1);
			dialog.setEnumValues(attribute);
		}
		dialog.setVisible(true);
	}

	public void mouseClicked(MouseEvent e) {
		forwardEventToButton(e);
	}

	public void mouseEntered(MouseEvent e) {
		//forwardEventToButton(e);
	}

	public void mouseExited(MouseEvent e) {
		//forwardEventToButton(e);
	}

	public void mousePressed(MouseEvent e) {
		//forwardEventToButton(e);
	}

	public void mouseReleased(MouseEvent e) {
		//forwardEventToButton(e);
	}
}