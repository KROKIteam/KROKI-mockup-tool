package graphedit.actions.edit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import graphedit.app.MainFrame;
import graphedit.util.ResourceLoader;

public class RedoAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public RedoAction() {
		putValue(NAME, "Redo");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("redo.png"));
		putValue(SHORT_DESCRIPTION, "Redo action...");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		MainFrame.getInstance().getCommandManager().redo();
	}
}
