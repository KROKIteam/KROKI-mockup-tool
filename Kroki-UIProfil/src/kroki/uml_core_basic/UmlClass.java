/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.uml_core_basic;

import java.util.List;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface UmlClass extends UmlType {

    public boolean isAbstract();

    public void setAbstract(boolean isAbstract);

    public List<UmlProperty> ownedAttribute();

    public List<UmlOperation> ownedOperation();

    public List<UmlClass> superClass();

//    public void addAttribute(UmlProperty umlProperty);
//
//    public void addOperation(UmlOperation umlOperation);
//
//    public void addUmlClass(UmlClass umlClass);
//
//    public void removeAttribute(UmlProperty umlProperty);
//
//    public void removeOperation(UmlOperation umlOperation);
//
//    public void removeSuperClass(UmlClass superClass);
}
