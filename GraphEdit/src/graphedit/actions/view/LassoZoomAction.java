package graphedit.actions.view;

import graphedit.app.MainFrame;
import graphedit.app.MainFrame.ToolSelected;
import graphedit.state.State;
import graphedit.util.ResourceLoader;
import graphedit.view.GraphEditView;
import graphedit.view.GraphEditView.GraphEditController;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class LassoZoomAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public LassoZoomAction() {
		
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("lasso_zoom.png"));
		putValue(SHORT_DESCRIPTION, "Lasso zoom...");
		putValue(NAME, "Lasso zoom");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		GraphEditView view = MainFrame.getInstance().getCurrentView();
		if (view == null) {
			return;
		}
		//ako se preslo iz povezivanje, mora da se ocisti
		State state=MainFrame.getInstance().getCurrentView().getCurrentState();
		state.clearEverything();
		
		GraphEditController context = view.getCurrentState().getController();
		// po state patternu
		state = MainFrame.getInstance().getCurrentView().getModel().getLassoZoomState();
		state.setController(context);
		state.setView(view);
		state.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		MainFrame.getInstance().setStatusTrack(state.toString());
		context.setCurrentState(state);
		
		// tehnicki detalji
		view.setSelectedTool(ToolSelected.SELECTION);
		view.requestFocusInWindow();
		
	}

}
