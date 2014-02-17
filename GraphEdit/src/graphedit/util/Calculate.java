package graphedit.util;

import graphedit.model.components.Attribute;
import graphedit.model.components.Class;
import graphedit.model.components.Connector;
import graphedit.model.components.Link;
import graphedit.model.components.Method;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.properties.Preferences;

import java.awt.FontMetrics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Class contains methods that are used when drawing links and elements 
 * @author tim1
 *
 */
public class Calculate {

	public static final int SOURCE = 0;
	public static final int DESTINATION = 1;

	public static final int LEFT = 1, BOTTOM = 2, RIGHT = 3, UPPER = 4;

	private static Line2D.Double segment = new Line2D.Double();;
	private static Point2D p1;
	private static Point2D p2;
	private static Connector connector;

	int p;

	/**
	 * Initialisation - sets the first and the second point - positions of corresponding link nodes
	 * @param type - source or destination
	 * @param link
	 * @author tim1
	 */
	private static void init(int type, Link link) {

		if (type == SOURCE) {
			connector = link.getSourceConnector();
			p1 = (Point2D) link.getNodes().get(1).getProperty(
					LinkNodeProperties.POSITION);
			p2 = (Point2D) connector.getProperty(LinkNodeProperties.POSITION);
		} else {
			connector = link.getDestinationConnector();
			p1 = (Point2D) link.getNodes().get(link.getNodes().size() - 2)
					.getProperty(LinkNodeProperties.POSITION);
			p2 = (Point2D) connector.getProperty(LinkNodeProperties.POSITION);
		}
		segment.setLine(p1, p2);

	}

	/**
	 * Calculates the position of the intersection point between a link segment and element 
	 * @param type - source or destination
	 * @param link
	 * @return intersection point or null if the above mentioned link segment and element don't intersect 
	 * @author tim1
	 */
	public static Point2D intersection(int type, Link link) {

		init(type, link);
		Line2D.Double line = new Line2D.Double();
		Point2D.Double[] coordinates = findCoordinates(type, link);

		int i = side(segment, type, link);
		if (segment != null)
			if (i != 0) {
				line.setLine(coordinates[i - 1], coordinates[i % 4]);
				return getIntersectionPoint(line, segment);
			}
		return null;
	}


	/**
	 * Used if we don't know whether the element covers some segments of the link or not. 
	 * If a link segment is being moved, method uses it's new position. 
	 * @param type - source or destination
	 * @param link
	 * @return retVal[0] and retVal[1] - two points contained by the link. Intersection point and the first node that isn't covered by the element.
	 * retVal[2] and retVal[3] - two vertices of the element that will make it easier to determine the side of the
	 * element that contains the intersection point, if needed
	 * @author tim1
	 */
	public static Point2D[] firstIntersectionSegment(int type, Link link) {
		Point2D p1, p2;
		Point2D.Double[] coordinates = findCoordinates(type, link);
		Line2D.Double line = new Line2D.Double();
		Line2D.Double segment = new Line2D.Double();
		Point2D[] retVal = new Point2D[4];

		if (type == SOURCE) {
			for (int i = 0; i < link.getNodes().size() - 1; i++) {
				p1 = (Point2D) link.getNodes().get(i).getProperty(
						LinkNodeProperties.POSITION);				
				p2 = (Point2D) link.getNodes().get(i + 1).getProperty(
						LinkNodeProperties.POSITION);
				segment.setLine(p1, p2);
				for (int j = 1; j <= 4; j++) {
					line.setLine(coordinates[j - 1], coordinates[j % 4]);
					if (line.intersectsLine(segment)) {
						retVal[1] = p2;
						retVal[0] = getIntersectionPoint(line, segment);
						retVal[2]=coordinates[j-1];
						retVal[3]=coordinates[j%4];
						return retVal;
					}
				}
			}
		} else {

			for (int i = link.getNodes().size() - 1; i > 0; i--) {
				p1 = (Point2D) link.getNodes().get(i).getProperty(
						LinkNodeProperties.POSITION);
				p2 = (Point2D) link.getNodes().get(i - 1).getProperty(
						LinkNodeProperties.POSITION);
				segment.setLine(p1, p2);
				for (int j = 1; j <= 4; j++) {
					line.setLine(coordinates[j - 1], coordinates[j % 4]);
					if (line.intersectsLine(segment)) {
						retVal[0] = getIntersectionPoint(line, segment);
						retVal[1] = p2;
						retVal[2]=coordinates[j-1];
						retVal[3]=coordinates[j%4];
						return retVal;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Method used when stereotypes should be drawn. Calculates the stereotype position. 
	 * Takes into consideration current positions of the elements, which might be covering  
	 * some link segments.
	 * @param link
	 * @param textWidth
	 * @param sourceFirstPoint
	 * @param destinationFirstPoint
	 * @return stereotype position
	 * @author tim1
	 */
	public static Point2D getStereotypePosition( Link link, int textWidth, Point2D sourceFirstPoint, Point2D destinationFirstPoint){
		Point2D p1, p2;
		Point2D.Double[] coordinates;
		Line2D.Double line = new Line2D.Double();
		Line2D.Double segment = new Line2D.Double();
		int sourceIndex=0, destinationIndex=0;
		Point2D sourceIntersection=null,destinationIntersection=null;


		//za destination
		coordinates= findCoordinates(DESTINATION, link);
		for (int i = 0; i < link.getNodes().size() - 1; i++) {
			if (i==0)
				p1=sourceFirstPoint;
			p1 = (Point2D) link.getNodes().get(i).getProperty(
					LinkNodeProperties.POSITION);				
			p2 = (Point2D) link.getNodes().get(i + 1).getProperty(
					LinkNodeProperties.POSITION);
			if (p1==null || p2==null)
				return null;
			segment.setLine(p1, p2);
			for (int j = 1; j <= 4; j++) {
				line.setLine(coordinates[j - 1], coordinates[j % 4]);
				if (line.intersectsLine(segment)) {
					destinationIntersection=getIntersectionPoint(line, segment);
					destinationIndex=i;
					if (destinationIndex>0)
						break;
				}
				if (destinationIntersection!=null && destinationIndex>0)
					break;
			}
		}

		//za source
		coordinates= findCoordinates(SOURCE, link);
		for (int i = link.getNodes().size() - 1; i > 0; i--) {
			if (i==link.getNodes().size()-1)
				p1=destinationFirstPoint;
			p1 = (Point2D) link.getNodes().get(i).getProperty(
					LinkNodeProperties.POSITION);
			p2 = (Point2D) link.getNodes().get(i - 1).getProperty(
					LinkNodeProperties.POSITION);
			if (p1==null || p2==null)
				return null;
			segment.setLine(p1, p2);
			for (int j = 1; j <= 4; j++) {
				line.setLine(coordinates[j - 1], coordinates[j % 4]);
				if (line.intersectsLine(segment)){ 
					sourceIndex=i;
					sourceIntersection=getIntersectionPoint(line, segment);
					if (sourceIndex<link.getNodes().size()-1)
						break;
				}
			}
			if (sourceIntersection!=null && sourceIndex<link.getNodes().size()-1)
				break;
		}
		if (destinationIndex>sourceIndex){
			int index=sourceIndex+(destinationIndex-sourceIndex)/2;
			p1=(Point2D) link.getNodes().get(index).getProperty(LinkNodeProperties.POSITION);
			p2=(Point2D) link.getNodes().get(index+1).getProperty(LinkNodeProperties.POSITION);
		}
		else{
			Point2D sourcePoint,destinationPoint;
			if (sourceIndex==1)
				sourcePoint=sourceFirstPoint;
			else
				sourcePoint=sourceIntersection;
			if (destinationIndex==link.getNodes().size()-2)
				destinationPoint=destinationFirstPoint;
			else
				destinationPoint=destinationIntersection;

			if (destinationIndex==sourceIndex){

				if (sourcePoint!=null || destinationPoint!=null){
					p1=(Point2D) link.getNodes().get(sourceIndex).getProperty(LinkNodeProperties.POSITION);
					//uzmi veci segment
					if (destinationPoint!=null && sourcePoint==null)
						p2=destinationPoint;
					else if (destinationPoint==null && sourcePoint!=null)
						p2=sourcePoint;
					else
						if ((Math.pow(p1.getX()-sourcePoint.getX(),2)+Math.pow(p1.getY()-sourcePoint.getY(),2))>
						(Math.pow(p1.getX()-destinationPoint.getX(),2)+Math.pow(p1.getY()-destinationPoint.getY(),2)))
							p2=sourcePoint;
						else
							p2=destinationPoint;
				}
				else 
					return null;
			}
			else
				if(destinationPoint!=null && sourcePoint!=null){
					p1=sourcePoint;
					p2=destinationPoint;
				}
				else
					return null;
		}

		return new Point2D.Double((p1.getX()+p2.getX()-textWidth)/2,(p1.getY()+p2.getY())/2);		
	}


	/**
	 * Method determines on which side of the rectangular element the intersection is, 
	 * based on the positions of two vertices of the element
	 * @param p1 
	 * @param p2 
	 * @param type - destination or source
	 * @param link
	 * @return side or 0, if the element doesn't even contain the points
	 * @author tim1
	 */
	public static int findSide(Point2D p1, Point2D p2,int type, Link link){
		init(type,link);
		Point2D.Double[] coordinates = findCoordinates(type, link);
		for (int j = 1; j <= 4; j++)
			if (coordinates[j - 1].getX()==p1.getX() && coordinates[j-1].getY()==p1.getY() &&
			coordinates[j%4].getX()==p2.getX() && coordinates[j%4].getY()==p2.getY())
				return j;
		return 0;

	}
	/**
	 * Determines the side of the element that intersects intersectionSegment
	 * @param intersectionSegment
	 * @param type -destination or source
	 * @param link
	 * @return side of the element, or 0 if there is no intersection
	 * @author tim1
	 */
	public static int side(Line2D intersectionSegment, int type, Link link) {

		Line2D.Double line = new Line2D.Double();
		Point2D.Double[] coordinates = findCoordinates(type, link);
		if (intersectionSegment != null)

			for (int i = 1; i <= 4; i++) {
				line.setLine(coordinates[i - 1], coordinates[i % 4]);
				if (line.intersectsLine(intersectionSegment))
					return i;
			}
		return 0;
	}

	/**
	 * Based on the position of the connector and its distance from the sides of the element, method calculates 
	 * coordinates of the four vertices
	 * @param type - source or destination
	 * @param link
	 * @return coordinates of the vertices 
	 * @author tim1
	 */
	public static Point2D.Double[] findCoordinates(int type, Link link) {
		init(type, link);

		Point2D.Double[] coordinates = new Point2D.Double[4];


		Point2D position =p2;
		double xMin = position.getX() - connector.getMinXRelative();
		double xMax = position.getX() + connector.getMaxXRelative();

		double yMin = position.getY() - connector.getMinYRelative();
		double yMax = position.getY() + connector.getMaxYRelative();

		coordinates[0] = new Point2D.Double(xMin, yMin);
		coordinates[1] = new Point2D.Double(xMin, yMax);
		coordinates[2] = new Point2D.Double(xMax, yMax);
		coordinates[3] = new Point2D.Double(xMax, yMin);

		return coordinates;
	}

	/**
	 * Based on the positions of first and the last vertices of two lines, method determines 
	 * the location of the intersection point. Assumes that the lines intersect.
	 * @param line1
	 * @param line2
	 * @return intersection point
	 * @author tim1
	 */
	public static Point2D getIntersectionPoint(Line2D.Double line1,
			Line2D.Double line2) {

		double x1 = line1.x1;
		double y1 = line1.y1;
		double x2 = line1.x2;
		double y2 = line1.y2;
		double x3 = line2.x1;
		double y3 = line2.y1;
		double x4 = line2.x2;
		double y4 = line2.y2;
		double x = ((x2 - x1) * (x3 * y4 - x4 * y3) - (x4 - x3)
				* (x1 * y2 - x2 * y1))
				/ ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
		double y = ((y3 - y4) * (x1 * y2 - x2 * y1) - (y1 - y2)
				* (x3 * y4 - x4 * y3))
				/ ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
		return new Point2D.Double(x, y);
	}
	/**
	 * Method used when drawing symbols that contain a circle - require and inner element.
	 * @param type - source or destination
	 * @param r - radius of the circle that should be drawn
	 * @param distance - distance between the point where the next part of the symbol should be drawn and
	 * the circle
	 * @param link
	 * @return retVal[0] - location of the circle, retVal[1] - centre of the circle, 
	 * retVal[2] - location of the point, contained by the circle which will be append other elements to the circle if inner element symbol is being drawn
	 * retVal[3] - location of the point, contained by the circle which will be append other elements to the circle if require symbol is being drawn
	 * retVal[4] - location of the outer circle
	 * @author tim1
	 */
	public static Point2D[] getCirclePoints(int type, double r,
			double distance, Link link) {
		Line2D.Double line = new Line2D.Double();
		Point2D.Double[] coordinates = new Point2D.Double[4];
		Point2D.Double[] retval = new Point2D.Double[5];
		init(type, link);
		coordinates = findCoordinates(type, link);

		if (segment != null) {
			line.setLine(coordinates[0], coordinates[1]);
			if (line.intersectsLine(segment)) {
				Point2D intersectionPoint = getIntersectionPoint(line, segment);
				retval[0] = new Point2D.Double(intersectionPoint.getX() - r,
						intersectionPoint.getY() - r / 2);
				retval[1] = new Point2D.Double(
						intersectionPoint.getX() - r / 2, intersectionPoint.getY());
				retval[2] = new Point2D.Double(intersectionPoint.getX() - r,
						intersectionPoint.getY());
				retval[3] = new Point2D.Double(intersectionPoint.getX() - r
						- distance, intersectionPoint.getY());
				retval[4] = new Point2D.Double(retval[0].getX() - distance,
						retval[0].getY() - distance);
				return retval;
			}
			line.setLine(coordinates[1], coordinates[2]);
			if (line.intersectsLine(segment)) {
				Point2D intersectionPoint = getIntersectionPoint(line, segment);
				retval[0] = new Point2D.Double(intersectionPoint.getX() - r / 2, 
						intersectionPoint.getY());
				retval[1] = new Point2D.Double(intersectionPoint.getX(),
						intersectionPoint.getY() + r / 2);
				retval[2] = new Point2D.Double(intersectionPoint.getX(),
						intersectionPoint.getY() + r);
				retval[3] = new Point2D.Double(intersectionPoint.getX(),
						intersectionPoint.getY() + r + distance);
				retval[4] = new Point2D.Double(retval[0].getX() - distance,
						retval[0].getY() - distance);
				return retval;
			}
			line.setLine(coordinates[2], coordinates[3]);
			if (line.intersectsLine(segment)) {
				Point2D intersectionPoint = getIntersectionPoint(line, segment);
				retval[0] = new Point2D.Double(intersectionPoint.getX(),
						intersectionPoint.getY() - r / 2);
				retval[1] = new Point2D.Double(	intersectionPoint.getX() + r / 2, 
						intersectionPoint.getY());
				retval[2] = new Point2D.Double(intersectionPoint.getX() + r,
						intersectionPoint.getY());
				retval[3] = new Point2D.Double(intersectionPoint.getX() + r
						+ distance, intersectionPoint.getY());
				retval[4] = new Point2D.Double(retval[0].getX() - distance,
						retval[0].getY() - distance);
				return retval;
			}
			line.setLine(coordinates[3], coordinates[0]);
			if (line.intersectsLine(segment)) {
				Point2D intersectionPoint = getIntersectionPoint(line, segment);
				retval[0] = new Point2D.Double(intersectionPoint.getX() - r / 2,
						intersectionPoint.getY()- r);
				retval[1] = new Point2D.Double(intersectionPoint.getX(),
						intersectionPoint.getY() - r / 2);
				retval[2] = new Point2D.Double(intersectionPoint.getX(),
						intersectionPoint.getY() - r);
				retval[3] = new Point2D.Double(intersectionPoint.getX(),
						intersectionPoint.getY() - r - distance);
				retval[4] = new Point2D.Double(retval[0].getX() - distance,
						retval[0].getY() - distance);
				return retval;
			}
		}

		return null;
	}

	/**
	 * Used to draw source and destination roles and cardinality
	 * @param textWidth
	 * @param textHeight
	 * @param type - source or destination
	 * @param link
	 * @param xDistance - distance between the x coordinates of firstPoint and secondPoint and the desired cardinality location
	 * @param yDistance - distance between the y coordinates of firstPoint and secondPoint and the desired cardinality location
	 * @param firstPoint - first point contained by link's segment close to which the text will be drawn 
	 * @param secondPoint - second point contained by link's segment close to which the text will be drawn 
	 * @param firstElementPoint - vertex
	 * @param secondElementPoint - vertex
	 * @return text position
	 * @author tim1
	 */
	public static Point2D getCardinalityPosition(int textWidth, int textHeight,
			int type, Link link, int xDistance, int yDistance, Point2D firstPoint,
			Point2D secondPoint, Point2D firstElementPoint, Point2D secondElementPoint) {

		int i;

		if (firstElementPoint!=null && secondElementPoint!=null)
			i=findSide(firstElementPoint,secondElementPoint,type,link);
		else
			i=side(segment, type, link);
		if (i == RIGHT)
			if (firstPoint.getY() < secondPoint.getY())
				return new Point2D.Double(firstPoint.getX() + xDistance,
						firstPoint.getY() -yDistance);
			else
				return new Point2D.Double(firstPoint.getX() + xDistance,
						firstPoint.getY() + textHeight);
		if (i == LEFT)
			if (firstPoint.getY() < secondPoint.getY())
				return new Point2D.Double(firstPoint.getX() - xDistance
						- textWidth, firstPoint.getY() - yDistance);
			else
				return new Point2D.Double(firstPoint.getX() - xDistance
						- textWidth, firstPoint.getY() + textHeight);
		if (i == UPPER)
			if (firstPoint.getX() < secondPoint.getX())
				return new Point2D.Double(firstPoint.getX() - textWidth-xDistance,
						firstPoint.getY() - yDistance);
			else
				return new Point2D.Double(firstPoint.getX() + xDistance,
						firstPoint.getY() - yDistance);
		if (i == BOTTOM)
			if (firstPoint.getX() < secondPoint.getX())
				return new Point2D.Double(firstPoint.getX() - textWidth-xDistance,
						firstPoint.getY() + textHeight);
			else
				return new Point2D.Double(firstPoint.getX() + xDistance,
						firstPoint.getY() + textHeight);
		return null;
	}
	/**
	 * Calculates the angle that will be used to draw an arc, which is a part of the symbol for require link
	 * @param minAngle
	 * @param type - destination or source 
	 * @param link
	 * @return angle in degrees
	 * @author tim1
	 */
	public static double getArcStart(double minAngle, int type, Link link) {

		init(type, link);
		int i = side(segment, type, link);
		if (i == RIGHT)
			return minAngle;
		if (i == UPPER)
			return minAngle + 90;
		if (i == LEFT)
			return minAngle + 180;
		return minAngle + 270;
	}

	/**
	 * Calculates the angle that the first or the last segment of the link forms with x, or y axis
	 * @param type
	 * @param link
	 * @return rotation angle in radians
	 * @author tim1
	 */
	public static double getRotationAngle(int type, Link link) {

		init(type, link);
		double theta = 0;
		int i = side(segment, type, link);

		if (i == LEFT)
			theta = Math.atan((p2.getY() - p1.getY()) / (p2.getX() - p1.getX()));
		else if (i == RIGHT)
			theta = Math.atan((p2.getY() - p1.getY()) / (p2.getX() - p1.getX()));
		else if (i == BOTTOM)
			theta = Math.atan((p2.getX() - p1.getX()) / (p1.getY() - p2.getY()));
		else if (i == UPPER)
			theta = Math.atan((p1.getX() - p2.getX()) / (p2.getY() - p1.getY()));

		return theta;
	}

	/**
	 * Used to calculate locations of points that are used when drawing arrows
	 * @param type - source or destination
	 * @param link
	 * @param angle - angle between lines that form the arrow
	 * @param length - length of the lines that form the arrow
	 * @return vertices of the two lines that form the arrow
	 */
	public static Point2D[] getPoints(int type, Link link, double angle,
			double length) {

		double theta = 0;
		double fi = 0;
		double x1 = 0;
		double y1 = 0;
		double x2 = 0;
		double y2 = 0;
		double dist = 0;
		Point2D[] retval = new Point2D[2];

		Point2D intersectionPoint = intersection(type, link);

		if (p1.getY() > p2.getY()) {
			if (p1.getX() != p2.getX())
				theta = Math.atan((p1.getY() - p2.getY())
						/ Math.abs((p2.getX() - p1.getX())));
			else
				theta = Math.PI / 2;
			fi = Math.PI / 2 - theta - angle;
			x1 = Math.sin(fi) * length;
			y1 = Math.cos(fi) * length;

			if (p1.getX() < p2.getX()) {
				x1 = intersectionPoint.getX() - x1;
				y1 = intersectionPoint.getY() + y1;
				dist = Math.abs(segment.ptLineDist(x1, y1)) * 2;
				x2 = Math.sin(theta) * dist;
				y2 = Math.cos(theta) * dist;
				x2 = x1 - x2;
				y2 = y1 - y2;
			} else {
				x1 = intersectionPoint.getX() + x1;
				y1 = intersectionPoint.getY() + y1;
				dist = Math.abs(segment.ptLineDist(x1, y1)) * 2;
				x2 = Math.sin(theta) * dist;
				y2 = Math.cos(theta) * dist;
				x2 = x1 + x2;
				y2 = y1 - y2;
			}
		} else {
			if (p1.getY() != p2.getY())
				theta = Math.atan(Math.abs((p1.getX() - p2.getX()))
						/ (p2.getY() - p1.getY()));
			else
				theta = Math.PI / 2;
			fi = Math.PI / 2 - theta - angle;
			x1 = Math.cos(fi) * length;
			y1 = Math.sin(fi) * length;
			if (p1.getX() < p2.getX()) {
				x1 = intersectionPoint.getX() - x1;
				y1 = intersectionPoint.getY() - y1;
				dist = Math.abs(segment.ptLineDist(x1, y1)) * 2;
				x2 = Math.cos(theta) * dist;
				y2 = Math.sin(theta) * dist;
				x2 = x1 + x2;
				y2 = y1 - y2;
			} else {
				x1 = intersectionPoint.getX() + x1;
				y1 = intersectionPoint.getY() - y1;
				dist = Math.abs(segment.ptLineDist(x1, y1)) * 2;
				x2 = Math.cos(theta) * dist;
				y2 = Math.sin(theta) * dist;
				x2 = x1 - x2;
				y2 = y1 - y2;
			}
		}
		retval[0] = new Point2D.Double(x1, y1);
		retval[1] = new Point2D.Double(x2, y2);
		return retval;
	}

	/**
	 * 
	 * @param c represents the class, which is to be drawn
	 * @param fontMetrics obtains necessary metrics according to font set used
	 * @return result[0] - optimal width, result[1] - optimal class height,
	 * result[2] - optimal attributes height, result[3] - optimal methods height
	 * @author tim1
	 */
	@SuppressWarnings("unchecked")
	public static double[] getOptimalMeasures(Class c, FontMetrics fontMetrics, boolean isShortcut, String shortcutInfo) {
		List<Attribute> attributes = (List<Attribute>) c.getProperty(GraphElementProperties.ATTRIBUTES);
		List<Method> methods = (List<Method>) c.getProperty(GraphElementProperties.METHODS);
		List<Attribute> visibleAttributes = new ArrayList<Attribute>();
		for (Attribute a : attributes)
			if (a.isVisible())
				visibleAttributes.add(a);

		List<Method> visibleMethods = new ArrayList<Method>();
		for (Method m : methods)
			if (m.isVisible())
				visibleMethods.add(m);

		attributes = visibleAttributes;
		methods = visibleMethods;


		double treshold = 2.0;
		double[] result = { 0, 0, 0, 0 };
		double fontHeight = fontMetrics.getHeight();
		String stereotype = "<<" + (String) c.getProperty(GraphElementProperties.STEREOTYPE) + ">>";



		result[1] = fontHeight + 2 * treshold;
		if (!stereotype.equals(""))
			result[1] += fontHeight + treshold;
		if (!shortcutInfo.equals(""))
			result[1] += fontHeight + treshold;

		//result[1] = stereotype.equals("") ? fontHeight + 2 * treshold : 2 * (treshold + fontHeight) + treshold; 
		result[2] = attributes.size() > 0 ? attributes.size() * (fontHeight + treshold) + treshold : fontHeight;
		result[3] = methods.size() > 0 ? methods.size() * (fontHeight + treshold) + treshold : fontHeight;

		double maxWidth = fontMetrics.stringWidth(stereotype);

		String className = "";
		if (isShortcut)
			className = "Shortcut to ";

		className += (String)c.getProperty(GraphElementProperties.NAME);

		if (className.length() > stereotype.length())
			maxWidth = fontMetrics.stringWidth((String)c.getProperty(GraphElementProperties.NAME));

		for (Attribute a : attributes)
			if (fontMetrics.stringWidth(a.toString()) > maxWidth) 
				maxWidth = fontMetrics.stringWidth(a.toString());

		for (Method m : methods)
			if (fontMetrics.stringWidth(m.toString()) > maxWidth) 
				maxWidth = fontMetrics.stringWidth(m.toString());

		double defaultClassWidth =  Double.parseDouble(Preferences.getInstance().getProperty(Preferences.CLASS_WIDTH));
		result[0] = maxWidth > defaultClassWidth ? (maxWidth + 25 * treshold) : defaultClassWidth;


		return result;
	}

	public static double positionDiff(Point2D p1, Point2D p2){
		return Math.hypot(Math.abs(p1.getX() - p2.getX()), Math.abs(p1.getY() - p2.getY()));
	}
}
