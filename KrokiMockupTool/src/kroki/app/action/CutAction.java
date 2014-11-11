package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import kroki.app.KrokiMockupToolApp;
import kroki.app.command.CommandManager;
import kroki.app.command.RemoveCommand;
import kroki.app.controller.TabbedPaneController;
import kroki.app.model.SelectionModel;
import kroki.app.state.Context;
import kroki.app.state.State;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.app.view.Canvas;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;

public class CutAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CutAction() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.cut.smallIcon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(NAME, StringResource.getStringResource("action.cut.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.cut.description"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
	}
	@Override
	public void actionPerformed(ActionEvent e) {
    	TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
    	if (tabbedPaneController == null)
    		return;
    	
    	if(tabbedPaneController.getCanvasList().size() == 0)
    		return;
    	
    	Context ctx = tabbedPaneController.getContext();
    	if (ctx == null)
    		return;
    	
    	State currentState = ctx.getCurrentState();
    	if (currentState != null && currentState instanceof kroki.app.state.SelectState) {
            Canvas c = tabbedPaneController.getCurrentTabContent();
            CommandManager commandManager = c.getCommandManager();
            SelectionModel selectionModel = c.getSelectionModel();
            
            //pravim listu elemenata za izbaciti iz selekcije
            List<VisibleElement> cutted = new ArrayList<VisibleElement>();
            for (VisibleElement visibleElement : selectionModel.getVisibleElementList()) {
                if (!(visibleElement instanceof VisibleClass) && visibleElement.getParentGroup() != null) {
                	cutted.add(visibleElement);
                }
            }
            KrokiMockupToolApp.getInstance().getClipboardManager().cutSelectedElements();
            KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getPasteAction().setCutAction(true);
            //cutAction = true;

            if (cutted.size() > 0) {
            	RemoveCommand removeCommand = new RemoveCommand(cutted);
                commandManager.addCommand(removeCommand);
                selectionModel.removeFromSelection(cutted);
            } 

            c.repaint();	
    	}


	}

}
