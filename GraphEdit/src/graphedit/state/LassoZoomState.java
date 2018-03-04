package graphedit.state;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import graphedit.app.MainFrame;
import graphedit.view.GraphEditView;
import graphedit.view.GraphEditView.GraphEditController;

public class LassoZoomState extends State {

	public LassoZoomState(GraphEditView view, GraphEditController controller) {
		super(controller);
	}
	
	public LassoZoomState() { }

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			view.setLassoZoomRectangle((int)controller.getPoint().getX(), (int)controller.getPoint().getY(), e.getX(), e.getY());
			// azuriraj promene
			view.repaint();
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		MainFrame.getInstance().setPositionTrack(e.getX(), e.getY());
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			switchToDefaultState();
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			view.setLassoZoomRectangle((int)controller.getPoint().getX(), (int)controller.getPoint().getY(), e.getX(), e.getY());
			view.lassoZoom();
		}
		//switchToSelectionState();
		
		// azuriraj promene
		view.repaint();
	}
	
	@Override
	public boolean isAutoScrollOnDragEnabled() {
		return true;
	}

	@Override
	public boolean isAutoScrollOnMoveEnabled() {
		return false;
	}
}