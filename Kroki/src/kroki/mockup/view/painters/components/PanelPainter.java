/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.view.painters.components;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import kroki.mockup.model.Component;
import kroki.mockup.model.components.Panel;
import kroki.mockup.view.GraphElement;
import kroki.mockup.view.painters.CompositePainter;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class PanelPainter extends CompositePainter {

    public PanelPainter(GraphElement graphElement) {
        super(graphElement);
    }

    public PanelPainter(GraphElement graphElement, Shape shape) {
        super(graphElement, shape);
    }

    @Override
    public boolean isComponentAt(Point p) {
        return shape.contains(p);
    }

    @Override
    public Component getComponentAt(Point p) {
        if (isComponentAt(p)) {
            return (Panel) graphElement;
        }
        return null;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
