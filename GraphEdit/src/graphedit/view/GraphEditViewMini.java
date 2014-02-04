package graphedit.view;

import graphedit.model.components.GraphElement;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

public class GraphEditViewMini extends GraphEditViewPanel {

	private static final long serialVersionUID = 1L;
	private GraphEditView view;
	private Rectangle viewRectangle;
	private Point position;

	public GraphEditViewMini (GraphEditView view){
		this.view = view;
		this.model = view.getModel();
		viewRectangle = new Rectangle();

		GraphMiniController controller = new GraphMiniController();
		addMouseListener(controller);
		addMouseMotionListener(controller);
		
		addComponentListener(new ComponentAdapter(){
			
			public void componentResized(ComponentEvent e) {
		        bestFitZoom();   
		        scaleView();
		    }
		});


	}
	


	protected void paintOurView(Graphics g, boolean includeTransform){
		Graphics2D g2 = (Graphics2D)g;
		if (viewRectangle.getSize() == null || viewRectangle.getSize().getWidth() == 0)
			viewRectangle.setSize(view.getSize());


		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(includeTransform){
			fillBackground(g);
			g2.transform(transformation);
		}		

		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		for (LinkPainter lPainter : view.getLinkPainters()) {
			// promena lokacije konektora
			lPainter.setShape();
			lPainter.paint(g2);
		}

		for (ElementPainter painter : (ArrayList<ElementPainter>) view.getElementPainters()) {
			painter.paint(g2);
		}

		Iterator<GraphElement> iter = view.getSelectionModel().getIteratorSelectedElements();
		while (iter.hasNext()) {

			GraphElement selectedElem = iter.next();
			if (view.getElementPainter(selectedElem) == null || view.getElementPainter(selectedElem).getShape() == null)
				continue;

			Shape shape = view.getElementPainter(selectedElem).getShape();

			position = shape.getBounds().getLocation();
			position.setLocation(position.getX() - 1, position.getY() - 1);


		}
		if (model.getDiagramElements().size() > 0){
			g2.setColor(Color.RED);
			g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2.draw(viewRectangle);
		}

	}


	protected void fillBackground(Graphics g) {
		// Do Not Draw A Rectangle If Table Is Selected
		int width = getWidth();
		int height = getHeight();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);
	}



	public void scroll() {
		setViewLocation();
		repaint();
	}


	public void scaleViewSize() {
		scaleView();
		repaint();
	}



	private void scaleView(){
		Dimension rectSize;

		Point2D upLeft = new Point2D.Double(0,0);
		view.transformToUserSpace(upLeft);

		Point2D downRight = new Point2D.Double(view.getSize().getWidth(), view.getSize().getHeight());
		view.transformToUserSpace(downRight);

		//velicina vidljivog dela
		rectSize = new Dimension((int)(downRight.getX() - upLeft.getX()), (int)(downRight.getY() - upLeft.getY()));

		//transformFromUserSpace(upLeft);

		viewRectangle.setLocation((int)upLeft.getX(), (int) upLeft.getY());
		viewRectangle.setSize(rectSize);

	}

	private void setViewLocation(){
		Point2D upLeft = new Point2D.Double(0,0);
		view.transformToUserSpace(upLeft);
		viewRectangle.setLocation((int)upLeft.getX(), (int) upLeft.getY());
	}

	private void setViewLocation(Point newLocation){
		int x = (int) (newLocation.getX() - viewRectangle.getSize().getWidth()/2);
		int y = (int) (newLocation.getY() - viewRectangle.getSize().getHeight()/2);
		viewRectangle.setLocation(x,y);
		repaint();
		view.scrollToPoint(new Point(x,y));
	}

	private void moveView(int offsetX, int offsetY){
		int x = (int) (viewRectangle.getLocation().getX() + offsetX);
		int y = (int) (viewRectangle.getLocation().getY() + offsetY);
		viewRectangle.setLocation(x,y);
		repaint();
		view.scrollBy(-offsetX, -offsetY);
	}



	public GraphEditViewMini getMiniView(){
		return this;
	}

	public class GraphMiniController implements MouseListener, MouseMotionListener{

		private Point point;
		private boolean rectHit;
		private int prevOffsetX, prevOffsetY;


		public GraphMiniController(){
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			if (rectHit){
				Point currentPos = arg0.getPoint();
				transformToUserSpace(currentPos);
				int offsetX = (int) (currentPos.getX() - point.getX());
				int offsetY = (int) (currentPos.getY() - point.getY());
				int deltaX = offsetX - prevOffsetX;
				int deltaY = offsetY - prevOffsetY;
				moveView(deltaX, deltaY);
				prevOffsetX = offsetX;
				prevOffsetY = offsetY;
			}
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			Point point = arg0.getPoint();
			transformToUserSpace(point);
			setViewLocation(point);

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {

		}

		@Override
		public void mouseExited(MouseEvent arg0) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			point = e.getPoint();
			transformToUserSpace(point);
			rectHit = viewRectangle.contains(point);
			if (rectHit){
				prevOffsetX = 0;
				prevOffsetY = 0;
				rectHit = true;
			}
		}


		@Override
		public void mouseReleased(MouseEvent arg0) {

		}

	}

	
	
}
