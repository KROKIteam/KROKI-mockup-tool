package com.panelcomposer.elements;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import util.staticnames.Settings;
import util.xml_readers.MenuReader;

import com.panelcomposer.core.AppCache;
import com.panelcomposer.core.UserLoginDialog;
import com.panelcomposer.enumerations.PanelType;
import com.panelcomposer.listeners.MySubMenuActionListener;
import com.panelcomposer.model.menu.MyMenu;
import com.panelcomposer.model.menu.MySubMenu;


@SuppressWarnings("serial")
public class SMainForm extends JFrame {

	private String act;
	private PanelType panelType;

	public SMainForm() {
		setTitle(Settings.MAIN_FORM);
		setSize(new Dimension(800, 600));
		setExtendedState(JFrame.NORMAL);
		setName("MainForm");
		UserLoginDialog userLogin = new UserLoginDialog(this);
		userLogin.setVisible(true);
		
		MenuReader.load();
		createMenu();
		createClose();
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		AppCache appCache = AppCache.getInstance();
		List<MyMenu> menus = appCache.getMenus();
		for (int i = 0; i < menus.size(); i++) {
			JMenu jm = new JMenu(menus.get(i).getLabel());
			MyMenu mm = null;
			mm = menus.get(i);
			for (int j = 0; j < mm.getSubmenus().size(); j++) {
				JMenuItem jmi = new JMenuItem(mm.getSubmenus().get(j).getLabel());
				MySubMenu msm = null;
				msm = mm.getSubmenus().get(j);
				act = msm.getActivate();
				panelType = msm.getPanelType();
				jmi.addActionListener(new MySubMenuActionListener(act, panelType, SMainForm.this));
				jm.add(jmi);
			}
			menuBar.add(jm);
		}
	}

	private void createClose() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
}
