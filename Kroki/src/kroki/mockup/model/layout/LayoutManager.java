package kroki.mockup.model.layout;

import java.awt.Dimension;
import java.io.Serializable;

import kroki.mockup.model.Component;

/**
 * Class represents an abstract layout manager
 * Classes which extend provide the rules which define how the components contained 
 * by a container will be laid out
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public abstract class LayoutManager implements Serializable{

	public static final int LEFT = 1;
    public static final int CENTER = 2;
    public static final int RIGHT = 3;
    /**Horizontal gap between components inside a container*/
    protected int hgap;
    /**Vertical gap between components inside a container*/
    protected int vgap;
    /**Align*/
    protected int align;

    /**
     * Layout manager constructor
     * @param hgap Horizontal gap between components inside a container
     * @param vgap Vertical gap between components inside a container
     */
    public LayoutManager(int hgap, int vgap, int align) {
        this.hgap = hgap;
        this.vgap = vgap;
        this.align = align;
    }

    /**
     * Default constructor of a layout manager
     */
    public LayoutManager() {
        this.hgap = 5;
        this.vgap = 5;
        this.align = LEFT;
    }

    /**
     * Arranges the components inside a passed container component 
     * @param c
     */
    public abstract void layoutComponent(Component c);

    /**
     * Determines the preferred size of a component <code>c</code> based on its content
     * Every layout manager implements this method based on its rules for arranging the components
     * @param c
     * @return Preferred size of a component {@link Dimension}
     */
    public abstract Dimension preferredLayoutSize(Component c);

    /**
     * Determines the minimal size of a component <code>c</code> based on its content
     * Every layout manager implements this method based on its rules for arranging the components
     * @param c
     * @return Minimal size of a component {@link Dimension}
     */
    public abstract Dimension minimumLayoutSize(Component c);

    /**
     * Determines the maximal size of a component <code>c</code> based on its content
     * Every layout manager implements this method based on its rules for arranging the components
     * @param c
     * @return Maximal size of a component {@link Dimension}
     */
    public abstract Dimension maximumLayoutSize(Component c);

    //GETTERS AND SETTERS
    public int getHgap() {
        return hgap;
    }

    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    public int getVgap() {
        return vgap;
    }

    public void setVgap(int vgap) {
        this.vgap = vgap;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }
}
