package kroki.mockup.view.painters.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;

import kroki.mockup.model.Component;
import kroki.mockup.model.components.RadioButton;
import kroki.mockup.utils.KrokiTextMeasurer;
import kroki.mockup.utils.SerializableBufferedImage;
import kroki.mockup.view.GraphElement;
import kroki.mockup.view.painters.ComponentPainter;

/**
 * Represents radio button painter
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class RadioButtonPainter extends ComponentPainter {

    private SerializableBufferedImage checked;
    private SerializableBufferedImage unchecked;

    public RadioButtonPainter(GraphElement graphElement) {
        super(graphElement);
        loadImages();
    }

    public RadioButtonPainter(GraphElement graphElement, Shape shape) {
        super(graphElement, shape);
        loadImages();
    }

    private void loadImages() {
        checked = new SerializableBufferedImage("radio-check");
        unchecked = new SerializableBufferedImage("radio-uncheck");
    }

    @Override
    public boolean isComponentAt(Point p) {
        return shape.contains(p);
    }

    @Override
    public Component getComponentAt(Point p) {
        if (isComponentAt(p)) {
            return (RadioButton) graphElement;
        }
        return null;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        RadioButton radioButton = (RadioButton) graphElement;

        int x = radioButton.getRelativePosition().x;
        int y = radioButton.getRelativePosition().y;

        Dimension dim = radioButton.getDimension();
        int height = dim.height;

        Graphics2D radioButtonGraphics2D = radioButton.getImage().getSource().createGraphics();
        radioButtonGraphics2D.setBackground(new Color(0, 0, 0, 0));
        radioButtonGraphics2D.clearRect(0, 0, radioButton.getImage().getSource().getWidth(), radioButton.getImage().getSource().getHeight());
        if (radioButton.isChecked()) {
            radioButtonGraphics2D.drawImage(checked.getSource(), 0, 0, radioButton.getImage().getSource().getWidth(), radioButton.getImage().getSource().getHeight(), null);
        } else {
            radioButtonGraphics2D.drawImage(unchecked.getSource(), 0, 0, radioButton.getImage().getSource().getWidth(), radioButton.getImage().getSource().getHeight(), null);
        }
        radioButtonGraphics2D.dispose();


        x += radioButton.getInsets().left;
        y += (height - radioButton.getImage().getSource().getHeight()) / 2;

        g2d.drawImage(radioButton.getImage().getSource(), x, y, null);

        if (radioButton.getName() != null && !radioButton.getName().equals("")) {
            Dimension nameDim = KrokiTextMeasurer.measureText(radioButton.getName(), radioButton.getFont());
            x += radioButton.getImage().getSource().getWidth();
            x += radioButton.getGap();
            y = radioButton.getRelativePosition().y;
            y += (height - nameDim.height) / 2 + nameDim.height - 2;

            g2d.setFont(radioButton.getFont());
            g2d.setColor(radioButton.getFgColor());

            g2d.drawString(radioButton.getName(), x, y);
        }
    }
}
