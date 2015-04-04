package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import kroki.app.KrokiMockupToolApp;
import kroki.app.model.Workspace;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;

/**
 * Saves all opened projects
 * @author Kroki Team
 */
public class SaveAllAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public SaveAllAction() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.saveAll.smallIcon"));
        ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.saveAll.largeIcon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(LARGE_ICON_KEY, largeIcon);
        putValue(NAME, StringResource.getStringResource("action.saveAll.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.saveAll.description"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
        //setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
    	Workspace workspace = KrokiMockupToolApp.getInstance().getWorkspace();
    	workspace.saveAllProjects();
    }
}
