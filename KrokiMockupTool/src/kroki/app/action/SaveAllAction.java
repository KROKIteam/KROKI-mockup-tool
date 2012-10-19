/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class SaveAllAction extends AbstractAction {

    public SaveAllAction() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.saveAll.smallIcon"));
        ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.saveAll.largeIcon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(LARGE_ICON_KEY, largeIcon);
        putValue(NAME, StringResource.getStringResource("action.saveAll.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.saveAll.description"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
