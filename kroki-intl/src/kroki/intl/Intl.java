package kroki.intl;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Class containing a method for reading values from a properties file
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class Intl {
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("kroki.intl.resources.Intl");
    private static HashMap<String, String> stringMap = new HashMap<String, String>();

    /**
     * Returns value corresponding to the given key in Intl.properties file
     * @param key Key
     * @return Value corresponding to the key
     */
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
