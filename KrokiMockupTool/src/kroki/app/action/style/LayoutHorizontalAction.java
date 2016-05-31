package kroki.app.action.style;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import kroki.app.KrokiMockupToolApp;
import kroki.app.command.ChangeLayoutCommand;
import kroki.app.command.CommandManager;
import kroki.app.gui.toolbar.StyleToolbar;
import kroki.app.model.SelectionModel;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.app.view.Canvas;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.group.GroupOrientation;
import kroki.profil.panel.VisibleClass;
import kroki.profil.utils.ComponentOrientation;

/**
 *
 * @author Kroki team
 */
public class LayoutHorizontalAction extends AbstractAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LayoutHorizontalAction() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.horizontalLayout.smallImage"));
        putValue(SMALL_ICON, smallIcon);
        //putValue(NAME, StringResource.getStringResource("action.horizontalLayout.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.horizontalLayout.description"));
    }

    public void actionPerformed(ActionEvent e) {
        Canvas c = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent();
        if (c == null) {
            return;
        }
        CommandManager commandManager = c.getCommandManager();

        VisibleClass visibleClass = c.getVisibleClass();
        visibleClass.setOrientation(ComponentOrientation.ORIENTATION_HORIZONTAL);
        SelectionModel selectionModel = c.getSelectionModel();

        //only one must be selected
        if (selectionModel.getSelectionNum() != 1) {
            return;
        }
        //that selected element must be an instance of ElementsGroup
        if (!(selectionModel.getVisibleElementAt(0) instanceof ElementsGroup)) {
            return;
        }
        VisibleElement visibleElement = selectionModel.getVisibleElementAt(0);
        ChangeLayoutCommand changeLayoutCommand = new ChangeLayoutCommand(GroupOrientation.horizontal, (ElementsGroup) visibleElement);
        commandManager.addCommand(changeLayoutCommand);

        visibleClass.update();
        c.repaint();

        StyleToolbar st = (StyleToolbar) KrokiMockupToolApp.getInstance().getGuiManager().getStyleToolbar();
        st.enableAlignToggles();
    }
}
