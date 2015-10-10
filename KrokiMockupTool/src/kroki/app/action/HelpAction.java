package kroki.app.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import kroki.app.gui.dialog.KrokiMockupToolHelpDialog;
import kroki.app.utils.StringResource;

/**
 * Shows help dialog
 * @author Kroki Team
 *
 */
public class HelpAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public HelpAction() {
		putValue(NAME, StringResource.getStringResource("action.help.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.help.description"));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		KrokiMockupToolHelpDialog help = new KrokiMockupToolHelpDialog();
		help.setVisible(true);
	}

}
