package kroki.app.action;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import net.sourceforge.plantuml.SourceStringReader;

import kroki.app.KrokiDiagramFrame;
import kroki.app.KrokiMockupToolApp;
import kroki.app.generators.UMLDescriptionGenerator;
import kroki.app.utils.DiagramProfile;
import kroki.profil.subsystem.BussinesSubsystem;

public class CreatePresistentDiagramAction extends AbstractAction {

	public CreatePresistentDiagramAction() {
		putValue(NAME, "Persistent profile");
		putValue(SHORT_DESCRIPTION, "Generate class diagram from project");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		UMLDescriptionGenerator descGen = new UMLDescriptionGenerator();

		//find selected project from workspace
		BussinesSubsystem proj = null;
		TreePath path = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath();
		if(path != null) {
			Object node = path.getLastPathComponent();
			if(node instanceof BussinesSubsystem) {
				proj = (BussinesSubsystem) node;
				KrokiDiagramFrame diagramFrame = new KrokiDiagramFrame();
				diagramFrame.setProjectName(proj.getLabel());

				String description = descGen.generateDescription(proj, DiagramProfile.PERSISTENT_PROFILE);
				//System.out.println("[DESC]\n" + description + "\n[/DESC]");
				
				diagramFrame.setCanvasDocument(description);
				diagramFrame.setVisible(true);
			}else {
				JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project or package from workspace!");
			}
		}

	}

}
