/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import kroki.profil.VisibleElement;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class HandleManager {

    public static int w = 10;

    /**
     * 
     * @param visibleElement
     * @param point
     * @return
     */
    public static Handle getHandleForPoint(VisibleElement visibleElement, Point point) {
        if (visibleElement != null && point != null) {
            for (Handle h : Handle.values()) {
                if (isPointInHandle(visibleElement, h, point)) {
                    return h;
                }
            }
        }
        return null;
    }

    /**
     *
     * 
     * @param visibleElement
     * @param handle
     * @param point
     * @return
     */
    public static boolean isPointInHandle(VisibleElement visibleElement, Handle handle, Point point) {
        Point center = getHandlePosition(visibleElement, handle);
        int x = center.x - w / 2;
        int y = center.y - w / 2;
        Rectangle2D rec = new Rectangle2D.Float(x, y, w, w);
        return rec.contains(point);
    }

    /**
     * 
     * @param visibleElement
     * @param handle
     * @return
     */
    public static Point getHandlePosition(VisibleElement visibleElement, Handle handle) {
        Point topLeft = visibleElement.getComponent().getAbsolutePosition();
        Dimension size = visibleElement.getComponent().getDimension();
        int x = 0, y = 0;

        //za y
        if (handle == Handle.NorthWest || handle == Handle.North || handle == Handle.NorthEast) {
            y = topLeft.y;
        }
        if (handle == Handle.East || handle == Handle.West) {
            y = topLeft.y + size.height / 2;
        }
        if (handle == Handle.SouthWest || handle == Handle.South || handle == Handle.SouthEast) {
            y = topLeft.y + size.height;
        }

        //za x
        if (handle == Handle.NorthWest || handle == Handle.West || handle == Handle.SouthWest) {
            x = topLeft.x;
        }

        if (handle == Handle.North || handle == Handle.South) {
            x = topLeft.x + size.width / 2;
        }

        if (handle == Handle.NorthEast || handle == Handle.East || handle == Handle.SouthEast) {
            x = topLeft.x + size.width;
        }
        Point p = new Point(x, y);
        return p;
    }
}
