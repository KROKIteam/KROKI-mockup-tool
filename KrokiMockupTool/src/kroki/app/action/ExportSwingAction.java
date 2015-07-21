package kroki.app.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import devHub.AppType;
import kroki.app.KrokiMockupToolApp;
import kroki.app.export.ProjectExporter;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Action that generates configuration xml files for swing app 
 * @author Kroki Team
 */
public class ExportSwingAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public ExportSwingAction() {
		putValue(NAME, "Export desktop application");
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.exportswing.smallicon"));
		ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.exportswing.largeicon"));
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.exportswing.description"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(KrokiMockupToolApp.getInstance().isBinaryRun()) {
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
				KrokiMockupToolApp.getInstance().displayTextOutput("Exporting project '" + proj.getLabel() + "'. Please wait...", 0);
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retValue = jfc.showSaveDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
				if (retValue == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					//pass selected project and directory to exporter class
					ProjectExporter exporter = new ProjectExporter(AppType.SWING);
					exporter.export(file, proj.getLabel().replace(" ", "_"), proj, "Project exported successfuly to " + file.getAbsolutePath());
				} else {
					KrokiMockupToolApp.getInstance().displayTextOutput("Export canceled by user.", 0);
				}

			} else {
				//if no project is selected, inform user to select one
				JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
			}
		}else {
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "Not available in the current release.");
		}
	}

}
