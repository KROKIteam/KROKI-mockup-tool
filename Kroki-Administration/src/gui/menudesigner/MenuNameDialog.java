package gui.menudesigner;

import gui.menudesigner.model.Submenu;

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

	public MenuNameDialog(MenuDesignerPanel menuDesignerPanel, DefaultMutableTreeNode menusRoot, JTree menusTree, List<Submenu> menusList, Submenu rootMenu) {
		setLocationRelativeTo(null);
		BtnOKActionLister btnOkActionListener = new BtnOKActionLister(menuDesignerPanel,
				menusRoot, menusTree, menusList, rootMenu);
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
		private DefaultMutableTreeNode selectedNode;
		private JTree menusTree;
		private Map<String, Submenu> menuMap;
		private Submenu rootMenu;
		private Submenu selectedSubmenu;
		private List<Submenu> menusList;
		MenuDesignerPanel menuDesignerPanel;
		public BtnOKActionLister(MenuDesignerPanel menuDesignerPanel, DefaultMutableTreeNode menusRoot,
				JTree menusTree, List<Submenu> menusList, Submenu rootMenu) {
			this.selectedNode = menusRoot;
			this.menusTree = menusTree;
			this.rootMenu = rootMenu;
			this.menusList = menusList;
			this.menuDesignerPanel = menuDesignerPanel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (selectedNode != null) {
				String menuName = tfMenuName.getText();
				selectedNode.add(new DefaultMutableTreeNode(menuName));
				DefaultTreeModel model = (DefaultTreeModel) menusTree.getModel();
				model.reload(selectedNode);
				menusList.add(rootMenu);
				menuName = "Menu"; // TODO: CHANGE
				rootMenu.setName(menuName);
				menuDesignerPanel.dispose();
				MenuNameDialog.this.dispose();
			}
		}

	}
}
