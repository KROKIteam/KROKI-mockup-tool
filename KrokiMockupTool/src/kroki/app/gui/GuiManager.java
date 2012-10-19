/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.gui;

import kroki.app.gui.toolbar.MainToolbar;
import kroki.app.gui.toolbar.StyleToolbar;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class GuiManager {
    
    private Palette pallete = new Palette();
    private MainToolbar mainToolbar = new MainToolbar();
    private StyleToolbar styleToolbar = new StyleToolbar();

    public GuiManager() {
    }

    public Palette getPallete() {
        return pallete;
    }

    public MainToolbar getMainToolbar() {
        return mainToolbar;
    }

    public StyleToolbar getStyleToolbar() {
        return styleToolbar;
    }
}
