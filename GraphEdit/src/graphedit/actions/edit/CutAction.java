package graphedit.actions.edit;

import graphedit.app.MainFrame;
import graphedit.command.Command;
import graphedit.command.CutElementsCommand;
import graphedit.util.ResourceLoader;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class CutAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public CutAction() {
		putValue(NAME, "Cut Element");
		putValue(MNEMONIC_KEY, KeyEvent.VK_U);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("cut.png"));
		putValue(SHORT_DESCRIPTION, "Cut selected element...");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		List<ElementPainter> elementPainters = MainFrame.getInstance().getClipboardManager().cutSelectedElements();
		GraphEditView view = MainFrame.getInstance().getCurrentView();
		Command command = new CutElementsCommand(view, view.getSelectionModel().getSelectedElements(), elementPainters);
		MainFrame.getInstance().getCommandManager().executeCommand(command);
		
		// enable Paste action
		MainFrame.getInstance().getPasteDiagramAction().setEnabled(true);
		MainFrame.getInstance().getPasteDiagramAction().setAllowedMultiplePaste(false);
		
	}

}
