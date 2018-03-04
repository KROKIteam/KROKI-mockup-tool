package gui.menudesigner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import gui.menudesigner.model.Submenu;
import gui.menudesigner.tree.MenusHierarchyModel;
import gui.menudesigner.tree.MenusTreeController;
import gui.menudesigner.tree.MenusWorkspace;
import kroki.generators.MenuGenerator;

public class MenuSketchDialog extends JDialog {
	
	private JTree tree;
	private JPanel panelBottom;
	DefaultMutableTreeNode root;
	DefaultMutableTreeNode selectedNode = null;
	private JButton btnNewMenu;
	private JButton btnDelete;
	private JButton btnGenerate;
	
	MenusTreeController menusTreeController;
	MenusHierarchyModel menusHierarchyModel;
	MenusWorkspace workspace;
	/**
	 * List containing all menus 
	 */
	public static List<Submenu> menus;
	
	public MenuSketchDialog() {
		setLocationRelativeTo(null);
		Container content = getContentPane();
		content.setBackground(Color.WHITE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
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

        //create the tree by passing in the root node
        tree = new JTree();
        tree.setShowsRootHandles(true);
        //tree.setRootVisible(false);
        
        workspace = new MenusWorkspace();
        workspace.setMenus(menus);
        menusTreeController = new MenusTreeController(tree, workspace);
        menusHierarchyModel = new MenusHierarchyModel(tree, workspace);
        
        tree.addMouseListener(menusTreeController);
        tree.addTreeSelectionListener(menusTreeController);
        menusHierarchyModel.addTreeModelListener(menusTreeController);
        tree.setModel(menusHierarchyModel);


        add(new JScrollPane(tree), BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);
        
        btnNewMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menusTreeController.openMenuDesignerPanel();
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

}
