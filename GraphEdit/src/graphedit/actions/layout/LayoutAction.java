package graphedit.actions.layout;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import graphedit.app.MainFrame;
import graphedit.layout.LayoutStrategy;
import graphedit.view.GraphEditView;

public abstract class LayoutAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	protected LayoutStrategy strategy;

	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		GraphEditView view = MainFrame.getInstance().getCurrentView();
		view.setLayout(true);
		view.getModel().setConnectorsLoaded(false);
		view.getModel().removeLinkNodes();
		view.setLayoutStrategy(strategy);
		view.getSelectionModel().clearSelection();
		view.getModel().fireUpdates();
	}
}