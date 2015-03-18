/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.property;

import java.io.Serializable;
import kroki.intl.Intl;

/**
 * Nabrojani tip  AggregateFunction definiÅ¡e skup funkcija za agregaciju
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public enum AggregateFunciton implements Serializable{

    MIN, MAX, SUM, AVG, COUNT;

    @Override
    public String toString() {
        String key = "aggregateFunction" + "." + name();
        return Intl.getValue(key);
    }
}
