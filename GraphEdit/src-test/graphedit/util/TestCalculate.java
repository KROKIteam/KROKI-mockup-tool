package graphedit.util;

import static org.junit.Assert.*;
import graphedit.model.components.Class;
import graphedit.model.components.Connector;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestCalculate {

	private Link link;
	private Class sourceClass, destinationClass;
	private Point2D.Double testPoint1,testPoint2,expectedPoint;
	private Line2D.Double testSegment;
	
	@Before
	//Create a link with more than one segment
	public void setUp(){
		ArrayList<LinkNode> nodes=new ArrayList<LinkNode>();
		sourceClass=new Class(new Point2D.Double(100,100));
		Connector c1=new Connector(new Point2D.Double(30,30), sourceClass);
		nodes.add(c1);
		LinkNode ln1=new LinkNode(new Point2D.Double(103,30));
		LinkNode ln2=new LinkNode(new Point2D.Double(143,134));
		LinkNode ln3=new LinkNode(new Point2D.Double(150,150));
		LinkNode ln4=new LinkNode(new Point2D.Double(145,150));
		LinkNode ln5=new LinkNode(new Point2D.Double(300,176));
		nodes.add(ln1);
		nodes.add(ln2);
		nodes.add(ln3);
		nodes.add(ln4);
		nodes.add(ln5);
		destinationClass = new Class(new Point2D.Double(300,300));
		Connector c2=new Connector(new Point2D.Double(300,300), destinationClass);
		nodes.add(c2);
		link=new Link(nodes);
		testPoint1=new Point2D.Double();
		testPoint2=new Point2D.Double();
		expectedPoint=new Point2D.Double();
		testSegment=new Line2D.Double();
	}

	
	@Test
	public void testFindSide(){
		testPoint1.setLocation(250,200);
		testPoint2.setLocation(250,400);
		//destination element
		assertEquals("Should be cointained by the left side",Calculate.LEFT,Calculate.findSide(testPoint1,testPoint2,Calculate.DESTINATION, link));
		testPoint1.setLocation(350,400);
		testPoint2.setLocation(350,200);
		assertEquals("Should be cointained by the right side",Calculate.RIGHT,Calculate.findSide(testPoint1,testPoint2,Calculate.DESTINATION, link));
		testPoint1.setLocation(350,200);
		testPoint2.setLocation(250,200);
		assertEquals("Should be cointained by the upper side",Calculate.UPPER,Calculate.findSide(testPoint1,testPoint2,Calculate.DESTINATION, link));
		testPoint1.setLocation(250,400);
		testPoint2.setLocation(350,400);
		assertEquals("Should be cointained by the bottom side",Calculate.BOTTOM,Calculate.findSide(testPoint1,testPoint2,Calculate.DESTINATION, link));
		//source element
		testPoint1.setLocation(50,0);
		testPoint2.setLocation(50,200);
		assertEquals("Should be cointained by the left side",Calculate.LEFT,Calculate.findSide(testPoint1,testPoint2,Calculate.SOURCE, link));
		testPoint1.setLocation(150,200);
		testPoint2.setLocation(150,0);
		assertEquals("Should be cointained by the right side",Calculate.RIGHT,Calculate.findSide(testPoint1,testPoint2,Calculate.SOURCE, link));
		testPoint1.setLocation(150,0);
		testPoint2.setLocation(50,0);
		assertEquals("Should be cointained by the upper side",Calculate.UPPER,Calculate.findSide(testPoint1,testPoint2,Calculate.SOURCE, link));
		testPoint1.setLocation(50,200);
		testPoint2.setLocation(150,200);
		assertEquals("Should be cointained by the bottom side",Calculate.BOTTOM,Calculate.findSide(testPoint1,testPoint2,Calculate.SOURCE, link));
		testPoint1.setLocation(-23,43);
		testPoint2.setLocation(143,43);
		assertEquals("Point is not contained by any side",0,Calculate.findSide(testPoint1,testPoint2,Calculate.SOURCE, link));
	}
	
	@Test
	public void testSide() {
		//destination
		testPoint1.setLocation(150,176);
		testPoint2.setLocation(300,300);
		testSegment.setLine(testPoint1,testPoint2);
		assertEquals("Should be cointained by the left side",Calculate.LEFT,Calculate.side(testSegment,Calculate.DESTINATION,link));
		testPoint1.setLocation(400,176);
		testSegment.setLine(testPoint1,testPoint2);
		assertEquals("Should be cointained by the left side",Calculate.RIGHT,Calculate.side(testSegment,Calculate.DESTINATION,link));
		testPoint1.setLocation(300,100);
		testSegment.setLine(testPoint1,testPoint2);
		assertEquals("Should be cointained by the upper side",Calculate.UPPER,Calculate.side(testSegment,Calculate.DESTINATION,link));
		testPoint1.setLocation(300,500);
		testSegment.setLine(testPoint1,testPoint2);
		assertEquals("Should be cointained by the bottom side",Calculate.BOTTOM,Calculate.side(testSegment,Calculate.DESTINATION,link));
		//source
		testPoint1.setLocation(-50,-76);
		testPoint2.setLocation(100,100);
		testSegment.setLine(testPoint1,testPoint2);
		assertEquals("Should be cointained by the left side",Calculate.LEFT,Calculate.side(testSegment,Calculate.SOURCE,link));
		testPoint1.setLocation(200,-76);
		testSegment.setLine(testPoint1,testPoint2);
		assertEquals("Should be cointained by the left side",Calculate.RIGHT,Calculate.side(testSegment,Calculate.SOURCE,link));
		testPoint1.setLocation(100,-100);
		testSegment.setLine(testPoint1,testPoint2);
		assertEquals("Should be cointained by the upper side",Calculate.UPPER,Calculate.side(testSegment,Calculate.SOURCE,link));
		testPoint1.setLocation(100,300);
		testSegment.setLine(testPoint1,testPoint2);
		assertEquals("Should be cointained by the bottom side",Calculate.BOTTOM,Calculate.side(testSegment,Calculate.SOURCE,link));
	}
	
	@Test
	public void testFindCoordinates(){
		//destination
		Double[] coordinates=Calculate.findCoordinates(Calculate.DESTINATION, link);
		Point2D.Double vertex=new Point2D.Double();
		vertex.setLocation(250,200);
		assertEquals("Upper-left vertex",vertex,coordinates[0]);
		vertex.setLocation(250,400);
		assertEquals("Bottom-left vertex",vertex,coordinates[1]);
		vertex.setLocation(350,400);
		assertEquals("Bottom-right vertex",vertex,coordinates[2]);
		vertex.setLocation(350,200);
		assertEquals("Upper-left vertex",vertex,coordinates[3]);
		//source
		coordinates=Calculate.findCoordinates(Calculate.SOURCE, link);
		vertex.setLocation(50,0);
		assertEquals("Upper-left vertex",vertex,coordinates[0]);
		vertex.setLocation(50,200);
		assertEquals("Bottom-left vertex",vertex,coordinates[1]);
		vertex.setLocation(150,200);
		assertEquals("Bottom-right vertex",vertex,coordinates[2]);
		vertex.setLocation(150,0);
		assertEquals("Upper-left vertex",vertex,coordinates[3]);
	}
	
	@Test 
	public void testGetIntersectionPoint(){
		double x1=4,y1=4,x2=4,y2=8,x3=2,y3=6,x4=8,y4=6;
		Line2D.Double line1=new Line2D.Double(x1,y1,x2,y2);
		Line2D.Double line2=new Line2D.Double(x3,y3,x4,y4);
		testPoint1.setLocation(4,6);
		Point2D intersectionPoint=Calculate.getIntersectionPoint(line1, line2);
		assertTrue("Lines should intersect at point" + testPoint1, Math.abs(intersectionPoint.getX()-testPoint1.getX())<0.1 && Math.abs(intersectionPoint.getY()-testPoint1.getY())<0.1);
		x1=269;y1=487;x2=269;y2=417;x3=443;y3=379;x4=204;y4=442;
		line1.setLine(x1,y1,x2,y2);
		line2.setLine(x3,y3,x4,y4);
		testPoint1.setLocation(269,424.866);
		intersectionPoint=Calculate.getIntersectionPoint(line1, line2);
		assertTrue("Lines should intersect at point" + testPoint1,Math.abs(intersectionPoint.getX()-testPoint1.getX())<0.1 && Math.abs(intersectionPoint.getY()-testPoint1.getY())<0.1);
	}
	
	@Test
	public void testIntersection(){
		assertNotNull("Link and line should intersect",Calculate.intersection(Calculate.DESTINATION, link));
		testPoint1.setLocation(300,200);
		assertEquals("Segment should intersect line at point" + testPoint1,testPoint1,Calculate.intersection(Calculate.DESTINATION, link));
		assertNotNull("Link and line should intersect",Calculate.intersection(Calculate.SOURCE, link));
		testPoint1.setLocation(50,30);
		assertEquals("Segment should intersect line at point" + testPoint1,testPoint1,Calculate.intersection(Calculate.SOURCE, link));
		link.getNodes().get(1).setProperty(LinkNodeProperties.POSITION,new Point2D.Double(200,30));
		assertNotNull("Link and line should intersect",Calculate.intersection(Calculate.SOURCE, link));
		assertEquals("Segment should intersect line at point" + testPoint1,testPoint1,Calculate.intersection(Calculate.SOURCE, link));
	}
	
	@Test
	public void testGetArcStart(){
		double minAngle=5;
		double expectedAngle=90+minAngle;
		assertEquals("Angle ",expectedAngle,Calculate.getArcStart(minAngle, Calculate.DESTINATION, link),0);
		link.getNodes().get(link.getNodes().size()-1).setProperty(LinkNodeProperties.POSITION,new Point2D.Double(100,170));
		expectedAngle=minAngle;
		assertEquals("Angle ",expectedAngle,Calculate.getArcStart(minAngle, Calculate.DESTINATION, link),0);
	    link.getNodes().get(link.getNodes().size()-1).setProperty(LinkNodeProperties.POSITION,new Point2D.Double(-100,470));
		assertEquals("Angle ",expectedAngle,Calculate.getArcStart(minAngle, Calculate.DESTINATION, link),0);
	    link.getNodes().get(link.getNodes().size()-1).setProperty(LinkNodeProperties.POSITION,new Point2D.Double(300,20));
	    expectedAngle=minAngle+270;
	    assertEquals("Angle ",expectedAngle,Calculate.getArcStart(minAngle, Calculate.DESTINATION, link),0);
	    link.getNodes().get(link.getNodes().size()-1).setProperty(LinkNodeProperties.POSITION,new Point2D.Double(400,300));
	    expectedAngle=minAngle+180;
	    assertEquals("Angle ",expectedAngle,Calculate.getArcStart(minAngle, Calculate.DESTINATION, link),0);
	}
	@Test
	public void testGetRotationAngle(){
		double expectedAngle=0;
		assertEquals("Angle ",expectedAngle,Calculate.getRotationAngle(Calculate.DESTINATION, link),0.1);
	    expectedAngle=-0.6;
	    link.getNodes().get(link.getNodes().size()-1).setProperty(LinkNodeProperties.POSITION,new Point2D.Double(-100,450));
		assertEquals("Angle ",expectedAngle,Calculate.getRotationAngle(Calculate.DESTINATION, link),0.1);
	    expectedAngle=0.02;
	    link.getNodes().get(link.getNodes().size()-1).setProperty(LinkNodeProperties.POSITION,new Point2D.Double(30,170));
		assertEquals("Angle ",expectedAngle,Calculate.getRotationAngle(Calculate.DESTINATION, link),0.1);
	    expectedAngle=0.72;
	    link.getNodes().get(link.getNodes().size()-1).setProperty(LinkNodeProperties.POSITION,new Point2D.Double(80,-20));
		assertEquals("Angle ",expectedAngle,Calculate.getRotationAngle(Calculate.DESTINATION, link),0.1);
	    expectedAngle=0;
		assertEquals("Angle ",expectedAngle,Calculate.getRotationAngle(Calculate.SOURCE, link),0.1);
	    expectedAngle=0.04;
	    link.getNodes().get(1).setProperty(LinkNodeProperties.POSITION,new Point2D.Double(556,55));
		assertEquals("Angle ",expectedAngle,Calculate.getRotationAngle(Calculate.SOURCE, link),0.1);
	    expectedAngle=-0.88;
	    link.getNodes().get(1).setProperty(LinkNodeProperties.POSITION,new Point2D.Double(100,-55));
		assertEquals("Angle ",expectedAngle,Calculate.getRotationAngle(Calculate.SOURCE, link),0.1);
	    expectedAngle=1.19;
	    link.getNodes().get(1).setProperty(LinkNodeProperties.POSITION,new Point2D.Double(50,80));
		assertEquals("Angle ",expectedAngle,Calculate.getRotationAngle(Calculate.SOURCE, link),0.1);
	}
	@Test
	public void firstIntersectionSegment(){
		Point2D[] expectedResult=new Point2D.Double[4];
		
		//Destination
		
		expectedResult[0]=new Point2D.Double(300,200);
		expectedResult[1]=new Point2D.Double(300,176);
		expectedResult[2]=new Point2D.Double(350,200);
		expectedResult[3]=new Point2D.Double(250,200);
		Point2D[] result = Calculate.firstIntersectionSegment(Calculate.DESTINATION, link);
		assertEquals("First point",expectedResult[0],result[0]);
		assertEquals("Second point",expectedResult[1],result[1]);
		assertEquals("Third point",expectedResult[2],result[2]);
		assertEquals("Fourth point",expectedResult[3],result[3]);
		
		//Move the element and connector
		destinationClass.setProperty(GraphElementProperties.POSITION, new Point2D.Double(145,150));
		link.getDestinationConnector().setProperty(LinkNodeProperties.POSITION, new Point2D.Double(145,150));
		link.getDestinationConnector().calculateRelativePositions(destinationClass);
		result = Calculate.firstIntersectionSegment(Calculate.DESTINATION, link);
		expectedResult[0].setLocation(195,158.3);
		expectedResult[1].setLocation(300,176);
		expectedResult[2].setLocation(195,250);
		expectedResult[3].setLocation(195,50);
		assertEquals("First point X",expectedResult[0].getX(),result[0].getX(),0.1);
		assertEquals("First point Y",expectedResult[0].getY(),result[0].getY(),0.1);
		assertEquals("Second point X",expectedResult[1].getX(),result[1].getX(),0.1);
		assertEquals("Second point Y",expectedResult[1].getY(),result[1].getY(),0.1);
		assertEquals("Third point",expectedResult[2],result[2]);
		assertEquals("Fourth point",expectedResult[3],result[3]);
		
		//Move link node
		link.setMovedNodeIndex(5);
		link.setMoveNodePosition(new Point2D.Double(200,150));
		result = Calculate.firstIntersectionSegment(Calculate.DESTINATION, link);
		expectedResult[0].setLocation(195,150);
		expectedResult[1].setLocation(200,150);
		expectedResult[2].setLocation(195,250);
		expectedResult[3].setLocation(195,50);
		assertEquals("First point X",expectedResult[0].getX(),result[0].getX(),0.1);
		assertEquals("First point Y",expectedResult[0].getY(),result[0].getY(),0.1);
		assertEquals("Second point X",expectedResult[1].getX(),result[1].getX(),0.1);
		assertEquals("Second point Y",expectedResult[1].getY(),result[1].getY(),0.1);
		assertEquals("Third point",expectedResult[2],result[2]);
		
		
		//Source
		
		result = Calculate.firstIntersectionSegment(Calculate.SOURCE, link);
		expectedResult[0].setLocation(50,30);
		expectedResult[1].setLocation(103,30);
		expectedResult[2].setLocation(50,0);
		expectedResult[3].setLocation(50,200);
		assertEquals("First point X",expectedResult[0].getX(),result[0].getX(),0.1);
		assertEquals("First point Y",expectedResult[0].getY(),result[0].getY(),0.1);
		assertEquals("Second point X",expectedResult[1].getX(),result[1].getX(),0.1);
		assertEquals("Second point Y",expectedResult[1].getY(),result[1].getY(),0.1);
		assertEquals("Third point",expectedResult[2],result[2]);
		assertEquals("Fourth point",expectedResult[3],result[3]);
		
		
		//Move the element and connector
		sourceClass.setProperty(GraphElementProperties.POSITION, new Point2D.Double(160,170));
		link.getSourceConnector().setProperty(LinkNodeProperties.POSITION, new Point2D.Double(90,100));
		link.getSourceConnector().calculateRelativePositions(sourceClass);
		result = Calculate.firstIntersectionSegment(Calculate.SOURCE, link);
		expectedResult[0].setLocation(118.4,70);
		expectedResult[1].setLocation(143,134);
		expectedResult[2].setLocation(210,70);
		expectedResult[3].setLocation(110,70);
		assertEquals("First point X",expectedResult[0].getX(),result[0].getX(),0.1);
		assertEquals("First point Y",expectedResult[0].getY(),result[0].getY(),0.1);
		assertEquals("Second point X",expectedResult[1].getX(),result[1].getX(),0.1);
		assertEquals("Second point Y",expectedResult[1].getY(),result[1].getY(),0.1);
		assertEquals("Third point",expectedResult[2],result[2]);
		assertEquals("Fourth point",expectedResult[3],result[3]);
		
		//Move link node
		link.setMovedNodeIndex(1);
		link.setMoveNodePosition(new Point2D.Double(30,10));
		result = Calculate.firstIntersectionSegment(Calculate.SOURCE, link);
		expectedResult[0].setLocation(110,97.8);
		expectedResult[2].setLocation(110,70);
		expectedResult[3].setLocation(110,270);
		assertEquals("First point X",expectedResult[0].getX(),result[0].getX(),0.1);
		assertEquals("First point Y",expectedResult[0].getY(),result[0].getY(),0.1);
		assertEquals("Second point X",expectedResult[1].getX(),result[1].getX(),0.1);
		assertEquals("Second point Y",expectedResult[1].getY(),result[1].getY(),0.1);
		assertEquals("Third point",expectedResult[2],result[2]);
		assertEquals("Fourth point",expectedResult[3],result[3]);
		
		link.setMovedNodeIndex(-1);
	}
	
	@Test
	public void testGetStereotypePosition(){
		int textWidth=20;
		Point2D sourceFirstPoint=new Point2D.Double(110,97.8);
		Point2D destinationFirstPoint=new Point2D.Double(195,158);
		expectedPoint.setLocation(215,163.4);
		Point2D result=Calculate.getStereotypePosition(link, textWidth, sourceFirstPoint, destinationFirstPoint);
		assertEquals("Stereotype x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Stereotype y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
		//Move one of the elements and connector
		sourceClass.setProperty(GraphElementProperties.POSITION, new Point2D.Double(145,150));
		link.getSourceConnector().setProperty(LinkNodeProperties.POSITION, new Point2D.Double(75,80));
		link.getSourceConnector().calculateRelativePositions(sourceClass);
		sourceFirstPoint.setLocation(110.7,50);
		expectedPoint.setLocation(237.5,167);
		result=Calculate.getStereotypePosition(link, textWidth, sourceFirstPoint, destinationFirstPoint);
		assertEquals("Stereotype x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Stereotype y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
		//Move one of the elements
		sourceClass.setProperty(GraphElementProperties.POSITION, new Point2D.Double(-10,40));
		link.getSourceConnector().setProperty(LinkNodeProperties.POSITION, new Point2D.Double(-80,-30));
		link.getSourceConnector().calculateRelativePositions(sourceClass);
		sourceFirstPoint.setLocation(-60,-23.4);
		expectedPoint.setLocation(137.5,150);
		result=Calculate.getStereotypePosition(link, textWidth, sourceFirstPoint, destinationFirstPoint);
		assertEquals("Stereotype x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Stereotype y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
		
		//Move one of the elements
		destinationClass.setProperty(GraphElementProperties.POSITION, new Point2D.Double(100,70));
		link.getDestinationConnector().setProperty(LinkNodeProperties.POSITION, new Point2D.Double(100,70));
		link.getDestinationConnector().calculateRelativePositions(destinationClass);
		destinationFirstPoint.setLocation(150,96.5);
		expectedPoint.setLocation(113,82);
		result=Calculate.getStereotypePosition(link, textWidth, sourceFirstPoint, destinationFirstPoint);
		assertEquals("Stereotype x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Stereotype y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
		
		//Move one of the elements
		destinationClass.setProperty(GraphElementProperties.POSITION, new Point2D.Double(300,180));
		link.getDestinationConnector().setProperty(LinkNodeProperties.POSITION, new Point2D.Double(300,180));
		link.getDestinationConnector().calculateRelativePositions(destinationClass);
		destinationFirstPoint.setLocation(250,167.6);
		expectedPoint.setLocation(136.4,142);
		result=Calculate.getStereotypePosition(link, textWidth, sourceFirstPoint, destinationFirstPoint);
		assertEquals("Stereotype x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Stereotype y coordinate",expectedPoint.getY(),result.getY(),0.1);

	}
	
	@Test
	public void testGetCirclePoints(){

	Point2D[] expectedResult=new Point2D.Double[5];
	for (int i=0;i<5;i++)
		expectedResult[i]=new Point2D.Double();
	
	//inner link
	double r=20, distance=0;
	
	expectedResult[0].setLocation(30,20);
	expectedResult[1].setLocation(40,30);
	expectedResult[2].setLocation(30,30);
	
	Point2D[] result=Calculate.getCirclePoints(Calculate.SOURCE, r, distance, link);
	assertEquals("Circle location x coordinate",expectedResult[0].getX(),result[0].getX(),0.1);
	assertEquals("Circle location y coordinate",expectedResult[0].getY(),result[0].getY(),0.1);
	assertEquals("Circle centre x coordinate",expectedResult[1].getX(),result[1].getX(),0.1);
	assertEquals("Circle centre y coordinate",expectedResult[1].getY(),result[1].getY(),0.1);
	assertEquals("Append point x coordinate",expectedResult[2].getX(),result[2].getX(),0.1);
	assertEquals("Append point y coordinate",expectedResult[2].getY(),result[2].getY(),0.1);

	
	//move node
	link.setMovedNodeIndex(1);
	link.setMovedNodePosition(new Point2D.Double(200,50));
	expectedResult[0].setLocation(30,22.4);
	expectedResult[1].setLocation(40,32.4);
	expectedResult[2].setLocation(30,32.4);
	result=Calculate.getCirclePoints(Calculate.SOURCE, r, distance, link);
	assertEquals("Circle location x coordinate",expectedResult[0].getX(),result[0].getX(),0.1);
	assertEquals("Circle location y coordinate",expectedResult[0].getY(),result[0].getY(),0.1);
	assertEquals("Circle centre x coordinate",expectedResult[1].getX(),result[1].getX(),0.1);
	assertEquals("Circle centre y coordinate",expectedResult[1].getY(),result[1].getY(),0.1);
	assertEquals("Append point x coordinate",expectedResult[2].getX(),result[2].getX(),0.1);
	assertEquals("Append point y coordinate",expectedResult[2].getY(),result[2].getY(),0.1);
	
	//move element and connector
	sourceClass.setProperty(GraphElementProperties.POSITION, new Point2D.Double(-100,-100));
	link.getSourceConnector().setProperty(LinkNodeProperties.POSITION, new Point2D.Double(-170,-170));
	link.getSourceConnector().calculateRelativePositions(sourceClass);
	expectedResult[0].setLocation(-170,-168.1);
	expectedResult[1].setLocation(-160,-158.1);
	expectedResult[2].setLocation(-170,-158.1);
	result=Calculate.getCirclePoints(Calculate.SOURCE, r, distance, link);
	assertEquals("Circle location x coordinate",expectedResult[0].getX(),result[0].getX(),0.1);
	assertEquals("Circle location y coordinate",expectedResult[0].getY(),result[0].getY(),0.1);
	assertEquals("Circle centre x coordinate",expectedResult[1].getX(),result[1].getX(),0.1);
	assertEquals("Circle centre y coordinate",expectedResult[1].getY(),result[1].getY(),0.1);
	assertEquals("Append point x coordinate",expectedResult[2].getX(),result[2].getX(),0.1);
	assertEquals("Append point y coordinate",expectedResult[2].getY(),result[2].getY(),0.1);
	
	//require link
	distance=5;
	
	expectedResult[0].setLocation(290,180);
	expectedResult[3].setLocation(300,180);
	expectedResult[4].setLocation(285,175);
	result=Calculate.getCirclePoints(Calculate.DESTINATION, r, distance, link);
	assertEquals("Circle location x coordinate",expectedResult[0].getX(),result[0].getX(),0.1);
	assertEquals("Circle location y coordinate",expectedResult[0].getY(),result[0].getY(),0.1);
	assertEquals("Append point x coordinate",expectedResult[3].getX(),result[2].getX(),0.1);
	assertEquals("Append point y coordinate",expectedResult[3].getY(),result[2].getY(),0.1);
	assertEquals("Outer circle location x coordinate",expectedResult[4].getX(),result[4].getX(),0.1);
	assertEquals("Outer circle location y coordinate",expectedResult[4].getY(),result[4].getY(),0.1);
	
	
	//move node
	link.setMovedNodeIndex(5);
	link.setMovedNodePosition(new Point2D.Double(200,50));
	expectedResult[0].setLocation(250,180);
	expectedResult[3].setLocation(260,180);
	expectedResult[4].setLocation(245,175);
	result=Calculate.getCirclePoints(Calculate.DESTINATION, r, distance, link);
	assertEquals("Circle location x coordinate",expectedResult[0].getX(),result[0].getX(),0.1);
	assertEquals("Circle location y coordinate",expectedResult[0].getY(),result[0].getY(),0.1);
	assertEquals("Append point x coordinate",expectedResult[3].getX(),result[2].getX(),0.1);
	assertEquals("Append point y coordinate",expectedResult[3].getY(),result[2].getY(),0.1);
	assertEquals("Outer circle location x coordinate",expectedResult[4].getX(),result[4].getX(),0.1);
	assertEquals("Outer circle location y coordinate",expectedResult[4].getY(),result[4].getY(),0.1);
	
	//move element and connector
	destinationClass.setProperty(GraphElementProperties.POSITION, new Point2D.Double(200,200));
	link.getSourceConnector().setProperty(LinkNodeProperties.POSITION, new Point2D.Double(200,-200));
	link.getDestinationConnector().calculateRelativePositions(destinationClass);
	expectedResult[0].setLocation(250,165);
	expectedResult[3].setLocation(270,175);
	expectedResult[4].setLocation(245,160);
	result=Calculate.getCirclePoints(Calculate.DESTINATION, r, distance, link);
	assertEquals("Circle location x coordinate",expectedResult[0].getX(),result[0].getX(),0.1);
	assertEquals("Circle location y coordinate",expectedResult[0].getY(),result[0].getY(),0.1);
	assertEquals("Append point x coordinate",expectedResult[3].getX(),result[2].getX(),0.1);
	assertEquals("Append point y coordinate",expectedResult[3].getY(),result[2].getY(),0.1);
	assertEquals("Outer circle location x coordinate",expectedResult[4].getX(),result[4].getX(),0.1);
	assertEquals("Outer circle location y coordinate",expectedResult[4].getY(),result[4].getY(),0.1);
	
	link.setMovedNodeIndex(-1);
	}
	
	@Test
	public void testGetCardinalityPosition(){
		int textHeight=10,textWidth=30;
		int xDistance=5,yDistance=5;
		
		
		//test situation when firstElementPoint==null || secondElementPoint==null
		//firstPoint!=null && secondPoint!=null
		testPoint1.setLocation(50,30);
		testPoint2.setLocation(103,30);
		expectedPoint.setLocation(15,40);
		Point2D result=Calculate.getCardinalityPosition(textWidth,textHeight,Calculate.SOURCE,link,xDistance,yDistance,testPoint1,testPoint2,null,null);
		assertEquals("Cardinality x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Cardinality y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
		//move node
		link.setMovedNodeIndex(1);
		link.setMovedNodePosition(new Point2D.Double(200,-140));
		testPoint1.setLocation(50,10);
		testPoint2.setLocation(200,-140);
		expectedPoint.setLocation(15,20);
		result=Calculate.getCardinalityPosition(textWidth,textHeight,Calculate.SOURCE,link,xDistance,yDistance,testPoint1,testPoint2,null,null);
		assertEquals("Cardinality x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Cardinality y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
		//move node
		link.setMovedNodePosition(new Point2D.Double(200,140));
		testPoint1.setLocation(50,42.9);
		testPoint2.setLocation(200,140);
		expectedPoint.setLocation(15,37.9);
		result=Calculate.getCardinalityPosition(textWidth,textHeight,Calculate.SOURCE,link,xDistance,yDistance,testPoint1,testPoint2,null,null);
		assertEquals("Cardinality x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Cardinality y coordinate",expectedPoint.getY(),result.getY(),0.1);
	
		link.setMovedNodeIndex(-1);
		
		testPoint1.setLocation(300,200);
		testPoint2.setLocation(300,176);
		expectedPoint.setLocation(305,195);
		result=Calculate.getCardinalityPosition(textWidth,textHeight,Calculate.DESTINATION,link,xDistance,yDistance,testPoint1,testPoint2,null,null);
		assertEquals("Cardinality x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Cardinality y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
		
		//move node
		link.setMovedNodeIndex(5);
		link.setMovedNodePosition(new Point2D.Double(100,-100));
		testPoint1.setLocation(250,200);
		testPoint2.setLocation(100,-100);
		expectedPoint.setLocation(215,210);
		result=Calculate.getCardinalityPosition(textWidth,textHeight,Calculate.DESTINATION,link,xDistance,yDistance,testPoint1,testPoint2,null,null);
		assertEquals("Cardinality x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Cardinality y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
		
		link.setMovedNodePosition(new Point2D.Double(-100,-100));
		testPoint1.setLocation(250,250);
		testPoint2.setLocation(-100,-100);
		expectedPoint.setLocation(215,260);
		result=Calculate.getCardinalityPosition(textWidth,textHeight,Calculate.DESTINATION,link,xDistance,yDistance,testPoint1,testPoint2,null,null);
		assertEquals("Cardinality x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Cardinality y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
		link.setMovedNodeIndex(-1);
		
		//test situation when firstElementPoint!=null && secondElementPoint!=null
		Point2D firstElementPoint=new Point2D.Double(50,0);
		Point2D secondElementPoint=new Point2D.Double(50,200);
		testPoint1.setLocation(50,30);
		testPoint2.setLocation(103,30);
		expectedPoint.setLocation(15,40);
		result=Calculate.getCardinalityPosition(textWidth,textHeight,Calculate.SOURCE,link,xDistance,yDistance,testPoint1,testPoint2,firstElementPoint,secondElementPoint);
		assertEquals("Cardinality x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Cardinality y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
	
		//move node
		link.setMovedNodeIndex(1);
		link.setMovedNodePosition(new Point2D.Double(30,300));
		firstElementPoint=new Point2D.Double(50,200);
		secondElementPoint=new Point2D.Double(150,200);
		testPoint2.setLocation(143,134);
		testPoint1.setLocation(98.1,200);
		expectedPoint.setLocation(63.1,210);
		result=Calculate.getCardinalityPosition(textWidth,textHeight,Calculate.SOURCE,link,xDistance,yDistance,testPoint1,testPoint2,firstElementPoint,secondElementPoint);
		assertEquals("Cardinality x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Cardinality y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
		
	//move node
		link.setMovedNodePosition(new Point2D.Double(30,-100));
		firstElementPoint.setLocation(150,0);
		secondElementPoint.setLocation(50,0);
		testPoint2.setLocation(143,134);
		testPoint1.setLocation(78.3,0);
		expectedPoint.setLocation(43.3,-5);
		result=Calculate.getCardinalityPosition(textWidth,textHeight,Calculate.SOURCE,link,xDistance,yDistance,testPoint1,testPoint2,firstElementPoint,secondElementPoint);
		assertEquals("Cardinality x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Cardinality y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
		link.setMovedNodeIndex(-1);
		
		firstElementPoint.setLocation(350,200);
		secondElementPoint.setLocation(250,200);
		testPoint1.setLocation(300,200);
		testPoint2.setLocation(300,176);
		expectedPoint.setLocation(305,195);
		result=Calculate.getCardinalityPosition(textWidth,textHeight,Calculate.DESTINATION,link,xDistance,yDistance,testPoint1,testPoint2,firstElementPoint,secondElementPoint);
		assertEquals("Cardinality x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Cardinality y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
		//move node
		link.setMovedNodeIndex(5);
		link.setMovedNodePosition(new Point2D.Double(100,-100));
		firstElementPoint.setLocation(250,200);
		secondElementPoint.setLocation(250,400);
		testPoint1.setLocation(250,200);
		testPoint2.setLocation(100,-10);
		expectedPoint.setLocation(215,210);
		result=Calculate.getCardinalityPosition(textWidth,textHeight,Calculate.DESTINATION,link,xDistance,yDistance,testPoint1,testPoint2,firstElementPoint,secondElementPoint);
		assertEquals("Cardinality x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Cardinality y coordinate",expectedPoint.getY(),result.getY(),0.1);
	
		//move node
		link.setMovedNodePosition(new Point2D.Double(-100,-100));
		testPoint1.setLocation(250,250);
		testPoint2.setLocation(-100,-100);
		expectedPoint.setLocation(215,260);
		result=Calculate.getCardinalityPosition(textWidth,textHeight,Calculate.DESTINATION,link,xDistance,yDistance,testPoint1,testPoint2,firstElementPoint,secondElementPoint);
		assertEquals("Cardinality x coordinate",expectedPoint.getX(),result.getX(),0.1);
		assertEquals("Cardinality y coordinate",expectedPoint.getY(),result.getY(),0.1);
		
		link.setMovedNodeIndex(-1);
	}
	
	@Test
	public void testGetPoints(){
		double angle=30,length=20;
		Point2D[] expectedResult=new Point2D.Double[2];
		expectedResult[0]=new Point2D.Double(53.1,10.2);
		expectedResult[1]=new Point2D.Double(53.1,-29.3);
		Point2D[] result=Calculate.getPoints(Calculate.SOURCE, link, angle, length);
		assertEquals("The first arrow point x coordinate",expectedResult[0].getX(),result[0].getX(),0.1);
		assertEquals("The first arrow point y coordinate",expectedResult[0].getY(),result[0].getY(),0.1);
		assertEquals("The second arrow point x coordinate",expectedResult[1].getX(),result[1].getX(),0.1);
		assertEquals("The second arrow point y coordinate",expectedResult[1].getY(),result[1].getY(),0.1);
		
		//move node
		link.setMovedNodeIndex(1);
		link.setMovedNodePosition(new Point2D.Double(200,75));
		expectedResult[0]=new Point2D.Double(58,17);
		expectedResult[1]=new Point2D.Double(68.2,-21.2);
		result=Calculate.getPoints(Calculate.SOURCE, link, angle, length);
		assertEquals("The first arrow point x coordinate",expectedResult[0].getX(),result[0].getX(),0.1);
		assertEquals("The first arrow point y coordinate",expectedResult[0].getY(),result[0].getY(),0.1);
		assertEquals("The second arrow point x coordinate",expectedResult[1].getX(),result[1].getX(),0.1);
		assertEquals("The second arrow point y coordinate",expectedResult[1].getY(),result[1].getY(),0.1);
		
		//move node
		link.setMovedNodePosition(new Point2D.Double(100,-75));
		expectedResult[0]=new Point2D.Double(35.3,-13.5);
		expectedResult[1]=new Point2D.Double(2.4,-35.4);
		result=Calculate.getPoints(Calculate.SOURCE, link, angle, length);
		assertEquals("The first arrow point x coordinate",expectedResult[0].getX(),result[0].getX(),0.1);
		assertEquals("The first arrow point y coordinate",expectedResult[0].getY(),result[0].getY(),0.1);
		assertEquals("The second arrow point x coordinate",expectedResult[1].getX(),result[1].getX(),0.1);
		assertEquals("The second arrow point y coordinate",expectedResult[1].getY(),result[1].getY(),0.1);
		
		
		expectedResult[0]=new Point2D.Double(280.2,197);
		expectedResult[1]=new Point2D.Double(240.7,196.9);
		result=Calculate.getPoints(Calculate.DESTINATION, link, angle, length);
		assertEquals("The first arrow point x coordinate",expectedResult[0].getX(),result[0].getX(),0.1);
		assertEquals("The first arrow point y coordinate",expectedResult[0].getY(),result[0].getY(),0.1);
		assertEquals("The second arrow point x coordinate",expectedResult[1].getX(),result[1].getX(),0.1);
		assertEquals("The second arrow point y coordinate",expectedResult[1].getY(),result[1].getY(),0.1);
		
		//move node
		link.setMovedNodeIndex(5);
		link.setMoveNodePosition(new Point2D.Double(200,150));
		expectedResult[0]=new Point2D.Double(264.7,211.5);
		expectedResult[1]=new Point2D.Double(297.6,189.5);
		result=Calculate.getPoints(Calculate.DESTINATION, link, angle, length);
		assertEquals("The first arrow point x coordinate",expectedResult[0].getX(),result[0].getX(),0.1);
		assertEquals("The first arrow point y coordinate",expectedResult[0].getY(),result[0].getY(),0.1);
		assertEquals("The second arrow point x coordinate",expectedResult[1].getX(),result[1].getX(),0.1);
		assertEquals("The second arrow point y coordinate",expectedResult[1].getY(),result[1].getY(),0.1);
		
		//move node
		link.setMovedNodePosition(new Point2D.Double(150,-75));
		expectedResult[0]=new Point2D.Double(277.2,189.8);
		expectedResult[1]=new Point2D.Double(313.9,175.1);
		result=Calculate.getPoints(Calculate.DESTINATION, link, angle, length);
		assertEquals("The first arrow point x coordinate",expectedResult[0].getX(),result[0].getX(),0.1);
		assertEquals("The first arrow point y coordinate",expectedResult[0].getY(),result[0].getY(),0.1);
		assertEquals("The second arrow point x coordinate",expectedResult[1].getX(),result[1].getX(),0.1);
		assertEquals("The second arrow point y coordinate",expectedResult[1].getY(),result[1].getY(),0.1);
		
	}
}
