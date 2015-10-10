package graphedit.actions.layout;

import graphedit.layout.LayoutStrategy;

public class CircleLayoutAction extends LayoutAction {

	private static final long serialVersionUID = 1L;

	public CircleLayoutAction() {
		putValue(NAME, "Circle algorithm");
		putValue(SHORT_DESCRIPTION, "Activate circle layouting...");
		strategy = LayoutStrategy.CIRCLE;
	}
	
}