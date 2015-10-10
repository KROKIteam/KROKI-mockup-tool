package graphedit.actions.pallete;

import graphedit.app.MainFrame;
import graphedit.app.MainFrame.ToolSelected;
import graphedit.state.State;
import graphedit.util.ResourceLoader;
import graphedit.view.GraphEditView;
import graphedit.view.GraphEditView.GraphEditController;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class InterfaceButtonAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public InterfaceButtonAction() {
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("interface.png"));
		putValue(SHORT_DESCRIPTION, "Insert interface...");
		putValue(NAME, "Interface");
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
		state = MainFrame.getInstance().getCurrentView().getModel().getAddElementState();
		state.setView(view); 
		state.setController(context);
		MainFrame.getInstance().setStatusTrack(state.toString());
		context.setCurrentState(state);
		
		// tehniciki detalji
		view.setSelectedTool(ToolSelected.INTERFACE_SELECTED);
		view.requestFocusInWindow();
		
		Image image = new ResourceLoader().loadImage("interface.png");
		// Ne znam zasto, ali mi javi exception kad ostavim x + 16, y + 16.
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(view.getX(), view.getY()), "addElementCursor");
		view.setCursor(cursor);
	}

}
