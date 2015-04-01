import junit.framework.TestCase;
import kroki.api.commons.ApiCommons;
import kroki.api.enums.OperationType;
import kroki.api.util.Util;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.property.VisibleProperty;
import kroki.profil.utils.ParentChildUtil;
import kroki.profil.utils.StandardPanelUtil;

import org.junit.Test;


public class TestCommons extends TestCase{
	
	private StandardPanel standardPanel;
	private ParentChild parentChildPanel;
	
	@Override
	public void setUp(){
		
		standardPanel = new StandardPanel();
		standardPanel.setLabel("TestPanel");
		StandardPanelUtil.defaultGuiSettings((StandardPanel)standardPanel);
		
		parentChildPanel = new ParentChild();
		parentChildPanel.setLabel("TestPanel");
		ParentChildUtil.defaultGuiSettings((ParentChild)parentChildPanel);
		
	}
	
	
	@Test
	public void testMakeAndRemoveVisibleOperationStandardPanel(){
		testMakeAndRemoveVisibleOperation(standardPanel);
	}
	
	
	@Test
	public void testMakeAndRemoveVisibleOperationParentChildPanel(){
		testMakeAndRemoveVisibleOperation(parentChildPanel);
	}
	
	public void testMakeAndRemoveVisibleOperation(VisibleClass panel){
		
		//initial number of visible elements contained by the panel
	    // operations panel, properties panel, toolbar
		int startingNumber = panel.getVisibleElementList().size();
		
		//get operations elements group
		int operationsGroupIndex = Util.getOperationGroupIndex(panel);
		ElementsGroup operationsGroup = (ElementsGroup) panel.getVisibleElementList().get(operationsGroupIndex);
		assertEquals(0, operationsGroup.getVisibleElementList().size());
		
		//add report
		VisibleOperation report = ApiCommons.makeVisibleOperation("test", true, ComponentType.BUTTON,
				panel, OperationType.REPORT);
		assertNotNull(report);
		assertEquals(Report.class, report.getClass());
		int number = startingNumber + 1;
		assertEquals("test", report.getLabel());
		assertEquals(true, report.isVisible());
		assertEquals(ComponentType.BUTTON, report.getComponentType());
		assertEquals(number, panel.getVisibleElementList().size());
		assertEquals(1, operationsGroup.getVisibleElementList().size());
		
		//add transaction
		//this time specify class and group indexes
		//and check their validity
		VisibleOperation transaction = ApiCommons.makeVisibleOperation("test", true, ComponentType.BUTTON,
				panel, OperationType.TRANSACTION, 3, 0);
		assertNotNull(transaction);
		assertEquals(Transaction.class, transaction.getClass());
		assertEquals("test", transaction.getLabel());
		assertEquals(true, transaction.isVisible());
		assertEquals(ComponentType.BUTTON, report.getComponentType());
		assertEquals(3, panel.getVisibleElementList().indexOf(transaction));
		assertEquals(0, operationsGroup.getVisibleElementList().indexOf(transaction));
		number ++;
		assertEquals(number, panel.getVisibleElementList().size());
		assertEquals(2, operationsGroup.getVisibleElementList().size());
		
		//remove the report
		ApiCommons.removeVisibleElement(panel, report);
		number --;
		assertEquals(number, panel.getVisibleElementList().size());
		assertEquals(1, operationsGroup.getVisibleElementList().size());
		
		//remove the transaction
		ApiCommons.removeVisibleElement(panel, transaction);
		number --;
		assertEquals(number, panel.getVisibleElementList().size());
		assertEquals(0, operationsGroup.getVisibleElementList().size());
	}
	
	
	

	@Test
	public void testMakeAndRemoveVisiblePropertyStandardPanel(){
		testMakeAndRemoveVisibleProperty(standardPanel);
	}
	
	
	@Test
	public void testMakeAndRemoveVisiblePropertyParentChildPanel(){
		testMakeAndRemoveVisibleProperty(parentChildPanel);
		
	}
	
	public void testMakeAndRemoveVisibleProperty(VisibleClass panel){
		
		//initial number of visible elements contained by the panel
	    // operations panel, properties panel, toolbar
		int startingNumber = panel.getVisibleElementList().size();
		
		//get operations elements group
		int propertiesGroupIndex = Util.getPropertiesGroupIndex(panel);
		ElementsGroup propertiesGroup = (ElementsGroup) panel.getVisibleElementList().get(propertiesGroupIndex);
		assertEquals(0, propertiesGroup.getVisibleElementList().size());
		
		//add a text field
		VisibleProperty textField = ApiCommons.makeVisibleProperty("test", true,
				ComponentType.TEXT_FIELD, panel);
		assertNotNull(textField);
		int number = startingNumber + 1;
		assertEquals("test", textField.getLabel());
		assertEquals(true, textField.isVisible());
		assertEquals(ComponentType.TEXT_FIELD, textField.getComponentType());
		assertEquals(number, panel.getVisibleElementList().size());
		assertEquals(1, propertiesGroup.getVisibleElementList().size());
		
		//add a combo box
		//this time specify class and group indexes
		//and check their validity
		VisibleProperty comboBox = ApiCommons.makeVisiblePropertyAt("test", true, 
				ComponentType.COMBO_BOX, panel,  3, 0);
		assertNotNull(comboBox);
		assertEquals("test", comboBox.getLabel());
		assertEquals(true, comboBox.isVisible());
		assertEquals(ComponentType.COMBO_BOX, comboBox.getComponentType());
		assertEquals(3, panel.getVisibleElementList().indexOf(comboBox));
		assertEquals(0, propertiesGroup.getVisibleElementList().indexOf(comboBox));
		number ++;
		assertEquals(number, panel.getVisibleElementList().size());
		assertEquals(2, propertiesGroup.getVisibleElementList().size());
		
		//remove the text field
		ApiCommons.removeVisibleElement(panel, textField);
		number --;
		assertEquals(number, panel.getVisibleElementList().size());
		assertEquals(1, propertiesGroup.getVisibleElementList().size());
		
		//remove the combo box
		ApiCommons.removeVisibleElement(panel, comboBox);
		number --;
		assertEquals(number, panel.getVisibleElementList().size());
		assertEquals(0, propertiesGroup.getVisibleElementList().size());
	}
	
	@Test
	public void testMoveElements(){
		
		int initialNumber = standardPanel.getVisibleElementList().size();
		int number = initialNumber;
		VisibleElement[] elements = new VisibleElement[5];
		
		//add 3 visible properties
		for (int i = 0; i < 3; i++){
			elements[i] = ApiCommons.makeVisibleProperty("test" + i, true, ComponentType.TEXT_FIELD, standardPanel);
			number ++;
		}
		assertEquals(number, standardPanel.getVisibleElementList().size());
		
		//first added element
		VisibleElement firstEl = standardPanel.getVisibleElementList().get(initialNumber); 
		assertSame(elements[0], firstEl);
		
		//second added element
		VisibleElement secondEl = standardPanel.getVisibleElementList().get(initialNumber + 1); 
		assertSame(elements[1], secondEl);
		
		//move first element down (this should increase indexes)
		ApiCommons.moveElementDown(standardPanel, initialNumber, 0, 1);
		assertSame(elements[1], standardPanel.getVisibleElementList().get(initialNumber));
		assertSame(elements[0], standardPanel.getVisibleElementList().get(initialNumber + 1));

		//move it down again (this should increase indexes)
		ApiCommons.moveElementDown(standardPanel, initialNumber + 1, 1, 1);
		assertSame(elements[0], standardPanel.getVisibleElementList().get(initialNumber + 2));
		assertSame(elements[2], standardPanel.getVisibleElementList().get(initialNumber + 1));
		
		//now move the currently second element up (this should decrease indexes)
		ApiCommons.moveElementUp(standardPanel, initialNumber + 1, 1, 1);
		assertSame(elements[0], standardPanel.getVisibleElementList().get(initialNumber + 2));
		assertSame(elements[1], standardPanel.getVisibleElementList().get(initialNumber + 1));
		
	}
	
}
