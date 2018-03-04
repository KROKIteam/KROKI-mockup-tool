package graphedit.model.components;

import java.util.ArrayList;

import graphedit.model.properties.PropertyEnums.LinkProperties;

public class CompositionLink extends AssociationLink{

	public static final String NAME = "CompositionLink";
	private static final long serialVersionUID = 1L;

	public CompositionLink(ArrayList<LinkNode> nodes,String sourceCardinality, String destinationCardinality, String sourceRole,
			String destinationRole,String name,boolean sourceNavigable, boolean destinationNavigable, 
			boolean showSourceRole, boolean showDestinationRole, int counter) {
		super(nodes,sourceCardinality,destinationCardinality,sourceRole,destinationRole,
				name,sourceNavigable,destinationNavigable, showSourceRole, showDestinationRole, counter);
		linkType = LinkType.ASSOCIATION;
		associationType = AssociationType.COMPOSITION;
		if (name.equals(""))
			properties.set(LinkProperties.NAME, NAME + getCurrentCount());
	}

}
