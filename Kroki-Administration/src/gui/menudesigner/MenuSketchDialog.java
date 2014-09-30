package gui.menudesigner;

import ejb.administration.Role;
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

public class MenuSketchDialog extends JDialog {
	
	private JTree tree;
	private JPanel panelBottom;
	DefaultMutableTreeNode root;
	DefaultMutableTreeNode selectedNode = null;
	private JButton btnNewMenu;
	private JButton btnDelete;
	private JButton btnGenerate;
	
	//Mapa predstavlja meni
	//Kljuc je naziv menija koji sadrzi podmenije
	//vrednost je lista menu nodova
	private Map<String, Submenu> menuMap;
	private Submenu rootMenu;
	
	private List<Submenu> menus;
	public MenuSketchDialog() {
		setLocationRelativeTo(null);
		Container content = getContentPane();
		content.setBackground(Color.WHITE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		menuMap = new HashMap<String, Submenu>();
		rootMenu = new Submenu("Menu");
		menuMap.put("Menu", rootMenu);
		menus = new ArrayList<Submenu>();
		
		panelBottom = new JPanel();
		panelBottom.setSize(200, 600);
		
		btnNewMenu = new JButton("New Menu");
		btnDelete = new JButton("Delete");
		btnGenerate = new JButton("Generate");
		
		panelBottom.add(btnNewMenu);
		panelBottom.add(btnDelete);
		panelBottom.add(btnGenerate);
		
		//create the root node
        root = new DefaultMutableTreeNode("Menus");
        //create the child nodes

        //create the tree by passing in the root node
        tree = new JTree(root);
        tree.setShowsRootHandles(true);
        //tree.setRootVisible(false);

        add(new JScrollPane(tree), BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);
         
        //Selekcija nodea u tree i postavljanje vrednosti selected node...
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			}
		});
              
        
        btnNewMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MenuDesignerPanel mdp = new MenuDesignerPanel(root, tree, menus);
				mdp.setVisible(true);	
			}
		});
        
        btnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedNode == null)
					return;
				
				Submenu temp = null;
				System.out.println("SELECTED NODE JE :" + selectedNode.getUserObject().toString());
				for(Submenu s : menus) {
					System.out.println("SUBMENU JE " + s.getName());
					if(s.getName().equals(selectedNode.getUserObject().toString()))
						temp = s;
				}
				if(temp != null)
					menus.remove(temp);
				
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
				if(parentNode != null)
					parentNode.remove(selectedNode);
				DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
				model.reload(parentNode);
			}
		});
        
        btnGenerate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MenuGenerator mgen = new MenuGenerator(menus);
				mgen.generate();
			}
		});
        
        this.pack();
        setLocationRelativeTo(null);
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
