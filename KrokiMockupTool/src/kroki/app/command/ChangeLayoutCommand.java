package kroki.app.command;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.toolbar.StyleToolbar;
import kroki.mockup.model.Composite;
import kroki.mockup.model.layout.FlowLayoutManager;
import kroki.mockup.model.layout.FreeLayoutManager;
import kroki.mockup.model.layout.LayoutManager;
import kroki.mockup.model.layout.VerticalLayoutManager;
import kroki.profil.group.ElementsGroup;
import kroki.profil.group.GroupOrientation;

/**
 * Command for chaning layout of a group
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ChangeLayoutCommand implements Command {

    private GroupOrientation oldOrientation;
    private GroupOrientation newOrientation;
    private ElementsGroup elementsGroup;

    public ChangeLayoutCommand(GroupOrientation newOrientation, ElementsGroup elementsGroup) {
        this.newOrientation = newOrientation;
        this.oldOrientation = elementsGroup.getGroupOrientation();
        this.elementsGroup = elementsGroup;
    }

    public void doCommand() {
        elementsGroup.setGroupOrientation(newOrientation);
        LayoutManager layoutManager;
        switch (newOrientation) {
            case area: {
                layoutManager = new FreeLayoutManager();
            }
            break;
            case horizontal: {
                layoutManager = new FlowLayoutManager();
            }
            break;
            case vertical: {
                layoutManager = new VerticalLayoutManager();
            }
            break;
            default: {
                layoutManager = new FreeLayoutManager();
            }
            break;
        }

        ((Composite) elementsGroup.getComponent()).setLayoutManager(layoutManager);
        ((Composite) elementsGroup.getComponent()).layout();
        elementsGroup.update();
        StyleToolbar st = (StyleToolbar) KrokiMockupToolApp.getInstance().getGuiManager().getStyleToolbar();
        st.updateAllToggles(elementsGroup);
    }

    public void undoCommand() {
        if (oldOrientation == null) {
            oldOrientation = GroupOrientation.area;
        }
        elementsGroup.setGroupOrientation(oldOrientation);
        LayoutManager layoutManager;
        switch (oldOrientation) {
            case area: {
                layoutManager = new FreeLayoutManager();
            }
            break;
            case horizontal: {
                layoutManager = new FlowLayoutManager();
            }
            break;
            case vertical: {
                layoutManager = new VerticalLayoutManager();
            }
            break;
            default: {
                layoutManager = new FreeLayoutManager();
            }
            break;
        }
        ((Composite) elementsGroup.getComponent()).setLayoutManager(layoutManager);
        ((Composite) elementsGroup.getComponent()).layout();
        elementsGroup.update();
        StyleToolbar st = (StyleToolbar) KrokiMockupToolApp.getInstance().getGuiManager().getStyleToolbar();
        st.updateAllToggles(elementsGroup);
    }
}
