package graphedit.actions.help;

import graphedit.app.MainFrame;
import graphedit.util.ResourceLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class AboutAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public AboutAction() {
		putValue(NAME, "About");
		putValue(MNEMONIC_KEY, KeyEvent.VK_A);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.ALT_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("about.png"));
		putValue(SHORT_DESCRIPTION, "About application...");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JOptionPane.showMessageDialog(MainFrame.getInstance(), "Graph Edit v1.2\nAuthors:\nIgor Cverdelj-Fogaraši\nRenata Vaderna\nMiloš Jokić\nRobert Molnar\nJan, 2012.\n\nIntegration with Kroki Mockup tool:\nRenata Vaderna", 
				"About", JOptionPane.INFORMATION_MESSAGE);
	}

}
