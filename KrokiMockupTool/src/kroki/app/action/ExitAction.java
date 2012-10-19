/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import kroki.app.utils.StringResource;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ExitAction extends AbstractAction {

    public ExitAction() {
        putValue(NAME, StringResource.getStringResource("action.exit.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.exit.description"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        //TODO: da li zeli da sacuva nesacuvano...
        //TODO: da li zeli da zatvori aplikaciju
        System.exit(1);
    }
}
