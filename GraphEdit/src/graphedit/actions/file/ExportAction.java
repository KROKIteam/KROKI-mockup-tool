package graphedit.actions.file;

import graphedit.util.GraphicsExportUtility;
import graphedit.util.ResourceLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class ExportAction extends AbstractAction  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExportAction() {
		putValue(NAME, "Export");
		putValue(MNEMONIC_KEY, KeyEvent.VK_P);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK));
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("export_icon.png"));
		putValue(SHORT_DESCRIPTION, "Export to image");
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GraphicsExportUtility.getInstance().export();
	}

}
