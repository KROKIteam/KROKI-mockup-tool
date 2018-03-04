package graphedit.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import graphedit.model.components.Link;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.util.Calculate;

public class RequireLinkPainter extends LinkPainter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double r=20, distance=5,minAngle=-70,extent=150;
	private AffineTransform aft1;
	private Ellipse2D.Double coverEllipse,mainEllipse;
	private Arc2D.Double arc;
	private boolean drawEllipse;
	private Point2D inverseTransformPoint;

	public RequireLinkPainter(Link link) {
		super(link);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.draw(path);
		g.setColor(Color.white);
		if (drawEllipse){
			g.fill(coverEllipse);
			g.draw(coverEllipse);
		}
		g.setColor(Color.black);
		AffineTransform a=g.getTransform();
		g.transform(aft1);
		g.draw(lastSegment);
		g.setTransform(a);
		paintStereotype(g);

	}

	@Override
	public void setShape() {
		if (link==null){
			path=new GeneralPath();
			lastSegment=new GeneralPath();
			link=(Link)element;
			coverEllipse= new Ellipse2D.Double();
			inverseTransformPoint=new Point2D.Double();
			mainEllipse=new Ellipse2D.Double();
			arc=new Arc2D.Double();
		}
		else{
			sourcePosition=null;
			destinationPosition=null;
			path.reset();
			lastSegment.reset();
		}
		path.moveTo(((Point2D)link.getSourceConnector().getProperty(LinkNodeProperties.POSITION)).getX(),
				((Point2D)link.getSourceConnector().getProperty(LinkNodeProperties.POSITION)).getY());
		for (int i=1;i<link.getNodes().size()-1;i++){
			path.lineTo(((Point2D)link.getNodes().get(i).getProperty(LinkNodeProperties.POSITION)).getX(),
					((Point2D)link.getNodes().get(i).getProperty(LinkNodeProperties.POSITION)).getY());
		}

		aft1=new AffineTransform();

		Point2D[] points=Calculate.getCirclePoints(Calculate.DESTINATION,r,distance, link);
		if (points!=null)
		{
			mainEllipse.setFrame(points[0].getX(),points[0].getY(),r,r);
			lastSegment.append(mainEllipse,false);
			lastSegment.moveTo(points[3].getX(),points[3].getY());
			arc.setArc(points[4].getX(),points[4].getY(),r+2*distance, r+2*distance, Calculate.getArcStart(minAngle, Calculate.DESTINATION, link),extent,Arc2D.OPEN);
			lastSegment.append(arc,false);

			aft1.rotate(Calculate.getRotationAngle(Calculate.DESTINATION, link),points[1].getX(),points[1].getY());


			aft1.transform(points[3],inverseTransformPoint);
			path.moveTo(inverseTransformPoint.getX(),inverseTransformPoint.getY());
			destinationPosition=inverseTransformPoint;
			path.lineTo(((Point2D)link.getNodes().get(link.getNodes().size()-2).getProperty(LinkNodeProperties.POSITION)).getX(),((Point2D)link.getNodes().get(link.getNodes().size()-2).getProperty(LinkNodeProperties.POSITION)).getY());

			coverEllipse.setFrame(points[4].getX(),points[4].getY(),r+2*distance, r+2*distance);
			drawEllipse=true;
		}
		else
			drawEllipse=false;
		sourcePosition=Calculate.intersection(Calculate.SOURCE, link);
	}
}


