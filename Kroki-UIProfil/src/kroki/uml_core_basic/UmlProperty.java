package kroki.uml_core_basic;

/**
 * Interface represents UML property
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface UmlProperty extends UmlTypedElement, UmlMultiplicityElement {

    public UmlClass umlClass();

    public boolean isComposite();

    public boolean isDerived();

    public boolean isReadOnly();

    public String getDefault();

    public UmlProperty opposite();

    public void setUmlClass(UmlClass umlClass);

    public void setDefault(String def);

    public void setComposite(boolean isComposite);

    public void setDerived(boolean isDerived);

    public void setReadOnly(boolean isReadOnly);

    public void setOpposite(UmlProperty opposite);
}
