package graphedit.view;

import graphedit.model.components.Link;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.util.Calculate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class GeneralizationLinkPainter extends LinkPainter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double arrowLength=20;
	private double arrowAngle=0.4;
	private Point2D middle;
	
	public GeneralizationLinkPainter(Link link) {
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
		paintStereotype(g);
	}
	
	@Override
	public void setShape() {
		if (link==null){
			path=new GeneralPath();
			lastSegment=new GeneralPath();
			link=(Link)element;
			middle=new Point2D.Double();
		}
		else{
			path.reset();
			lastSegment.reset();
			sourcePosition=null;
			destinationPosition=null;	
		}

		if (link.getMovedNodeIndex()==0)
			path.moveTo(link.getMovedNodePosition().getX(),link.getMovedNodePosition().getY());
		else
			path.moveTo(((Point2D)link.getSourceConnector().getProperty(LinkNodeProperties.POSITION)).getX(),
				((Point2D)link.getSourceConnector().getProperty(LinkNodeProperties.POSITION)).getY());
		for (int i=1;i<link.getNodes().size()-1;i++){
			if (i==link.getMovedNodeIndex())
				path.lineTo(link.getMovedNodePosition().getX(),link.getMovedNodePosition().getY());
			else
				path.lineTo(((Point2D)link.getNodes().get(i).getProperty(LinkNodeProperties.POSITION)).getX(),
					 ((Point2D)link.getNodes().get(i).getProperty(LinkNodeProperties.POSITION)).getY());
		}
		//poslednji segment, sa strelicom
			Point2D intersectionPoint=Calculate.intersection(Calculate.DESTINATION,link);			
			if (intersectionPoint!=null){
				
				Point2D[] points=Calculate.getPoints(Calculate.DESTINATION,link,arrowAngle,arrowLength);
				
				middle.setLocation((points[0].getX()+points[1].getX())/2,(points[0].getY()+points[1].getY())/2);
			
				path.lineTo(middle.getX(),middle.getY());
				lastSegment.moveTo(middle.getX(),middle.getY());
				lastSegment.lineTo(points[0].getX(),points[0].getY());
				lastSegment.lineTo(intersectionPoint.getX(), intersectionPoint.getY());
				lastSegment.lineTo(points[1].getX(),points[1].getY());
				lastSegment.lineTo(middle.getX(),middle.getY()); 
				shape=lastSegment;
				
				destinationPosition=middle;
			}
				sourcePosition=Calculate.intersection(Calculate.SOURCE, link);
		}
}
