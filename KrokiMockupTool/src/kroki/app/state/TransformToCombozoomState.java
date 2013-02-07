/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.state;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.List;

import kroki.app.KrokiMockupToolApp;
import kroki.app.controller.TabbedPaneController;
import kroki.app.utils.CursorResource;
import kroki.app.view.Canvas;
import kroki.profil.ComponentType;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.VisibleProperty;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class TransformToCombozoomState extends State {

    Image addEnabledIcon = CursorResource.getCursorResource("action.transformToCombozoom.smallImage");
    Image addDisabledIcon = CursorResource.getCursorResource("action.denied.smallImage");

    public TransformToCombozoomState(Context context) {
        super(context, "app.state.combozoom");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
        Canvas c = tabbedPaneController.getCurrentTabContent();
        VisibleClass visibleClass = c.getVisibleClass();

        List<VisibleProperty> visiblePropertyList = visibleClass.containedProperties();
        boolean flag = false;
        for (int i = 0; i < visiblePropertyList.size(); i++) {
            VisibleProperty visibleProperty = visiblePropertyList.get(i);
            if (visibleProperty.getComponent().contains(e.getPoint())) {
                if (visibleProperty.getComponentType() == ComponentType.COMBO_BOX) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            tabbedPaneController.changeCursorImage(addEnabledIcon);
        } else {
            tabbedPaneController.changeCursorImage(addDisabledIcon);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
        TabbedPaneController tabbedPaneController = KrokiMockupToolApp.getInstance().getTabbedPaneController();
        Canvas c = tabbedPaneController.getCurrentTabContent();
        VisibleClass visibleClass = c.getVisibleClass();

        if (e.getButton() == MouseEvent.BUTTON3) {
            tabbedPaneController.changeCursorImage(null);
            context.goNext(SELECT_STATE);
            return;
        }

        List<VisibleProperty> visiblePropertyList = visibleClass.containedProperties();
        boolean flag = false;
        VisibleProperty visibleProperty = null;
        for (int i = 0; i < visiblePropertyList.size(); i++) {
            visibleProperty = visiblePropertyList.get(i);
            if (visibleProperty.getComponent().contains(e.getPoint())) {
                if (visibleProperty.getComponentType() == ComponentType.COMBO_BOX) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            ElementsGroup elg = visibleClass.getElementsGroupAtPoint(e.getPoint());
            if (elg != null) {
                int position = elg.indexOf(visibleProperty);
                elg.removeVisibleElement(visibleProperty);
                visibleClass.removeVisibleElement(visibleProperty);

                Zoom zoom = new Zoom(visibleProperty);
                zoom.setActivationPanel(visibleClass);
                elg.addVisibleElement(position, zoom);
                visibleClass.addVisibleElement(zoom);

                elg.update();
                visibleClass.update();
                tabbedPaneController.changeCursorImage(null);

                c.getSelectionModel().clearSelection();
                c.getSelectionModel().addToSelection(zoom);
                c.repaint();
                context.goNext(SELECT_STATE);
            }

        } else {
        }





    }
}
