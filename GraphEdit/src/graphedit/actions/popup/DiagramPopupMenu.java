package graphedit.actions.popup;

import graphedit.actions.file.DeleteAction;

import javax.swing.JPopupMenu;

public class DiagramPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	
	public DiagramPopupMenu() {
		add(new DeleteAction());
	}
}
