package graphedit.command;

import graphedit.model.components.AssociationLink;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.view.GraphEditView;


public class ChangeAssociationPropertiesComman extends Command {
	private String oldSourceCardinality,oldDestinationCardinality,oldSourceRole,oldDestinationRole;
	private String newSourceCardinality,newDestinationCardinality,newSourceRole,newDestinationRole;
	private boolean newSourceNavigable, newDestinationNavigable;
	private boolean oldSourceNavigable, oldDestinationNavigable;
	private GraphEditModel model;
	private AssociationLink link;
	
	@Override
	public void execute() {
		model.changeAssociationLinkProperties(newSourceCardinality, newDestinationCardinality, newSourceRole, newDestinationRole, newSourceNavigable,newDestinationNavigable, link);
	}

	@Override
	public void undo() {
		model.changeAssociationLinkProperties(oldSourceCardinality, oldDestinationCardinality, oldSourceRole, oldDestinationRole,oldSourceNavigable, oldDestinationNavigable,link);
	}


	public ChangeAssociationPropertiesComman(GraphEditView view,String newSourceCardinality,
			String newDestinationCardinality, String newSourceRole,
			String newDestinationRole, boolean newSourceNavigable, boolean newDestinationNavigable,AssociationLink link) {
		this.newSourceCardinality = newSourceCardinality;
		this.newDestinationCardinality = newDestinationCardinality;
		this.newSourceRole = newSourceRole;
		this.newDestinationRole = newDestinationRole;
		this.link = link;
		this.model = view.getModel();
		oldSourceCardinality=(String)link.getProperty(LinkProperties.SOURCE_CARDINALITY);
		oldDestinationCardinality=(String) link.getProperty(LinkProperties.DESTINATION_CARDINALITY);
		oldSourceRole=(String)link.getProperty(LinkProperties.SOURCE_ROLE);
		oldDestinationRole=(String)link.getProperty(LinkProperties.DESTINATION_ROLE);
		this.newSourceNavigable=newSourceNavigable;
		this.newDestinationNavigable=newDestinationNavigable;
		oldSourceNavigable=(Boolean)link.getProperty(LinkProperties.SOURCE_NAVIGABLE);
		oldDestinationNavigable=(Boolean)link.getProperty(LinkProperties.DESTINATION_NAVIGABLE);
		
	}

	
}
