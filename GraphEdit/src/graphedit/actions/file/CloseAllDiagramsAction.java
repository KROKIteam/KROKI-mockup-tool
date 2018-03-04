package graphedit.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import graphedit.app.MainFrame;
import graphedit.util.ResourceLoader;

public class CloseAllDiagramsAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private Object[] options = { "Yes, please", "No, thanks", "Cancel" };
	
	public CloseAllDiagramsAction() {
		putValue(NAME, "Close All Diagrams");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("close_all.png"));
		putValue(SHORT_DESCRIPTION, "Close all active diagrams...");
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
	
		if (MainFrame.getInstance().isMarkedWithAsterisk()) {
			int n = JOptionPane.showOptionDialog(MainFrame.getInstance(),
					"Save changes?",
					"The changes were made.", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null,
					options,
					options[0]); 
			if (n != JOptionPane.CANCEL_OPTION) {
				if (n == JOptionPane.YES_OPTION) {
					MainFrame.getInstance().closeAllDiagrams(true, false);
				}
				MainFrame.getInstance().closeAllDiagrams(false, true);
			}
		} else {
			MainFrame.getInstance().closeAllDiagrams(false, false);
		}
	}

}
