package graphedit.state;

import graphedit.view.GraphEditView;
import graphedit.view.GraphEditView.GraphEditController;

public class ZoomInState extends State {

	public ZoomInState(GraphEditView view, GraphEditController controller) {
		super(controller);
	}

	public ZoomInState() { }

	@Override
	public boolean isAutoScrollOnDragEnabled() {
		return false;
	}

	@Override
	public boolean isAutoScrollOnMoveEnabled() {
		return false;
	}
}