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
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.app.view.Canvas;
import kroki.common.copy.DeepCopy;
import kroki.profil.panel.StandardPanel;
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
		try {
			BussinesSubsystem proj = null;
			
			//get selected item from jtree and find its project
			TreePath path =  KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath();
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
					System.out.println(vc.getLabel());
				}
			}
			
			//if project allready has a file to save, save to that file, else display choose file dialog
			if(proj.getFile() != null) {
				System.out.println("saving to file: " + proj.getFile().getAbsolutePath());
				if (DeepCopy.saveXStream(proj, proj.getFile())){
					JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), 
							"Project " + (String)proj.getLabel() +  " successfully saved!");
				}
				else{
					JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), 
							"Project " + (String)proj.getLabel() +  " wasn't successfully saved");
				}
			}else {
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
					proj.setFile(file);
					if (DeepCopy.saveXStream(proj, proj.getFile())){
						JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), 
								"Project " + (String)proj.getLabel() +  " successfully saved!");
					}
					else{
						JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), 
								"Project " + (String)proj.getLabel() +  " wasn't saved successfully");
					}
				} else {
					System.out.println("saving canceled: ");
				}
			}
		//if no project is selected, display message to user
		} catch (NullPointerException e2) {
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
		}
	}
}
