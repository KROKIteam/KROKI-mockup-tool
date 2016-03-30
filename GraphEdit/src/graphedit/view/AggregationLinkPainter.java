package graphedit.view;

import graphedit.model.components.Link;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.util.Calculate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class AggregationLinkPainter extends AssociationLinkPainter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double length=30;
	private double angle=0.4;
	private GeneralPath pathShape;
	private Point2D lastSymbolPoint;

	public AggregationLinkPainter(Link link) {
		super(link);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.black);
		g.draw(path);
		g.setColor(Color.white);
		g.fill(shape);
		g.setColor(Color.black);
		g.draw(shape);
		paintCardinality(g);
		paintStereotype(g);
	}

	@Override
	public void setShape() {
		if (link==null){
			path=new GeneralPath();
			pathShape=new GeneralPath();
			link=(Link)element;
			lastSymbolPoint=new Point2D.Double();
		}
		else{
			path.reset();
			firstSourceElementPoint=null;
			secondSourceElementPoint=null;
			firstDestinationElementPoint=null;
			secondDestinationElementPoint=null;
			sourcePosition=null;
			destinationPosition=null;
			pathShape.reset();

		}

		Point2D[] points;
		Point2D intersectionPoint=Calculate.intersection(Calculate.SOURCE,link);
		double x3;
		double y3;
		if (intersectionPoint!=null){
			points=Calculate.getPoints(Calculate.SOURCE, link, angle, length);
			pathShape.moveTo(intersectionPoint.getX(), intersectionPoint.getY());
			pathShape.lineTo(points[0].getX(),points[0].getY());
			x3=(points[0].getX()+points[1].getX())/2;
			y3=(points[0].getY()+points[1].getY())/2;
			pathShape.lineTo(2*x3-intersectionPoint.getX(), 2*y3-intersectionPoint.getY());
			pathShape.lineTo(points[1].getX(),points[1].getY());
			pathShape.lineTo(intersectionPoint.getX(), intersectionPoint.getY());
			shape=pathShape;
			lastSymbolPoint.setLocation(2*x3-intersectionPoint.getX(), 2*y3-intersectionPoint.getY());
			path.moveTo(2*x3-intersectionPoint.getX(), 2*y3-intersectionPoint.getY());
			sourceFirstPoint=lastSymbolPoint;
			sourceSecondPoint=(Point2D) link.getNodes().get(1).getProperty(LinkNodeProperties.POSITION);
			drawSourceCardinality=true;
		}
		else{
			
			path.moveTo(((Point2D)link.getSourceConnector().getProperty(LinkNodeProperties.POSITION)).getX(),
					((Point2D)link.getSourceConnector().getProperty(LinkNodeProperties.POSITION)).getY());
			Point2D[] sourcePoints=Calculate.firstIntersectionSegment(Calculate.SOURCE, link);
			shape =new GeneralPath();
			if (sourcePoints!=null){
				sourceFirstPoint=sourcePoints[0];
				sourceSecondPoint=sourcePoints[1];
				firstSourceElementPoint=sourcePoints[2];
				secondSourceElementPoint=sourcePoints[3];
				drawSourceCardinality=true;
			}
			else
				drawSourceCardinality=false;

		}
		int end;
		if (!(Boolean) link.getProperty(LinkProperties.DESTINATION_NAVIGABLE)|| (Boolean)link.getProperty(LinkProperties.SOURCE_NAVIGABLE))
			end=link.getNodes().size();
		else 
			end =link.getNodes().size()-1;
		for (int i=1;i<end;i++){
			path.lineTo(((Point2D)link.getNodes().get(i).getProperty(LinkNodeProperties.POSITION)).getX(),
					((Point2D)link.getNodes().get(i).getProperty(LinkNodeProperties.POSITION)).getY());

		}

		//poslednji segment, sa strelicom
		intersectionPoint=Calculate.intersection(Calculate.DESTINATION,link);
		if (intersectionPoint!=null && (Boolean)link.getProperty(LinkProperties.DESTINATION_NAVIGABLE) && !(Boolean)link.getProperty(LinkProperties.SOURCE_NAVIGABLE) ){
			path.lineTo(intersectionPoint.getX(),intersectionPoint.getY()); //poslednja linija		
			points=Calculate.getPoints(Calculate.DESTINATION,link,arrowAngle,arrowLength);
			path.lineTo(points[0].getX(),points[0].getY());
			path.moveTo(points[1].getX(),points[1].getY());
			path.lineTo(intersectionPoint.getX(), intersectionPoint.getY());

			Point2D middle=new Point2D.Double((points[0].getX()+points[1].getX())/2,(points[0].getY()+points[1].getY())/2);
			destinationFirstPoint=middle;
			destinationSecondPoint=(Point2D)link.getNodes().get(link.getNodes().size()-2).getProperty(LinkNodeProperties.POSITION);
			drawDestinationCardinality=true;

		}
		else{  
			Point2D[] destinationPoints=Calculate.firstIntersectionSegment(Calculate.DESTINATION, link);
			if (destinationPoints!=null){
				destinationFirstPoint=destinationPoints[0];
				destinationSecondPoint=destinationPoints[1];
				firstDestinationElementPoint=destinationPoints[2];
				secondDestinationElementPoint=destinationPoints[3];
				drawDestinationCardinality=true;
			}
			else
				drawDestinationCardinality=false;
		}
		sourcePosition=sourceFirstPoint;
		destinationPosition=destinationFirstPoint;
		
	}	
}

