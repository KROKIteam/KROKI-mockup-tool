package graphedit.command;

import graphedit.model.components.Attribute;
import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.elements.ClassElement;
import graphedit.model.elements.UIClassElement;
import graphedit.view.GraphEditView;
import kroki.uml_core_basic.UmlProperty;

public class ChangeAttributeTypeCommand extends Command {

	private GraphElement element;
	private Attribute attribute;
	private String oldName, newName;
	private ClassElement classElement;
	private UmlProperty oldProperty;
	private int classIndex, groupIndex;
	private String oldDataType;
	
	public ChangeAttributeTypeCommand(GraphEditView view, GraphElement element, Attribute attribute, String newName) {
		this.view = view;
		this.attribute = attribute;
		this.oldName = attribute.getType();
		this.newName = newName;
		
		if (element instanceof Shortcut)
			this.element = ((Shortcut) element).shortcutTo();
		else
			this.element = element;
		
		
		classElement = (ClassElement) this.element.getRepresentedElement();
		oldProperty = attribute.getUmlProperty();
		
		if (classElement instanceof UIClassElement){
			classIndex = ((UIClassElement)classElement).getClassIndexForAttribute(attribute);
			groupIndex = ((UIClassElement)classElement).getGroupIndexForAttribute(attribute);
			oldDataType = attribute.getDataType();
		}
	}

	@Override
	public void execute() {
		attribute.setType(newName);
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
			if (classElement instanceof UIClassElement)
				classElement.changeAttributeType(attribute, newName, classIndex, groupIndex);
		}
		view.getModel().fireUpdates();
		
	}

	@Override
	public void undo() {
		attribute.setType(oldName);
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
			if (classElement instanceof UIClassElement)
				classElement.setOldProperty(attribute, oldProperty, classIndex, groupIndex);
				attribute.setDataType(oldDataType);
		}
		view.getModel().fireUpdates();
	}

}
