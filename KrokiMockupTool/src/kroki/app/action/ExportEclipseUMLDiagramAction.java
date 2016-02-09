package kroki.app.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTree;

import kroki.app.KrokiMockupToolApp;
import kroki.app.export.ExportProjectToEclipseUML;
import kroki.app.utils.FileChooserHelper;
import kroki.app.utils.StringResource;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Abstract action for the menu item that starts the export functionality for exporting Kroki project to Eclipse UML diagram files.
 * Exported Eclipse UML diagram can be version 4.0 or version 3.0 and UML elements can have applied stereotypes or not.
 * @author Zeljko Ivkovic
 *
 */
public class ExportEclipseUMLDiagramAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	/**
	 * Indicates if the exported Eclipse UML elements should have a corresponding 
	 * stereotypes applied.
	 */
	private boolean withStereotypes;
	
	/**
	 * Indicates if the exported Eclipse UML diagram should be version 4.0 or version 3.0.
	 */
	private boolean version4;
	
	/**
	 * Creates an action that exports Kroki project to a Eclipse UML 4.0 or 3.0 version
	 * diagram depending on the version4 parameter. Exported UML elements can have applied stereotypes or not depending on the
	 * value of the withStereotypes parameter.
	 * @param withStereotypes  <code>true</code> if the UML elements should have
	 * stereotypes applied, <code>false</code> if only UML elements should be exported
	 * without the stereotypes applied
	 * @param version4          <code>true</code> if the exported UML diagram should be a
	 * version 4.0, or <code>false</code> if the exported UML diagram should be version 3.0 
	 */
	public ExportEclipseUMLDiagramAction(boolean withStereotypes,boolean version4){
		this.withStereotypes=withStereotypes;
		this.version4=version4;
		String version="(3.0)";
		if(version4)
			version="(4.0)";
		if(withStereotypes)
		{
			putValue(NAME, changeValue(StringResource.getStringResource("action.export.eclipseUML.diagram.stereotype.name"),version));
			/*
			ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.smallicon"));
			ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.largeicon"));
			putValue(SMALL_ICON, smallIcon);
			putValue(LARGE_ICON_KEY, largeIcon);
			*/
			putValue(SHORT_DESCRIPTION, changeValue(StringResource.getStringResource("action.export.eclipseUML.diagram.stereotype.description"),version));
		}else
		{
			putValue(NAME, changeValue(StringResource.getStringResource("action.export.eclipseUML.diagram.name"),version));
			/*
			ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.smallicon"));
			ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.runswing.largeicon"));
			putValue(SMALL_ICON, smallIcon);
			putValue(LARGE_ICON_KEY, largeIcon);
			*/
			putValue(SHORT_DESCRIPTION, changeValue(StringResource.getStringResource("action.export.eclipseUML.diagram.description"),version));
		}
	}
	
	/**
	 * Action that starts a thread for creating Eclipse UML diagram file from a Kroki project.
	 * Checks if a project is selected for export and receives a file path where to save 
	 * the Eclipse UML diagram file. For exporting Kroki project to a Eclipse UML diagram
	 * and saving Eclipse UML diagram to a file abstract method executeExport is called.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				//find selected project from workspace
				BussinesSubsystem proj = null;
				if(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().isSelectionEmpty())
				{
					JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
					return;
				}
					
				Object selectedNoded = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath().getLastPathComponent();
				if(selectedNoded != null) {
					//if package is selected, find parent project
					if(selectedNoded instanceof BussinesSubsystem) {
						BussinesSubsystem subsys = (BussinesSubsystem) selectedNoded;
						proj = KrokiMockupToolApp.getInstance().findProject(subsys);
					}else if(selectedNoded instanceof VisibleClass) {
						//panel is selected, get parent node from tree and find project
						JTree tree = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree();
						Object parent = tree.getSelectionPath().getParentPath().getLastPathComponent();
						if(parent instanceof BussinesSubsystem) {
							proj = KrokiMockupToolApp.getInstance().findProject((BussinesSubsystem)parent);
						}
					}
				}

				if(proj != null) {					    
					File file=FileChooserHelper.fileChooser(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), true, "Export", "Eclipse UML diagram files","uml");
					if(file!=null)
					{
						//KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Exporting project to file "+file.getAbsolutePath()+". Please wait...", 0);
						new ExportProjectToEclipseUML(file, proj, withStereotypes, version4).execute();;
					}
					else
					{
						JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "Exporting project aborted.");
						//KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Exporting project aborted.", 0);
					}
				}else {
					//if no project is selected, inform user to select one
					JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
				}

			}
		});
		//thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
	}
	
	/**
	 * Changes a string that contains {0} to a specified value string.
	 * @param original  string containing {0} to be replaced
	 * @param value     new string to replace {0}
	 * @return          string with {0} replaced with the the value specified
	 */
	private String changeValue(String original,String value){
		return original.replace("{0}", value);
	}
}
