package graphedit.model.components;

import graphedit.model.properties.PropertyEnums.LinkProperties;

import java.util.ArrayList;

public class GeneralizationLink extends Link{

	public static final String NAME = "GeneralizationLink";
	private static final long serialVersionUID = 1L;
	public final static String[] generalizationStereotypes={"", "access","implementation","import","merge"};
	public GeneralizationLink(ArrayList<LinkNode> nodes, int counter) {
		super(nodes);
		linkType=LinkType.GENERALIZATION;
		properties.set(LinkProperties.NAME, NAME + counter);
		stereotypes=new String[4];
		stereotypes[0]="access";
		stereotypes[1]="implementation";
		stereotypes[2]="import";
		stereotypes[3]="merge";
		stereotypes=generalizationStereotypes;
		properties.set(LinkProperties.STEREOTYPE,stereotypes[0]);
	}

}
