package gui.menudesigner;

import gui.menudesigner.model.Submenu;
import gui.menudesigner.tree.MenusWorkspace;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class MenuNameDialog extends JDialog {
	private JLabel lblMenuName;
	private JTextField tfMenuName;

	private JButton btnOK;

	public MenuNameDialog(MenuDesignerPanel menuDesignerPanel, Submenu rootMenu, JTree tree, MenusWorkspace workspace, JTree menusTree) {
		setLocationRelativeTo(null);
		BtnOKActionLister btnOkActionListener = new BtnOKActionLister(menuDesignerPanel, rootMenu, tree, workspace, menusTree);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400, 90);
		setModal(true);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		lblMenuName = new JLabel("Menu Name");
		tfMenuName = new JTextField(20);
		menuPanel.add(lblMenuName);
		menuPanel.add(tfMenuName);

		btnOK = new JButton("OK");
		btnOK.addActionListener(btnOkActionListener);
		menuPanel.add(btnOK);

		mainPanel.add(menuPanel);
		add(mainPanel);
	}

	private class BtnOKActionLister implements ActionListener {
		private JTree tree;
		private Submenu rootMenu;
		MenuDesignerPanel menuDesignerPanel;
		MenusWorkspace workspace;
		JTree menusTree;
		public BtnOKActionLister(MenuDesignerPanel menuDesignerPanel, Submenu rootMenu, JTree tree, MenusWorkspace workspace, JTree menusTree) {
			this.tree = tree;
			this.rootMenu = rootMenu;
			this.menuDesignerPanel = menuDesignerPanel;
			this.workspace = workspace;
			this.menusTree = menusTree;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String menuName = tfMenuName.getText();
			rootMenu.setName(menuName);
			workspace.addMenu(rootMenu);
			tree.updateUI();
			menusTree.updateUI();
			menuDesignerPanel.dispose();
			MenuNameDialog.this.dispose();
		}

	}
}
