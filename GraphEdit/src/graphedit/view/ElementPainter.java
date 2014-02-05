package graphedit.view;

import graphedit.model.components.GraphElement;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

public abstract class ElementPainter implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	protected Shape shape;
	protected GraphElement element;
	protected Color fillColor1 = new Color(180, 255, 255); 
	protected Color fillColor2 = new Color(255, 255, 255);
	protected AffineTransform transform = new AffineTransform();
	private Point2D position;
	
	public boolean elementIn(Shape s) {
		return s.contains(shape.getBounds());
	}

	public boolean elementAt(Point2D p) {
		return shape.contains(p);
	}

	public void paint(Graphics2D g) {
		g.setPaint(Color.white);
		g.fill(shape);
		g.setPaint(Color.black);
		g.draw(shape);
	}

	public ElementPainter(GraphElement element) {
		this.element = element;
		setShape();
	}

	public GraphElement getElement() {
		return element;
	}
	
	public Shape getShape(){
		return this.shape;
	}
	
	public void setShape(Shape shape){
		this.shape = shape;
	}

	public void setElement(GraphElement newGraphElement) {
		this.element = newGraphElement;
		setShape();
	}
	
	public Color getFillColor1() {
		return fillColor1;
	}

	public void setFillColor1(Color fillColor1) {
		this.fillColor1 = fillColor1;
	}

	public Color getFillColor2() {
		return fillColor2;
	}

	public void setFillColor2(Color fillColor2) {
		this.fillColor2 = fillColor2;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void setShape() {}

	public abstract void formShape();
	
	public void resizeShape(double width, double height){
		transform = new AffineTransform();
		//skaliranje
		transform.scale(width, height);
		position = shape.getBounds().getLocation();
		shape = transform.createTransformedShape(shape);
		//ponistavanje smicanja
		moveShape(position.getX()-shape.getBounds().getLocation().getX(),
				position.getY()-shape.getBounds().getLocation().getY());
	}
	
	public void moveShape(double xPos, double yPos){
		transform = new AffineTransform();
		transform.translate(xPos, yPos);
		shape = transform.createTransformedShape(shape);
	}

	/**
	 * Should be redefines for those painters where it makes sense.
	 * @return minimum allowed size of <code>GraphElement</code> instance
	 */
	public Dimension2D getMinimumSize() { return null; }
}