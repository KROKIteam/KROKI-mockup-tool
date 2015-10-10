package graphedit.state;

import graphedit.app.MainFrame;

import java.awt.event.MouseEvent;

/**
 * State which is entered if an action is cancelled 
 * Waits for user to release mouse buttons
 * @author xxx
 *
 */
public class WaitToBeReleasedState extends State{
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
		switchToDefaultState();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// status bar
		MainFrame.getInstance().setPositionTrack(e.getX(), e.getY());
	}

	@Override
	public boolean isAutoScrollOnDragEnabled() {
		return false;
	}

	@Override
	public boolean isAutoScrollOnMoveEnabled() {
		return false;
	}

}
