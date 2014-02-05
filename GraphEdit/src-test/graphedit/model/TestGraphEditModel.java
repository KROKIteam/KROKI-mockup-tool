package graphedit.model;

import static org.junit.Assert.*;
import graphedit.model.components.Class;
import graphedit.model.diagram.GraphEditModel;

import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

public class TestGraphEditModel {

	private GraphEditModel testModel;
	
	@Before
	public void setUp() {
		testModel = new GraphEditModel("test name");
	}

	@Test
	public void testAddElement() {
		Class newElement = new Class(new Point2D.Double(100, 100));
		//testModel.addDiagramElement(newElement);
		assertSame("Element u listi nije isti kao dodati element.", testModel.getDiagramElements().get(0), newElement);
	}
	
	/*@Test(expected=RuntimeException.class)
	public void testAddElementTwice() {
		Class newElement = new Class(new Point2D.Double(100, 100));
		testModel.addDiagramElement(newElement);
		testModel.addDiagramElement(newElement);
		assertTrue("Ne bi trebao biti dodat",testModel.getDiagramElements().size()==1);
	}*/

	@Test
	public void testRemoveElement() {
		Class newElement1 = new Class(new Point2D.Double(100, 100));
		testModel.addElement(newElement1);
		//testModel.removeDiagramElement(newElement1);
		assertTrue("Nije izbrisan",testModel.getDiagramElements().size()==0);
	}
}
