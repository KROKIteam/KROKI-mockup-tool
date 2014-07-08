package graphedit.gui.table;

import graphedit.app.MainFrame;
import graphedit.model.components.Attribute;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Method;
import graphedit.model.components.Parameter;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

public class PropertiesButtonMouseListener implements MouseListener {

	private JTable table;

	public PropertiesButtonMouseListener(JTable table) {
		this.table = table;
	}

	@SuppressWarnings({ "unchecked"})
	private void forwardEventToButton(MouseEvent e) {
		TableColumnModel columnModel = table.getColumnModel();
		int column = columnModel.getColumnIndexAtX(e.getX());
		int row = e.getY() / table.getRowHeight();
		Object value;
//		JButton button;
//		MouseEvent buttonEvent;

		if (row >= table.getRowCount() || row < 0
				|| column >= table.getColumnCount() || column < 0)
			return;

		value = table.getValueAt(row, column);

		if (!(value instanceof List)) {
			return;
		}
/*
		button = (JButton) value;
		buttonEvent = (MouseEvent) SwingUtilities.convertMouseEvent(table, e, button);
		button.dispatchEvent(buttonEvent);
		// This is necessary so that when a button is pressed and released
		// it gets rendered properly. Otherwise, the button may still appear
		// pressed down when it has been released.
		table.repaint();
*/
		ClassDialog dialog = new ClassDialog();
		GraphElement element = MainFrame.getInstance().getCurrentView().getSelectionModel().getSelectedElements().get(0);
		if (table.getModel() instanceof PropertiesTableModel) {
			String selected = (String)table.getValueAt(row, 0); 
			if (selected.equals("Attributes"))
				dialog.setAttributes(element, (List<Attribute>) value);
			else
				dialog.setMethods(element, (List<Method>) value);
		} else if (table.getModel() instanceof MethodTableModel) {
			Method method = ((MethodTableModel)table.getModel()).getMethod(table.getSelectedRow());
			dialog.setParameters(element, method, (List<Parameter>) value);
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