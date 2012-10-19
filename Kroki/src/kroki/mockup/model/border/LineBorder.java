/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.model.border;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import kroki.mockup.model.Border;
import kroki.mockup.model.Component;
import kroki.mockup.model.Insets;

/**
 * Klasa koja predstavlja okvir koji se ne vidi (ne iscrtava se nista)
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
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
