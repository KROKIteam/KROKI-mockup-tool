package kroki.mockup.utils;

import java.awt.Point;

/**
 * Represents a grid and contains a method which aligns the compoenents to the grid
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class KrokiGrid {

    private static final int cellWidth = 10;
    private static final int cellHeight = 10;
    private static final int half = 5;

    public static void snapToGrid(Point point) {
        int modX = point.x % cellWidth;
        int modY = point.y % cellHeight;

        if (modX >= half) {
            point.x += cellWidth - modX;
        } else {
            point.x -= modX;
        }
        if (modY >= half) {
            point.y += cellHeight - modY;
        } else {
            point.y -= modY;
        }
    }
}
