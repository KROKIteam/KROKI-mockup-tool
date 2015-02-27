package kroki.mockup.view.painters.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;

import kroki.mockup.model.Component;
import kroki.mockup.model.components.CheckBox;
import kroki.mockup.utils.KrokiTextMeasurer;
import kroki.mockup.utils.SerializableBufferedImage;
import kroki.mockup.view.GraphElement;
import kroki.mockup.view.painters.ComponentPainter;

/**
 * Represents check box painter
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class CheckBoxPainter extends ComponentPainter {

    private SerializableBufferedImage checked;
    private SerializableBufferedImage unchecked;

    public CheckBoxPainter(GraphElement graphElement) {
        super(graphElement);
        loadImages();
    }

    public CheckBoxPainter(GraphElement graphElement, Shape shape) {
        super(graphElement, shape);
        loadImages();
    }

    private void loadImages() {
        checked = new SerializableBufferedImage("check-box-check");
        unchecked = new SerializableBufferedImage("check-box-uncheck");
    }

    @Override
    public boolean isComponentAt(Point p) {
        return shape.contains(p);
    }

    @Override
    public Component getComponentAt(Point p) {
        if (isComponentAt(p)) {
            return (CheckBox) graphElement;
        }
        return null;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        CheckBox checkBox = (CheckBox) graphElement;

        int x = checkBox.getRelativePosition().x;
        int y = checkBox.getRelativePosition().y;

        Dimension dim = checkBox.getDimension();
        int height = dim.height;


        Graphics2D checkBoxGraphics2D = checkBox.getImage().getSource().createGraphics();
        checkBoxGraphics2D.setBackground(new Color(0, 0, 0, 0));
        checkBoxGraphics2D.clearRect(0, 0, checkBox.getImage().getSource().getWidth(), checkBox.getImage().getSource().getHeight());

        if (checkBox.isChecked()) {
            checkBoxGraphics2D.drawImage(checked.getSource(), 0, 0, checkBox.getImage().getSource().getWidth(), checkBox.getImage().getSource().getHeight(), null);
        } else {
            checkBoxGraphics2D.drawImage(unchecked.getSource(), 0, 0, checkBox.getImage().getSource().getWidth(), checkBox.getImage().getSource().getHeight(), null);
        }
        checkBoxGraphics2D.dispose();

        x += checkBox.getInsets().left;
        y += (height - checkBox.getImage().getSource().getHeight()) / 2;
        g2d.drawImage(checkBox.getImage().getSource(), x, y, null);

        if (checkBox.getName() != null && !checkBox.getName().equals("")) {
            Dimension nameDim = KrokiTextMeasurer.measureText(checkBox.getName(), checkBox.getFont());
            x += checkBox.getImage().getSource().getWidth();
            x += checkBox.getGap();
            y = checkBox.getRelativePosition().y;
            y += (height - nameDim.height) / 2 + nameDim.height - 2;

            g2d.setColor(checkBox.getFgColor());
            g2d.setFont(checkBox.getFont());

            g2d.drawString(checkBox.getName(), x, y);
        }
    }
}
