/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.action.style;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import kroki.app.KrokiMockupToolApp;
import kroki.app.model.SelectionModel;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.app.view.Canvas;
import kroki.mockup.model.Composite;
import kroki.mockup.model.layout.LayoutManager;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.group.GroupAlignment;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.VisibleClass.ComponentAlign;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class AlignCenterAction extends AbstractAction {

    public AlignCenterAction() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.alignCenter.smallImage"));
        putValue(SMALL_ICON, smallIcon);
        //putValue(NAME, StringResource.getStringResource("action.alignCenter.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.alignCenter.description"));
    }

    public void actionPerformed(ActionEvent e) {
        Canvas c = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent();
        if (c == null) {
            return;
        }
        VisibleClass visibleClass = c.getVisibleClass();
        visibleClass.setAlign(ComponentAlign.ALIGN_CENTER);
        SelectionModel selectionModel = c.getSelectionModel();

        for (VisibleElement visibleElement : selectionModel.getVisibleElementList()) {
            if (visibleElement instanceof ElementsGroup) {
                ((ElementsGroup) visibleElement).setGroupAlignment(GroupAlignment.center);
                ((Composite) visibleElement.getComponent()).getLayoutManager().setAlign(LayoutManager.CENTER);
                visibleElement.update();
            }
        }
        visibleClass.update();
        c.repaint();
    }
}
