package graphedit.actions.view;

import graphedit.app.MainFrame;
import graphedit.util.ResourceLoader;
import graphedit.view.GraphEditView;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class BestFitZoomAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public BestFitZoomAction() {
		putValue(NAME, "Best Fit Zoom");
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("best_fit.png"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F12, ActionEvent.CTRL_MASK));
		putValue(SHORT_DESCRIPTION, "Best fit zoom...");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {		
		GraphEditView view = MainFrame.getInstance().getCurrentView();
		if (view == null) {
			return;
		}
		
		view.bestFitZoom();
	}

}

