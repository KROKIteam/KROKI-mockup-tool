package graphedit.command;

import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.Method;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.elements.ClassElement;
import graphedit.model.elements.UIClassElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.view.GraphEditView;

import java.util.List;

import kroki.uml_core_basic.UmlOperation;

public class RemoveMethodCommand extends Command {

	private GraphElement element;
	private Method method;
	private ClassElement classElement;
	private int index;
	private int classIndex, groupIndex;
	private UmlOperation removedOperation;

	public RemoveMethodCommand(GraphEditView view, GraphElement element, Method method) {
		this.view = view;
		this.method = method;
		
		if (element instanceof Shortcut)
			this.element = ((Shortcut) element).shortcutTo();
		else
			this.element = element;
		
		classElement = (ClassElement) this.element.getRepresentedElement();
		
		if (classElement instanceof UIClassElement){
			classIndex = ((UIClassElement)classElement).getClassIndexForMethod(method);
			groupIndex = ((UIClassElement)classElement).getGroupIndexForMethod(method);
		}
		removedOperation = method.getUmlOperation();
		System.out.println(classIndex);
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public void execute() {
		classElement.removeMethod(classIndex);
		List<Method> list = ((List<Method>)element.getProperty(GraphElementProperties.METHODS));
		index = list.indexOf(method);
		list.remove(method);
		updatePainters((LinkableElement) element);
		view.getModel().fireUpdates();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void undo() {
		List<Method> list = ((List<Method>)element.getProperty(GraphElementProperties.METHODS));
		list.add(index, method);
		
		if (classElement instanceof UIClassElement)
			classElement.setOldOperation(method, removedOperation, classIndex, groupIndex);
		
		updatePainters((LinkableElement) element);
		view.getModel().fireUpdates();
	}

}
