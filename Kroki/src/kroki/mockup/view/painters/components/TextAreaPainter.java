/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.view.painters.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import kroki.mockup.model.Component;
import kroki.mockup.model.components.TextArea;
import kroki.mockup.view.GraphElement;
import kroki.mockup.view.painters.ComponentPainter;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class TextAreaPainter extends ComponentPainter {

    public TextAreaPainter(GraphElement graphElement) {
        super(graphElement);
    }

    public TextAreaPainter(GraphElement graphElement, Shape shape) {
        super(graphElement, shape);
    }

    @Override
    public boolean isComponentAt(Point p) {
        return shape.contains(p);
    }

    @Override
    public Component getComponentAt(Point p) {
        if (isComponentAt(p)) {
            return (TextArea) graphElement;
        }
        return null;
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        TextArea textArea = (TextArea) graphElement;
        Dimension fieldDim = textArea.getFieldDim();
        Dimension labelDim = textArea.getLabelDim();

        int x = textArea.getRelativePosition().x;
        int y = textArea.getRelativePosition().y;

        x += textArea.getInsets().left;
        x += textArea.getBlank();
        if (textArea.getName() != null && !textArea.getName().equals("")) {
            y += labelDim.height - 2;
            g2d.setFont(textArea.getFont());
            g2d.setPaint(textArea.getFgColor());
            g2d.drawString(textArea.getName(), x, y);
            x += labelDim.width - textArea.getBlank();
        }

        y = textArea.getRelativePosition().y + textArea.getInsets().top;
        Rectangle2D box = new Rectangle2D.Float(x, y, fieldDim.width, fieldDim.height);

        if (textArea.isEnabled()) {
            g2d.setPaint(Color.WHITE);
        } else {
            g2d.setPaint(new Color(240,240,240));
        }
        g2d.fill(box);

        g2d.setPaint(Color.GRAY);
        g2d.draw(box);
    }
}
