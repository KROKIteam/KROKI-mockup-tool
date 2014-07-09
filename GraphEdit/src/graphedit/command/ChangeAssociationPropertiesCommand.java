package graphedit.command;

import graphedit.model.components.AssociationLink;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.view.GraphEditView;

import java.util.ArrayList;
import java.util.List;


public class ChangeAssociationPropertiesCommand extends Command {


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


	public ChangeAssociationPropertiesCommand(GraphEditView view,String newSourceCardinality,
			String newDestinationCardinality, String newSourceRole,
			String newDestinationRole, boolean newSourceNavigable, boolean newDestinationNavigable,AssociationLink link) {
		String oldSourceCardinality=(String)link.getProperty(LinkProperties.SOURCE_CARDINALITY);
		String oldDestinationCardinality=(String) link.getProperty(LinkProperties.DESTINATION_CARDINALITY);
		String oldSourceRole=(String)link.getProperty(LinkProperties.SOURCE_ROLE);
		String oldDestinationRole=(String)link.getProperty(LinkProperties.DESTINATION_ROLE);
		Boolean oldSourceNavigable=(Boolean)link.getProperty(LinkProperties.SOURCE_NAVIGABLE);
		Boolean oldDestinationNavigable=(Boolean)link.getProperty(LinkProperties.DESTINATION_NAVIGABLE);

		if (!newSourceCardinality.equals(oldSourceCardinality))
			commands.add(new ChangeLinkCommand(view, link, newSourceCardinality, LinkProperties.SOURCE_CARDINALITY));
		if (!newDestinationCardinality.equals(oldDestinationCardinality))
			commands.add(new ChangeLinkCommand(view, link, newDestinationCardinality, LinkProperties.DESTINATION_CARDINALITY));
		if (!newSourceRole.equals(oldSourceRole))
			commands.add(new ChangeLinkCommand(view, link, newSourceRole, LinkProperties.SOURCE_ROLE));
		if (!newDestinationRole.equals(oldDestinationRole))
			commands.add(new ChangeLinkCommand(view, link, newDestinationRole, LinkProperties.DESTINATION_ROLE));
		if (newDestinationNavigable != oldDestinationNavigable)
			commands.add(new ChangeLinkCommand(view, link, newDestinationNavigable + "", LinkProperties.DESTINATION_NAVIGABLE));
		if (newSourceNavigable != oldSourceNavigable)
			commands.add(new ChangeLinkCommand(view, link, newSourceNavigable + "", LinkProperties.SOURCE_NAVIGABLE));
	}


}
