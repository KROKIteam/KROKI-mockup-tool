package graphedit.view;

import graphedit.model.components.LinkNode;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class LinkNodePainter extends ElementPainter
{
	protected LinkNode linkNode;
	protected double dim=6; //odredjuje velicinu konektora

	public LinkNodePainter(LinkNode linkNode){
		super(linkNode);
		this.linkNode=linkNode;
		formShape();
		
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
	
	@Override
	public void formShape() {
		shape = new Rectangle2D.Double(
				((Point2D)linkNode.getProperty(LinkNodeProperties.POSITION)).getX() - dim/2, 
				((Point2D)linkNode.getProperty(LinkNodeProperties.POSITION)).getY() - dim/2, 
				dim, dim);
		
	}

	public LinkNode getLinkNode() {
		return linkNode;
	}
	
}
