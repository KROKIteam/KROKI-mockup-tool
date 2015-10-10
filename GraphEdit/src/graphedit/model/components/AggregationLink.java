package graphedit.model.components;

import graphedit.model.properties.PropertyEnums.LinkProperties;

import java.util.ArrayList;

public class AggregationLink extends AssociationLink{

	public static final String NAME = "AggregationLink";
	private static final long serialVersionUID = 1L;

	public AggregationLink(ArrayList<LinkNode> list,String sourceCardinality, String destinationCardinality, String sourceRole,
			String destinationRole,String name,boolean sourceNavigable, boolean destinationNavigable,
			boolean showSourceRole, boolean showDestinationRole, int counter) {
		super(list,sourceCardinality,destinationCardinality,sourceRole,destinationRole,name,
				sourceNavigable,destinationNavigable, showSourceRole, showDestinationRole, counter);
		linkType = LinkType.ASSOCIATION;
		associationType = AssociationType.AGGREGATION;
		if (name.equals(""))
			properties.set(LinkProperties.NAME, NAME + getCurrentCount());
	}

}
