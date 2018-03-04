package graphedit.command;

import java.util.ArrayList;
import java.util.List;

import graphedit.model.components.AssociationLink;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.view.GraphEditView;


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
			String newDestinationRole, boolean newSourceNavigable, boolean newDestinationNavigable,
			boolean newShowSourceRole, boolean newShowDestinationRole, AssociationLink link) {
		String oldSourceCardinality=(String)link.getProperty(LinkProperties.SOURCE_CARDINALITY);
		String oldDestinationCardinality=(String) link.getProperty(LinkProperties.DESTINATION_CARDINALITY);
		String oldSourceRole=(String)link.getProperty(LinkProperties.SOURCE_ROLE);
		String oldDestinationRole=(String)link.getProperty(LinkProperties.DESTINATION_ROLE);
		Boolean oldSourceNavigable=(Boolean)link.getProperty(LinkProperties.SOURCE_NAVIGABLE);
		Boolean oldDestinationNavigable=(Boolean)link.getProperty(LinkProperties.DESTINATION_NAVIGABLE);
		Boolean oldShowSourceRole = (Boolean)link.getProperty(LinkProperties.SHOW_SOURCE_ROLE);
		Boolean oldShowDestinationRole = (Boolean)link.getProperty(LinkProperties.SHOW_DESTINATION_ROLE);

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
		if (newShowSourceRole != oldShowSourceRole)
			commands.add(new ChangeLinkCommand(view, link, newShowSourceRole + "", LinkProperties.SHOW_SOURCE_ROLE));
		if (newShowDestinationRole != oldShowDestinationRole)
			commands.add(new ChangeLinkCommand(view, link, newShowDestinationRole  + "", LinkProperties.SHOW_DESTINATION_ROLE));
	}


}
