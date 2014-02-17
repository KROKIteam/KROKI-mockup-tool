package graphedit.view;

import graphedit.model.components.Link;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.util.Calculate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class InnerLinkPainter extends LinkPainter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double r=20, crossLength=5;
	private AffineTransform aft1;
	private Point2D inverseTransformPoint;
	private Ellipse2D.Double ellipse;
	private boolean drawEllipse;

	public InnerLinkPainter(Link link) {
		super(link);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.black);
		g.draw(path);
		if (drawEllipse){
			g.setColor(Color.white);
			g.fill(ellipse);
			g.setColor(Color.black);
			g.draw(ellipse);
		}
		AffineTransform a=g.getTransform();
		g.transform(aft1);
		g.draw(lastSegment);
		g.setTransform(a);
	}

	@Override
	public void setShape() {
		if (link==null){
			path=new GeneralPath();
			lastSegment=new GeneralPath();
			link=(Link)element;
			inverseTransformPoint=new Point2D.Double();
			ellipse=new Ellipse2D.Double();
		}
		else{
			path.reset();
			lastSegment.reset();
		}

		path.moveTo(((Point2D)link.getDestinationConnector().getProperty(LinkNodeProperties.POSITION)).getX(),
				((Point2D)link.getDestinationConnector().getProperty(LinkNodeProperties.POSITION)).getY());
		for (int i=link.getNodes().size()-2; i>0;i--){
			path.lineTo(((Point2D)link.getNodes().get(i).getProperty(LinkNodeProperties.POSITION)).getX(),
					((Point2D)link.getNodes().get(i).getProperty(LinkNodeProperties.POSITION)).getY());
		}

		aft1=new AffineTransform();

		Point2D[] points=Calculate.getCirclePoints(Calculate.SOURCE,r,0, link);
		if (points!=null)
		{
			drawEllipse=true;
			ellipse.setFrame(points[0].getX(),points[0].getY(),r,r);
			lastSegment.moveTo(points[1].getX()-crossLength,points[1].getY());
			lastSegment.lineTo(points[1].getX()+crossLength,points[1].getY());
			lastSegment.moveTo(points[1].getX(),points[1].getY()-crossLength);
			lastSegment.lineTo(points[1].getX(),points[1].getY()+crossLength);

			aft1.rotate(Calculate.getRotationAngle(Calculate.SOURCE, link),points[1].getX(),points[1].getY());

			aft1.transform(points[2],inverseTransformPoint );
			path.moveTo(inverseTransformPoint.getX(),inverseTransformPoint.getY());
			path.lineTo(((Point2D)link.getNodes().get(1).getProperty(LinkNodeProperties.POSITION)).getX(),((Point2D)link.getNodes().get(1).getProperty(LinkNodeProperties.POSITION)).getY());

		}
		else
			drawEllipse=false;
	}
}
