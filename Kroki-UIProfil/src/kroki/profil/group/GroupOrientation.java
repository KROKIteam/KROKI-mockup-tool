package kroki.profil.group;

import java.io.Serializable;
import kroki.intl.Intl;

/**
 * Enumerated type <code>GroupOrientation</code> defines a set of possible
 * values for defining orientation of groups inside panels.
 * CURRENTLY NOT USED
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public enum GroupOrientation implements Serializable{

    horizontal, vertical, area;

    @Override
    public String toString() {
        String key = "groupOrientation" + "." + name();
        return Intl.getValue(key);
    }
}
