package kroki.app.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
import kroki.app.importt.ImportEclipseUMLToProject;
import kroki.app.utils.FileChooserHelper;
import kroki.app.utils.StringResource;
import kroki.app.utils.uml.SettingsForUMLImportDialog;

/**
 * Action for the menu item that starts the import functionality for importing Eclipse UML diagram files to Kroki project.
 * @author Zeljko Ivkovic
 *
 */
public class ImportEclipseUMLDiagramAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for the action that starts the import functionality.
	 */
	public ImportEclipseUMLDiagramAction(){
		putValue(NAME, StringResource.getStringResource("action.import.eclipseUML.diagram.name"));
		/*
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.smallicon"));
		ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.largeicon"));
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		*/
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.import.eclipseUML.diagram.description"));
	}
	
	/**
	 * Action that starts a thread for creating a Kroki project from Eclipse UML diagram file.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				File file=FileChooserHelper.fileChooser(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), false, "Import", "Eclipse UML diagram files", "uml");
				if(file!=null)
				{
					
					//KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Importing Eclipse UML diagram from file "+file.getAbsolutePath()+". Please wait...", 0);
					SettingsForUMLImportDialog frame=new SettingsForUMLImportDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
					frame.setVisible(true);
					if(frame.isOK())
					{
						try{
							new ImportEclipseUMLToProject(file,frame.getTextsToBeRemoved());
						}catch(Exception e){
							e.printStackTrace(); 
						}
					}else
					{
						JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "Importing eclipse UML diagram aborted.");
					}
					
				}
				else
				{
					//KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Importing eclipse UML diagram aborted.", 0);
					JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "Importing eclipse UML diagram aborted.");
				}
			}
		});
		thread.start();
	}

}
