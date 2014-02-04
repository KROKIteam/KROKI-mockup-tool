package graphedit.state;
import static org.junit.Assert.*;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import graphedit.app.MainFrame.ToolSelected;
import graphedit.model.components.Class;
import graphedit.model.components.Link;
import graphedit.model.components.LinkableElement;
import graphedit.model.diagram.GraphEditModel;
import graphedit.state.TestSelectionState.GraphEditViewMockup.GraphEditControllerMockup;
import graphedit.view.GraphEditView;

import org.junit.Before;
import org.junit.Test;

public class TestLinkState {


	LinkState state;
	GraphEditViewMockup view;
	GraphEditControllerMockup controller;
	
	@Before
	public void setUp() {
		state = new LinkState();
		GraphEditModel model=new GraphEditModel("test");
		view=new GraphEditViewMockup(model);
		state.setView(view);
		view.setSelectedTool(ToolSelected.ASSOCIATION);
	}

	@Test
	public void testMousePressedForTheFirstTime() {
		MouseEvent event = new MouseEvent(state.view, 0, 0, 0, 0, 0, 1, false, MouseEvent.BUTTON1);
		state.mousePressed(event);

		assertNotNull("Klik na element ne postavlja sourceElement", state.getSourceElement());
		assertTrue("Klik na element ne kreira link node", state.getNodes().size()>0);
	}
	
	@Test
	public void testMouseReleased() {
		MouseEvent event = new MouseEvent(state.view, 0, 0, 0, 0, 0, 1, false, MouseEvent.BUTTON1);
		state.mousePressed(event);
		while (state.isLinkingInProgress())
			state.mouseReleased(event);

		assertNotNull("link nije kreiran", state.getLink());
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
			public LinkableElement getMousePressedElement(){
					return new Class(new Point2D.Double(Math.random()*100,Math.random()*100));	
			}
			
			@Override 
			public LinkableElement getMouseReleasedElement(){
				if (Math.random()*100<=50)
					return new Class(new Point2D.Double(Math.random()*100,Math.random()*100));
				return null;
			}
		}
	}
}
	


