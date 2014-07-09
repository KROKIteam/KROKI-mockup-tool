package graphedit.gui.table;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.model.enums.ClassStereotypeUI;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

@SuppressWarnings("serial")
public class PropertiesTable extends JTable{

	private boolean elementSelected = false;
	private DefaultCellEditor cbEditor, cbCheckEditor; 



	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PropertiesTable(AbstractTableModel propertiesTableModel) {
		super(propertiesTableModel);
		JComboBox cbTypes = new JComboBox(ClassStereotypeUI.values());
		JComboBox<String> cbCheck = new JComboBox<String>(new String[] {"true","false"});
		
		cbEditor =  new DefaultCellEditor(cbTypes);
		cbCheckEditor = new DefaultCellEditor(cbCheck);
	}

	public TableCellEditor getCellEditor(int row, int column){
		if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE || 
				MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE_MIXED ){
			System.out.println(elementSelected);
			if (elementSelected && row==5 && column==1){
				System.out.println(cbEditor);
				return cbEditor;
			}
			else if (((row == 6 && column == 1) || (row == 7 && column == 1))){
				return cbCheckEditor;
			}
			else
				return super.getCellEditor(row, column);
		}
		return super.getCellEditor(row, column);
	}

	public boolean isElementSelected() {
		return elementSelected;
	}

	public void setElementSelected(boolean elementSelected) {
		this.elementSelected = elementSelected;
	}


}
