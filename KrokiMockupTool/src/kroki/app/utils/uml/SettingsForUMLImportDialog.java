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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import kroki.app.KrokiMockupToolApp;

import org.eclipse.uml2.uml.Operation;

/**
 * Dialog that lets the user input text that can represent prefixes and 
 * suffixes in the names of package, class, property and operation elements
 * of the UML diagram and that will be removed from the names during import.
 *
 * @author Zeljko Ivkovic (zekljo89ps@gmail.com)
 */
public class SettingsForUMLImportDialog extends JDialog implements ActionListener {

	/**
	 * List of components that shows the list of text values that the user has
	 * entered.
	 */
	private List<PrefixSuffixTextComponent> components;
	
	/**
	 * Panel that contains all the components that represent the texts the user
	 * has entered.
	 */
	private JPanel textsToRemoveComponents;
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
	 * Command set for the add button on the dialog.
	 */
	private String addCommand="add";
	/**
	 * Text field for entering texts to be removed.
	 */
	private JTextField textField;
	
	JScrollPane pane;
	
	/**
	 * Creates a dialog that shows a text field for entering text to be removed
	 * from the names of package, class, property and operation elements and
	 * a list of all the texts entered.
	 * @param frame       parent dialog
	 */
	public SettingsForUMLImportDialog(Frame frame){
		super(frame,true);
		//determine
		setTitle("Settings for UML import");
		setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.gridx=0;
		c.gridy=0;
		c.weightx=0;
		c.weighty=0;
		c.gridwidth=1;
		add(new Label("Text to remove:"),c);
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.gridx=1;
		c.gridy=0;
		c.weightx=0;
		c.weighty=0;
		c.gridwidth=1;
		textField=new JTextField(10);
		add(textField,c);
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.gridx=2;
		c.gridy=0;
		c.weightx=0;
		c.weighty=0;
		c.gridwidth=1;
		JButton addButton=new JButton("Add text");
		addButton.setActionCommand(addCommand);
		addButton.addActionListener(this);
		add(addButton,c);
		
		
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.gridx=0;
		c.gridy=0;
		c.weightx=0;
		c.weighty=0;
		c.gridwidth=1;
		add(new Label("Entered texts:"),c);
		
		c.fill=GridBagConstraints.BOTH;
		c.gridy=1;
		c.weightx=1;
		c.weighty=1;
		c.gridwidth=3;
		textsToRemoveComponents=new JPanel();
		BoxLayout box=new BoxLayout(textsToRemoveComponents, BoxLayout.Y_AXIS);
		textsToRemoveComponents.setLayout(box);

		components=new ArrayList<PrefixSuffixTextComponent>();
		pane=new JScrollPane(textsToRemoveComponents);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(pane,c);
		
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
			
			boolean isCorrect=true;
			textsToBeRemoved=new ArrayList<TextToRemove>();
			for(PrefixSuffixTextComponent component:components)
				if(component.checkCorrectSelected())
					textsToBeRemoved.add(component.getTextToRemove());
				else
					isCorrect=false;
			if(!isCorrect)
				JOptionPane.showMessageDialog(this, "There has to be at least one option selected.","Error during determining settings",JOptionPane.ERROR_MESSAGE);
			else
			{
				ok=true;
				dispose();
			}
		}else if(arg0.getActionCommand().equals(cancelCommand))
		{
			ok=false;
			dispose();
		}else if(arg0.getActionCommand().equals(addCommand))
		{
			String text=textField.getText();
			if(text!=null)
				if(!text.isEmpty())
					if(!text.trim().isEmpty())
					{
						components.add(new PrefixSuffixTextComponent(text));
						textsToRemoveComponents.removeAll();
						for(PrefixSuffixTextComponent component:components)
						{
							textsToRemoveComponents.add(component);
						}
						//textsToRemoveComponents.validate();
						//textsToRemoveComponents.repaint();
						pane.validate();
						pane.repaint();
					}
		}
		
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
	 * Saves the entered text and all the determined attributes of the
	 * entered text.
	 */
	private List<TextToRemove> textsToBeRemoved;
	
	/**
	 * Gets all the texts with all the determined attributes that the user
	 * entered.
	 * @return   texts with the determined attributes
	 */
	public List<TextToRemove> getTextsToBeRemoved(){
		return textsToBeRemoved;
	}

}
