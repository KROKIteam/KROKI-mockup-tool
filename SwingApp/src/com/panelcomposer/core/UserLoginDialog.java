package com.panelcomposer.core;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import util.staticnames.Messages;
import util.staticnames.Settings;

import com.panelcomposer.elements.SMainForm;

@SuppressWarnings("serial")
public class UserLoginDialog extends JDialog {
	
	protected JTextField jtfUsername = new JTextField(30); 
	protected JPasswordField jpfPassword = new JPasswordField(30);
	protected JButton btnOk = new JButton(Settings.BTN_OK);
	protected JButton btnCancel = new JButton(Settings.BTN_CANCEL);
	
	public UserLoginDialog(SMainForm mainForm) {
		super(mainForm, true);
		setSize(new Dimension(300, 150));
		setLayout(new MigLayout("", "[0:0,grow 100,fill]", ""));
		setLocationRelativeTo(mainForm);
		JPanel dialogPanel = new JPanel();
		setTitle(Settings.LOGIN);
		
		jtfUsername.setText("admin");
		jpfPassword.setText("admin");
		
		dialogPanel.setLayout(new GridLayout(3, 2));
		dialogPanel.add(new JLabel(Settings.USERNAME + ":"));
		dialogPanel.add(jtfUsername);
		dialogPanel.add(new JLabel(Settings.PASSWORD + ":"));
		dialogPanel.add(jpfPassword);
		
		getRootPane().setDefaultButton(btnOk);
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String username = jtfUsername.getText();
				char[] password = jpfPassword.getPassword();
				boolean success = AppCache.getInstance().logIn(username, password);
				if(success) {
					UserLoginDialog.this.dispose();
					System.out
							.println("UserLoginDialog.UserLoginDialog(...).new ActionListener() {...}.actionPerformed()");
					
				} else {
					JOptionPane.showMessageDialog(UserLoginDialog.this, Messages.BAD_LOGIN, 
							Messages.ERROR, JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		dialogPanel.add(btnOk);
		dialogPanel.add(btnCancel);
		add(dialogPanel);
	}
}
