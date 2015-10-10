package kroki.mockup.view.painters;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import kroki.mockup.model.Component;
import kroki.mockup.view.GraphElement;

/**
 * Abstract class which represent a painter of user interface components
 * All other painter classes extend it and implement their {@link #paint(java.awt.Graphics)} method
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public abstract class ComponentPainter extends ElementPainter {

	/**Shape which is being painted. It is a rectangle with dimension and position of the component*/
    protected Shape shape;

    /**
     * Constructor of <code>ComponentPainter</code>-a. Passed parameters have <code>null</code> values.
     * @param graphElement Graphical element
     * @param shape Shape being painted
     */
    public ComponentPainter(GraphElement graphElement, Shape shape) {
        super(graphElement);
        this.shape = shape;
    }

    /**
     * Constructor of <code>ComponentPainter</code>-a. 
     * Creates default {@link #shape} based on dimensions of graphical element {@link #graphElement}.
     * @param graphElement Graphical element
     */
    public ComponentPainter(GraphElement graphElement) {
        super(graphElement);

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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setPaint(graphElement.getBgColor());
        g2d.fill(shape);
    }


    //GETTERS AND SETTERS
    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
