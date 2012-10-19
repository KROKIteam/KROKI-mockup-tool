/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.gui.toolbar;

import javax.swing.JToolBar;
import kroki.app.action.NewFileAction;
import kroki.app.action.NewProjectAction;
import kroki.app.action.OpenProjectAction;
import kroki.app.action.RedoAction;
import kroki.app.action.SaveAction;
import kroki.app.action.SaveAllAction;
import kroki.app.action.UndoAction;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class MainToolbar extends JToolBar {

    public MainToolbar() {
        setOrientation(JToolBar.HORIZONTAL);
        setFloatable(false);
        initComponents();
    }

    private void initComponents() {
        setName("mainToolbar");
        add(new NewFileAction());
        add(new NewProjectAction());
        add(new OpenProjectAction());
        add(new SaveAction());
        add(new SaveAllAction());
        addSeparator();
        add(new UndoAction());
        add(new RedoAction());
    }
}
