package kroki.app.utils;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.console.CommandPanel;
import kroki.app.gui.console.OutputPanel;
import kroki.profil.subsystem.BussinesSubsystem;

public class FileChooserHelper {

	private static String extension(File file)
	{
		int index=file.getName().lastIndexOf(".")+1;
		if(index<=0)
			return null;
		return file.getName().substring(index);
	}
	
	public static File fileChooser(JFrame frame,boolean saveDialog,String windowName,String filterName,String filterExtension)
	{
		JFileChooser fileChooser=new JFileChooser(System.getProperty("user.dir"));
		fileChooser.setAcceptAllFileFilterUsed(false);
		//fileChooser.setFileFilter(new CustomFileFilter(fileExtension));
		fileChooser.setFileFilter(new FileNameExtensionFilter(filterName,filterExtension));
		File file = null;
		if(fileChooser.showDialog(frame, windowName)==JFileChooser.APPROVE_OPTION)
		{
			
			String ext=extension(fileChooser.getSelectedFile());
			String message=null;
			if(saveDialog)
				message=windowName.toLowerCase()+" to";
			else
				message=windowName.toLowerCase()+" from";
			if(ext==null)
			{
				if(saveDialog)
				{
					String fileName=fileChooser.getSelectedFile().getAbsolutePath();
					file=new File(fileName+"."+filterExtension);
				}else
					JOptionPane.showMessageDialog(frame, "Choose file with extension\n"+filterExtension+"\n to "+message+"!", "Eror choosing file to "+message, JOptionPane.ERROR_MESSAGE);
			}
			else if(!filterExtension.equals(ext))
			{
				JOptionPane.showMessageDialog(frame, "Choose file with extension\n"+filterExtension+"\n to "+message+"!", "Eror choosing file to "+message, JOptionPane.ERROR_MESSAGE);
			}else
				file=fileChooser.getSelectedFile();
			
		}
		return file;
	}
	
	/**
	 * Checks if selected directory is exported Eclipse project directory
	 * It should contain 'ApplicationRepository' and 'WebApp' directories
	 * WebApp should have the '.project' file.
	 * If the specified directory does not comply to this, user can select another directory which is also checked. 
	 */
	public static boolean checkDirectory(BussinesSubsystem project) {
		File dir = project.getEclipseProjectPath();
		boolean ok = false;
		System.out.println("[ECLIPSE PROJECT EXPORT] Checking project dir: " + dir.getAbsolutePath());
		if(dir.exists()) {
			System.out.println("[ECLIPSE PROJECT EXPORT] Project directory OK.");
			File repoDir = new File(dir.getAbsolutePath() + File.separator + "ApplicationRepository");
			File appDir = new File(dir.getAbsolutePath() + File.separator + "WebApp");
			System.out.println("[ECLIPSE PROJECT EXPORT] Checking the ApplicationRepository folder at: " + repoDir.getAbsolutePath());
			if(repoDir.exists()) {
				System.out.println("[ECLIPSE PROJECT EXPORT] ApplicationRepository found.");
				System.out.println("[ECLIPSE PROJECT EXPORT] Checking the WebApp folder at: " + appDir.getAbsolutePath());
				if(appDir.exists()) {
					System.out.println("[ECLIPSE PROJECT EXPORT] WebApp found");
					File projFile = new File(appDir.getAbsolutePath() + File.separator + ".project");
					System.out.println("[ECLIPSE PROJECT EXPORT] Checking the .project file in: " + projFile.getAbsolutePath());
					if(projFile.exists()) {
						System.out.println("[ECLIPSE PROJECT EXPORT] .project file found!");
						KrokiMockupToolApp.getInstance().displayTextOutput("Export directory Ok. Exporting project...", OutputPanel.KROKI_RESPONSE);
						project.setEclipseProjectPath(dir);
						return true;
					}else {
						System.out.println("[ECLIPSE PROJECT EXPORT] WebApp does not contain Eclipse .project file!");
						ok = false;
					}
				}else {
					System.out.println("[ECLIPSE PROJECT EXPORT] Cannot fint the associated WebApp location!");
					ok =false;
				}
			}else {
				System.out.println("[ECLIPSE PROJECT EXPORT] Cannot find the associated ApplicationRepository location!");
				ok =false;
			}
		}
		KrokiMockupToolApp.getInstance().displayTextOutput("Existing export directory could not be found. Please select a new one.", OutputPanel.KROKI_WARNING);
		// If the function has not returned yet, choose another folder and check it
		System.out.println("[ECLIPSE PROJECT EXPORT] Directory check failed! Specifying a new one...");
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int retValue = jfc.showDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "Select project");
		if(retValue == JFileChooser.APPROVE_OPTION) {
			File newDir = jfc.getSelectedFile();
			project.setEclipseProjectPath(newDir);
			checkDirectory(project);
		}else {
			return false;
		}
		return ok;
	}
}
