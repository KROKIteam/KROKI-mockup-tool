package graphedit.gui.table;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.gui.utils.Dialogs;
import graphedit.model.components.Attribute;
import graphedit.model.components.AttributeTypeUI;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Interface;
import graphedit.model.components.Method;
import graphedit.model.components.MethodStereotypeUI;
import graphedit.model.components.Modifier;
import graphedit.model.components.Parameter;
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

public class ClassDialog extends JDialog {

	private static final long serialVersionUID = 1L;


	private JTable table;
	private JButton add;
	private JButton remove;
	private ClassToolBar toolBar;

	private ApplicationModeProperties properties;


	public ClassDialog() {
		super(MainFrame.getInstance(), true);
		properties = ApplicationModeProperties.getInstance();

		MainFrame mf = MainFrame.getInstance(); 
		setSize(new Dimension(mf.getWidth()/2, mf.getHeight()/2));

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add = new JButton("Add");
		remove = new JButton("Remove");
		toolBar = new ClassToolBar(add, remove);

		add(toolBar, BorderLayout.SOUTH);
		add(new JScrollPane(table), BorderLayout.CENTER);

		setLocationRelativeTo(mf);
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setAttributes(final GraphElement element, List<Attribute> attributes) {
		setTitle("Attributes");

		table.setModel(new AttributeTableModel(element, attributes));
		int columnNum = table.getColumnCount();

		JComboBox cbModifiers = new JComboBox(Modifier.values());
		JComboBox cbElements = new JComboBox(MainFrame.getInstance().getCurrentView().getModel().getDiagramElements().toArray());


		int removedColumns = 0;


		if (!(Boolean) properties.getPropertyValue("attributeClassEditable")){
			table.removeColumn(table.getColumnModel().getColumn(0));
			removedColumns++;
		}
		else
			table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cbElements));
		if (!(Boolean) properties.getPropertyValue("attributeNameEditable")){
			table.removeColumn(table.getColumnModel().getColumn(1 - removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("attributeTypeEditable")){
			table.removeColumn(table.getColumnModel().getColumn(2 - removedColumns));
			removedColumns++;
		}
		else if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE){
			JComboBox cbTypes = new JComboBox(AttributeTypeUI.values());
			table.getColumnModel().getColumn(2 - removedColumns).setCellEditor(new DefaultCellEditor(cbTypes));
		}

		if (!(Boolean) properties.getPropertyValue("attributeModifierEditable")){
			table.removeColumn(table.getColumnModel().getColumn(3 - removedColumns));
			removedColumns++;
		}
		else
			table.getColumnModel().getColumn(3 - removedColumns).setCellEditor(new DefaultCellEditor(cbModifiers));

		if (!(Boolean) properties.getPropertyValue("attributeStaticEditable")){
			table.removeColumn(table.getColumnModel().getColumn(4 - removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("attributeFinalEditable")){
			table.removeColumn(table.getColumnModel().getColumn(5 - removedColumns));
			removedColumns++;
		}

		double percent = (double)(columnNum-removedColumns)/columnNum;
		MainFrame mf = MainFrame.getInstance(); 
		setSize((int) ((mf.getWidth())*0.5 * percent) , mf.getHeight() / 2);
		setLocationRelativeTo(mf);


		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Attribute attribute = new Attribute();
				if (element instanceof Interface) {
					attribute.setFinalAttribute(true);
					attribute.setModifier(Modifier.PUBLIC);
					attribute.setStaticAttribute(true);
				}
				((AttributeTableModel)table.getModel()).addAttribute(attribute);
			}
		});
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				String name = (String) table.getValueAt(selectedRow, 0);
				int answer = Dialogs.showYesNoDialog("Are you sure you want to delete '" + name + "'?", "Delete");
				if (answer == JOptionPane.YES_OPTION) {
					((AttributeTableModel)table.getModel()).removeAttribute(selectedRow);
				}
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
			table.removeColumn(table.getColumnModel().getColumn(0));
			removedColumns++;
		}
		else
			table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cbElements));

		if (!(Boolean) properties.getPropertyValue("methodNameEditable")){
			table.removeColumn(table.getColumnModel().getColumn(1 - removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("methodStereotypeEditable")){
			table.removeColumn(table.getColumnModel().getColumn(2-removedColumns));
			removedColumns++;
		}
		else if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE){
			JComboBox cbTypes = new JComboBox(MethodStereotypeUI.values());
			table.getColumnModel().getColumn(2 - removedColumns).setCellEditor(new DefaultCellEditor(cbTypes));
		}

		if (!(Boolean) properties.getPropertyValue("methodReturnTypeEditable")){
			table.removeColumn(table.getColumnModel().getColumn(3-removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("methodModifierEditable")){
			table.removeColumn(table.getColumnModel().getColumn(4-removedColumns));
			removedColumns++;
		}
		else{
			JComboBox cbModifiers = new JComboBox(Modifier.values());
			table.getColumnModel().getColumn(4-removedColumns).setCellEditor(new DefaultCellEditor(cbModifiers));
		}

		if (!(Boolean) properties.getPropertyValue("methodConstructorEditable")){
			table.removeColumn(table.getColumnModel().getColumn(5-removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("methodStaticEditable")){
			table.removeColumn(table.getColumnModel().getColumn(6-removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("methodFinalEditable")){
			table.removeColumn(table.getColumnModel().getColumn(7-removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("methodAbstractEditable")){
			table.removeColumn(table.getColumnModel().getColumn(8-removedColumns));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("methodParametersEditable")){
			table.removeColumn(table.getColumnModel().getColumn(9-removedColumns));
			removedColumns++;
		}


		double percent = (double)(columnNum-removedColumns)/columnNum;

		MainFrame mf = MainFrame.getInstance(); 
		setSize((int) ((mf.getWidth())*0.7 * percent) , mf.getHeight() / 2);
		setLocationRelativeTo(mf);


		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Method method = new Method();
				if (element instanceof Interface) {
					method.setAbstractMethod(true);
					method.setConstructorMethod(false);
					method.setModifier(Modifier.PUBLIC);
				}
				((MethodTableModel)table.getModel()).addMethod(method);
			}
		});
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				String name = (String) table.getValueAt(selectedRow, 1);
				int answer = Dialogs.showYesNoDialog("Are you sure you want to delete '" + name + "'?", "Delete");
				if (answer == JOptionPane.YES_OPTION) {
					((MethodTableModel)table.getModel()).removeMethod(selectedRow);
				}
			}
		});
	}

	public void setParameters(Method method, List<Parameter> parameters) {
		setTitle("Parameters");

		table.setModel(new ParameterTableModel(method, parameters));

		int removedColumns = 0;

		if (!(Boolean) properties.getPropertyValue("parameterNameEditable")){
			table.removeColumn(table.getColumnModel().getColumn(0));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("parameterTypeEditable")){
			table.removeColumn(table.getColumnModel().getColumn(1));
			removedColumns++;
		}
		if (!(Boolean) properties.getPropertyValue("parameterFinalEditable")){
			table.removeColumn(table.getColumnModel().getColumn(2));
			removedColumns++;
		}
		
		int columnNum = table.getColumnCount();
		double percent = (double)(columnNum-removedColumns)/columnNum;

		MainFrame mf = MainFrame.getInstance(); 
		setSize((int) ((mf.getWidth())*0.3 * percent) , mf.getHeight() / 2);
		setLocationRelativeTo(mf);
		
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Parameter parameter = new Parameter();
				((ParameterTableModel)table.getModel()).addParameter(parameter);
			}
		});
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				String name = (String) table.getValueAt(selectedRow, 0);
				int answer = Dialogs.showYesNoDialog("Are you sure you want to delete '" + name + "'?", "Delete");
				if (answer == JOptionPane.YES_OPTION) {
					((ParameterTableModel)table.getModel()).removeParameter(selectedRow);
				}
			}
		});
	}
}
