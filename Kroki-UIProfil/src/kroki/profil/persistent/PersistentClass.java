/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.persistent;

import java.io.Serializable;
import java.util.List;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlProperty;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class PersistentClass implements UmlClass, Serializable {
	
	private static final long serialVersionUID = 1L;

    /**Naziv elementa*/
    protected String name;
    /**Kvalitifikovano ime elementa.*/
    protected String qualifiedName;
    /**Pokazatelj da li je klasa apstraktna*/
    protected boolean isAbstract;
    /**Paket kojem klasa pripada*/
    protected UmlPackage umlPackage;
    /**Obeležja klase*/
    protected List<UmlProperty> ownedAttribute;
    /**Operacije*/
    protected List<UmlOperation> ownedOperation;
    /**Generalizacije date klase*/
    protected List<UmlClass> superClass;

    /*IMPLEMENTIRANE METODE INTERFEJSA UmlClass*/
    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public void setUmlPackage(UmlPackage umlPackage) {
        this.umlPackage = umlPackage;
    }

    public UmlPackage umlPackage() {
        return umlPackage;
    }

    public List<UmlProperty> ownedAttribute() {
        return ownedAttribute;
    }

    public List<UmlOperation> ownedOperation() {
        return ownedOperation;
    }

    public List<UmlClass> superClass() {
        return superClass;
    }

    /*JAVNE METODE KLASE PerisistentClass*/
    public void addAttribute(UmlProperty umlProperty) {
        ownedAttribute.add(umlProperty);
    }

    public void removeAttribute(UmlProperty umlProperty) {
        ownedAttribute.remove(umlProperty);
    }

    public void addOperation(UmlOperation umlOperation) {
        ownedOperation.add(umlOperation);
    }

    public void removeOperation(UmlOperation umlOperation) {
        ownedOperation.remove(umlOperation);
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
