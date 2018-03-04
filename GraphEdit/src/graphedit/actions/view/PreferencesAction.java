package graphedit.actions.view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import graphedit.app.MainFrame;
import graphedit.gui.dialog.PreferencesDialog;
import graphedit.properties.Preferences;
import graphedit.util.ResourceLoader;

public class PreferencesAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private Preferences prefs;
	
	private PreferencesDialog preferencesDialog;
	
	public PreferencesAction() {
		putValue(NAME, "Preferences");
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("preferences.png"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_P);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		putValue(SHORT_DESCRIPTION, "View preferences...");
		prefs = Preferences.getInstance();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {		
		
		preferencesDialog = new PreferencesDialog();
		preferencesDialog.setVisible(true);
		if (preferencesDialog.getReturnCode() == PreferencesDialog.APPLY) {
			if (preferencesDialog.isLoadDefaultsChecked()) {
				prefs.setProperty(Preferences.CLASS_COLOR_1, prefs.getProperty(Preferences.DEFAULT + Preferences.CLASS_COLOR_1));
				prefs.setProperty(Preferences.CLASS_COLOR_2, prefs.getProperty(Preferences.DEFAULT + Preferences.CLASS_COLOR_2));
				prefs.setProperty(Preferences.INTERFACE_COLOR_1, prefs.getProperty(Preferences.DEFAULT + Preferences.INTERFACE_COLOR_1));
				prefs.setProperty(Preferences.INTERFACE_COLOR_2, prefs.getProperty(Preferences.DEFAULT + Preferences.INTERFACE_COLOR_2));
				prefs.setProperty(Preferences.MIN_ZOOM, prefs.getProperty(Preferences.DEFAULT + Preferences.MIN_ZOOM));
				prefs.setProperty(Preferences.MAX_ZOOM, prefs.getProperty(Preferences.DEFAULT + Preferences.MAX_ZOOM));
				prefs.setProperty(Preferences.BESTFIT_FACTOR, prefs.getProperty(Preferences.DEFAULT + Preferences.BESTFIT_FACTOR));
				prefs.setProperty(Preferences.BESTFIT_FACTOR, prefs.getProperty(Preferences.DEFAULT + Preferences.BESTFIT_FACTOR));
				prefs.setProperty(Preferences.RIGHT_ANGLE, prefs.getProperty(Preferences.DEFAULT + Preferences.RIGHT_ANGLE));
			} else {
				prefs.setProperty(Preferences.CLASS_COLOR_1, prefs.unparseColor(preferencesDialog.getClassColor1()));
				prefs.setProperty(Preferences.CLASS_COLOR_2, prefs.unparseColor(preferencesDialog.getClassColor2()));
				prefs.setProperty(Preferences.INTERFACE_COLOR_1, prefs.unparseColor(preferencesDialog.getInterfaceColor1()));
				prefs.setProperty(Preferences.INTERFACE_COLOR_2, prefs.unparseColor(preferencesDialog.getInterfaceColor2()));
				prefs.setProperty(Preferences.MIN_ZOOM, preferencesDialog.getMinimumZoomFactor());
				prefs.setProperty(Preferences.MAX_ZOOM, preferencesDialog.getMaximumZoomFactor());
				prefs.setProperty(Preferences.BESTFIT_FACTOR, preferencesDialog.getBestFitFactor());
				prefs.setProperty(Preferences.RIGHT_ANGLE, preferencesDialog.isRightAngleChecked());
			}
			prefs.saveProperties();
			System.out.println("Preferences applied.");
			
			if (preferencesDialog.isColorChanged() || preferencesDialog.isLoadDefaultsChecked()) 
				MainFrame.getInstance().updateColorsForAllOpenViews();
			
			if (preferencesDialog.isLoadDefaultsChecked())
				JOptionPane.showMessageDialog(MainFrame.getInstance(), "Default settings has been restored.");
		} else {
			System.out.println("Preferences canceled.");
		}
	}

}

