package com.panelcomposer.elements;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import util.staticnames.Settings;

import com.panelcomposer.elements.spanel.SPanel;

@SuppressWarnings("serial")
public class SToolBar extends JToolBar {

	/**
	 * Map of JButton objects that will appear on this SToolBar.
	 */
	protected Map<String, JButton> buttons;
	/**
	 * SPanel which contains this SToolBar
	 */
	protected SPanel panel;

	/***
	 * Constructor for SToolBar that calls initialization method: init() 
	 * @param horizontal 
	 * @param panel SPanel which contains this SToolBar
	 */
	public SToolBar(int horizontal, SPanel panel) {
		super(horizontal);
		this.panel = panel;
		init();
	}

	public void init() {
		setFloatable(false);
		setRollover(true);
		buttons = new HashMap<String, JButton>();
	}

	/***
	 * Creates JButton object from given parameters and adds it on the SToolBar
	 * and into the map of buttons
	 * @param iconFile
	 * @param mapKey 
	 * @param al ActionListener object if necessary 
	 * @return
	 */
	public JButton makeButton(String iconFile, String mapKey, ActionListener al) {
		JButton btn = new JButton(new ImageIcon(getClass().getResource(
				Settings.iconsDirectory + iconFile)));
		buttons.put(mapKey, btn);
		add(btn);
		btn.setFocusable(false);
		btn.addActionListener(al);
		return btn;
	}

	public SPanel getPanel() {
		return panel;
	}

	public void setPanel(SPanel panel) {
		this.panel = panel;
	}

	public Map<String, JButton> getButtons() {
		return buttons;
	}

	public void setButtons(Map<String, JButton> buttons) {
		this.buttons = buttons;
	}
}
