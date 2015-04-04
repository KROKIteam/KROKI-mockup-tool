package kroki.app.model;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kroki.app.KrokiMockupToolApp;
import kroki.app.view.Canvas;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;

/**
 * Clipboard manager, used to implement cut/copy/paste features  
 * @author Kroki Team
 */
public class ClipboardManager implements ClipboardOwner {

	private Clipboard clipboard;
	
	private static ClipboardManager clipboardManager;
	
	private ClipboardContents newClipboard = new ClipboardContents(null);
	
	private Canvas canvas;
	
	private ClipboardManager () {
		this.clipboard = new Clipboard(KrokiMockupToolApp.getInstance().toString());
		canvas = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent();
	}
	
	public List<VisibleElement> copySelectedElements() {
        clearClipboard();

        List<VisibleElement> cutted = new ArrayList<VisibleElement>();
        for (VisibleElement visibleElement : canvas.getSelectionModel().getVisibleElementList()) {
            if (!(visibleElement instanceof VisibleClass) && visibleElement.getParentGroup() != null) {
            	cutted.add(visibleElement);
            }
        }
       
        ClipboardContents contents = new ClipboardContents(cutted);
        clipboard.setContents(contents, this);

        return cutted;
    }
	
	public List<VisibleElement> cutSelectedElements() {
        return copySelectedElements();
    }
	
	public List<?> getElement(DataFlavor dataFlavor) {
		Transferable clipboardContent = clipboard.getContents(this);
		List<?> elements = null;
		try {
			if (clipboardContent instanceof Transferable)
				elements = (List<?>) clipboardContent
						.getTransferData(dataFlavor);
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return elements;
	}
	
	public void clearClipboard() {
		clipboard.setContents(newClipboard, this);
	}
	
	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
	}
	
	public static ClipboardManager getInstance() {
		if (clipboardManager == null)
			clipboardManager = new ClipboardManager();
		return clipboardManager;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

}
