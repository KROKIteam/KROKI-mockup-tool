package graphedit.actions.help;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import graphedit.app.MainFrame;
import graphedit.util.ResourceLoader;

public class ContentsAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public ContentsAction() {
		putValue(NAME, "Contents");
		putValue(MNEMONIC_KEY, KeyEvent.VK_C);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("contents.png"));
		putValue(SHORT_DESCRIPTION, "Open help contents...");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JOptionPane.showMessageDialog(MainFrame.getInstance(), "Option '" + getValue(NAME) + "' shall be implemented until dd.MM.YYY.");
	}

}
