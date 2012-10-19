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
public interface UmlOperation extends UmlTypedElement, UmlMultiplicityElement {

    public UmlClass umlClass();

    public List<UmlParameter> ownedParameter();

    public List<UmlType> raisedException();

    public void setUmlClass(UmlClass umlClass);
}
