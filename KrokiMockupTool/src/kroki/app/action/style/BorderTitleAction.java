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
import kroki.mockup.model.border.TitledBorder;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class BorderTitleAction extends AbstractAction {

    public BorderTitleAction() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.titleBorder.smallImage"));
        putValue(SMALL_ICON, smallIcon);
        //putValue(NAME, StringResource.getStringResource("action.titleBorder.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.titleBorder.description"));

    }

    public void actionPerformed(ActionEvent e) {
        Canvas c = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent();
        if (c == null) {
            return;
        }
        VisibleClass visibleClass = c.getVisibleClass();
        SelectionModel selectionModel = c.getSelectionModel();

        for (VisibleElement visibleElement : selectionModel.getVisibleElementList()) {
            if (visibleElement instanceof ElementsGroup) {
                ((Composite) visibleElement.getComponent()).setBorder(new TitledBorder());
                visibleElement.update();
            }
        }
        visibleClass.update();
        c.repaint();
    }
}
