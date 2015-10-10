package kroki.mockup.view.painters.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import kroki.mockup.model.Component;
import kroki.mockup.model.components.Link;
import kroki.mockup.view.GraphElement;
import kroki.mockup.view.painters.ComponentPainter;

/**
 * Represents link painter
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class LinkPainter extends ComponentPainter {

    public LinkPainter(GraphElement graphElement) {
        super(graphElement);
    }

    public LinkPainter(GraphElement graphElement, Shape shape) {
        super(graphElement, shape);
    }

    @Override
    public Component getComponentAt(Point p) {
        if (isComponentAt(p)) {
            return (Link) graphElement;
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        Link link = (Link) graphElement;
        int x = link.getRelativePosition().x;
        int y = link.getRelativePosition().y;

        x += link.getInsets().left;
        y += link.getDimension().height - link.getInsets().bottom;

        AttributedString as = new AttributedString(link.getName());
        as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        as.addAttribute(TextAttribute.FONT, link.getFont());

        g2d.setPaint(link.getFgColor());
        g2d.drawString(as.getIterator(), x, y);
    }
}
