/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.utils;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Klasa koja je zaduzena za podesavanje look and feel-a aplikacije. Podrazumevani look and feel je windowsLookAndFeel
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class KrokiLookAndFeel {

    private static String lookAndFeelName = "windowslookandfeel";

    /**
     * Podešava look and feel na podrazumevanu vrednost.
     */
    public static void setLookAndFeel() {
        LookAndFeelInfo lfis[] = UIManager.getInstalledLookAndFeels();
        boolean found = false;
        String calssName = "";
        for (LookAndFeelInfo lfi : lfis) {
            if (lfi.getClassName().toLowerCase().contains(lookAndFeelName.toLowerCase())) {
                found = true;
                calssName = lfi.getClassName();
                break;
            }
        }
        try {
            if (found) {
                System.out.println(calssName);
                UIManager.setLookAndFeel(calssName);
            } else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setLookAndFeelName(String name) {
        lookAndFeelName = name;
    }
}
