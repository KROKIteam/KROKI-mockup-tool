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
import kroki.mockup.model.components.TextField;
import kroki.mockup.view.GraphElement;
import kroki.mockup.view.painters.ComponentPainter;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class TextFieldPainter extends ComponentPainter {

    public TextFieldPainter(GraphElement graphElement) {
        super(graphElement);
    }

    public TextFieldPainter(GraphElement graphElement, Shape shape) {
        super(graphElement, shape);
    }

    @Override
    public boolean isComponentAt(Point p) {
        return shape.contains(p);
    }

    @Override
    public Component getComponentAt(Point p) {
        if (isComponentAt(p)) {
            return (TextField) graphElement;
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
        TextField textField = (TextField) graphElement;
        Dimension dim = textField.getDimension();
        Dimension labelDim = textField.getLabelDim();
        Dimension fieldDim = textField.getFieldDim();

        int x = textField.getRelativePosition().x;
        int y = textField.getRelativePosition().y;
        
        x += textField.getInsets().left;
        x += textField.getBlank();
        if (textField.getName() != null && !textField.getName().equals("")) {
            y += (dim.height - labelDim.height) / 2 + labelDim.height - 2;
            g2d.setFont(textField.getFont());
            g2d.setPaint(textField.getFgColor());
            g2d.drawString(textField.getName(), x, y);
            x += labelDim.width - textField.getBlank();
        }

        y = textField.getRelativePosition().y + textField.getInsets().top;
        Rectangle2D box = new Rectangle2D.Float(x, y, fieldDim.width, fieldDim.height);
        if (textField.isEnabled()) {
            g2d.setPaint(Color.WHITE);
        } else {
            g2d.setPaint(new Color(240, 240, 240));
        }
        g2d.fill(box);
        g2d.setPaint(Color.GRAY);
        g2d.draw(box);
    }
}
