/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.utils;

import java.awt.Point;

/**
 * Klasa koja predstavlja grid. Poseduje metodu za poravnavanje komponenti sa gridom.
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
