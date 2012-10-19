/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.uml_core_basic;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface UmlParameter extends UmlTypedElement, UmlMultiplicityElement{

    public UmlOperation operation();

    public void setOperation(UmlOperation umlOperation);
}
