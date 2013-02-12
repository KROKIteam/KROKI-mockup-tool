package kroki.app.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;

/**
 * Action that runs selected project as web application
 * @author Milorad Filipovic
 */
public class RunWebAction extends AbstractAction {

	public RunWebAction() {
		putValue(NAME, "Run web version");
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.runweb.smallicon"));
        ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.runweb.largeicon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(LARGE_ICON_KEY, largeIcon);
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.runweb.description"));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
	}

}
