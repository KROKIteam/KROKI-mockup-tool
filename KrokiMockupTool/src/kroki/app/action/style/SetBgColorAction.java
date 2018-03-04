package kroki.app.action.style;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import kroki.app.KrokiMockupToolApp;
import kroki.app.command.ChangeColorCommand;
import kroki.app.command.CommandManager;
import kroki.app.model.SelectionModel;
import kroki.app.view.Canvas;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class SetBgColorAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
    private Color color;

    public void actionPerformed(ActionEvent e) {
        Canvas c = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent();
        if (c == null) {
            return;
        }
        CommandManager commandManager = c.getCommandManager();
        SelectionModel selectionModel = c.getSelectionModel();
        ChangeColorCommand changeColorCommand = new ChangeColorCommand(selectionModel.getVisibleElementList(), 1, color);
        commandManager.addCommand(changeColorCommand);
        c.repaint();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
