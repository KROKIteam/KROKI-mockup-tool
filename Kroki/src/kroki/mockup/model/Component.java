/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import kroki.mockup.view.GraphElement;

/**
 * Klasa koja predstavlja apstraktnu komponentu kroki.mockup sistema.
 * Deo je implementacije <code>Composite design pattern</code>-a.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public abstract class Component extends GraphElement implements Serializable {

    /**Referenca ka roditeljskoj komponenti. Vrednost može biti <code>null</code> ukoliko je ova komponenta korenska tj. u vrhu hijerarhije.*/
    protected Composite parent;
    /**Prazan prostor oko sadržaja komponente*/
    protected Insets insets;
    /**Prazan prostor unutar sadržaja komponente. Koristi se u slučaju tekstualnih komponenti*/
    protected Margins margins;
    /**Apsolutna pozicija komponente.*/
    protected Point absolutePosition;
    /**Relativna pozicija komponente. Pozicija komponente u odnosu na njenu roditeljsku komponentu*/
    protected Point relativePosition;
    /** Dimenzije komponente*/
    protected Dimension dimension;
    /** Naziv komponente*/
    protected String name;
    /**Pokazatelj da je komponenti omogućen rad*/
    protected boolean enabled;
    /**Pokazatelj da je komponenta vidljiva*/
    protected boolean visible;
    /**Tekst koji se ispisuje unutar komponente kao njen podrazumevani sadržaj ili kao naslov komponente u slučaju da je u pitanju panel*/
    protected String text;
    /**Indikator da li je komponenta zaključana za pomeranje, promenu veličine, selekciju*/
    protected boolean locked;

    /**
     * Podrazumevani konstruktor
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
     * Konstruktor klase {@link Component}
     * @param name naziv komponente
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
    /*JAVNE METODE*/
    /**************/
    /**
     * Metoda koja izračunava dimenziju komponente na osnovu njenog sadržaja i načina raspoređivanja sadržaja
     * @return dimenzije komponente
     */
    public Dimension getPreferredSize() {
        return dimension;
    }

    /**
     * Metoda koja proverava da li se prosleđena lokacija kursora nalazi unutar komponente.
     * @param point pozicija kursora
     * @return true ako da, false ako ne.
     */
    public boolean contains(Point point) {
        if (point.x > absolutePosition.x && point.x < absolutePosition.x + dimension.width) {
            if (point.y > absolutePosition.y && point.y < absolutePosition.y + dimension.height) {
                return true;
            }
        }
        return false;
    }

    /**Osvezava komponentu i njen painter*/
    public abstract void updateComponent();

    /**Vraća minimalnu dimenziju komponente - najmanji prostor koji komponenta može da zauzme*/
    public abstract Dimension getMinimumSize();

    /**Vraća maksimalnu dimenziju komponente - najveći prostor koji komponenta može da zauzme*/
    public abstract Dimension getMaximumSize();

    /**Vraća pravougaonik koji uokviruje komponentu sa njenim relativnim pozicijama*/
    public Rectangle2D getBoundsForRelativePosition() {
        int x = relativePosition.x;
        int y = relativePosition.y;

        int width = dimension.width;
        int height = dimension.height;

        Rectangle2D rec = new Rectangle2D.Float(x, y, width, height);
        return rec;
    }

    /**Vraća pravougaonik koji uokviruje komponentu sa njenim apsolutnim pozicijama*/
    public Rectangle2D getBoundsForAbsolutePosition() {
        int x = absolutePosition.x;
        int y = absolutePosition.y;

        int width = dimension.width;
        int height = dimension.height;

        Rectangle2D rec = new Rectangle2D.Float(x, y, width, height);
        return rec;
    }

    /*****************/
    /*GETERI I SETERI*/
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
