package graphedit.actions.view;

import graphedit.app.MainFrame;
import graphedit.model.GraphEditWorkspace;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.elements.GraphEditPackage;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.util.ResourceLoader;
import graphedit.view.GraphEditView;

import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

public class FindInProjectAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private GraphElement element;

	public FindInProjectAction() {
		putValue(NAME, "Find in project");
		putValue(SMALL_ICON, new ResourceLoader().loadImageIcon("folder_search.png"));
		putValue(SHORT_DESCRIPTION, "Find in project...");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {		

		TreePath selectedPath = MainFrame.getInstance().getMainTree().getSelectionPath();
		Object selected = selectedPath.getLastPathComponent();
		if (selected instanceof GraphEditPackage)
			selected = ((GraphEditPackage)selected).getPackageElement();
		
		if (selected instanceof GraphElement){
			
			element = (GraphElement) selected;
			
			GraphEditModel model = GraphEditWorkspace.getInstance().findModelContainingElement(element);
			MainFrame.getInstance().showDiagram(model);
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					Point2D position;
					GraphEditView view = MainFrame.getInstance().getCurrentView();
					view.getSelectionModel().clearSelection();
					if (element instanceof Link){
						Link link = (Link) element;
						view.getSelectionModel().setSelectedLink(link);
						position = (Point2D) link.getSourceConnector().getProperty(LinkNodeProperties.POSITION); 
					}
					else {
						view.getSelectionModel().addSelectedElement(element);
						position = (Point2D) element.getProperty(GraphElementProperties.POSITION);
					}
					view.zoomToPoint(position);
					
				}
			});
			
			
		}

	}

}

