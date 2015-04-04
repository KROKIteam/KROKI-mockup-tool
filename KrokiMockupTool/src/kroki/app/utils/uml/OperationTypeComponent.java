package kroki.app.utils.uml;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;

import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;

/**
 * Represents a component that shows a radio group with
 * radio buttons that represent the available choices
 * of business operation types to be created for the
 * given operation.
 * 
 * @author Zeljko Ivkovic (ivkovicszeljko@gmail.com)
 *
 */
public class OperationTypeComponent extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Operation for which to choose which type of operation
	 * to create.
	 */
	private Operation operation;
	
	/**
	 * Radio button for the Report type business operation
	 * to be created.
	 */
	private JRadioButton report;
	/**
	 * Radio button for the Transaction type business operation
	 * to be created.
	 */
	private JRadioButton transaction;
	/**
	 * Creates a component that shows a radio group with
	 * radio buttons that represent the available choices
	 * of business operation types to be created for the
	 * given operation.
	 * @param operation  operation for which to choose a business type operation
	 * to be created
	 */
	public OperationTypeComponent(Operation operation){
		this.operation=operation;
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(Color.black));
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.gridx=0;
		c.gridy=0;
		c.weightx=0;
		c.weighty=0;
		add(new Label("Operation name:"),c);
		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridx=1;
		c.gridy=0;
		c.weightx=1;
		c.weighty=0;
				
		add(new Label(operation.getName()),c);
		
		
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.gridx=0;
		c.gridy=1;
		c.weightx=0;
		c.weighty=0;
		add(new Label("Operation Class:"),c);
		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridx=1;
		c.gridy=1;
		c.weightx=1;
		c.weighty=0;
				
		add(new Label(((ClassImpl)operation.getOwner()).getName()),c);
		
		
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.gridx=0;
		c.gridy=2;
		c.weightx=0;
		c.weighty=0;
		add(new Label("Operation Type:"),c);
		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridx=1;
		c.gridy=2;
		c.weightx=1;
		c.weighty=0;
		JPanel radioButtons=new JPanel(new FlowLayout());
		report=new JRadioButton("Report", true);
		transaction=new JRadioButton("Transaction", false);
		radioButtons.add(report);
		radioButtons.add(transaction);
		ButtonGroup group=new ButtonGroup();
		group.add(report);
		group.add(transaction);
		add(radioButtons,c);		
	}
	
	/**
	 * Gets a operation for which to choose a type of
	 * business operation to be created.
	 * @return  operation for which to choose a type of
	 * business operation to be created.
	 */
	public Operation getOperation(){
		return operation;
	}
	
	/**
	 * Gets the type of business operation to be
	 * created for the given operation.
	 * @return  type of business operation to be
	 * created for the given operation.
	 */
	public Class<?> getOperationType(){
		if(transaction.isSelected())
			return Transaction.class;
		else
			return Report.class;
	}
}
