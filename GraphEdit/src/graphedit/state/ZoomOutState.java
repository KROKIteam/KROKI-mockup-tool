package graphedit.state;

import graphedit.view.GraphEditView;
import graphedit.view.GraphEditView.GraphEditController;

public class ZoomOutState extends State {
	
	public ZoomOutState(GraphEditView view, GraphEditController controller) {
		super(controller);
	}

	public ZoomOutState() { }


	@Override
	public boolean isAutoScrollOnDragEnabled() {
		return false;
	}

	@Override
	public boolean isAutoScrollOnMoveEnabled() {
		return false;
	}
	
}