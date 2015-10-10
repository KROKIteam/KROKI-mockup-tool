package graphedit.command;

import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.Parameter;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.view.GraphEditView;

public class ChangeParameterCommand extends Command {

	private GraphElement element;
	private Parameter parameter;
	private String name;
	private String type;
	private boolean finalParameter;
	private String nameOld;
	private String typeOld;
	private boolean finalParameterOld;

	public ChangeParameterCommand(GraphEditView view, GraphElement element, Parameter parameter, String name, String type, boolean finalParameter) {
		this.view = view;
		this.parameter = parameter;
		this.name = name;
		this.type = type;
		this.finalParameter = finalParameter;
		
		if (element instanceof Shortcut)
			this.element = ((Shortcut) element).shortcutTo();
		else
			this.element = element;
		
		nameOld = parameter.getName();
		typeOld = parameter.getType();
		finalParameterOld = parameter.isFinalParameter();
	}

	@Override
	public void execute() {
		parameter.setName(name);
		parameter.setFinalParameter(finalParameter);
		parameter.setType(type);
		updatePainters((LinkableElement) element);
		view.getModel().fireUpdates();
	}

	@Override
	public void undo() {
		parameter.setName(nameOld);
		parameter.setFinalParameter(finalParameterOld);
		parameter.setType(typeOld);
		updatePainters((LinkableElement) element);
		view.getModel().fireUpdates();
	}

}
