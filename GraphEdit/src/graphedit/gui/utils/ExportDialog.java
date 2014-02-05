package graphedit.gui.utils;

import graphedit.app.MainFrame;
import graphedit.model.GraphEditWorkspace;
import graphedit.util.GraphicsExportUtility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ExportDialog extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tfName = new JTextField(15);
	private JComboBox<Object> cbxType = new JComboBox<Object>(GraphicsExportUtility.getSupportedTypes());
	private JLabel fileNameLabel = new JLabel("File name:");
	private JLabel fileTypeLabel = new JLabel("File type:");
	private JButton btnOK = new JButton("Ok");
	private JButton btnCancel = new JButton("Cancel");
	private JPanel filePropertiesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JPanel buttonsPanel = new JPanel();
	private JPanel firstRowPanel = new JPanel();
	private JPanel secondRowPanel = new JPanel();
	
	private String fileName;
	private static File file;
	private static boolean fileCorrect = false;
	
	private ExportDialog(){
		setTitle("GraphEdit Export");
		setModal(true);
		setResizable(false);
		setPreferredSize(new Dimension(300, 210));
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		filePropertiesPanel.setBorder(BorderFactory.createTitledBorder("Select image name and type"));
		
		fileCorrect = false;
		
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkFile();
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		tfName.addKeyListener(new KeyListener() {
			private boolean removeLastChar = false;
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(removeLastChar){
					tfName.setText(tfName.getText().substring(0, tfName.getText().length()-1));
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					checkFile();
					removeLastChar = false;
					return;
				}
				if(e.isActionKey() || e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
					removeLastChar = false;
					return;
				}
				if(!Character.isLetterOrDigit(e.getKeyChar())){
					removeLastChar = true;
				}else{
					removeLastChar = false;
				}
			}
		});
		tfName.setAlignmentY(RIGHT_ALIGNMENT);
		
		fileNameLabel.setForeground(Color.blue);
		fileTypeLabel.setForeground(Color.blue);
		
		firstRowPanel.add(fileNameLabel);
		firstRowPanel.add(tfName);
		
		secondRowPanel.add(fileTypeLabel);
		secondRowPanel.add(cbxType);
		
		filePropertiesPanel.add(firstRowPanel);
		filePropertiesPanel.add(secondRowPanel);
		
		btnCancel.setMnemonic(KeyEvent.VK_C);
		btnOK.setMnemonic(KeyEvent.VK_O);
		
		buttonsPanel.add(btnCancel);
		buttonsPanel.add(btnOK);
		setLocationRelativeTo(MainFrame.getInstance());
		add(Box.createVerticalStrut(10));
		add(filePropertiesPanel);
		add(buttonsPanel);
		pack();
		setLocationRelativeTo(MainFrame.getInstance());
		
		setVisible(true);
	}
	
	private void checkFile(){
		if (tfName.getText().equals("")) {
			Dialogs.showErrorMessage("File name cannot be left empty", "Error");
			return;
		}  
		fileName = tfName.getText()+"."+cbxType.getSelectedItem();
		file = new File(GraphEditWorkspace.getInstance().getFile(), fileName);
		if(file.exists()){
			Dialogs.showErrorMessage("File already exists", "Error");
		}else{
			setVisible(false);
			fileCorrect = true;
		}
	}
	
	public static File getFileName(){
		new ExportDialog();
		if(fileCorrect)
			return file;
		else
			return null;
	}

}
