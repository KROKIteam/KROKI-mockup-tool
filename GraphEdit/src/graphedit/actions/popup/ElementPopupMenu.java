package graphedit.actions.popup;

import graphedit.actions.file.DeleteAction;
import graphedit.actions.view.FindInProjectAction;

import javax.swing.JPopupMenu;

public class ElementPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	
	public ElementPopupMenu() {
		add(new DeleteAction());
		add(new FindInProjectAction());
	}
}
