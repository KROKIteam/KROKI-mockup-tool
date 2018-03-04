package graphedit.actions.edit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import graphedit.app.MainFrame;
import graphedit.model.ShortcutsManager;
import graphedit.util.ResourceLoader;

public class PrepareShortcutAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	public PrepareShortcutAction() {
		putValue(NAME, "Prepare Shortcut(s)");
		putValue(MNEMONIC_KEY, KeyEvent.VK_R);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("shortcut1.png"));
		putValue(SHORT_DESCRIPTION, "Prepare shortcut(s) for selected element(s)...");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ShortcutsManager.getInsance().prepareContents();
		MainFrame.getInstance().getShortcutAction().setEnabled(true);
	}

}
