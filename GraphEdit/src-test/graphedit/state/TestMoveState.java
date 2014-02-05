package graphedit.state;

import static org.junit.Assert.assertTrue;
import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.properties.PropertyEnums.GraphElementProperties;
import graphedit.view.ClassPainter;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

public class TestMoveState {
	
	MoveElementState state;
	GraphEditViewMockup view;
	MouseEvent meStart, meEnd;
		
	@Before
	public void setUp() {
		state = new MoveElementState();
		GraphEditModel model = new GraphEditModel("test");
		view = new GraphEditViewMockup(model);
		state.setView(view);
		
		GraphElement klasa = new Class(new Point2D.Double(500, 500));
		klasa.setProperty(GraphElementProperties.SIZE, new Dimension(150,70));
		ElementPainter painter = new ClassPainter(klasa);
		
		//model.addDiagramElement(klasa);
		view.addElementPainter(painter);
		view.getSelectionModel().addSelectedElement(klasa);
		
		meStart = new MouseEvent(view, 0, 0, 0, 500, 500, 1, false);
		meEnd = new MouseEvent(view, 0, 0, 0, 550, 550, 1, false);
	}
	
	@Test
	public void testMousePressed() {
		state.mousePressed(meStart);
		assertTrue("Nema izabranog elementa", state.getView().getSelectionModel().getSelectedElements().size()>0);
	}
	
	@Test
	public void testMouseDragged() {		
		state.mousePressed(meStart);
		state.mouseDragged(meStart);
		state.mouseDragged(meEnd);
		boolean condition = state.getView().getElementPainters().get(0).getShape().getBounds().getCenterX()==550 && state.getView().getElementPainters().get(0).getShape().getBounds().getCenterY()==550;
		assertTrue("Nije pomeren painter", condition);
	}
	
	@Test
	public void testMouseReleased() {
		state.mousePressed(meStart);
		state.mouseDragged(meStart);
		state.mouseDragged(meEnd);
		state.mouseReleased(meEnd);
		boolean condition = ((Point2D)state.getView().getSelectionModel().getSelectedElements().get(0).getProperty(GraphElementProperties.POSITION)).getX()==550 && ((Point2D)state.getView().getSelectionModel().getSelectedElements().get(0).getProperty(GraphElementProperties.POSITION)).getY()==550;
		assertTrue("Element se nije pomerio", condition);
	}
	
	@SuppressWarnings("serial")
	class GraphEditViewMockup extends GraphEditView {

		GraphEditControllerMockup controller;
		public GraphEditViewMockup (GraphEditModel model) {
			super(model);
			controller=new GraphEditControllerMockup();
			state.setController(controller);
		}

		class GraphEditControllerMockup extends GraphEditController{
			
			public GraphEditControllerMockup(){
				state.setController(this);
			}
			@Override
			public Link getCurrentLink(){
				return null;
			}
			@Override 
			public GraphElement getCurrentElement(){
				return state.getView().getModel().getDiagramElements().get(0);
			}
		}
	}

}
