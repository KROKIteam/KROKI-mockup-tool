package gui.menudesigner;

import ejb.administration.Role;
import gui.menudesigner.model.Menu;
import gui.menudesigner.model.MenuItem;
import gui.menudesigner.model.Submenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import kroki.generators.MenuGenerator;

public class MenuDesignerPanel extends JDialog {

	private JTree tree;
	private JTree menusTree;
	private JPanel panelBottom;
	private JSplitPane splitPane;
	DefaultMutableTreeNode root;
	DefaultMutableTreeNode selectedNode = null;
	DefaultMutableTreeNode menusRoot;
	
	private JButton btnMenu;
	private JButton btnSubmenu;
	private JButton btnDelete;
	private JButton btnSeparator;
	private JButton btnOK;
	JList panelJList = null;
	
	//Mapa predstavlja meni
	//Kljuc je naziv menija koji sadrzi podmenije
	//vrednost je lista menu nodova
	private Map<String, Submenu> menuMap;
	private Submenu rootMenu;
	private List<Submenu> menusList;
	public MenuDesignerPanel(DefaultMutableTreeNode menusRoot, JTree menusTree, List<Submenu> menusList) {
		
		this.menusRoot = menusRoot;
		this.menusTree = menusTree;
		this.menusList = menusList;
		
		BtnOKActionLister btnOkActionListener = new BtnOKActionLister();
		Container content = getContentPane();
		content.setBackground(Color.WHITE);
		//setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		menuMap = new HashMap<String, Submenu>();
		rootMenu = new Submenu("Menu");
		menuMap.put("Menu", rootMenu);
		
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
		
		//create the root node
        root = new DefaultMutableTreeNode("Menu");
        //create the child nodes

        //create the tree by passing in the root node
        tree = new JTree(root);
        tree.setShowsRootHandles(true);
        //tree.setRootVisible(false);

        
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
         
        //Selekcija nodea u tree i postavljanje vrednosti selected node...
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			}
		});
        
        btnMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedNode == null)
					return;
				Menu temp = menuMap.get(selectedNode.getUserObject().toString());
				if (temp instanceof Submenu) {
					MenuDialog mn = new MenuDialog(selectedNode, tree, menuMap, rootMenu, (Submenu) temp);
					mn.setVisible(true);
				} else {
					JOptionPane.showMessageDialog (null, "You must select Submenu!", "Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
        
        btnSubmenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedNode == null)
					return;
				MenuItemDialog mn = new MenuItemDialog(selectedNode, tree, menuMap, menuMap.get(selectedNode.getUserObject().toString()));
				mn.setVisible(true);
			}
		});
        
        btnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedNode == null)
					return;
				
				Menu temp = menuMap.get(selectedNode.getUserObject().toString());
				if (temp != null && temp.getParent() == null) {
					JOptionPane.showMessageDialog (null, "Can't delete root menu!", "Error!", JOptionPane.ERROR_MESSAGE);
				} else if (temp != null && temp instanceof Submenu) {
					Submenu tempSubmenu = (Submenu)temp;
					Submenu parentSubmenu = (Submenu)tempSubmenu.getParent();
					parentSubmenu.getChildren().remove(tempSubmenu);
					menuMap.remove(temp);
					
					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
					parentNode.remove(selectedNode);
					DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
					model.reload(parentNode);
				} else if (temp == null && selectedNode != null) {
					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();

					Menu tempParent = menuMap.get(parentNode.getUserObject().toString());
					Submenu tempSubmenu = (Submenu) tempParent;
					if (tempSubmenu != null) {
						MenuItem forDelete = null;
						for (Menu m : tempSubmenu.getChildren()) {
							if (m instanceof MenuItem ) {
								if (((MenuItem) m).getMenuName().equals(selectedNode.getUserObject().toString()))
									forDelete = (MenuItem)m;
							}
						}
						if (forDelete != null)
							tempSubmenu.getChildren().remove(forDelete);
					}

					parentNode.remove(selectedNode);
					DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
					model.reload(parentNode);
				}
			}
		});
        
        btnSeparator.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedNode == null)
					return;
				Menu temp = menuMap.get(selectedNode.getUserObject().toString());
				if (temp != null && temp.getParent() == null) {
					root.add(new DefaultMutableTreeNode("Separator"));
					DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
					model.reload(root);
					MenuItem miSeparator = new MenuItem();
					miSeparator.setMenuName("Separator");
					miSeparator.setParent(temp);
					rootMenu.getChildren().add(miSeparator);
				} else if (temp != null && temp instanceof Submenu) {
					Submenu tempSubmenu = (Submenu)temp;
					MenuItem miSeparator = new MenuItem();
					miSeparator.setMenuName("Separator");
					miSeparator.setParent(tempSubmenu);
					tempSubmenu.getChildren().add(miSeparator);
					selectedNode.add(new DefaultMutableTreeNode("Separator"));
					DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
					model.reload(selectedNode);
					
				} else if (temp == null && selectedNode != null) {
					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
					Submenu tempSubmenu = (Submenu) menuMap.get(parentNode.getUserObject().toString());
					if (tempSubmenu != null) {
						MenuItem miSeparator = new MenuItem();
						miSeparator.setMenuName("Separator");
						miSeparator.setParent(tempSubmenu);
						tempSubmenu.getChildren().add(miSeparator);
						parentNode.add(new DefaultMutableTreeNode("Separator"));
						DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
						model.reload(parentNode);
					}
				}

				
			}
		});
        
        btnOK.addActionListener(btnOkActionListener);
        this.pack();
        setLocationRelativeTo(null);
	}
	
	private class BtnOKActionLister implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (rootMenu.getChildren().isEmpty()) {
				JOptionPane.showMessageDialog (null, "Root menu is empty!", "Error!", JOptionPane.ERROR_MESSAGE);
			} else if (panelJList.getSelectedValuesList().isEmpty()) {
				JOptionPane.showMessageDialog (null, "You must select Role!", "Error!", JOptionPane.ERROR_MESSAGE);
			} else {
				rootMenu.setRoles((ArrayList<String>) panelJList.getSelectedValuesList());
				MenuNameDialog mnd = new MenuNameDialog(MenuDesignerPanel.this,menusRoot, menusTree, menusList, rootMenu);
				mnd.setVisible(true);
				MenuDesignerPanel.this.dispose();
			}
		}
	}

	public DefaultMutableTreeNode getRoot() {
		return root;
	}

	public void setRoot(DefaultMutableTreeNode root) {
		this.root = root;
	}

	public Map<String, Submenu> getMenuMap() {
		return menuMap;
	}

	public void setMenuMap(Map<String, Submenu> menuMap) {
		this.menuMap = menuMap;
	}
}
