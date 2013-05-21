package kroki.app.action;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import kroki.app.KrokiDiagramFrame;
import kroki.app.KrokiMockupToolApp;
import kroki.app.generators.UMLDescriptionGenerator;
import kroki.app.utils.StringResource;
import kroki.profil.subsystem.BussinesSubsystem;

public class CreateDiagramAction extends AbstractAction {

	public CreateDiagramAction() {
		putValue(NAME, "Generate class diagram");
		putValue(SHORT_DESCRIPTION, "Generate class diagram from project");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		UMLDescriptionGenerator descGen = new UMLDescriptionGenerator();
		
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
		
			KrokiDiagramFrame diagramFrame = new KrokiDiagramFrame();
			diagramFrame.setProjectName(proj.getLabel());
			
			String description = descGen.generateDescription(proj);
			diagramFrame.setCanvasDocument(description);
			
			diagramFrame.setVisible(true);
		}catch (NullPointerException ex) {
			//if no project is selected, inform user to select one
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
			ex.printStackTrace();
		}
	}

}
