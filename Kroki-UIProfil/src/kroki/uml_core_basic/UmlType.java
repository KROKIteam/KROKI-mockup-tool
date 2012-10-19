/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.uml_core_basic;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface UmlType extends UmlNamedElement {

    public UmlPackage umlPackage();

    public void setUmlPackage(UmlPackage umlPackage);
}
