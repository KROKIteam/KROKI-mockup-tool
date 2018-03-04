package graphedit.gui.tree;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import graphedit.model.GraphEditWorkspace;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.interfaces.GraphEditTreeNode;

public class WorkspaceModel implements TreeModel {

	GraphEditWorkspace root;

	public WorkspaceModel(GraphEditWorkspace root) {
		this.root = root;
	}
	
	@Override
	public Object getChild(Object parent, int index) {
		if (parent instanceof GraphElement || parent instanceof Link) {
			return null;
		}
		
		return ((GraphEditTreeNode)parent).getNodeAt(index); 
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof GraphElement || parent instanceof Link) {
			return 0;
		}
		
		return ((GraphEditTreeNode)parent).getNodeCount();
	}

	@Override
	public boolean isLeaf(Object node) {
		return (node instanceof GraphElement || node instanceof Link);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent instanceof GraphElement || parent instanceof Link) {
			return -1;
		}
		
		return ((GraphEditTreeNode)parent).getNodeIndex(child);
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		
	}
	
	public void setRoot(GraphEditWorkspace root) {
		this.root = root;
	}
}
