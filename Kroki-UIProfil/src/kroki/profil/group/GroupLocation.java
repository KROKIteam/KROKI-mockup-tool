/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.group;

import java.io.Serializable;
import kroki.intl.Intl;

/**
 * Nabrojani tip GroupLocation definiše skup vrednosti za definisanje
 * lokacije grupe u okviru panela.
 * NE KORISTI SE ZA SAD
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
