package graphedit.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

import graphedit.view.ElementPainter;
import graphedit.view.LinkPainter;

public class ClipboardContents implements Transferable {

	private List<ElementPainter> elementPainters;
	
	private List<LinkPainter> linkPainters;
	
	public static DataFlavor clipboardLinkPaintersFlavor = new DataFlavor(LinkPainter.class, LinkPainter.class.getSimpleName());

	public static DataFlavor clipboardElementPaintersFlavor = new DataFlavor(ElementPainter.class, ElementPainter.class.getSimpleName());
	
	public ClipboardContents(List<ElementPainter> elementPainters, List<LinkPainter> linkPainters) {
		this.linkPainters = linkPainters;
		this.elementPainters = elementPainters;
	}
	
	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (flavor.equals(clipboardLinkPaintersFlavor))
			return linkPainters;
		else if (flavor.equals(clipboardElementPaintersFlavor))
			return elementPainters;
		else throw new UnsupportedFlavorException(flavor);
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { clipboardLinkPaintersFlavor, clipboardElementPaintersFlavor };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(clipboardLinkPaintersFlavor) || 
			flavor.equals(clipboardElementPaintersFlavor);
	}

}
