package gui.menudesigner.tree;

import gui.menudesigner.MenuDialog;
import gui.menudesigner.MenuItemDialog;
import gui.menudesigner.model.Menu;
import gui.menudesigner.model.MenuItem;
import gui.menudesigner.model.Submenu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class MenuTreeController implements TreeModelListener, TreeSelectionListener, MouseListener {
	
	private JTree tree;
	private MenuWorkspace workspace;
	public MenuTreeController(JTree tree, MenuWorkspace menuWorkspace) {
		this.tree = tree;
		this.workspace = menuWorkspace;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.getClickCount() == 2) {
                TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());
                if (treePath == null) {
                    return;
                }
                Object node = treePath.getLastPathComponent();
                if (node instanceof MenuWorkspace) {
                	MenuWorkspace menuWorkspace = (MenuWorkspace) node;
                	//Otvori dijalog sa datim submenijem
					MenuDialog mn = new MenuDialog(tree, menuWorkspace);
					mn.setVisible(true);
                } else if (node instanceof Submenu) {
                	Submenu submenu = (Submenu)node;
                	MenuItemDialog mid = new MenuItemDialog(submenu, tree, workspace);
                	mid.setVisible(true);
                }
            }
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	public void openNewMenuDialog() {
		TreePath treePath = tree.getSelectionPath();
        if (treePath == null) {
            return;
        }
        Object node = treePath.getLastPathComponent();
        //Ukoliko dodajemo u root
        //Ili drugi submeni
        if (node instanceof MenuWorkspace) {
        	MenuWorkspace menuWorkspace = (MenuWorkspace) node;
			MenuDialog mn = new MenuDialog(tree, menuWorkspace);
			mn.setVisible(true);
        }else if (node instanceof Submenu) {
        	MenuDialog mn = new MenuDialog(tree, (Submenu) node);
			mn.setVisible(true);
        }
	}
	
	public void openNewMenuItemDialog(){
		TreePath treePath = tree.getSelectionPath();
        if (treePath == null) {
            return;
        }
        Object node = treePath.getLastPathComponent();
        Submenu temp = null;
        if (node instanceof MenuWorkspace) {
        	MenuWorkspace menuWorkspace = (MenuWorkspace) node;
        	temp = ((MenuWorkspace) node).getRoot();
        } else if (node instanceof Submenu) {
        	temp = (Submenu)node;
        }
        if (temp != null) {
        	MenuItemDialog mid = new MenuItemDialog(temp, tree, workspace);
        	mid.setVisible(true);
        }
	}

	public void deleteSelected() {
		TreePath treePath = tree.getSelectionPath();
        if (treePath == null) {
            return;
        }
        Object node = treePath.getLastPathComponent();
        if (node instanceof Submenu) {
        	Submenu submenu = (Submenu)node;
        	Submenu parent = (Submenu) submenu.getParent();
        	parent.getChildren().remove(submenu);
        } else if (node instanceof MenuItem) {
        	Submenu parent = (Submenu) ((MenuItem) node).getParent();
        	parent.getChildren().remove(node);
        }
    	tree.updateUI();
	}

	public void addSeparator() {
		TreePath treePath = tree.getSelectionPath();
        if (treePath == null) {
            return;
        }
        Object node = treePath.getLastPathComponent();
        MenuItem miSeparator = new MenuItem();
		miSeparator.setMenuName("Separator");
        if (node instanceof Submenu){
			miSeparator.setParent((Menu) node);
			((Submenu) node).getChildren().add(miSeparator);
        } else if( node instanceof MenuWorkspace) {
        	MenuWorkspace menuWorkspace = (MenuWorkspace) node;
        	miSeparator.setParent(menuWorkspace.getRoot());
        	menuWorkspace.getRoot().getChildren().add(miSeparator);
        }
        tree.updateUI();
	}

}
