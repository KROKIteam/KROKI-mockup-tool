package graphedit.actions.file;

import graphedit.app.MainFrame;
import graphedit.util.ResourceLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class ExitAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public ExitAction() {
		putValue(NAME, "Exit");
		putValue(MNEMONIC_KEY, KeyEvent.VK_E);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("exit.png"));
		putValue(SHORT_DESCRIPTION, "Exit...");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
			MainFrame.getInstance().setVisible(false);

	}

}
