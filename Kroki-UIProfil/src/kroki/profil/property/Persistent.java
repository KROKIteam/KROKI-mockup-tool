package kroki.profil.property;

import kroki.profil.persistent.PersistentProperty;

/**
 * Class <code>Persistent</code> represents a persistent property
 * connected to a user interface component which enables its value
 * to be viewed, entered, edited or searched
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Persistent extends VisibleProperty {
	
	private static final long serialVersionUID = 1L;

    /**Persistent field*/
    protected PersistentProperty persistentProperty;

    public Persistent() {
        super();
    }

    /**********************/
    /*GETTERS AND SETTERS*/
    /********************/
    public PersistentProperty getPersistentProperty() {
        return persistentProperty;
    }

    public void setPersistentProperty(PersistentProperty persistentProperty) {
        this.persistentProperty = persistentProperty;
    }
}
