/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import kroki.profil.operation.Report;
import kroki.profil.operation.VisibleOperation;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AddButtonAction extends AbstractAction {

    ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.addButton.smallImage"));
    Image addEnabledIcon = CursorResource.getCursorResource("action.addButton.smallImage");
    Image addDisabledIcon = CursorResource.getCursorResource("action.denied.smallImage");

    public AddButtonAction() {
        putValue(SMALL_ICON, smallIcon);
        putValue(NAME, StringResource.getStringResource("action.addButton.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.addButton.description"));
    }

    public void actionPerformed(ActionEvent e) {
        TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
        tabbedPaneController.changeCursorImage(addEnabledIcon);
        tabbedPaneController.getContext().goNext(State.ADD_STATE);

        VisibleOperation visibleOperation = new Report("button_1", true, ComponentType.BUTTON);
        ((AddState) tabbedPaneController.getContext().getCurrentState()).setElement(visibleOperation);
        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddEnabledIcon(addEnabledIcon);
        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddDisabledIcon(addDisabledIcon);
    }
}
