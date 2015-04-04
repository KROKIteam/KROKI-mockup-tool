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
 * Action for transforming a field into an aggregated field
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class TransformToAggregatedAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.transformToAggregated.smallImage"));
	private Image addEnabledIcon = CursorResource.getCursorResource("action.transformToAggregated.smallImage");
	private Image addDisabledIcon = CursorResource.getCursorResource("action.denied.smallImage");

    public TransformToAggregatedAction() {
        putValue(SMALL_ICON, smallIcon);
        putValue(NAME, StringResource.getStringResource("action.transformToAggregated.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.transformToAggregated.description"));
    }

    public void actionPerformed(ActionEvent e) {
        TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
        tabbedPaneController.changeCursorImage(addEnabledIcon);
        tabbedPaneController.getContext().goNext(State.TRANSFORM_TO_AGGREGATED_STATE);
    }
}
