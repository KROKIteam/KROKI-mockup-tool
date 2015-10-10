package kroki.profil.utils;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import org.apache.commons.lang.StringUtils;

/**
 * Class contains name transformation methods
 * @author Kroki Team
 */
public class NamingUtils {

    private static String[] searchList = {"Ã„", "Ã¤", "Ã–", "Ã¶", "Ãœ", "Ã¼", "ÃŸ", "Å ", "Å¡", "Ä�", "Ä‘", "ÄŒ", "Ä�", "Ä†", "Ä‡", "Å½", "Å¾"};
    private static String[] replaceList = {"Ae", "ae", "Oe", "oe", "Ue", "ue", "sz", "Sh", "sh", "Dj", "dj", "Ch", "ch", "Cj", "cj", "Z", "z"};

    /**
     * Normalizes a String by removing all accents to original 127 US-ASCII
     * characters. This method handles German umlauts and "sharp-s" correctly
     *
     * @param s The String to normalize
     * @return The normalized String
     */
    public static String normalize(String s) {
        if (s == null) {
            return null;
        }

        String n = null;
        n = StringUtils.replaceEachRepeatedly(s, searchList, replaceList);
        n = Normalizer.normalize(n, Form.NFD).replaceAll("[^\\p{ASCII}]", "");

        return n;
    }

    /**
     * Returns a clean representation of a String which might be used safely
     * within an URL. Slugs are a more human friendly form of URL encoding a
     * String.
     * <p>
     * The method first normalizes a String, then converts it to lowercase and
     * removes ASCII characters, which might be problematic in URLs:
     * <ul>
     * <li>all whitespaces
     * <li>dots ('.')
     * <li>slashes ('/')
     * <li>colon (':')
     * <li>semicolon (';')
     * <li>Less than
     * <li>Equals
     * <li>Question mark
     * </ul>
     *
     * @param s The String to slugify
     * @return The slugified String
     * @see #normalize(String)
     */
    public static String slugify(String s) {

        if (s == null) {
            return null;
        }

        String n = normalize(s);
        //n = StringUtils.lowerCase(n);
        n = n.replaceAll("[\\s.]", "_");
        n = n.replaceAll("[/:;<>=?~^%&*#!${}|,.\\(.+\\)\\[.+\\]]", "");

        return n;
    }

    public static void main(String[] args) {
        String a = "Naseljeno   messto";
        String b = slugify(a);
        System.out.println("a: " + a);
        System.out.println("b: " + b);
    }
}
