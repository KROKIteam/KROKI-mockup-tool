/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import kroki.mockup.model.border.LineBorder;
import kroki.mockup.model.layout.BorderLayoutManager;
import kroki.mockup.model.layout.FlowLayoutManager;
import kroki.mockup.model.layout.LayoutManager;
import kroki.mockup.view.painters.CompositePainter;

/**
 * Class represents a composite component of the user interface.
 * In this case, that is a panel
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
@SuppressWarnings("serial")
public class Composite extends Component {

    /** List of component contained by the composite component*/
    private List<Component> childrenList;
    /**Layout manager responsible for arranging the components*/
    protected LayoutManager layoutManager;
    /**Components border*/
    protected Border border;

    public Composite(String name) {
        super(name);
        childrenList = new ArrayList<Component>();
        layoutManager = new FlowLayoutManager();
        elementPainter = new CompositePainter(this);
        border = new LineBorder();
    }

    public Composite() {
        super();
        childrenList = new ArrayList<Component>();
        layoutManager = new FlowLayoutManager();
        elementPainter = new CompositePainter(this);
        border = new LineBorder();
    }

    /**************/
    /*PUBLIC METHODS*/
    /**************/
    /**
     * Adds component to the composite structure in case when the layout  manager of the paren's component is <code>FlowLayoutManager</code> ili <code>FreeLayoutManager</code>
     * <b>Additional remark:</b>
     * Component {@code child} has to be different than the parent component. There can't be two identical component inside one composite pomponent.
     * @param child component
     */
    public void addChild(Component child) {
        //TODO: Zabrana cirkularnog uvezivanja i ponavljanja.
    	//if(!childrenList.contains(child))
    	{
	        child.setParent(this);
	        childrenList.add(child);
    	}
    }

    public void addChild(int index, Component child) {
        child.setParent(this);
        childrenList.add(index, child);
    }

    /**
     * Adds component to the composite structure in case when the layout  manager of the paren's component is  <code>BorderLayoutManager</code>
     * @param child component
     * @param layout position. NORTH (1), WEST (2), CENTER (3), EAST (4) ili SOUTH (5)
     */
    public void addChild(Component child, int layout) {
        if (layoutManager instanceof BorderLayoutManager) {
            if (layout == BorderLayoutManager.NORTH) {
                if (((BorderLayoutManager) layoutManager).getTop() != null) {
                    removeChild(((BorderLayoutManager) layoutManager).getTop());
                }
                addChild(child);
                ((BorderLayoutManager) layoutManager).setTop(child);
            } else if (layout == BorderLayoutManager.SOUTH) {
                if (((BorderLayoutManager) layoutManager).getBottom() == null) {
                    removeChild(((BorderLayoutManager) layoutManager).getBottom());
                }
                addChild(child);
                ((BorderLayoutManager) layoutManager).setBottom(child);
            } else if (layout == BorderLayoutManager.WEST) {
                if (((BorderLayoutManager) layoutManager).getLeft() == null) {
                    removeChild(((BorderLayoutManager) layoutManager).getLeft());
                }
                addChild(child);
                ((BorderLayoutManager) layoutManager).setLeft(child);
            } else if (layout == BorderLayoutManager.CENTER) {
                if (((BorderLayoutManager) layoutManager).getCenter() == null) {
                    removeChild(((BorderLayoutManager) layoutManager).getCenter());
                }
                addChild(child);
                ((BorderLayoutManager) layoutManager).setCenter(child);
            } else if (layout == BorderLayoutManager.EAST) {
                if (((BorderLayoutManager) layoutManager).getRight() != null) {
                    removeChild(((BorderLayoutManager) layoutManager).getRight());
                }
                addChild(child);
                ((BorderLayoutManager) layoutManager).setRight(child);
            } else {
                System.err.println("Opercija dodavanja komponente " + child.toString() + " nije uspela zato Ã…Â¡to je layout \"" + layout + "\" pogreÃ…Â¡no naznaÃ„ï¿½en.");
            }
        } else {
            System.err.println("Opercija dodavanja komponente " + child.toString() + " nije uspela zato Ã…Â¡to layout manager komponente nije BorderLayoutManager.");
        }
    }

    /**
     * Removes a component from the list of components
     * @param child component to be removed
     */
    public void removeChild(Component child) {
        if (childrenList.contains(child)) {
            childrenList.remove(child);
        }
    }

    /**
     * Returns child component with index <code>i</code>
     * @param i index
     */
    public Component getComponent(int i) {
        return childrenList.get(i);
    }

    /**
     * Return number of directly contained components
     * @return
     */
    public int getComponentCount() {
        return childrenList.size();
    }

    /**
     * Lays out components inside the composite structure based on the chosen <code>LayoutManager</code>
     */
    public void layout() {
        layout(this);
    }

    /**
     * Lays out components inside the composite structure based on the chosen <code>LayoutManager</code>
     * @param c composite component
     */
    public void layout(Composite c) {
        c.getLayoutManager().layoutComponent(c);
        for (int i = 0; i < c.getComponentCount(); i++) {
            Component m = c.getComponent(i);
            if (m instanceof Composite) {
                layout((Composite) m);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (layoutManager != null) {
            return layoutManager.preferredLayoutSize(this);
        } else {
            return dimension;
        }
    }

    /**
     * Returns a contained component if it is in cursor's position and the component itself otherwise
     * @param point cursor's position
     * @return component at the passed position
     */
    public Component getComponentAt(Point point) {
        for (Component child : childrenList) {
            if (child.contains(point)) {
                if (child instanceof Composite) {
                    return ((Composite) child).getComponentAt(point);
                } else {
                    return child;
                }
            }
        }
        return this;
    }

    @Override
    public void updateComponent() {
        elementPainter.update();
    }

    @Override
    public Dimension getMinimumSize() {
        if (layoutManager != null) {
            return layoutManager.minimumLayoutSize(this);
        } else {
            return dimension;
        }
    }

    @Override
    public Dimension getMaximumSize() {
        return layoutManager.maximumLayoutSize(this);
    }

    /*****************/
    /*GETTERS AND SETTERS/
    /*****************/
    public List<Component> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<Component> childrenList) {
        this.childrenList = childrenList;
    }

    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public Border getBorder() {
        return border;
    }

    public void setBorder(Border border) {
        this.border = border;
    }
}
