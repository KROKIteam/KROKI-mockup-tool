package kroki.app.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.RootPaneContainer;

import kroki.app.KrokiMockupToolApp;
import kroki.app.export.ExportProjectToEclipseUML;
import kroki.app.utils.FileChooserHelper;
import kroki.app.utils.StringResource;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Abstract action for the menu item that starts the export functionality for exporting Kroki project to Eclipse UML diagram files.
 * Contains abstract method executeExport that needs to be implemented for exporting Kroki project to Eclipse UML diagram file.
 * @author Zeljko Ivkovic
 *
 */
public abstract class ExportEclipseUMLDiagramAction extends AbstractAction {

	
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
				
				//KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setVisible(true);
				if(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().isSelectionEmpty())
				{
					JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
					//KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setVisible(true);
					return;
				}
					
				String selectedNoded = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath().getLastPathComponent().toString();
				for(int j=0; j<KrokiMockupToolApp.getInstance().getWorkspace().getPackageCount(); j++) {
					BussinesSubsystem pack = (BussinesSubsystem)KrokiMockupToolApp.getInstance().getWorkspace().getPackageAt(j);
					if(pack.getLabel().equals(selectedNoded)) {
						proj = pack;
					}
				}

				if(proj != null) {					    
					File file=FileChooserHelper.fileChooser(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), true, "Export", "Eclipse UML diagram files","uml");
					if(file!=null)
					{
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Exporting project to file "+file.getAbsolutePath()+". Please wait...", 0);
						executeExport(file,proj);
					}
					else
					{
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Exporting project aborted.", 0);
					}
						//KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					    ((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setVisible(true);
					    /**/
					/*
					} catch (NullPointerException e) {
						//KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					    ((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setVisible(true);
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("An error occured. Running aborted", 3);
						e.printStackTrace();
					}
					*/
				}else {
					//if no project is selected, inform user to select one
					JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
					//KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					((RootPaneContainer)KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getRootPane().getTopLevelAncestor()).getGlassPane().setVisible(true);
				}

			}
		});
		//thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
	}
	
	/**
	 * Should implement export functionality of Kroki project to different versions of Eclipse UML diagram file.
	 * @param file  file where to save the Eclipse UML diagram.
	 * @param project  Kroki project to be exported.
	 */
	public abstract void executeExport(File file,BussinesSubsystem project);

}
