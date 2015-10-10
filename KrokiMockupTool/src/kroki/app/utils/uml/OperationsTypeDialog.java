package kroki.app.utils.uml;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.eclipse.uml2.uml.Operation;

/**
 * Dialog that asks the user to choose which kind of Business operation should
 * be created when when the operations in a UML diagram do not have
 * a stereotype set that determines which kind of operation to create.
 *
 * @author Zeljko Ivkovic (ivkovicszeljko@gmail.com)
 */
public class OperationsTypeDialog extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * List of components that show the available business operation types for
	 * the user to choose from.
	 */
	private List<OperationTypeComponent> components;
	/**
	 * Determines if the user excepted the offered choices. By default it is
	 * false if the user closes the dialog with out pressing the OK button.
	 */
	private boolean ok;
	/**
	 * Command set for the OK button on the dialog.
	 */
	private String okCommand="OK";
	/**
	 * Command set for the Cancel button on the dialog.
	 */
	private String cancelCommand="cancel";
	
	/**
	 * Creates a dialog that shows a list of operations for which the user should choose
	 * the type of business operation to be created.
	 * @param frame       parent dialog
	 * @param operations  operations for which to check what type of operation to create
	 */
	public OperationsTypeDialog(Frame frame, List<Operation> operations){
		super(frame,true);
		//determine
		setTitle("Determine type of operation");
		setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.gridx=0;
		c.gridy=0;
		c.weightx=0;
		c.weighty=0;
		add(new Label("Operations:"),c);
		c.fill=GridBagConstraints.BOTH;
		c.gridy=1;
		c.weightx=1;
		c.weighty=1;
		JPanel centerPanel=new JPanel();
		BoxLayout box=new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
		centerPanel.setLayout(box);

		components=new ArrayList<OperationTypeComponent>();
		OperationTypeComponent component;
		for(Operation operation:operations)
		{
			component=new OperationTypeComponent(operation);
			centerPanel.add(component);
			components.add(component);
		}
		
		add(new JScrollPane(centerPanel),c);
		
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_END;
		c.gridy=2;
		c.weightx=0;
		c.weighty=0;
		JPanel buttonPanel=new JPanel();
		
		BoxLayout buttonLayout=new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
		buttonPanel.setLayout(buttonLayout);
		
		buttonPanel.add(Box.createGlue());
		JButton okBtn=new JButton("OK");
		okBtn.setActionCommand(okCommand);
		okBtn.addActionListener(this);
		buttonPanel.add(okBtn,c);
		buttonPanel.add(Box.createHorizontalStrut(10));
		JButton cancelBtn=new JButton("Cancel");
		cancelBtn.setActionCommand(cancelCommand);
		cancelBtn.addActionListener(this);
		buttonPanel.add(cancelBtn,c);
		buttonPanel.add(Box.createHorizontalStrut(10));
		add(buttonPanel,c);
		Dimension size=frame.getSize();
		setMinimumSize(new Dimension(400, 400));
		setSize(size.width*2/3, size.height*2/3);
		setLocationRelativeTo(frame);
		ok=false;
	}
	
	
	/**
	 * If OK button was pressed, gets what type of business operation was
	 * selected to be created for a given operation. If OK or Cancel button
	 * were pressed closes the dialog.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().equals(okCommand))
		{
			ok=true;
			operationTypes=new HashMap<Operation, Class<?>>();
			for(OperationTypeComponent component:components)
				operationTypes.put(component.getOperation(), component.getOperationType());
		}
		dispose();
	}
	
	/**
	 * Determine if the OK button was pressed, or the user closed the dialog in some other
	 * way.
	 * @return true  if the OK button was pressed, false for any other way of closing the dialog
	 */
	public boolean isOK(){
		return ok;
	}
	
	/**
	 * Saves the chosen type of business operation to be created for the
	 * given operation.
	 */
	private Map<Operation,Class<?>> operationTypes;
	
	/**
	 * Gets the user selected business operation types to be created for the
	 * given operations.
	 * @return   user selected business operation types to be created for the
	 * given operations
	 */
	public Map<Operation,Class<?>> getOperationTypes(){
		return operationTypes;
	}
	
}
