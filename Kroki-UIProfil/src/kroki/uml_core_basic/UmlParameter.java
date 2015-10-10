package kroki.uml_core_basic;

/**
 * Interface represents UML parameter
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface UmlParameter extends UmlTypedElement, UmlMultiplicityElement{

    public UmlOperation operation();

    public void setOperation(UmlOperation umlOperation);
}
