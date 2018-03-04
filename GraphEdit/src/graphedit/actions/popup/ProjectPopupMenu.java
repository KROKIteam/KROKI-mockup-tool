package graphedit.actions.popup;

import javax.swing.JPopupMenu;

import graphedit.actions.file.DeleteAction;
import graphedit.actions.file.NewPackageAction;

public class ProjectPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	
	
	private NewPackageAction newPackageAction;
	
	private DeleteAction deleteAction;
	
	public ProjectPopupMenu() {
		newPackageAction = new NewPackageAction();
		deleteAction = new DeleteAction();
		add(newPackageAction);
		add(deleteAction);
	}


	public NewPackageAction getNewPackageAction() {
		return newPackageAction;
	}

	public DeleteAction getDeleteAction() {
		return deleteAction;
	}
}
