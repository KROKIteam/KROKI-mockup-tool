package kroki.uml_core_basic;

import java.util.List;

/**
 * Interface representing UML class
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface UmlClass extends UmlType {

    public boolean isAbstract();

    public void setAbstract(boolean isAbstract);

    public List<UmlProperty> ownedAttribute();

    public List<UmlOperation> ownedOperation();

    public List<UmlClass> superClass();

}
