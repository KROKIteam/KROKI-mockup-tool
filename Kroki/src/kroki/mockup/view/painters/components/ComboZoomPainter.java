/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.view.painters.components;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import kroki.mockup.model.Component;
import kroki.mockup.model.components.ComboZoom;
import kroki.mockup.view.GraphElement;
import kroki.mockup.view.painters.CompositePainter;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ComboZoomPainter extends CompositePainter {

    public ComboZoomPainter(GraphElement graphElement) {
        super(graphElement);
    }

    public ComboZoomPainter(GraphElement graphElement, Shape shape) {
        super(graphElement, shape);
    }

    @Override
    public boolean isComponentAt(Point p) {
        ComboZoom comboZoom = (ComboZoom) graphElement;
        return comboZoom.getElementPainter().isComponentAt(p);
    }

    @Override
    public Component getComponentAt(Point p) {
        ComboZoom comboZoom = (ComboZoom) graphElement;
        if (isComponentAt(p)) {
            return comboZoom;
        }
        return null;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
