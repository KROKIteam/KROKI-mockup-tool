/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.app.utils.uml.OperationsUtil;
import kroki.profil.VisibleElement;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class DeleteAction extends AbstractAction {

	VisibleElement visibleElement;

	public DeleteAction(VisibleElement visibleElement) {
		this.visibleElement = visibleElement;
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.delete.smallIcon"));
		ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.delete.largeIcon"));
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(NAME, StringResource.getStringResource("action.delete.name"));
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.delete.description"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
	}

	public void actionPerformed(ActionEvent e) {

		int result = JOptionPane.showConfirmDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(),
				StringResource.getStringResource("action.delete.message"),StringResource.getStringResource("action.delete.title"),
				JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.YES_OPTION) {
			OperationsUtil.delete(visibleElement);
		}
	}
}
