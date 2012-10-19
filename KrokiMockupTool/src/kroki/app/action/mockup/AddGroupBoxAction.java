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
import kroki.app.view.Canvas;
import kroki.mockup.model.Composite;
import kroki.mockup.model.border.TitledBorder;
import kroki.profil.ComponentType;
import kroki.profil.group.ElementsGroup;
import kroki.profil.group.GroupOrientation;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AddGroupBoxAction extends AbstractAction {

    ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.addGroupBox.smallImage"));
    Image addEnabledIcon = CursorResource.getCursorResource("action.addGroupBox.smallImage");
    Image addDisabledIcon = CursorResource.getCursorResource("action.denied.smallImage");

    public AddGroupBoxAction() {
        putValue(SMALL_ICON, smallIcon);
        putValue(NAME, StringResource.getStringResource("action.addGroupBox.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.addGroupBox.description"));
    }

    public void actionPerformed(ActionEvent e) {
        TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
        Canvas currentCanvas = tabbedPaneController.getCurrentTabContent();
        tabbedPaneController.changeCursorImage(addEnabledIcon);
        tabbedPaneController.getContext().goNext(State.ADD_STATE);

        ElementsGroup elementsGroup = new ElementsGroup("elementsGroup", enabled, ComponentType.PANEL);
        elementsGroup.setGroupOrientation(GroupOrientation.area);
        ((Composite) elementsGroup.getComponent()).setBorder(new TitledBorder());
        ((AddState) tabbedPaneController.getContext().getCurrentState()).setElement(elementsGroup);
        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddEnabledIcon(addEnabledIcon);
        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddDisabledIcon(addDisabledIcon);
    }
}
