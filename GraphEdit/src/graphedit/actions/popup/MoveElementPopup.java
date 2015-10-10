package graphedit.actions.popup;

import graphedit.actions.edit.CopyHereAction;
import graphedit.actions.edit.CreateShortcutHereAction;
import graphedit.app.MainFrame;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class MoveElementPopup extends JPopupMenu{

	private static final long serialVersionUID = 1L;

	private JMenuItem copyMI, createShortcutMI;

	public MoveElementPopup(CopyHereAction copyAction, CreateShortcutHereAction shortcutAction){
		copyMI = new JMenuItem(copyAction);
		createShortcutMI = new JMenuItem(shortcutAction);
	}

	public void customizeShortcut(){
		removeAll();
		if (!MainFrame.getInstance().getCurrentView().getSelectionModel().hasShortcut())
			add(copyMI);
		if(!MainFrame.getInstance().getCurrentView().getSelectionModel().hasPackage()
				&& !MainFrame.getInstance().getCurrentView().getSelectionModel().hasShortcut())
			add(createShortcutMI);
	}
}
