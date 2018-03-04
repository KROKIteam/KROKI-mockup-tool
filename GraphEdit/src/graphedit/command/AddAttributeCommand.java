package graphedit.command;

import java.util.List;

import graphedit.model.components.Attribute;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.elements.ClassElement;
import graphedit.model.elements.UIClassElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.view.GraphEditView;

public class AddAttributeCommand extends Command {

	private GraphElement element;
	private Attribute attribute;
	private int index;
	private ClassElement classElement;
	private int classIndex, groupIndex;

	@SuppressWarnings("unchecked")
	public AddAttributeCommand(GraphEditView view, GraphElement element, Attribute attribute) {
		this.view = view;
		
		this.attribute = attribute;
		index = ((List<Attribute>)element.getProperty(GraphElementProperties.ATTRIBUTES)).size();
		
		if (element instanceof Shortcut)
			this.element = ((Shortcut) element).shortcutTo();
		else
			this.element = element;
		
		
		classElement = (ClassElement) this.element.getRepresentedElement();
		if (classElement instanceof UIClassElement){
			classIndex = ((UIClassElement)classElement).getClassIndexForAttribute(attribute);
			groupIndex = ((UIClassElement)classElement).getGroupIndexForAttribute(attribute);
		}
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public void execute() {
		List<Attribute> list = ((List<Attribute>)element.getProperty(GraphElementProperties.ATTRIBUTES));
		list.add(index, attribute);
		
		if (classElement instanceof UIClassElement)
			classElement.addAttribute(attribute, classIndex, groupIndex);
		
		updatePainters((LinkableElement) element);
		
		view.getModel().fireUpdates();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void undo() {
		classElement.removeAttribute(classIndex);
		((List<Attribute>)element.getProperty(GraphElementProperties.ATTRIBUTES)).remove(attribute);
		
		updatePainters((LinkableElement) element);
		
		view.getModel().fireUpdates();
	}
	


}
