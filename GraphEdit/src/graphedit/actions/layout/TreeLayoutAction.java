package graphedit.actions.layout;

import graphedit.layout.LayoutStrategy;

public class TreeLayoutAction extends LayoutAction {

	private static final long serialVersionUID = 1L;

	public TreeLayoutAction() {
		putValue(NAME, "Tree algorithm");
		putValue(SHORT_DESCRIPTION, "Activate tree layouting...");
		strategy = LayoutStrategy.TREE;
	}
	
}