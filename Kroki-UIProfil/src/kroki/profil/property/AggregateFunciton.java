package kroki.profil.property;

import java.io.Serializable;
import kroki.intl.Intl;

/**
 * Defines a set of aggregation functions
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
