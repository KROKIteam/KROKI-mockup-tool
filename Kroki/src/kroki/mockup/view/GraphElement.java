/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.view;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import kroki.mockup.view.painters.ElementPainter;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class GraphElement implements Serializable {

    /**Painter grafičkog elementa*/
    protected ElementPainter elementPainter;
    /**Boja podzadine*/
    protected Color bgColor = new Color(0, 0, 0, 0);
    /**Boja teksta, okvira*/
    protected Color fgColor = Color.BLACK;
    /**Font teksta*/
    protected Font font = new Font("Arial", Font.PLAIN, 13);
    /**Debljina linije*/
    protected float strokeWidth = 1;

    /**
     * Podrazumevani konstruktor grafičkog elementa.
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
