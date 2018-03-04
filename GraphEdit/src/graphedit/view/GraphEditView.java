package graphedit.view;

import static graphedit.concurrency.AutoScrollTask.AUTOSCROLL_THRESHOLD;
import static graphedit.concurrency.AutoScrollTask.DOWN;
import static graphedit.concurrency.AutoScrollTask.LEFT;
import static graphedit.concurrency.AutoScrollTask.RIGHT;
import static graphedit.concurrency.AutoScrollTask.UP;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import graphedit.actions.popup.PopupListener;
import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.app.MainFrame.EventSource;
import graphedit.app.MainFrame.ToolSelected;
import graphedit.command.CutElementsCommand;
import graphedit.command.CutLinkCommand;
import graphedit.concurrency.AutoScrollTask;
import graphedit.layout.LayoutStrategy;
import graphedit.layout.Layouter;
import graphedit.layout.LayouterException;
import graphedit.layout.LayouterFactory;
import graphedit.layout.random.RandomLayouter;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.components.LinkableElement;
import graphedit.model.components.Package;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.enums.ClassStereotypeUI;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.state.SelectionState;
import graphedit.state.State;

@SuppressWarnings("unused")
public class GraphEditView extends GraphEditViewPanel implements View {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Rectangle lassoRectangle;
	private Rectangle lassoZoomRectangle;
	private List<Line2D.Double> linkLines;
	private List<ElementPainter> elementPainters;
	private List<ElementPainter> shadowPainters;

	private Link selectedLink;
	private List<LinkNodePainter> linkNodePainters;
	protected List<LinkPainter> linkPainters; 
	private static final double TRANSLATION_STEP = 12;
	private boolean scaleMini;

	private SelectionModel selectionModel;
	private State currentState;
	private ToolSelected toolSelected;
	private Point2D position;

	private boolean layout = true;
	private LayoutStrategy layoutStrategy;
	private boolean first = true;

	protected boolean showGrid;

	private GraphEditViewMini miniView;

	private boolean disablCenterZoom;

	public GraphEditView(GraphEditModel model) {
		this.model = model;
		model.addObserver(this);

		elementPainters = new ArrayList<ElementPainter>();
		linkPainters = new ArrayList<LinkPainter>();
		linkLines = new ArrayList<Line2D.Double>();
		linkNodePainters = new ArrayList<LinkNodePainter>();
		shadowPainters = new ArrayList<ElementPainter>();

		GraphEditController controller = new GraphEditController();
		addMouseListener(controller);
		addKeyListener(controller);
		addMouseMotionListener(controller);
		addMouseWheelListener(controller);

		lassoRectangle = new Rectangle();
		lassoZoomRectangle = new Rectangle();

		// Popup Event
		addMouseListener(new PopupListener(EventSource.GRAPHICAL_VIEW));

		selectionModel = new SelectionModel(model);

		setFocusable(true);

		toolSelected = ToolSelected.SELECTION;
		MainFrame.getInstance().setStatusTrack(model.getSelectionState().toString());
		showGrid = true;

		miniView = new GraphEditViewMini(this);
		addComponentListener(new ComponentAdapter(){

			public void componentResized(ComponentEvent e) {
				miniView.scaleViewSize();
			}
		});

		layoutStrategy = model.getParentPackage().getLayoutStrategy();

	}

	/**
	 * Generate package painters
	 * Shows packages created outside of diagram (Popup menu, Tool bar...)
	 */
	public void generatePackagePainters(){
		if (model.getDiagramElements().size() + model.getGraphEditPackages().size() > elementPainters.size()){
			ElementPainter painter;
			for (Package pack : model.getContainedPackages()){
				painter = new PackagePainter(pack);
				addElementPainter(painter);
			}
		}
	}

	public void update(Observable o, Object arg) {
		repaint();
		scaleMini = true;
	}


	@Override
	protected void paintOurView(Graphics g, boolean includeTransform){
		

		Graphics2D g2 = (Graphics2D)g;

		// UkljuÄ�ujemo omekÅ¡avanje ivica (antialiasing)
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if(includeTransform){
			// popuni belu pozadinu
			fillBackground(g);
			// Skrolovanje
			g2.transform(transformation);
			//mreza
			if (showGrid) {
				drawGrid(g2);
			}
		}		

		g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		Link link;
		GraphElement element;

		if (!layout && !model.isLayout()){


			for (LinkPainter lPainter : linkPainters) {

				//only paint links not including parent-child panels if in persistent mode
				if (MainFrame.getInstance().getAppMode().equals(ApplicationMode.USER_INTERFACE_PERSISTENT)){
					link = lPainter.getLink();
					element = model.getElementByConnector().get(link.getSourceConnector());
					if (element.getProperty(GraphElementProperties.STEREOTYPE).equals(ClassStereotypeUI.PARENT_CHILD.toString()))
						continue;
					element = model.getElementByConnector().get(link.getDestinationConnector());
					if (element.getProperty(GraphElementProperties.STEREOTYPE).equals(ClassStereotypeUI.PARENT_CHILD.toString()))
						continue;
				}
				lPainter.setShape();
				lPainter.paint(g2);
			}


		}
		
		for (ElementPainter painter : (ArrayList<ElementPainter>) elementPainters) {
			element = painter.getElement();
			if (MainFrame.getInstance().getAppMode().equals(ApplicationMode.USER_INTERFACE_PERSISTENT)){
				
				if (!element.getProperty(GraphElementProperties.STEREOTYPE).equals(ClassStereotypeUI.PARENT_CHILD.toString()))
					painter.paint(g2);
			}
			else
				painter.paint(g2);
		}

		

		if (layout || model.isLayout()){
			Layouter layouter = LayouterFactory.createLayouter(layoutStrategy, 
					this, g);

			try {
				layouter.layout();
			} catch (LayouterException e) {
				System.out.println(e.getMessage());
				if (first){
					layouter = new RandomLayouter(this);
					try {
						layouter.layout();
					} catch (LayouterException e1) {
						e1.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(MainFrame.getInstance(), "Selected algorithm cannot be applied to current diagram");
				}
			}

			for (LinkPainter lPainter : linkPainters) {

				//only paint links not including parent-child panels if in persistent mode
				if (MainFrame.getInstance().getAppMode().equals(ApplicationMode.USER_INTERFACE_PERSISTENT)){
					link = lPainter.getLink();
					element = model.getElementByConnector().get(link.getSourceConnector());
					if (element.getProperty(GraphElementProperties.STEREOTYPE).equals(ClassStereotypeUI.PARENT_CHILD.toString()))
						continue;
					element = model.getElementByConnector().get(link.getDestinationConnector());
					if (element.getProperty(GraphElementProperties.STEREOTYPE).equals(ClassStereotypeUI.PARENT_CHILD.toString()))
						continue;
				}
				lPainter.setShape();
				lPainter.paint(g2);
			}
		}

		first = false;
		
		for (ElementPainter painter : (ArrayList<ElementPainter>) elementPainters) {
			if (MainFrame.getInstance().getAppMode().equals(ApplicationMode.USER_INTERFACE_PERSISTENT)){
				element = painter.getElement();
				if (!element.getProperty(GraphElementProperties.STEREOTYPE).equals(ClassStereotypeUI.PARENT_CHILD.toString()))
					painter.paint(g2);
			}
			else
				painter.paint(g2);
		}
		


		//if (selectionModel.getSelectedLink()!=selectedLink || (selectedLink==null && linkNodePainters.size()!=0)){
		//linkNodePainters.clear();
		selectedLink = selectionModel.getSelectedLink();
		if (selectedLink==null)
			linkNodePainters.clear();
			

		if (selectedLink != null) {
			setLinkNodePainters(selectedLink);
			for (LinkNodePainter lnp:linkNodePainters) 
				lnp.paint(g2);
		}


		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, 
				new float[] {2, 2}, 0));

		Iterator<GraphElement> iter = selectionModel.getIteratorSelectedElements();
		while (iter.hasNext()) {
			GraphElement selectedElem = iter.next();

			if (getElementPainter(selectedElem) == null || getElementPainter(selectedElem).getShape() == null)
				continue;

			Shape shape = this.getElementPainter(selectedElem).getShape();

			position = shape.getBounds().getLocation();
			position.setLocation(position.getX() - 1, position.getY() - 1);
			Dimension2D size = shape.getBounds().getSize();
			size.setSize(size.getWidth() + 1, size.getHeight() + 1);


			//okvir
			g2.draw(new Rectangle2D.Double(position.getX(),position.getY(), 
					size.getWidth(), size.getHeight()));
			//levo-gore
			g2.fill(new Rectangle2D.Double(position.getX(), position.getY(), 5, 5));
			//levo-sredina
			g2.fill(new Rectangle2D.Double(position.getX(), position.getY() + size.getHeight()/2 - 2, 5, 5));
			//levo-dole
			g2.fill(new Rectangle2D.Double(position.getX(), position.getY() + size.getHeight()-4, 5, 5));
			//sredina-gore
			g2.fill(new Rectangle2D.Double(position.getX() + size.getWidth()/2 - 2, position.getY(), 5, 5));
			//sredina-dole
			g2.fill(new Rectangle2D.Double(position.getX() + size.getWidth()/2 - 2, position.getY() + size.getHeight() - 4, 5, 5));
			//desno-gore
			g2.fill(new Rectangle2D.Double(position.getX() + size.getWidth() - 4, position.getY(), 5, 5));
			//desno-sredina
			g2.fill(new Rectangle2D.Double(position.getX() + size.getWidth() - 4, position.getY() + size.getHeight()/2 - 2, 5, 5));
			//desno-dole
			g2.fill(new Rectangle2D.Double(position.getX() + size.getWidth() - 4, position.getY() + size.getHeight() - 4, 5, 5));
		}

		// proveri da li ima potrebe za iscrtavanjem lasso-a
		if (lassoRectangle.getWidth() > 0 && lassoRectangle.getHeight() > 0) {
			g2.draw(lassoRectangle);
			g2.setColor(new Color(187, 235, 255, 50));
			g2.fill(lassoRectangle);
			lassoRectangle.setBounds(0, 0, 0, 0);

		}

		if (!lassoZoomRectangle.isEmpty()) {
			g2.draw(lassoZoomRectangle);
			g2.setColor(new Color(160, 230, 160, 50));
			g2.fill(lassoZoomRectangle);
			lassoZoomRectangle.setBounds(0, 0, 0, 0);
		}

		if (linkLines.size()>0)
			for (Line2D.Double line:linkLines)
				g2.draw(line);

		g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (ElementPainter painter: shadowPainters){
			painter.paint(g2);
		}


		if (scaleMini){
			miniView.bestFitZoom(false);

			scaleMini = false;
		}
		miniView.repaint();
		
		if (layout){
			layout = false;
			model.setLayout(false);
			repaint();
		}
	}

	public void setLinkNodePainters(Link link){
		linkNodePainters.clear();
		if (link != null) 
			for (LinkNode ln : link.getNodes()) {
				LinkNodePainter lnp = new LinkNodePainter(ln);
				linkNodePainters.add(lnp);
			}
	}
	protected void drawGrid(Graphics2D g2) {
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.LIGHT_GRAY);

		Point2D start = new Point2D.Double(0, 0);
		Point2D end = new Point2D.Double(getWidth(), getHeight());
		double step = GRID_WIDTH;

		transformToUserSpace(start);
		transformToUserSpace(end);

		start.setLocation(
				start.getX() > 0 ?
						start.getX() - Math.abs(start.getX() % step) :
							start.getX() - step + Math.abs(start.getX() % step),
							start.getY() > 0 ?
									start.getY() - Math.abs(start.getY() % step) :
										start.getY() - step + Math.abs(start.getY() % step));

		end.setLocation(
				end.getX() > 0 ? 
						end.getX() + step - Math.abs(end.getX() % step) :
							end.getX() + Math.abs(end.getX() % step),
							end.getY() > 0 ?
									end.getY() + step - Math.abs(end.getY() % step) :
										end.getY() + Math.abs(end.getY() % step));


		for (int x = (int) Math.round(start.getX()); x < end.getX(); x += step) {
			g2.drawLine(x, (int) Math.round(start.getY()), x, (int) Math.round(end.getY()));
		}


		for (int y = (int) Math.round(start.getY()); y < end.getY(); y += step) {
			g2.drawLine((int) Math.round(start.getX()), y, (int) Math.round(end.getX()), y);
		}
	}

	protected void fillBackground(Graphics g) {
		Object obj = MainFrame.getInstance().getMainTabbedPane().getSelectedComponent();
		// Do Not Draw A Rectangle If Table Is Selected
		if (obj instanceof JPanel) {
			int width = ((JPanel) obj).getWidth();
			int height = ((JPanel) obj).getHeight();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, width, height);
		}
	}

	public List<ElementPainter> getElementPainters() {
		if (elementPainters == null)
			elementPainters = new ArrayList<ElementPainter>();
		return elementPainters;
	}

	public List<ElementPainter> getElementPainters(List<GraphElement> elements) {
		List<ElementPainter> elementPainters = new ArrayList<ElementPainter>();
		for (GraphElement element : elements) 
			elementPainters.add(getElementPainter(element));
		return elementPainters;
	}

	public Iterator<ElementPainter> getIteratorElementPainters() {
		if (elementPainters == null)
			elementPainters = new ArrayList<ElementPainter>();
		return elementPainters.iterator();
	}

	public void setElementPainters(List<ElementPainter> newElementPainters) {
		removeAllElementPainters();
		for (Iterator<ElementPainter> iter = newElementPainters.iterator(); iter
				.hasNext();)
			addElementPainter(iter.next());
	}

	public void addElementPainter(ElementPainter newElementPainter) {
		if (newElementPainter == null)
			return;
		if (this.elementPainters == null)
			this.elementPainters = new ArrayList<ElementPainter>();
		if (!this.elementPainters.contains(newElementPainter))
			this.elementPainters.add(newElementPainter);
	}

	public void addElementPainters(List<ElementPainter> elementPainters) {
		this.elementPainters.addAll(elementPainters);
	}

	public List<ElementPainter> addElementPaintersDeepCopy(List<ElementPainter> painters) throws CloneNotSupportedException {
		List<ElementPainter> addedElementPainters = new ArrayList<ElementPainter>();
		ElementPainter clonedElementPainter = null;
		GraphElement clonedElement = null;
		for (ElementPainter elementPainter : painters) {
			clonedElementPainter = (ElementPainter)elementPainter.clone();
			clonedElement = (GraphElement)elementPainter.getElement().clone();
			clonedElementPainter.setElement(clonedElement);
			elementPainters.add(clonedElementPainter);
			addedElementPainters.add(clonedElementPainter);
		}
		return addedElementPainters;
	}

	public void removeElementPainter (ElementPainter painter){
		if (painter == null)
			return;
		if (elementPainters.contains(painter))
			elementPainters.remove(painter);
	}

	public void removeElementPainters(List<ElementPainter> elementPainters) {
		this.elementPainters.removeAll(elementPainters);
	}

	public void removeLinkPainters(List<LinkPainter> linkPainters) {
		this.linkPainters.removeAll(linkPainters);
	}

	public void addLinkPainters(List<LinkPainter> linkPainters) {
		this.linkPainters.addAll(linkPainters);
	}

	public void addLinkLine(Point startingPoint, Point endingPoint){
		Line2D.Double line=new Line2D.Double();
		if (startingPoint!=null && endingPoint!=null){
			line.setLine(startingPoint,endingPoint);
			linkLines.add(line);
		}

	}
	public void temporarelyAddLinkLine(Point startingPoint, Point endingPoint){
		if (linkLines.size()>0)
			linkLines.remove(linkLines.size()-1);
		addLinkLine(startingPoint, endingPoint);
	}

	public void clearLinkLines(){
		linkLines.removeAll(linkLines);
	}

	public void removeAllElementPainters() {
		if (elementPainters != null)
			elementPainters.clear();
	}

	//sve sto postoji za ElementPainter dodato za linkPainter
	public List<LinkPainter> getLinkPainters() {
		if (linkPainters == null)
			linkPainters = new ArrayList<LinkPainter>();
		return linkPainters;
	}

	public Iterator<LinkPainter> getIteratorLinkPainters() {
		if (linkPainters == null)
			linkPainters = new ArrayList<LinkPainter>();
		return linkPainters.iterator();
	}

	public void setLinkPainters(List<LinkPainter> newLinkPainters) {
		removeAllLinkPainters();
		for (Iterator<LinkPainter> iter = newLinkPainters.iterator(); iter
				.hasNext();)
			addLinkPainter(iter.next());
	}

	public void addLinkPainter(LinkPainter newLinkPainter) {
		if (newLinkPainter == null)
			return;
		if (this.linkPainters == null)
			this.linkPainters = new ArrayList<LinkPainter>();
		if (!this.linkPainters.contains(newLinkPainter))
			this.linkPainters.add(newLinkPainter);
	}

	public void removeLinkPainters(LinkPainter oldLinkPainter) {
		if (oldLinkPainter == null)
			return;
		if (this.linkPainters != null)
			if (this.linkPainters.contains(oldLinkPainter))
				this.linkPainters.remove(oldLinkPainter);
	}

	public void removeAllLinkPainters() {
		if (linkPainters != null)
			linkPainters.clear();
	}


	public GraphEditModel getModel() {
		return model;
	}

	public void setModel(GraphEditModel newGraphEditModel) {
		this.model = newGraphEditModel;
	}

	public SelectionModel getSelectionModel() {
		return selectionModel;
	}

	public void setSelectionModel(SelectionModel newSelectionModel) {
		this.selectionModel = newSelectionModel;
	}

	public Link getLinkAtPosition(Point2D position){
		if (linkPainters != null) {
			for (int i=linkPainters.size()-1; i >=0; i--) 
				if ((linkPainters.get(i)).elementAt(position))
					return (linkPainters.get(i)).getLink();
		}
		return null;

	}
	public GraphElement getElementAtPosition(Point2D position) {
		// selektuje posledje-dodatog sa iste pozicije, oponasa Z-order
		if (elementPainters != null) {
			for (int i=elementPainters.size()-1; i >=0; i--) 
				if ((elementPainters.get(i)).elementAt(position))
					return (elementPainters.get(i)).getElement();
		}
		return null;
	}

	//gleda da li se na datoj poziciji nalazi element koji se moze povezati
	public LinkableElement getLinkableElementAtPosition(Point2D position) {
		try{
			LinkableElement elem = (LinkableElement) getElementAtPosition(position);
			return elem;
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	public LinkNode getLinkNodeAtPosition(Point2D position){
		if (linkNodePainters!=null){
			for (int i=linkNodePainters.size()-1; i >=0; i--) 
				if ((linkNodePainters.get(i)).elementAt(position)){
					return (LinkNode)linkNodePainters.get(i).getElement();
				}
		}
		return null;	
	}

	public void selectLink (Link link){
		linkNodePainters.clear();
		setLinkNodePainters(link);
	}

	//Mora se ovako nesto uraditi, da ne bi moralo da se prvo selektuje veza, pa tek onda linkNode
	public LinkNode getSelectedLinkNode(Link link, Point2D position){
		selectLink(link);
		return getLinkNodeAtPosition(position);
	}
	public void addToLassoSelection(int oldX, int oldY, int currentX, int currentY) {
		setLassoRectangle(oldX, oldY, currentX, currentY);
		for (ElementPainter painter : (ArrayList<ElementPainter>)elementPainters) {
			if (painter.elementIn(lassoRectangle)) {
				if (!getSelectionModel().getSelectedElements().contains(painter.getElement())) {
					getSelectionModel().addSelectedElement(painter.getElement());
				} else {
					getSelectionModel().removeSelectedElement(painter.getElement());
				}
			}
		}
		lassoRectangle.setBounds(0, 0, 0, 0);
	}

	public void setLassoRectangle(int oldX, int oldY, int currentX, int currentY) {
		int rectX, rectY, width, height;
		// omogucava lasso po sva cetiri kvadranta
		width = Math.abs(currentX - oldX);
		height = Math.abs(currentY - oldY);
		rectX = currentX < oldX ? currentX : oldX;
		rectY = currentY < oldY ? currentY : oldY;			
		lassoRectangle.setBounds(rectX, rectY, width, height);
	}

	public void setLassoZoomRectangle(int oldX, int oldY, int currentX, int currentY) {
		int rectX, rectY, width, height;
		// omogucava lasso po sva cetiri kvadranta
		width = Math.abs(currentX - oldX);
		height = Math.abs(currentY - oldY);
		rectX = currentX < oldX ? currentX : oldX;
		rectY = currentY < oldY ? currentY : oldY;			
		lassoZoomRectangle.setBounds(rectX, rectY, width, height);
	}

	public void selectAll() {
		selectionModel.selectAllElements();
	}

	public void selectInverse() {
		selectionModel.inverseSelection();
	}

	public State getCurrentState() {
		return currentState;
	}

	public GraphEditView getView() {
		return this;
	}

	public ToolSelected getSelectedTool() {
		return toolSelected;
	}

	public void setSelectedTool(ToolSelected toolSelected) {
		this.toolSelected = toolSelected;
	}

	public ElementPainter removeElementPainter(GraphElement element) {
		for (ElementPainter painter : (ArrayList<ElementPainter>)elementPainters)
			if (painter.getElement() == element) {
				elementPainters.remove(painter);
				return painter;
			}
		return null;
	}

	public ElementPainter getElementPainter(GraphElement element){
		for(ElementPainter painter : elementPainters){
			if(painter.getElement() == element){
				return painter;
			}
		}
		return null;
	}

	public ElementPainter getShadowElementPainter(GraphElement element){
		for(ElementPainter painter : shadowPainters){
			if(painter.getElement() == element){
				return painter;
			}
		}
		return null;
	}

	public LinkPainter getLinkPainter(Link link) {
		for (LinkPainter linkPainter : linkPainters) {
			if (linkPainter.getLink() == link) {
				return linkPainter;
			}
		}
		return null;
	}

	private MouseEvent makeUserSpaceMouseEvent(MouseEvent e) {
		Point2D point = e.getPoint();
		transformToUserSpace(point);
		return new MouseEvent(
				e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), 
				(int)point.getX(), (int)point.getY(), 
				e.getXOnScreen(), e.getYOnScreen(), 
				e.getClickCount(), e.isPopupTrigger(), e.getButton()
				);
	}

	private MouseEvent makeUserSpaceMouseWheelEvent(MouseWheelEvent e) {
		Point2D point = e.getPoint();
		transformToUserSpace(point);
		return new MouseWheelEvent(
				e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), 
				(int)point.getX(), (int)point.getY(), 
				e.getXOnScreen(), e.getYOnScreen(), 
				e.getClickCount(), e.isPopupTrigger(),
				e.getScrollType(), e.getScrollAmount(), e.getWheelRotation()
				);
	}




	public void zoom(double scale) {
		if (!disablCenterZoom)
			zoomAtPoint(scale, getCenterPoint());
		else
			disablCenterZoom = false;
	}

	public void zoomAtPoint(double scale, Point2D position) {


		scale = limitScaleFactor(scale);


		Point2D oldPosition = new Point2D.Double(position.getX(), position.getY());
		Point2D origPosition = new Point2D.Double(position.getX(), position.getY());


		transformToUserSpace(oldPosition);

		transformation.setToScale(scale, scale);

		Point2D newPosition = new Point2D.Double(position.getX(), position.getY());
		transformToUserSpace(newPosition);


		double tx = newPosition.getX() - oldPosition.getX();
		double ty = newPosition.getY() - oldPosition.getY();

		transformation.translate(tx, ty);

		disablCenterZoom = true;
		MainFrame.getInstance().updateZoomSlider(scale);
		repaint();

		miniView.scaleViewSize();
	}

	public void zoomToPoint(Point2D position){
		transformFromUserSpace(position);
		zoomToPoint(1, position, false);
	}
	
	@Override
	public void zoomToPoint(double scale, Point2D position, boolean limistScale) {
		super.zoomToPoint(scale, position, limistScale);
		miniView.scaleViewSize();
		MainFrame.getInstance().updateZoomSlider(scale);
	}

	public void lassoZoom() {
		if (lassoZoomRectangle.isEmpty()) {
			return;
		}
		Point2D center = new Point2D.Double(lassoZoomRectangle.getCenterX(), lassoZoomRectangle.getCenterY());
		double scale;
		if (lassoZoomRectangle.getWidth() > lassoZoomRectangle.getHeight()) {
			scale = getWidth() / lassoZoomRectangle.getWidth();
		} else {
			scale = getHeight() / lassoZoomRectangle.getHeight();
		}

		lassoZoomRectangle.setBounds(0, 0, 0, 0);
		transformFromUserSpace(center);
		zoomToPoint(scale, center, true);
	}



	public void toggleGrid() {
		showGrid = !showGrid;
		repaint();
	}

	public void scrollToPoint(Point target){
		Point current = new Point(0,0);
		transformToUserSpace(current);
		double scrollX = current.getX() - target.getX();
		double scrollY = current.getY() - target.getY();
		transformation.translate(scrollX, scrollY);
		repaint();
	}

	public void scrollBy(int deltaX, int deltaY){
		transformation.translate(deltaX, deltaY);
		repaint();
	}


	public void scrollRight(){
		transformation.translate(-TRANSLATION_STEP / transformation.getScaleX(), 0);
		miniView.scroll();
		repaint();
	}


	public void scrollLeft(){
		transformation.translate(TRANSLATION_STEP / transformation.getScaleX(), 0);
		miniView.scroll();
		repaint();
	}

	public void scrollUp(){
		transformation.translate(0, TRANSLATION_STEP / transformation.getScaleY());
		miniView.scroll();
		repaint();
	}

	public void scrollDown(){
		transformation.translate(0, -TRANSLATION_STEP / transformation.getScaleY());
		miniView.scroll();
		repaint();
	}

	public class GraphEditController implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {



		private GraphElement currentElement;
		private Link currentLink;
		private LinkNode currentLinkNode;

		//za povezivanje
		private LinkableElement mousePressedElement;
		private LinkableElement mouseReleasedElement;

		private Point point, mouseReleased;
		private MouseEvent lastMouseEvent;

		private AutoScrollTask autoScrollTask;

		public GraphEditController() {
			currentState = new SelectionState();
			currentState.setController(this);
			currentState.setView(getView());		

			autoScrollTask = new AutoScrollTask(GraphEditView.this, this);
		}

		public Point getPoint() {
			return point;
		}

		public Point getMouseReleased(){
			return mouseReleased;
		}

		public GraphElement getCurrentElement() {
			return currentElement;
		}

		public void setCurrentElement(GraphElement currentElement) {
			this.currentElement = currentElement;
		}

		public void setCurrentState(State currState) {
			currentState = currState;
		}



		@Override
		public void mousePressed(MouseEvent e) {
			getView().requestFocusInWindow();
			point = e.getPoint();
			transformToUserSpace(point);
			currentElement = getElementAtPosition(point);
			currentLink = getLinkAtPosition(point);
			currentLinkNode=getLinkNodeAtPosition(point);
			mousePressedElement=getLinkableElementAtPosition(point);
			if (e.getButton() == MouseEvent.BUTTON1) {

				lastMouseEvent = e;
			}

			currentState.mousePressed(makeUserSpaceMouseEvent(e));
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mouseReleased = e.getPoint();
			MouseEvent event = makeUserSpaceMouseEvent(e);
			if (event.getButton() == MouseEvent.BUTTON1) 
				mouseReleasedElement = getLinkableElementAtPosition(event.getPoint()); 
			currentState.mouseReleased(event);

			lastMouseEvent = null;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			currentState.mouseClicked(makeUserSpaceMouseEvent(e));

			lastMouseEvent = e;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			currentState.mouseMoved(makeUserSpaceMouseEvent(e));

			lastMouseEvent = e;
			if (currentState.isAutoScrollOnMoveEnabled()) {
				maybeScroll();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			currentState.mouseDragged(makeUserSpaceMouseEvent(e));

			lastMouseEvent = e;
			if (currentState.isAutoScrollOnDragEnabled()) {
				maybeScroll();
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			handleMouseWheelEvent(e);
			currentState.mouseWheelMoved(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// ovaj dogadjaj se hvata u bilo kom stanju
			switch (e.getKeyCode()) {
			case KeyEvent.VK_DELETE:
				if (getView().getSelectionModel().getSelectedElements().size() > 0)
					getModel().getCommandManager().executeCommand(new CutElementsCommand(
							getView(), getView().getSelectionModel().getSelectedElements(), 
							getView().getElementPainters(getView().getSelectionModel().getSelectedElements())));
				else if (getView().getSelectionModel().getSelectedLink() instanceof Link) {
					// here goes command for link removal
					getModel().getCommandManager().executeCommand(new CutLinkCommand(getView(), getView().getSelectionModel().getSelectedLink()));
				}
				break;
			case KeyEvent.VK_UP:
				scroll(UP, false);
				break;
			case KeyEvent.VK_DOWN:
				scroll(DOWN, false);
				break;
			case KeyEvent.VK_LEFT:
				scroll(LEFT, false);
				break;
			case KeyEvent.VK_RIGHT:
				scroll(RIGHT, false);
				break;
			}
			currentState.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			currentState.keyReleased(e);
		}

		@Override
		public void keyTyped(KeyEvent e) {
			currentState.keyTyped(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			currentState.mouseEntered(makeUserSpaceMouseEvent(e));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			currentState.mouseExited(makeUserSpaceMouseEvent(e));

		}

		public LinkableElement getMousePressedElement() {
			return mousePressedElement;
		}

		public LinkableElement getMouseReleasedElement() {
			return mouseReleasedElement;
		}

		public Link getCurrentLink() {
			return currentLink;
		}
		public LinkNode getCurrentLinkNode() {
			return currentLinkNode;
		}

		private void handleMouseWheelEvent(MouseWheelEvent e) {
			if (e.isControlDown()) { // Ako je pritisnut Ctrl, radimo zoom u
				// taÄ�ki
				// Prvo je potrebno da odredimo novo skaliranje
				double newScaling = transformation.getScaleX();
				if (e.getWheelRotation() > 0)
					newScaling *= (double) e.getWheelRotation() * scalingFactor;
				else
					newScaling /= -(double) e.getWheelRotation()
					* scalingFactor;

				zoomAtPoint(newScaling, e.getPoint());

			} else if (e.isShiftDown()) { // ako je pritisnut Shift
				double t = (double) e.getWheelRotation()
						* translationFactor / transformation.getScaleX();

				transformation.translate(t, 0);
				miniView.scroll();
			} else { // u ostalim sluÄ�ajevima vrÅ¡imo skrolovanje po Y osi
				double t = -(double) e.getWheelRotation()
						* translationFactor / transformation.getScaleY();
				transformation.translate(0, t);
				miniView.scroll();
			}

			repaint();
		}

		private int getDirection() {
			if (lastMouseEvent == null) {
				return 0;
			}
			return getDirection(lastMouseEvent.getPoint());
		}

		private int getDirection(Point2D point) {
			int direction = 0;

			Point2D p = new Point2D.Double(point.getX(), point.getY());

			if (p.getX() <= AUTOSCROLL_THRESHOLD) {
				direction |= LEFT;
			}
			if (p.getX() >= getWidth() - AUTOSCROLL_THRESHOLD) {
				direction |= RIGHT;
			}
			if (p.getY() <= AUTOSCROLL_THRESHOLD) {
				direction |= UP;
			}
			if (p.getY() >= getHeight() - AUTOSCROLL_THRESHOLD) {
				direction |= DOWN;
			}

			return direction;
		}

		private void maybeScroll() {
			if (getDirection() != 0) {
				if (!autoScrollTask.isRunning()) {
					new Thread(autoScrollTask).start();
				}
			} else if (autoScrollTask.isRunning()) {
				autoScrollTask.stop();
			}
		}


		public void scroll() {
			scroll(getDirection(), true);
		}

		public void scroll(int direction, boolean isMouseEvent) {
			if (direction == 0 || lastMouseEvent == null) {
				autoScrollTask.stop();
				if (direction == 0)
					return;
			}
			if ((direction & UP) != 0) {
				transformation.translate(0, TRANSLATION_STEP / transformation.getScaleY());
			}
			if ((direction & DOWN) != 0) {
				transformation.translate(0, -TRANSLATION_STEP / transformation.getScaleY());
			}
			if ((direction & LEFT) != 0) {
				transformation.translate(TRANSLATION_STEP / transformation.getScaleX(), 0);
			}
			if ((direction & RIGHT) != 0) {
				transformation.translate(-TRANSLATION_STEP / transformation.getScaleX(), 0);
			}


			miniView.scroll();



			if (isMouseEvent)
				makeMouseEvent();
			repaint();
		}



		private void makeMouseEvent() {
			lastMouseEvent = new MouseEvent(
					lastMouseEvent.getComponent(), lastMouseEvent.getID(), System.currentTimeMillis(), 
					lastMouseEvent.getModifiers(), lastMouseEvent.getX(), lastMouseEvent.getY(), 
					lastMouseEvent.getClickCount(), lastMouseEvent.isPopupTrigger(), lastMouseEvent.getButton()
					);

			currentState.mouseDragged(makeUserSpaceMouseEvent(lastMouseEvent));

		}

	}

	public List<LinkPainter> getLinkPainters(List<Link> links) {
		List<LinkPainter> result = new ArrayList<LinkPainter>();
		for (LinkPainter linkPainter : linkPainters)
			if (links.contains(linkPainter.getLink())) 
				result.add(linkPainter);
		return result;
	}

	public LinkNodePainter getLinkNodePainter(LinkNode ln) {
		for (LinkNodePainter painter: linkNodePainters)
			if (painter.getLinkNode().equals(ln))
				return painter;
		return null;
	}

	public GraphEditViewMini getMiniView() {
		return miniView;
	}

	public boolean isScaleMini() {
		return scaleMini;
	}

	public void setScaleMini(boolean scaleMini) {
		this.scaleMini = scaleMini;
	}

	public List<ElementPainter> getShadowPainters() {
		return shadowPainters;
	}

	public void setShadowPainters(List<ElementPainter> shadowPainters) {
		this.shadowPainters = shadowPainters;
	}

	public boolean isLayout() {
		return layout;
	}

	public void setLayout(boolean layout) {
		this.layout = layout;
	}

	public LayoutStrategy getLayoutStrategy() {
		return layoutStrategy;
	}

	public void setLayoutStrategy(LayoutStrategy layoutStrategy) {
		this.layoutStrategy = layoutStrategy;
	}



}