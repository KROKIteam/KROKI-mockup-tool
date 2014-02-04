package graphedit.actions.popup;

import graphedit.actions.file.DeleteAction;
import graphedit.actions.file.NewDiagramAction;
import graphedit.actions.file.NewPackageAction;

import javax.swing.JPopupMenu;

public class ProjectPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	
	private NewDiagramAction newDiagramAction;
	
	private NewPackageAction newPackageAction;
	
	private DeleteAction deleteAction;
	
	public ProjectPopupMenu() {
		newDiagramAction = new NewDiagramAction();
		newPackageAction = new NewPackageAction();
		deleteAction = new DeleteAction();
		add(newDiagramAction);
		add(newPackageAction);
		add(deleteAction);
	}

	public NewDiagramAction getNewDiagramAction() {
		return newDiagramAction;
	}

	public NewPackageAction getNewPackageAction() {
		return newPackageAction;
	}

	public DeleteAction getDeleteAction() {
		return deleteAction;
	}
}
