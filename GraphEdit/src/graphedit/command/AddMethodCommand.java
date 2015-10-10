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

public class AddMethodCommand extends Command {

	private GraphElement element;
	private Method method;
	private int index;
	private ClassElement classElement;
	private int classIndex, groupIndex;

	@SuppressWarnings("unchecked")
	public AddMethodCommand(GraphEditView view, GraphElement element, Method method) {
		this.view = view;
		this.method = method;
		
		
		if (element instanceof Shortcut)
			this.element = ((Shortcut) element).shortcutTo();
		else
			this.element = element;
		
		index = ((List<Method>)element.getProperty(GraphElementProperties.METHODS)).size();
		classElement = (ClassElement) this.element.getRepresentedElement();
		if (classElement instanceof UIClassElement){
			classIndex = ((UIClassElement)classElement).getClassIndexForMethod(method);
			groupIndex = ((UIClassElement)classElement).getGroupIndexForMethod(method);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void execute() {
		List<Method> list = ((List<Method>)element.getProperty(GraphElementProperties.METHODS));
		list.add(index, method);
		
		if (classElement instanceof UIClassElement)
			classElement.addMethod(method, classIndex, groupIndex);
		
		updatePainters((LinkableElement) element);
		
		view.getModel().fireUpdates();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void undo() {
		classElement.removeMethod(classIndex);
		((List<Method>)element.getProperty(GraphElementProperties.METHODS)).remove(method);
		
		updatePainters((LinkableElement) element);
		
		view.getModel().fireUpdates();
	}

}
