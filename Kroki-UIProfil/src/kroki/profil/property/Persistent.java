/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.property;

import kroki.profil.persistent.PersistentProperty;

/**
 * Klasa koja označava perzistentno obeležje
 * vidljivo u okviru korisničkog interfejsa aplikacije  čija pridružena
 * komponenta korisničkog interfejsa omogućava pregled, ažuriranje i/ili
 * pretragu njegove vrednosti.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Persistent extends VisibleProperty {
	
	private static final long serialVersionUID = 1L;

    /**Perzistentno polje*/
    protected PersistentProperty persistentProperty;

    public Persistent() {
        super();
    }

    /*****************/
    /*GETERI I SETERI*/
    /*****************/
    public PersistentProperty getPersistentProperty() {
        return persistentProperty;
    }

    public void setPersistentProperty(PersistentProperty persistentProperty) {
        this.persistentProperty = persistentProperty;
    }
}
