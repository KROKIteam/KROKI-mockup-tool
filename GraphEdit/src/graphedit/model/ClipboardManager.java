package graphedit.model;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import graphedit.app.MainFrame;
import graphedit.model.components.GraphElement;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;
import graphedit.view.LinkPainter;

public class ClipboardManager implements ClipboardOwner {

	private Clipboard clipboard;

	private static ClipboardManager clipboardManager;

	private ClipboardContents emptyClipboard = new ClipboardContents(null, null);
	
	private GraphEditView view;
	
	private ClipboardManager() {
		this.clipboard = new Clipboard(MainFrame.getInstance().toString());
		view = MainFrame.getInstance().getCurrentView();
	}

	public List<ElementPainter> copySelectedElements() {
		clearClipboard();
		// gets contained links
		List<GraphElement> selectedElements = new ArrayList<GraphElement>(view.getSelectionModel().getSelectedElements());
		List<ElementPainter> selectedElementPainters = new ArrayList<ElementPainter>(view.getElementPainters(selectedElements));
		List<LinkPainter> containedLinkPainters = new ArrayList<LinkPainter>(view.getLinkPainters(view.getModel().getContainedLinks(selectedElements)));
		// those two encapsulate elements, and links respectively...
		ClipboardContents contents = new ClipboardContents(selectedElementPainters, containedLinkPainters);
		clipboard.setContents(contents, this);

		System.out.println("ClipboardManager - Copied " + selectedElements.size() + " elements!");

		return selectedElementPainters;
	}

	public List<ElementPainter> cutSelectedElements() {
		System.out.println("ClipboardManager - Elements Cut!");
		return copySelectedElements();
	}

	public List<?> getElement(DataFlavor dataFlavor) {
		Transferable clipboardContent = clipboard.getContents(this);
		List<?> elements = null;
		try {
			if (clipboardContent instanceof Transferable)
				elements = (List<?>) clipboardContent.getTransferData(dataFlavor);
		} catch (UnsupportedFlavorException e) {
		} catch (IOException e) {
		}
		return elements;
	}
	
	private void clearClipboard() {
		clipboard.setContents(emptyClipboard, this);
	}
	
	public static ClipboardManager getInstance() {
		if (clipboardManager == null)
			clipboardManager = new ClipboardManager();
		return clipboardManager;
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
	}

	public GraphEditView getView() {
		return view;
	}

	public void setView(GraphEditView view) {
		this.view = view;
	}
	
}
