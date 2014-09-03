package graphedit.actions.layout;

import graphedit.layout.LayoutStrategy;

public class BoxLayoutAction extends LayoutAction {

	private static final long serialVersionUID = 1L;

	public BoxLayoutAction() {
		putValue(NAME, "Box algorithm");
		putValue(SHORT_DESCRIPTION, "Activate box layouting...");
		strategy = LayoutStrategy.BOX;
	}
	
}