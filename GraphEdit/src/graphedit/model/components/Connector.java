package graphedit.model.components;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import graphedit.app.MainFrame;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;

public class Connector extends LinkNode {


	/**
	 * This makes the association between <code>Connector</code> and <code>Link</code> classes bidirectional
	 */
	private Link link;

	private static final long serialVersionUID = 1L;
	
	private double minXRelative, maxXRelative, minYRelative, maxYRelative;
	private double percentX, percentY;
	private Point2D loadedPosition;

	public Connector(Point2D position, LinkableElement element) {
		super(position);
		calculateRelativePositions(element);
	}
	
	public void calculateRelativePositions(LinkableElement element){
		Point2D position = (Point2D)this.getProperty(LinkNodeProperties.POSITION);
		Point2D elementPosition = (Point2D)element.getProperty(GraphElementProperties.POSITION);
		Dimension size = (Dimension)element.getProperty(GraphElementProperties.SIZE);
		if (size==null){//za test
			size=new Dimension();
			size.setSize(100, 200);
		}
		percentX = (position.getX()-elementPosition.getX())/size.getWidth();
		percentY = (position.getY()-elementPosition.getY())/size.getHeight();		
		minXRelative=-elementPosition.getX() + size.getWidth()/2 + position.getX();
		maxXRelative=elementPosition.getX() + size.getWidth()/2 - position.getX();
		minYRelative=-elementPosition.getY() + size.getHeight()/2 + position.getY();
		maxYRelative=elementPosition.getY() + size.getHeight()/2 - position.getY();
	}
	
	public void setRelativePositions(Point2D position){
		GraphElement element=MainFrame.getInstance().getCurrentView().getModel().getFromElementByConnectorStructure(this);
		Dimension size = (Dimension)element.getProperty(GraphElementProperties.SIZE);
		Point2D elementPosition = (Point2D)element.getProperty(GraphElementProperties.POSITION);
		minXRelative=-elementPosition.getX() + size.getWidth()/2 + position.getX();
		maxXRelative=elementPosition.getX() + size.getWidth()/2 - position.getX();
		minYRelative=-elementPosition.getY() + size.getHeight()/2 + position.getY();
		maxYRelative=elementPosition.getY() + size.getHeight()/2 - position.getY();

	}
	
	public void setPercents(Point2D position){
		GraphElement element=MainFrame.getInstance().getCurrentView().getModel().getFromElementByConnectorStructure(this);
		Dimension size = (Dimension)element.getProperty(GraphElementProperties.SIZE);
		Point2D elementPosition = (Point2D)element.getProperty(GraphElementProperties.POSITION);
		percentX = (position.getX()-elementPosition.getX())/size.getWidth();
		percentY = (position.getY()-elementPosition.getY())/size.getHeight();	
	}
	
	public double getPercentX(){
		return percentX;
	}
	public void setPercentX(double newPercent){
		if(newPercent > 1){
			percentX = 1;
		}else if(newPercent<0){
			percentX = 0;
		}else{
			percentX = newPercent;
		}
	}
	
	public double getPercentY(){
		return percentY;
	}
	public void setPercentY(double newPercent){
		if(newPercent > 1){
			percentY = 1;
		}else if(newPercent<0){
			percentY = 0;
		}else{
			percentY = newPercent;
		}
	}
	

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public double getMinXRelative() {
		return minXRelative;
	}

	public void setMinXRelative(double minXRelative) {
		this.minXRelative = minXRelative;
	}

	public double getMaxXRelative() {
		return maxXRelative;
	}

	public void setMaxXRelative(double maxXRelative) {
		this.maxXRelative = maxXRelative;
	}

	public double getMinYRelative() {
		return minYRelative;
	}

	public void setMinYRelative(double minYRelative) {
		this.minYRelative = minYRelative;
	}

	public double getMaxYRelative() {
		return maxYRelative;
	}

	public void setMaxYRelative(double maxYRelative) {
		this.maxYRelative = maxYRelative;
	}

	public void scaleRelativePositions(double scaleX, double scaleY) {
		maxXRelative*=scaleX;
		minXRelative*=scaleX;
		maxYRelative*=scaleY;
		minYRelative*=scaleY;	
	}

	public Point2D getLoadedPosition() {
		return loadedPosition;
	}

	public void setLoadedPosition(Point2D loadedPosition) {
		this.loadedPosition = loadedPosition;
	}

	
	
}
