package bp.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import bp.app.AppCore;

public class NewDiagramAction extends AbstractAction{

    /**
     * 
     */
    private static final long serialVersionUID = 8237800191665124730L;

    public NewDiagramAction(String name, String description, KeyStroke key) {
        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, description);
        putValue(Action.ACCELERATOR_KEY, key);
        
        // temporarily disabled
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        AppCore.getInstance().createBPPanel();
        
    }

    
}
