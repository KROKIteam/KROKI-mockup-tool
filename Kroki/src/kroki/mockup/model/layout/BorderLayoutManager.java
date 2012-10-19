/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.model.layout;

import java.awt.Dimension;
import java.awt.Point;
import kroki.mockup.model.Insets;
import kroki.mockup.model.Component;
import kroki.mockup.model.Composite;

/**
 * Layout manager koji rasporedjuje komponente u gornji, donji, levi, desni i centralni deo kontejnera.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class BorderLayoutManager extends LayoutManager {

    /*Konstante koje označavaju u kojem delu kontejnera se nalazi komponenta*/
    public static final int NORTH = 1;
    public static final int WEST = 2;
    public static final int CENTER = 3;
    public static final int EAST = 4;
    public static final int SOUTH = 5;
    /*Komponente*/
    Component top;
    Component left;
    Component right;
    Component bottom;
    Component center;

    public BorderLayoutManager() {
        super();
    }

    public BorderLayoutManager(int hgap, int vgap, int align) {
        super(hgap, vgap, 1);
        //I DONT USE ALIGN
    }

    @Override
    public void layoutComponent(Component c) {
        Insets insets = c.getInsets();

//        insets.top = 40;
//        insets.bottom = 4;
//        insets.left = 4;
//        insets.right = 4;

        Point cP = c.getAbsolutePosition();
        int iTop = insets.top;
        int iBottom = c.getDimension().height - insets.bottom;
        int iLeft = insets.left;
        int iRight = c.getDimension().width - insets.right;

        Component child = null;
        if ((child = top) != null) {
            child.getDimension().setSize(iRight - iLeft, child.getDimension().height);
            Dimension d = child.getPreferredSize();
            child.getRelativePosition().setLocation(iLeft, iTop);
            child.getAbsolutePosition().setLocation(cP.x + iLeft, cP.y + iTop);
            child.getDimension().setSize(iRight - iLeft, d.height);
            child.getElementPainter().update();
            iTop += d.height + vgap;
        }
        if ((child = bottom) != null) {
            child.getDimension().setSize(iRight - iLeft, child.getDimension().height);
            Dimension d = child.getPreferredSize();
            child.getRelativePosition().setLocation(iLeft, iBottom - d.height);
            child.getAbsolutePosition().setLocation(cP.x + iLeft, cP.y + iBottom - d.height);
            child.getDimension().setSize(iRight - iLeft, d.height);
            child.getElementPainter().update();
            iBottom -= d.height + vgap;
        }
        if ((child = right) != null) {
            child.getDimension().setSize(child.getDimension().width, iBottom - iTop);
            Dimension d = child.getPreferredSize();
            child.getRelativePosition().setLocation(iRight - d.width, iTop);
            child.getAbsolutePosition().setLocation(cP.x + iRight - d.width, cP.y + iTop);
            child.getDimension().setSize(d.width, iBottom - iTop);
            child.getElementPainter().update();
            iRight -= d.width + hgap;
        }
        if ((child = left) != null) {
            child.getDimension().setSize(child.getDimension().width, iBottom - iTop);
            Dimension d = child.getPreferredSize();
            child.getRelativePosition().setLocation(iLeft, iTop);
            child.getAbsolutePosition().setLocation(cP.x + iLeft, cP.y + iTop);
            child.getDimension().setSize(d.width, iBottom - iTop);
            child.getElementPainter().update();
            iLeft += d.width + hgap;
        }
        if ((child = center) != null) {
            child.getRelativePosition().setLocation(iLeft, iTop);
            child.getAbsolutePosition().setLocation(cP.x + iLeft, cP.y + iTop);
            child.getDimension().setSize(iRight - iLeft, iBottom - iTop);
            child.getElementPainter().update();
        }
    }

    @Override
    public Dimension preferredLayoutSize(Component c) {

        //TODO: ovo se moze izbaciti. Nije potrebno staviti ovo ograničenje...ukoliko je kompozit prazan da vrati podesenu vrednost.
        if (((Composite) c).getComponentCount() == 0) {
            return c.getDimension();
        }

        Dimension dim = new Dimension(0, 0);
        Component child = null;
        if ((child = right) != null) {
            Dimension d = child.getPreferredSize();
            dim.width += d.width + hgap;
            dim.height = Math.max(d.height, dim.height);
        }
        if ((child = left) != null) {
            Dimension d = child.getPreferredSize();
            dim.width += d.width + hgap;
            dim.height = Math.max(d.height, dim.height);
        }
        if ((child = center) != null) {
            Dimension d = child.getPreferredSize();
            dim.width += d.width;
            dim.height = Math.max(d.height, dim.height);
        }
        if ((child = top) != null) {
            Dimension d = child.getPreferredSize();
            dim.width = Math.max(d.width, dim.width);
            dim.height += d.height + vgap;
        }
        if ((child = bottom) != null) {
            Dimension d = child.getPreferredSize();
            dim.width = Math.max(d.width, dim.width);
            dim.height += d.height + vgap;
        }

        Insets insets = c.getInsets();
        dim.width += insets.getLeft() + insets.getRight();
        dim.height += insets.getTop() + insets.getBottom();

        return dim;
    }

    @Override
    public Dimension minimumLayoutSize(Component c) {
        return new Dimension(200, 200);
    }

    @Override
    public Dimension maximumLayoutSize(Component c) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    //GETERI I SETERI
    public Component getBottom() {
        return bottom;
    }

    public void setBottom(Component bottom) {
        this.bottom = bottom;
    }

    public Component getCenter() {
        return center;
    }

    public void setCenter(Component center) {
        this.center = center;
    }

    public Component getLeft() {
        return left;
    }

    public void setLeft(Component left) {
        this.left = left;
    }

    public Component getRight() {
        return right;
    }

    public void setRight(Component right) {
        this.right = right;
    }

    public Component getTop() {
        return top;
    }

    public void setTop(Component top) {
        this.top = top;
    }
}
