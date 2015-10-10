package graphedit.actions.edit;

import graphedit.app.MainFrame;
import graphedit.command.AddLinkNodeCommand;
import graphedit.command.Command;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.properties.Preferences;
import graphedit.strategy.AsIsStrategy;
import graphedit.strategy.LinkStrategy;
import graphedit.strategy.RightAngledStrategy;
import graphedit.view.GraphEditView;
import graphedit.view.LinkPainter;

import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;

import javax.swing.AbstractAction;

public class AddLinkNodeAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	Point2D location;

	public AddLinkNodeAction(){
		putValue(NAME, "Add link node");
		putValue(SHORT_DESCRIPTION, "Add new link node...");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GraphEditView view = MainFrame.getInstance().getCurrentView(); 
		Link link = view.getSelectionModel().getSelectedLink();
		LinkPainter painter = view.getLinkPainter(link);
		int index = painter.newLinkNodeIndex(location);
		if (index != -1){
			LinkNode newNode = new LinkNode(location);
			Preferences prefs = Preferences.getInstance();
			LinkStrategy strategy;
			if (prefs.getProperty(Preferences.RIGHT_ANGLE).equalsIgnoreCase("true")) 
				strategy = new RightAngledStrategy();
			else 
				strategy = new AsIsStrategy();
			
			Command command = new AddLinkNodeCommand(newNode, link, index, view, strategy);
			MainFrame.getInstance().getCommandManager().executeCommand(command);
		}

	}

	public void setLocation(Point2D location){
		this.location = location;
	}

}
