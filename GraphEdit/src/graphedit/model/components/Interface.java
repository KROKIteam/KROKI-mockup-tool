package graphedit.model.components;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import graphedit.model.components.Link.LinkType;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.properties.ApplicationModeProperties;

public class Interface extends Class {

	private static final long serialVersionUID = 1L;
	private ApplicationModeProperties appModeProperties;
	
	
	public Interface(Point2D position, int counter) {
		super();
		appModeProperties = ApplicationModeProperties.getInstance();
		
		properties.set(GraphElementProperties.POSITION, position);
		properties.set(GraphElementProperties.NAME, (String) appModeProperties.getPropertyValue("interfaceName") + counter);
		properties.set(GraphElementProperties.ATTRIBUTES, new ArrayList<Attribute>());
		properties.set(GraphElementProperties.METHODS, new ArrayList<Method>());
		properties.set(GraphElementProperties.STEREOTYPE, (String)appModeProperties.getPropertyValue("interfaceStereotype"));
	}
	

	@Override
	public boolean linkingCanStart(LinkType linkType) {
		if (linkType==Link.LinkType.REALIZATION ||linkType==Link.LinkType.REQUIRE)
			return false;
		return true;
	}

	@Override
	public boolean linkingCanEnd(LinkType linkType, LinkableElement sourceElement) {
		if (linkType==Link.LinkType.GENERALIZATION && !(sourceElement instanceof Interface))
			return false;
		return true;
	}
	
}
