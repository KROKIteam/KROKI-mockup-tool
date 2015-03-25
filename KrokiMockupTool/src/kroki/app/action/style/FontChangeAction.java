package kroki.app.action.style;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;

import kroki.app.KrokiMockupToolApp;
import kroki.app.model.SelectionModel;
import kroki.app.view.Canvas;
import kroki.profil.VisibleElement;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class FontChangeAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
    public void actionPerformed(ActionEvent e) {
        Canvas c = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent();
        if (c == null) {
            return;
        }
        SelectionModel selectionModel = c.getSelectionModel();
        String fontFamily = (String) ((JComboBox<?>) e.getSource()).getSelectedItem();
        for (VisibleElement visibleElement : selectionModel.getVisibleElementList()) {
            Font oldFont = visibleElement.getComponent().getFont();
            visibleElement.getComponent().setFont(new Font(fontFamily, oldFont.getStyle(), oldFont.getSize()));
            //visibleElement.getComponent().packComponent();
            visibleElement.update();
        }
        c.repaint();
    }
}
