package graphedit.actions.popup;

import graphedit.actions.file.NewProjectAction;

import javax.swing.JPopupMenu;

public class WorkspacePopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	
	public WorkspacePopupMenu() {
		add(new NewProjectAction());
	}
	
}
