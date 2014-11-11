/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.gui.settings;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import kroki.app.KrokiMockupToolApp;
import kroki.app.event.UpdateListener;
import kroki.app.model.SelectionModel;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class SettingsFactory implements UpdateListener, SettingsCreator {

	private HashMap<String, Settings> settingsMap;

	public SettingsFactory() {
		settingsMap = new HashMap<String, Settings>();
	}

	public void treeUpdatePerformed(VisibleElement visibleElement){
		performUpdate(visibleElement);
	}

	public void updatePerformed(EventObject e) {
		SelectionModel selectionModel = (SelectionModel) e.getSource();
		if (selectionModel.getSelectionNum() == 1) {
			VisibleElement visibleElment = selectionModel.getVisibleElementAt(0);
			performUpdate(visibleElment);
		} else {
			KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getLeftSplitPane().setRightComponent(null);
			KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getLeftSplitPane().resetToPreferredSizes();
		}

	}

	private void performUpdate(VisibleElement visibleElment){

		SettingsPanel annotation = visibleElment.getClass().getAnnotation(SettingsPanel.class);
		if (annotation != null) {
			Class clazz = annotation.value();
			Settings settings = null;
			if (settingsMap.containsKey(clazz.getName())) {
				settings = settingsMap.get(clazz.getName());
			} else {
				try {
					settings = (Settings) clazz.getDeclaredConstructor(SettingsCreator.class).newInstance(this);
					settingsMap.put(clazz.getName(), settings);
				} catch (NoSuchMethodException ex) {
					Logger.getLogger(SettingsFactory.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SecurityException ex) {
					Logger.getLogger(SettingsFactory.class.getName()).log(Level.SEVERE, null, ex);
				} catch (InstantiationException ex) {
					Logger.getLogger(SettingsFactory.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					Logger.getLogger(SettingsFactory.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					Logger.getLogger(SettingsFactory.class.getName()).log(Level.SEVERE, null, ex);
				} catch (InvocationTargetException ex) {
					Logger.getLogger(SettingsFactory.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

			if (settings != null) {
				settings.updateSettings(visibleElment);
				KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getLeftSplitPane().setRightComponent((Component) settings);
				KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getLeftSplitPane().resetToPreferredSizes();
			}

		}



	}

	public void settingsPreformed() {
		if (KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent() != null){
			KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent().getVisibleClass().update();
			KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent().repaint();
		}
	}

	public void settingsPreformedIncludeTree() {
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
		if (KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent() != null){
			VisibleClass visibleClass = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent().getVisibleClass();
			int index = KrokiMockupToolApp.getInstance().getTabbedPaneController().getTabIndex(visibleClass);
			KrokiMockupToolApp.getInstance().getTabbedPaneController().getTabbedPane().setTitleAt(index, visibleClass.getLabel());
			KrokiMockupToolApp.getInstance().getTabbedPaneController().getTabbedPane().updateUI();
			visibleClass.update();
			KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent().repaint();
		}
	}
}
