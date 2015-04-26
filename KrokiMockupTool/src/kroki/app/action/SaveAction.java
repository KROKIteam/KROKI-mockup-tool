package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.app.view.Canvas;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Saves current project
 * @author Kroki Team
 */
public class SaveAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

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

		//get selected item from jtree and find its project
		TreePath path =  KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath();
		
		System.out.println(path);
		if (path != null){
			Object node = path.getLastPathComponent();
			if(node != null) {
				//if package is selected, find parent project
				if(node instanceof BussinesSubsystem) {
					BussinesSubsystem subsys = (BussinesSubsystem) node;
					proj = KrokiMockupToolApp.getInstance().findProject(subsys);
				}else if(node instanceof VisibleClass) {
					//if panel is selected, get parent node from tree and find project
					JTree tree = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree();
					Object parent = tree.getSelectionPath().getParentPath().getLastPathComponent();
					if(parent instanceof BussinesSubsystem) {
						proj = KrokiMockupToolApp.getInstance().findProject((BussinesSubsystem)parent);
					}
				}
			}

			if(proj == null) {
				if(KrokiMockupToolApp.getInstance().getTabbedPaneController().getTabbedPane().getTabCount() != 0) {
					Canvas canv = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent();
					VisibleClass vc = canv.getVisibleClass();
					BussinesSubsystem parent = (BussinesSubsystem) vc.umlPackage();
					proj = KrokiMockupToolApp.getInstance().findProject(parent);
				}
			}
		}

		if (proj != null)
			KrokiMockupToolApp.getInstance().getWorkspace().saveProject(proj);
		else
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
	}
}
