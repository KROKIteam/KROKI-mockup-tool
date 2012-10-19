/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.view.painters.components;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import kroki.mockup.model.Component;
import kroki.mockup.model.components.NullComponent;
import kroki.mockup.view.GraphElement;
import kroki.mockup.view.painters.ComponentPainter;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class NullComponentPainter extends ComponentPainter {

    public NullComponentPainter(GraphElement graphElement) {
        super(graphElement);
    }

    public NullComponentPainter(GraphElement graphElement, Shape shape) {
        super(graphElement, shape);
    }

    @Override
    public Component getComponentAt(Point p) {
        if (isComponentAt(p)) {
            return (NullComponent) graphElement;
        } else {
            return null;
        }
    }

    @Override
    public boolean isComponentAt(Point p) {
        return shape.contains(p);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        NullComponent nullComponent = (NullComponent) graphElement;
        int x = nullComponent.getRelativePosition().x;
        int y = nullComponent.getRelativePosition().y;

        x += nullComponent.getInsets().left;
        y += nullComponent.getDimension().height - nullComponent.getInsets().bottom;

        g2d.setPaint(nullComponent.getFgColor());
        g2d.drawString(nullComponent.getName(), x, y);

        g2d.setPaint(nullComponent.getFgColor());
        g2d.setStroke(new BasicStroke(nullComponent.getStrokeWidth()));
        g2d.draw(shape);
    }
}
