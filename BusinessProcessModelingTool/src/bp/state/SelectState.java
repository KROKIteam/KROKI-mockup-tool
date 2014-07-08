package bp.state;

import java.awt.event.MouseEvent;

import bp.app.AppCore;
import bp.gui.BPPanel;
import bp.model.data.Edge;
import bp.model.data.Element;
import bp.model.data.Lane;
import bp.model.data.Vertex;

public class SelectState extends BPState {

    public SelectState(final BPPanel panel) {
        super(panel);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        // if handler is clicked don't do anything
        for (final Element el : getGraphicPanel().getSelectionManager().getSelectedElements()) {
            if (el.getComponent().getHandlers().isHandlerAt(e.getPoint())) {
                System.out.println("Handler hit");
                return;
            }
        }

        final Element el = getGraphicPanel().getElementAt(e.getPoint());

        if (el != null) {
            System.out.println("Element hit");
            if (!getGraphicPanel().getSelectionManager().isElementSelected(el)) {
                getGraphicPanel().getSelectionManager().clearSelection();
                getGraphicPanel().getSelectionManager().addToSelection(el);
                AppCore.getInstance().updateDetails(el.getDetails());
            }
        }

        if (el == null) {
            getGraphicPanel().getSelectionManager().clearSelection();
            AppCore.getInstance().updateDetails(getPanel().getProcess().getDetails());
        }
        getGraphicPanel().repaint();
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        // check handlers first
        for (final Element el : getGraphicPanel().getSelectionManager().getSelectedElements()) {
            if (el.getComponent().getHandlers().isHandlerAt(e.getPoint())) {
                if (el instanceof Edge) {
                    getPanel().getStateManager().moveToState(StateType.MOVE_EDGE);
                    return;
                } else if (el instanceof Vertex || el instanceof Lane) {
                    getPanel().getStateManager().moveToState(StateType.RESIZE);
                    return;
                }
            }
        }

        // check elements
        final Element el = getGraphicPanel().getElementAt(e.getPoint());
        if (el != null && (el instanceof Vertex || el instanceof Lane)) {
            getPanel().getStateManager().moveToState(StateType.MOVE);
            return;
        }
    }

    @Override
    public void enteringState() {
        System.out.println("entering select state");

    }

    @Override
    public void exitingState() {
        System.out.println("exiting select state");
    }
}
