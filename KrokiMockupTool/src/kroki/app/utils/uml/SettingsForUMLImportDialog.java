package kroki.app.utils.uml;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

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
	 * Command set for the remove button on the dialog.
	 */
	private String removeCommand="remove";
	
	/**
	 * Removes a selected PrefixSuffixTextComponent that
	 * was created when the user entered text that is to
	 * be removed from the names of the elements being
	 * imported.
	 */
	private JButton removeButton; 
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
		
		c.gridx=3;
		removeButton=new JButton("Remove");
		removeButton.setActionCommand(removeCommand);
		removeButton.addActionListener(this);
		removeButton.setEnabled(false);
		add(removeButton,c);
		
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.gridx=0;
		c.gridy=1;
		c.weightx=0;
		c.weighty=0;
		c.gridwidth=4;
		add(new Label("Entered texts:"),c);
		
		c.fill=GridBagConstraints.BOTH;
		c.gridy=2;
		c.weightx=1;
		c.weighty=1;
		c.gridwidth=4;
		textsToRemoveComponents=new JPanel();
		BoxLayout box=new BoxLayout(textsToRemoveComponents, BoxLayout.Y_AXIS);
		textsToRemoveComponents.setLayout(box);
		textsToRemoveComponents.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Point xp = SwingUtilities.convertPoint((Component) arg0.getSource(), arg0.getPoint(), textsToRemoveComponents);
				Component comp=textsToRemoveComponents.getComponentAt(xp);
				if(comp instanceof PrefixSuffixTextComponent)
				{
					PrefixSuffixTextComponent textComp=(PrefixSuffixTextComponent)comp;
					changeSelectedPrefixSuffix(textComp);
				}
				else
				{
					//System.out.println("Component "+comp);
				}
			}
			
		});
		
		components=new ArrayList<PrefixSuffixTextComponent>();
		pane=new JScrollPane(textsToRemoveComponents);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(pane,c);
		
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_END;
		c.gridy=3;
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
		repaintTextsToBeRemoved();
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
				{
					String trimedText=text.trim();
					if(!trimedText.isEmpty())
					{
						boolean notFound=true;
						for(PrefixSuffixTextComponent comp:components)
						{
							if(comp.getText().equals(trimedText))
							{
								notFound=false;
								JOptionPane.showMessageDialog(this, "Text:\""+trimedText+"\" has already been entered.","Error during determining settings",JOptionPane.ERROR_MESSAGE);
								break;
							}
						}
						if(notFound)
						{
							components.add(new PrefixSuffixTextComponent(trimedText));
							repaintTextsToBeRemoved();
						}
					}
				}
		}else if(arg0.getActionCommand().equals(removeCommand))
		{
			components.remove(selected);
			changeSelectedPrefixSuffix(null);
			repaintTextsToBeRemoved();
		}
		
	}
	
	/**
	 * Shows all the PrefixSuffixTextComponent that
	 * where created for the texts the user has
	 * entered to be removed or if there are no
	 * texts entered then it shows a message
	 * that there are no texts entered.
	 */
	private void repaintTextsToBeRemoved(){
		textsToRemoveComponents.removeAll();
		if(components.isEmpty())
		{
			JPanel noTexts=new JPanel();
			noTexts.add(new JLabel("No texts entered to be removed as prefix or suffix from the names of the UML diagram elements that are being imported."));
			textsToRemoveComponents.add(noTexts);
		}else
		{
			for(PrefixSuffixTextComponent component:components)
			{
				textsToRemoveComponents.add(component);
			}
		}
		//textsToRemoveComponents.validate();
		//textsToRemoveComponents.repaint();
		pane.validate();
		pane.repaint();
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
	
	/**
	 * {@link PrefixSuffixTextComponent} that is currently selected.
	 */
	private PrefixSuffixTextComponent selected;
	
	/**
	 * Changes the selected {@link PrefixSuffixTextComponent},
	 * and resets the component that was formerly selected.
	 * @param newSelected new component that is to be selected
	 */
	private void changeSelectedPrefixSuffix(PrefixSuffixTextComponent newSelected){
		if(selected!=null)
			selected.setSelected(false);
		selected=newSelected;
		if(selected!=null)
		{
			selected.setSelected(true);
			removeButton.setEnabled(true);
		}else
			removeButton.setEnabled(false);
	}

}
