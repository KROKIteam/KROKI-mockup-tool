package kroki.uml_core_basic;

/**
 * Interface represents UML named element.
 * Extends UML element and adds name and qualified name.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface UmlNamedElement extends UmlElement {

    public String name();

    public String qualifiedName();

    public void setName(String name);

    public void setQualifiedName(String qualifiedName);
}
