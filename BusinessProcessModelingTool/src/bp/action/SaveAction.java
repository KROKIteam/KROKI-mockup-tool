package bp.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import bp.app.AppCore;
import bp.model.data.Process;

public class SaveAction extends AbstractAction{

    /**
     * 
     */
    private static final long serialVersionUID = -3235833466723101089L;

    public SaveAction() { }
    
    public SaveAction(String name, String description, KeyStroke key) {
        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, description);
        putValue(Action.ACCELERATOR_KEY, key);
        
        // temporarily disabled
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
    	AppCore appCore = AppCore.getInstance();
    	Process process = appCore.getBpPanel().getProcess();
    	
    	/*
    	if (WorkspaceUtility.saveProcess(process)) {
    		JOptionPane.showMessageDialog(AppCore.getInstance(), "Business process " + process.getUniqueName() +  " is saved successfully!", "Business Process Modeling Tool", JOptionPane.INFORMATION_MESSAGE);
    		System.out.println("Business process saved successfully...");
    	} else {
    		JOptionPane.showMessageDialog(AppCore.getInstance(), "Business process " + process.getUniqueName() +  " wasn't saved or is saved with errors!", "Business Process Modeling Tool", JOptionPane.ERROR_MESSAGE);
    	}
    	*/
    	appCore.setSaveActionInvoked(true);
    	JOptionPane.showMessageDialog(AppCore.getInstance(), "Business process " + process.getUniqueName() +  " is saved successfully!", "Business Process Modeling Tool", JOptionPane.INFORMATION_MESSAGE);
    }
    
}
