package kroki.mockup.view;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

import kroki.mockup.view.painters.ElementPainter;

/**
 * Graphical element of the mockup editor
 * Contains the element painter and other visual properties, such a foreground and background color
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class GraphElement implements Serializable {

    /**The elements painter*/
    protected ElementPainter elementPainter;
    /**Background color*/
    protected Color bgColor = new Color(0, 0, 0, 0);
    /**Text and border color*/
    protected Color fgColor = Color.BLACK;
    /**Font*/
    protected Font font = new Font("Arial", Font.PLAIN, 13);
    /**Stroke width*/
    protected float strokeWidth = 1;

    /**
     * Default constructor
     */
    public GraphElement() {
    }

    public ElementPainter getElementPainter() {
        return elementPainter;
    }

    public void setElementPainter(ElementPainter elementPainter) {
        this.elementPainter = elementPainter;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public Color getFgColor() {
        return fgColor;
    }

    public void setFgColor(Color fgColor) {
        this.fgColor = fgColor;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }
}
