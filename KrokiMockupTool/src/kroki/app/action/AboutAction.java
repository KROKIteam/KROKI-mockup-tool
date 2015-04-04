package kroki.app.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import kroki.app.gui.dialog.KrokiMockupToolAboutDialog;
import kroki.app.utils.StringResource;

/**
 * Shows about dialog
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AboutAction extends AbstractAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AboutAction() {
        putValue(NAME, StringResource.getStringResource("action.about.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.about.description"));
    }

    public void actionPerformed(ActionEvent e) {
    	KrokiMockupToolAboutDialog about = new KrokiMockupToolAboutDialog();
    	about.setVisible(true);
    }
}
