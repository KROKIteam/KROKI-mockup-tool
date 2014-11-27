/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.persistent;

import java.io.Serializable;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlProperty;
import kroki.uml_core_basic.UmlType;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class PersistentProperty implements UmlProperty, Serializable {
	
	private static final long serialVersionUID = 1L;

    /*Obelezja klase PersistentProperty*/
    protected boolean identifier = false;
    protected boolean identity = false;
    protected boolean nullable = true;
    /*Obelezja metaklase NamedElement*/
    protected String name;
    protected String qualifiedName;
    /*Obelezja metaklase Property*/
    protected String defaultValue = "";
    protected boolean isComposite = false;
    protected boolean isDerived = false;
    protected boolean isReadOnly = false;
    protected UmlProperty opposite = null;
    protected UmlClass umlClass;
    /*Obelezja metaklase TypedElement*/
    protected UmlType umlType;
    /*Obelezja metaklase MultiplicityElement*/
    protected boolean isOrdered;
    protected boolean isUnique;
    protected int lower;
    protected int upper;

    /****************************************/
    /*Operacije nasledjene od uml property-a*/
    /****************************************/
    public String getDefault() {
        return defaultValue;
    }

    public boolean isComposite() {
        return isComposite;
    }

    public boolean isDerived() {
        return isDerived;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public UmlProperty opposite() {
        return opposite;
    }

    public void setComposite(boolean isComposite) {
        this.isComposite = isComposite;
    }

    public void setDefault(String def) {
        this.defaultValue = def;
    }

    public void setDerived(boolean isDerived) {
        this.isDerived = isDerived;
    }

    public void setOpposite(UmlProperty opposite) {
        this.opposite = opposite;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
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

    public int upper() {
        return upper;
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

    /*****************/
    /*GETERI I SETERI*/
    /*****************/
    public boolean isIdentifier() {
        return identifier;
    }

    public void setIdentifier(boolean identifier) {
        this.identifier = identifier;
    }

    public boolean isIdentity() {
        return identity;
    }

    public void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
