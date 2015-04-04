package kroki.mockup.model.layout;

import java.awt.Dimension;
import java.awt.Point;

import kroki.mockup.model.Component;
import kroki.mockup.model.Composite;
import kroki.mockup.model.Insets;
import kroki.mockup.utils.KrokiTextMeasurer;

/**
 * Layout manager which arranges the components vertically
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class VerticalLayoutManager extends LayoutManager {

    public VerticalLayoutManager(int hgap, int vgap, int aligment) {
        super(hgap, vgap, aligment);
    }

    public VerticalLayoutManager() {
        super();
    }

    @Override
    public void layoutComponent(Component c) {
        Composite composite = (Composite) c;

        Point cP = c.getAbsolutePosition();
        int x = c.getInsets().left + hgap;
        int y = c.getInsets().top + vgap;
        int nmembers = composite.getComponentCount();
        for (int i = 0; i < nmembers; i++) {
            Component m = composite.getComponent(i);

            Dimension d = m.getPreferredSize();
            m.getDimension().setSize(d.width, d.height);

            int newX = x;
            if (align == 1) {
                newX = x;
            } else if (align == 2) {
                newX = x + (c.getDimension().width - m.getDimension().width) / 2 - hgap - c.getInsets().getLeft();
            } else if (align == 3) {
                newX = x + (c.getDimension().width - m.getDimension().width) - 2 * hgap - c.getInsets().getRight();
            }

            m.getRelativePosition().setLocation(newX, y);
            m.getAbsolutePosition().setLocation(cP.x + newX, cP.y + y);

            y += d.height + vgap;
            m.getElementPainter().update();
        }

    }

    @Override
    public Dimension preferredLayoutSize(Component c) {
        if (c instanceof Composite) {
            if (((Composite) c).getComponentCount() == 0) {
                return c.getDimension();
            }

            Dimension dim = new Dimension(0, 0);
            Composite composite = (Composite) c;
            int nmembers = composite.getComponentCount();
            for (int i = 0; i < nmembers; i++) {
                Component m = composite.getComponent(i);
                Dimension d = m.getPreferredSize();

                dim.width = Math.max(dim.width, d.width);

                if (i > 0) {
                    dim.height += vgap;
                }
                dim.height += d.height;
            }

            Insets insets = c.getInsets();
            dim.width += insets.getLeft() + insets.getRight() + hgap * 2;
            dim.height += insets.getTop() + insets.getBottom() + vgap * 2;

            dim.width = Math.max(KrokiTextMeasurer.measureText(c.getName(), c.getFont()).width + c.getInsets().left + c.getInsets().right, dim.width);
            return dim;
        } else {
            return c.getPreferredSize();
        }
    }

    @Override
    public Dimension minimumLayoutSize(Component c) {

        if (c instanceof Composite) {
            if (((Composite) c).getComponentCount() == 0) {
                return new Dimension(10, 10);
            }

            Dimension dim = new Dimension(0, 0);
            Composite composite = (Composite) c;
            int nmembers = composite.getComponentCount();
            for (int i = 0; i < nmembers; i++) {
                Component m = composite.getComponent(i);
                Dimension d = m.getPreferredSize();

                dim.width = Math.max(dim.width, d.width);

                if (i > 0) {
                    dim.height += vgap;
                }
                dim.height += d.height;
            }

            Insets insets = c.getInsets();
            dim.width += insets.getLeft() + insets.getRight() + hgap * 2;
            dim.height += insets.getTop() + insets.getBottom() + vgap * 2;

            dim.width = Math.max(KrokiTextMeasurer.measureText(c.getName(), c.getFont()).width + c.getInsets().left + c.getInsets().right, dim.width);
            return dim;
        } else {
            return c.getPreferredSize();
        }
    }

    @Override
    public Dimension maximumLayoutSize(Component c) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
}
