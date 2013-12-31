package kroki.app.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UML302UMLResource;

import sun.misc.IOUtils;
import net.sourceforge.plantuml.FileUtils;
import kroki.app.export.ExportProjectToEclipseUML;
import kroki.app.utils.StringResource;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Action that exports Kroki project to version 3.0 Eclipse UML diagram file.
 * @author Zeljko Ivkovic
 *
 */
public class ExportEclipseUMLDiagramAction3version extends ExportEclipseUMLDiagramAction{

	public ExportEclipseUMLDiagramAction3version(){
		putValue(NAME, StringResource.getStringResource("action.export.eclipseUML.diagram.version3.name"));
		/*
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.smallicon"));
		ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.largeicon"));
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		*/
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.export.eclipseUML.diagram.version3.description"));
	}
	
	@Override
	public void executeExport(File file, BussinesSubsystem project) {
		try{
			(new ExportProjectToEclipseUML(file,project)).executeExport();
			changeVersion(file);
		}catch(Exception e)
		{
			//e.printStackTrace();
			//in method executeExport of class ExportProjectToEclipseUML errors are writen to the Console window in the main form of the Kroki application
		}
	}
	
	/**
	 * Change version of the exported uml diagram 4.0 file to version 3.0.
	 * @param file File with uml diagram for which to change version of uml
	 * diagram
	 */
	protected void changeVersion(File file){
		try{
		BufferedReader reader = new BufferedReader( new FileReader (file));
		String         line = null;
		StringBuilder  stringBuilder = new StringBuilder();
		String         ls = System.getProperty("line.separator");
	
		while( ( line = reader.readLine() ) != null ) {
			stringBuilder.append( line );
		    stringBuilder.append( ls );
		}

		reader.close();
		
	    String fileAsString=stringBuilder.toString();
	    
	    fileAsString = fileAsString.replace(UMLPackage.eNS_URI, UML302UMLResource.UML_METAMODEL_NS_URI);
	    
	    PrintWriter printWriter=new PrintWriter(file);
	    
	    printWriter.write(fileAsString);
	    printWriter.flush();
	    printWriter.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
