package kroki.mockup.model;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public abstract class Border implements Serializable {
    protected Color color = Color.BLACK;

    public abstract void paintBorder(Component c, Graphics g);

    public abstract Insets getBorderInsets(Component c);

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
