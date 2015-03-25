package kroki.uml_core_basic;

/**
 * Interface represents UML typed element. That is, named element 
 * with the specified type
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface UmlTypedElement extends UmlNamedElement {

    public UmlType type();

    public void setType(UmlType umlType);
}
