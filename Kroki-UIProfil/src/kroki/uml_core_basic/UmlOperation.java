package kroki.uml_core_basic;

import java.util.List;

/**
 * Interface represents UML operation
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface UmlOperation extends UmlTypedElement, UmlMultiplicityElement {

    public UmlClass umlClass();

    public List<UmlParameter> ownedParameter();

    public List<UmlType> raisedException();

    public void setUmlClass(UmlClass umlClass);
}
