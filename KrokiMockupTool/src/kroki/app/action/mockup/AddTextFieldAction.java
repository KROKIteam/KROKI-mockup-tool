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
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.property.VisibleProperty;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AddTextFieldAction extends AbstractAction {

    ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.addTextField.smallImage"));
    Image addEnabledIcon = CursorResource.getCursorResource("action.addTextField.smallImage");
    Image addDisabledIcon = CursorResource.getCursorResource("action.denied.smallImage");

    public AddTextFieldAction() {
        putValue(SMALL_ICON, smallIcon);
        putValue(NAME, StringResource.getStringResource("action.addTextField.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.addTextField.description"));
    }

    public void actionPerformed(ActionEvent e) {
    	TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
        tabbedPaneController.changeCursorImage(addEnabledIcon);
        tabbedPaneController.getContext().goNext(State.ADD_STATE);

        VisibleProperty visibleProperty = new VisibleProperty("text_field_1", true, ComponentType.TEXT_FIELD);
        visibleProperty.setDataType("String");
        
        ((AddState) tabbedPaneController.getContext().getCurrentState()).setElement(visibleProperty);
        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddEnabledIcon(addEnabledIcon);
        ((AddState) tabbedPaneController.getContext().getCurrentState()).setAddDisabledIcon(addDisabledIcon);
    }
}
