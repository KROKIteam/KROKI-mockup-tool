package graphedit.view;
import static org.junit.Assert.*;
import graphedit.model.components.Class;
import graphedit.model.components.Connector;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestLinkPainter {

	private LinkPainter lp;
	private Link link;
	private ArrayList<Point2D> testPoints=new ArrayList<Point2D>();
	private Point2D testPoint;
	
	@Before
	//Create a link with more than one segment
	public void setUp(){
		ArrayList<LinkNode> nodes=new ArrayList<LinkNode>();
		Class sourceClass=new Class(new Point2D.Double(100,100));
		Connector c1=new Connector(new Point2D.Double(30,30), sourceClass);
		nodes.add(c1);
		LinkNode ln1=new LinkNode(new Point2D.Double(30,200));
		LinkNode ln2=new LinkNode(new Point2D.Double(100,200));
		LinkNode ln3=new LinkNode(new Point2D.Double(110,270));
		LinkNode ln4=new LinkNode(new Point2D.Double(300,102));
		LinkNode ln5=new LinkNode(new Point2D.Double(220,176));
		nodes.add(ln1);
		nodes.add(ln2);
		nodes.add(ln3);
		nodes.add(ln4);
		nodes.add(ln5);
		Class destinationClass = new Class(new Point2D.Double(300,300));
		Connector c2=new Connector(new Point2D.Double(300,300), destinationClass);
		nodes.add(c2);
		link=new Link(nodes);
		lp=new LinkPainter(link);
		lp.setLink(link);
		testPoints=new ArrayList<Point2D>();
		testPoint=new Point2D.Double();
		
	}
	
	@Test
	public void inRange(){
		Point2D.Double lineStart=new Point2D.Double(100,100);
		Point2D.Double lineEnd=new Point2D.Double(100,200);
		double[] xInRange1={101,102,103,104,105,106,107,108,99,98,97,96,95,94,93,92};
		double[] xNotInRange1={109,110,111,91,90,89};
		double[] yInRange1={100,101,102,103,107,108,99,98,94,201,204,205};
		double[] yNotInRange1={90,86,85,209,210,211,212};
		
		for (double x:xInRange1)
			for (double y:yInRange1){
				testPoint.setLocation(x,y);
				assertTrue("Not in range, but should be",lp.inRange(lineStart, lineEnd, testPoint));
			}
		for (double x:xNotInRange1)
			for (double y:yNotInRange1){
				testPoint.setLocation(x,y);
				assertTrue("In range, but shouldn't be",!lp.inRange(lineStart, lineEnd, testPoint));
			}
		
		lineStart=new Point2D.Double(100,100);
		lineEnd=new Point2D.Double(200,100);
		double[] yInRange2={100,102,103,104,105,106,107,108,99,98,97,96,95,94,93,92};
		double[] yNotInRange2={109,110,111,91,90,89};
		double[] xInRange2={100,101,102,103,107,108,99,98,94,201,204,205};
		double[] xNotInRange2={90,86,85,209,210,211,212};
		
		for (double x:xInRange2)
			for (double y:yInRange2){
				testPoint.setLocation(x,y);
				assertTrue("Point (" + x + ", " + y  + " ) is not in range, but should be",lp.inRange(lineStart, lineEnd, testPoint));
			}
		for (double x:xNotInRange2)
			for (double y:yNotInRange2){
				testPoint.setLocation(x,y);
				assertFalse("Point (" + x + ", " + y + ") is in range, but shouldn't be",lp.inRange(lineStart, lineEnd, testPoint));
			}
		}
	
	@Test
	public void testElementAt(){
		//Contained points
		testPoints.add(new Point2D.Double(32,150));
		testPoints.add(new Point2D.Double(34,120));
		testPoints.add(new Point2D.Double(26,90));
		testPoints.add(new Point2D.Double(101,200));
		testPoints.add(new Point2D.Double(35,201));
		testPoints.add(new Point2D.Double(80,195));
		testPoints.add(new Point2D.Double(101,200));
		testPoints.add(new Point2D.Double(115,250));
		testPoints.add(new Point2D.Double(117,265));
		testPoints.add(new Point2D.Double(301,102));
		testPoints.add(new Point2D.Double(219,170));
		for (Point2D p:testPoints)
			assertTrue(p + " is not contained, but should be", lp.elementAt(p));
		
		//Points that are not contained
		testPoints.clear();
		testPoints.add(new Point2D.Double(40,150));
		testPoints.add(new Point2D.Double(47,156));
		testPoints.add(new Point2D.Double(21,102));
		testPoints.add(new Point2D.Double(110,210));
		testPoints.add(new Point2D.Double(34,220));
		testPoints.add(new Point2D.Double(89,209));
		testPoints.add(new Point2D.Double(93,220));
		testPoints.add(new Point2D.Double(111,283));
		testPoints.add(new Point2D.Double(114,280));
		testPoints.add(new Point2D.Double(322,112));
		testPoints.add(new Point2D.Double(220,112));
		for (Point2D p:testPoints)
			assertFalse(p + " is contained, but shouldn't be", lp.elementAt(p));

	}
}
