package graphedit.actions.edit;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import graphedit.app.MainFrame;
import graphedit.command.Command;
import graphedit.command.RemoveLinkNodeCommand;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.properties.Preferences;
import graphedit.strategy.AsIsStrategy;
import graphedit.strategy.LinkStrategy;
import graphedit.strategy.RightAngledStrategy;
import graphedit.view.GraphEditView;

public class RemoveLinkNodeAction extends AbstractAction{

	private static final long serialVersionUID = 1L;


	public RemoveLinkNodeAction(){
		putValue(NAME, "Remove link node");
		putValue(SHORT_DESCRIPTION, "Remove selected link node...");
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		GraphEditView view = MainFrame.getInstance().getCurrentView(); 
		LinkNode node = view.getSelectionModel().getSelectedNode();
		Link link = view.getSelectionModel().getSelectedLink();
		if (node != null && link != null){
			Preferences prefs = Preferences.getInstance();
			LinkStrategy strategy;
			if (prefs.getProperty(Preferences.RIGHT_ANGLE).equalsIgnoreCase("true")) 
				strategy = new RightAngledStrategy();
			else 
				strategy = new AsIsStrategy();

			Command command = new RemoveLinkNodeCommand(node, link, view, strategy);
			MainFrame.getInstance().getCommandManager().executeCommand(command);
		}
	}

}
