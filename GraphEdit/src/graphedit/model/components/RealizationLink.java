package graphedit.model.components;

import java.util.ArrayList;

import graphedit.model.properties.PropertyEnums.LinkProperties;

public class RealizationLink extends Link{

	private static final long serialVersionUID = 1L;
	public static final String NAME = "RealizationLink";

	public RealizationLink(ArrayList<LinkNode> nodes, int counter) {
		super(nodes);
		linkType=LinkType.REALIZATION;
		properties.set(LinkProperties.NAME, NAME +  counter);
		properties.set(LinkProperties.SOURCE_NAVIGABLE, false);
		properties.set(LinkProperties.DESTINATION_NAVIGABLE, true);
	}

}
