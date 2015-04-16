package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import kroki.app.KrokiMockupToolApp;
import kroki.app.command.Command;
import kroki.app.command.CommandManager;
import kroki.app.command.PasteCommand;
import kroki.app.controller.TabbedPaneController;
import kroki.app.model.ClipboardContents;
import kroki.app.model.SelectionModel;
import kroki.app.state.Context;
import kroki.app.state.State;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.app.view.Canvas;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;
import kroki.profil.utils.ElementsGroupUtil;

/**
 * Pastes elements
 * @author Kroki Team
 */
public class PasteAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean cutAction = false;
	
	public PasteAction() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.paste.smallIcon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(NAME, StringResource.getStringResource("action.paste.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.paste.description"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));;
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
            
    		@SuppressWarnings("unchecked")
    		List<VisibleElement> clipboardElements = (List<VisibleElement>) KrokiMockupToolApp.getInstance().getClipboardManager().getElement(ClipboardContents.clipboardVisibleElementsFlavor);
    		if(clipboardElements == null || clipboardElements.isEmpty())
    			return;
    		
    		//list containing elements to be removed from the selection
            List<VisibleElement> selected = new ArrayList<VisibleElement>();
            for (VisibleElement visibleElement : selectionModel.getVisibleElementList()) {
                if (!(visibleElement instanceof VisibleClass)) {
                	selected.add(visibleElement);
                }
            }
            
            //if only one element is selection and it is an element group
    		if (selected.size() == 1 && selected.get(0) instanceof ElementsGroup) {
    			ElementsGroup temp = (ElementsGroup) selected.get(0);
    			
    			for (VisibleElement el : clipboardElements) {
    				if (!ElementsGroupUtil.checkIfCanAdd(temp, el)) {
    					return;
    				}
    			}
    			
    			Command command = new PasteCommand(c.getVisibleClass(), temp, clipboardElements, null, cutAction);
    			commandManager.addCommand(command);
    			selectionModel.clearSelection();			
    			if(cutAction)
    				KrokiMockupToolApp.getInstance().getClipboardManager().clearClipboard();
    		}
            c.repaint();	
    	}
	}

	public boolean isCutAction() {
		return cutAction;
	}

	public void setCutAction(boolean cutAction) {
		this.cutAction = cutAction;
	}

}
