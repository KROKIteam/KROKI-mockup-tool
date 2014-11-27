package gui.menudesigner.tree;

import gui.menudesigner.model.Menu;
import gui.menudesigner.model.MenuItem;
import gui.menudesigner.model.Submenu;

import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class MenuHierarchyModel implements TreeModel {

	private MenuWorkspace root;
	private JTree tree;
	
	public MenuHierarchyModel(JTree tree, MenuWorkspace menuWorkspace) {
		this.tree = tree;
		this.root = menuWorkspace;
	}
	
	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public Object getChild(Object parent, int index) {
		if (parent instanceof MenuWorkspace) {
			return root.getRoot().getChildren().get(index);
		} else if (parent instanceof Submenu) {
			return ((Submenu) parent).getChildren().get(index);
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof MenuWorkspace) {
			return root.getRoot().getChildren().size();
		} else if (parent instanceof Submenu) {
			return ((Submenu) parent).getChildren().size();
		}
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		return node instanceof MenuItem;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		//?
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent instanceof MenusWorkspace) {
			return root.getRoot().getChildren().indexOf((Menu) child);
		}else if (parent instanceof Submenu) {
			return ((Submenu) parent).getChildren().indexOf(child);
		}
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
	}

}
