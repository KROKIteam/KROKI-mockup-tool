/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.property;

import kroki.profil.persistent.PersistentProperty;

/**
 * Klasa koja oznaÄ�ava perzistentno obeleÅ¾je
 * vidljivo u okviru korisniÄ�kog interfejsa aplikacije  Ä�ija pridruÅ¾ena
 * komponenta korisniÄ�kog interfejsa omoguÄ‡ava pregled, aÅ¾uriranje i/ili
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
