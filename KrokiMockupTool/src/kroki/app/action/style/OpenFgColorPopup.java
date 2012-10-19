/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.action.style;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import kroki.app.gui.ColorPalette;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class OpenFgColorPopup extends AbstractAction {

    public OpenFgColorPopup() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.openColorText.smallImage"));
        putValue(SMALL_ICON, smallIcon);
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.openColorText.description"));
    }

    public void actionPerformed(ActionEvent e) {
        JPopupMenu p = new JPopupMenu();
        p.setBackground(Color.WHITE);
        p.add(new ColorPalette(ColorPalette.FG_COLOR), JPopupMenu.CENTER_ALIGNMENT);
        p.pack();
        p.show((Component) e.getSource(), 0, ((Component) e.getSource()).getWidth());
    }
}
