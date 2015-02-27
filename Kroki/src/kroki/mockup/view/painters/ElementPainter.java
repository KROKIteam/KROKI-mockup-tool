package kroki.mockup.view.painters;

import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

import kroki.mockup.model.Component;
import kroki.mockup.view.GraphElement;

/**
 * Abstract element painter which defines the methods implemented by other painter classes, which extend it.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public abstract class ElementPainter implements Serializable {

    /**Graphical element*/
    protected GraphElement graphElement;

    /**
     * Constructor of <code>ElementPainter</code>-a
     * @param graphElement Graphical element
     */
    public ElementPainter(GraphElement graphElement) {
        this.graphElement = graphElement;
    }

    /**
     * Checks if a component is located at the cursor's position
     * @param p Cursor's position
     * @return <code>true</code> if a component is located at the cursor's position, otherwise <code>false</code>
     * @see {@link #getComponentAt(java.awt.Point) }
     */
    public abstract boolean isComponentAt(Point p);

    /**
     * Finds the component located at the cursor's position. Returns the last one if more than one is found.
     * @param p Cursor's position
     * @return {@link Component}
     * @see {@link #isComponentAt(java.awt.Point) }
     */
    public abstract Component getComponentAt(Point p);

    /**
     * Paints the component
     * @param g
     */
    public abstract void paint(Graphics g);

    /**
     * Method which is called after the component's position of dimension is changed
     * and refreshes the position or dimension of its graphical representation
     */
    public abstract void update();

    //GETTERS AND SETTERS
    public GraphElement getGraphElement() {
        return graphElement;
    }

    public void setGraphElement(GraphElement graphElement) {
        this.graphElement = graphElement;
    }
}
