package graphedit.actions.popup;

import javax.swing.JPopupMenu;

import graphedit.actions.file.DeleteAction;
import graphedit.actions.file.NewPackageAction;
import graphedit.actions.view.FindInProjectAction;

public class PackagePopupMenu extends JPopupMenu {
	
	private static final long serialVersionUID = 1L;
	
	
	private NewPackageAction newPackageAction;
	
	private DeleteAction deleteAction;
	
	public PackagePopupMenu() {
		newPackageAction = new NewPackageAction();
		deleteAction = new DeleteAction();
		add(newPackageAction);
		add(deleteAction);
		add(new FindInProjectAction());
	}


	public NewPackageAction getNewPackageAction() {
		return newPackageAction;
	}

	public DeleteAction getDeleteAction() {
		return deleteAction;
	}
}
