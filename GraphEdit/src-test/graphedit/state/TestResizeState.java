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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

public class TestResizeState {
	
	ResizeState state;
	GraphEditViewMockup view;
	MouseEvent meStart, meEnd;
	
	@Before
	public void setUp() {
		state = new ResizeState();
		GraphEditModel model = new GraphEditModel("test");
		view = new GraphEditViewMockup(model);
		state.setView(view);
		
		GraphElement klasa = new Class(new Point2D.Double(500, 500));
		klasa.setProperty(GraphElementProperties.SIZE, new Dimension(150,70));
		ElementPainter painter = new ClassPainter(klasa);
		
		//model.addDiagramElement(klasa);
		view.addElementPainter(painter);
		view.getSelectionModel().addSelectedElement(klasa);
		/*
		meStart = new MouseEvent(view, 0, 0, 0, 500, 500, 1, false);
		meEnd = new MouseEvent(view, 0, 0, 0, 800, 550, 1, false);
		*/
	}
	
	@Test
	public void testNorthResize() { //gore
		view.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
		meStart = new MouseEvent(view, 0, 0, 0, 500, 466, 1, false);
		meEnd = new MouseEvent(view, 0, 0, 0, 436, 366, 1, false);
		
		try{
			state.mousePressed(meStart);
			state.mouseDragged(meStart);
			state.mouseDragged(meEnd);
			state.mouseReleased(meEnd);
		}catch(Throwable t){
			
		}
		
		//System.out.println("x: "+((Point2D)state.getView().getSelectionModel().getSelectedElements().get(0).getProperty(GraphElementProperties.POSITION)).getX());
		//System.out.println("y: "+((Point2D)state.getView().getSelectionModel().getSelectedElements().get(0).getProperty(GraphElementProperties.POSITION)).getY());
		
		boolean condition = ((Point2D)state.getView().getSelectionModel().getSelectedElements().get(0).getProperty(GraphElementProperties.POSITION)).getX()==500 && ((Point2D)state.getView().getSelectionModel().getSelectedElements().get(0).getProperty(GraphElementProperties.POSITION)).getY()==450;
		assertTrue("Nije uradio dobar NORTH resize",condition);
	}
	
	@Test
	public void testSouthResize() { //dole
		view.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
		meStart = new MouseEvent(view, 0, 0, 0, 500, 534, 1, false);
		meEnd = new MouseEvent(view, 0, 0, 0, 436, 634, 1, false);
		
		try{
			state.mousePressed(meStart);
			state.mouseDragged(meEnd);
			state.mouseReleased(meEnd);
		}catch(Throwable t){
			
		}
		
		boolean condition = ((Dimension2D)state.getView().getSelectionModel().getSelectedElements().get(0).getProperty(GraphElementProperties.SIZE)).getWidth()==150 && ((Dimension2D)state.getView().getSelectionModel().getSelectedElements().get(0).getProperty(GraphElementProperties.SIZE)).getHeight()==170;
		assertTrue("Nije uradio dobar SOUTH resize",condition);
	}
	
	@Test
	public void testWestResize() { //levo
		
	}
	
	@Test
	public void testEastResize() { //desno
		
	}
	
	@Test
	public void testNorthWestResize() { //gore-levo
		
	}
	
	@Test
	public void testSouthWestResize() { //dole-levo
		
	}
	
	@Test
	public void testNorthEastResize() { //gore-desno
		
	}
	
	@Test
	public void testSouthEastResize() { //dole-desno
		
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
