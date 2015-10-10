package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.dialog.NewFileDialog;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Shows new file dialog (for inputting date regarding a new panel)
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class NewFileAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private BussinesSubsystem bussinesSubsystem;

	public NewFileAction() {
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.newFile.smallIcon"));
		ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.newFile.largeIcon"));
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(NAME, StringResource.getStringResource("action.newFile.name"));
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.newFile.description"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
	}

	public NewFileAction(BussinesSubsystem bussinesSubsystem) {
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.newFile.smallIcon"));
		ImageIcon largeIcon = new ImageIcon(ImageResource.getImageResource("action.newFile.largeIcon"));
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(NAME, StringResource.getStringResource("action.newFile.name"));
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.newFile.description"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));

		this.bussinesSubsystem = bussinesSubsystem;
	}

	public void actionPerformed(ActionEvent e) {
		if (KrokiMockupToolApp.getInstance().getWorkspace().getPackageCount() == 0){
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "Please create a project first");
			return;
		}

		BussinesSubsystem selectedPack = null;
		if (bussinesSubsystem == null){
			Object selected = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getLastSelectedPathComponent();
			if (selected instanceof BussinesSubsystem)
				selectedPack = (BussinesSubsystem) selected;
			else if (selected instanceof VisibleClass){
				selectedPack = (BussinesSubsystem) ((VisibleClass)selected).umlPackage();
			}
			else{ //workspace
			}
		}
		
		NewFileDialog dialog;
		if (bussinesSubsystem != null)
			dialog = new NewFileDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), bussinesSubsystem);
		else
			dialog = new NewFileDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), selectedPack);
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		if (dialog.getVisibleClass() != null) {
			KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
			KrokiMockupToolApp.getInstance().getTabbedPaneController().openTab(dialog.getVisibleClass());
		}
	}
}
