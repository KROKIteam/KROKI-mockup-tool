package kroki.mockup.view.painters.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import kroki.mockup.model.Component;
import kroki.mockup.model.components.ComboBox;
import kroki.mockup.utils.SerializableBufferedImage;
import kroki.mockup.view.GraphElement;
import kroki.mockup.view.painters.ComponentPainter;

/**
 * Represents combobox painter
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class ComboBoxPainter extends ComponentPainter {

    private SerializableBufferedImage downArrow;

    public ComboBoxPainter(GraphElement graphElement) {
        super(graphElement);
        loadImage();
    }

    public ComboBoxPainter(GraphElement graphElement, Shape shape) {
        super(graphElement, shape);
        loadImage();
    }

    @Override
    public boolean isComponentAt(Point p) {
        return shape.contains(p);
    }

    @Override
    public Component getComponentAt(Point p) {
        if (isComponentAt(p)) {
            return (ComboBox) graphElement;
        }
        return null;
    }

    private void loadImage() {
        downArrow = new SerializableBufferedImage("control-small");
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
        ComboBox comboBox = (ComboBox) graphElement;

        Dimension dim = comboBox.getDimension();
        Dimension fieldDim = comboBox.getFieldDim();
        Dimension labelDim = comboBox.getLabelDim();

        int x = comboBox.getRelativePosition().x;
        int y = comboBox.getRelativePosition().y;

        x += comboBox.getInsets().left;
        x += comboBox.getBlank();
        if (comboBox.getName() != null && !comboBox.getName().equals("")) {
            y += (dim.height - labelDim.height) / 2 + labelDim.height - 2;
            g2d.setFont(comboBox.getFont());
            g2d.setPaint(comboBox.getFgColor());
            g2d.drawString(comboBox.getName(), x, y);
            x += labelDim.width - comboBox.getBlank();
        }

        y = comboBox.getRelativePosition().y + comboBox.getInsets().top;
        Rectangle2D box = new Rectangle2D.Float(x, y, fieldDim.width, fieldDim.height);
        g2d.setPaint(Color.WHITE);
        g2d.fill(box);

        g2d.setPaint(Color.GRAY);
        g2d.draw(box);

        //iscrtavanje slike
        x += (fieldDim.width - downArrow.getSource().getWidth());
        y = comboBox.getRelativePosition().y + (dim.height - downArrow.getSource().getHeight()) / 2;

        g2d.drawImage(downArrow.getSource(), x, y, null);
        //iscrtavanje dugmeta
        if (comboBox.isZoom()) {

            Dimension zoomDim = comboBox.getZoomDim();

            x += downArrow.getSource().getWidth() + comboBox.getInsets().right;
            y = comboBox.getRelativePosition().y + (dim.height - zoomDim.height) / 2;


            RoundRectangle2D button = new RoundRectangle2D.Float(x, y, zoomDim.width, zoomDim.height, 5, 5);

            GradientPaint gp = new GradientPaint(x, y, Color.WHITE, x, y + comboBox.getZoomDim().height, Color.LIGHT_GRAY);
            g2d.setPaint(gp);
            g2d.fill(button);

            g2d.setPaint(Color.GRAY);
            g2d.draw(button);

            x += comboBox.getMargins().getLeft();
            y += zoomDim.height - comboBox.getMargins().getBottom() - 2;

            g2d.setPaint(Color.BLACK);
            g2d.drawString("...", x, y);

        }
    }
}
