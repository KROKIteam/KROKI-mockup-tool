package graphedit.gui.table;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.gui.utils.Dialogs;
import graphedit.model.components.Attribute;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Interface;
import graphedit.model.components.Method;
import graphedit.model.components.MethodStereotypeUI;
import graphedit.model.components.Modifier;
import graphedit.model.components.Parameter;
import graphedit.model.enums.AttributeDataTypeUI;
import graphedit.model.enums.AttributeTypeUI;
import graphedit.properties.ApplicationModeProperties;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ClassDialog extends JDialog {

	private static final long serialVersionUID = 1L;


	private JTable table;
	private JButton remove;
	JButton btnUp;
	JButton btnDown;
	private ClassToolBar toolBar;

	private ApplicationModeProperties properties;


	public ClassDialog() {
		super(MainFrame.getInstance(), true);
		properties = ApplicationModeProperties.getInstance();

		MainFrame mf = MainFrame.getInstance(); 
		setSize(new Dimension(mf.getWidth()/2, mf.getHeight()/2));

		table = new RXTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		remove = new JButton("Remove");
		btnUp = new JButton("\u25B2");
		btnDown = new JButton("\u25BC");
		toolBar = new ClassToolBar(remove, btnUp, btnDown);

		add(toolBar, BorderLayout.SOUTH);
		add(new JScrollPane(table), BorderLayout.CENTER);

		setLocationRelativeTo(mf);


	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setAttributes(final GraphElement element, List<Attribute> attributes) {
		setTitle("Attributes");

		table.setModel(new AttributeTableModel(element, attributes));
		table.setDefaultRenderer(Object.class, new PropertiesTableRenderer());
		table.addMouseListener(new EnumValuesButtonMouseListener(table));

		int columnNum = table.getColumnCount();

		JComboBox cbModifiers = new JComboBox(Modifier.values());
		JComboBox cbElements = new JComboBox(MainFrame.getInstance().getCurrentView().getModel().getDiagramElements().toArray());


		int removedColumns = 0;


		if (!(Boolean) properties.getPropertyValue("attributeClassEditable")){
			table.removeColumn(table.getColumnModel().getColumn(1));
			removedColumns++;
		}
		else
			table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(cbElements));
		if (!(Boolean) properties.getPropertyValue("attributeNameEditable")){
			table.removeColumn(table.getColumnModel().getColumn(2 - removedColumns));
			removedColumns++;
		}

		if (!(Boolean) properties.getPropertyValue("attributeTypeEditable")){
			table.removeColumn(table.getColumnModel().getColumn(3 - removedColumns));
			removedColumns++;
		}
		else if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE
				|| MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE_MIXED){
			JComboBox cbTypes = new JComboBox(AttributeTypeUI.values());
			table.getColumnModel().getColumn(3 - removedColumns).setCellEditor(new DefaultCellEditor(cbTypes));
		}


		if (!(Boolean) properties.getPropertyValue("attributeDataTypeEditable")){
			table.removeColumn(table.getColumnModel().getColumn(4 - removedColumns));
			removedColumns++;
		}
		else if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE_PERSISTENT
				|| MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE_MIXED){
			JComboBox cbTypes = new JComboBox(AttributeDataTypeUI.values());
			table.getColumnModel().getColumn(4 - removedColumns).setCellEditor(new DefaultCellEditor(cbTypes));
		}

		if (!(Boolean) properties.getPropertyValue("attributeModifierEditable")){
			table.removeColumn(table.getColumnModel().getColumn(5 - removedColumns));
			removedColumns++;
		}
		else
			table.getColumnModel().getColumn(4 - removedColumns).setCellEditor(new DefaultCellEditor(cbModifiers));

		if (!(Boolean) properties.getPropertyValue("attributeStaticEditable")){
			table.removeColumn(table.getColumnModel().getColumn(6 - removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("attributeFinalEditable")){
			table.removeColumn(table.getColumnModel().getColumn(7 - removedColumns));
			removedColumns++;
		}

		double percent = (double)(columnNum-removedColumns)/columnNum;
		MainFrame mf = MainFrame.getInstance(); 
		setSize((int) ((mf.getWidth())*0.5 * percent) , mf.getHeight() / 2);
		setLocationRelativeTo(mf);

		table.getColumnModel().getColumn(0).setMaxWidth(20);
		table.getColumnModel().getColumn(0).setMinWidth(20);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = table.getSelectedRow();
				boolean last = table.getSelectedRow() == table.getModel().getRowCount() - 1;
				//add new attribute
				if (last){
					Attribute attribute = new Attribute();
					if (element instanceof Interface) {
						attribute.setFinalAttribute(true);
						attribute.setModifier(Modifier.PUBLIC);
						attribute.setStaticAttribute(true);
					}
					((AttributeTableModel)table.getModel()).addAttribute(attribute);
					table.changeSelection(index, 2, true, true);
					((RXTable) table).setSelectAllForEdit(true);
				}

			}
		});



		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				String name = (String) table.getValueAt(selectedRow, 2);
				int answer = Dialogs.showYesNoDialog("Are you sure you want to delete '" + name + "'?", "Delete");
				if (answer == JOptionPane.YES_OPTION) {
					((AttributeTableModel)table.getModel()).removeAttribute(selectedRow);
					selectPrevious(selectedRow);
				}
			}
		});


		btnUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow <= 0)
					return;
				((AttributeTableModel)table.getModel()).moveAttributeUp(selectedRow);
				table.getSelectionModel().setSelectionInterval(selectedRow - 1, selectedRow - 1);

			}
		});

		btnDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = table.getSelectedRow();
				int rowsNum = table.getRowCount();
				if (selectedRow == -1 || selectedRow == rowsNum - 1)
					return;
				((AttributeTableModel)table.getModel()).moveAttributeDown(selectedRow);
				table.getSelectionModel().setSelectionInterval(selectedRow + 1, selectedRow + 1);

			}
		});

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setMethods(final GraphElement element, List<Method> methods) {
		setTitle("Methods");
		table.setModel(new MethodTableModel(element, methods));
		table.setDefaultRenderer(Object.class, new PropertiesTableRenderer());
		table.addMouseListener(new PropertiesButtonMouseListener(table));
		int columnNum = table.getColumnCount();

		JComboBox cbElements = new JComboBox(MainFrame.getInstance().getCurrentView().getModel().getDiagramElements().toArray());

		int removedColumns = 0;

		if (!(Boolean) properties.getPropertyValue("methodClassEditable")){
			table.removeColumn(table.getColumnModel().getColumn(1));
			removedColumns++;
		}
		else
			table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(cbElements));

		if (!(Boolean) properties.getPropertyValue("methodNameEditable")){
			table.removeColumn(table.getColumnModel().getColumn(2 - removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("methodStereotypeEditable")){
			table.removeColumn(table.getColumnModel().getColumn(3-removedColumns));
			removedColumns++;
		}
		else if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE){
			JComboBox cbTypes = new JComboBox(MethodStereotypeUI.values());
			table.getColumnModel().getColumn(3 - removedColumns).setCellEditor(new DefaultCellEditor(cbTypes));
		}

		if (!(Boolean) properties.getPropertyValue("methodReturnTypeEditable")){
			table.removeColumn(table.getColumnModel().getColumn(4-removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("methodModifierEditable")){
			table.removeColumn(table.getColumnModel().getColumn(5-removedColumns));
			removedColumns++;
		}
		else{
			JComboBox cbModifiers = new JComboBox(Modifier.values());
			table.getColumnModel().getColumn(5-removedColumns).setCellEditor(new DefaultCellEditor(cbModifiers));
		}

		if (!(Boolean) properties.getPropertyValue("methodConstructorEditable")){
			table.removeColumn(table.getColumnModel().getColumn(6-removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("methodStaticEditable")){
			table.removeColumn(table.getColumnModel().getColumn(7-removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("methodFinalEditable")){
			table.removeColumn(table.getColumnModel().getColumn(8-removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("methodAbstractEditable")){
			table.removeColumn(table.getColumnModel().getColumn(9-removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("methodParametersEditable")){
			table.removeColumn(table.getColumnModel().getColumn(10-removedColumns));
			removedColumns++;
		}


		double percent = (double)(columnNum-removedColumns)/columnNum;

		MainFrame mf = MainFrame.getInstance(); 
		setSize((int) ((mf.getWidth())*0.7 * percent) , mf.getHeight() / 2);
		setLocationRelativeTo(mf);

		table.getColumnModel().getColumn(0).setMaxWidth(20);
		table.getColumnModel().getColumn(0).setMinWidth(20);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = table.getSelectedRow();
				boolean last = table.getSelectedRow() == table.getModel().getRowCount() - 1;
				//add new method
				if (last){
					Method method = new Method();
					if (element instanceof Interface) {
						method.setAbstractMethod(true);
						method.setConstructorMethod(false);
						method.setModifier(Modifier.PUBLIC);
					}
					((MethodTableModel)table.getModel()).addMethod(method);
					table.changeSelection(index, 2, true, true);
					((RXTable) table).setSelectAllForEdit(true);
				}

			}
		});



		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				String name = (String) table.getValueAt(selectedRow, 2);
				int answer = Dialogs.showYesNoDialog("Are you sure you want to delete '" + name + "'?", "Delete");
				if (answer == JOptionPane.YES_OPTION) {
					((MethodTableModel)table.getModel()).removeMethod(selectedRow);
					selectPrevious(selectedRow);
				}
			}
		});


		btnUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow <= 0)
					return;
				((MethodTableModel)table.getModel()).moveMethodUp(selectedRow);
				table.getSelectionModel().setSelectionInterval(selectedRow - 1, selectedRow - 1);

			}
		});

		btnDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = table.getSelectedRow();
				int rowsNum = table.getRowCount();
				if (selectedRow == -1 || selectedRow == rowsNum - 1)
					return;
				((MethodTableModel)table.getModel()).moveMethodDown(selectedRow);
				table.getSelectionModel().setSelectionInterval(selectedRow + 1, selectedRow + 1);

			}
		});
	}

	public void setParameters(final GraphElement element, Method method, List<Parameter> parameters) {
		setTitle("Parameters");

		table.setModel(new ParameterTableModel(element, method, parameters));

		int removedColumns = 0;

		if (!(Boolean) properties.getPropertyValue("parameterNameEditable")){
			table.removeColumn(table.getColumnModel().getColumn(1));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("parameterTypeEditable")){
			table.removeColumn(table.getColumnModel().getColumn(2));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("parameterFinalEditable")){
			table.removeColumn(table.getColumnModel().getColumn(3));
			removedColumns++;
		}

		int columnNum = table.getColumnCount();
		double percent = (double)(columnNum-removedColumns)/columnNum;

		MainFrame mf = MainFrame.getInstance(); 
		setSize((int) ((mf.getWidth())*0.3 * percent) , mf.getHeight() / 2);
		setLocationRelativeTo(mf);

		table.getColumnModel().getColumn(0).setMaxWidth(20);
		table.getColumnModel().getColumn(0).setMinWidth(20);

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = table.getSelectedRow();
				boolean last = table.getSelectedRow() == table.getModel().getRowCount() - 1;
				//add new parameter
				if (last){
					Parameter parameter = new Parameter();
					((ParameterTableModel)table.getModel()).addParameter(parameter);
					table.changeSelection(index, 1, true, true);
					((RXTable) table).setSelectAllForEdit(true);
				}
			}
		});

		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				String name = (String) table.getValueAt(selectedRow, 1);
				int answer = Dialogs.showYesNoDialog("Are you sure you want to delete '" + name + "'?", "Delete");
				if (answer == JOptionPane.YES_OPTION) {
					((ParameterTableModel)table.getModel()).removeParameter(selectedRow);
					selectPrevious(selectedRow);
				}
			}
		});

		btnUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow <= 0)
					return;
				((ParameterTableModel)table.getModel()).moveParameterUp(selectedRow);
				table.getSelectionModel().setSelectionInterval(selectedRow - 1, selectedRow - 1);

			}
		});

		btnDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = table.getSelectedRow();
				int rowsNum = table.getRowCount();
				if (selectedRow == -1 || selectedRow == rowsNum - 1)
					return;
				((ParameterTableModel)table.getModel()).moveParameterDown(selectedRow);
				table.getSelectionModel().setSelectionInterval(selectedRow + 1, selectedRow + 1);

			}
		});
	}


	public void setEnumValues(Attribute attribute) {
		setTitle("Possible values");

		table.setModel(new EnumValuesTableModel(attribute));


		MainFrame mf = MainFrame.getInstance(); 
		setSize((int) ((mf.getWidth())*0.3 ) , mf.getHeight() / 2);
		setLocationRelativeTo(mf);

		table.getColumnModel().getColumn(0).setMaxWidth(20);
		table.getColumnModel().getColumn(0).setMinWidth(20);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent event) {
				int index = table.getSelectedRow();
				boolean last = table.getSelectedRow() == table.getModel().getRowCount() - 1;
				//add possible value
				if (last){
					String value = "value";
					((EnumValuesTableModel)table.getModel()).addPossibleValue(value);
					table.changeSelection(index, 1, true, true);
					((RXTable) table).setSelectAllForEdit(true);
				}
			}
		});

		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				((EnumValuesTableModel)table.getModel()).removeValue(selectedRow);
				selectPrevious(selectedRow);
			}
		});

		btnUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow <= 0)
					return;
				((EnumValuesTableModel)table.getModel()).moveValueUp(selectedRow);
				table.getSelectionModel().setSelectionInterval(selectedRow - 1, selectedRow - 1);

			}
		});

		btnDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = table.getSelectedRow();
				int rowsNum = table.getRowCount();
				if (selectedRow == -1 || selectedRow == rowsNum - 1)
					return;
				((EnumValuesTableModel)table.getModel()).moveValueDown(selectedRow);
				table.getSelectionModel().setSelectionInterval(selectedRow + 1, selectedRow + 1);

			}
		});
	}





	private void selectPrevious(int selectedRow){
		//select previous row if row count > 1
		if (table.getRowCount() <= 1)
			return;
		int toSelect;
		if (selectedRow == 0)
			toSelect = selectedRow;
		else 
			toSelect = selectedRow - 1;
		table.getSelectionModel().setSelectionInterval(toSelect, toSelect);
	}
}
