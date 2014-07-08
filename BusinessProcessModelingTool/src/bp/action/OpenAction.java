package bp.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import bp.app.AppCore;
import bp.model.data.Process;
import bp.util.WorkspaceUtility;

public class OpenAction extends AbstractAction{

    /**
     * 
     */
    private static final long serialVersionUID = -3235833466723101089L;

    private JFileChooser fileChooser = new JFileChooser();

    public OpenAction() { }
    
    public OpenAction(String name, String description, KeyStroke key) {
        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, description);
        putValue(Action.ACCELERATOR_KEY, key);
        
        // temporarily disabled
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
    	
    	AppCore appCore = AppCore.getInstance();
    	Process process;
    	File file;
    	
    	initializeFileChooser(appCore.getProjectFile());
    	
    	int checker = fileChooser.showOpenDialog(appCore);
    	
    	if (checker == JFileChooser.APPROVE_OPTION) {
    		file = fileChooser.getSelectedFile();
    		System.out.println("Trying to open a file: " + file.getAbsolutePath());
    		
    		process = WorkspaceUtility.load(file);
    		
    		if (process instanceof Process) {
    			
    			appCore.loadProcess(process);
    			
    			JOptionPane.showMessageDialog(AppCore.getInstance(), "Business process " + process.getUniqueName() +  " is loaded successfully!", "Business Process Modeling Tool", JOptionPane.INFORMATION_MESSAGE);
        		System.out.println("Business process loaded successfully...");
        	} else {
        		JOptionPane.showMessageDialog(AppCore.getInstance(), "Business process " + process.getUniqueName() +  " wasn't loaded successfully!", "Business Process Modeling Tool", JOptionPane.ERROR_MESSAGE);
        	}
    		
    		
    		
    	}
    }
    
    private void initializeFileChooser(File projectDirectory) {
    	
    	fileChooser.setCurrentDirectory(projectDirectory);
    	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	fileChooser.setFileFilter(new FileNameExtensionFilter("Kroki business process file", WorkspaceUtility.BUSINESS_PROCESS_EXTENSION.substring(1)));
    	fileChooser.setDialogTitle(NAME);
    }
    
}
