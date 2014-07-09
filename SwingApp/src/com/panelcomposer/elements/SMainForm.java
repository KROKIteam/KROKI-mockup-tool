package com.panelcomposer.elements;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.h2.constant.SysProperties;

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
			MyMenu myMenu = menus.get(i);
			JMenu jMenu = createJMenuFromMyMenu(myMenu);
			menuBar.add(jMenu);
		}
	}

	public JMenu createJMenuFromMyMenu(MyMenu myMenu) {
		JMenu jMenu = new JMenu(myMenu.getLabel());
		for (MySubMenu submenu : myMenu.getSubmenus()) {
			JMenuItem item = createJMenuItemFromMySubMenu(submenu);
			jMenu.add(item);
		}
		
		for (MyMenu menu : myMenu.getMenus()) {
			JMenu mmenu = createJMenuFromMyMenu(menu);
			jMenu.add(mmenu);
		}
		return jMenu;
	}
	
	public JMenuItem createJMenuItemFromMySubMenu(MySubMenu submenu) {
		JMenuItem item = new JMenuItem(submenu.getLabel());
		item.addActionListener(new MySubMenuActionListener(submenu.getActivate(), submenu.getPanelType(), SMainForm.this));
		return item;
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
