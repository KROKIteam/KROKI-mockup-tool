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
public class OpenBgColorPopup extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
    public OpenBgColorPopup() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.openColorFill.smallImage"));
        putValue(SMALL_ICON, smallIcon);
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.openColorFill.description"));
    }

    public void actionPerformed(ActionEvent e) {
        JPopupMenu p = new JPopupMenu();
        p.setBackground(Color.WHITE);
        p.add(new ColorPalette(ColorPalette.BG_COLOR), JPopupMenu.CENTER_ALIGNMENT);
        p.pack();
        p.show((Component) e.getSource(), 0, ((Component) e.getSource()).getWidth());

    }
}
