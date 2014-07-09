package com.panelcomposer.elements;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import util.EntityHelper;
import util.column.XTableCellRenderer;
import util.column.XTableColumnModel;
import util.resolvers.ComponentResolver;
import util.staticnames.Messages;
import util.staticnames.Settings;

import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.exceptions.EntityAttributeNotFoundException;
import com.panelcomposer.listeners.TableCellEditorListener;
import com.panelcomposer.listeners.TableKeyAdapter;
import com.panelcomposer.listeners.TableMouseAdapter;
import com.panelcomposer.model.attribute.AbsAttribute;
import com.panelcomposer.model.attribute.ColumnAttribute;
import com.panelcomposer.model.attribute.JoinColumnAttribute;
import com.panelcomposer.model.ejb.EntityBean;

@SuppressWarnings("serial")
public class STable extends JTable {

	/***
	 * Implementation of AbstractTableModel
	 */
	protected STModel tableModel;
	/***
	 * Scroll pane
	 */
	protected JScrollPane scrollPane = new JScrollPane();
	/***
	 * Standard panel
	 */
	protected SPanel panel;

	protected TableCellRenderer renderer = new XTableCellRenderer();//new DefaultTableCellRenderer();

	public STable(STModel tableModel, SPanel panel) {
		super(tableModel, new XTableColumnModel());
		createDefaultColumnsFromModel();
		this.tableModel = tableModel;
		this.panel = panel;
		init();
	}

	/***
	 * Initializes table
	 */
	protected void init() {
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setRowSelectionAllowed(true);
		setSelectionMode(0);
		setColumnSelectionAllowed(false);
		addMouseListener(new TableMouseAdapter(this));
		addKeyListener(new TableKeyAdapter(this));

		setIntercellSpacing(new java.awt.Dimension(3, 3));
		setRowHeight(getRowHeight() + 3);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(true);
		scrollPane.getViewport().setView(this);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		List<AbsAttribute> attributes = tableModel.entityBean.getAttributes();
		ColumnAttribute colAttr = null;
		int counter = 0; 

		for (int i = 0; i < attributes.size(); i++) {
			if (attributes.get(i) instanceof ColumnAttribute) {
				colAttr = (ColumnAttribute) attributes.get(i);
				costumizeColumn(colAttr, counter);
				counter++;
			} else if (attributes.get(i) instanceof JoinColumnAttribute) {
				JoinColumnAttribute jcAttr = (JoinColumnAttribute) attributes.get(i);
				for (int j = 0; j < jcAttr.getColumns().size(); j++) {
					colAttr = jcAttr.getColumns().get(j);
					costumizeColumn(colAttr, counter);
					counter++;
				}
			} 
			packColumns(); 
		}
		setPreferredScrollableViewportSize(new Dimension((int) getPreferredSize().getWidth(), 800));
	}

	/***
	 * Sets up TableColumn properties for ColumnAttribute
	 * @param colAttr Column attribute that represents one attribute in entity
	 * @param number Number of column
	 * @return
	 */
	private int costumizeColumn(ColumnAttribute colAttr, int number) {
		TableColumn tc = ((XTableColumnModel) getColumnModel()).getColumn(number);
		if(colAttr.getHidden()) {
			tc.setMinWidth(0);
			tc.setMaxWidth(0);
			tc.setWidth(0);
			return 0;
		}
		tc.setHeaderValue(colAttr.getLabel());
		Integer length = colAttr.getLength();
		Integer scale = colAttr.getScale();
		int charSize = ComponentResolver.charactersCount(length, scale);
		charSize = (charSize > Settings.MAX_CHAR_SIZE) ? Settings.MAX_CHAR_SIZE
				: charSize;
		tc.setPreferredWidth(charSize);
		tc.setCellRenderer(renderer);
		if (charSize <= 0)
			tc.setPreferredWidth(10);
		if (colAttr.getDerived()) {
			makeFormulaForTableCellEdit(colAttr);
		}
		tc.setResizable(true);
		return tc.getPreferredWidth();
	}

	private void makeFormulaForTableCellEdit(ColumnAttribute colAttr) {
		EntityBean bean = getPanel().getModelPanel().getEntityBean();
		String operands = "[-*/()%+]+";
		String[] values = colAttr.getFormula().split(operands);
		JoinColumnAttribute jca = null;
		ColumnAttribute ca = null;
		int index = 0;
		try {
			for (int i = 0; i < values.length; i++) {
				String regex = "[.]";
				if (values[i].indexOf(".") != -1) {
					String[] subvalues = values[i].split(regex);
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
				TableColumn tableColumn = ((XTableColumnModel) getColumnModel())
						.getColumn(index);
				tableColumn.setCellEditor(new DefaultCellEditor(
						new JTextField()));
				tableColumn.getCellEditor().addCellEditorListener(
						new TableCellEditorListener(bean, colAttr, this));
			}
		} catch (EntityAttributeNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void packColumns() {
		for (int c = 0; c < getColumnCount(); c++) {
			packColumn(c, 10);
		}
	}

	/***
	 * Optimized sizing of columns in the table based on the label length or
	 * maximum data length for the column.
	 * @param index Index of table column.
	 * @param margin Margin size.
	 * @return
	 */
	public int packColumn(int index, int margin) {
		DefaultTableColumnModel colModel = (DefaultTableColumnModel) getColumnModel();
		TableColumn col = colModel.getColumn(index);
		int width = 0;
		// Get width of column header
		TableCellRenderer renderer = col.getHeaderRenderer();
		if (renderer == null) {
			renderer = getTableHeader().getDefaultRenderer();
		}
		Component comp = renderer.getTableCellRendererComponent(this,
				col.getHeaderValue(), false, false, 0, 0);
		width = comp.getPreferredSize().width;
		width = Math.max(width, col.getPreferredWidth());
		// Add margin
		width += 2 * margin;
		// Set the width
		col.setPreferredWidth(width);
		return width;
	}

	/***
	 * Table navigation - select the first row
	 */
	public void goToFirst() {
		tableModel.setCurrentRow(0);
		setRowSelectionInterval(0, 0);
	}

	/***
	 * Table navigation - select the last row
	 */
	public void goToLast() {
		int last = tableModel.getRowCount() - 1;
		tableModel.setCurrentRow(last);
		setRowSelectionInterval(last, last);
	}

	/***
	 * Table navigation - select previous row
	 */
	public void goToPrev() {
		int pos = tableModel.getCurrentRow();
		if (pos > 0) {
			tableModel.setCurrentRow(--pos);
			setRowSelectionInterval(pos, pos);
		}
	}

	/***
	 * Table navigation - select next row
	 */
	public void goToNext() {
		int pos = tableModel.getCurrentRow();
		if (pos < tableModel.getRowCount() - 1) {
			tableModel.setCurrentRow(++pos);
			setRowSelectionInterval(pos, pos);
		}
	}

	/***
	 * Method handling deletion of selected row.
	 */
	public void handleDelete() {
		int rowIndex = getSelectedRow();
		if (rowIndex >= 0) {
			int answer = JOptionPane.showConfirmDialog(this,
					Messages.DELETE_QUESTION, Messages.DELETE_TITLE,
					JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				try {
					Class<?> clazz = getTableModel().getEntityBean()
							.getEntityClass();
					getTableModel().remove(
							clazz.cast(getTableModel().get(rowIndex)));
					rowIndex = getTableModel().getCurrentRow();
					if (rowIndex != -1)
						setRowSelectionInterval(rowIndex, rowIndex);
					panel.refreshInputPanel();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		}
	}

	public STModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(STModel tableModel) {
		this.tableModel = tableModel;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public SPanel getPanel() {
		return panel;
	}

	public void setPanel(SPanel panel) {
		this.panel = panel;
	}

}
