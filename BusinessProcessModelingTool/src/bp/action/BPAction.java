package bp.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import bp.app.AppCore;
import bp.gui.BPPanel;
import bp.state.StateType;

public class BPAction extends AbstractAction {

    /**
     * 
     */
    private static final long serialVersionUID = -5984597151859754940L;

    private final StateType state;

    // private final Image enabledIcon = CursorResource.getCursorResource("action.addCheckBoxes.smallImage");
    // private final Image disabledIcon = CursorResource.getCursorResource("action.denied.smallImage");

    public BPAction(final String name, final String description, final StateType state) {
        if (name != null)
            putValue(NAME, name);

        if (description != null)
            putValue(SHORT_DESCRIPTION, description);

        this.state = state;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final BPPanel panel = AppCore.getInstance().getBpPanel();
        if (panel != null)
            panel.getStateManager().moveToState(this.state);
    }

    // public Image getEnabledIcon() {
    // return enabledIcon;
    // }
    //
    // public Image getDisabledIcon() {
    // return disabledIcon;
    // }
}
