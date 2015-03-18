package kroki.profil.operation;

import kroki.profil.ComponentType;
import kroki.profil.persistent.PersistentOperation;

/**
 * Abstract stereotype BusinessOperation represents a method which,
 * through a UI component, enables activation of the persistent operation
 * connected with the business operation
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class BussinessOperation extends VisibleOperation {

	private static final long serialVersionUID = 1L;
	
	/**Indicates if it is necessary to generate a parameters input dialog for the operation*/
    protected boolean hasParametersForm = true;
    /**Indicates if the operation has an implicitly defined parameter which represents an id of the class it belongs to*/ 
    protected boolean filteredByKey = true;
    /**Persistent operation connected to the business operation*/
    protected PersistentOperation persistentOperation;

    public BussinessOperation(String label) {
        super(label);

    }

    public BussinessOperation(String label, boolean visible, ComponentType componentType) {
        super(label, visible, componentType);
    }

    public BussinessOperation() {
    }

    /**********************/
    /*GETTERS AND SETTERS*/
    /*********************/
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
