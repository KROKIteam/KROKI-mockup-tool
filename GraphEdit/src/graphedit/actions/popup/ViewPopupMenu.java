package graphedit.actions.popup;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import graphedit.actions.edit.CopyAction;
import graphedit.actions.edit.CutAction;
import graphedit.actions.edit.PasteAction;
import graphedit.actions.edit.PrepareShortcutAction;
import graphedit.actions.edit.RedoAction;
import graphedit.actions.edit.ShortcutAction;
import graphedit.actions.edit.UndoAction;
import graphedit.actions.view.ShowGridAction;

public class ViewPopupMenu extends JPopupMenu {
	
	private static final long serialVersionUID = 1L;
	public static final String POPUP_ACTION_COMMAND = "popupActionCommand";
	private JCheckBoxMenuItem showGridCheckBox;
	private JMenuItem pasteItem;
	
	public ViewPopupMenu(CutAction cutDiagramAction, CopyAction copyDiagramAction, PasteAction pasteDiagramAction, PrepareShortcutAction prepareShortcutsAction, 
			ShortcutAction shortcutAction, ShowGridAction showGridAction, UndoAction undoAction, RedoAction redoAction) {
		add(cutDiagramAction);
		add(copyDiagramAction);
		pasteItem = new JMenuItem(pasteDiagramAction);
		pasteItem.setActionCommand(POPUP_ACTION_COMMAND);
		add(pasteItem);
		add(prepareShortcutsAction);
		add(shortcutAction);
		addSeparator();
		add(undoAction);
		add(redoAction);
		addSeparator();
		showGridCheckBox = new JCheckBoxMenuItem(showGridAction);
		showGridCheckBox.setSelected(true);
		add(showGridCheckBox);
	}
	
	public JCheckBoxMenuItem getShowGridCheckBox() {
		return showGridCheckBox;
	}
	
}
