package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import kroki.app.KrokiMockupToolApp;
import kroki.app.command.CommandManager;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.app.view.Canvas;
import kroki.profil.panel.VisibleClass;

/**
 * Redo action
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class RedoAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public RedoAction() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.redo.smallIcon"));
        ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.redo.largeIcon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(LARGE_ICON_KEY, largeIcon);
        putValue(NAME, StringResource.getStringResource("action.redo.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.redo.description"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        Canvas c = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent();
        VisibleClass visibleClass = c.getVisibleClass();
        CommandManager commandManager = c.getCommandManager();
        commandManager.doCommand();
        visibleClass.update();
        c.repaint();
    }
}
