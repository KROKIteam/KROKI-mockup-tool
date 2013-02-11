package kroki.app.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.panelcomposer.core.MainApp;

import kroki.app.KrokiMockupToolApp;
import kroki.app.export.SwingExporter;
import kroki.app.utils.ImageResource;
import kroki.app.utils.RunAnt;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.DatabaseProps;

/**
 * Action that runs selected project as Java Swing application
 * @author Milorad Filipovic
 */
public class RunSwingAction extends AbstractAction {

	public RunSwingAction() {
		putValue(NAME, "Desktop version");
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.smallicon"));
        ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.largeicon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(LARGE_ICON_KEY, largeIcon);
	}

	/**
	 * When action is performed, selected project gets exported to temporary location
	 * and embedded h2 database is ran.
	 */
	public void actionPerformed(ActionEvent arg0) {
		//find selected project from workspace
		BussinesSubsystem proj = null;
		try {
			String selectedNoded = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath().getLastPathComponent().toString();
			for(int j=0; j<KrokiMockupToolApp.getInstance().getWorkspace().getPackageCount(); j++) {
				BussinesSubsystem pack = (BussinesSubsystem)KrokiMockupToolApp.getInstance().getWorkspace().getPackageAt(j);
				if(pack.getLabel().equals(selectedNoded)) {
					proj = pack;
				}
			}

			//get temporary location in KROKI directory
			File f = new File(".");
			String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1) + "Temp";
			File tempDir = new File(appPath);
			
			//generate connection settings foe embedded h2 database
			DatabaseProps tempProps = new DatabaseProps();
			//proj.setDBConnectionProps(tempProps);
			SwingExporter exporter = new SwingExporter();
			exporter.export(tempDir, proj);
			
			//run exported jar file
			RunAnt runner = new RunAnt();
			runner.runRun(proj.getLabel().replace(" ", "_"), tempDir);
			
		} catch (NullPointerException e2) {
			//if no project is selected, inform user to select one
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
		}
	}
}