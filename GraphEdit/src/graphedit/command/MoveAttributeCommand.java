package graphedit.command;

import graphedit.model.components.Attribute;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.elements.ClassElement;
import graphedit.model.elements.UIClassElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.view.GraphEditView;

import java.util.Collections;
import java.util.List;


public class MoveAttributeCommand extends Command {

	private GraphElement element;
	public enum DIRECTION {UP, DOWN};

	private ClassElement classElement;
	private int classIndex, groupIndex;
	private DIRECTION direction;
	private int index;

	public MoveAttributeCommand(GraphEditView view, GraphElement element, Attribute attribute, int row, DIRECTION direction) {
		this.view = view;

		this.element = element;
		this.direction = direction;
		this.index = row;

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
		if (direction == DIRECTION.UP){
			Collections.swap(list, index, index - 1);

			if (classElement instanceof UIClassElement){
				UIClassElement uiClass = (UIClassElement)classElement;
				uiClass.moveElementUp(classIndex, groupIndex, 1);
			}
		}

		if (direction == DIRECTION.DOWN){
			Collections.swap(list, index, index + 1);

			if (classElement instanceof UIClassElement){
				UIClassElement uiClass = (UIClassElement)classElement;
				uiClass.moveElementDown(classIndex, groupIndex, 1);
			}
		}

		updatePainters((LinkableElement) element);

		view.getModel().fireUpdates();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void undo() {
		List<Attribute> list = ((List<Attribute>)element.getProperty(GraphElementProperties.ATTRIBUTES));
		if (direction == DIRECTION.UP){
			Collections.swap(list, index - 1, index);

			if (classElement instanceof UIClassElement){
				UIClassElement uiClass = (UIClassElement)classElement;
				uiClass.moveElementDown(classIndex - 1, groupIndex - 1, 1);
			}
		}

		if (direction == DIRECTION.DOWN){
			Collections.swap(list, index + 1, index);

			if (classElement instanceof UIClassElement){
				UIClassElement uiClass = (UIClassElement)classElement;
				uiClass.moveElementUp(classIndex + 1, groupIndex + 1, 1);
			}
		}

		updatePainters((LinkableElement) element);

		view.getModel().fireUpdates();
	}



}
