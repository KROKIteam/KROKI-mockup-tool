package graphedit.model.components;

import graphedit.model.properties.PropertyEnums.LinkProperties;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class InnerLink extends Link{
	public static final String NAME = "InnerLink";

	public InnerLink(ArrayList<LinkNode> nodes, int counter) {
		super(nodes);
		linkType=LinkType.INNERLINK;
		properties.set(LinkProperties.NAME, NAME + counter);	}

}
