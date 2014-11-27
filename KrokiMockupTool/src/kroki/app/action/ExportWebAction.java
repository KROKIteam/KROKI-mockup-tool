package kroki.app.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import kroki.app.KrokiMockupToolApp;
import kroki.app.export.ProjectExporter;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;


public class ExportWebAction extends AbstractAction {

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
		BussinesSubsystem proj = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getCurrentProject();
		
		if(proj != null) {
			KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Exporting project '" + proj.getLabel() + "'. Please wait...", 0);
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int retValue = jfc.showSaveDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
			if (retValue == JFileChooser.APPROVE_OPTION) {
				File file = jfc.getSelectedFile();
				//pass selected project and directory to exporter class
				ProjectExporter exporter = new ProjectExporter(false);
				
				exporter.export(file, proj.getLabel().replace(" ", "_") ,proj, "Project exported successfuly to " + file.getAbsolutePath());
			} else {
				KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Export canceled by user.", 0);
			}
		} else {
			//if no project is selected, inform user to select one
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
		}
	}
}
