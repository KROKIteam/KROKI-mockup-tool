package kroki.mockup.view.painters.components;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

import kroki.mockup.model.Insets;
import kroki.mockup.model.components.TitledContainer;
import kroki.mockup.view.GraphElement;
import kroki.mockup.view.painters.CompositePainter;

/**
 * Represents titled container painter
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class TitledContainerPainter extends CompositePainter {

    public TitledContainerPainter(GraphElement graphElement, Shape shape) {
        super(graphElement, shape);
    }

    public TitledContainerPainter(GraphElement graphElement) {
        super(graphElement);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        TitledContainer c = (TitledContainer) graphElement;
        int x = c.getRelativePosition().x;
        int y = c.getRelativePosition().y;

        Insets insets = c.getInsets();
        x += insets.left;
        y += insets.top - 10;
        g2d.setPaint(c.getFgColor());
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString(c.getName(), x, y);
    }
}
