package graphedit.command;

import java.util.Collections;
import java.util.List;

import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.Method;
import graphedit.model.components.Parameter;
import graphedit.view.GraphEditView;

public class MoveParameterCommand extends Command {

	private Method method;
	public enum DIRECTION {UP, DOWN};
	private DIRECTION direction;
	private int index;
	private GraphElement element;

	public MoveParameterCommand(GraphEditView view,  GraphElement element, Method method,  int row, DIRECTION direction) {
		this.view = view;
		this.direction = direction;
		this.index = row;
		this.element = element;
		this.method = method;
	}

	@Override
	public void execute() {
		List<Parameter> list = method.getParameters();
		if (direction == DIRECTION.UP)
			Collections.swap(list, index, index - 1);


		if (direction == DIRECTION.DOWN)
			Collections.swap(list, index, index + 1);

		updatePainters((LinkableElement) element);

		view.getModel().fireUpdates();
	}

	@Override
	public void undo() {
		List<Parameter> list = method.getParameters();
		if (direction == DIRECTION.UP)
			Collections.swap(list, index - 1, index);


		if (direction == DIRECTION.DOWN)
			Collections.swap(list, index + 1, index);


		updatePainters((LinkableElement) element);

		view.getModel().fireUpdates();
	}
}
