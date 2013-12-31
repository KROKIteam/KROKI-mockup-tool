package kroki.app.action;

import java.io.File;

import kroki.app.export.ExportProjectToEclipseUML;
import kroki.app.utils.StringResource;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Action that exports Kroki project to version 4.0 Eclipse UML diagram file.
 * @author Zeljko Ivkovic
 *
 */
public class ExportEclipseUMLDiagramAction4version extends ExportEclipseUMLDiagramAction{

	public ExportEclipseUMLDiagramAction4version(){
		putValue(NAME, StringResource.getStringResource("action.export.eclipseUML.diagram.version4.name"));
		/*
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.smallicon"));
		ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.largeicon"));
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		*/
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.export.eclipseUML.diagram.version4.description"));
	}

	@Override
	public void executeExport(File file, BussinesSubsystem project) {
		try {
			(new ExportProjectToEclipseUML(file,project)).executeExport();
		} catch (Exception e) {
			//e.printStackTrace();
			//in method executeExport of class ExportProjectToEclipseUML errors are writen to the Console window in the main form of the Kroki application
		}
	}

}
