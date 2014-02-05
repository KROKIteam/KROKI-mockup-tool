package graphedit.actions.edit;

import graphedit.app.MainFrame;
import graphedit.util.ResourceLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class UndoAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public UndoAction() {
		putValue(NAME, "Undo");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("undo.png"));
		putValue(SHORT_DESCRIPTION, "Undo action...");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		MainFrame.getInstance().getCommandManager().undo();
	}
}
