package graphedit.command;

import graphedit.model.components.GraphElement;
import graphedit.model.components.Method;
import graphedit.view.GraphEditView;

public class ChangeMethodOwnerCommand extends Command{

	private Command addMethodCommand, removeMethodCommand;

	public ChangeMethodOwnerCommand(GraphEditView view, GraphElement elementOld, 
			GraphElement elementNew, Method method) {

		super();
		removeMethodCommand = new RemoveMethodCommand(view, elementOld, method);
		addMethodCommand = new AddMethodCommand(view, elementNew, method);
	}

	@Override
	public void execute() {
		addMethodCommand.execute();
		removeMethodCommand.execute();
	}

	@Override
	public void undo() {
		addMethodCommand.undo();
		removeMethodCommand.undo();
	}
}

