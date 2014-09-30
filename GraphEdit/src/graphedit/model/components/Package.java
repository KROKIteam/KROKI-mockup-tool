package graphedit.model.components;

import graphedit.model.components.Link.LinkType;
import graphedit.model.elements.GraphEditPackage;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;

import java.awt.geom.Point2D;

/**
 * Package graph element
 * @author xxx
 *
 */
public class Package extends LinkableElement{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "Package";
	

	
	public Package(Point2D position, int counter) {
		properties.set(GraphElementProperties.POSITION, position);
		properties.set(GraphElementProperties.NAME, Package.NAME + counter);
	}

	@Override
	public boolean linkingCanStart(LinkType linkType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean linkingCanEnd(LinkType linkType,
			LinkableElement sourceElement) {
		// TODO Auto-generated method stub
		return false;
	}

	public GraphEditPackage getHierarchyPackage() {
		return (GraphEditPackage) representedElement;
	}

	


}
