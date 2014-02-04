package graphedit.command;

import graphedit.model.components.Attribute;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.elements.ClassElement;
import graphedit.model.elements.UIClassElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.view.GraphEditView;

import java.util.List;

import kroki.uml_core_basic.UmlProperty;

public class RemoveAttributeCommand extends Command {

	private GraphElement element;
	private Attribute attribute;
	private int index;
	private ClassElement classElement;
	private int classIndex, groupIndex;		
	private UmlProperty removedProperty; 
	

	public RemoveAttributeCommand(GraphEditView view, GraphElement element, Attribute attribute) {
		this.view = view;
		this.attribute = attribute;
		
		if (element instanceof Shortcut)
			this.element = ((Shortcut) element).shortcutTo();
		else
			this.element = element;
		
		
		classElement = (ClassElement) this.element.getRepresentedElement();
		removedProperty = attribute.getUmlProperty();
		if (classElement instanceof UIClassElement){
			classIndex = ((UIClassElement)classElement).getClassIndexForAttribute(attribute);
			groupIndex = ((UIClassElement)classElement).getGroupIndexForAttribute(attribute);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void execute() {
		classElement.removeAttribute(classIndex);
		List<Attribute> list = ((List<Attribute>)element.getProperty(GraphElementProperties.ATTRIBUTES));
		index = list.indexOf(attribute);
		list.remove(index);
		((List<Attribute>)element.getProperty(GraphElementProperties.ATTRIBUTES)).remove(attribute);
		updatePainters((LinkableElement) element);
		view.getModel().fireUpdates();
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public void undo() {
		List<Attribute> list = ((List<Attribute>)element.getProperty(GraphElementProperties.ATTRIBUTES));
		list.add(index, attribute);
		
		if (classElement instanceof UIClassElement)
			classElement.setOldProperty(attribute, removedProperty, classIndex, groupIndex);
		
		updatePainters((LinkableElement) element);
		view.getModel().fireUpdates();
	}

}
