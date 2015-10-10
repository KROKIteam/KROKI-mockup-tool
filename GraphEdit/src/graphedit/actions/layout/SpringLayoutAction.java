package graphedit.actions.layout;

import graphedit.layout.LayoutStrategy;

public class SpringLayoutAction extends LayoutAction {

	private static final long serialVersionUID = 1L;

	public SpringLayoutAction() {
		putValue(NAME, "Spring algorithm");
		putValue(SHORT_DESCRIPTION, "Activate spring layouting...");
		strategy = LayoutStrategy.SPRING;
	}
	
}