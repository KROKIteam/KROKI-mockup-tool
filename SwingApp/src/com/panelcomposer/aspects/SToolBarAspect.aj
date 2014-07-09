package com.panelcomposer.aspects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import util.staticnames.Buttons;
import util.staticnames.Settings;

import com.panelcomposer.elements.SToolBar;
import com.panelcomposer.elements.spanel.SPanel;
import com.panelcomposer.enumerations.OpenedAs;
import com.panelcomposer.enumerations.StateMode;
import com.panelcomposer.enumerations.ViewMode;
import com.panelcomposer.listeners.NextActionListener;
import com.panelcomposer.model.panel.configuration.Next;

public aspect SToolBarAspect {

	/**
	 * Pointcut for method init()
	 * @param toolbar
	 */
	public pointcut createButtons(SToolBar toolbar) : call (* SToolBar.init()) && target(toolbar);

	after(final SToolBar toolbar) : createButtons(toolbar) {
		// search button
		toolbar.makeButton("search.gif", Buttons.SEARCH, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				toolbar.getPanel().handleSearch();
			}
		});

		// zoom pick-up button
		if (toolbar.getPanel().getModelPanel().getPanelSettings().getOpenedAs() == OpenedAs.ZOOM) {
			toolbar.makeButton("zoompick.gif", Buttons.ZOOM_PICK, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					SPanel sp = toolbar.getPanel();
					int crow = sp.getTable().getTableModel().getCurrentRow();
					Object obj = sp.getTable().getTableModel().get(crow);
					sp.getModelPanel().getDataSettings().setZoomed(obj);
					sp.dispose();
				}
			});
		}

		// change view mode button
		if (toolbar.getPanel().getModelPanel().getPanelSettings().getChangeMode() == true) {
			String gif = null;
			if (toolbar.getPanel().getModelPanel().getPanelSettings()
					.getViewMode().equals(ViewMode.TABLEVIEW)) {
				gif = "viewmode2.gif";
			} else {
				gif = "viewmode1.gif";
			}
			toolbar.makeButton(gif, Buttons.VIEW_MODE, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String img = "";
					SPanel panel = toolbar.getPanel();
					ViewMode grid = panel.getModelPanel().getPanelSettings().getViewMode();
					if(grid.equals(ViewMode.TABLEVIEW)) {
						panel.getCardLayout().show(panel.getCardPanel(), Settings.INPUT);
						img = "viewmode1.gif";
						panel.getModelPanel().getPanelSettings().setViewMode(ViewMode.INPUTPANELVIEW);
					} else {
						panel.getCardLayout().show(panel.getCardPanel(), Settings.TABLE);
						img = "viewmode2.gif";
						panel.getModelPanel().getPanelSettings().setViewMode(ViewMode.TABLEVIEW);
					}
					toolbar.getButtons().get(Buttons.VIEW_MODE).setIcon(
							new ImageIcon(getClass().getResource(Settings.iconsDirectory + img)));
					panel.getModelPanel().getPanelSettings().setStateMode(StateMode.UPDATE);
				}
			});	
		}
		toolbar.makeButton("refresh.gif", Buttons.REFRESH, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				String dataFilter = toolbar.getPanel()
						.getModelPanel().getDataSettings().getDataFilter();
				toolbar.getPanel().loadData(dataFilter);
			}
		});
		toolbar.makeButton("help.gif",Buttons.HELP, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				JOptionPane.showMessageDialog(null, "No Help!");
			}
		});
		toolbar.addSeparator();

		// Data navigation part - first, previous, next and last
		if (toolbar.getPanel().getModelPanel().getPanelSettings().getDataNavigation() == true) {
			toolbar.makeButton("first.gif", Buttons.FIRST, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					toolbar.getPanel().getTable().goToFirst();
				}
			});
			toolbar.makeButton("prev.gif", Buttons.PREVIOUS, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					toolbar.getPanel().getTable().goToPrev();
				}
			});
			toolbar.makeButton("next.gif", Buttons.NEXT, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					toolbar.getPanel().getTable().goToNext();
				}
			});
			toolbar.makeButton("last.gif", Buttons.LAST, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					toolbar.getPanel().getTable().goToLast();
				}
			});
			toolbar.addSeparator();
		}

		// Add button
		if (toolbar.getPanel().getModelPanel().getPanelSettings().getAdd() == true) {
			toolbar.makeButton("add.gif", Buttons.ADD, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					toolbar.getPanel().handleAdd();
				}
			});
		}

		// Copy button
		if (toolbar.getPanel().getModelPanel().getPanelSettings().getCopy() == true) {
			toolbar.makeButton("copy.gif", Buttons.COPY, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					toolbar.getPanel().handleCopy();
				}
			});
		}

		// Remove button
		if (toolbar.getPanel().getModelPanel().getPanelSettings().getDelete() == true) {
			toolbar.makeButton("remove.gif", Buttons.REMOVE, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					toolbar.getPanel().getTable().handleDelete();
				}
			});
		}

		// Next button with popup menu
		if(toolbar.getPanel().getModelPanel().getNextPanels().size() > 0) {
			toolbar.makeButton("chain.gif", Buttons.NEXT_MENU, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					JPopupMenu popup = new JPopupMenu();
					JMenuItem nextItem = null;
					List<Next> nexts = toolbar.getPanel().getModelPanel().getNextPanels();
					for (int i = 0; i < nexts.size(); i++) {
						nextItem = new JMenuItem(nexts.get(i).getLabel());
						nextItem.addActionListener(new NextActionListener(nexts.get(i), toolbar.getPanel()));
						popup.add(nextItem);
					}
					popup.show((JButton)event.getSource(),0,((JButton)event.getSource()).getHeight());
				}
			});
		}
	}

	/****
	 * Disabling buttons 
	 * @param enable Enables or disables buttons depending on state
	 */
	public void SToolBar.setEnablingOnToolBar(boolean enable) {
		StateMode state = getPanel().getModelPanel().getPanelSettings().getStateMode();
		switch(state) {
		case ADD: 
			// Disable all buttons that are present on toolbar
			Iterator<String> iter = getButtons().keySet().iterator();
			while (iter.hasNext()) {
				setEnabledButton(iter.next(), enable);
			}
			break;
		case SEARCH: 
			setEnabledButton(Buttons.SEARCH, enable);
			setEnabledButton(Buttons.ADD, enable);
			setEnabledButton(Buttons.REMOVE, enable);
			setEnabledButton(Buttons.COPY, enable);
			break;
		default: 
			Iterator<String> iter2 = getButtons().keySet().iterator();
			while (iter2.hasNext()) {
				setEnabledButton(iter2.next(), enable);
			}
		}
	}

	/***
	 * Enable or disable one button
	 * @param name Key for map of buttons
	 * @param enable If true then button will be enabled or else it will be disabled
	 */
	public void SToolBar.setEnabledButton(String name, boolean enable) {
		if(name != null) {
			JButton btn = getButtons().get(name);
			if(btn != null) {
				btn.setEnabled(enable);
			}
		}
	}
}
