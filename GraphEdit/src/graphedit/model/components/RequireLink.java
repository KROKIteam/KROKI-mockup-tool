package graphedit.model.components;

import graphedit.model.properties.PropertyEnums.LinkProperties;

import java.util.ArrayList;

public class RequireLink  extends Link{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String NAME = "RequireLink";
	
	public RequireLink(ArrayList<LinkNode> nodes, int counter) {
		super(nodes);
		linkType=LinkType.REQUIRE;
		properties.set(LinkProperties.NAME, NAME + counter);
	}

}
