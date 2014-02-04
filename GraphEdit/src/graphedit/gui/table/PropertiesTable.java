package graphedit.gui.table;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.model.components.ClassStereotypeUI;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

@SuppressWarnings("serial")
public class PropertiesTable extends JTable{

	private boolean elementSelected = false;
	private DefaultCellEditor cbEditor; 


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PropertiesTable(AbstractTableModel propertiesTableModel) {
		super(propertiesTableModel);
		JComboBox cbTypes = new JComboBox(ClassStereotypeUI.values());
		cbEditor =  new DefaultCellEditor(cbTypes);
	}

	public TableCellEditor getCellEditor(int row, int column){
		if (elementSelected && row==5 && column==1 && MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE)
			return cbEditor;
		return super.getCellEditor(row, column);
	}

	public boolean isElementSelected() {
		return elementSelected;
	}

	public void setElementSelected(boolean elementSelected) {
		this.elementSelected = elementSelected;
	}


}
