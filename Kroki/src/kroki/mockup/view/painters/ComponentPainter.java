/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.view.painters;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import kroki.mockup.model.Component;
import kroki.mockup.view.GraphElement;

/**
 * Klasa koja predstavlja painter za komponente korisničkog interfejsa. Nju svaka konkretna komponenta nasleđuje i implementira svoju {@link #paint(java.awt.Graphics) } metodu.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public abstract class ComponentPainter extends ElementPainter {

    /**Oblik koji se iscrtava. U pitanju je pravougaonik sa dimenzijama i pozicijom komponente.*/
    protected Shape shape;

    /**
     * Konstrukror <code>ComponentPainter</code>-a. Prosleđeni parametri ne smeju imati <code>null</code> vrednosti.
     * @param graphElement grafički element
     * @param shape oblik koji se iscrtava
     */
    public ComponentPainter(GraphElement graphElement, Shape shape) {
        super(graphElement);
        this.shape = shape;
    }

    /**
     * Konsturktor <code>ComponentPainter</code>-a. Ovaj konstruktor kreira podrazumevani oblik {@link #shape} na osnovu dimenzuja dobijenih iz prosleđenog grafičkog elementa {@link #graphElement}.
     * @param graphElement grafički element
     */
    public ComponentPainter(GraphElement graphElement) {
        super(graphElement);

        //Podrazumeva se da je okvir svake komponente u obliku pravougaonika.
        int x = ((Component) graphElement).getRelativePosition().x;
        int y = ((Component) graphElement).getRelativePosition().y;
        int w = ((Component) graphElement).getDimension().width;
        int h = ((Component) graphElement).getDimension().height;

        shape = new Rectangle2D.Float(x, y, w, h);
    }

    @Override
    public void update() {
        int x = ((Component) graphElement).getRelativePosition().x;
        int y = ((Component) graphElement).getRelativePosition().y;
        int w = ((Component) graphElement).getDimension().width;
        int h = ((Component) graphElement).getDimension().height;

        shape = new Rectangle2D.Float(x, y, w, h);
    }

    @Override
    public void paint(Graphics g) {
        //iscrtava okvir komponente
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setPaint(graphElement.getBgColor());
        g2d.fill(shape);
    }


    //GETERI I SETERI
    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
