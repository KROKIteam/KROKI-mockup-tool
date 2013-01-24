/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import kroki.app.gui.dialog.KrokiMockupToolAboutDialog;
import kroki.app.utils.StringResource;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AboutAction extends AbstractAction {

    public AboutAction() {
        putValue(NAME, StringResource.getStringResource("action.about.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.about.description"));
    }

    public void actionPerformed(ActionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    	KrokiMockupToolAboutDialog about = new KrokiMockupToolAboutDialog();
    	about.setVisible(true);
    }
}
