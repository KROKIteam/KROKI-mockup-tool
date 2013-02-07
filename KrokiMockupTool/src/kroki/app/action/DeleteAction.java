/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.action;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;
import kroki.profil.utils.visitor.ContainingPanels;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class DeleteAction extends AbstractAction {

    VisibleElement visibleElement;

    public DeleteAction(VisibleElement visibleElement) {
        this.visibleElement = visibleElement;
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.delete.smallIcon"));
        ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.delete.largeIcon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(LARGE_ICON_KEY, largeIcon);
        putValue(NAME, StringResource.getStringResource("action.delete.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.delete.description"));
    }

    public void actionPerformed(ActionEvent e) {
        if (visibleElement instanceof UmlPackage) {
            UmlPackage subsystem = (UmlPackage) visibleElement;
            int result = JOptionPane.showConfirmDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), StringResource.getStringResource("action.delete.message"));
            if (result == JOptionPane.YES_OPTION) {

                ContainingPanels visitor = new ContainingPanels();
                visitor.visit(subsystem);
                List<Object> visibleClassList = visitor.getObjectList();
                for (int i = 0; i < visibleClassList.size(); i++) {
                    VisibleClass visibleClass = (VisibleClass) visibleClassList.get(i);
                    int index = KrokiMockupToolApp.getInstance().getTabbedPaneController().getTabIndex(visibleClass);
                    if (index != -1) {
                        KrokiMockupToolApp.getInstance().getTabbedPaneController().closeTab(index);
                    }
                }

                if (subsystem.nestingPackage() != null) {
                    subsystem.nestingPackage().removeNestedPackage(subsystem);
                } else {
                    //izbrisi ga iz workspace-a
                    KrokiMockupToolApp.getInstance().getWorkspace().removePackage(subsystem);
                }
                KrokiMockupToolApp.getInstance().getProjectHierarchyController().getTree().updateUI();
            } else {
            }
        } else if (visibleElement instanceof UmlType) {
            UmlType umlType = (UmlType) visibleElement;
            int result = JOptionPane.showConfirmDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), StringResource.getStringResource("action.delete.message"));
            if (result == JOptionPane.YES_OPTION) {

                int index = KrokiMockupToolApp.getInstance().getTabbedPaneController().getTabIndex((VisibleClass) umlType);
                if (index != -1) {
                    KrokiMockupToolApp.getInstance().getTabbedPaneController().closeTab(index);
                }
                umlType.umlPackage().removeOwnedType(umlType);
                KrokiMockupToolApp.getInstance().getProjectHierarchyController().getTree().updateUI();
            }
        }
    }
}
