package graphedit.model.components;

import graphedit.model.properties.Properties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.model.properties.PropertyEnums.LinkProperties;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map.Entry;

public class Link extends GraphElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Connector sourceConnector;
	protected Connector destinationConnector;
	protected ArrayList<LinkNode> nodes; 
	public enum LinkType {ASSOCIATION,GENERALIZATION, DEPENDENCY, INNERLINK,REALIZATION,REQUIRE};
	protected LinkType linkType;
	protected Point2D movedNodePosition;
	protected int movedNodeIndex;
	protected String[] stereotypes;

	
	
	protected Properties<LinkProperties> properties;
	
	public Link(ArrayList<LinkNode> list) {
		super();
		this.sourceConnector = (Connector) list.get(0);
		this.destinationConnector = (Connector) list.get(list.size()-1);
		this.nodes=list;
		properties = new Properties<LinkProperties>();
		movedNodePosition=new Point2D.Double();
		movedNodeIndex=-1;
	}

	public Object getProperty(LinkProperties key) {
		return properties.get(key);
	}
	
	@Override
	public Set<Entry<LinkProperties, Object>> getEntrySet() {
		return properties.getEntrySet();
	}
	
	public Object setProperty(LinkProperties key, Object value) {
		Object result = properties.set(key, value);
		// uradi nesto
		return result;
	}
	
	public void setSourceConnector(Connector sourceConnector) {
		this.sourceConnector = sourceConnector;
	}
	public Connector getSourceConnector() {
		return sourceConnector;
	}

	public Connector getDestinationConnector() {
		return destinationConnector;
	}
	
	public void setDestinationConnector(Connector destinationConnector) {
		this.destinationConnector = destinationConnector;
	}
	
	@Override
	public String toString() {
		return (String) properties.get(LinkProperties.NAME);
	}


	public ArrayList<LinkNode> getNodes() {
		return nodes;
	}


	public void setNodes(ArrayList<LinkNode> nodes) {
		this.nodes = nodes;
	}

	public LinkType getLinkType() {
		return linkType;
	}

	public Point2D getMovedNodePosition() {
		return movedNodePosition;
	}

	public void setMovedNodePosition(Point2D movedNodePosition) {
		this.movedNodePosition = movedNodePosition;
	}

	public int getMovedNodeIndex() {
		return movedNodeIndex;
	}


	public void moveNode(int deltaX, int deltaY) {
		movedNodePosition.setLocation(movedNodePosition.getX()+deltaX, movedNodePosition.getY()+deltaY);
	}
	
	/**
	 * This method shifts all of the non-connector link nodes
	 * @param deltaX 
	 * @param deltaY
	 */
	public void moveNodes(int deltaX, int deltaY) {
		Point2D position; 
		for (LinkNode node : nodes) {
			position = (Point2D) node.getProperty(LinkNodeProperties.POSITION);
			if (!(node instanceof Connector)) {
				node.setProperty(LinkNodeProperties.POSITION, new Point((int)position.getX() + deltaX, (int)position.getY() + deltaY));
			}
		}
	}
	
	public void setMoveNodePosition(Point2D position){
		movedNodePosition.setLocation(position);
	}
	public void setMovedNodeIndex(int movedNodeIndex) {
		this.movedNodeIndex = movedNodeIndex;
	}

	public String getStereotype() {
		return (String)properties.get(LinkProperties.STEREOTYPE);
	}
	
	public void setNodesWithPositions(ArrayList<Point2D>positions){
		nodes.clear();
		nodes.add(sourceConnector);
		for (int i=1;i<positions.size()-1;i++)
			nodes.add(new LinkNode(positions.get(i)));
		nodes.add(destinationConnector);
		
	}
	
}