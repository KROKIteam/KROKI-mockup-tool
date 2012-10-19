/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kroki.intl;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Intl {
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("kroki.intl.resources.Intl");
    private static HashMap<String, String> stringMap = new HashMap<String, String>();

    public static String getValue(String key) {
        String value;
        if (stringMap.containsKey(key)) {
            value = stringMap.get(key);
        } else {
            try {
                value = resourceBundle.getString(key);
            } catch (Exception e) {
                value = key;
            }
            stringMap.put(key, value);
        }
        return value;
    }
}
