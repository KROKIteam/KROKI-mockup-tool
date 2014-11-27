/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.operation;

import kroki.profil.ComponentType;
import kroki.profil.persistent.PersistentOperation;

/**
 * Apstraktni stereotip  BussinesOperation označava metodu koja
 * putem komponente korisničkog interfejsa omogućava aktiviranje
 * pridružene perzistentne metode
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class BussinessOperation extends VisibleOperation {

	private static final long serialVersionUID = 1L;
	
    /**Indikator koji označava da li je za metodu potrebno generisati panel za unos parametara*/
    protected boolean hasParametersForm = true;
    /**indikator da metoda ima i implicitno definisan parametar koji odgovara identifikatoru klase kojoj pripada. */
    protected boolean filteredByKey = true;
    /**Pridružena perzistentna metoda*/
    protected PersistentOperation persistentOperation;

    public BussinessOperation(String label) {
        super(label);

    }

    public BussinessOperation(String label, boolean visible, ComponentType componentType) {
        super(label, visible, componentType);
    }

    public BussinessOperation() {
    }

    /*****************/
    /*GETERI I SETERI*/
    /*****************/
    public boolean isFilteredByKey() {
        return filteredByKey;
    }

    public void setFilteredByKey(boolean filteredByKey) {
        this.filteredByKey = filteredByKey;
    }

    public boolean isHasParametersForm() {
        return hasParametersForm;
    }

    public void setHasParametersForm(boolean hasParametersForm) {
        this.hasParametersForm = hasParametersForm;
    }

    public PersistentOperation getPersistentOperation() {
        return persistentOperation;
    }

    public void setPersistentOperation(PersistentOperation persistentOperation) {
        this.persistentOperation = persistentOperation;
    }

}
