package graphedit.gui.dialog;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.gui.table.ClassDialog;
import graphedit.model.components.Attribute;
import graphedit.model.components.Class;
import graphedit.model.components.Method;
import graphedit.model.enums.ClassStereotypeUI;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ClassElementDialog extends JDialog{


	private static final long serialVersionUID = 1L;

	private JTextField tfClassName = new JTextField(10);
	private JTextField tfStereotype = new JTextField(10);
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JComboBox<?> cbStereotype = new JComboBox(ClassStereotypeUI.values());
	private JButton btnAttributes = new JButton("...");
	private JButton btnMethods = new JButton("...");
	private JLabel lblClassName = new JLabel("Name:"), lblStereotype = new JLabel("Stereotype:"),
			lblAttributes = new JLabel("Attributes:"), lblMethods = new JLabel("Methods:"); 
	private boolean ok = false;

	private Class classElement1;


	public ClassElementDialog(Class classElement){

		super(MainFrame.getInstance(),"Class properties",true);
		setSize(220,180);
		setResizable(false);
		setLayout(new MigLayout("fillx"));
		setLocationRelativeTo(MainFrame.getInstance());
		this.classElement1 = classElement;

		add(lblClassName);
		add(tfClassName, "wrap");
		tfClassName.setText((String) classElement.getProperty(GraphElementProperties.NAME));

		add(lblStereotype);
		if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE || 
				MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE_MIXED){
			add(cbStereotype, "wrap");

			if (((String)classElement.getProperty(GraphElementProperties.STEREOTYPE)).equals("StandardPanel"))
				cbStereotype.setSelectedIndex(0);
			else
				cbStereotype.setSelectedIndex(1);
		}
		else if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE_PERSISTENT){
			add(tfStereotype,  "wrap");
			tfStereotype.setEditable(false);
			tfStereotype.setText((String) classElement.getProperty(GraphElementProperties.STEREOTYPE));
		}
		else{
			add(tfStereotype,  "wrap");
			tfStereotype.setText((String) classElement.getProperty(GraphElementProperties.STEREOTYPE));
		}


		add(lblAttributes);
		add(btnAttributes,  "wrap");

		btnAttributes.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				ClassDialog dlg = new ClassDialog();
				dlg.setAttributes(classElement1, (List<Attribute>) classElement1.getProperty(GraphElementProperties.ATTRIBUTES));
				dlg.setVisible(true);
			}
		});

		add(lblMethods);
		add(btnMethods,  "wrap");

		btnMethods.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				ClassDialog dlg = new ClassDialog();
				dlg.setMethods(classElement1, (List<Method>) classElement1.getProperty(GraphElementProperties.METHODS));
				dlg.setVisible(true);
			}
		});

		JPanel lowerPanel=new JPanel(new MigLayout());

		JButton btnCancel=new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);		
			}			
		});

		JButton btnOk=new JButton("OK");
		btnOk.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				ok = true;
				setVisible(false);
			}
		});

		lowerPanel.add(btnOk);
		lowerPanel.add(btnCancel);
		add(lowerPanel, "span 2, right");
		pack();
	}

	public String getName(){
		return tfClassName.getText().trim();
	}

	public String getStereotype(){
		if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE || 
				MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE_MIXED)
			return cbStereotype.getSelectedItem().toString();
		return tfClassName.getText().trim();
	}


	public boolean isOk() {
		return ok;
	}

}
