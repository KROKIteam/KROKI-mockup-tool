package graphedit.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import graphedit.model.components.AssociationLink;
import graphedit.model.components.Link;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.util.Calculate;

public class AssociationLinkPainter extends LinkPainter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	protected Font font=new Font(Font.SANS_SERIF,Font.PLAIN,12);
	protected Point2D sourceFirstPoint,sourceSecondPoint, destinationFirstPoint, destinationSecondPoint,
	firstSourceElementPoint,secondSourceElementPoint,firstDestinationElementPoint,secondDestinationElementPoint;
	protected int xDistance=5,yDistance=5;
	protected boolean drawSourceCardinality;
	protected boolean drawDestinationCardinality;
	private Point2D middle;

	public AssociationLinkPainter(Link link) {
		super(link);
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.black);
		g.draw(path);
		g.draw(lastSegment);
		paintCardinality(g);
		paintStereotype(g);
	}

	protected void  paintCardinality(Graphics2D g){
		String sourceCardinality=(String) ((AssociationLink)link).getProperty(LinkProperties.SOURCE_CARDINALITY);
		String destinationCardinality=(String) ((AssociationLink)link).getProperty(LinkProperties.DESTINATION_CARDINALITY);
		String sourceRole=(String) ((AssociationLink)link).getProperty(LinkProperties.SOURCE_ROLE);
		String destinationRole=(String) ((AssociationLink)link).getProperty(LinkProperties.DESTINATION_ROLE);
		boolean showSourceRole = (Boolean) link.getProperty(LinkProperties.SHOW_SOURCE_ROLE);
		boolean showDestinationRole = (Boolean) link.getProperty(LinkProperties.SHOW_DESTINATION_ROLE);
		g.setFont(font);
		FontMetrics fm=g.getFontMetrics();
		int mulFactor;

		int width=0;
		int height=fm.getHeight();
		int sourceRoleWidth = 0;
		int destinationRoleWidth = 0;
		if (sourceRole != null && showSourceRole)
			sourceRoleWidth=fm.stringWidth(sourceRole);
		if (destinationRole != null && showDestinationRole)
			destinationRoleWidth=fm.stringWidth(destinationRole);

		Point2D position;

		if (drawSourceCardinality){
			width=fm.stringWidth(sourceCardinality);
			if (sourceRole == null || sourceRole.equals("") || !showSourceRole)
				mulFactor=0;
			else 
				mulFactor=1;
			position=Calculate.getCardinalityPosition(width, height,Calculate.SOURCE, link, xDistance+sourceRoleWidth/2,yDistance+height*mulFactor, sourceFirstPoint, sourceSecondPoint,
					firstSourceElementPoint,secondSourceElementPoint);
			if (position!=null)				
				g.drawString(sourceCardinality,(int) position.getX(), (int)position.getY());
			if (sourceRole!=null && showSourceRole){
				position=Calculate.getCardinalityPosition(sourceRoleWidth, 2*height,Calculate.SOURCE, link, xDistance,yDistance  , sourceFirstPoint, sourceSecondPoint,
						firstSourceElementPoint,secondSourceElementPoint);
				if (position!=null)
					g.drawString(sourceRole,(int) position.getX(), (int)position.getY());
			}
		}

		if (drawDestinationCardinality){
			width=fm.stringWidth(destinationCardinality);
			if (destinationRole==null || destinationRole.equals("") || !showDestinationRole)
				mulFactor=0;
			else 
				mulFactor=1;
			position=Calculate.getCardinalityPosition(width, height,Calculate.DESTINATION, link, xDistance+ destinationRoleWidth/2,yDistance + height*mulFactor,destinationFirstPoint, destinationSecondPoint,
					firstDestinationElementPoint,secondDestinationElementPoint);
			if (position!=null)
				g.drawString(destinationCardinality,(int) position.getX(), (int)position.getY());
			if (destinationRole!=null && showDestinationRole){
				position=Calculate.getCardinalityPosition(destinationRoleWidth, 2*height,Calculate.DESTINATION, link, xDistance,yDistance , destinationFirstPoint, destinationSecondPoint,
						firstDestinationElementPoint,secondDestinationElementPoint);
				if (position!=null)
					g.drawString(destinationRole,(int) position.getX(), (int)position.getY());
			}

		}	
	}

	@Override
	public void setShape() {
		// TODO Auto-generated method stub		
		if (link==null){
			path=new GeneralPath();
			lastSegment=new GeneralPath();
			link=(Link)element;
			middle=new Point2D.Double();
		}
		else{
			path.reset();
			lastSegment.reset();
			firstSourceElementPoint=null;
			secondSourceElementPoint=null;
			firstDestinationElementPoint=null;
			secondDestinationElementPoint=null;
			sourcePosition=null;
			destinationPosition=null;
		}

		int end;
		if (!(Boolean) link.getProperty(LinkProperties.DESTINATION_NAVIGABLE) || (Boolean)link.getProperty(LinkProperties.SOURCE_NAVIGABLE))
			end=link.getNodes().size();
		else 
			end =link.getNodes().size()-1;
		path.moveTo(((Point2D)link.getSourceConnector().getProperty(LinkNodeProperties.POSITION)).getX(),
				((Point2D)link.getSourceConnector().getProperty(LinkNodeProperties.POSITION)).getY());
		for (int i=1;i<end;i++){
			path.lineTo(((Point2D)link.getNodes().get(i).getProperty(LinkNodeProperties.POSITION)).getX(),
					((Point2D)link.getNodes().get(i).getProperty(LinkNodeProperties.POSITION)).getY());
		}


		//poslednji segment, sa strelicom
		Point2D intersectionPoint=Calculate.intersection(Calculate.DESTINATION,link);
		if (intersectionPoint!=null && (Boolean) link.getProperty(LinkProperties.DESTINATION_NAVIGABLE) && !(Boolean)link.getProperty(LinkProperties.SOURCE_NAVIGABLE)){
			path.lineTo(intersectionPoint.getX(),intersectionPoint.getY()); //poslednja linija

			lastSegment.moveTo(intersectionPoint.getX(),intersectionPoint.getY());
			Point2D[] points=Calculate.getPoints(Calculate.DESTINATION,link,arrowAngle,arrowLength);
			lastSegment.lineTo(points[0].getX(),points[0].getY());
			lastSegment.moveTo(points[1].getX(),points[1].getY());
			lastSegment.lineTo(intersectionPoint.getX(), intersectionPoint.getY());

			middle.setLocation((points[0].getX()+points[1].getX())/2,(points[0].getY()+points[1].getY())/2);
			destinationFirstPoint=middle;
			destinationSecondPoint=(Point2D)link.getNodes().get(link.getNodes().size()-2).getProperty(LinkNodeProperties.POSITION);
			drawDestinationCardinality=true;

		}
		else{  //nadji prvi segment linka koji sece element
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

		intersectionPoint=Calculate.intersection(Calculate.SOURCE,link);
		if (intersectionPoint!=null){
			if (!(Boolean) link.getProperty(LinkProperties.DESTINATION_NAVIGABLE) && (Boolean)link.getProperty(LinkProperties.SOURCE_NAVIGABLE)){


				lastSegment.moveTo(intersectionPoint.getX(),intersectionPoint.getY());
				Point2D[] points=Calculate.getPoints(Calculate.SOURCE,link,arrowAngle,arrowLength);
				lastSegment.lineTo(points[0].getX(),points[0].getY());
				lastSegment.moveTo(points[1].getX(),points[1].getY());
				lastSegment.lineTo(intersectionPoint.getX(), intersectionPoint.getY());

				middle.setLocation((points[0].getX()+points[1].getX())/2,(points[0].getY()+points[1].getY())/2);
				sourceFirstPoint=middle;
			}
			else
				sourceFirstPoint=Calculate.intersection(Calculate.SOURCE, link);
			sourceSecondPoint=(Point2D) link.getNodes().get(1).getProperty(LinkNodeProperties.POSITION);
			drawSourceCardinality=true;
		}
		else{  //nadji prvi segment linka koji sece element
			Point2D[] sourcePoints=Calculate.firstIntersectionSegment(Calculate.SOURCE, link);
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
		sourcePosition=sourceFirstPoint;
		destinationPosition=destinationFirstPoint;			
	}
}
