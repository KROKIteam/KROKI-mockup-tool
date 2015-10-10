package kroki.uml_core_basic;

/**
 * Interface represents UML multiplicity element. Defines a collection of elements and specifies
 * the cardinality i.e. the number of elements, as well as their order and uniqueness 
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface UmlMultiplicityElement extends UmlElement {

    
    public boolean isOrdered();

    public boolean isUnique();

    public int lower();

    public int upper();

    public void setOrdered(boolean ordered);

    public void setUnique(boolean unique);

    public void setLower(int lower);

    public void setUpper(int upper);
}
