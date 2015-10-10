package graphedit.actions.view;

import graphedit.app.MainFrame;
import graphedit.util.ResourceLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class FullScreenAction extends AbstractAction {
	
	public static final String FULL_SCREEN = "Full screen...", STANDARD_SCREEN = "Go back...";

	private ImageIcon fullScreenIcon;

	private ImageIcon standardScreenIcon;

	private MainFrame mainFrame;
	
	private BasicSplitPaneUI ui;
	
	private JButton oneClick;
	
	private static final long serialVersionUID = 1L;

	public FullScreenAction() {
		ResourceLoader resLoader = new ResourceLoader();
		standardScreenIcon = resLoader.loadImageIcon("standard_screen.png");
		fullScreenIcon = resLoader.loadImageIcon("full_screen.png");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F11, ActionEvent.CTRL_MASK));
		setFullScreenIcon();
		setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {	
		mainFrame = MainFrame.getInstance();
		if (getValue(SHORT_DESCRIPTION).equals(FULL_SCREEN)) {
			mainFrame.getStandardToolBar().setSelected(false);
			mainFrame.getMainToolBar().setVisible(false);
			
			ui = (BasicSplitPaneUI) mainFrame.getMainSplitPane().getUI();
			oneClick = (JButton) ui.getDivider().getComponent(0);
			oneClick.doClick();
			
			setStandardScreenIcon();
		} else {
			mainFrame.getStandardToolBar().setSelected(true);
			mainFrame.getMainToolBar().setVisible(true);
			
			ui = (BasicSplitPaneUI) mainFrame.getMainSplitPane().getUI();
			oneClick = (JButton) ui.getDivider().getComponent(1);
			oneClick.doClick();
			
			setFullScreenIcon();
		}
		
	}
	
	public void setFullScreenIcon() {
		putValue(NAME, "Full screen");
		putValue(SMALL_ICON, fullScreenIcon);
		putValue(SHORT_DESCRIPTION, FULL_SCREEN);
	}

	public void setStandardScreenIcon() {
		putValue(NAME, "Standard screen");
		putValue(SMALL_ICON, standardScreenIcon);
		putValue(SHORT_DESCRIPTION, STANDARD_SCREEN);
	}

}

