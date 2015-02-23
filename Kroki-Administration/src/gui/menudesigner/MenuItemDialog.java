package gui.menudesigner;

import dao.administration.ResourceHibernateDao;
import ejb.administration.Resource;
import framework.MainFrame;
import gui.menudesigner.model.MenuItem;
import gui.menudesigner.model.Submenu;
import gui.menudesigner.tree.MenuWorkspace;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class MenuItemDialog extends JDialog {
	
	private JLabel lblForm;
	private JComboBox cbForm;
	private JLabel lblMenuName;
	private JTextField tfMenuName;
	
	private JButton btnOK;
	

	public MenuItemDialog(Submenu submenu, JTree tree, MenuWorkspace workspace) {
		setLocationRelativeTo(null);
		BtnOKActionLister btnOkActionListener = new BtnOKActionLister(submenu, tree, workspace);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400,150);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		lblForm = new JLabel("Form");
		cbForm = new JComboBox();
		
		//Punjenje combo boxa resursima
		dao.administration.ResourceHibernateDao rDao = new dao.administration.ResourceHibernateDao();
		List<Resource> resources = rDao.findAll();
		List<String> resourceNames = new ArrayList<String>();
		for (Resource r : resources) {
			resourceNames.add(r.getName());
		}
		cbForm.setModel(new DefaultComboBoxModel(resourceNames.toArray()));
		formPanel.add(lblForm);
		formPanel.add(Box.createHorizontalStrut(32));
		formPanel.add(cbForm);
		
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
		
		mainPanel.add(formPanel);
		mainPanel.add(menuPanel);
		mainPanel.add(btnPanel);
		add(mainPanel);
	}
	

	private class BtnOKActionLister implements ActionListener {
		private JTree tree;
		private Submenu submenu;
		private MenuWorkspace workspace;
		public BtnOKActionLister(Submenu submenu, JTree tree, MenuWorkspace workspace) {
			this.tree = tree;
			this.submenu = submenu;
			this.workspace = workspace;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (cbForm.getSelectedItem() == null) {
				JOptionPane.showMessageDialog (null, "Form can't be null!", "Warning!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String formName = cbForm.getSelectedItem().toString();
			String menuName = tfMenuName.getText();
			
			ResourceHibernateDao rDao = new ResourceHibernateDao();
			Resource tempRes = null;
			List<Resource> listaRes = rDao.findAll();
			for (Resource r : listaRes) {
				if (r.getName().equals(formName)) {
					tempRes = r;
				}
			}
			
			MenuItem tempMenuItem = new MenuItem();
			tempMenuItem.setFormName(formName);
			tempMenuItem.setMenuName(menuName);
			tempMenuItem.setParent(submenu);
			tempMenuItem.setActivate(tempRes.getLink());
			tempMenuItem.setPanelType(tempRes.getPaneltype());
//			tempMenuItem.setPanelType(MainFrame.getInstance().getPanelType().get(formName));
			submenu.getChildren().add(tempMenuItem);
			tree.updateUI();
			dispose();
		}
	}
}
