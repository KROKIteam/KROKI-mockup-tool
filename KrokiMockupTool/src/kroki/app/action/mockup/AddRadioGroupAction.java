package kroki.app.action.mockup;

import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import kroki.app.utils.CursorResource;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AddRadioGroupAction extends AbstractAction {

	private ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.addRadioGroup.smallImage"));
	private Image addEnabledIcon = CursorResource.getCursorResource("action.addRadioGroup.smallImage");
	private Image addDisabledIcon = CursorResource.getCursorResource("action.denied.smallImage");

    public AddRadioGroupAction() {
        putValue(SMALL_ICON, smallIcon);
        putValue(NAME, StringResource.getStringResource("action.addRadioGroup.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.addRadioGroup.description"));
        setEnabled(false);

    }

    public void actionPerformed(ActionEvent e) {
//        TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
//        Canvas currentCanvas = tabbedPaneController.getCurrentTabContent();
//        tabbedPaneController.changeCursorImage(addEnabledIcon);
//        tabbedPaneController.getContext().goNext(State.ADD_STATE);

//        ElementsGroup elementsGroup = new ElementsGroup("RadioGroup", enabled, ComponentType.RADIO_GROUP);
//        ((AddState) tabbedPaneController.getContext().getCurrentState()).setElement(elementsGroup);
//        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddEnabledIcon(addEnabledIcon);
//        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddDisabledIcon(addDisabledIcon);

    }
}
