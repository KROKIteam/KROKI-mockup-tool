package kroki.mockup.view.painters;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;

import kroki.mockup.model.Component;
import kroki.mockup.model.Composite;
import kroki.mockup.view.GraphElement;

/**
 * Composite component painter
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class CompositePainter extends ComponentPainter {

    public CompositePainter(GraphElement graphElement) {
        super(graphElement);
    }

    public CompositePainter(GraphElement graphElement, Shape shape) {
        super(graphElement, shape);
    }

    @Override
    public boolean isComponentAt(Point p) {
        return shape.contains(p);
    }

    @Override
    public Component getComponentAt(Point p) {
        Composite cc = (Composite) graphElement;
        if (isComponentAt(p)) {
            for (int i = 0; i < cc.getChildrenList().size(); i++) {
                Component c = cc.getChildrenList().get(i);
                Component inner = c.getElementPainter().getComponentAt(p);
                if (inner != null) {
                    return inner;
                }
            }
            return cc;
        }
        return null;
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void paint(Graphics g) {
        //super.paint(g);
        Composite cc = (Composite) graphElement;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setPaint(cc.getBgColor());
        g2d.fill(shape);

        int x = cc.getRelativePosition().x;
        int y = cc.getRelativePosition().y;
        int w = cc.getDimension().width;
        int h = cc.getDimension().height;
        cc.getBorder().paintBorder(cc, g);

        for (int i = 0; i < cc.getChildrenList().size(); i++) {
            Component c = cc.getChildrenList().get(i);
            Graphics2D newG = (Graphics2D) g.create(x, y, w, h);
            c.getElementPainter().paint(newG);
            newG.dispose();
        }
        //g.dispose();

    }
}
