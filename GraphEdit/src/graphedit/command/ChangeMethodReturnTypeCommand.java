package graphedit.command;

import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.Method;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.view.GraphEditView;

public class ChangeMethodReturnTypeCommand extends Command {

	private GraphElement element;
	private Method method;
	private String oldName, newName;
	
	public ChangeMethodReturnTypeCommand(GraphEditView view, GraphElement element, Method method, String newName) {
		this.view = view;
		this.method = method;
		this.oldName = method.getName();
		this.newName = newName;
		
		if (element instanceof Shortcut)
			this.element = ((Shortcut) element).shortcutTo();
		else
			this.element = element;
	}

	@Override
	public void execute() {
		method.setReturnType(newName);
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
		}
		view.getModel().fireUpdates();
	}

	@Override
	public void undo() {
		method.setReturnType(oldName);
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
		}
		view.getModel().fireUpdates();
	}

}
