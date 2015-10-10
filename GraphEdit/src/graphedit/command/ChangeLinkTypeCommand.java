package graphedit.command;
import graphedit.model.components.Link;
import graphedit.model.components.LinkableElement;
import graphedit.view.GraphEditView;
import graphedit.view.LinkPainter;

public class ChangeLinkTypeCommand extends Command{
	private CutLinkCommand cutLink;
	private LinkElementsCommand linkElements;

	
	public void execute() {

		cutLink.execute();
		linkElements.execute();

	}

	public void undo() {
		linkElements.undo();	
		cutLink.undo();
	}


	public ChangeLinkTypeCommand(GraphEditView view, Link oldLink, Link newLink, LinkPainter linkPainter) {
		cutLink=new CutLinkCommand(view,oldLink);
		LinkableElement sourceElement=(LinkableElement) view.getModel().getFromElementByConnectorStructure(oldLink.getSourceConnector());
		LinkableElement destinationElement=(LinkableElement) view.getModel().getFromElementByConnectorStructure(oldLink.getDestinationConnector());
		linkElements=new LinkElementsCommand(view, newLink, linkPainter,sourceElement,destinationElement);
	}
}
