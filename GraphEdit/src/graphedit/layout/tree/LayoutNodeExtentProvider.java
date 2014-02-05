package graphedit.layout.tree;

import org.abego.treelayout.NodeExtentProvider;


public class LayoutNodeExtentProvider  implements NodeExtentProvider<LayoutTreeNode> {

	
	@Override
	public double getWidth(LayoutTreeNode treeNode) {
		return treeNode.getWidth();
	}

	@Override
	public double getHeight(LayoutTreeNode treeNode) {
		return treeNode.getHeight();
	}

}
