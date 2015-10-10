package kroki.app.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

import kroki.app.KrokiMockupToolApp;
import kroki.app.export.ProjectExporter;
import kroki.app.gui.console.CommandPanel;
import kroki.app.gui.console.OutputPanel;
import kroki.app.utils.FileChooserHelper;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;
import static java.nio.file.StandardCopyOption.*;

public class ExportEclipseProjectAction extends AbstractAction {

	boolean needsApp = false;
	BussinesSubsystem proj;

	public ExportEclipseProjectAction() {
		putValue(NAME, "Export as Eclipse project");
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.exporteclipse.smallicon"));
		ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.exporteclipse.largeicon"));
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.exporteclipse.description"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//find selected project from workspace
		proj = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getCurrentProject();
		boolean proceed = true;

		if(proj != null) {
			// If the Kroki project is exported for the first time, both the Application repository and Web app need to be exported
			// Else, only the Application repository and src_gen from the web app are exported, so the manually added files are kept in WebApp
			KrokiMockupToolApp.getInstance().displayTextOutput("Exporting project '" + proj.getLabel() + "'. Please wait...", OutputPanel.KROKI_RESPONSE);

			// If the project doesn't have asoiciated Eclipse directory with it, chose one
			if(proj.getEclipseProjectPath() == null) {
				NamingUtil namer = new NamingUtil();
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retValue = jfc.showSaveDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
				if (retValue == JFileChooser.APPROVE_OPTION) {
					File projectFile = new File(jfc.getSelectedFile(), namer.toCamelCase(proj.getLabel(), false));
					proj.setEclipseProjectPath(projectFile);
					// Set flag for the first export
					needsApp = true;
				}
			}else {
				// If it has been exported before check the existing linked Eclipse project, and export to it if everything is ok
				System.out.println("ELSE: " + proj.getEclipseProjectPath());
				proceed = FileChooserHelper.checkDirectory(proj);
				System.out.println("[ECLIPSE PROJECT EXPORT] Project directory ok! Exporting project...");
			}

			if(proceed) {
				// If at this point the associated Eclipse directory does not exist for some reason, export everything
				if(proj.getEclipseProjectPath() != null) {
					if(!proj.getEclipseProjectPath().exists()) {
						needsApp = true;
					}

					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							// Generate application repository and web app contents
							ProjectExporter exporter = new ProjectExporter(false);
							exporter.generateAppAndRepo(proj, null);
							exporter.writeProjectName(proj.getLabel(), proj.getProjectDescription());

							File rootFile = new File(".");
							String appPath = rootFile.getAbsolutePath().substring(0,rootFile.getAbsolutePath().length()-18);
							if(KrokiMockupToolApp.getInstance().isBinaryRun()) {
								appPath = rootFile.getAbsolutePath();
							}

							// WebApp and ApplicationRepository directories need to be copied to a selected location
							File repositoryDirSrc = new File(appPath + File.separator + "ApplicationRepository");
							File repositoryDirDest = new File(proj.getEclipseProjectPath().getAbsolutePath() + File.separator + "ApplicationRepository");
							File appDirScr = new File(appPath + File.separator + "WebApp");
							File appDirDest = new File(proj.getEclipseProjectPath().getAbsolutePath() + File.separator + "WebApp");

							// If needed, copy WebApp files
							if(needsApp) {
								if(appDirScr.exists()) {
									appDirDest.mkdir();
									try {
										FileUtils.copyDirectory(appDirScr, appDirDest);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}else {
									KrokiMockupToolApp.getInstance().displayTextOutput("Unable to locate generated web application!", OutputPanel.KROKI_ERROR);
								}
							}

							// Copy the exported application repository
							if(repositoryDirSrc.exists()) {
								try {
									proj.getEclipseProjectPath().mkdir();
									repositoryDirDest.mkdir();
									FileUtils.copyDirectory(repositoryDirSrc, repositoryDirDest);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}else {
								KrokiMockupToolApp.getInstance().displayTextOutput("Unable to locate generated application repository!", OutputPanel.KROKI_ERROR);
							}
							// Copy generated src folder
							boolean exportOK = true;
							if(appDirScr.exists()) {
								File ejbSrc = new File(appDirScr.getAbsolutePath() + File.separator + "src_gen");
								if(ejbSrc.exists()) {
									if(appDirDest.exists()) {
										File ejbDest = new File(appDirDest.getAbsolutePath() + File.separator + "src_gen");
										if(ejbDest.exists()) {
											// First cleanup the destination folder
											deleteFiles(ejbDest);
											try {
												FileUtils.copyDirectory(ejbSrc, ejbDest);
											} catch (IOException e) {
												e.printStackTrace();
											}
										}else {
											KrokiMockupToolApp.getInstance().displayTextOutput("Unable to locate src_gen location in associated folder!", OutputPanel.KROKI_ERROR);
											exportOK = false;
										}
									}else {
										KrokiMockupToolApp.getInstance().displayTextOutput("Unable to locate WebApp location in associated folder!", OutputPanel.KROKI_ERROR);
										exportOK = false;
									}
								}else {
									KrokiMockupToolApp.getInstance().displayTextOutput("Unable to locate generated src_gen location!", OutputPanel.KROKI_ERROR);
									exportOK = false;
								}
							}else {
								KrokiMockupToolApp.getInstance().displayTextOutput("Unable to locate generated WebApp location!", OutputPanel.KROKI_ERROR);
								exportOK = false;
							}
							if(exportOK) {
								KrokiMockupToolApp.getInstance().displayTextOutput("Project exported successfuly to " + proj.getEclipseProjectPath().getAbsolutePath() + ". It can now be imported to Eclipse IDE", OutputPanel.KROKI_FINISHED);
							}else {
								KrokiMockupToolApp.getInstance().displayTextOutput("Project export failed.", 3);
							}
							KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						}
					});
					thread.setPriority(Thread.NORM_PRIORITY);
					thread.start();
				}
			} else {
				//if no project is selected, inform user to select one
				JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
			}
		}
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Deletes all files from a directory
	 */
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
			try {
				FileDeleteStrategy.FORCE.delete(file);
				success =  !file.delete();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return success;
	}
}
