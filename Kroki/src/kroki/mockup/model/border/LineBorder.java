package kroki.mockup.model.border;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import kroki.mockup.model.Border;
import kroki.mockup.model.Component;
import kroki.mockup.model.Insets;

/**
 * Class represent an invisible border (it is not drawn)
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class LineBorder extends Border {

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public void paintBorder(Component c, Graphics g) {
        Rectangle2D bounds = c.getBoundsForRelativePosition();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setPaint(color);
        g2d.draw(bounds);
    }
}
