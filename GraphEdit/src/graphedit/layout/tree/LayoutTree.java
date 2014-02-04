package graphedit.layout.tree;

import graphedit.model.components.Package;

import java.util.List;

import org.abego.treelayout.util.DefaultTreeForTreeLayout;

public class LayoutTree extends DefaultTreeForTreeLayout<LayoutTreeNode> implements Comparable<LayoutTree>{

	public LayoutTree(LayoutTreeNode root) {
		super(root);
	}

	public void addNode(LayoutTreeNode node){
		addChild(node.getParentNode(), node);
	}


	@Override
	public List<LayoutTreeNode> getChildrenList(LayoutTreeNode node) {
		return node.getChildren();
	}

	@Override
	public LayoutTreeNode getParent(LayoutTreeNode node) {
		return node.getParentNode();
	}

	public int getNodesNumber(){
		System.out.println("*************");
		return getNodesNumber(getRoot());
	}

	private int getNodesNumber(LayoutTreeNode node){
		int num = node.getChildren().size();
		for (LayoutTreeNode child : node.getChildren()){
			num += getNodesNumber(child);
		}
		return num;
	}
	
	private boolean rootIsPackage(){
		return getRoot().getElement() instanceof Package;
	}


	@Override
	public int compareTo(LayoutTree tree) {
		if (rootIsPackage())
			return -1;
		if (tree.rootIsPackage())
			return 1;
		int numOfElements = getNodesNumber();
		int otherNumberOfElements = tree.getNodesNumber();
		if (numOfElements > otherNumberOfElements)
			return -1;
		else if (numOfElements < otherNumberOfElements)
			return 1;
		return 0;
	}

}
