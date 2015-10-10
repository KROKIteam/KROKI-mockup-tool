package graphedit.actions.layout;

import graphedit.layout.LayoutStrategy;


public class FRLayoutAction extends LayoutAction {

	private static final long serialVersionUID = 1L;

	public FRLayoutAction() {
		putValue(NAME, "Fruchterman-Reingold algorithm");
		putValue(SHORT_DESCRIPTION, "Activate Fruchterman-Reingold layouting...");
		strategy = LayoutStrategy.FRGRAPH;
	}
	
}
