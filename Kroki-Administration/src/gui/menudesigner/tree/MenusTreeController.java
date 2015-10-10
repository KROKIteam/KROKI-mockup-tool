package gui.menudesigner.tree;

import gui.menudesigner.MenuDesignerPanel;
import gui.menudesigner.model.Submenu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class MenusTreeController implements TreeModelListener, TreeSelectionListener, MouseListener {
	
	private JTree tree;
	private MenusWorkspace workspace;
	public MenusTreeController(JTree tree, MenusWorkspace workspace) {
		this.tree = tree;
		this.workspace = workspace;
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
                if (node instanceof Submenu) {
                	Submenu submenu = (Submenu) node;
                	MenuDesignerPanel mdp = new MenuDesignerPanel(workspace, submenu, tree);
    				mdp.setVisible(true);	
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

	public void openMenuDesignerPanel() {
		Submenu temp = new Submenu();
		temp.setName("Menu");
		MenuDesignerPanel mdp = new MenuDesignerPanel(workspace, temp, tree);
		mdp.setVisible(true);	
	}

}
