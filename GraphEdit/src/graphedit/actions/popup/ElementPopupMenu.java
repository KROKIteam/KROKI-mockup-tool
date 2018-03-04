package graphedit.actions.popup;

import javax.swing.JPopupMenu;

import graphedit.actions.file.DeleteAction;
import graphedit.actions.view.FindInProjectAction;

public class ElementPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	
	public ElementPopupMenu() {
		add(new DeleteAction());
		add(new FindInProjectAction());
	}
}
