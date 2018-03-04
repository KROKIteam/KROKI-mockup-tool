package graphedit.actions.edit;

import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.AbstractAction;

import graphedit.app.MainFrame;
import graphedit.command.Command;
import graphedit.command.CreateShortcutCommand;
import graphedit.model.ShortcutsManager;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.util.ResourceLoader;
import graphedit.view.ElementPainter;

public class CreateShortcutHereAction extends AbstractAction{

	private static final long serialVersionUID = 1L;


	public CreateShortcutHereAction() {
		putValue(NAME, "Create Shortcut(s) here");
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("shortcut2.png"));;
		putValue(SHORT_DESCRIPTION, "Create shortcut(s) here...");
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ShortcutsManager.getInsance().prepareContents();
		Point2D elementPosition = (Point2D) MainFrame.getInstance().getCurrentView().getCurrentState().getController().getMousePressedElement().getProperty(GraphElementProperties.POSITION);
		Point2D location = MainFrame.getInstance().getCurrentView().getCurrentState().getController().getMouseReleased();
		Double xDiff = - elementPosition.getX() + location.getX();
		Double yDiff = - elementPosition.getY() + location.getY();
		List<ElementPainter> shortcutPainters = ShortcutsManager.getInsance().createShortcutsAndPainters(xDiff, yDiff);
		Command command = new CreateShortcutCommand(MainFrame.getInstance().getCurrentView(), shortcutPainters);
		MainFrame.getInstance().getCommandManager().executeCommand(command);
		
	}

}
