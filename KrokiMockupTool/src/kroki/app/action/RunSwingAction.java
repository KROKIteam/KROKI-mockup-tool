package kroki.app.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import devHub.AppType;
import kroki.app.KrokiMockupToolApp;
import kroki.app.export.ProjectExporter;
import kroki.app.utils.ImageResource;
import kroki.app.utils.RunAnt;
import kroki.app.utils.StringResource;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Action that runs selected project as Java Swing application
 * @author Milorad Filipovic
 */
public class RunSwingAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public RunSwingAction() {
		putValue(NAME, "Run desktop version");
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.smallicon"));
		ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.largeicon"));
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.runswing.description"));
	}

	/**
	 * When action is performed, selected project gets exported to temporary location
	 * and embedded h2 database is ran.
	 */
	public void actionPerformed(ActionEvent arg0) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				//find selected project from workspace
				BussinesSubsystem proj = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getCurrentProject();

				if(proj != null) {
					try {
						KrokiMockupToolApp.getInstance().displayTextOutput("Exporting project '" + proj.getLabel() + "'. Please wait...", 0);
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

						//get temporary location in KROKI directory
						File f = new File(".");
						String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1) + "Temp";
						File tempDir = new File(appPath);

						//deleteFiles(tempDir);

						//generate connection settings for embedded h2 database
						//DatabaseProps tempProps = new DatabaseProps();
						//proj.setDBConnectionProps(tempProps);
						
						ProjectExporter exporter = new ProjectExporter(AppType.SWING);
						exporter.export(tempDir, proj.getLabel().replace(" ", "_"), proj, "Project exported OK! Running project...");

						
						//run exported jar file
						RunAnt runner = new RunAnt();
						runner.runRun(proj, tempDir, true, null);
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					} catch (NullPointerException e) {
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						KrokiMockupToolApp.getInstance().displayTextOutput("An error occured. Running aborted", 3);
						e.printStackTrace();
					}
				}else {
					//if no project is selected, inform user to select one
					JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}

			}
		});
		if(KrokiMockupToolApp.getInstance().isBinaryRun()) {
			thread.setPriority(Thread.NORM_PRIORITY);
			thread.start();
		}else {
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "Not available in the current release.");
		}
	}

	public boolean deleteFiles(File directory) {
		boolean success = false;

		if (!directory.exists()) {
			return false;
		}
		if (!directory.canWrite()) {
			return false;
		}

		File[] files = directory.listFiles();
		for(int i=0; i<files.length; i++) {
			File file = files[i];
			if(file.isDirectory()) {
				deleteFiles(file);
			}
			if(!file.delete()) {
				success = false;
			}
		}
		return success;
	}
}