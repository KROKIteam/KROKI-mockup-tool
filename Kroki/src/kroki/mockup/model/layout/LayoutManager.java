/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.model.layout;

import java.awt.Dimension;
import java.io.Serializable;
import kroki.mockup.model.Component;

/**
 * Klasa koja predstavla abstraktni layout manager. Njegove naslednice definišu pravila raspoređivanja komponenti unutar određenog kontejnera.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public abstract class LayoutManager implements Serializable{

    public static final int LEFT = 1;
    public static final int CENTER = 2;
    public static final int RIGHT = 3;
    /**Horizontalno rastojanje izmedju komponenti unutar određenog kontejnera*/
    protected int hgap;
    /**Vertikalno rastojanje između komponenti unutar nekog kontejnera*/
    protected int vgap;
    /**Poravnavanje*/
    protected int align;

    /**
     * Konstruktor layout managera.
     * @param hgap horizontalno rastojanje izmedju komponenti unutar određenog kontejnetra.
     * @param vgap vertikalno rastojanje između komponenti unutar nekog kontejnera.
     */
    public LayoutManager(int hgap, int vgap, int align) {
        this.hgap = hgap;
        this.vgap = vgap;
        this.align = align;
    }

    /**
     * Konstruktor podrazumevanog layout managera.
     */
    public LayoutManager() {
        this.hgap = 5;
        this.vgap = 5;
        this.align = LEFT;
    }

    /**
     * Metoda koja raspoređuje komponente unutar prosleđene po određenom pravilu.
     * @param c
     */
    public abstract void layoutComponent(Component c);

    /**
     * Određuje najpoželjnije dimenzije komponente <code>c</code> na osnovu njenog sadržaja.
     * Svaki layout manager implementira ovu metodu u zavisnosti od svojih pravila raspoređivanja komponenti.
     * @param c
     * @return najpoželjnija dimenzija komponente {@link Dimension}
     */
    public abstract Dimension preferredLayoutSize(Component c);

    /**
     * Određuje minimalne dimenzije komponente <code>c</code> na osnovu njenog sadržaja.
     * Svaki layout manager implementira ovu metodu u zavisnosti od svojih pravila raspoređivanja komponenti.
     * @param c
     * @return minimalna dimenzija komponente {@link Dimension}
     */
    public abstract Dimension minimumLayoutSize(Component c);

    public abstract Dimension maximumLayoutSize(Component c);

    //GETERI I SETERI
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
