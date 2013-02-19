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
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.app.view.Canvas;
import kroki.common.copy.DeepCopy;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 *
 * @author Vladan Marsenic (vladan.marsenic@gmail.com)
 */
public class SaveAction extends AbstractAction {

	public SaveAction() {
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.save.smallIcon"));
		ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.save.largeIcon"));
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(NAME, StringResource.getStringResource("action.save.name"));
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.save.description"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
	}

	public void actionPerformed(ActionEvent e) {
		//find selected project to save
		BussinesSubsystem proj = null;
		try {
			String selectedNoded = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath().getLastPathComponent().toString();
			for(int j=0; j<KrokiMockupToolApp.getInstance().getWorkspace().getPackageCount(); j++) {
				BussinesSubsystem pack = (BussinesSubsystem)KrokiMockupToolApp.getInstance().getWorkspace().getPackageAt(j);
				if(pack.getLabel().equals(selectedNoded)) {
					proj = pack;
				}
			}
			
			JFileChooser jfc = new JFileChooser();
			jfc.setSelectedFile(new File(proj.getLabel().replace(" ", "_")));
			FileFilter filter = new FileNameExtensionFilter("KROKI files", "kroki");
	        jfc.setAcceptAllFileFilterUsed(false);
	        jfc.setFileFilter(filter);
			int retValue = jfc.showSaveDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
			if (retValue == JFileChooser.APPROVE_OPTION) {
				File file = jfc.getSelectedFile();
				if(!file.getAbsolutePath().endsWith(".kroki")) {
					file = new File(file.getAbsolutePath() + ".kroki");
				}
				System.out.println("saving to file: " + file.getAbsolutePath());
				DeepCopy.save(proj, file);
			} else {
				System.out.println("saving canceled: ");
			}
		//if no project is selected, display message to user
		} catch (NullPointerException e2) {
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
		}
	}
}
