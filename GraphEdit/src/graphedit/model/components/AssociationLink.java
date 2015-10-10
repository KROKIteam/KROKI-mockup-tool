	package graphedit.model.components;

import graphedit.model.properties.PropertyEnums.LinkProperties;

import java.util.ArrayList;


public class AssociationLink extends Link{
	
	private static final long serialVersionUID = 1L;
	public static final String NAME = "AssociationLink";
	public enum AssociationType {REGULAR,AGGREGATION,COMPOSITION};
	protected AssociationType associationType;
	protected int currentCount;
	public static final String[] associationStereotypes={"", "implicit"};

	
	public AssociationLink(ArrayList<LinkNode> list ,String sourceCardinality, String destinationCardinality, String sourceRole,
			String destinationRole, String name, boolean sourceNavigable, boolean destinationNavigable,
			boolean showSourceRole, boolean showDestinationRole, int counter) {
		super(list);
		linkType=LinkType.ASSOCIATION;
		associationType = AssociationType.REGULAR;
		properties.set(LinkProperties.SOURCE_CARDINALITY, sourceCardinality);
		properties.set(LinkProperties.DESTINATION_CARDINALITY,destinationCardinality);
		properties.set(LinkProperties.SOURCE_ROLE, sourceRole);
		properties.set(LinkProperties.DESTINATION_ROLE, destinationRole);
		properties.set(LinkProperties.SOURCE_NAVIGABLE, sourceNavigable);
		properties.set(LinkProperties.DESTINATION_NAVIGABLE, destinationNavigable);
		properties.set(LinkProperties.SHOW_SOURCE_ROLE, showSourceRole);
		properties.set(LinkProperties.SHOW_DESTINATION_ROLE, showDestinationRole);
		
		if (name.equals("")) {
			properties.set(LinkProperties.NAME, NAME + counter);
			currentCount = counter;
		} else 
			properties.set(LinkProperties.NAME, name);
		
		
		stereotypes=associationStereotypes;
		properties.set(LinkProperties.STEREOTYPE,stereotypes[0]);
	}

	public Object getProperty(LinkProperties key) {
		return properties.get(key);
	}
	public Object setProperty(LinkProperties key, Object value) {
		 Object result=properties.set(key, value);
		 return result;
	}

	public AssociationType getAssociationType() {
		return associationType;
	}

	public int getCurrentCount() {
		return currentCount;
	}
}
