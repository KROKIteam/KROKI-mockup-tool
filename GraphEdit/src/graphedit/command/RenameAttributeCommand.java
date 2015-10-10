package graphedit.command;

import graphedit.model.components.Attribute;
import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.elements.ClassElement;
import graphedit.view.GraphEditView;

public class RenameAttributeCommand extends Command {

	private GraphElement element;
	private Attribute attribute;
	private String oldName, newName;
	private ClassElement classElement;
	
	public RenameAttributeCommand(GraphEditView view, GraphElement element, Attribute attribute, String newName) {
		this.view = view;
		this.attribute = attribute;
		this.oldName = attribute.getName();
		this.newName = newName;
		
		if (element instanceof Shortcut)
			this.element = ((Shortcut) element).shortcutTo();
		else
			this.element = element;
		
		classElement = (ClassElement) this.element.getRepresentedElement();
	}

	@Override
	public void execute() {
		attribute.setName(newName);
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
		}
		classElement.renameAttribute(attribute, newName);
		view.getModel().fireUpdates();
	}

	@Override
	public void undo() {
		attribute.setName(oldName);
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
		}
		classElement.renameAttribute(attribute, newName);
		view.getModel().fireUpdates();
	}

}
