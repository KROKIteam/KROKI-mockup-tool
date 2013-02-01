/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import kroki.app.utils.StringResource;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class SaveAsAction extends AbstractAction {

    public SaveAsAction() {
        putValue(NAME, StringResource.getStringResource("action.saveAs.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.saveAs.description"));
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
