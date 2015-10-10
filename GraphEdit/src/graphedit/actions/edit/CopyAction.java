package graphedit.actions.edit;

import graphedit.app.MainFrame;
import graphedit.util.ResourceLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class CopyAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public CopyAction() {
		putValue(NAME, "Copy Element");
		putValue(MNEMONIC_KEY, KeyEvent.VK_C);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("copy.png"));
		putValue(SHORT_DESCRIPTION, "Copy selected element...");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		MainFrame.getInstance().getClipboardManager().copySelectedElements();
		// enable Paste action
		MainFrame.getInstance().getPasteDiagramAction().setEnabled(true);
		MainFrame.getInstance().getPasteDiagramAction().setAllowedMultiplePaste(true);
		
	}
}
