package kroki.app.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

import kroki.profil.VisibleElement;

/**
 * Contains list of visible elements which represent the contents
 * of the clipboard
 * @author Kroki Team
 *
 */
public class ClipboardContents implements Transferable {

	private List<VisibleElement> visibleElements;
	
	public static DataFlavor clipboardVisibleElementsFlavor = new DataFlavor(VisibleElement.class, VisibleElement.class.getSimpleName());
	
	public ClipboardContents(List<VisibleElement> visibleElements) {
		this.visibleElements = visibleElements;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { clipboardVisibleElementsFlavor };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(clipboardVisibleElementsFlavor);
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor.equals(clipboardVisibleElementsFlavor))
            return visibleElements;
		else throw new UnsupportedFlavorException(flavor);
	}

}
