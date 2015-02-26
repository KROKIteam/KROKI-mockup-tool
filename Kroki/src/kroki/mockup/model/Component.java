package kroki.mockup.model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import kroki.mockup.view.GraphElement;

/**
 * The class represent and abstract component of the Kroki mockup system
 * It is a part of an implementation of <code>Composite design pattern</code>-a.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public abstract class Component extends GraphElement implements Serializable {

    /**Reference to the parent component. Its value can be <code>null</code> 
     * if the component is the root of the hierarchy*/
    protected Composite parent;
    /**Empty space around the component's content*/
    protected Insets insets;
    /**Empty space inside the component's content. Used if the component is a textual component */
    protected Margins margins;
    /**The absolute position of the component*/
    protected Point absolutePosition;
    /**Relative position of the component - in respect to its parent component */
    protected Point relativePosition;
    /** Component's dimension*/
    protected Dimension dimension;
    /** Component's name*/
    protected String name;
    /**Indicates if the component is enabled*/
    protected boolean enabled;
    /**Indicates if the component is visible*/
    protected boolean visible;
    /**Text shown inside the component as its default content or is used as its default title if it is a panel*/
    protected String text;
    /**Indicates if the component is locked i.e if it can be moved, resized and selected*/
    protected boolean locked;

    /**
     * Default constructor
     */
    public Component() {
        super();
        this.insets = new Insets();
        this.margins = new Margins();
        this.absolutePosition = new Point(0, 0);
        this.relativePosition = new Point(0, 0);
        this.dimension = new Dimension(10, 10);
        this.enabled = true;
        this.visible = true;
        this.locked = false;
    }

    /**
     * Constructor of {@link Component}
     * @param name Components name
     */
    public Component(String name) {
        super();
        this.name = name;
        this.insets = new Insets();
        this.margins = new Margins();
        this.absolutePosition = new Point(0, 0);
        this.relativePosition = new Point(0, 0);
        this.dimension = new Dimension(10, 10);
        this.enabled = true;
        this.visible = true;
        this.locked = false;
    }

    /**************/
    /*PUBLIC METHODS
    /**************/
    /**Calculates the compoenent's dimension based on its content and the way its content are arranged
     *@return compoent's dimension 
     */
    public Dimension getPreferredSize() {
        return dimension;
    }

    /**
     * Checks if the passed cursor position is inside the component
     * @param point cursor's position
     * @return true if it is, false otherwise
     */
    public boolean contains(Point point) {
        if (point.x > absolutePosition.x && point.x < absolutePosition.x + dimension.width) {
            if (point.y > absolutePosition.y && point.y < absolutePosition.y + dimension.height) {
                return true;
            }
        }
        return false;
    }

    /**Refreshes the component and its painter*/
    public abstract void updateComponent();

    /**Returns the minimal dimension of the component- the minimal amount of space that it can take*/
    public abstract Dimension getMinimumSize();

    /**Return maximal dimension of the compoenent- the maximal amount of space that it can take*/
    public abstract Dimension getMaximumSize();

    /**Return a rectangle which surrounds the component with its relative positions*/
    public Rectangle2D getBoundsForRelativePosition() {
        int x = relativePosition.x;
        int y = relativePosition.y;

        int width = dimension.width;
        int height = dimension.height;

        Rectangle2D rec = new Rectangle2D.Float(x, y, width, height);
        return rec;
    }

    /**Return a rectangle which surrounds the component with its absolute positions*/
    public Rectangle2D getBoundsForAbsolutePosition() {
        int x = absolutePosition.x;
        int y = absolutePosition.y;

        int width = dimension.width;
        int height = dimension.height;

        Rectangle2D rec = new Rectangle2D.Float(x, y, width, height);
        return rec;
    }

    /*****************/
    /*GETTERS AND SETTERS*/
    /*****************/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Insets getInsets() {
        return insets;
    }

    public void setInsets(Insets insets) {
        this.insets = insets;
    }

    public Composite getParent() {
        return parent;
    }

    public void setParent(Composite parent) {
        this.parent = parent;
    }

    public Point getAbsolutePosition() {
        return absolutePosition;
    }

    public void setAbsolutePosition(Point absolutePosition) {
        this.absolutePosition = absolutePosition;
        if (parent != null) {
            this.relativePosition.x = this.absolutePosition.x - parent.getAbsolutePosition().x;
            this.relativePosition.y = this.absolutePosition.y - parent.getAbsolutePosition().y;
        } else {
            this.relativePosition.x = this.absolutePosition.x;
            this.relativePosition.y = this.absolutePosition.y;
        }
    }

    public Point getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(Point relativePosition) {
        this.relativePosition = relativePosition;
    }

    public Margins getMargins() {
        return margins;
    }

    public void setMargins(Margins margins) {
        this.margins = margins;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
