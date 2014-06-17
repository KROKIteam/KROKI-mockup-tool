package graphedit.state;

import graphedit.app.MainFrame;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

public class LassoSelectionState extends State {

	/*public LassoSelectionState(GraphEditView view, GraphEditController controller) {
		super(controller);
		this.view = view;
		view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}*/
	
	public LassoSelectionState() {
		super();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			view.getSelectionModel().removeAllSelectedElements();
			
			switchToDefaultState();
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if (view == null || controller == null)
			return;
		view.getSelectionModel().setSelectedLink(null);
		if (SwingUtilities.isLeftMouseButton(e)) {
			view.setLassoRectangle((int)controller.getPoint().getX(), (int)controller.getPoint().getY(), e.getX(), e.getY());
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
			view.getSelectionModel().removeAllSelectedElements();
			
			switchToDefaultState();
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			// controller zna gde je korisnik kliknuo, MouseEvent zna gde je sada
			if (!e.isControlDown()) {
				view.getSelectionModel().removeAllSelectedElements();
			}
			view.addToLassoSelection((int)controller.getPoint().getX(), (int)controller.getPoint().getY(), e.getX(), e.getY());

			switchToDefaultState();
		} else {
			view.getSelectionModel().removeAllSelectedElements();

			switchToDefaultState();
		}
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