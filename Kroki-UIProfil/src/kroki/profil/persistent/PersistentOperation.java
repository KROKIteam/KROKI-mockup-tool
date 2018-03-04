package kroki.profil.persistent;

import java.io.Serializable;
import java.util.List;

import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlParameter;
import kroki.uml_core_basic.UmlType;

/**
 * Contains properties of an operation directly mapped to certain database concepts.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class PersistentOperation implements UmlOperation, Serializable {
	
	private static final long serialVersionUID = 1L;

    //NamdedElement metaclass properties
    /**Element's name*/
    protected String name;
    /**Element's qualified name	*/
    protected String qualifiedName;
    //Operation metaclass properties 
    protected UmlClass umlClass;
    protected UmlType umlType;
    protected List<UmlType> raisedException;
    protected List<UmlParameter> ownedParameter;
    //MultiplicityElement metaclass properties
    protected boolean isOrdered;
    protected boolean isUnique;
    protected int lower;
    protected int upper;

    public List<UmlParameter> ownedParameter() {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public String name() {
        return name;
    }

    public String qualifiedName() {
        return qualifiedName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }
}
