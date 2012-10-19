/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.mockup.view.painters;

import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import kroki.mockup.model.Component;
import kroki.mockup.view.GraphElement;

/**
 * Klasa koja predstavlja apstraktni element painter. Definiše metode koje njene naslednice implementiraju.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public abstract class ElementPainter implements Serializable {

    /**'Referenca ka grafičkom elementu*/
    protected GraphElement graphElement;

    /**
     * Konstruktor <code>ElementPainter</code>-a
     * @param graphElement grafički element
     */
    public ElementPainter(GraphElement graphElement) {
        this.graphElement = graphElement;
    }

    /**
     * Proverava da li se nad pozicijom kursora <code>p</code> nalazi komponenta.
     * @param p pozicija kursora
     * @return <code>true</code> ukoliko se nad pozicijom kursora nalazi komponenta, u suprotnom <code>false</code>
     * @see {@link #getComponentAt(java.awt.Point) }
     */
    public abstract boolean isComponentAt(Point p);

    /**
     * Pronalazi komponentu nad pozicijom kursora <code>p</code>
     * Pošto se pretraga vrši kroz kompozitnu strukturu povratna vrednost ove metode je komponeta poslednja u hijerarhiji nad kojom se <code>p</code> nalazi.
     * @param p pozicija kursora
     * @return {@link Component}
     * @see {@link #isComponentAt(java.awt.Point) }
     */
    public abstract Component getComponentAt(Point p);

    /**
     * Iscrtava komponentu.
     * @param g
     */
    public abstract void paint(Graphics g);

    /**
     * Metoda koja se poziva nakon promene pozicije ili dimenzija komponente.
     * Tom prilikom potrebno je osvežiti i poziciju ili dimenzije njene grafičke reprezentacije.
     */
    public abstract void update();

    //GETERI I SETERI
    public GraphElement getGraphElement() {
        return graphElement;
    }

    public void setGraphElement(GraphElement graphElement) {
        this.graphElement = graphElement;
    }
}
