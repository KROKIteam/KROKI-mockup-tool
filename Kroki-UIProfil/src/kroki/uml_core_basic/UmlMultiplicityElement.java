/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.uml_core_basic;

/**
 * Definiše kolekciju elemenata i specificira kardinalitet tj. broj elemenata kao i redosled i jedinstvenost elemenata.
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
