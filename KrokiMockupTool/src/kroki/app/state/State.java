/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.state;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import kroki.app.utils.StringResource;

/**
 * Аbstraktna klasa koja predstavlja stanje aplikacije. Ona implementira interfejs {@link  IState} ali metode ostavlja nerealizovane (prazne).
 * Na taj način ostavlja mogućnost svakom konkretnom stanju da implementira operacije koje su mu potrebne za rad i time smanji broj linija koda
 * i poveća preglednost i razumljivost koda.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public abstract class State implements IState {

    Context context;
    String name;
    String displayName;
    /*Konstante*/
    public static String ADD_STATE = "ADD_STATE";
    public static String MOVE_STATE = "MOVE_STATE";
    public static String SELECT_STATE = "SELECT_STATE";
    public static String RESIZE_STATE = "RESIZE_STATE";
    public static String TRANSFORM_TO_AGGREGATED_STATE = "TRANSFORM_TO_AGGREGATED_STATE";
    public static String TRANSFORM_TO_CALCULATED_STATE = "TRANSFORM_TO_CALCULATED_STATE";
    public static String TRANSFORM_TO_COMBOZOOM_STATE = "TRANSFORM_TO_COMBOZOOM_STATE";

    public State(Context context, String name) {
        this.context = context;
        this.name = name;
        this.displayName = StringResource.getStringResource(name);
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
