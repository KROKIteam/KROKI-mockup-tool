package kroki.mockup.view.painters.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import kroki.mockup.model.Component;
import kroki.mockup.model.components.Button;
import kroki.mockup.utils.KrokiTextMeasurer;
import kroki.mockup.view.GraphElement;
import kroki.mockup.view.painters.ComponentPainter;

/**
 * Represents button painter
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class ButtonPainter extends ComponentPainter {

    public ButtonPainter(GraphElement graphElement) {
        super(graphElement);
        int x = ((Component) graphElement).getRelativePosition().x;
        int y = ((Component) graphElement).getRelativePosition().y;
        int w = ((Component) graphElement).getDimension().width;
        int h = ((Component) graphElement).getDimension().height;

        shape = new RoundRectangle2D.Float(x, y, w, h, 8, 8);
    }

    public ButtonPainter(GraphElement graphElement, Shape shape) {
        super(graphElement, shape);
    }

    @Override
    public void update() {
        int x = ((Component) graphElement).getRelativePosition().x;
        int y = ((Component) graphElement).getRelativePosition().y;
        int w = ((Component) graphElement).getDimension().width;
        int h = ((Component) graphElement).getDimension().height;

        shape = new RoundRectangle2D.Float(x, y, w, h, 8, 8);
    }

    @Override
    public boolean isComponentAt(Point p) {
        return shape.contains(p);
    }

    @Override
    public Component getComponentAt(Point p) {
        if (isComponentAt(p)) {
            return (Button) graphElement;
        }
        return null;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setPaint(Color.BLACK);
        Button button = (Button) graphElement;


        int x = button.getRelativePosition().x;
        int y = button.getRelativePosition().y;

        //GradientPaint gp = new GradientPaint(x, y, new Color(240, 240, 240), x, y + button.getDimension().height, new Color(210, 210, 210));
//        GradientPaint gp = new GradientPaint(x, y, Color.WHITE, x, y + button.getDimension().height, button.getBgColor());
//        g2d.setPaint(gp);
//        g2d.fill(shape);

        //g2d.setPaint(new Color(190, 190, 190));
        g2d.setPaint(button.getFgColor());
        g2d.draw(shape);

        int width = button.getDimension().width;
        int height = button.getDimension().height;

        int minWidth = button.getMinimumSize().width;
        int minHeigth = button.getMinimumSize().height;

        if (button.getImage() != null) {
            x += (width - minWidth) / 2;
            y += (height - minHeigth) / 2 + (minHeigth - button.getImage().getSource().getHeight()) / 2;
            g2d.drawImage(button.getImage().getSource(), x, y, null);
            x += button.getImage().getSource().getWidth();
        }

        //reset y
        y = button.getRelativePosition().y;

        if (button.getName() != null && !button.getName().equals("")) {
            Dimension nameDim = KrokiTextMeasurer.measureText(button.getName(), button.getFont());
            if (button.getImage() != null) {
                x += button.getGap();
            } else {
                x = button.getRelativePosition().x;
                x += (width - nameDim.width) / 2;
            }
            y += (height - nameDim.height) / 2 + nameDim.height - 2;

            g2d.setPaint(button.getFgColor());
            g2d.setFont(button.getFont());
            g2d.drawString(button.getName(), x, y);
        }
    }
}
