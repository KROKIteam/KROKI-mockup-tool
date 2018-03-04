package graphedit.actions.popup;

import javax.swing.JPopupMenu;

import graphedit.actions.file.NewProjectAction;

public class WorkspacePopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	
	public WorkspacePopupMenu() {
		add(new NewProjectAction());
	}
	
}
