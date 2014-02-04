package graphedit.actions.edit;

import graphedit.actions.popup.ViewPopupMenu;
import graphedit.app.MainFrame;
import graphedit.command.Command;
import graphedit.command.PasteElementsCommand;
import graphedit.model.ClipboardContents;
import graphedit.util.ResourceLoader;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;
import graphedit.view.LinkPainter;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

@SuppressWarnings({"unchecked"})
public class PasteAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private Point location;
	
	private boolean allowedMultiplePaste = true;
	
	public PasteAction() {
		putValue(NAME, "Paste Element");
		putValue(MNEMONIC_KEY, KeyEvent.VK_P);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("paste.png"));
		putValue(SHORT_DESCRIPTION, "Paste element from clipboard...");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		List<ElementPainter> elementPainters = (List<ElementPainter>) MainFrame.getInstance().getClipboardManager().getElement(ClipboardContents.clipboardElementPaintersFlavor);
		List<LinkPainter> linkPainters = (List<LinkPainter>) MainFrame.getInstance().getClipboardManager().getElement(ClipboardContents.clipboardLinkPaintersFlavor);
		
		GraphEditView view = MainFrame.getInstance().getCurrentView();
		Command command = new PasteElementsCommand(view, linkPainters, elementPainters);
		if (event.getActionCommand().equals(ViewPopupMenu.POPUP_ACTION_COMMAND)) {
			location = MainFrame.getInstance().getMousePosition();
			command = new PasteElementsCommand(view, linkPainters, elementPainters, location);
		} else {
			command = new PasteElementsCommand(view, linkPainters, elementPainters);
		}
		MainFrame.getInstance().getCommandManager().executeCommand(command);
		
		// disable/enable Paste action
		MainFrame.getInstance().getPasteDiagramAction().setEnabled(MainFrame.getInstance().getPasteDiagramAction().isAllowedMultiplePaste());
	}
	
	public void setAllowedMultiplePaste(boolean allowedMultiplePaste) {
		this.allowedMultiplePaste = allowedMultiplePaste;
	}
	
	public boolean isAllowedMultiplePaste() {
		return allowedMultiplePaste;
	}

}
