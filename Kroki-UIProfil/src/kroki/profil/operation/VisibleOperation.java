package kroki.profil.operation;

import java.util.List;

import kroki.profil.BusinessProcessModelingSubject;
import kroki.profil.ComponentType;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlParameter;
import kroki.uml_core_basic.UmlType;

/**
 * Stereotype visible operation represents a method of the VisibleClass class
 * which is connected with a UI component (a button or menu item) which enables
 * the operation to be activated by the user
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class VisibleOperation extends BusinessProcessModelingSubject implements UmlOperation {

	private static final long serialVersionUID = 1L;
	
	/*METACLASS OPERATION PROPERTIES*/
    protected UmlClass umlClass;
    protected List<UmlType> raisedException;
    protected List<UmlParameter> ownedParameter;
    /*METACLASS TYPEDELEMENT PROPERTIES*/
    protected UmlType umlType;
    /*METALASS MULTIPLICITYELEMENT PROPERTIES*/
    protected boolean isOrdered;
    protected boolean isUnique;
    protected int lower;
    protected int upper;

    public VisibleOperation() {
        super();
    }

    public VisibleOperation(String label, boolean visible, ComponentType componentType) {
        super(label, visible, componentType);
    }

    public VisibleOperation(String label) {
        super(label, true, ComponentType.BUTTON);
    }

    /***********************************************/
    /*UmlOperation interface methods*/
    /***********************************************/
    public List<UmlParameter> ownedParameter() {
        //throw new UnsupportedOperationException("Not supported yet.");
    	return null;
    }

    public List<UmlType> raisedException() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setUmlClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }

    public UmlClass umlClass() {
        return umlClass;
    }

    public void setType(UmlType umlType) {
        this.umlType = umlType;
    }

    public UmlType type() {
        return umlType;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public int lower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public void setOrdered(boolean ordered) {
        this.isOrdered = ordered;
    }

    public void setUnique(boolean unique) {
        this.isUnique = unique;
    }

    public void setUpper(int upper) {
        this.upper = upper;
    }

    public int upper() {
        return upper;
    }

	@Override
	public String toString() {
		//this.qualifiedName() does not return anything 
		return umlClass.name() + "." + label;
	}
}
