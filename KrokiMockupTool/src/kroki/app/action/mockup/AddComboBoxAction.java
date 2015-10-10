package kroki.app.action.mockup;

import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import kroki.app.KrokiMockupToolApp;
import kroki.app.controller.TabbedPaneController;
import kroki.app.state.AddState;
import kroki.app.state.State;
import kroki.app.utils.CursorResource;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.profil.ComponentType;
import kroki.profil.property.VisibleProperty;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AddComboBoxAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.addComboBox.smallImage"));
	private Image addEnabledIcon = CursorResource.getCursorResource("action.addComboBox.smallImage");
	private Image addDisabledIcon = CursorResource.getCursorResource("action.denied.smallImage");

    public AddComboBoxAction() {
        putValue(SMALL_ICON, smallIcon);
        putValue(NAME, StringResource.getStringResource("action.addComboBox.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.addComboBox.description"));
    }

    public void actionPerformed(ActionEvent e) {
        TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
        //Canvas currentCanvas = tabbedPaneController.getCurrentTabContent();
        tabbedPaneController.changeCursorImage(addEnabledIcon);
        tabbedPaneController.getContext().goNext(State.ADD_STATE);

        VisibleProperty visibleProperty = new VisibleProperty("combo_box_1", true, ComponentType.COMBO_BOX);
        ((AddState) tabbedPaneController.getContext().getCurrentState()).setElement(visibleProperty);
        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddEnabledIcon(addEnabledIcon);
        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddDisabledIcon(addDisabledIcon);
    }
}
