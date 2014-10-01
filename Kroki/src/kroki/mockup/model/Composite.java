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
 * Klasa koja predstavlja kompozitnu komponentu korisniÄ�kog interfejsa. U ovom sluÄ�aju to bi bio Panel.
 * @author Vladan MarseniÄ‡ (vladan.marsenic@gmail.com)
 */
public class Composite extends Component {

    /** Lista komponenti koje ova kompozitna komponenta u sebi sadrÅ¾i*/
    private List<Component> childrenList;
    /** MenadÅ¾er za rasporedjivanje komponenti*/
    protected LayoutManager layoutManager;
    /**Okvir komponente*/
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
    /*JAVNE METODE*/
    /**************/
    /**
     * Dodavanje komponente u kompozitnu strukturu u sluÄ�aju kada je layout manager roditeljske komponente <code>FlowLayoutManager</code> ili <code>FreeLayoutManager</code>
     * <b>Napomena:</b>
     * Komponenta {@code child} mora biti razliÄ�ita od roditeljske komponente. Isto tako komponete unutar nekog kompozita se ne smeju ponavljati.
     * @param child komponenta
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
     * Dodavanje komponente u kompozitnu strukturu u sluÄ�aju kada je layout manager roditeljske komponente <code>BorderLayoutManager</code>
     * @param child komponenta
     * @param layout deo prostora koji komponenta zauzima. NORTH (1), WEST (2), CENTER (3), EAST (4) ili SOUTH (5)
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
                System.err.println("Opercija dodavanja komponente " + child.toString() + " nije uspela zato Å¡to je layout \"" + layout + "\" pogreÅ¡no naznaÄ�en.");
            }
        } else {
            System.err.println("Opercija dodavanja komponente " + child.toString() + " nije uspela zato Å¡to layout manager komponente nije BorderLayoutManager.");
        }
    }

    /**
     * Brisanje komponente iz liste komponenti.
     * @param child komponenta koju je potrebno izbaciti iz liste.
     */
    public void removeChild(Component child) {
        if (childrenList.contains(child)) {
            childrenList.remove(child);
        }
    }

    /**
     * VraÄ‡a child komponentu sa indexom <code>i</code>
     * @param i
     */
    public Component getComponent(int i) {
        return childrenList.get(i);
    }

    /**
     * VraÄ‡a broj direktno sadrÅ¾anih komponenti.
     * @return
     */
    public int getComponentCount() {
        return childrenList.size();
    }

    /**
     * IzvrÅ¡ava rasporeÄ‘ivanje komponenti unutar kompozitne strukture na osnovu odabranog <code>LayoutManager</code>-a
     */
    public void layout() {
        layout(this);
    }

    /**
     * IzvrÅ¡ava rasporeÄ‘ivanje komponenti unutar prosleÄ‘ene kompozitne komponente na osnovu odabranog <code>LayoutManager</code>-a
     * @param c Kompozitna komponenta
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
     * Ukoliko se na prosleÄ‘enoj poziciji kursora nalazi odreÄ‘ena komponenta deo sadrÅ¾aja strukture vraÄ‡a tu komponentu u suprotnom vraÄ‡a sebe.
     * @param point pozicija kursora.
     * @return
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
    /*GETERI I SETERI*/
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
