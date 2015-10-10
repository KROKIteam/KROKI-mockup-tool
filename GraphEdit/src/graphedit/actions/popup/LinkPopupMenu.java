package graphedit.actions.popup;

import graphedit.actions.edit.AddLinkNodeAction;
import graphedit.actions.edit.RemoveLinkNodeAction;
import graphedit.app.MainFrame;
import graphedit.view.SelectionModel;

import java.awt.geom.Point2D;

import javax.swing.JPopupMenu;

public class LinkPopupMenu extends JPopupMenu{
	
	private static final long serialVersionUID = 1L;
	
	private AddLinkNodeAction addNodeAction;
	private RemoveLinkNodeAction removeNodeAction;
	
	public LinkPopupMenu(){
		addNodeAction = new AddLinkNodeAction();
		removeNodeAction = new RemoveLinkNodeAction();
		add(addNodeAction);
		add(removeNodeAction);
	}
	
	public void preparePopup(){
		SelectionModel model = MainFrame.getInstance().getCurrentView().getSelectionModel();
			
		if (model.getSelectedNode() != null){
			addNodeAction.setEnabled(false);
			removeNodeAction.setEnabled(true);
		}
		else{
			addNodeAction.setEnabled(true);
			removeNodeAction.setEnabled(false);
		}
			
	}

	public void setActionPoint(Point2D location) {
		addNodeAction.setLocation(location);
		
	}

}
