
package graphedit.view;

import graphedit.model.components.Connector;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class ConnectorPainter {
	protected Shape shape;
	protected Connector connector;
	protected double dim=5; //odredjuje velicinu konektora

	public ConnectorPainter(Connector connector){
		this.connector=connector;
		System.out.println((Point2D)connector.getProperty(LinkNodeProperties.POSITION));
		shape = new Rectangle2D.Double(
				((Point2D)connector.getProperty(LinkNodeProperties.POSITION)).getX() - dim/2, 
				((Point2D)connector.getProperty(LinkNodeProperties.POSITION)).getY() - dim/2, 
				dim, dim);
		
	}
	public boolean elementAt(Point2D p) {
		return shape.contains(p);
	}

	public void paint(Graphics2D g) {
		g.setColor(Color.black);
		g.fill(shape);
		g.draw(shape);
	}
	public double getDim() {
		return dim;
	}
	public void setDim(double dim) {
		this.dim = dim;
	}
	
	
}

