package com.panelcomposer.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
		//setLayout(new MigLayout("", "[0:0,grow 100,fill]", ""));
		setLocationRelativeTo(mainForm);
		JPanel topPanel = new JPanel(new GridBagLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		setTitle(Settings.LOGIN);
		setResizable(false);
		
		jtfUsername.setText("admin");
		jpfPassword.setText("admin");
		
		GridBagConstraints gbc = new GridBagConstraints();
		 
		gbc.insets = new Insets(4, 4, 4, 4);
 
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		topPanel.add(new JLabel(Settings.USERNAME + ":"), gbc);
 
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		topPanel.add(jtfUsername, gbc);
 
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		topPanel.add(new JLabel(Settings.PASSWORD + ":"), gbc);
 
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		topPanel.add(jpfPassword, gbc);
		
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
		setAlwaysOnTop(true);
		setModal(true);
		
		buttonPanel.add(btnOk);
		buttonPanel.add(btnCancel);
		
		add(topPanel);
		add(buttonPanel, BorderLayout.SOUTH);
	}
}
