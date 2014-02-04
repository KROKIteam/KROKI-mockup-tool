package graphedit.command;

import graphedit.model.components.GraphElement;
import graphedit.model.diagram.GraphEditModel;
import graphedit.view.GraphEditView;

import java.util.List;

@SuppressWarnings("unused")
public class RotateElementsCommand extends Command {
	
	private List<GraphElement> elements;
	private double angle;

	private GraphEditModel model;

	public void execute() {
	}

	public void undo() {
	}

	public RotateElementsCommand(GraphEditView view, List<GraphElement> elements, double angle) {
	}

	public GraphEditModel getModel() {
		return model;
	}

	public void setModel(GraphEditModel newGraphEditModel) {
		this.model = newGraphEditModel;
	}

}