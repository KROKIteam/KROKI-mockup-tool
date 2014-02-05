package graphedit.state;
import static org.junit.Assert.*;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;





import graphedit.model.components.Class;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.diagram.GraphEditModel;
import graphedit.view.GraphEditView;

import org.junit.Before;
import org.junit.Test;

public class TestSelectionState {


	SelectionState state;
	GraphEditViewMockup view;
	
	@Before
	public void setUp() {
		state = new SelectionState();
		GraphEditModel model=new GraphEditModel("test");
		view=new GraphEditViewMockup(model);
		state.setView(view);
		
	}

	
	//test ne prolazi jer SwingUtilities.isLeftMouseButton vraca false, ako se stavi provera tamo e.getButton()==1, sve ok...
	@Test
	public void testMousePressed() {
		
		MouseEvent event = new MouseEvent(state.getView(), 0, 0, 0, 0, 0, 1, true, MouseEvent.BUTTON1);
		state.mousePressed(event);
		assertTrue("Klik na element ne postavlja currentElement.", state.getView().getSelectionModel().getSelectedElements().size()>1);
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
				return new Class(new Point2D.Double(50,50));
			}
		}
	}
}
	


