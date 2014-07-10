package bp.state;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import bp.app.AppCore;
import bp.gui.BPPanel;
import bp.model.data.Edge;
import bp.model.data.Element;
import bp.model.data.Lane;
import bp.model.data.Process;
import bp.model.data.Vertex;
import bp.model.graphic.util.SelectionManager;

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
        System.out.println("requesting focus for canvas...");
        getGraphicPanel().requestFocus();
    }

    @Override
    public void exitingState() {
        System.out.println("exiting select state");
    }

    @Override
	public void keyPressed(KeyEvent e) {
    	System.out.println("SelectState: Key [" + e.getKeyCode() + "] is pressed.");
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			
			AppCore appCore = AppCore.getInstance();
			SelectionManager selectionManager = getGraphicPanel().getSelectionManager();
			Process process = getPanel().getProcess();
			
			List<Element> egdes = new ArrayList<>();
			Edge edge;
			
			/* Do the logic */
			System.out.println("Elements to delete:");
			for (Element el : selectionManager.getSelectedElements()) {
				System.out.println("> " + el.getUniqueName());

				/* Remove all of its links */
				for (Element el2 : process.getElements()) {
					if (el2 instanceof Edge) {
						edge = (Edge) el2;
						System.out.println("Edge: " + edge.getUniqueName());
						if (edge.getSource().getUniqueName().equals(el.getUniqueName()) ||
								edge.getTarget().getUniqueName().equals(el.getUniqueName())) {
							System.out.println("Edge added: " + el2.getUniqueName());
							egdes.add(el2);
						}
							
					}
				}
				
				/* Remove the associated links */
				for (Element l : egdes) process.removeElement(l);
				
				/* Remove the element itself */
				process.removeElement(el);
					
			}
			
			selectionManager.clearSelection();
			appCore.updateDetails(getPanel().getProcess().getDetails());
			getGraphicPanel().repaint();
		}
	}
    
}
