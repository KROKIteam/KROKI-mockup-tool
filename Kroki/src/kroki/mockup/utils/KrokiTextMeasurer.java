package kroki.mockup.utils;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**Utils class which contains methods for measuring the width and height of the text
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class KrokiTextMeasurer {

    /**
     * Return size of a text if the selected font is used
     * @param text 
     * @param font 
     * @return size of text
     */
    public static Dimension measureText(String text, Font font) {
        Graphics2D g = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).createGraphics();
        g.setFont(font);
        FontMetrics m = g.getFontMetrics();
        int w = m.stringWidth(text);
        int h = m.getHeight();
        Dimension dim = new Dimension(w, h);
        return dim;
    }

    public static FontMetrics getFontMetrics(Font font) {
        Graphics2D g = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).createGraphics();
        if(font!=null){
            g.setFont(font);
        }
        FontMetrics m = g.getFontMetrics();
        return m;
    }

    public static int getWidth(int columns) {
        AffineTransform af = new AffineTransform();
        FontRenderContext fr = new FontRenderContext(af, true, true);
        //TODO: Izbaciti font i prebaciti ga u neku drugu komp.
        Font f = new Font("Arial", 0, 13);
        double width = f.getStringBounds("M", fr).getWidth();
        return (int) width * columns;
    }

    public static int getHeight(int rows) {
        AffineTransform af = new AffineTransform();
        FontRenderContext fr = new FontRenderContext(af, true, true);
        //TODO: Izbaciti font i prebaciti ga u neku drugu komp.
        Font f = new Font("Arial", 0, 13);
        double height = f.getStringBounds("M", fr).getHeight();
        return (int) height * rows;
    }
}
