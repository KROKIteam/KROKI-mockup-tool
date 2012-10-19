/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.uml_core_basic;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface UmlNamedElement extends UmlElement {

    public String name();

    public String qualifiedName();

    public void setName(String name);

    public void setQualifiedName(String qualifiedName);
}
