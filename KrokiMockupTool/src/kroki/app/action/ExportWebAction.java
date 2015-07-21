package kroki.app.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import devHub.AppType;
import kroki.app.KrokiMockupToolApp;
import kroki.app.export.ProjectExporter;
import kroki.app.gui.console.CommandPanel;
import kroki.app.gui.console.OutputPanel;
import kroki.app.utils.FileChooserHelper;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Action that generates configuration xml files for web app 
 * @author Kroki Team
 */
public class ExportWebAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public ExportWebAction() {
		putValue(NAME, "Export web application");
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.exportweb.smallicon"));
        ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.exportweb.largeicon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(LARGE_ICON_KEY, largeIcon);
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.exportweb.description"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		//find selected project from workspace
		final BussinesSubsystem proj = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getCurrentProject();
		
		if(proj != null) {
			boolean proceed = true;
			if(proj.getEclipseProjectPath() != null) {
				if(!FileChooserHelper.checkDirectory(proj)) {
					proceed = false;
					KrokiMockupToolApp.getInstance().displayTextOutput("The selected project has associated Eclipse project path, but is seems to be missing or corrupted. Please review these settings in the project properties panel.", OutputPanel.KROKI_WARNING);
				}
			}
			if (proceed) {
				KrokiMockupToolApp.getInstance().displayTextOutput("Exporting project '" + proj.getLabel() + "'. Please wait...", 0);
				final JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retValue = jfc.showSaveDialog(KrokiMockupToolApp
						.getInstance().getKrokiMockupToolFrame());
				if (retValue == JFileChooser.APPROVE_OPTION) {
					Thread exportThread = new Thread(new Runnable() {
						@Override
						public void run() {
							KrokiMockupToolApp
									.getInstance()
									.getKrokiMockupToolFrame()
									.setCursor(
											Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							File file = jfc.getSelectedFile();
							//pass selected project and directory to exporter class
							ProjectExporter exporter = new ProjectExporter(AppType.WEB);

							exporter.export(
									file,
									proj.getLabel().replace(" ", "_"),
									proj,
									"Project exported successfuly to "
											+ file.getAbsolutePath());
						}
					});
					exportThread.setPriority(Thread.NORM_PRIORITY);
					exportThread.start();
					KrokiMockupToolApp
							.getInstance()
							.getKrokiMockupToolFrame()
							.setCursor(
									Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				} else {
					KrokiMockupToolApp.getInstance().displayTextOutput("Export canceled by user.", 0);
				}
			}
		} else {
			//if no project is selected, inform user to select one
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
		}
	}
}
