package graphedit.view;

import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.util.Calculate;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class LinkPainter  extends ElementPainter implements Serializable {

	private static final long serialVersionUID = 1L;
	protected Font font=new Font(Font.SANS_SERIF,Font.PLAIN,12);
	protected Link link;
	protected GeneralPath path;
	protected GeneralPath lastSegment;
	protected Shape shape;
	protected double arrowAngle=0.3;
	protected double arrowLength=20;
	protected double selectionMaxDistance=8;
	protected Point2D sourcePosition, destinationPosition; 
	private Rectangle2D.Double stereotypeRectangle;


	public LinkPainter(Link link){
		super(link);
		stereotypeRectangle=new Rectangle2D.Double();
	}

	@Override
	public boolean elementAt(Point2D p) {
		Point2D currentPoint=(Point2D)link.getSourceConnector().getProperty(LinkNodeProperties.POSITION);
		Point2D previousPoint;
		Line2D.Double currentSegment=new Line2D.Double();
		for (int i=1;i<link.getNodes().size();i++){
			previousPoint=currentPoint;
			currentPoint=(Point2D)link.getNodes().get(i).getProperty(LinkNodeProperties.POSITION);
			currentSegment.setLine(previousPoint,currentPoint);
			if (currentSegment.ptLineDist(p)<= selectionMaxDistance && inRange(previousPoint,currentPoint,p))
				return true;
		}
		return false;
	}

	public int newLinkNodeIndex(Point2D p){
		Point2D currentPoint=(Point2D)link.getSourceConnector().getProperty(LinkNodeProperties.POSITION);
		Point2D previousPoint;
		Line2D.Double currentSegment=new Line2D.Double();
		for (int i=1;i<link.getNodes().size();i++){
			previousPoint=currentPoint;
			currentPoint=(Point2D)link.getNodes().get(i).getProperty(LinkNodeProperties.POSITION);
			currentSegment.setLine(previousPoint,currentPoint);
			if (currentSegment.ptLineDist(p) <= selectionMaxDistance && inRange(previousPoint,currentPoint,p)){
				return i;
			}
		}
		return -1;
	}
	
	public LinkNode[] selectedSegmentNodes(Point2D p){
		LinkNode[] ret = new LinkNode[2];
		Point2D currentPoint=(Point2D)link.getSourceConnector().getProperty(LinkNodeProperties.POSITION);
		Point2D previousPoint;
		Line2D.Double currentSegment=new Line2D.Double();
		for (int i=1;i<link.getNodes().size();i++){
			previousPoint=currentPoint;
			currentPoint=(Point2D)link.getNodes().get(i).getProperty(LinkNodeProperties.POSITION);
			currentSegment.setLine(previousPoint,currentPoint);
			if (currentSegment.ptLineDist(p) <= selectionMaxDistance && inRange(previousPoint,currentPoint,p)){
				ret[0] = link.getNodes().get(i-1);
				ret[1] = link.getNodes().get(i);
				break;
			}
		}
		return ret;
	}

	/**
	 * @param lineStart - the first vertex
	 * @param lineEnd - the second vertex
	 * @param p 
	 * @return indications whether point p is within selectionMaxDistance from the line
	 * @author tim1
	 */
	public boolean inRange(Point2D lineStart, Point2D lineEnd, Point2D p){
		double xMin,xMax,yMin,yMax;
		if(lineStart.getX()>=lineEnd.getX()){
			xMax=lineStart.getX()+ selectionMaxDistance;
			xMin=lineEnd.getX()- selectionMaxDistance;
		}
		else{
			xMax=lineEnd.getX() +  selectionMaxDistance;
			xMin=lineStart.getX() -  selectionMaxDistance;
		}
		if(lineStart.getY()>=lineEnd.getY()){
			yMax=lineStart.getY() +  selectionMaxDistance;
			yMin=lineEnd.getY() -  selectionMaxDistance;
		}
		else{
			yMax=lineEnd.getY() +  selectionMaxDistance;
			yMin=lineStart.getY() -  selectionMaxDistance;
		}
		if (p.getX()<=xMax && p.getX()>=xMin && p.getY()<=yMax && p.getY()>=yMin)
			return true;
		return false;
	}

	/**
	 * Method is used when stereotype is not null
	 * @param g
	 * @author tim1
	 */
	public void paintStereotype(Graphics2D g){
		if (!link.getStereotype().equals("")) {
			String stereotype = "<<" + link.getStereotype() + ">>";
			g.setFont(font);
			FontMetrics fm=g.getFontMetrics();
			int width=fm.stringWidth(stereotype);
			int height=fm.getHeight();
			Point2D position=Calculate.getStereotypePosition(link, width, sourcePosition, destinationPosition );
			if (position!=null){
				position.setLocation(position.getX(),position.getY()+height/2 - fm.getDescent());		
				stereotypeRectangle.setRect(position.getX(),position.getY()-height/2 ,width,height/2);
				g.setColor(Color.WHITE);
				g.fill(stereotypeRectangle);
				g.draw(stereotypeRectangle);
				g.setColor(Color.BLACK);
				g.drawString(stereotype,(int) position.getX(),(int) position.getY());

			}	
		}
	}
	@Override
	public void setShape() {

	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	@Override
	public void formShape() {

	}

}