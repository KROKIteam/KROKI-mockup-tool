package graphedit.command;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.components.LinkableElement;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.strategy.LinkStrategy;
import graphedit.view.GraphEditView;

public class ResizeElementsCommand extends Command {
   private GraphElement element;
   private Point2D startPosition, endPosition;
   private Dimension2D startDimension, endDimension;
   private Link link;
   private double sx;
   private double sy;
   private LinkStrategy strategy;
	private ArrayList<ArrayList<Point2D>> oldLinkNodePositions;
	private List<Link> associatedLinks;
   
   private GraphEditModel model;
   
   public ResizeElementsCommand(GraphEditView view, GraphElement element,  Shape shape, LinkStrategy strategy) {
	   this.view = view;
	   this.model = view.getModel();
	   this.element = element;
	   this.startPosition = (Point2D)element.getProperty(GraphElementProperties.POSITION);
	   this.startDimension = (Dimension2D)element.getProperty(GraphElementProperties.SIZE);
	   this.endDimension = shape.getBounds().getSize();
	   this.endPosition = new Point2D.Double(shape.getBounds().getCenterX(), shape.getBounds().getCenterY());
	   this.strategy=strategy;
	   sx=endDimension.getWidth()/startDimension.getWidth();
	   sy=endDimension.getHeight()/startDimension.getHeight(); //zbog undo/redo
	   if(element instanceof LinkableElement)
		   ((LinkableElement)element).changeRelativePositions(1/sx,1/sy);	{
	   oldLinkNodePositions=new ArrayList<ArrayList<Point2D>>();
		associatedLinks = view.getModel().getAssociatedLinks(element);
		for (Link link:associatedLinks){
			ArrayList<Point2D> oldList=new ArrayList<Point2D>();
			for (LinkNode ln:link.getNodes())
				oldList.add((Point2D)ln.getProperty(LinkNodeProperties.POSITION));
			oldLinkNodePositions.add(oldList);
		}
		   }
   }
   
   public void execute() {
	   element.setProperty(GraphElementProperties.POSITION, endPosition);
	   element.setProperty(GraphElementProperties.SIZE, endDimension);
	   view.getElementPainter(element).formShape();
	   if(element instanceof LinkableElement){
		   ((LinkableElement)element).scaleAllConnectors(sx,sy,view.getElementPainter(element).getShape());
		   LinkableElement linkableElement = (LinkableElement) element;
		   for (Connector conn : linkableElement.getConnectors()) {
				link=conn.getLink();
				link.setNodes(strategy.setLinkNodes(link.getNodes()));
	   }
	   }
	   model.fireUpdates();
   }
   
   public void undo() {
	   element.setProperty(GraphElementProperties.POSITION, startPosition);
	   element.setProperty(GraphElementProperties.SIZE, startDimension);
	   view.getElementPainter(element).formShape();
	   
	  if(element instanceof LinkableElement){
		//  Connector conn;
		   ((LinkableElement)element).scaleAllConnectors(1/sx,1/sy,view.getElementPainter(element).getShape());
		  // LinkableElement linkableElement = (LinkableElement) element;
			for (int i=0;i<associatedLinks.size();i++){
				link=associatedLinks.get(i);
					link.setNodesWithPositions(oldLinkNodePositions.get(i));
			}
		}
	   model.fireUpdates();
   }
   
   public GraphEditModel getModel() {
      return model;
   }

   public void setModel(GraphEditModel newGraphEditModel) {
      this.model = newGraphEditModel;
   }

}