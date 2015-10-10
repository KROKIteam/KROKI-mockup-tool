package graphedit.command;

import graphedit.model.components.Attribute;
import graphedit.model.components.GraphElement;
import graphedit.view.GraphEditView;

public class ChangeAttributeOwnerCommand extends Command {

	private Command addAttributeCommand, removeAttributeCommand;
	
	public ChangeAttributeOwnerCommand(GraphEditView view, GraphElement elementOld, 
			GraphElement elementNew, Attribute attribute) {
		super();
		removeAttributeCommand = new RemoveAttributeCommand(view, elementOld, attribute);
		addAttributeCommand = new AddAttributeCommand(view, elementNew, attribute);
	}

	@Override
	public void execute() {
		addAttributeCommand.execute();
		removeAttributeCommand.execute();
	}

	@Override
	public void undo() {
		addAttributeCommand.undo();
		removeAttributeCommand.undo();
	}
}
