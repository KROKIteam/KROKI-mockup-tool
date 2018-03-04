package kroki.mockup.model.border;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import kroki.mockup.model.Border;
import kroki.mockup.model.Component;
import kroki.mockup.model.Insets;

/**
 * Class represents a titled border
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class TitledBorder extends Border {

    static public final int LEADING = 4;

    @Override
    public void paintBorder(Component c, Graphics g) {

        int x = c.getRelativePosition().x;
        int y = c.getRelativePosition().y;

        Rectangle2D bounds = c.getBoundsForRelativePosition();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setPaint(color);
        g2d.draw(bounds);

        String title = c.getName();

        if (title != null && !title.equals("")) {
            Font font = c.getFont();
            FontMetrics fm = g2d.getFontMetrics(font);
            int textWidth = fm.stringWidth(c.getName());
            int ascent = fm.getAscent();

            //ocisti onu crtu iza teksta!!!
            g2d.clearRect(x + LEADING, y - 1, textWidth, 2);

            //ispisi tekst
            g2d.setPaint(c.getFgColor());
            g2d.setFont(font);
            g2d.drawString(c.getName(), x + LEADING, y + ascent / 2);
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }
}
