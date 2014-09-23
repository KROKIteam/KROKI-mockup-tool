package gui.menudesigner;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import ejb.administration.Resource;
import gui.menudesigner.model.Menu;
import gui.menudesigner.model.Submenu;

public class MenuDialog extends JDialog {
	
	private JLabel lblMenuName;
	private JTextField tfMenuName;
	private JButton btnOK;
	
	public MenuDialog(DefaultMutableTreeNode selectedNode, JTree tree, Map<String, Submenu> menuMap, Submenu rootMenu, Submenu selectedSubmenu) {
		setLocationRelativeTo(null);
		BtnOKActionLister btnOkActionListener = new BtnOKActionLister(selectedNode, tree, menuMap, rootMenu, selectedSubmenu);
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
		private DefaultMutableTreeNode selectedNode;
		private JTree tree;
		private Map<String, Submenu> menuMap;
		private Submenu rootMenu;
		private Submenu selectedSubmenu;
		
		public BtnOKActionLister(DefaultMutableTreeNode selectedNode, JTree tree, Map<String, Submenu> menuMap2, Submenu rootMenu, Submenu selectedSubmenu) {
			this.selectedNode = selectedNode;
			this.tree = tree;
			this.menuMap = menuMap2;
			this.rootMenu = rootMenu;
			this.selectedSubmenu = selectedSubmenu;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (selectedNode != null) {
				String menuName = tfMenuName.getText();
				Submenu tempMenu = new Submenu(menuName);
				tempMenu.setParent(selectedSubmenu);
				selectedSubmenu.getChildren().add(tempMenu);
				menuMap.put(menuName, tempMenu);
				selectedNode.add(new DefaultMutableTreeNode(menuName));
				DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
				model.reload(selectedNode);
				dispose();
			} else {
				System.out.println("Selected node je null");
			}
		}
		
	}

}
