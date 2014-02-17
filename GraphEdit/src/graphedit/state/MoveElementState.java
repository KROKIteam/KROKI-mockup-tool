package graphedit.state;

import graphedit.actions.popup.MoveElementPopup;
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
import graphedit.view.GraphEditView.GraphEditController;
import graphedit.view.LinkNodePainter;
import graphedit.view.LinkPainter;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
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
	private List<GraphElement> elements;
	private boolean rightMove;
	private LinkNode[] segmentNodes;
	private LinkNodePainter[] nodePainters;
	private List<Point2D> oldPositions;



	/**
	 * represents a list of links contained by the selection of elements
	 * whose link nodes' position should be changed
	 * links = model.getContainedLinks(elements)
	 */
	private List<Link> links;

	public MoveElementState(GraphEditView view, GraphEditController controller, List<GraphElement> elements) {
		super(controller);
		this.view = view;
		this.elements = elements;
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
			link=null;
			linkNode.setProperty(LinkNodeProperties.POSITION, oldPositions.get(0));
			if (linkNode instanceof Connector)
				((Connector) linkNode).setRelativePositions((Point2D)linkNode.getProperty(LinkNodeProperties.POSITION));
			painter.formShape();
			linkNode=null;
		}
		else if (segmentNodes != null){
			for (int i = 0; i < segmentNodes.length; i++ ){
				segmentNodes[i].setProperty(LinkNodeProperties.POSITION, oldPositions.get(i));
				nodePainters[i].formShape();
			}
			link = null;
			segmentNodes = null;
			nodePainters = null;
		}
		else{
			// vrati elemente na pocetnu poziciju
			for (GraphElement graphElement : elements) {
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
					Point2D position = (Point2D) linkNode.getProperty(LinkNodeProperties.POSITION);
					testPoint.setLocation(position.getX()+deltaX,position.getY()+deltaY);
					if (connectorCanBeMoved((Connector)linkNode, (LinkNodePainter) painter)){		
						Link.moveNode(linkNode, deltaX, deltaY);
						painter.moveShape(deltaX, deltaY);
						((Connector) linkNode).setRelativePositions((Point2D) linkNode.getProperty(LinkNodeProperties.POSITION));
					}
				}
				else{
					Link.moveNode(linkNode, deltaX, deltaY);
					painter.moveShape(deltaX, deltaY);
				}
			}
			else if (segmentNodes != null){
				boolean move = true;
				Point2D position;
				if (segmentNodes[0] instanceof Connector){
					position = (Point2D) segmentNodes[0].getProperty(LinkNodeProperties.POSITION);
					testPoint.setLocation(position.getX()+deltaX,position.getY()+deltaY);
					move = connectorCanBeMoved((Connector)segmentNodes[0], nodePainters[0]);
				}
				if (move && segmentNodes[1] instanceof Connector){
					position = (Point2D) segmentNodes[1].getProperty(LinkNodeProperties.POSITION);
					testPoint.setLocation(position.getX()+deltaX,position.getY()+deltaY);
					move = connectorCanBeMoved((Connector)segmentNodes[1], nodePainters[1]);
				}
				if (move){
					for (int i = 0; i < segmentNodes.length; i++){
						nodePainters[i].moveShape(deltaX, deltaY);
						Link.moveNode(segmentNodes[i], deltaX, deltaY);
						if (segmentNodes[i] instanceof Connector)
							((Connector) segmentNodes[i]).setRelativePositions((Point2D) segmentNodes[i].getProperty(LinkNodeProperties.POSITION));
					}
				}
			}

			else{
				for (GraphElement graphElement : elements) {
					if (SwingUtilities.isLeftMouseButton(e))
						painter = view.getElementPainter(graphElement);
					else
						painter = view.getShadowElementPainter(graphElement);


					painter.moveShape(deltaX, deltaY);
					if (SwingUtilities.isLeftMouseButton(e)){
						if (graphElement instanceof LinkableElement) {
							linkableElement = (LinkableElement) graphElement;
							linkableElement.moveAllConnectors(deltaX, deltaY);
						}
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

		startXPos = oldXPos = e.getX();
		startYPos = oldYPos = e.getY();

		if (SwingUtilities.isLeftMouseButton(e)){
			if ( view!=null && view.getSelectionModel().getSelectedNode()!=null){
				link=view.getSelectionModel().getSelectedLink();
				painter=view.getLinkNodePainter(view.getSelectionModel().getSelectedNode());
				linkNode=view.getSelectionModel().getSelectedNode();
				if (linkNode instanceof Connector){
					setElementShape((Connector)linkNode);
					connectorShape=new Rectangle2D.Double();
					testPoint=new Point2D.Double();
				}
				oldPositions = new ArrayList<Point2D>();
				Point position = new Point();
				position.setLocation((Point) linkNode.getProperty(LinkNodeProperties.POSITION));
				oldPositions.add(position);
			}
			else if (view != null && view.getSelectionModel().getSelectedLink() != null){
				link=view.getSelectionModel().getSelectedLink();
				LinkPainter painter = view.getLinkPainter(link);
				segmentNodes = painter.selectedSegmentNodes(e.getPoint());
				nodePainters = new LinkNodePainter[2];
				nodePainters[0] = view.getLinkNodePainter(segmentNodes[0]);
				nodePainters[1] = view.getLinkNodePainter(segmentNodes[1]);
				oldPositions = new ArrayList<Point2D>();
				Point position;
				for (LinkNode node : segmentNodes){
					position = new Point();
					position.setLocation((Point) node.getProperty(LinkNodeProperties.POSITION));
					oldPositions.add(position);
				}
				testPoint=new Point2D.Double();
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

		if (!isRightMove()){
			Command command = null;
			// konacno pomeranje

			//check if left or right button - cancel or execute action
			int deltaX;
			int deltaY;
			if (SwingUtilities.isRightMouseButton(e)){
				cancelMove();
				return;
			}
			deltaX = e.getX() - startXPos;
			deltaY = e.getY() - startYPos;


			if (linkNode != null){
				Point2D newPosition = new Point();
				newPosition.setLocation((Point2D) linkNode.getProperty(LinkNodeProperties.POSITION));
				List<LinkNode> nodes = new ArrayList<LinkNode>();
				nodes.add(linkNode);
				List<Point2D> newPositions = new ArrayList<Point2D>();
				newPositions.add(newPosition);
				command=new MoveLinkNodeCommand(view, nodes, link, newPositions, oldPositions, strategy);
				link=null;
				linkNode=null;
			}
			else if (link != null){
				List<LinkNode> nodes = new ArrayList<LinkNode>();
				List<Point2D> newPositions = new ArrayList<Point2D>();
				Point2D position;
				for (LinkNode node : segmentNodes){
					position = new Point();
					position.setLocation((Point2D) node.getProperty(LinkNodeProperties.POSITION));
					newPositions.add(position);
					nodes.add(node);
				}
				command=new MoveLinkNodeCommand(view, nodes, link, newPositions, oldPositions, strategy);
				link = null;
				segmentNodes = null;
				nodePainters = null;
			}
			else {
				// vrati konektore na pocetnu poziciju
				for (GraphElement graphElement : elements) {
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
		else if (SwingUtilities.isRightMouseButton(e)){
			view.getShadowPainters().clear();
			view.repaint();
			MoveElementPopup popup = MainFrame.getInstance().getMoveElementPopup();
			popup.customizeShortcut();
			popup.show(e.getComponent(), e.getX(), e.getY());
			switchToDefaultState();
		}

	}


	@Override
	public boolean isAutoScrollOnDragEnabled() {
		return true;
	}

	@Override
	public boolean isAutoScrollOnMoveEnabled() {
		return false;
	}

	private boolean connectorCanBeMoved(Connector connector, LinkNodePainter painter){
		double dim=painter.getDim();
		if (connectorShape == null)
			connectorShape=new Rectangle2D.Double();
		connectorShape.setRect(testPoint.getX() - dim/2, testPoint.getY()- dim/2, dim, dim);
		setElementShape(connector);
		return elementShape.contains(connectorShape);
	}
	private void setElementShape(Connector connector){
		GraphElement element=model.getFromElementByConnectorStructure(connector);
		Point2D position = (Point2D) element.getProperty(GraphElementProperties.POSITION);
		Dimension2D size = (Dimension2D) element.getProperty(GraphElementProperties.SIZE);
		if (elementShape == null)
			elementShape = new Rectangle2D.Double();
		((Rectangle2D) elementShape).setRect(
				position.getX() - size.getWidth()/2, 
				position.getY() - size.getHeight()/2, 
				size.getWidth(), 
				size.getHeight());
	}

	public List<GraphElement> getElements() {
		return elements;
	}

	public void setElements(List<GraphElement> elements) {
		this.elements = elements;
	}

	public boolean isRightMove() {
		return rightMove;
	}

	public void setRightMove(boolean rightMove) {
		this.rightMove = rightMove;
	}

}