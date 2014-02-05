package graphedit.command;

import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.Method;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.elements.ClassElement;
import graphedit.model.elements.UIClassElement;
import graphedit.view.GraphEditView;
import kroki.uml_core_basic.UmlOperation;

public class ChangeMethodStereotypeCommand extends Command {

	private GraphElement element;
	private Method method;
	private String oldStereotype, newStereotype;
	private ClassElement classElement;
	private int classIndex, groupIndex;
	private UmlOperation operation;
	
	public ChangeMethodStereotypeCommand (GraphEditView view, GraphElement element, Method method, String newStereotype) {
		this.view = view;
		this.method = method;
		this.oldStereotype = method.getStereotype();
		
		if (element instanceof Shortcut)
			this.element = ((Shortcut) element).shortcutTo();
		else
			this.element = element;
		
		classElement = (ClassElement) this.element.getRepresentedElement();
		operation = method.getUmlOperation();
		this.newStereotype = newStereotype;
		if (classElement instanceof UIClassElement){
			classIndex = ((UIClassElement)classElement).getClassIndexForMethod(method);
			groupIndex = ((UIClassElement)classElement).getGroupIndexForMethod(method);
		}
	}

	@Override
	public void execute() {
		method.setStereotype(newStereotype);
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
			if (classElement instanceof UIClassElement)
				classElement.changeOperationStereotype(method, newStereotype, classIndex, groupIndex);
		}
		view.getModel().fireUpdates();
	}

	@Override
	public void undo() {
		method.setStereotype(oldStereotype);
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
			
			if (classElement instanceof UIClassElement)
				classElement.setOldOperation(method, operation, classIndex, groupIndex);
		}
		view.getModel().fireUpdates();
	}

}
