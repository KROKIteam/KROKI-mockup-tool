package graphedit.model.components;
import graphedit.model.components.shortcuts.Shortcut;
import graphedit.model.properties.Properties;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;

import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public abstract class LinkableElement extends GraphElement {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected ArrayList<Connector> connectors;


	protected Properties<GraphElementProperties> properties;
	
	private Point2D conPosition, shapePosition;
	private Dimension2D shapeDimension;
	
	private List<Shortcut> shortcuts;


	public LinkableElement() {
		super();
		connectors = new ArrayList<Connector>();
		properties = new Properties<GraphElementProperties>();
		shortcuts = new ArrayList<Shortcut>();
	}
	
	/**
	 * Determines weather an instance of LinkableElement can be source element, if link is of type linkType
	 * @param linkType
	 * @return
	 */
	public abstract boolean linkingCanStart(Link.LinkType linkType);
	public abstract boolean linkingCanEnd (Link.LinkType linkType, LinkableElement sourceElement);

	public Object getProperty(GraphElementProperties key) {
		return properties.get(key);
	}

	@Override
	public Set<Entry<GraphElementProperties, Object>> getEntrySet() {
		return properties.getEntrySet();
	}

	public Object setProperty(GraphElementProperties key, Object value) {
		Object result = properties.set(key, value);
		// uradi nesto
		return result;
	}


	public List<Connector> getConnectors() {
		if (connectors == null)
			connectors = new ArrayList<Connector>();
		return connectors;
	}

	public Iterator<Connector> getIteratorconnectors() {
		if (connectors == null)
			connectors = new ArrayList<Connector>();
		return connectors.iterator();
	}

	public void setConnectors(List<Connector> newconnectors) {
		removeAllconnectors();
		for (Iterator<Connector> iter = newconnectors.iterator(); iter
				.hasNext();)
			addConnectors(iter.next());
	}

	public void addConnectors(Connector newConnector) {
		if (connectors != null)
			connectors.add(newConnector);
	}

	public void removeConnectors(Connector oldConnector) {
		if (oldConnector == null)
			return;
		if (this.connectors != null)
			if (this.connectors.contains(oldConnector))
				this.connectors.remove(oldConnector);
	}

	public void removeAllconnectors() {
		if (connectors != null)
			connectors.clear();
	}

	/*
	 * Robert
	 * 
	 * metoda koja pomera konektore da se manje patimo u ostalim metodama
	 */

	public void moveAllConnectors(double deltaX, double deltaY) {
		for (Connector conn : connectors) {
			conPosition = (Point2D) conn.getProperty(LinkNodeProperties.POSITION);
			conPosition.setLocation(conPosition.getX() + deltaX, conPosition.getY()
					+ deltaY);
		}
	}
	
	public void scaleAllConnectors(double scaleX,double scaleY,Shape newShape){
		shapePosition = newShape.getBounds().getLocation();
		shapeDimension = newShape.getBounds().getSize();		
		for(Connector conn : connectors){
			conPosition = (Point2D) conn.getProperty(LinkNodeProperties.POSITION);
			conPosition.setLocation(shapePosition.getX()+shapeDimension.getWidth()*(conn.getPercentX()+0.5),
					shapePosition.getY()+shapeDimension.getHeight()*(conn.getPercentY()+0.5));
			conn.scaleRelativePositions(scaleX,scaleY);
		}
	}
	public void changeRelativePositions(double scaleX,double scaleY){
		for(Connector conn : connectors){
			conn.scaleRelativePositions(scaleX,scaleY);
		}
	}
	
	@Override
	public String toString() {
		return (String) properties.get(GraphElementProperties.NAME);
	}

	public List<Shortcut> getShortcuts() {
		return shortcuts;
	}

	public void setShortcuts(List<Shortcut> shortcuts) {
		this.shortcuts = shortcuts;
	}
	
	public void addShortcut(Shortcut shortcut){
		shortcuts.add(shortcut);
	}
	
	public void removeShortcut(Shortcut shortcut){
		shortcuts.remove(shortcut);
	}


}
