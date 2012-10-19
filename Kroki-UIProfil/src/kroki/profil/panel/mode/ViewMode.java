/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.panel.mode;

import java.io.Serializable;
import kroki.intl.Intl;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public enum ViewMode implements Serializable{

    TABLE_VIEW, INPUT_PANEL_MODE;

    @Override
    public String toString() {
        String key = "viewMode" + "." + name();
        return Intl.getValue(key);
    }
}
