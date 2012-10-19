/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.utils;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Utils klasa koja na osnovu ključa vraća resurs koji je tekst.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class StringResource {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("kroki.resources.KrokiMockupToolApp");
    private static HashMap<String, String> stringMap = new HashMap<String, String>();

    public static String getStringResource(String key) {
        String value;
        if (stringMap.containsKey(key)) {
            value = stringMap.get(key);
        } else {
            value = resourceBundle.getString(key);
            stringMap.put(key, value);
        }
        return value;
    }
}
