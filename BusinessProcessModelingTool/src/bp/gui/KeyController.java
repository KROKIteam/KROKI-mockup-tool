package bp.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyController extends KeyAdapter {

	private BPPanel panel;
	
	public KeyController(BPPanel panel) {
		this.panel = panel;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		panel.getStateManager().getCurrentState().keyPressed(e);
	}

	public BPPanel getPanel() {
		return panel;
	}

	public void setPanel(BPPanel panel) {
		this.panel = panel;
	}
	
}
