package graphedit.state;

import graphedit.app.MainFrame;
import graphedit.command.ChangeAssociationPropertiesCommand;
import graphedit.command.ChangeLinkTypeCommand;
import graphedit.command.Command;
import graphedit.gui.dialog.AssociationLinkDialog;
import graphedit.model.components.AggregationLink;
import graphedit.model.components.AssociationLink;
import graphedit.model.components.CompositionLink;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.LinkNode;
import graphedit.model.components.Package;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.view.AggregationLinkPainter;
import graphedit.view.AssociationLinkPainter;
import graphedit.view.CompositionLinkPainter;
import graphedit.view.LinkPainter;

import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;


public class SelectionState extends State {

	public SelectionState() { 
		super();
	}

	@Override
	public void mousePressed(MouseEvent e) {

		Link hitLink = controller.getCurrentLink();
		view.getSelectionModel().setSelectedNode(null);
		GraphElement hitElement = controller.getCurrentElement();
		LinkNode hitNode=controller.getCurrentLinkNode();
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (hitNode!=null) //provera mora pre nego sto se gleda da li je pogodjen element, zbog konektora koji je na elementu
				view.getSelectionModel().setSelectedNode(hitNode);
			else{
				view.getSelectionModel().setSelectedLink(null);
				// vrsi se provera da li je pritom pogodjen element
				if (hitElement != null) {

					// vrsi se provera da li je pritisnut ctrl
					if (e.isControlDown()) {
						// da li je pogodjeni element prethodno bio selektovan
						if (view.getSelectionModel().getSelectedElements().contains(hitElement)) {
							view.getSelectionModel().removeSelectedElement(hitElement);
						} else {
							view.getSelectionModel().addSelectedElement(hitElement);
						}

					} else {
						if (!view.getSelectionModel().getSelectedElements().contains(hitElement)) {
							view.getSelectionModel().removeAllSelectedElements();
							view.getSelectionModel().addSelectedElement(hitElement);
						}
					}
				} else if (hitLink != null){  
					view.getSelectionModel().removeAllSelectedElements();
					view.getSelectionModel().setSelectedLink(hitLink);
					hitNode=view.getSelectedLinkNode(hitLink, e.getPoint()); //kako bi pomeranje moglo odmah poctu u slucaju da se kliknulo direktno na konektor
					view.getSelectionModel().setSelectedNode(hitNode);
				} else {
					// added on 19.12.2011. 
					if (!e.isControlDown()) 
						view.getSelectionModel().removeAllSelectedElements();
				}
			}
		} else if (SwingUtilities.isRightMouseButton(e)) {

			view.getSelectionModel().removeAllSelectedElements();
			if (hitElement != null) {
				view.getSelectionModel().removeAllSelectedElements();
				view.getSelectionModel().addSelectedElement(hitElement);
			}
			showGraphElementPopup(e);

		}
		// azuriraj promene
		view.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		GraphElement hitElement = controller.getCurrentElement();
		LinkNode hitNode=view.getSelectionModel().getSelectedNode();
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (hitNode!=null){ 
				//predji u move state
				State state = MainFrame.getInstance().getCurrentView().getModel().getMoveElementState();
				state.setView(view);
				state.setController(controller);
				state.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				MainFrame.getInstance().setStatusTrack(state.toString());
				controller.setCurrentState(state);
				state.mousePressed(e);
			}
			else if (hitElement != null) {
				if (MainFrame.getInstance().getCurrentView().getSelectionModel().getSelectedElements().contains(hitElement)) {
					if (e.isControlDown()) {
						// kopiraj element
						// selektuj kopirani element
					}
					if(view.getCursor().getName().equals(Cursor.getDefaultCursor().getName())){
						// predji u MoveState
						State state = MainFrame.getInstance().getCurrentView().getModel().getMoveElementState();
						state.mousePressed(e);
						state.setView(view);
						state.setController(controller);
						state.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
						MainFrame.getInstance().setStatusTrack(state.toString());
						controller.setCurrentState(state);
					}else{
						// predji u ResizeState
						State state = MainFrame.getInstance().getCurrentView().getModel().getResizeState();
						state.setView(view);
						state.setController(controller);
						state.mousePressed(e);
						MainFrame.getInstance().setStatusTrack(state.toString());
						controller.setCurrentState(state);
						//ne treba da se menja cursor
					}
				}
			}

			/*
			else if (pogodjenKruzic) {
				predji u RotateState
			}
			 */
			else {
				State state = MainFrame.getInstance().getCurrentView().getModel().getLassoSelectionState();
				state.setView(view); 
				state.setController(controller);
				state.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				MainFrame.getInstance().setStatusTrack(state.toString());
				controller.setCurrentState(state);
			}
		} else {
			if (hitElement != null) {
				if (view.getSelectionModel().getSelectedElements().contains(hitElement)) {
					// kopiraj element
					// selektuj kopirani element
					// predji u MoveState
				}
			}
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int xPos = e.getX();
		int yPos = e.getY();
		// azuriranje koordinata
		MainFrame.getInstance().setPositionTrack(xPos, yPos);

		if(view.getSelectionModel().getSelectedElements().size() == 1){
			GraphElement selectedElement = view.getSelectionModel().getSelectedElements().get(0);
			Dimension2D size = (Dimension2D)selectedElement.getProperty(GraphElementProperties.SIZE);
			Point2D position = new Point2D.Double(((Point2D)selectedElement.getProperty(GraphElementProperties.POSITION)).getX()-size.getWidth()/2,
					((Point2D)selectedElement.getProperty(GraphElementProperties.POSITION)).getY()-size.getHeight()/2);

			//levo (polje sirine 5 piksela : po 2 piksela sa obe strane linije + linija)
			if(position.getX() <= xPos && xPos <= position.getX()+4){
				//levo-gore
				if(position.getY() <= yPos && yPos <= position.getY()+4){
					setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
				}//levo-sredina
				else if(position.getY()+4 < yPos && yPos < position.getY()+size.getHeight()-4){
					setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
				}//levo-dole
				else if(position.getY()+size.getHeight()-4 <= yPos && yPos <= position.getY()+size.getHeight()){
					setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
				}//ostalo
				else{
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}//sredina
			else if(position.getX()+4 < xPos && xPos < position.getX()+size.getWidth()-4){
				//sredina-gore
				if(position.getY() <= yPos && yPos <= position.getY()+4){
					setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
				}//sredina-dole
				else if(position.getY()+size.getHeight()-4 <= yPos && yPos <= position.getY()+size.getHeight()){
					setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
				}//ostalo
				else{
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}//desno
			else if(position.getX()+size.getWidth()-4 <= xPos && xPos <= position.getX()+size.getWidth()){
				//desno-gore
				if(position.getY() <= yPos && yPos <= position.getY()+4){
					setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
				}//desno-sredina
				else if(position.getY()+4 < yPos && yPos < position.getY()+size.getHeight()-4){
					setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				}//desno-dole
				else if(position.getY()+size.getHeight()-4 <= yPos && yPos <= position.getY()+size.getHeight()){
					setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
				}//ostalo
				else{
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}//ostalo
			else{
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}else{
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (view.getSelectionModel().getSelectedElements().size() > 0) {
				// otvori properties window za pojedinacni ili grupu elemenata
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// tockic je pomeren na gore
		if (e.getWheelRotation() < 0) {
			if (e.isControlDown()) {
				// zoom in nad kanvasom
			} else if (e.isShiftDown()) {
				// horizontalno skrolovanje na levo
			} else {
				// vertikalno skrolovanje na gore
			}
		} else {
			if (e.isControlDown()) {
				// zoom out nad kanvasom
			} else if (e.isShiftDown()) {
				// horizontalno skrolovanje na desno
			} else {
				// vertikalno skrolovanje na dole
			}
		}
	}

	@Override
	public void mouseClicked (MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) 
			if (e.getClickCount()==2){
				Link hitLink = controller.getCurrentLink();
				if (hitLink!=null){
					if (hitLink instanceof AssociationLink){
						AssociationLinkDialog d=new AssociationLinkDialog((AssociationLink ) hitLink);
						d.setVisible(true);
						if (d.isSomethingChanged()){
							if (d.isCreateNewLink()){
								AssociationLink newLink;
								LinkPainter painter=null;
								if (d.isAssociation()){
									newLink=new AssociationLink(hitLink.getNodes(),d.getSourceCardinality(),d.getDestinationCardinality(),
											d.getSourceRole(),d.getDestinationRole(),"AssociationLink"+((AssociationLink)hitLink).getCurrentCount(),
											d.isSourceNavigable(),d.isDestinationNavigable());
									painter=new AssociationLinkPainter(newLink);
								}
								else if (d.isAggregation()){
									newLink=new AggregationLink(hitLink.getNodes(),d.getSourceCardinality(),d.getDestinationCardinality(),
											d.getSourceRole(),d.getDestinationRole(),"AggregationLink"+((AssociationLink)hitLink).getCurrentCount(),
											d.isSourceNavigable(),d.isDestinationNavigable());
									painter=new AggregationLinkPainter(newLink);
								}
								else{  //if (d.isComposition()){
									newLink=new CompositionLink(hitLink.getNodes(),d.getSourceCardinality(),d.getDestinationCardinality(),
											d.getSourceRole(),d.getDestinationRole(),"CompositionLink"+((AssociationLink)hitLink).getCurrentCount(),
											d.isSourceNavigable(),d.isDestinationNavigable());
									painter=new CompositionLinkPainter(newLink);
								}

								Command command=new ChangeLinkTypeCommand(view, hitLink, newLink, painter);
								view.getModel().getCommandManager().executeCommand(command);

								newLink.getSourceConnector().setLink(newLink);
								newLink.getDestinationConnector().setLink(newLink);						
							}
							else {
								Command command = new ChangeAssociationPropertiesCommand(view,d.getSourceCardinality(),d.getDestinationCardinality(),
										d.getSourceRole(),d.getDestinationRole(),d.isSourceNavigable(),d.isDestinationNavigable(),(AssociationLink)hitLink);
								view.getModel().getCommandManager().executeCommand(command);
							}
							view.repaint();
						}
					}
				}		
				else{
					GraphElement element = controller.getCurrentElement();
					if (element instanceof Package)
						MainFrame.getInstance().showDiagram(((Package)element).getHierarchyPackage().getDiagram());
				}

			}

	}

	public void showGraphElementPopup(MouseEvent e) {
		MainFrame.getInstance().getViewPopupMenu().show(e.getComponent(), e.getX(), e.getY());
	}

	@Override
	public boolean isAutoScrollOnDragEnabled() {
		return false;
	}

	@Override
	public boolean isAutoScrollOnMoveEnabled() {
		return false;
	}
}
