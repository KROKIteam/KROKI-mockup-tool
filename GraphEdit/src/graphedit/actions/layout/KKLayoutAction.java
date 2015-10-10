package graphedit.actions.layout;

import graphedit.layout.LayoutStrategy;

public class KKLayoutAction extends LayoutAction {

	private static final long serialVersionUID = 1L;

	public KKLayoutAction() {
		putValue(NAME, "Kamada-Kawai algorithm");
		putValue(SHORT_DESCRIPTION, "Activate Kamada-Kawai layouting...");
		strategy = LayoutStrategy.KKGRAPH;
	}
	
}