package bp.util;

import java.awt.Point;

public class PointHelper {

    /**
     * 
     * @param possiblePoints
     *            - points that can be considered, must be at leats one
     * @param x
     *            - current x coordinate
     * @param y
     *            - current y coordinate
     * @param eX
     *            - top left x coordinate of element
     * @param eY
     *            - top left y coordinate of element
     * @param eW
     *            - width of element
     * @param eH
     *            - height of element
     * @return
     */
    public static Point findClosestPoint(final EdgePoints[] possiblePoints, final Integer x, final Integer y, final Integer eX, final Integer eY,
            final Integer eW, final Integer eH) {
        if (possiblePoints == null || possiblePoints.length == 0) {
            return null;
        }
        if (intNullOrNegative(x) || intNullOrNegative(y) || intNullOrNegative(eX) || intNullOrNegative(eY)
                || intNullOrNegative(eW) || intNullOrNegative(eH)) {
            return null;
        }

        final Point currentPoint = new Point(x, y);

        Integer northDistance = null;
        Integer southDistance = null;
        Integer eastDistance = null;
        Integer westDistance = null;

        Point northPoint = null;
        Point southPoint = null;
        Point eastPoint = null;
        Point westPoint = null;

        if (hasPointType(possiblePoints, EdgePoints.NORTH)) {
            northPoint = getNorthPoint(eX, eY, eW, eH);
            final Double nDistance = currentPoint.distance(northPoint);
            northDistance = nDistance.intValue();
        }
        if (hasPointType(possiblePoints, EdgePoints.SOUTH)) {
            southPoint = getSouthPoint(eX, eY, eW, eH);
            final Double sDistance = currentPoint.distance(southPoint);
            southDistance = sDistance.intValue();
        }
        if (hasPointType(possiblePoints, EdgePoints.EAST)) {
            eastPoint = getEastPoint(eX, eY, eW, eH);
            final Double eDistance = currentPoint.distance(eastPoint);
            eastDistance = eDistance.intValue();
        }
        if (hasPointType(possiblePoints, EdgePoints.WEST)) {
            westPoint = getWestPoint(eX, eY, eW, eH);
            final Double wDistance = currentPoint.distance(westPoint);
            westDistance = wDistance.intValue();
        }

        Integer min = null;
        Point minPoint = null;

        if (northDistance != null) {
            min = northDistance;
            minPoint = northPoint;
        }

        if (southDistance != null) {
            if (min != null) {
                if (southDistance < min) {
                    min = southDistance;
                    minPoint = southPoint;
                }
            } else {
                min = southDistance;
                minPoint = southPoint;
            }
        }

        if (eastDistance != null) {
            if (min != null) {
                if (eastDistance < min) {
                    min = eastDistance;
                    minPoint = eastPoint;
                }
            } else {
                min = eastDistance;
                minPoint = eastPoint;
            }
        }

        if (westDistance != null) {
            if (min != null) {
                if (westDistance < min) {
                    min = westDistance;
                    minPoint = westPoint;
                }
            } else {
                min = westDistance;
                minPoint = westPoint;
            }
        }

        return minPoint;
    }

    private static boolean intNullOrNegative(final Integer x) {
        if (x == null || x < 0)
            return true;

        return false;
    }

    private static Point getNorthPoint(final Integer x, final Integer y, final Integer w, final Integer h) {
        return new Point(x + w / 2, y);
    }

    private static Point getEastPoint(final Integer x, final Integer y, final Integer w, final Integer h) {
        return new Point(x + w, y + h / 2);
    }

    private static Point getSouthPoint(final Integer x, final Integer y, final Integer w, final Integer h) {
        return new Point(x + w / 2, y + h);
    }

    private static Point getWestPoint(final Integer x, final Integer y, final Integer w, final Integer h) {
        return new Point(x, y + h / 2);
    }

    private static boolean hasPointType(final EdgePoints[] points, final EdgePoints type) {
        for (final EdgePoints p : points) {
            if (p.equals(type))
                return true;
        }
        return false;
    }
}
