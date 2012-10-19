/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil;

import java.io.Serializable;

/**
 * Nabrojani tip  <code>ComponentType</code> definiše skup mogućih vrednosti za
 * vrstu komponente koja se može pridružiti elementu {@link VisibleElement}F
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public enum ComponentType implements Serializable {

    TEXT_FIELD,
    TEXT_AREA,
    COLUMN,
    CHECK_BOX,
    COMBO_BOX,
    SELECTION_LIST,
    RADIO_BUTTON,
    LABEL,
    IMAGE,
    TABBED_PANE,
    PANEL,
    GRID,
    BORDER,
    MENU,
    MENU_ITEM,
    BUTTON,
    LINK
}
