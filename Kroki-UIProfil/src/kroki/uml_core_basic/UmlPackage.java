package kroki.uml_core_basic;

import java.util.List;

/**
 * Interface represents UML package.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface UmlPackage extends UmlNamedElement {

    public List<UmlType> ownedType();

    public List<UmlPackage> nestedPackage();

    public UmlPackage nestingPackage();

    public void setNestingPackage(UmlPackage umlPackage);

    public void addNestedPackage(UmlPackage umlPackage);

    public void removeNestedPackage(UmlPackage umlPackage);

    public void addOwnedType(UmlType ownedType);

    public void removeOwnedType(UmlType ownedType);
}
