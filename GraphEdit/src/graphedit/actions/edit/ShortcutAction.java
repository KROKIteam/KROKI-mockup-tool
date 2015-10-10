package graphedit.actions.edit;

import graphedit.app.MainFrame;
import graphedit.command.Command;
import graphedit.command.CreateShortcutCommand;
import graphedit.model.ShortcutsManager;
import graphedit.util.ResourceLoader;
import graphedit.view.ElementPainter;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class ShortcutAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	
	
	public ShortcutAction() {
		putValue(NAME, "Create Shortcut");
		putValue(MNEMONIC_KEY, KeyEvent.VK_T);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("shortcut2.png"));;
		putValue(SHORT_DESCRIPTION, "Create shortcut here...");
		setEnabled(false);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		List<ElementPainter> shortcutPainters = ShortcutsManager.getInsance().createShortcutsAndPainters(5,5);
		Command command = new CreateShortcutCommand(MainFrame.getInstance().getCurrentView(), shortcutPainters);
		MainFrame.getInstance().getCommandManager().executeCommand(command);
		setEnabled(false);
		
	}
	
}
