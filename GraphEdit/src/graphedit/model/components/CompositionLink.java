package graphedit.model.components;

import graphedit.model.properties.PropertyEnums.LinkProperties;

import java.util.ArrayList;

public class CompositionLink extends AssociationLink{

	public static final String NAME = "CompositionLink";
	private static final long serialVersionUID = 1L;

	public CompositionLink(ArrayList<LinkNode> nodes,String sourceCardinality, String destinationCardinality, String sourceRole,
			String destinationRole,String name,boolean sourceNavigable, boolean destinationNavigable, int counter) {
		super(nodes,sourceCardinality,destinationCardinality,sourceRole,destinationRole,
				name,sourceNavigable,destinationNavigable, counter);
		linkType = LinkType.ASSOCIATION;
		associationType = AssociationType.COMPOSITION;
		if (name.equals(""))
			properties.set(LinkProperties.NAME, NAME + getCurrentCount());
	}

}
