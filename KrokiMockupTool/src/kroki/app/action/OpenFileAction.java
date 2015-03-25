package kroki.app.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import kroki.app.KrokiMockupToolApp;
import kroki.app.model.Workspace;
import kroki.app.utils.SaveUtil;
import kroki.app.utils.StringResource;

/**
 * Loads a saved project
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class OpenFileAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public OpenFileAction() {
        putValue(NAME, StringResource.getStringResource("action.openFile.name"));
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.openFile.description"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O,KeyEvent.CTRL_DOWN_MASK));
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    	JFileChooser jfc = new JFileChooser();
        int retValue = jfc.showOpenDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
        if (retValue == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            System.out.println("opening from file: " + file.getAbsolutePath());
            Workspace workspace = null;
            try {
                workspace = (Workspace) SaveUtil.loadXStream(file);
                //KrokiMockupToolApp.getInstance().getWorkspace().addBussinesSubsystem(bussinesSubsystem);
                KrokiMockupToolApp.getInstance().setWorkspace(workspace);
                KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
            } catch (Exception ex) {
            	ex.printStackTrace();
                JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "Opening failed.");
            }

        } else {
            System.out.println("opening canceled: ");
        }
    }
}
