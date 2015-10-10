package kroki.profil.group;

import java.io.Serializable;
import kroki.intl.Intl;

/**
 * Enumerated type <code>GroupLocation</code> defines a set of possible
 * values for defining location of groups inside panels.
 * CURRENTLY NOT USED
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public enum GroupLocation implements Serializable{

    root, componentPanel, header, operationPanel, parameterPanel, toolbar, nextSubmenu, mainMenu;

    @Override
    public String toString() {
        String key = "groupLocation" + "." + name();
        return Intl.getValue(key);
    }
}
