package kroki.mockup.model.layout;

import java.awt.Dimension;

import kroki.mockup.model.Component;
import kroki.mockup.model.Composite;
import kroki.mockup.model.Insets;
import kroki.mockup.utils.KrokiTextMeasurer;

/**
 * Layout manager which arranges the components horizontally
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class FlowLayoutManager extends LayoutManager {

    public FlowLayoutManager() {
        super();
    }

    public FlowLayoutManager(int hgap, int vgap, int alignment) {
        super(hgap, vgap, alignment);
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
                dim.height = Math.max(dim.height, d.height);
                if (i > 0) {
                    dim.width += hgap;
                }
                dim.width += d.width;
            }

            Insets insets = c.getInsets();
            dim.width += insets.getLeft() + insets.getRight() + hgap * 2;
            dim.height += insets.getTop() + insets.getBottom() + vgap * 2;

            //dodato da bi se sprecilo da komponeta ima manje dimenzije nego sto je to propisano njenim naslovom (ili predefinisanim stanjem)

            dim.width = Math.max(KrokiTextMeasurer.measureText(c.getName(), c.getFont()).width + c.getInsets().left + c.getInsets().right, dim.width);

            return dim;
        } else {
            return c.getPreferredSize();
        }
    }

    public void layoutComponent(Component c) {
        Composite composite = (Composite) c;

        Insets insets = composite.getInsets();
        int maxwidth = composite.getDimension().width
                - (insets.left + insets.right + hgap * 2);
        int nmembers = composite.getComponentCount();
        int x = 0, y = insets.top + vgap;
        int rowh = 0, start = 0;
        for (int i = 0; i < nmembers; i++) {
            Component m = composite.getComponent(i);
            //if (m.visible) {
            Dimension d = m.getPreferredSize();
            m.getDimension().setSize(d.width, d.height);
            if ((x == 0) || ((x + d.width) <= maxwidth)) {
                if (x > 0) {
                    x += hgap;
                }
                x += d.width;
                rowh = Math.max(rowh, d.height);
            } else {
                moveComponents(composite, insets.left + hgap, y,
                        maxwidth - x, rowh, start, i);
                x = d.width;
                y += vgap + rowh;
                rowh = d.height;
                start = i;
            }
            //}
        }
        moveComponents(composite, insets.left + hgap, y, maxwidth - x,
                rowh, start, nmembers);

    }

    @Override
    public Dimension minimumLayoutSize(Component c) {
        return preferredLayoutSize(c);
    }


    @Override
    public Dimension maximumLayoutSize(Component c) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Centrira komponente u redove.
     * @param c komponenta
     * @param x pozicija x
     * @param y pozicija y
     * @param width širina
     * @param height visina
     * @param rowStart početak reda
     * @param rowEnd kraj reda
     */
    private void moveComponents(Component c, int x, int y, int width, int height, int rowStart, int rowEnd) {

        Composite composite = (Composite) c;
        int startX = c.getAbsolutePosition().x;
        int startY = c.getAbsolutePosition().y;
        switch (align) {
            case LEFT:
                break;
            case CENTER:
                x += width / 2;
                break;
            case RIGHT:
                x += width;
                break;
        }
        for (int i = rowStart; i < rowEnd; i++) {
            Component m = composite.getComponent(i);
            //if (m.visible) {
            m.getAbsolutePosition().setLocation(startX + x, startY + y + (height - m.getDimension().height) / 2);
            m.getRelativePosition().setLocation(x, y + (height - m.getDimension().height) / 2);
            m.getElementPainter().update();
            x += hgap + m.getDimension().width;
            //}
        }
        c.getElementPainter().update();
    }
}
