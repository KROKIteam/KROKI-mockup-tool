package kroki.uml_core_basic;

/**
 * Interface UML type (named element belonging to a package)
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface UmlType extends UmlNamedElement {

    public UmlPackage umlPackage();

    public void setUmlPackage(UmlPackage umlPackage);
}
