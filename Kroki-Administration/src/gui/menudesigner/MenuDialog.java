package gui.menudesigner;

import gui.menudesigner.model.Submenu;
import gui.menudesigner.tree.MenuWorkspace;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;

public class MenuDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblMenuName;
	private JTextField tfMenuName;
	private JButton btnOK;
	private BtnOKActionLister btnOkActionListener;
	public MenuDialog(JTree tree, MenuWorkspace menuWorkspace) {
		btnOkActionListener = new BtnOKActionLister(tree, menuWorkspace);
		initGui();
	}
	
	public MenuDialog(JTree tree, Submenu submenu) {
		btnOkActionListener = new BtnOKActionLister(tree, submenu);
		initGui();
	}
	
	private void initGui() {
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400,100);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		lblMenuName = new JLabel("Menu Name");
		tfMenuName = new JTextField(20);
		menuPanel.add(lblMenuName);
		menuPanel.add(tfMenuName);
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		btnOK = new JButton("OK");
		btnOK.addActionListener(btnOkActionListener);
		btnPanel.add(btnOK);
		
		mainPanel.add(menuPanel);
		mainPanel.add(btnPanel);
		add(mainPanel);
	}
	
	private class BtnOKActionLister implements ActionListener {
		private JTree tree;
		private MenuWorkspace menuWorkspace;
		private Submenu submenu;
		
		public BtnOKActionLister(JTree tree, MenuWorkspace menuWorkspace) {
			this.tree = tree;
			this.menuWorkspace = menuWorkspace;
		}

		public BtnOKActionLister(JTree tree2, Submenu submenu) {
			this.tree = tree2;
			this.submenu = submenu;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String menuName = tfMenuName.getText();
			Submenu tempMenu = new Submenu(menuName);
			if(submenu != null) {
				tempMenu.setParent(submenu);
				submenu.getChildren().add(tempMenu);
			} else {
				tempMenu.setParent(menuWorkspace.getRoot());
				menuWorkspace.addMenu(tempMenu);
			}
			tree.updateUI();
			dispose();
		}
	}

}
