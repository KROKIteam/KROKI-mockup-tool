package graphedit.gui.dialog;
import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.gui.utils.Dialogs;
import graphedit.model.components.AssociationLink;
import graphedit.model.components.AssociationLink.AssociationType;
import graphedit.model.elements.LinkType;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import graphedit.util.LinkingUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class AssociationLinkDialog extends JDialog{

	private static final long serialVersionUID = 1L;


	private JTextField sourceRoleNameTextField;
	private JTextField destinationRoleNameTextField;
	private JComboBox<String> sourceCardinalityComboBox;
	private JComboBox<String> destinationCardinalityComboBox;
	private Dimension labelSize=new Dimension(120,20);
	private String[] possibleCardinality={"0..1","1..1","0..*","1..*","*"};
	private String sourceCardinality,destinationCardinality,sourceRole,destinationRole;
	private JRadioButton rbAssociation, rbAggregation,rbComposition;
	private JCheckBox sourceNavigable, destinationNavigable, showSourceRole, showDestinationRole;
	private AssociationLink.AssociationType associationType;
	private boolean somethingChanged=false;
	private boolean createNewLink=false;
	private AssociationLink associationLink;



	public AssociationLinkDialog(AssociationLink link){
		super(MainFrame.getInstance(),"Association properties",true);
		setSize(480,260);
		setResizable(false);
		setLayout(new BorderLayout());
		setLocationRelativeTo(MainFrame.getInstance());
		associationLink=link;
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
		JPanel sourceDestinationPanel = new JPanel(new GridLayout(1,2));
		JLabel lblSourceRole = new JLabel("Role name:");
		JLabel lblDestinationRole = new JLabel("Role name:");
		
		JLabel lblType = new JLabel("Type:");
		lblType.setPreferredSize(labelSize);
		lblSourceRole.setPreferredSize(labelSize);
		lblDestinationRole.setPreferredSize(labelSize);
		JLabel lblSourceCarinality = new JLabel("Cardinality:");
		lblSourceCarinality.setPreferredSize(labelSize);
		JLabel lblDestinationCarinality = new JLabel("Cardinality:");
		lblDestinationCarinality.setPreferredSize(labelSize);
		rbAggregation = new JRadioButton();
		rbAssociation = new JRadioButton();
		rbComposition = new JRadioButton();
		
		JLabel lblSourceNavigable = new JLabel("Navigable:");
		JLabel lblDestinationNavigable = new JLabel("Navigable:");
		lblSourceNavigable.setPreferredSize(labelSize);
		lblDestinationNavigable.setPreferredSize(labelSize);
		sourceNavigable = new JCheckBox();
		destinationNavigable = new JCheckBox();
		sourceNavigable.setSelected((Boolean) link.getProperty(LinkProperties.SOURCE_NAVIGABLE));
		destinationNavigable.setSelected ((Boolean) link.getProperty(LinkProperties.DESTINATION_NAVIGABLE));
		
		JLabel lblShowSourceRole = new JLabel("Show source role:");
		lblShowSourceRole.setPreferredSize(labelSize);
		showSourceRole = new JCheckBox();
		showSourceRole.setSelected((Boolean) link.getProperty(LinkProperties.SHOW_SOURCE_ROLE));
		
		JLabel lblShowDestinationRole = new JLabel("Show destination role:");
		lblShowDestinationRole.setPreferredSize(labelSize);
		showDestinationRole = new JCheckBox();
		showDestinationRole.setSelected((Boolean) link.getProperty(LinkProperties.SHOW_DESTINATION_ROLE));

		//source panel

		JPanel sourcePanel = new JPanel(new GridLayout(4,1));
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel1.add(lblSourceRole);
		sourceRoleNameTextField = new JTextField(10);
		sourceRoleNameTextField.setText((String)link.getProperty(LinkProperties.SOURCE_ROLE));
		panel1.add(sourceRoleNameTextField);
		sourcePanel.add(panel1);	
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel2.add(lblSourceCarinality);
		sourceCardinalityComboBox = new JComboBox<String>(possibleCardinality);
		sourceCardinalityComboBox.setSelectedItem(link.getProperty(LinkProperties.SOURCE_CARDINALITY));
		panel2.add(sourceCardinalityComboBox);
		sourcePanel.add(panel2);
		JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel5.add(lblSourceNavigable);
		panel5.add(sourceNavigable);
		sourcePanel.add(panel5);
		JPanel panel7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel7.add(lblShowSourceRole);
		panel7.add(showSourceRole);
		sourcePanel.add(panel7);
		
		TitledBorder sourceBorder=(new TitledBorder(new EtchedBorder(),"Source"));
		sourceBorder.setTitleColor(Color.BLUE);
		sourcePanel.setBorder(sourceBorder);

		sourceDestinationPanel.add(sourcePanel);

		//destination panel 

		JPanel destinationPanel =new JPanel(new GridLayout(4,1));
		JPanel panel3=new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel3.add(lblDestinationRole);
		destinationRoleNameTextField=new JTextField(10);
		destinationRoleNameTextField.setText((String)link.getProperty(LinkProperties.DESTINATION_ROLE));
		panel3.add(destinationRoleNameTextField);
		destinationPanel.add(panel3);	
		JPanel panel4=new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel4.add(lblDestinationCarinality);
		destinationCardinalityComboBox=new JComboBox<String>(possibleCardinality);
		destinationCardinalityComboBox.setSelectedItem(link.getProperty(LinkProperties.DESTINATION_CARDINALITY));
		panel4.add(destinationCardinalityComboBox);
		destinationPanel.add(panel4);
		JPanel panel6=new  JPanel(new FlowLayout(FlowLayout.LEFT));
		panel6.add(lblDestinationNavigable);
		panel6.add(destinationNavigable);
		destinationPanel.add(panel6);
		JPanel panel8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel8.add(lblShowDestinationRole);
		panel8.add(showDestinationRole);
		destinationPanel.add(panel8);


		TitledBorder destinationBorder=(new TitledBorder(new EtchedBorder(),"Destination"));
		destinationBorder.setTitleColor(Color.BLUE);
		destinationPanel.setBorder(destinationBorder);

		sourceDestinationPanel.add(destinationPanel);
		upperPanel.add(sourceDestinationPanel);


		JPanel typePanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		typePanel.add(lblType);
		ButtonGroup buttonGroup=new ButtonGroup();
		associationType=link.getAssociationType();
		if (associationType==AssociationType.COMPOSITION)
			rbComposition.setSelected(true);
		else if (associationType==AssociationType.AGGREGATION)
			rbAggregation.setSelected(true);
		else
			rbAssociation.setSelected(true);

		buttonGroup.add(rbAggregation);
		buttonGroup.add(rbAssociation);
		buttonGroup.add(rbComposition);
		typePanel.add(rbAssociation);
		typePanel.add(new JLabel("Association     "));
		typePanel.add(rbAggregation);
		typePanel.add(new JLabel("Aggregation     "));
		typePanel.add(rbComposition);
		typePanel.add(new JLabel("Composition     "));
		upperPanel.add(typePanel);
		add(upperPanel,BorderLayout.CENTER);
		//lower panel - buttons
		JPanel lowerPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton btnCancel=new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				somethingChanged=false;
				setVisible(false);		
			}			
		});

		JButton btnOk=new JButton("OK");
		btnOk.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				destinationCardinality=(String)destinationCardinalityComboBox.getSelectedItem();
				sourceCardinality=(String)sourceCardinalityComboBox.getSelectedItem();
				destinationRole=destinationRoleNameTextField.getText();
				sourceRole=sourceRoleNameTextField.getText();

				if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE){
					LinkType linkType= LinkingUtil.checkLinkType(associationLink);
					if (linkType == LinkType.NEXT_ZOOM){
						if (sourceCardinality.charAt(sourceCardinality.length()-1) == destinationCardinality.charAt(destinationCardinality.length()-1)){
							Dialogs.showErrorMessage("Invalid cardinalities", "Error");
							return;
						}
					}
					else{
						if (sourceCardinality.charAt(sourceCardinality.length()-1) != '1' || destinationCardinality.charAt(destinationCardinality.length()-1)!='1'){
							Dialogs.showErrorMessage("Invalid cardinalities", "Error");
							return;
						}
					}
						

				}

				//gledamo da li se promenio tip veze
				if ((rbAssociation.isSelected() && associationType!=AssociationType.REGULAR) 
						|| (rbAggregation.isSelected() && associationType!=AssociationType.AGGREGATION)
						|| (rbComposition.isSelected() && associationType!=AssociationType.COMPOSITION)){
					createNewLink=true;
					somethingChanged=true;
				}
				else if (!destinationCardinality.equals((String)associationLink.getProperty(LinkProperties.DESTINATION_CARDINALITY))
						||!sourceCardinality.equals((String)associationLink.getProperty(LinkProperties.SOURCE_CARDINALITY))
						|| !destinationRole.equals((String)associationLink.getProperty(LinkProperties.DESTINATION_ROLE))
						||!sourceRole.equals((String)associationLink.getProperty(LinkProperties.SOURCE_ROLE)))
					somethingChanged=true;
				else if (((Boolean) associationLink.getProperty(LinkProperties.DESTINATION_NAVIGABLE)) != isDestinationNavigable()
						||((Boolean) associationLink.getProperty(LinkProperties.SOURCE_NAVIGABLE)) != isSourceNavigable())
					somethingChanged=true;
				else if (((Boolean) associationLink.getProperty(LinkProperties.SHOW_DESTINATION_ROLE)) != isShowDestinationRole()
						||((Boolean) associationLink.getProperty(LinkProperties.SHOW_SOURCE_ROLE)) != isShowSourceRole())
					somethingChanged=true;
				setVisible(false);	
			}		
		});

		lowerPanel.add(btnOk);
		lowerPanel.add(btnCancel);

		add(lowerPanel,BorderLayout.SOUTH);
		pack();
	}

	public String getSourceCardinality() {
		return sourceCardinality;
	}

	public String getDestinationCardinality() {
		return destinationCardinality;
	}

	public String getSourceRole() {
		return sourceRole;
	}

	public String getDestinationRole() {
		return destinationRole;
	}


	public boolean isCreateNewLink() {
		return createNewLink;
	}

	public boolean isAssociation(){
		return rbAssociation.isSelected();
	}
	public boolean isAggregation(){
		return rbAggregation.isSelected();
	}
	public boolean isComposition(){
		return rbComposition.isSelected();
	}

	public boolean isSomethingChanged() {
		return somethingChanged;
	}

	public void setSomethingChanged(boolean somethingChanged) {
		this.somethingChanged = somethingChanged;
	}
	public boolean isSourceNavigable(){
		return sourceNavigable.isSelected();
	}
	public boolean isDestinationNavigable(){
		return destinationNavigable.isSelected();
	}
	
	public boolean isShowSourceRole(){
		return showSourceRole.isSelected();
	}
	
	public boolean isShowDestinationRole(){
		return showDestinationRole.isSelected();
	}

}
