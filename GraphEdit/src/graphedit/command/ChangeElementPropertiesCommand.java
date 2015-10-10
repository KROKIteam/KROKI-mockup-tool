package graphedit.command;

import graphedit.model.components.GraphElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.view.GraphEditView;

import java.util.ArrayList;
import java.util.List;

public class ChangeElementPropertiesCommand extends Command {


	private List<Command> commands = new ArrayList<Command>();

	@Override
	public void execute() {
		//	model.changeAssociationLinkProperties(newSourceCardinality, newDestinationCardinality, newSourceRole, newDestinationRole, newSourceNavigable,newDestinationNavigable, link);
		for (Command command : commands)
			command.execute();
	}

	@Override
	public void undo() {
		//model.changeAssociationLinkProperties(oldSourceCardinality, oldDestinationCardinality, oldSourceRole, oldDestinationRole,oldSourceNavigable, oldDestinationNavigable,link);
		for (Command command : commands)
			command.undo();
	}


	public ChangeElementPropertiesCommand(GraphEditView view,String newName, String newStereotype, GraphElement element){
		String oldName = (String) element.getProperty(GraphElementProperties.NAME);
		String oldStereotype = (String) element.getProperty(GraphElementProperties.STEREOTYPE);
	
		if (!oldName.equals(newName))
				commands.add(new ChangeElementCommand(view, element, newName, GraphElementProperties.NAME));
		if (!oldStereotype.equals(newStereotype))
			commands.add(new ChangeElementCommand(view, element, newStereotype, GraphElementProperties.STEREOTYPE));
	}
	
}
