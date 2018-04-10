package kroki.app.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
import kroki.app.export.ProjectExporter;
import kroki.app.gui.console.OutputPanel;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.profil.subsystem.BussinesSubsystem;

public class ValidateProjectAction extends AbstractAction {

	public ValidateProjectAction() {
		putValue(NAME, StringResource.getStringResource("action.validate.name"));
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.validate.description"));
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.validate.smallIcon"));
        ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.validate.largeIcon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(LARGE_ICON_KEY, largeIcon);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		BussinesSubsystem proj = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getCurrentProject();
		if(proj != null) {
			KrokiMockupToolApp.getInstance().displayTextOutput("Validating project '" + proj.getLabel() + "'. Please wait...", OutputPanel.KROKI_RESPONSE);
			ProjectExporter exporter = new ProjectExporter(false);
			exporter.validateProject(proj);
		}else {
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
		}
	}

}
