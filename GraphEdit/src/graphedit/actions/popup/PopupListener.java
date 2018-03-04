package graphedit.actions.popup;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.SwingUtilities;

import graphedit.app.MainFrame;
import graphedit.app.MainFrame.EventSource;

public class PopupListener extends MouseAdapter {
	
	private EventSource eventSource;
	
	public PopupListener(EventSource eventSource) {
		this.eventSource = eventSource;
	}

	public void mousePressed(MouseEvent e) {
		maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			if (eventSource == EventSource.MAIN_TOOLBAR) {
				MainFrame.getInstance().getMainToolBarPopupMenu().show(e.getComponent(), e.getX(), e.getY());
			} else if (eventSource == EventSource.TREE_VIEW) {
				JTree tree = MainFrame.getInstance().getMainTree();
				
				if (SwingUtilities.isRightMouseButton(e)) {  
				   int row = tree.getClosestRowForLocation(e.getX(), e.getY());  
				   tree.setSelectionRow(row);  
				} 
				
				if (tree.getSelectionCount() > 0) {
					Object node = tree.getSelectionPath().getLastPathComponent();
					MainFrame.getInstance().getMainPopupMenu(node).show(e.getComponent(), e.getX(), e.getY());
				}
				
			}/* else if (eventSource == EventSource.GRAPHICAL_VIEW) {
				MainFrame.getInstance().getViewPopupMenu().show(e.getComponent(), e.getX(), e.getY());
			}*/
		}
	}
}
