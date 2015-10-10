package graphedit.model.components;

import graphedit.model.properties.PropertyEnums.LinkProperties;

import java.util.ArrayList;

public class DependencyLink extends Link{

	public static final String NAME = "DependencyLink";
	private static final long serialVersionUID = 1L;
	public final static String[] dependencyStereotypes={"", "access","bind","call","derive","ejb-ref","extend","friend","import","include","instantiate","merge","refine","same file","trace","use"};
	public DependencyLink(ArrayList<LinkNode> nodes, int counter) {
		super(nodes);
		linkType=LinkType.DEPENDENCY;
		properties.set(LinkProperties.NAME, NAME + counter);
		stereotypes=dependencyStereotypes;
		properties.set(LinkProperties.STEREOTYPE,stereotypes[1]);
		properties.set(LinkProperties.SOURCE_NAVIGABLE, false);
		properties.set(LinkProperties.DESTINATION_NAVIGABLE, true);
	}

}
