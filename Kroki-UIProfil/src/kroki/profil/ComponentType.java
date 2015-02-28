/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil;

import java.io.Serializable;

/**
 * Enumerated type <code>ComponentType</code> defining a set of possible values
 * of component type which an instance of {@link VisibleElement} can have
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
