package graphedit.actions.edit;

import graphedit.app.MainFrame;
import graphedit.command.Command;
import graphedit.command.PasteElementsCommand;
import graphedit.model.ClipboardContents;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.util.ResourceLoader;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;
import graphedit.view.LinkPainter;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;

public class CopyHereAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	

	public CopyHereAction(){

		putValue(NAME, "Copy here");
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("copy.png"));
		putValue(SHORT_DESCRIPTION, "Copy selected element(s) here...");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent arg0) {
		MainFrame.getInstance().getClipboardManager().copySelectedElements();
		List<ElementPainter> elementPainters = (List<ElementPainter>) MainFrame.getInstance().getClipboardManager().getElement(ClipboardContents.clipboardElementPaintersFlavor);
		List<LinkPainter> linkPainters = (List<LinkPainter>) MainFrame.getInstance().getClipboardManager().getElement(ClipboardContents.clipboardLinkPaintersFlavor);
		GraphEditView view = MainFrame.getInstance().getCurrentView();
		Point elementPosition = (Point) MainFrame.getInstance().getCurrentView().getCurrentState().getController().getMousePressedElement().getProperty(GraphElementProperties.POSITION);
		Point location = MainFrame.getInstance().getCurrentView().getCurrentState().getController().getMouseReleased();
		Double xDiff = - elementPosition.getX() + location.getX();
		Double yDiff = - elementPosition.getY() + location.getY();
		Command command = new PasteElementsCommand(view, linkPainters, elementPainters, xDiff, yDiff);
		MainFrame.getInstance().getCommandManager().executeCommand(command);
		
	}
}
