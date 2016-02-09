
package kroki.app.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
import kroki.app.export.ExportProjectToEclipseUML;
import kroki.app.export.ProjectExporter;
import kroki.app.gui.console.CommandPanel;
import kroki.app.gui.console.OutputPanel;
import kroki.app.utils.FileChooserHelper;
import kroki.app.utils.ImageResource;
import kroki.app.utils.RunAnt;
import kroki.app.utils.StringResource;
import kroki.app.utils.uml.KrokiComponentOutputMessage;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.DatabaseProps;

import org.apache.commons.io.FileDeleteStrategy;

/**
 * Action that runs selected project as web application
 * @author Milorad Filipovic
 */
public class RunWebAction extends AbstractAction {

	public RunWebAction() {
		putValue(NAME, "Run web version");
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.runweb.smallicon"));
        ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.runweb.largeicon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(LARGE_ICON_KEY, largeIcon);
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.runweb.description"));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				//find selected project from workspace
				final BussinesSubsystem proj = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getCurrentProject();
				
				if(proj != null) {
					try {
						KrokiMockupToolApp.getInstance().displayTextOutput("Exporting project '" + proj.getLabel() + "'. Please wait...", 0);

						Thread runThread = new Thread(new Runnable() {
							@Override
							public void run() {
								boolean proceed = true;
								if(proj.getEclipseProjectPath() != null) {
									if(!FileChooserHelper.checkDirectory(proj)) {
										proceed = false;
										KrokiMockupToolApp.getInstance().displayTextOutput("The selected project has associated Eclipse project path, but is seems to be missing or corrupted. Please review these settings in the project properties panel.", OutputPanel.KROKI_WARNING);
									}
								}
								
								if(proceed) {
									KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
									SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyHmm");
									Date today = new Date();
									String dateSuffix = formatter.format(today);
									String jarName = proj.getLabel().replace(" ", "_") + "_WEB_" + formatter.format(today);
									
									//get temporary location in KROKI directory
									File f = new File(".");
									String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1) + "Temp";
									File tempDir = new File(appPath);

									//generate connection settings for embedded h2 database
									DatabaseProps tempProps = new DatabaseProps();
									//proj.setDBConnectionProps(tempProps);
									ProjectExporter exporter = new ProjectExporter(false);
									
									KrokiMockupToolApp.getInstance().displayTextOutput("Generating UML model...", 0);
									File tempUMLFile = new File(tempDir.getAbsolutePath() + File.separator + jarName + ".uml");
									try{
										new ExportProjectToEclipseUML(tempUMLFile, proj, true, true).exportToUMLDiagram(new KrokiComponentOutputMessage(), ExportProjectToEclipseUML.MESSAGES_FOR_CLASS, false);
										exporter.export(tempDir, jarName, proj, "Project exported OK! Running project...");
										//run exported jar file
										RunAnt runner = new RunAnt();
										runner.runRun(proj, tempDir, false, jarName);
									}catch(Exception e){
										/*
										 * Ovde bi trebalo ispisati gresku kada exort nije uspeo i verovatno zaustaviti dalje pokretanje
										 */
										e.printStackTrace();
									}
								}
							}
						});
						runThread.setPriority(Thread.NORM_PRIORITY);
						runThread.start();
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					} catch (Exception e) {
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
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
