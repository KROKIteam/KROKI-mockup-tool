package kroki.app.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.dialog.NewPackageDialog;
import kroki.app.utils.StringResource;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Shows dialog for inputting data regarding a new package
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class NewPackageAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private BussinesSubsystem bussinesSubsystem;

    public NewPackageAction(BussinesSubsystem bussinesSubsystem) {
        this.bussinesSubsystem = bussinesSubsystem;
        putValue(NAME, StringResource.getStringResource("action.newPackage.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.newPackage.description"));
    }

    public NewPackageAction() {
        putValue(NAME, StringResource.getStringResource("action.newPackage.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.newPackage.description"));
    }

    public void actionPerformed(ActionEvent e) {
        NewPackageDialog dialog = new NewPackageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), bussinesSubsystem);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
    }
}
