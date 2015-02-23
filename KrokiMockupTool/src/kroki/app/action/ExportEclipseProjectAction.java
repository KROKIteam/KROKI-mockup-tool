package kroki.app.action;

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

import org.apache.commons.io.FileUtils;

import kroki.app.KrokiMockupToolApp;
import kroki.app.export.ProjectExporter;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;
import static java.nio.file.StandardCopyOption.*;

public class ExportEclipseProjectAction extends AbstractAction {

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
		BussinesSubsystem proj = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getCurrentProject();

		if(proj != null) {
			//get selected item from jtree and find its project
			TreePath path =  KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath();
			Object node = path.getLastPathComponent();
			if(node != null) {
				//if package is selected, find parent project
				if(node instanceof BussinesSubsystem) {
					BussinesSubsystem subsys = (BussinesSubsystem) node;
					proj = KrokiMockupToolApp.getInstance().findProject(subsys);
				}else if(node instanceof VisibleClass) {
					//panel is selected, get parent node from tree and find project
					JTree tree = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree();
					Object parent = tree.getSelectionPath().getParentPath().getLastPathComponent();
					if(parent instanceof BussinesSubsystem) {
						proj = KrokiMockupToolApp.getInstance().findProject((BussinesSubsystem)parent);
					}
				}
			}
			// If the Kroki project is exported for the first time, both the Application repository and Web app need to be exported
			// Else, only the Application repository is exported, so the manualy added files are kept in WebApp
			boolean needsApp = false;
			
			// If the project doesn't have asoiciated Eclipse directory with it, chose one
			if(proj.getEclipseProjectPath() == null) {
				NamingUtil namer = new NamingUtil();
				KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Exporting project '" + proj.getLabel() + "'. Please wait...", 0);
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retValue = jfc.showSaveDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
				if (retValue == JFileChooser.APPROVE_OPTION) {
					File projectFile = new File(jfc.getSelectedFile(), namer.toCamelCase(proj.getLabel(), false));
					proj.setEclipseProjectPath(projectFile);
					// Set flag for the first export
					needsApp = true;
				}
			}
			
			System.out.println("Needs APP: " + needsApp);
			if(proj.getEclipseProjectPath() != null) {
				if(!proj.getEclipseProjectPath().exists()) {
					needsApp = true;
				}
				
				ProjectExporter exporter = new ProjectExporter(false);
				exporter.generateAppAndRepo(proj, null);
				
				File rootFile = new File(".");
				String appPath = rootFile.getAbsolutePath().substring(0,rootFile.getAbsolutePath().length()-18);
				
				// WebApp and ApplicationRepository directories need to be copied to a selected location
				File repositoryDirSrc = new File(appPath + File.separator + "ApplicationRepository");
				File repositoryDirDest = new File(proj.getEclipseProjectPath().getAbsolutePath() + File.separator + "ApplicationRepository");
				
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
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Unable to locate generated application repository!", 3);
				}
				
				// If needed, copy WebApp files
				if(needsApp) {
					File appDirScr = new File(appPath + File.separator + "WebApp");
					File appDirDest = new File(proj.getEclipseProjectPath().getAbsolutePath() + File.separator + "WebApp");
					
					if(appDirScr.exists()) {
						appDirDest.mkdir();
						try {
							FileUtils.copyDirectory(appDirScr, appDirDest);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else {
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Unable to locate generated web application!", 3);
					}
				}
				KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Project exported successfuly to " + proj.getEclipseProjectPath().getAbsolutePath() + ". It can now be imported to Eclipse IDE", 0);
			}
		} else {
			//if no project is selected, inform user to select one
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
		}
	}
}
