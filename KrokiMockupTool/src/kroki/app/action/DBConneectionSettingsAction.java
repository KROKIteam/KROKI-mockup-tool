package kroki.app.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.dialog.DBConnectionPropsDialog;
import kroki.app.utils.StringResource;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Shows database connection settings dialog 
 * @author Kroki Team
 */
public class DBConneectionSettingsAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public DBConneectionSettingsAction() {
		putValue(NAME, StringResource.getStringResource("action.dbsetting.name"));
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.dbsetting.description"));
	}

	public void actionPerformed(ActionEvent e) {

		BussinesSubsystem proj = null;
		try {
			String selectedNoded = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath().getLastPathComponent().toString();
			for(int j=0; j<KrokiMockupToolApp.getInstance().getWorkspace().getPackageCount(); j++) {
				BussinesSubsystem pack = (BussinesSubsystem)KrokiMockupToolApp.getInstance().getWorkspace().getPackageAt(j);
				if(pack.getLabel().equals(selectedNoded)) {
					proj = pack;
				}
			}
		} catch (NullPointerException e2) {
		}


		if(proj!= null) {
			DBConnectionPropsDialog settingsDialog = new DBConnectionPropsDialog(proj);
			settingsDialog.setVisible(true);
		}else {
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
		}
	}

}
