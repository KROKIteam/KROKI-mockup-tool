package kroki.profil.utils.settings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.subsystem.BussinesSubsystem;
import net.miginfocom.swing.MigLayout;

public class BusinessSubsystemSettings extends JPanel implements Settings{

	private static final long serialVersionUID = 1L;

	protected BussinesSubsystem businessSubsystem;
	protected SettingsCreator settingsCreator;
	protected LayoutManager panelLayout;
	protected JLabel lblLabel;
	protected JTextField tfLabel;
	protected JLabel lblLabelToCode;
	protected JCheckBox chLabelToCode;
	protected JLabel lblEclipsePath;
	protected JTextField tfEclipsePath;
	protected JButton btnUnlink;
	protected JButton btnLink;
	protected JLabel descriptionLabel;
	protected JTextArea descriptionTextArea;

	public BusinessSubsystemSettings(SettingsCreator settingsCreator){
		panelLayout = new MigLayout("wrap 2,hidemode 0", "[right, shrink][fill, 200]");
		this.settingsCreator = settingsCreator;
		initComponents();
		layoutComponents();
		addActions();
	}

	private void initComponents(){
		lblLabel = new JLabel();
		lblLabel.setText(Intl.getValue("visibleElement.label"));
		tfLabel = new JTextField(30);
		lblLabelToCode = new JLabel();
		lblLabelToCode.setText(Intl.getValue("stfPanelSettings.labelToCode"));
		chLabelToCode = new JCheckBox();
		lblEclipsePath = new JLabel();
		lblEclipsePath.setText(Intl.getValue("stfPanelSettings.eclipseProjectPath"));
		tfEclipsePath = new JTextField(50);
		tfEclipsePath.setEditable(false);
		btnUnlink = new JButton();
		btnLink = new JButton();
		try {
			Image imgUnlink = ImageIO.read(getClass().getResource("/resources/images/remove.png"));
			btnUnlink.setIcon(new ImageIcon(imgUnlink));
			btnUnlink.setMaximumSize(new Dimension(24, 24));
			
			Image imgEdit = ImageIO.read(getClass().getResource("/resources/images/select_folder.png"));
			btnLink.setIcon(new ImageIcon(imgEdit));
			btnLink.setMaximumSize(new Dimension(24, 24));
		} catch (IOException e) {
			btnUnlink.setText("unlink");
			btnLink.setText("...");
		}
		btnUnlink.setToolTipText(Intl.getValue("stfPanelSettings.unlinkTooltip"));
		btnLink.setToolTipText(Intl.getValue("stfPanelSettings.linkTooltip"));
		descriptionLabel = new JLabel(Intl.getValue("stfPanelSettings.projectDescription"));
		descriptionTextArea = new JTextArea(5, 5);
		Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		descriptionTextArea.setBorder(border);
		descriptionTextArea.setFont(tfLabel.getFont());
		descriptionTextArea.setLineWrap(true);
	}

	private void layoutComponents() {
		setLayout(new MigLayout("wrap 2,hidemode 0", "[right, shrink][fill, 200]"));
		add(lblLabel);
		add(tfLabel);
		add(lblLabelToCode);
		add(chLabelToCode);
		add(lblEclipsePath);
		add(tfEclipsePath, "split 3");
		add(btnLink);
		add(btnUnlink);
		add(descriptionLabel);
		add(descriptionTextArea);
	}

	public void updateComponents() {
		tfLabel.setText(businessSubsystem.getLabel());
		if (businessSubsystem.nestingPackage() != null){
			remove(lblLabelToCode);
			remove(chLabelToCode);
			remove(lblEclipsePath);
			remove(tfEclipsePath);
			remove(btnLink);
			remove(btnUnlink);
			remove(descriptionLabel);
			remove(descriptionTextArea);
		}
		else{
			remove(lblLabelToCode);
			remove(chLabelToCode);
			remove(lblLabel);
			remove(lblLabel);
			remove(lblEclipsePath);
			remove(tfEclipsePath);
			remove(btnLink);
			remove(btnUnlink);
			remove(descriptionLabel);
			remove(descriptionTextArea);
			add(lblLabel);
			add(tfLabel);
			add(lblLabelToCode);
			add(chLabelToCode);
			add(lblEclipsePath);
			add(tfEclipsePath, "split 3");
			add(btnLink);
			add(btnUnlink);
			add(descriptionLabel);
			add(descriptionTextArea);
		}
		chLabelToCode.setSelected(businessSubsystem.isLabelToCode());
		if(businessSubsystem.getEclipseProjectPath() != null) {
			tfEclipsePath.setText(businessSubsystem.getEclipseProjectPath().getAbsolutePath());
			btnUnlink.setVisible(true);
		}else{
			tfEclipsePath.setText("");
			btnUnlink.setVisible(false);
		}
		if(businessSubsystem.getProjectDescription() != null) {
			descriptionTextArea.setText(businessSubsystem.getProjectDescription());
		}else {
			descriptionTextArea.setText("This application is a prototype generated from KROKI specification. Please log in to continue.");
		}
	}


	private void addActions(){
		tfLabel.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				contentChanged(e);
			}

			public void removeUpdate(DocumentEvent e) {
				contentChanged(e);
			}

			public void changedUpdate(DocumentEvent e) {
				//nista se ne desava
			}

			private void contentChanged(DocumentEvent e) {
				Document doc = e.getDocument();
				String text = "";
				try {
					text = doc.getText(0, doc.getLength());
				} catch (BadLocationException ex) {
				}
				businessSubsystem.setLabel(text);
				updatePreformedIncludeTree();
			}
		});


		chLabelToCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox src = (JCheckBox) e.getSource();
				boolean value = src.isSelected();
				businessSubsystem.setLabelToCode(value);
				System.out.println(businessSubsystem.isLabelToCode());
				updatePreformed();
			}
		});
		
		btnUnlink.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				businessSubsystem.setEclipseProjectPath(null);
				updatePreformed();
				tfEclipsePath.setText("");
				btnUnlink.setVisible(false);
			}
		});
		
		
		btnLink.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retValue = jfc.showDialog(null, "Select project");
				if (retValue == JFileChooser.APPROVE_OPTION) {
					File newDir = jfc.getSelectedFile();
					if(checkDirectory(newDir)) {
						businessSubsystem.setEclipseProjectPath(newDir);
						tfEclipsePath.setText(newDir.getAbsolutePath());
						btnUnlink.setVisible(true);
						updatePreformed();
					}
				}
			}
		});
		descriptionTextArea.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				String newDescription = descriptionTextArea.getText();
				if(!newDescription.equals("")) {
					businessSubsystem.setProjectDescription(newDescription);
					updatePreformed();
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
	}


	@Override
	public void updatePreformed() {
		businessSubsystem.update();
		settingsCreator.settingsPreformed();
	}

	@Override
	public void updatePreformedIncludeTree() {
		businessSubsystem.update();
		settingsCreator.settingsPreformed();
	}

	@Override
	public void updateSettings(VisibleElement visibleElement) {
		this.businessSubsystem = (BussinesSubsystem)visibleElement;
		updateComponents();

	}
	
	/**
	 * Checks if selected directory is exported Eclipse project directory
	 * It should contain 'ApplicationRepository' and 'WebApp' directories
	 * WebApp should have the '.project' file.
	 * If the specified directory does not comply to this, user can select another directory which is also checked. 
	 */
	// TODO This should be transfered to API project
	public boolean checkDirectory(File dir) {
		boolean ok = false;
		System.out.println("[ECLIPSE PROJECT CHECK] Checking project dir: " + dir.getAbsolutePath());
		if(dir.exists()) {
			System.out.println("[ECLIPSE PROJECT CHECK] Project directory OK.");
			File repoDir = new File(dir.getAbsolutePath() + File.separator + "ApplicationRepository");
			File appDir = new File(dir.getAbsolutePath() + File.separator + "WebApp");
			System.out.println("[ECLIPSE PROJECT CHECK] Checking the ApplicationRepository folder at: " + repoDir.getAbsolutePath());
			if(repoDir.exists()) {
				System.out.println("[ECLIPSE PROJECT CHECK] ApplicationRepository found.");
				System.out.println("[ECLIPSE PROJECT CHECK] Checking the WebApp folder at: " + appDir.getAbsolutePath());
				if(appDir.exists()) {
					System.out.println("[ECLIPSE PROJECT CHECK] WebApp found");
					File projFile = new File(appDir.getAbsolutePath() + File.separator + ".project");
					System.out.println("[ECLIPSE PROJECT CHECK] Checking the .project file in: " + projFile.getAbsolutePath());
					if(projFile.exists()) {
						System.out.println("[ECLIPSE PROJECT CHECK] .project file found!");
						System.out.println("Eclipse directory Ok.");
						return true;
					}else {
						System.out.println("[ECLIPSE PROJECT CHECK] WebApp does not contain Eclipse .project file!");
						ok = false;
					}
				}else {
					System.out.println("[ECLIPSE PROJECT CHECK] Cannot fint the associated WebApp location!");
					ok =false;
				}
			}else {
				System.out.println("[ECLIPSE PROJECT CHECK] Cannot find the associated ApplicationRepository location!");
				ok =false;
			}
		}
		JOptionPane.showMessageDialog(null, "Existing export directory is not valid. Please select a new one.");
		// If the function has not returned yet, choose another folder and check it
		System.out.println("[ECLIPSE PROJECT CHECK] Directory check failed! Specifying a new one...");
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int retValue = jfc.showDialog(null, "Select project");
		if(retValue == JFileChooser.APPROVE_OPTION) {
			File newDir = jfc.getSelectedFile();
			checkDirectory(newDir);
		}
		return ok;
	}
	
}
