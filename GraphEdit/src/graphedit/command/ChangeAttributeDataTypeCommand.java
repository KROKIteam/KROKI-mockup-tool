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

public class ChangeAttributeDataTypeCommand extends Command {

	private GraphElement element;
	private Attribute attribute;
	private String oldName, newName;
	private ClassElement classElement;
	private UmlProperty oldProperty;
	private int classIndex, groupIndex;
	private String oldType;
	
	public ChangeAttributeDataTypeCommand(GraphEditView view, GraphElement element, Attribute attribute, String newName) {
		this.view = view;
		this.attribute = attribute;
		this.oldName = attribute.getDataType();
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
			oldType = attribute.getType();
		}
	}

	@Override
	public void execute() {
	
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
			if (classElement instanceof UIClassElement)
				((UIClassElement) classElement).changeAttributeDataType(attribute, newName, classIndex, groupIndex);
		}
		attribute.setDataType(newName);
		view.getModel().fireUpdates();
		
	}

	@Override
	public void undo() {
		attribute.setDataType(oldName);
		if (element instanceof Class) {
			updatePainters((LinkableElement) element);
			if (classElement instanceof UIClassElement)
				classElement.setOldProperty(attribute, oldProperty, classIndex, groupIndex);
				attribute.setType(oldType);
		}
		view.getModel().fireUpdates();
	}

}
