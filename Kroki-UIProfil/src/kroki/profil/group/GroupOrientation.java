/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.group;

import java.io.Serializable;
import kroki.intl.Intl;

/**
 * NE KORISTI SE ZA SAD
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
