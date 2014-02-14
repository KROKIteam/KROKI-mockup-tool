package graphedit.model.components;

import graphedit.model.properties.Properties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;

import java.awt.geom.Point2D;

public class LinkNode extends GraphElement{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Properties<LinkNodeProperties> properties;
	
	public LinkNode(Point2D position) {
		super();
		properties = new Properties<LinkNodeProperties>();
		properties.set(LinkNodeProperties.POSITION, position);
	}

	public Object getProperty(LinkNodeProperties key) {
		return properties.get(key);
	}
	
	public void setProperty(LinkNodeProperties key, Object value) {
		properties.set(key, value);
	}
	
	@Override
	public String toString() {
		return  properties.get(LinkNodeProperties.POSITION).toString();
	}

}
