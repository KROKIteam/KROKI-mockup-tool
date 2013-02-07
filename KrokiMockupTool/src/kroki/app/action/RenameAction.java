/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.dialog.RenameDialog;
import kroki.app.utils.StringResource;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class RenameAction extends AbstractAction {

    VisibleElement visibleElement;

    public RenameAction(VisibleElement visibleElement) {
        this.visibleElement = visibleElement;
        putValue(NAME, StringResource.getStringResource("action.rename.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.rename.description"));
    }

    public void actionPerformed(ActionEvent e) {
        RenameDialog dialog = new RenameDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), visibleElement);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
        if (visibleElement instanceof VisibleClass) {
            KrokiMockupToolApp.getInstance().getTabbedPaneController().setTitleAt((VisibleClass) visibleElement);
            KrokiMockupToolApp.getInstance().getTabbedPaneController().updateTabbedPane();
        }

    }
}
