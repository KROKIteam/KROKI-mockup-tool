/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.dialog.KrokiMockupToolAboutDialog;
import kroki.app.model.Workspace;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.common.copy.DeepCopy;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class SaveAllAction extends AbstractAction {

    public SaveAllAction() {
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.saveAll.smallIcon"));
        ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.saveAll.largeIcon"));
        putValue(SMALL_ICON, smallIcon);
        putValue(LARGE_ICON_KEY, largeIcon);
        putValue(NAME, StringResource.getStringResource("action.saveAll.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.saveAll.description"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
        //setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
    	Workspace workspace = KrokiMockupToolApp.getInstance().getWorkspace();
    	JFileChooser jfc = new JFileChooser();
    	jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int retValue = jfc.showSaveDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
		if (retValue == JFileChooser.APPROVE_OPTION) {
			for(int i=0; i<workspace.getPackageCount(); i++) {
				BussinesSubsystem sys = (BussinesSubsystem) workspace.getPackageAt(i);
				String fileName = jfc.getSelectedFile().getAbsolutePath() + File.separator + sys.getLabel().replace(" ", "_");
				System.out.println("saving to file: " + fileName);
				DeepCopy.save(sys, new File(fileName));
			}
			
		} else {
			System.out.println("saving canceled: ");
		}
    }
}
