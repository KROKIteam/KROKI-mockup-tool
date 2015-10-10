package graphedit.command;

import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.Method;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.elements.ClassElement;
import graphedit.view.GraphEditView;

public class RenameMethodCommand extends Command {

	private GraphElement element;
	private Method method;
	private String oldName, newName;
	private ClassElement classElement;
	
	public RenameMethodCommand(GraphEditView view, GraphElement element, Method method, String newName) {
		this.view = view;
		this.element = element;
		this.method = method;
		this.oldName = method.getName();
		this.newName = newName;
		
		if (element instanceof Shortcut)
			this.element = ((Shortcut) element).shortcutTo();
		else
			this.element = element;
		
		classElement = (ClassElement) this.element.getRepresentedElement();
	}

	@Override
	public void execute() {
		method.setName(newName);
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
		}
		classElement.renameMathod(method, newName);
		view.getModel().fireUpdates();
	}

	@Override
	public void undo() {
		method.setName(oldName);
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
		}
		classElement.renameMathod(method, oldName);
		view.getModel().fireUpdates();
	}

}
