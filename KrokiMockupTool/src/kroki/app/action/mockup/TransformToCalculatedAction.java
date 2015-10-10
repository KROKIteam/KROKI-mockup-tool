package kroki.app.action.mockup;

import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import kroki.app.KrokiMockupToolApp;
import kroki.app.controller.TabbedPaneController;
import kroki.app.state.State;
import kroki.app.utils.CursorResource;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;

/**
 * Action for transforming a field into a calculated field
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class TransformToCalculatedAction extends AbstractAction {

	
	private static final long serialVersionUID = 1L;
	
	private ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.transformToCalculated.smallImage"));
	private Image addEnabledIcon = CursorResource.getCursorResource("action.transformToCalculated.smallImage");
	private Image addDisabledIcon = CursorResource.getCursorResource("action.denied.smallImage");

    public TransformToCalculatedAction() {
        putValue(SMALL_ICON, smallIcon);
        putValue(NAME, StringResource.getStringResource("action.transformToCalculated.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.transformToCalculated.description"));
    }

    public void actionPerformed(ActionEvent e) {
        TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
        tabbedPaneController.changeCursorImage(addEnabledIcon);
        tabbedPaneController.getContext().goNext(State.TRANSFORM_TO_CALCULATED_STATE);

//        VisibleOperation visibleOperation = new Transaction("transaction_1", true, ComponentType.BUTTON);
//        ((AddState) tabbedPaneController.getContext().getCurrentState()).setElement(visibleOperation);
//        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddEnabledIcon(addEnabledIcon);
//        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddDisabledIcon(addDisabledIcon);
    }
}
