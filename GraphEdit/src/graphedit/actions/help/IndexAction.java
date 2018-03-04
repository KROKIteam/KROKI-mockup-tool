package graphedit.actions.help;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import graphedit.app.MainFrame;
import graphedit.util.ResourceLoader;

public class IndexAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public IndexAction() {
		putValue(NAME, "Index");
		putValue(MNEMONIC_KEY, KeyEvent.VK_I);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("help.png"));
		putValue(SHORT_DESCRIPTION, "Open help index...");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JOptionPane.showMessageDialog(MainFrame.getInstance(), "Option '" + getValue(NAME) + "' shall be implemented until dd.MM.YYY.");
	}

}
