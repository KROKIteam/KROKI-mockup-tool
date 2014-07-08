package bp.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import bp.app.AppCore;

public class ExitAction extends AbstractAction{

    /**
     * 
     */
    private static final long serialVersionUID = -3235833466723101089L;

    public ExitAction(String name, String description) {
        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, description);
    }

    public ExitAction() { }
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        AppCore appCore = AppCore.getInstance();
    	int test = JOptionPane.showConfirmDialog(appCore, "Do you want to save the changes?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION);
    	
    	if (test != JOptionPane.CANCEL_OPTION) {
    		
    		if (test == JOptionPane.YES_OPTION)
    			new SaveAction().actionPerformed(null);
    			
    		if (appCore.isRunningStandalone()) System.exit(0);
    		else appCore.setVisible(false);
    	}

    }

}
