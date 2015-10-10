package kroki.profil.group;

import java.io.Serializable;
import kroki.intl.Intl;

/**
 * Enumerated type <code>GroupAlignment</code> defines a set of possible
 * values for defining alignment of elements inside a group.
 * Not suited for groups which model menus
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public enum GroupAlignment implements Serializable{

    left, right, center;

    @Override
    public String toString() {
        String key = "groupAlignment" + "." + name();
        return Intl.getValue(key);
    }
}
