package bp.util;

import java.awt.Point;

import bp.model.graphic.ActivityEventComponent;
import bp.model.graphic.BPComponent;

public class ActivityEventLocationHelper {

    public static final Integer X_EDGE = 0;
    public static final Integer Y_EDGE = 1;
    public static final Integer X_W_EDGE = 2;
    public static final Integer Y_H_EDGE = 3;

    public static Point getValidPoint(final BPComponent component, final ActivityEventComponent eventComponent,
            final Point point) {
        if (component == null || eventComponent == null || point == null) {
            return null;
        }

        final Integer x = component.getX();
        final Integer y = component.getY();
        final Integer w = component.getWidth();
        final Integer h = component.getHeight();

        if (point.x < x) {
            point.setLocation(x, point.y);
        } else if (point.x > x + w) {
            point.setLocation(x + w, point.y);
        }
        if (point.y < y) {
            point.setLocation(point.x, y);
        } else if (point.y > y + h) {
            point.setLocation(point.x, y + h);
        }

        Integer minValue = Integer.MAX_VALUE;
        Integer closestEdge = -1;

        if ((point.x - x) < minValue) {
            minValue = point.x - x;
            closestEdge = X_EDGE;
        }
        if ((point.y - y) < minValue) {
            minValue = point.y - y;
            closestEdge = Y_EDGE;
        }
        if ((x + w - point.x) < minValue) {
            minValue = x + w - point.x;
            closestEdge = X_W_EDGE;
        }
        if ((y + h - point.y) < minValue) {
            minValue = y + h - point.y;
            closestEdge = Y_H_EDGE;
        }

        if (closestEdge == X_EDGE) {
            return new Point(x - eventComponent.getWidth() / 2, point.y - eventComponent.getHeight() / 2);
        }
        if (closestEdge == Y_EDGE) {
            return new Point(point.x - eventComponent.getWidth() / 2, y - eventComponent.getHeight() / 2);
        }
        if (closestEdge == X_W_EDGE) {
            return new Point(x + w - eventComponent.getWidth() / 2, point.y - eventComponent.getHeight() / 2);
        }
        if (closestEdge == Y_H_EDGE) {
            return new Point(point.x - eventComponent.getWidth() / 2, y + h - eventComponent.getHeight() / 2);
        }

        return null;
    }
}
