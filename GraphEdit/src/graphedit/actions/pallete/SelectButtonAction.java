package graphedit.actions.pallete;

import graphedit.app.MainFrame;
import graphedit.app.MainFrame.ToolSelected;
import graphedit.state.State;
import graphedit.util.ResourceLoader;
import graphedit.view.GraphEditView;
import graphedit.view.GraphEditView.GraphEditController;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class SelectButtonAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public SelectButtonAction() {
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("cursor.png"));
		putValue(SHORT_DESCRIPTION, "Select element...");
		putValue(NAME, "   Selection");
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
		state = MainFrame.getInstance().getCurrentView().getModel().getSelectionState();
		state.setController(context);
		state.setView(view);
		state.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		MainFrame.getInstance().setStatusTrack(state.toString());
		context.setCurrentState(state);
		
		// tehnicki detalji
		view.setSelectedTool(ToolSelected.SELECTION);
		view.requestFocusInWindow();
		
		view.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
	}

}
