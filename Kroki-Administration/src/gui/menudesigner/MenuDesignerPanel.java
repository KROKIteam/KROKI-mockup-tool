package gui.menudesigner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;

import ejb.administration.Role;
import gui.menudesigner.model.Submenu;
import gui.menudesigner.tree.MenuHierarchyModel;
import gui.menudesigner.tree.MenuTreeController;
import gui.menudesigner.tree.MenuWorkspace;
import gui.menudesigner.tree.MenusWorkspace;

public class MenuDesignerPanel extends JDialog {

	private JTree tree;
	private JPanel panelBottom;
	private JSplitPane splitPane;
	
	private JButton btnMenu;
	private JButton btnSubmenu;
	private JButton btnDelete;
	private JButton btnSeparator;
	private JButton btnOK;
	JList panelJList = null;

	private MenuWorkspace menuWorkspace;
	private MenuTreeController menuTreeController;
	private MenuHierarchyModel menuHierarchyModel;
	private MenusWorkspace workspace;
	private Submenu submenu;
	private JTree menusTree;
	public MenuDesignerPanel(MenusWorkspace workspace, Submenu submenu, JTree tree2) {
		this.submenu = submenu;
		this.workspace = workspace;
		this.menusTree = tree2;
		BtnOKActionLister btnOkActionListener = new BtnOKActionLister();
		Container content = getContentPane();
		content.setBackground(Color.WHITE);
		//setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		panelBottom = new JPanel();
		panelBottom.setSize(200, 600);
		
		btnMenu = new JButton("Add Menu");
		btnSubmenu = new JButton("Add MenuItem");
		btnDelete = new JButton("Delete");
		btnSeparator = new JButton("Add Separator");
		btnOK = new JButton("OK");
		
		panelBottom.add(btnMenu);
		panelBottom.add(btnSubmenu);
		panelBottom.add(btnSeparator);
		panelBottom.add(btnDelete);
		panelBottom.add(btnOK);

        //create the tree by passing in the root node
        tree = new JTree();
        tree.setShowsRootHandles(true);
        //tree.setRootVisible(false);
        
        menuWorkspace = new MenuWorkspace(submenu);
        menuTreeController = new MenuTreeController(tree, menuWorkspace);
        menuHierarchyModel = new MenuHierarchyModel(tree, menuWorkspace);
        
        tree.addMouseListener(menuTreeController);
        tree.addTreeSelectionListener(menuTreeController);
        menuHierarchyModel.addTreeModelListener(menuTreeController);
        tree.setModel(menuHierarchyModel);
        
        DefaultListModel listModel = new DefaultListModel();
        dao.administration.RoleHibernateDao roleDao = new dao.administration.RoleHibernateDao();
        ArrayList<ejb.administration.Role> roles = (ArrayList<Role>) roleDao.findAll();
        for (Role r : roles) {
        	listModel.addElement(r.getName());
        }

        panelJList = new JList(listModel);
        panelJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        panelJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        panelJList.setVisibleRowCount(-1);
        JScrollPane listScroller = new JScrollPane(panelJList);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setToolTipText("Select desired roles for this menu");
        panelJList.setToolTipText("Select desired roles for this menu");
        
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tree), listScroller);
		splitPane.setDividerLocation(200);
        
        add(splitPane, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);
         
        btnMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menuTreeController.openNewMenuDialog();
			}
		});
        
        btnSubmenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menuTreeController.openNewMenuItemDialog();
			}
		});
        
        btnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				menuTreeController.deleteSelected();
			}
		});
        
        btnSeparator.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				menuTreeController.addSeparator();
			}
		});
        
        btnOK.addActionListener(btnOkActionListener);
        this.pack();
        setLocationRelativeTo(null);
	}
	
	private class BtnOKActionLister implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (submenu.getChildren().isEmpty()) {
				JOptionPane.showMessageDialog (null, "Root menu is empty!", "Error!", JOptionPane.ERROR_MESSAGE);
			} else if (panelJList.getSelectedValuesList().isEmpty()) {
				JOptionPane.showMessageDialog (null, "You must select Role!", "Error!", JOptionPane.ERROR_MESSAGE);
			} else {
				submenu.setRoles((ArrayList<String>) panelJList.getSelectedValuesList());
				MenuNameDialog mnd = new MenuNameDialog(MenuDesignerPanel.this,submenu,tree,workspace, menusTree);
				mnd.setVisible(true);
				MenuDesignerPanel.this.dispose();
			}
		}
	}
	

}
