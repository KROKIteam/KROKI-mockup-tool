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
 * Undoes an action
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class UndoAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public UndoAction() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.undo.smallIcon"));
        ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.undo.largeIcon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(LARGE_ICON_KEY, largeIcon);
        putValue(NAME, StringResource.getStringResource("action.undo.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.undo.description"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        Canvas c = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent();
        VisibleClass visibleClass = c.getVisibleClass();
        CommandManager commandManager = c.getCommandManager();
        commandManager.undoCommand();
        visibleClass.update();
        c.repaint();
    }
}
