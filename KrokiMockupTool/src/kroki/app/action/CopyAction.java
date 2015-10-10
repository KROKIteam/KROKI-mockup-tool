package kroki.app.action;

import java.awt.event.ActionEvent;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import kroki.app.KrokiMockupToolApp;
import kroki.app.controller.TabbedPaneController;
import kroki.app.model.SelectionModel;
import kroki.app.state.Context;
import kroki.app.state.State;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.app.view.Canvas;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;

/**
 * Copies elements
 * @author Kroki Team
 */
public class CopyAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public CopyAction() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.copy.smallIcon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(NAME, StringResource.getStringResource("action.copy.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.copy.description"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
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
            SelectionModel selectionModel = c.getSelectionModel();
            
            //list of elements to be removed from the selection
            List<VisibleElement> cutted = new ArrayList<VisibleElement>();
            for (VisibleElement visibleElement : selectionModel.getVisibleElementList()) {
                if (!(visibleElement instanceof VisibleClass) && visibleElement.getParentGroup() != null) {
                	cutted.add(visibleElement);
                }
            }
            KrokiMockupToolApp.getInstance().getClipboardManager().copySelectedElements();
            KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getPasteAction().setCutAction(false);
            c.repaint();
    	}
		
	}

}
