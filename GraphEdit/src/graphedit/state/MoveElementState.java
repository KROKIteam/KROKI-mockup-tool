package graphedit.state;

import graphedit.app.MainFrame;
import graphedit.command.Command;
import graphedit.command.MoveElementsCommand;
import graphedit.command.MoveLinkNodeCommand;
import graphedit.model.components.Connector;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.components.LinkableElement;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.model.properties.PropertyEnums.LinkNodeProperties;
import graphedit.properties.Preferences;
import graphedit.strategy.AsIsStrategy;
import graphedit.strategy.LinkStrategy;
import graphedit.strategy.RightAngledStrategy;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;
import graphedit.view.LinkNodePainter;
import graphedit.view.GraphEditView.GraphEditController;

import java.awt.Cursor;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.SwingUtilities;


public class MoveElementState extends State {

	private int startXPos, startYPos;
	private int oldXPos, oldYPos;
	private ElementPainter painter;
	private LinkableElement linkableElement;
	private Link link;
	private Shape elementShape; //za proveru da li se moze pomerati konektor
	private Rectangle2D.Double connectorShape;
	private Point2D testPoint;
	private LinkNode linkNode;
	private Preferences prefs = Preferences.getInstance();
	private LinkStrategy strategy;


	/**
	 * represents a list of links contained by the selection of elements
	 * whose link nodes' position should be changed
	 * links = model.getContainedLinks(elements)
	 */
	private List<Link> links;

	public MoveElementState(GraphEditView view, GraphEditController controller) {
		super(controller);
		this.view = view;
	}

	public MoveElementState() {
	}
	
	
	public void cancelMove(){
		//if esc is pressed wait for mouse buttons to be released 
		//otherwise the state will keep changing from move to select
		State state = MainFrame.getInstance().getCurrentView().getModel().getWaitToBeReleasedState();
		state.setView(view);
		state.setController(controller);
		state.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		MainFrame.getInstance().setStatusTrack("Pleae release all mouse buttons");
		controller.setCurrentState(state);
		

		int deltaX = oldXPos - startXPos;
		int deltaY = oldYPos - startYPos;

		if (linkNode!=null ){
			link.setMovedNodeIndex(-1);
			link=null;
			if (linkNode instanceof Connector)
				((Connector) linkNode).setRelativePositions((Point2D)linkNode.getProperty(LinkNodeProperties.POSITION));
			painter.formShape();
			linkNode=null;
		}
		else{
			// vrati elemente na pocetnu poziciju
			for (GraphElement graphElement : view.getSelectionModel().getSelectedElements()) {
				view.getElementPainter(graphElement).setShape();
				if (graphElement instanceof LinkableElement) {
					linkableElement = (LinkableElement) graphElement;
					linkableElement.moveAllConnectors(-deltaX, -deltaY);
				}
			}
			for (Link link : links) 
				link.moveNodes(-deltaX, -deltaY);
			links=null;
		}
		view.repaint();
		
	}
	
	
	/**
	 * Cancel action if Esc is pressed
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) 
			cancelMove();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int xPos = e.getX();
		int yPos = e.getY();

		MainFrame.getInstance().setPositionTrack(xPos, yPos);

		int deltaX = xPos - oldXPos;
		int deltaY = yPos - oldYPos;
		if (deltaX != 0 || deltaY != 0) {
			oldXPos = xPos;
			oldYPos = yPos;
			if (linkNode!=null){
				if (linkNode instanceof Connector){
					testPoint.setLocation(link.getMovedNodePosition().getX()+deltaX,link.getMovedNodePosition().getY()+deltaY);
					if (connectorCanBeMoved((Connector)linkNode)){		
						link.moveNode(deltaX,deltaY);
						painter.moveShape(deltaX, deltaY);
						((Connector) linkNode).setRelativePositions(link.getMovedNodePosition());
					}
				}
				else{
					link.moveNode(deltaX,deltaY);
					painter.moveShape(deltaX, deltaY);
				}
			}
			else{
				for (GraphElement graphElement : view.getSelectionModel().getSelectedElements()) {
					painter = view.getElementPainter(graphElement);

					painter.moveShape(deltaX, deltaY);

					if (graphElement instanceof LinkableElement) {
						linkableElement = (LinkableElement) graphElement;
						linkableElement.moveAllConnectors(deltaX, deltaY);
					}
				}
				
				if (links==null)
					links=view.getModel().getContainedLinks(view.getSelectionModel().getSelectedElements());
				for (Link link : links) 
					link.moveNodes(deltaX, deltaY);
				
			}
			view.repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//initialize if left mouse button
		if (SwingUtilities.isLeftMouseButton(e)){
			startXPos = oldXPos = e.getX();
			startYPos = oldYPos = e.getY();
		
		if ( view!=null && view.getSelectionModel().getSelectedNode()!=null){
			link=view.getSelectionModel().getSelectedLink();
			link.setMovedNodeIndex(link.getNodes().indexOf(view.getSelectionModel().getSelectedNode()));
			link.setMoveNodePosition((Point2D) view.getSelectionModel().getSelectedNode().getProperty(LinkNodeProperties.POSITION));
			painter=view.getLinkNodePainter(view.getSelectionModel().getSelectedNode());
			linkNode=view.getSelectionModel().getSelectedNode();
			if (linkNode instanceof Connector){
				setElementShape((Connector)linkNode);
				connectorShape=new Rectangle2D.Double();
				testPoint=new Point2D.Double();
			}
		}
		if (prefs.getProperty(Preferences.RIGHT_ANGLE).equalsIgnoreCase("true")) 
			//	if (!(strategy instanceof RightAngledStrategy))
			strategy = new RightAngledStrategy();
		else 
			//if (!(strategy instanceof AsIsStrategy))
			strategy = new AsIsStrategy();
		}

}

@Override
public void mouseReleased(MouseEvent e) {
	Command command;
	// konacno pomeranje

	//check if left or right button - cancel or execute action
	int deltaX;
	int deltaY;
	if (SwingUtilities.isRightMouseButton(e)){
		cancelMove();
		return;
	}
	else{
		deltaX = e.getX() - startXPos;
		deltaY = e.getY() - startYPos;
	}


	if (link!=null){
		Point2D oldPosition=(Point2D) linkNode.getProperty(LinkNodeProperties.POSITION);
		Point2D newPosition=link.getMovedNodePosition();
		link.setMovedNodeIndex(-1);
		link=null;
		linkNode=null;
		command=new MoveLinkNodeCommand(view, newPosition, oldPosition,strategy);
	}
	else
	{
		// vrati konektore na pocetnu poziciju
		for (GraphElement graphElement : view.getSelectionModel().getSelectedElements()) {
			if (graphElement instanceof LinkableElement) {
				linkableElement = (LinkableElement) graphElement;
				linkableElement.moveAllConnectors(-deltaX, -deltaY);
			}
		}	
		if (links!=null)
			for (Link link : links) 
				link.moveNodes(-deltaX, -deltaY);
		links=null;
		// i onda sve pomeri komandom
		command = new MoveElementsCommand(view, deltaX, deltaY,strategy);
	}

	view.getModel().getCommandManager().executeCommand(command);
	switchToDefaultState();
}


@Override
public boolean isAutoScrollOnDragEnabled() {
	return true;
}

@Override
public boolean isAutoScrollOnMoveEnabled() {
	return false;
}

private boolean connectorCanBeMoved(Connector connector){
	double dim=((LinkNodePainter)painter).getDim();
	Point2D position=testPoint;
	connectorShape.setRect(position.getX() - dim/2, position.getY()- dim/2, dim, dim);
	return elementShape.contains(connectorShape);
}
private void setElementShape(Connector connector){
	GraphElement element=model.getFromElementByConnectorStructure(connector);
	Point2D position = (Point2D) element.getProperty(GraphElementProperties.POSITION);
	Dimension2D size = (Dimension2D) element.getProperty(GraphElementProperties.SIZE);
	elementShape=new Rectangle2D.Double(
			position.getX() - size.getWidth()/2, 
			position.getY() - size.getHeight()/2, 
			size.getWidth(), 
			size.getHeight());
}

}