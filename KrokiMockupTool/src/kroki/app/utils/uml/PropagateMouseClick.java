package kroki.app.utils.uml;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

/**
 * Used to enable propagation of the MouseEvent 
 * from the components on the lowest level of
 * hierarchy to the first component on the
 * hire level of hierarchy that has mouse listener
 * set on the MouseClicked event.
 * 
 * @author Zeljko Ivkovic (ivkovicszeljko@gmail.com)
 *
 */
public class PropagateMouseClick extends MouseAdapter {

	@Override
	public void mouseClicked(MouseEvent arg0) {
		dispatchClickToParent((JComponent)((JComponent)arg0.getSource()).getParent(),arg0);
	}
	
	/**
	 * For a received component checks if there are
	 * MouseListeners added. If there is a MouseListener
	 * calls the MouseClicked method. If there are no
	 * MouseListeners set calls this same method
	 * for a Parent container if any is set.
	 * @param comp component for which MouseClicked is 
	 * called or calls the same method for the parent
	 * container if there is any
	 * @param e  MouseEvent to be propagated to the MouseClicked
	 * method
	 */
	private void dispatchClickToParent(JComponent comp,MouseEvent e){
		if(comp.getMouseListeners().length>0)
		{
			for(MouseListener listener:comp.getMouseListeners())
				listener.mouseClicked(e);
			return;
		}
		if(comp.getParent()!=null)
			dispatchClickToParent((JComponent)comp.getParent(),e);
	}
}
