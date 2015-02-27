/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.model.layout;

import java.awt.Dimension;

import kroki.mockup.model.Component;
import kroki.mockup.model.Composite;
import kroki.mockup.utils.KrokiGrid;

/**
 * Layout manager which enables manual placement of the components
 * Method which lays out the components calculates the relative position based on the absolute ones
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class FreeLayoutManager extends LayoutManager {

    @Override
    public void layoutComponent(Component c) {
        if (c instanceof Composite) {
            if (((Composite) c).getComponentCount() == 0) {
                return;
            }
            Composite composite = (Composite) c;
            int nmembers = composite.getComponentCount();
            for (int i = 0; i < nmembers; i++) {
                Component m = composite.getComponent(i);

                KrokiGrid.snapToGrid(m.getAbsolutePosition());
                KrokiGrid.snapToGrid(m.getRelativePosition());

                int posX = c.getAbsolutePosition().x + m.getRelativePosition().x;
                int posY = c.getAbsolutePosition().y + m.getRelativePosition().y;

                m.getAbsolutePosition().setLocation(posX, posY);
                m.updateComponent();
            }
            if (!composite.isLocked()) {
                composite.setDimension(composite.getPreferredSize());
                composite.updateComponent();
            }
        } else {
        }
    }

    @Override
    public Dimension preferredLayoutSize(Component c) {
        return c.getDimension();
    }

    @Override
    public Dimension minimumLayoutSize(Component c) {
        return new Dimension(50, 50);
    }


    @Override
    public Dimension maximumLayoutSize(Component c) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
}
