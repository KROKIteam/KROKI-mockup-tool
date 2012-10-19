/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.action;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class OpenPaletteAction extends AbstractAction {

    public OpenPaletteAction() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.palette.smallIcon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(NAME, enabled);
        putValue(NAME, StringResource.getStringResource("action.palette.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.palette.description"));

    }

    public void actionPerformed(ActionEvent e) {
        Color newColor = JColorChooser.showDialog((JButton) e.getSource(), "Choose Background Color", Color.RED);
        System.out.println("new color" + newColor);
    }
}
