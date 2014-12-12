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
import kroki.profil.utils.ElementsGroupUtil;
import kroki.profil.utils.UIPropertyUtil;
import kroki.profil.utils.VisibleClassUtil;

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

        List<VisibleProperty> visiblePropertyList = VisibleClassUtil.containedProperties(visibleClass);
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

        List<VisibleProperty> visiblePropertyList = VisibleClassUtil.containedProperties(visibleClass);
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
            ElementsGroup elg = VisibleClassUtil.getElementsGroupAtPoint(visibleClass, e.getPoint());
            if (elg != null) {
                int position = ElementsGroupUtil.indexOf(elg, visibleProperty);
                ElementsGroupUtil.removeVisibleElement(elg, visibleProperty);
                UIPropertyUtil.removeVisibleElement(visibleClass,visibleProperty);

                Zoom zoom = new Zoom(visibleProperty);
                zoom.setActivationPanel(visibleClass);
                ElementsGroupUtil.addVisibleElement(elg, position, zoom);
                UIPropertyUtil.addVisibleElement(visibleClass, zoom);

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
