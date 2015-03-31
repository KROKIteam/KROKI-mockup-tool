package kroki.app.utils.uml;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


/**
 * Represent a component that shows a radio buttons
 * with which the user can select if the Text is prefix
 * or suffix and also to select where the text can be
 * in the package, class, property or operation elements
 * of the UML diagram.
 * 
 * @author Zeljko Ivkovic (zekljo89ps@gmail.com)
 *
 */
public class PrefixSuffixTextComponent extends JPanel implements ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Text for which this component is created
	 */
	private String text;
	
	/**
	 * Radio button with which it is determined 
	 * if the text is prefix in the names.
	 */
	private JRadioButton prefix;
	
	/**
	 * Radio button with which it is determined 
	 * if the text is suffix in the names.
	 */
	private JRadioButton suffix;
	
	/**
	 * Radio button with which it is determined 
	 * if the text is should be removed from 
	 * the names of package elements.
	 */
	private JRadioButton packageElement;
	
	/**
	 * Radio button with which it is determined 
	 * if the text is should be removed from 
	 * the names of class elements.
	 */
	private JRadioButton classElement;
	
	/**
	 * Radio button with which it is determined 
	 * if the text is should be removed from 
	 * the names of property elements.
	 */
	private JRadioButton propertyElement;
	
	/**
	 * Radio button with which it is determined 
	 * if the text is should be removed from 
	 * the names of operation elements.
	 */
	private JRadioButton operationElement;
	
	/**
	 * Color to set for the radio buttons when
	 * enough radio buttons have been selected. 
	 */
	private Color colorWhenCorrect;
	
	/**
	 * Border of the component when it is not 
	 * selected.  
	 */
	private Border borderUnselected;
	/**
	 * Creates a component that shows a radio group with
	 * radio buttons that represent the available choices
	 * of business operation types to be created for the
	 * given operation.
	 * @param text text entered by user that should be 
	 * removed
	 */
	public PrefixSuffixTextComponent(String text){
		this.text=text;
		PropagateMouseClick propagateMouseClick=new PropagateMouseClick();
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(Color.black));
		borderUnselected=getBorder();
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.FIRST_LINE_END;
		c.gridx=0;
		c.gridy=0;
		c.weightx=0;
		c.weighty=0;
		JLabel label=new JLabel("Text to be removed:");
		label.addMouseListener(propagateMouseClick);
		add(label,c);
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.gridx=1;
		c.gridy=0;
		c.weightx=1;
		c.weighty=0;
		label=new JLabel(text);
		label.addMouseListener(propagateMouseClick);
		add(label,c);
		
		c.anchor=GridBagConstraints.FIRST_LINE_END;
		c.gridx=0;
		c.gridy=1;
		c.weightx=0;
		c.weighty=0;
		label=new JLabel("Text is:");
		label.addMouseListener(propagateMouseClick);
		add(label,c);
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.gridx=1;
		c.gridy=1;
		c.weightx=1;
		c.weighty=0;
		JPanel radioButtonsPS=new JPanel(new FlowLayout());
		prefix=new JRadioButton("Prefix", true);
		prefix.addMouseListener(propagateMouseClick);
		prefix.addItemListener(this);
		colorWhenCorrect=prefix.getBackground();
		suffix=new JRadioButton("Suffix", false);
		suffix.addMouseListener(propagateMouseClick);
		suffix.addItemListener(this);
		radioButtonsPS.add(prefix);
		radioButtonsPS.add(suffix);
		add(radioButtonsPS,c);	
		
		c.anchor=GridBagConstraints.FIRST_LINE_END;
		c.gridx=0;
		c.gridy=2;
		c.weightx=0;
		c.weighty=0;
		add(new Label("Remove from name of:"),c);
		
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		c.gridx=1;
		c.gridy=2;
		c.weightx=1;
		c.weighty=0;
		JPanel radioButtonsNameOf=new JPanel(new FlowLayout());
		packageElement=new JRadioButton("Package", true);
		packageElement.addMouseListener(propagateMouseClick);
		packageElement.addItemListener(this);
		classElement=new JRadioButton("Class", true);
		classElement.addMouseListener(propagateMouseClick);
		classElement.addItemListener(this);
		propertyElement=new JRadioButton("Property", true);
		propertyElement.addMouseListener(propagateMouseClick);
		propertyElement.addItemListener(this);
		operationElement=new JRadioButton("Operation", true);
		operationElement.addMouseListener(propagateMouseClick);
		operationElement.addItemListener(this);
		radioButtonsNameOf.add(packageElement);
		radioButtonsNameOf.add(classElement);
		radioButtonsNameOf.add(propertyElement);
		radioButtonsNameOf.add(operationElement);
		add(radioButtonsNameOf,c);		
	}
	
	/**
	 * Creates a {@link TextToRemove} object with the
	 * attributes set to the values the user has
	 * entered on this PrefixSuffixTextComponent
	 * @return {@link TextToRemove} object with the
	 * attributes set
	 */
	public TextToRemove getTextToRemove(){
		TextToRemove toRemove=new TextToRemove(text);
		toRemove.setPrefix(prefix.isSelected());
		toRemove.setSuffix(suffix.isSelected());
		toRemove.setFromPackageElement(packageElement.isSelected());
		toRemove.setFromClassElement(classElement.isSelected());
		toRemove.setFromPropertyElement(propertyElement.isSelected());
		toRemove.setFromOperationElement(operationElement.isSelected());
		return toRemove;
		
	}
	/**
	 * Returns the text which the user entered to be 
	 * removed from the names of the UML elements
	 * that are being imported.
	 * @return text the user entered to be removed
	 */
	public String getText(){
		return text;
	}
	
	/**
	 * Checks prefix and suffix radio buttons if they
	 * are both deselected in that case paints the
	 * background red, otherwise if either one or both
	 * are selected uses the default background color.
	 * Also checks if radio buttons for package, class,
	 * property and operation elements are all deselected
	 * in that case paints the background red, otherwise
	 * if either one of them is selected uses the default
	 * background color.
	 * @return true if there is at least one radio button
	 * for prefix or suffix selected and at least one radio
	 * button for package, class, property or operation 
	 * element selected, false otherwise. 
	 */
	public boolean checkCorrectSelected(){
		boolean isCorrect=true;
		if(!prefix.isSelected()&&!suffix.isSelected())
		{
			prefix.setBackground(Color.RED);
			suffix.setBackground(Color.RED);
			isCorrect=false;
		}else
		{
			prefix.setBackground(colorWhenCorrect);
			suffix.setBackground(colorWhenCorrect);
		}
		
		if(!packageElement.isSelected()
				&&!classElement.isSelected()
				&&!propertyElement.isSelected()
				&&!operationElement.isSelected())
		{
			packageElement.setBackground(Color.RED);
			classElement.setBackground(Color.RED);
			propertyElement.setBackground(Color.RED);
			operationElement.setBackground(Color.RED);
			isCorrect=false;
		}else
		{
			packageElement.setBackground(colorWhenCorrect);
			classElement.setBackground(colorWhenCorrect);
			propertyElement.setBackground(colorWhenCorrect);
			operationElement.setBackground(colorWhenCorrect);
		}
		return isCorrect;
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		checkCorrectSelected();
	}
	
	public void setSelected(boolean isSelected){
		if(isSelected){
			//setBorder(BorderFactory.createLoweredBevelBorder());
			setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		}else
		{
			setBorder(borderUnselected);
		}
	}
}
