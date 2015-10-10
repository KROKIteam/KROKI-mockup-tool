package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.StringResource;

/**
 * Terminates the application
 * @author Kroki Team
 */
public class ExitAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;

	public ExitAction() {
        putValue(NAME, StringResource.getStringResource("action.exit.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.exit.description"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
    }

    public void actionPerformed(ActionEvent e) {
    	
    	if (KrokiMockupToolApp.getInstance().getWorkspace().getPackageCount() == 0)
    		System.exit(1);
    	
    	int answer = JOptionPane.showConfirmDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(),
    			"Save changes before closing the application?", "?", JOptionPane.YES_NO_CANCEL_OPTION);
    	if (answer == JOptionPane.NO_OPTION)
    		System.exit(1);
    	else if (answer == JOptionPane.YES_OPTION){
    		KrokiMockupToolApp.getInstance().getWorkspace().saveAllProjects();
    		System.exit(1);
    	}
    	else {} //canceled
    }
}
