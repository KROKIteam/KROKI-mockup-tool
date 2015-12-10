package kroki.app.utils;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Class which is used to set look and feel of the application.
 * WindowsLookAndFeel is the default look and feel
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class KrokiLookAndFeel {

    private static String lookAndFeelName = "Synth";

    /**
     * Sets look and feel to the default value
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
