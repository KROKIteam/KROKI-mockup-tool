package kroki.app.gui.settings;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import kroki.profil.property.VisibleProperty;

/**
 * Simple GUI component that contains editable list which enables combo box values management in KROKI Mockup tool
 * @author Milorad Filipovic
 */
public class ComboBoxValuesPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	protected final JTable valuesTable;
	protected final JScrollPane scrollPane;
	protected VisibleProperty visibleProperty;
	protected VisiblePropertySettings settingsPanel;
	
	public ComboBoxValuesPanel(VisiblePropertySettings settingsPanel, VisibleProperty visibleProperty) {
		setSize(200, 300);
		
		this.settingsPanel = settingsPanel;
		
		//Initialize table and table model
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("Values");
		
		valuesTable = new JTable(tableModel);
		valuesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		valuesTable.setTableHeader(null);
		
		setVisibleProperty(visibleProperty);
		//Default row which is always displayed at the bottom of the table
		//Click on this row adds new row to the table
		String[] row = {"..."};
		tableModel.addRow(row);
		initTableListeners();
		
		scrollPane = new JScrollPane(valuesTable);
		valuesTable.setFillsViewportHeight(true);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
	private void initTableListeners() {
		//Click listener
		valuesTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					//Click on last row adds new row
					if(row == target.getRowCount()-1) {
						String[] newRow = {"value" + ( row+1)};
						DefaultTableModel model = (DefaultTableModel) target.getModel();
						model.insertRow(row, newRow);
						setVisiblePropertyEnum();
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		//DELETE button removes selected row
		valuesTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
		valuesTable.getActionMap().put("delete", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTable target = (JTable)e.getSource();
				int row = target.getSelectedRow();
				if(row != target.getRowCount()-1) {
					DefaultTableModel model = (DefaultTableModel) target.getModel();
					model.removeRow(row);
				}
				setVisiblePropertyEnum();
			}
		});
		
		valuesTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				setVisiblePropertyEnum();
			}
			
		});
		
		valuesTable.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					setVisiblePropertyEnum();
				} catch (Exception e2) {
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
	}
	
	public String getValuesAsString() {
		String vals = "";
		int rows = valuesTable.getRowCount();
		if(rows > 1) {
			for(int i=0; i<rows-1; i++) {
				vals += valuesTable.getValueAt(i, 0).toString() + ";";
			}
		}
		return vals;
	}
	
	public String[] getValuesAsArray() {
		int rows = valuesTable.getRowCount()-1;
		String[] vals = new String[rows];
		if(rows > 0) {
			for(int i=0; i<rows; i++) {
				vals[i] =  valuesTable.getValueAt(i, 0).toString();
			}
		}
		return vals;
	}
	
	public void resetTable() {
		DefaultTableModel model = (DefaultTableModel)valuesTable.getModel();
		model.getDataVector().removeAllElements();
		String[] row = {"..."};
		model.addRow(row);
	}
	
	public void setValues() {
		if(visibleProperty.getEnumeration() != null) {
			String[] newValues = visibleProperty.getEnumeration().split(";");
			DefaultTableModel model = (DefaultTableModel)valuesTable.getModel();
			model.getDataVector().removeAllElements();
			
			//Add all rows from array
			for(int i=0; i<newValues.length; i++) {
				String[] newRow = {newValues[i]};
				model.addRow(newRow);
			}
			String[] row = {"..."};
			model.addRow(row);
		}else {
			resetTable();
		}
	}

	public void setVisiblePropertyEnum() {
		try {
			String enumeration = getValuesAsString();
			if(!enumeration.equals("")) {
				System.out.println("SETTING ENUMERATION: " + enumeration);
				visibleProperty.setEnumeration(enumeration);
				settingsPanel.updatePreformed();
			}
		} catch (Exception e) {
		}
	}
	
	public VisibleProperty getVisibleProperty() {
		return visibleProperty;
	}

	public void setVisibleProperty(VisibleProperty visibleProperty) {
		this.visibleProperty = visibleProperty;
		try {
			setValues();
		} catch (Exception e) {
		}
	}
}
