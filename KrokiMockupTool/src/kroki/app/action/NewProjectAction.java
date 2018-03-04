package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.dialog.NewProjectDialog;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;

/**
 * Shows dialog for inputting data regarding a new project
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */

public class NewProjectAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public NewProjectAction() {
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.newProject.smallIcon"));
		ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.newProject.largeIcon"));
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(NAME, StringResource.getStringResource("action.newProject.name"));
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.newProject.description"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
	}

	public void actionPerformed(ActionEvent e) {
		NewProjectDialog dialog = new NewProjectDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		if (dialog.getNewSubystem() != null) {
			KrokiMockupToolApp.getInstance().getWorkspace().addPackage(dialog.getNewSubystem());
			KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
		}
	}
}
