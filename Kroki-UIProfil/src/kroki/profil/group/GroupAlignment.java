/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.group;

import java.io.Serializable;
import kroki.intl.Intl;

/**
 * Nabrojani tip  GroupAlignment definiše skup mogućih vrednosti za
 * definisanje poravnanja elemenata u okviru grupe. Nemaju smisla za grupe
 * koje modeluju meni.
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
