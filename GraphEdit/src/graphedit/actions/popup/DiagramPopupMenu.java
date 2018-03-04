package graphedit.actions.popup;

import javax.swing.JPopupMenu;

import graphedit.actions.file.DeleteAction;

public class DiagramPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	
	public DiagramPopupMenu() {
		add(new DeleteAction());
	}
}
