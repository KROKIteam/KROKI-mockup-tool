package gui.menudesigner.tree;

import gui.menudesigner.model.Submenu;

import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class MenusHierarchyModel implements TreeModel {

	MenusWorkspace root;
	private JTree tree;
	
	public MenusHierarchyModel(JTree tree, MenusWorkspace root) {
		this.tree = tree;
		this.root = root;
	}
	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public Object getChild(Object parent, int index) {
		if (parent instanceof MenusWorkspace) {
			return root.getMenuAt(index);
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof MenusWorkspace) {
			return root.getMenuCount();
		}
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		return node instanceof Submenu;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		//?
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent instanceof MenusWorkspace) {
			return root.getIndexOf((Submenu) child);
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
