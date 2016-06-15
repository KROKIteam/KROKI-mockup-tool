package kroki.app.gui.console;

import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.shape.Box;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.tools.ant.taskdefs.optional.depend.JarFileIterator;

import kroki.resources.images.*;

import org.eclipse.uml2.uml.ObjectNodeOrderingKind;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.validation.IEValidatorProvider.UML;

import antlr.StringUtils;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import freemarker.template.utility.StringUtil;
import graphedit.model.components.Link;
import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.toolbar.StyleToolbar;
import kroki.app.model.Workspace;
import kroki.app.utils.ImageResource;
import kroki.commons.camelcase.NamingUtil;
import kroki.mockup.model.Component;
import kroki.mockup.model.Composite;
import kroki.mockup.model.layout.VerticalLayoutManager;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.panel.mode.OperationMode;
import kroki.profil.panel.mode.ViewMode;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.ElementsGroupUtil;
import kroki.profil.utils.ParentChildUtil;
import kroki.profil.utils.StandardPanelUtil;
import kroki.profil.utils.UIPropertyUtil;
import kroki.uml_core_basic.UmlPackage;
import kroki.app.gui.dialog.KrokiMockupToolAboutDialog;//poziv za about prikaz, prikaz o applikaciji
import kroki.app.action.ExitAction;
import kroki.app.action.UndoAction;//poziv za undo akciju
import kroki.app.action.RedoAction;//poziv za redo akciju
import kroki.app.action.OpenProjectAction;
import kroki.app.controller.TabbedPaneController;
import kroki.app.gui.console.HelpText;

import java.awt.event.*;

import javax.swing.*;

import java.io.*;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

//////////////

/**
 * GUI component that simulates console behavior
 * @author Milorad Filipovic <milorad.filipovic19@gmail.com>
 * https://github.com/MiloradFilipovic/JCommandPanel
 */
public class CommandPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * TextArea that contains previously entered user commands
	 * and application output text (not editable)
	 */
	private JTextPane previousLines;
	private JScrollPane consoleScroll;
	
	private JToolBar consoleToolbar;
	private JButton btnLoad, btnSave;
	/**
	 * TextField used for command typing
	 */
	private JTextField currentLine;
	/**
	 * Command history
	 */
	private ArrayList<String> listory;
	/**
	 * Current command index in history list
	 */
	private int commandInex;
	
	/**
	 * Command tab opetions
	 */
	private ArrayList<String> tabHelpOption;
	private ArrayList<String> tempTabHelpOption;
	
	private ArrayList<String> tabMakeOption;
	private ArrayList<String> tempTabMakeOption;
	private ArrayList<String> tabComponentMakeOption;
	private ArrayList<String> tempTabComponentMakeOption;
	private ArrayList<String> tabPackageOption;
	private ArrayList<String> tabStartOption;
	private ArrayList<String> tempTabStartOption;
	private ArrayList<String> tempTabPackageOption;
	//	private ArrayList<String> tabAddComponentsOption;
	
	//elementi od komponeti kao sto su labela, visible i ostalo
	private ArrayList<String> panelElements;
	private int panelElementsIndex;
	
	private ArrayList<String> textfieldElements;
	private int textfieldElementsIndex;
	
	private ArrayList<String> textareaElements;
	private int textareaElementsIndex;
	
	private ArrayList<String> comboboxElements;
	private int comboboxElementsIndex;
	
	private ArrayList<String> checkboxElements;
	private int checkboxElementsIndex;
	
	private ArrayList<String> reportElements;
	private int reportElementsIndex;
	
	private ArrayList<String> transactionElements;
	private int transactionElementsIndex;
	
	private ArrayList<String> linkElements;
	private int linkElementsIndex;
	
	private ArrayList<String> hierarchyElement;
	private int hierarchyElementIndex;
	
	private ArrayList<String> combozoomElements;
	private int combozoomElementsIndex;
	
	private ArrayList<String> commonElements;
	private int commonElementsIndex;
	
	/*
	 *  index for tab options
	 * */
	private int startOptionTabIndex;
	private int commandTabHelpOptionIndex;	
	private int commandMakeOptionIndex;	
	private static int tabIndex;
	private static String tabName;
	private int componentMakeTabOptionIndex;
	public int commaIndexComponent;
	
	private List<UmlPackage> projectList;
	private int countProjectWorkspace;
	private int componentPackageTabOptionIndex;
	private UmlPackage projekat;
	private Boolean postoji;
	private Boolean componentExist;
	private Boolean notEmptyTabPackageOption;
	private Boolean kreiranElement;

	private static ArrayList<String> allLabelValues;
	
//	private int commandAddComponentsOptionIndex;

	public CommandPanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		allLabelValues = new ArrayList<String>();
		
		tabHelpOption = new ArrayList<String>();
		tempTabHelpOption = new ArrayList<String>();
		
		tabMakeOption = new ArrayList<String>();
		tempTabMakeOption = new ArrayList<String>();
		
		tabComponentMakeOption = new ArrayList<String>();
		tempTabComponentMakeOption = new ArrayList<String>();
		
		tabPackageOption = new ArrayList<String>();
		
		tabStartOption = new ArrayList<String>();
		tempTabStartOption = new ArrayList<String>();
		
		projekat = null;
		postoji = false;
		componentExist = false;
		notEmptyTabPackageOption = false;
		kreiranElement = false;
		
		commonElements = new ArrayList<String>();
		commonElements.add("Vislible");
		commonElements.add("BackgroundColor");
		commonElements.add("FontColor");
		commonElements.add("DisplayFormat");
		commonElements.add("Mandatory");
		commonElements.add("Representative");
		commonElements.add("AutoGo");
		commonElements.add("Disabled");
		commonElements.add("DefaultValue");
		commonElements.add("LabelToCode");
		commonElements.add("ColumnName");
		commonElements.add("PersistentType");
		commonElements.add("Length");
		commonElements.add("Precision");
		commonElementsIndex = 0;
		
		panelElements = new ArrayList<String>();
		panelElements.add("Label");
		panelElements.add("Visible");
		panelElements.add("BackgroundColor");
		panelElements.add("ForegroundColor");
		panelElements.add("FontColor");
		panelElements.add("BorderColor");
		panelElements.add("Orientation");
		panelElements.add("Alignment");
		panelElementsIndex = 0;
		
		textfieldElements = new ArrayList<String>();
		textfieldElements.add("Label");
		textfieldElements.add("ElementName");
		textfieldElements.add("DataType");
		textfieldElements.add("TextFieldDataType");
		textfieldElements.addAll(commonElements);
		textfieldElementsIndex = 0;
		
		textareaElements = new ArrayList<String>();
		textareaElements.add("Label");
		textareaElements.add("ElementName");
		textareaElements.addAll(commonElements);
		textareaElementsIndex = 0;
		
		comboboxElements = new ArrayList<String>();
		comboboxElements.add("Label");
		comboboxElements.add("ElementName");
		comboboxElements.add("Values");
		comboboxElements.addAll(commonElements);
		comboboxElementsIndex = 0;
		
		checkboxElements = new ArrayList<String>();
		checkboxElements.add("Label");
		checkboxElements.add("ElementName");
		checkboxElements.addAll(commonElements);
		checkboxElementsIndex = 0;
		
		reportElements = new ArrayList<String>();
		reportElements.add("Label");
		reportElements.add("ElementName");
		reportElements.add("Visible");
		reportElements.add("BackgroundColor");
		reportElements.add("ForegroundColor");
		reportElements.add("FontColor");
		reportElements.add("HasParametersForm");
		reportElements.add("FilteredByKey");
		reportElements.add("PersistentOperation");
		reportElements.add("RefreshNow");
		reportElements.add("RefreshAll");
		reportElements.add("AskConfirmation");
		reportElements.add("ShowErrors");
		reportElements.add("ConfirmationMessage");
		reportElementsIndex = 0;
		
		transactionElements = new ArrayList<String>();
		transactionElements.add("Label");
		transactionElements.add("ElementName");
		transactionElements.add("Visible");
		transactionElements.add("BackgroundColor");
		transactionElements.add("ForegroundColor");
		transactionElements.add("FontColor");
		transactionElements.add("HasParametersForm");
		transactionElements.add("HasParametersForm");
		transactionElements.add("PersistentOperation");
		transactionElements.add("RefreshNow");
		transactionElements.add("RefreshAll");
		transactionElements.add("AskConfirmation");
		transactionElements.add("ShowErrors");
		transactionElements.add("ConfirmationMessage");
		transactionElementsIndex = 0;
		
		linkElements = new ArrayList<String>();
		linkElements.add("Label");
		linkElements.add("ElementName");
		linkElements.add("Visible");
		linkElements.add("BackgroundColor");
		linkElements.add("ForegroundColor");
		linkElements.add("FontColor");
		linkElements.add("Add");
		linkElements.add("Update");
		linkElements.add("Copy");
		linkElements.add("Delete");
		linkElements.add("Search");
		linkElements.add("ChangeMode");
		linkElements.add("DataNavigation");
		linkElements.add("Mandatory");
		linkElements.add("DefaultViewMode");
		linkElements.add("DefaultOperationMode");
		linkElements.add("ConfirmDelete");
		linkElements.add("StayInAddMode");
		linkElements.add("GoToLastAdded");
		linkElements.add("DataFilter");
		linkElements.add("SortBy");
		linkElements.add("TargetPanel");
		linkElements.add("TargetZoom");
		linkElements.add("Position");
		linkElements.add("AutoActivate");
		linkElements.add("DisplayRepresentative");
		linkElements.add("DisplayIdentifier");
		linkElementsIndex = 0;
		
		hierarchyElement = new ArrayList<String>();
		hierarchyElement.add("Label");
		hierarchyElement.add("ElementName");
		hierarchyElement.add("Visible");
		hierarchyElement.add("BackgroundColor");
		hierarchyElement.add("ForegroundColor");
		hierarchyElement.add("FontColor");
		hierarchyElement.add("Add");
		hierarchyElement.add("Update");
		hierarchyElement.add("Copy");
		hierarchyElement.add("Delete");
		hierarchyElement.add("Search");
		hierarchyElement.add("ChangeMode");
		hierarchyElement.add("DataNavigation");
		hierarchyElement.add("Mandatory");
		hierarchyElement.add("DefaultViewMode");
		hierarchyElement.add("DefaultOperationMode");
		hierarchyElement.add("ConfirmDelete");
		hierarchyElement.add("StayInAddMode");
		hierarchyElement.add("GoToLastAdded");
		hierarchyElement.add("DataFilter");
		hierarchyElement.add("SortBy");
		hierarchyElement.add("ActivationPanel");
		hierarchyElement.add("TargetPanel");
		hierarchyElement.add("AppliedToPanel");
		hierarchyElement.add("HierarchyPanel");
		hierarchyElementIndex = 0;
		
		combozoomElements = new ArrayList<String>();
		combozoomElements.add("Label");
		combozoomElements.add("ElementName");
		combozoomElements.add("Visible");
		combozoomElements.add("BackgroundColor");
		combozoomElements.add("ForegroundColor");
		combozoomElements.add("FontColor");
		combozoomElements.add("Add");
		combozoomElements.add("Update");
		combozoomElements.add("Copy");
		combozoomElements.add("Delete");
		combozoomElements.add("Search");
		combozoomElements.add("ChangeMode");
		combozoomElements.add("DataNavigation");
		combozoomElements.add("Mandatory");
		combozoomElements.add("DefaultViewMode");
		combozoomElements.add("DefaultOperationMode");
		combozoomElements.add("ConfirmDelete");
		combozoomElements.add("StayInAddMode");
		combozoomElements.add("GoToLastAdded");
		combozoomElements.add("DataFilter");
		combozoomElements.add("SortBy");
		combozoomElements.add("ActivationPanel");
		combozoomElements.add("TargetPanel");
		combozoomElementsIndex = 0;
	
		//projectList = new ArrayList<UmlPackage>();
			
		countProjectWorkspace = 0;
		commaIndexComponent = 0;
		
		tempTabStartOption.add("make");
		tempTabStartOption.add("help");
		tempTabStartOption.add("udno");
		tempTabStartOption.add("redo");
		tempTabStartOption.add("clear");
		tempTabStartOption.add("about");
		tempTabStartOption.add("exit");
		
		tabStartOption = tempTabStartOption;
		startOptionTabIndex = 0;
		
		/* sad videti rename i delete
		tabStartOption.add("make");
		tabStartOption.add("make");
		*/
		
		//help tab option
		tempTabHelpOption.add("help make project");
		tempTabHelpOption.add("help make package");
		tempTabHelpOption.add("help make std-panel");
		tempTabHelpOption.add("help make parent child panel");
		tempTabHelpOption.add("help make combozoom");
		tempTabHelpOption.add("help make hierarchy");
		tempTabHelpOption.add("help rename project");// Project
		tempTabHelpOption.add("help rename component");
		tempTabHelpOption.add("help undo");
		tempTabHelpOption.add("help redo");
		tempTabHelpOption.add("help clear");
		tempTabHelpOption.add("help about");
		tempTabHelpOption.add("help exit");
		
		tabHelpOption = tempTabHelpOption;
		commandTabHelpOptionIndex = 0;
		
		//////////////////////////////////////////////////////////////////////
		
		//make option tab
		tempTabMakeOption.add("make project");
		tempTabMakeOption.add("make package");
		tempTabMakeOption.add("make std-panel");
		tempTabMakeOption.add("make parent-child panel");
		tempTabMakeOption.add("make component");
		tempTabMakeOption.add("make combozoom");
		tempTabMakeOption.add("make hierarchy");
		
		tabMakeOption = tempTabMakeOption;
		commandMakeOptionIndex = 0;	
		
		///////////////////////////////////////////////////////////////////////
		
		//component tab option
		tempTabComponentMakeOption.add("textfield");
		tempTabComponentMakeOption.add("textarea");
		tempTabComponentMakeOption.add("combobox");
		tempTabComponentMakeOption.add("report");
		tempTabComponentMakeOption.add("transaction");
		tempTabComponentMakeOption.add("link");
		
		tabComponentMakeOption = tempTabComponentMakeOption;
		componentMakeTabOptionIndex = 0;
		
		/////////////////////////////////////////////////////////////////////////
		
		componentPackageTabOptionIndex = 0;
		listory = new ArrayList<String>();

		initGUI();
	}

	public void initGUI() {

		previousLines = new JTextPane();
		previousLines.setFont(new Font("Monospaced",Font.PLAIN,12));
		previousLines.setEditable(false);

		displayText("Type a command or help to begin.", OutputPanel.KROKI_RESPONSE);
		
		consoleScroll = new JScrollPane(previousLines);

		currentLine = new JTextField();
		currentLine.setFont(new Font("Monospaced",Font.PLAIN,14));
		
		//*********************************************************************************
		//                                         								  LISTENERS
		//*********************************************************************************
		previousLines.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				//when user clicks on textArea, focus is transfered to textField
				currentLine.requestFocusInWindow();

				
			}
		});
		
		currentLine.setFocusTraversalKeysEnabled(false); //enable tab, see if 
		
		currentLine.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
								
				projectList = KrokiMockupToolApp.getInstance().getWorkspace().getPackageList();
				
				//if ENTER key is pressed inside text box, command is read and parsed
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!currentLine.getText().equals("")) {
						displayText(currentLine.getText(), OutputPanel.KROKI_USER_ECHO);
						String response = parseCommand(currentLine.getText());
						if(response == null) {
							response = "Type a command or 'help' to begin.";
						}
						displayText(response, OutputPanel.KROKI_RESPONSE);
						listory.add(currentLine.getText());
						commandInex = listory.size();
						currentLine.setText("");
					}
					
					//if user presses UP key inside textBox
					//decrease command index, and display command on that position in history
				}else if(arg0.getKeyCode() == KeyEvent.VK_UP) {
					if(!listory.isEmpty()) {
						if(commandInex > 0) {
							commandInex--;
						}
						
						if(commandMakeOptionIndex > 0) {
							commandMakeOptionIndex--;
						}
						if (commandTabHelpOptionIndex>0) {
							commandTabHelpOptionIndex--;
						}
						//tako za svako dodavanje elemenata,projekata itd
						currentLine.setText(listory.get(commandInex));
					}
					//if DOWN key is pressed inside textBox
					//increase command index and disp
				}else if(arg0.getKeyCode() == KeyEvent.VK_DOWN) {
					if(!listory.isEmpty()) {
						if(commandInex < listory.size()-1) {
							commandInex++;
							currentLine.setText(listory.get(commandInex));
						}else if (commandInex == listory.size()-1) {
							//if last command in list is reached, after pressing DOWN key, empty command is displayed
							commandInex = listory.size();
							currentLine.setText("");
						}
					}
				} else if (arg0.getKeyCode() >= KeyEvent.VK_0 && arg0.getKeyCode() <= KeyEvent.VK_9 || 
						arg0.getKeyCode() >= KeyEvent.VK_A && arg0.getKeyCode() <= KeyEvent.VK_Z) {
					kreiranElement = false;
				}else if (arg0.getKeyCode() == KeyEvent.VK_SPACE){
					commandMakeOptionIndex = 0;
					tabMakeOption = tempTabMakeOption;
					
					commandTabHelpOptionIndex = 0;
					tabHelpOption = tempTabHelpOption;
					
					kreiranElement = false;					
				} else if (arg0.getKeyCode() == KeyEvent.VK_COMMA) {
					componentMakeTabOptionIndex = 0;
					tabComponentMakeOption = tempTabComponentMakeOption;
					kreiranElement = false;
				} else if (arg0.getKeyCode() == KeyEvent.VK_QUOTE){
					kreiranElement = false;
					
					countProjectWorkspace = 0;
					
					tabPackageOption = tempTabPackageOption;
					componentPackageTabOptionIndex = 0;
				} else if (arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
								
					if (currentLine.getText().endsWith("\"")) {
						kreiranElement = false;
					} else if (currentLine.getText().endsWith("{")) {
						kreiranElement = false;
					} else if (currentLine.getText().endsWith(";")) {
						kreiranElement = false;
						tabComponentMakeOption = tempTabComponentMakeOption;
						componentMakeTabOptionIndex = 0;
					} else if (currentLine.getText().endsWith("/")) {
						kreiranElement = false;
					} else if (currentLine.getText().isEmpty() || !in_array(currentLine.getText(), tempTabStartOption)) {
						startOptionTabIndex = 0;
						tabStartOption = tempTabStartOption;
						kreiranElement = false;
					} else if (currentLine.getText().endsWith(" ")){
						commandMakeOptionIndex = 0;
						tabMakeOption = tempTabMakeOption;
						
						commandTabHelpOptionIndex = 0;
						tabHelpOption = tempTabHelpOption;
						
						kreiranElement = false;
					}
					
					if (currentLine.getText().endsWith(" {")  || currentLine.getText().endsWith(";")) {
						componentMakeTabOptionIndex = 0;
					} else if(currentLine.getText().startsWith("make component {")){
						String component = currentLine.getText().substring(currentLine.getText().lastIndexOf(";") + 1);
						
						if(tabComponentMakeOption.contains(component)){
							for (int i=0;i<tabComponentMakeOption.size();i++) {
								if (tabComponentMakeOption.get(i).equalsIgnoreCase(component)) {
									componentMakeTabOptionIndex = i+1;
								}
							}
						}
					}
					
					
					
					if(currentLine.getText().endsWith("/")){
						makeTabPackageOption();						
					} else {
						
						if(projekat != null && postoji) {
							
							//ovo jos videti kako da prebaci kada izbirisem / i onda na sledeci paket
							String sad = currentLine.getText();
							String segments[] = sad.split("/");
							
							tabPackageOption = new ArrayList<String>();
							
							if(segments.length == 2){
							
								String pack = segments[segments.length-1];
								String projssss = projekat.toString();
								
								BussinesSubsystem projekatOwner = KrokiMockupToolApp.getInstance().findProject(projssss);
								
								if (projekatOwner != null && projekatOwner.ownedElementCount() > 0 ) {
									for (int j=0;j<projekatOwner.ownedElementCount(); j++) {
										if (projekatOwner.getOwnedElementAt(j) instanceof BussinesSubsystem) {
											tabPackageOption.add(projekatOwner.getOwnedElementAt(j).toString());
										}
									}
								}
								
								for (int i=0;i<tabPackageOption.size();i++) {
									if(tabPackageOption.get(i).equalsIgnoreCase(pack)){
										componentPackageTabOptionIndex = i + 1;
										break;
									}
								}
								
							} else if (segments.length > 2) {
	
								String packString = segments[segments.length-1];
								String projectString = segments[segments.length-2];
								
								BussinesSubsystem owner = findNode(projectString, true);
								
								if(owner != null && owner.ownedElementCount() > 0){								
									
									for (int j=0;j<owner.ownedElementCount(); j++) {
										if (owner.getOwnedElementAt(j) instanceof BussinesSubsystem) {
											tabPackageOption.add(owner.getOwnedElementAt(j).getLabel());
										}
									}
								}
								
								for (int i=0;i<tabPackageOption.size();i++) {
									if(tabPackageOption.get(i).equalsIgnoreCase(packString)){
										componentPackageTabOptionIndex = i + 1;
										break;
									}
								}
							}
							if (tabPackageOption.size() > 0) {
								notEmptyTabPackageOption = true;
							} else {
								notEmptyTabPackageOption = false;
								tabPackageOption = tempTabPackageOption;
							}
						}
					}
				} else if (arg0.getKeyCode() == KeyEvent.VK_SLASH) {
					
					makeTabPackageOption();
					kreiranElement = false;
					
				} else if (arg0.getKeyCode() == KeyEvent.VK_TAB) { // akcija gde cemo da proveravamo sa tabom kuda da ide sve	
					
					if (!kreiranElement && currentLine.getText() != "") {
						
						//int poslednji = currentLine.getText().lastIndexOf(currentLine.getText().substring(currentLine.getText().length()-1));
						int navodnik = currentLine.getText().lastIndexOf("in \"");
						int kosaCrta = currentLine.getText().lastIndexOf("/");
						int prazno = currentLine.getText().lastIndexOf(" ");
						int zagrada = currentLine.getText().lastIndexOf("{");
						int zarez = currentLine.getText().lastIndexOf(";");
						
						int heighstNumber = Collections.max(Arrays.asList(navodnik, kosaCrta, prazno, zagrada, zarez));
						
						if (heighstNumber == -1) {
							
							if (currentLine.getText().length() != 0 && !in_array(currentLine.getText(), tempTabStartOption)) {
								tabStartOption = new ArrayList<String>();
								
								for (int i=0;i<tempTabStartOption.size();i++) {
									if (tempTabStartOption.get(i).startsWith(currentLine.getText())){
										tabStartOption.add(tempTabStartOption.get(i));
									}
								}
								
								if (tabStartOption.size() >0) {
									startOptionTabIndex=0;
									currentLine.setText(tabStartOption.get(startOptionTabIndex));
									startOptionTabIndex++;								
								}
								
								kreiranElement = true;
							} 
							
					    } else if (navodnik+2 == heighstNumber && navodnik != -1) {
							
					    	String textPosleNavodnika = currentLine.getText();
							String segments[] = textPosleNavodnika.split("in \"");
							String textNavodnikNajveci = segments[segments.length-1];
							
							String textPreNavodnika = textPosleNavodnika.substring(0, navodnik);
							
							if (textNavodnikNajveci.endsWith(" ")) {
								projectList = KrokiMockupToolApp.getInstance().getWorkspace().getPackageList();
							} else {
								List<UmlPackage> projectListTemp = new ArrayList<UmlPackage>();
								projectListTemp = projectList;
								projectList = new ArrayList<UmlPackage>();
								
								for (int i=0;i<projectListTemp.size();i++) {
									String tempProjectName = projectListTemp.get(i).toString();
									if (tempProjectName.startsWith(textNavodnikNajveci)) {
										projectList.add(projectListTemp.get(i));
									}
								}
								
								if (projectList.size() > 0 ) {
									countProjectWorkspace = 0;
									currentLine.setText(textPreNavodnika + "in \"" + projectList.get(countProjectWorkspace).toString());
									countProjectWorkspace++;
								}
							}
							
							if (projectList.size() > 0 ) {
								kreiranElement = true;
							} else {
								kreiranElement = false;
							}
							
						} else if (kosaCrta == heighstNumber) {
							
							tabPackageOption = new ArrayList<String>();	
							String textSegmentFirst = currentLine.getText();
							String segmentsFirst[] = textSegmentFirst.split("/");
							
							if (currentLine.getText().endsWith("/")) {
								if (segmentsFirst.length == 1) {
									String textSegmentSecond = segmentsFirst[segmentsFirst.length-1];
									String segmentsSecond[] = textSegmentSecond.split("\"");
									String projectName = segmentsSecond[segmentsSecond.length-1];
									
									 BussinesSubsystem project = KrokiMockupToolApp.getInstance().findProject(projectName);
									
									if (project != null) {
										for (int i=0;i<project.ownedElementCount();i++) {
											if(currentLine.getText().startsWith("make component")){
												tabPackageOption.add(project.getOwnedElementAt(i).toString());
											} else {
												if (project.getOwnedElementAt(i) instanceof BussinesSubsystem) {
													tabPackageOption.add(project.getOwnedElementAt(i).toString());
												}
											}
										}
									}
									
									if(tabPackageOption.size() > 0){
										componentPackageTabOptionIndex = 0;
										currentLine.setText(textSegmentFirst + tabPackageOption.get(componentPackageTabOptionIndex));
										componentPackageTabOptionIndex++;
									}
								} else if (segmentsFirst.length == 2) {
									
									String packageLabel = segmentsFirst[segmentsFirst.length-1];
									String segmentsSecond[] = segmentsFirst[segmentsFirst.length-2].split("\"");									
									String projectName = segmentsSecond[segmentsSecond.length-1];
									
									BussinesSubsystem project = KrokiMockupToolApp.getInstance().findProject(projectName);
									BussinesSubsystem pack = KrokiMockupToolApp.getInstance().findPackage(packageLabel, project);
									
									if (pack != null) {
										for (int i=0;i<pack.ownedElementCount();i++) {
											if(currentLine.getText().startsWith("make component")){
												tabPackageOption.add(pack.getOwnedElementAt(i).toString());
											} else {
												if (project.getOwnedElementAt(i) instanceof BussinesSubsystem) {
													tabPackageOption.add(pack.getOwnedElementAt(i).toString());
												}
											}
										}
									}
									
									if (tabPackageOption.size() > 0) {									
										componentPackageTabOptionIndex = 0;
										currentLine.setText(textSegmentFirst + tabPackageOption.get(componentPackageTabOptionIndex));
										componentPackageTabOptionIndex++;
									}
								} else {
																	
									String packageName = segmentsFirst[segmentsFirst.length-1];
									
									BussinesSubsystem project =  findNode(packageName, true);
									
									if (project != null) {
										for (int i=0;i<project.ownedElementCount();i++) {
											if(currentLine.getText().startsWith("make component")){
												tabPackageOption.add(project.getOwnedElementAt(i).toString());
											} else {
												if (project.getOwnedElementAt(i) instanceof BussinesSubsystem) {
													tabPackageOption.add(project.getOwnedElementAt(i).toString());
												}
											}
										}
									}
									
									if (tabPackageOption.size() > 0) {
										componentPackageTabOptionIndex = 0;
										currentLine.setText(textSegmentFirst + tabPackageOption.get(componentPackageTabOptionIndex));
										componentPackageTabOptionIndex++;
									}
								}
								
								if (tabPackageOption.size() > 0) {
									
								}
								
								System.out.println(tabPackageOption.toString());
							} else {
								if (segmentsFirst.length == 1 && !kreiranElement) {
									
									String textSegmentSecond = segmentsFirst[segmentsFirst.length-1];
									String segmentsSecond[] = textSegmentSecond.split("\"");
									
									String projectName = segmentsSecond[segmentsSecond.length-1];
									
									BussinesSubsystem project = KrokiMockupToolApp.getInstance().findProject(projectName);
									
									if (project != null) {
										for (int i=0;i<project.ownedElementCount();i++) {
											if(currentLine.getText().startsWith("make component")){
												tabPackageOption.add(project.getOwnedElementAt(i).toString());
											} else {
												if (project.getOwnedElementAt(i) instanceof BussinesSubsystem) {
													tabPackageOption.add(project.getOwnedElementAt(i).toString());
												}
											}
										}
									}
									
									componentPackageTabOptionIndex = 0;
									
								} else if (segmentsFirst.length ==2 && !kreiranElement) {
									
									String packageName = segmentsFirst[segmentsFirst.length-1];
									String projectName = segmentsFirst[segmentsFirst.length-2];
									
									if (projectName.contains("/")) {
										BussinesSubsystem project = KrokiMockupToolApp.getInstance().findProject(projectName);
										
										if (project != null) {
											for (int i=0;i<project.ownedElementCount();i++) {
												if(currentLine.getText().startsWith("make component")){
													if(project.getOwnedElementAt(i).toString().startsWith(packageName)){
														tabPackageOption.add(project.getOwnedElementAt(i).toString()); 
													}
												} else {
													if (project.getOwnedElementAt(i) instanceof BussinesSubsystem) {
														if(project.getOwnedElementAt(i).toString().startsWith(packageName)){
															tabPackageOption.add(project.getOwnedElementAt(i).toString());
														}
													}
												}
											}
										}
									} else {
										String textSegmentSecond = segmentsFirst[segmentsFirst.length-2];
										String segmentsSecond[] = textSegmentSecond.split("\"");
										
										String projectNam = segmentsSecond[segmentsSecond.length-1];
										
										BussinesSubsystem project = KrokiMockupToolApp.getInstance().findProject(projectNam);
										
										if (project != null) {
											for (int i=0;i<project.ownedElementCount();i++) {
												if(currentLine.getText().startsWith("make component")){
													if(project.getOwnedElementAt(i).toString().startsWith(packageName)){
														tabPackageOption.add(project.getOwnedElementAt(i).toString()); 
													}
												} else {
													if (project.getOwnedElementAt(i) instanceof BussinesSubsystem) {
														if(project.getOwnedElementAt(i).toString().startsWith(packageName)){
															tabPackageOption.add(project.getOwnedElementAt(i).toString());
														}
													}
												}
											}
										}
									}
									
									if (tabPackageOption.size() > 0 ){
										
										componentPackageTabOptionIndex = 0;
										currentLine.setText(projectName + "/" + tabPackageOption.get(componentPackageTabOptionIndex));
										componentPackageTabOptionIndex++;
									}
									
								} else if (segmentsFirst.length > 2 && !kreiranElement) {
									
									String text = currentLine.getText();
									int endIndex = text.lastIndexOf("/");
									
								    if (endIndex != -1) {
								    	text = text.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
								    	text = text + "/";
								    }
									
									String packageName = segmentsFirst[segmentsFirst.length-1];
									String projectName = segmentsFirst[segmentsFirst.length-2];
									
									BussinesSubsystem project =  findNode(projectName, true);
									
									if (project != null) {
										for (int i=0;i<project.ownedElementCount();i++) {
											if(currentLine.getText().startsWith("make component")){
												if(project.getOwnedElementAt(i).toString().startsWith(packageName)){
													tabPackageOption.add(project.getOwnedElementAt(i).toString()); 
												}
											} else {
												if (project.getOwnedElementAt(i) instanceof BussinesSubsystem) {
													if(project.getOwnedElementAt(i).toString().startsWith(packageName)){
														tabPackageOption.add(project.getOwnedElementAt(i).toString());
													}
												}
											}
										}
									}
									
									if (tabPackageOption.size() > 0 ){
										
										componentPackageTabOptionIndex = 0;
										currentLine.setText(text + tabPackageOption.get(componentPackageTabOptionIndex));
										componentPackageTabOptionIndex++;
									}
									
								}
							}
							
							
							if (tabPackageOption.size() > 0) {
								kreiranElement = true;
							} else {
								kreiranElement = false;
							}
						} else if (prazno == heighstNumber) {
							
							if (currentLine.getText().startsWith("make")) {
								
								tabMakeOption = new ArrayList<String>();
								
								for (int i=0;i<tempTabMakeOption.size();i++) {
									if (tempTabMakeOption.get(i).startsWith(currentLine.getText())) {
										tabMakeOption.add(tempTabMakeOption.get(i));
									}
								}
								
								if(tabMakeOption.size() > 0){
									commandMakeOptionIndex = 0;
									currentLine.setText(tabMakeOption.get(commandMakeOptionIndex));
									//commandMakeOptionIndex++;
								}
								
							} else if (currentLine.getText().startsWith("help")) {
								
								tabHelpOption = new ArrayList<String>();
								
								for (int i=0;i<tempTabHelpOption.size();i++){
									if (tempTabHelpOption.get(i).startsWith(currentLine.getText())) {
										tabHelpOption.add(tempTabHelpOption.get(i));
									}
								}
								
								if (tabHelpOption.size() > 0) {
									commandTabHelpOptionIndex = 0;
									currentLine.setText(tabHelpOption.get(commandTabHelpOptionIndex));
									//commandTabHelpOptionIndex++;
								}
							}	
							
							kreiranElement = true;
												
						} else if (zagrada == heighstNumber && zagrada != -1) {
							
					    	String textPosleNavodnika = currentLine.getText();
							String segments[] = textPosleNavodnika.split("\\{");
							String textNavodnikNajveci = segments[segments.length-1];
							
							String textPreNavodnika = textPosleNavodnika.substring(0, zagrada);
							
							if (textNavodnikNajveci.startsWith("make")) {
								componentMakeTabOptionIndex = 0;
								tabComponentMakeOption = tempTabComponentMakeOption;
							} else {
								
								tabComponentMakeOption = new ArrayList<String>();
								
								for (int i=0;i<tempTabComponentMakeOption.size();i++) {
									if (tempTabComponentMakeOption.get(i).startsWith(textNavodnikNajveci)) {
										tabComponentMakeOption.add(tempTabComponentMakeOption.get(i));
									}
								}
								
								if (tabComponentMakeOption.size() > 0) {
									componentMakeTabOptionIndex = 0;
									currentLine.setText(textPreNavodnika + "{" + tabComponentMakeOption.get(componentMakeTabOptionIndex).toString());
									//componentMakeTabOptionIndex++;
								}
							}
							
							kreiranElement = true;
							
							
						} else if (zarez == heighstNumber && zarez != -1) {
							
					    	String textPosleNavodnika = currentLine.getText();
							String segments[] = textPosleNavodnika.split(";");
							String textNavodnikNajveci = segments[segments.length-1];
							
							String textPreNavodnika = textPosleNavodnika.substring(0, zarez);
							
							if (textPosleNavodnika.endsWith(";")) {
								
								componentMakeTabOptionIndex = 0;
								tabComponentMakeOption = tempTabComponentMakeOption;
							} else {
								ArrayList<String> tempCompoenentMakeOption = new ArrayList<String>();
								tempCompoenentMakeOption = tempTabComponentMakeOption;
								tabComponentMakeOption = new ArrayList<String>();

								for (int i=0;i<tempCompoenentMakeOption.size();i++) {
									String tempProjectName = tempCompoenentMakeOption.get(i);
									if (tempProjectName.startsWith(textNavodnikNajveci)) {
										tabComponentMakeOption.add(tempCompoenentMakeOption.get(i));
									}
								}

								if (tabComponentMakeOption.size() > 0) {
									componentMakeTabOptionIndex = 0;
									currentLine.setText(textPreNavodnika + ";" + tabComponentMakeOption.get(componentMakeTabOptionIndex).toString());
									componentMakeTabOptionIndex++;
								}
							}
							
							kreiranElement = true;
						} 
					}
					
					if (projectList.size() == 0 && currentLine.getText().startsWith("make") && currentLine.getText().endsWith(" in \"")) {
						displayText("Import existing or create new project.", OutputPanel.KROKI_RESPONSE);
					}
					
					if (projekat != null && currentLine.getText().endsWith(projekat.toString() + "/")) {
						postoji = false;
					}
					
					if(currentLine.getText().trim().equals("") || 
							in_array(currentLine.getText(), tabStartOption)){
						
						if(startOptionTabIndex < tabStartOption.size()){
							currentLine.setText(tabStartOption.get(startOptionTabIndex));
							startOptionTabIndex++;
							if(startOptionTabIndex == tabStartOption.size()){
								startOptionTabIndex = 0;
							}
						}
						
					}
					
					//help actions
					//proveriti jos
					if(currentLine.getText().endsWith("help ") ||
							in_array(currentLine.getText(), tabHelpOption)) {
												
						if (commandTabHelpOptionIndex < tabHelpOption.size()){
							
							currentLine.setText(tabHelpOption.get(commandTabHelpOptionIndex));
							listory.add(currentLine.getText());
							commandInex = listory.size();
							commandTabHelpOptionIndex++;
							
							if (commandTabHelpOptionIndex == tabHelpOption.size()) {
								commandTabHelpOptionIndex = 0;
							}
							
						}
					}

					//make actions
					//proveriti jos
					if(currentLine.getText().endsWith("make ") || 
							in_array(currentLine.getText(), tabMakeOption)){
						
						//proveriti zasto ovo
						componentExist = false;
						
						if (commandMakeOptionIndex < tabMakeOption.size()) {
							currentLine.setText(tabMakeOption.get(commandMakeOptionIndex));
							listory.add(currentLine.getText());
							commandInex = listory.size();
							
							commandMakeOptionIndex++;
							if(commandMakeOptionIndex == tabMakeOption.size()){
								commandMakeOptionIndex = 0;
							}
						}
					}
					
					///////////////////
					//dodavanje komponenti
					////////////////////////////////////
					///provirit uslove jos
					/////
					if(currentLine.getText().endsWith("component {") || 
							currentLine.getText().endsWith(";") ||
							componentExist && componentMakeTabOptionIndex > 0 && currentLine.getText().endsWith(";"+tabComponentMakeOption.get(componentMakeTabOptionIndex-1)) ||
							componentMakeTabOptionIndex > 0 && currentLine.getText().endsWith(tabComponentMakeOption.get(componentMakeTabOptionIndex-1)) ||
							componentMakeTabOptionIndex == 0 && currentLine.getText().endsWith(tabComponentMakeOption.get(tabComponentMakeOption.size()-1))){
						
						String text = currentLine.getText();
						int endIndex = text.lastIndexOf(";");
						
						
						if (componentMakeTabOptionIndex < tabComponentMakeOption.size()) {
						    if (endIndex != -1) {
						    	text = text.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
						    	text = text + ";";
						    } else {
						    	int endIndexSecond = text.lastIndexOf("{");
						    	if (endIndexSecond != -1) {
						    		text = text.substring(0, endIndexSecond);
						    		text = text + "{";
						    	}
						    }
						    
						    currentLine.setText(text + tabComponentMakeOption.get(componentMakeTabOptionIndex));
						    componentMakeTabOptionIndex++;
						    if (componentMakeTabOptionIndex == tabComponentMakeOption.size()) {
								componentMakeTabOptionIndex = 0;
							}
						}
						
						
						componentExist = true;
					}
					
					
					/////////////
					//tab za projekte
					//
					/////
					///proveriti uslove jos
					////
					if (projectList.size() > 0 &&
							(currentLine.getText().endsWith(" in \"") || 
							 currentLine.getText().endsWith("rename ") || //ovo jos videti za rename
							(countProjectWorkspace > 0 && currentLine.getText().endsWith(" in \"" + projectList.get(countProjectWorkspace-1).toString())) || 
							currentLine.getText().endsWith(" in \"" + projectList.get(projectList.size()-1).toString()) && countProjectWorkspace == 0)) {
						
						String text = currentLine.getText();
						if (countProjectWorkspace == projectList.size()) {
							countProjectWorkspace = 0;
						}
						
						if (countProjectWorkspace > 0) {
							text = text.replace(" in \"" + projectList.get(countProjectWorkspace-1).toString(), " in \"");
						} else if (countProjectWorkspace == 0) {
							text = text.replace(" in \"" + projectList.get(projectList.size()-1).toString(), " in \"");
						}
						currentLine.setText(text + projectList.get(countProjectWorkspace).toString());	
						projekat = projectList.get(countProjectWorkspace);
						countProjectWorkspace++;
						postoji = false;
					}
					
					/////////////////////
					///dodavanje paketa
					//////////////////////
					// proveriri jos uslove
					////
				if (notEmptyTabPackageOption && (currentLine.getText().endsWith("/") || 
						projekat != null && currentLine.getText().endsWith(projekat.toString() + "/") ||
						componentPackageTabOptionIndex > 0 && componentPackageTabOptionIndex < tabPackageOption.size() && 
						currentLine.getText().endsWith(tabPackageOption.get(componentPackageTabOptionIndex-1).toString()) ||
						tabPackageOption.size() > 0 && componentPackageTabOptionIndex == tabPackageOption.size() && currentLine.getText().endsWith(tabPackageOption.get(componentPackageTabOptionIndex-1).toString()))){

						String text = currentLine.getText();
	
						if(componentPackageTabOptionIndex < tabPackageOption.size()) {
							//System.out.println(tabPackageOption.get(componentPackageTabOptionIndex).toString());
							
							if(componentPackageTabOptionIndex > 0) {
								text = text.substring(0, text.lastIndexOf(tabPackageOption.get(componentPackageTabOptionIndex-1).toString()));
							} else if (componentPackageTabOptionIndex == 0) {
								text = text.substring(0, text.lastIndexOf(tabPackageOption.get(tabPackageOption.size()-1).toString()));
							}
						  
							currentLine.setText(text + tabPackageOption.get(componentPackageTabOptionIndex).toString());
						  
							componentPackageTabOptionIndex++;
							if (componentPackageTabOptionIndex == tabPackageOption.size()) {
								componentPackageTabOptionIndex = 0;
						   }
					  }
//						else {
//						  if(!currentLine.getText().endsWith("/")) {
//							  text = text.substring(0, text.lastIndexOf(tabPackageOption.get(componentPackageTabOptionIndex-1).toString()));
//							  currentLine.setText(text);
//							  componentPackageTabOptionIndex = 0;
//						  }
//					  }
					}
				}
				
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});
		
		JFileChooser fc = new JFileChooser();
		
		consoleToolbar = new JToolBar(JToolBar.HORIZONTAL);
		consoleToolbar.setFloatable(false);
		btnLoad = new JButton("Load history");
		btnSave = new JButton("Save history");
				
		ImageIcon imgSaveScript = new ImageIcon(ImageResource.getImageResource("action.save.smallIcon"));
		ImageIcon imgOpenScript = new ImageIcon(ImageResource.getImageResource("tree.open.icon"));
		btnSave.setIcon(imgSaveScript);
		btnLoad.setIcon(imgOpenScript);
		
		consoleToolbar.add(javax.swing.Box.createHorizontalGlue());
		consoleToolbar.add(btnSave); 
        consoleToolbar.add(btnLoad);

        add(consoleToolbar, BorderLayout.NORTH);
		add(consoleScroll, BorderLayout.CENTER);
		add(currentLine, BorderLayout.SOUTH);
		

		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				saveKrokiHistory();
				
			}
		});
		
		
		btnLoad.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				parseSelectedFile(chooseKrokiHistory(e));
			}
		});
		
	}
	
	public boolean pretragaKaraktera(String text){
		int counter=0;
		boolean rez = false;
		for( int i=0; i<text.length(); i++ ) {
		    if( text.charAt(i) == '/' ) {
		        counter++;
		    } 
		}
		
		if(counter == 1){
			rez = true;
		} else if (counter > 1) {
			rez = false;
		}
		
		return rez;
	}


	//*********************************************************************************
	//               									        COMMAND PARSING METHODS
	//*********************************************************************************
	public String parseCommand(String command) {
		commandMakeOptionIndex = 0;
		String ret = "KROKI does not understand '" + command +"'";

		if (command.startsWith("help")) {
			ret = displayHelp(command);
		} else if (command.startsWith("open project")){
			openProjectCommand(command);
			ret = "";
		} else if (command.startsWith("save project")) {
			
		} else if(command.startsWith("make project")) {//pravljenje projekta
			ret = makeProjectCommand(command);
		} else if (command.startsWith("make package")) {//pravljenje paketa u projektu
			ret = makePackageCommand(command);
		} else if(command.startsWith("make std-panel")) {//pravljenje std panela u projktu
			ret = makeStdPanelCommand(command);			
		} else if(command.startsWith("make parent-child panel")) {
			ret = makeParentChildPanelCommand(command);
			
		} else if (command.startsWith("make component")){
			 ret = addComponentToPanel(command);
			
		} else if(command.startsWith("rename")){ 
			ret = renameElement(command);
			
		}else if(command.equalsIgnoreCase("redo")){ //redo akcija
			redoKrokiMockupToolAction();
			ret = "";
		} else if(command.equalsIgnoreCase("undo")){ // undo akcija
			undoKrokiMockupToolAction();
			ret = "";
		}else if(command.equalsIgnoreCase("clear")) {
			previousLines.setText("");
			ret = "";
		} else if(command.equalsIgnoreCase("about")) {
			aboutKrokiMockupTool(command);
			ret ="";
			
		} else if(command.equalsIgnoreCase("exit")){
			exitKrokiMockupToolApp();
		}
		return ret;
	}

	public static String addComponentToPanel(String command) {
		
		Pattern patt = Pattern.compile("\\{(.+?)\\} in [\"']([^\"']+)[\"']");
		String components;
		Matcher matcher = patt.matcher(command);
		if(matcher.find()){
			if(matcher.groupCount() > 0){
				components = matcher.group(1);
				String panel = matcher.group(2);
				String[] comps = components.split(";");
				
				String[] paths = panel.split("/");
				int pathsLength = paths.length;
				String panelName = paths[pathsLength - 1];				
				paths = (String[]) ArrayUtils.remove(paths, pathsLength - 1);
				
				for ( int k=0;  k <paths.length; k++) {
					if ( k == 0 ) {
						panel = paths[0];
					} else {
						panel += "/" + paths[k];
					}
					
				}
				
				BussinesSubsystem owner = getOwnerPackage(panel);
				
				VisibleClass pa = KrokiMockupToolApp.getInstance().findPanel(panelName, owner);
				
				if (pa != null) {
				
					for(int i=0; i<comps.length; i++) { // select
						String c = comps[i];
						String[] data = c.trim().split("-");
	
						String propLabel = data[1];
						String type = data[0];
						
						if (type.equalsIgnoreCase("textfield")) {
							makeVisibleProperty(propLabel, type, true, ComponentType.TEXT_FIELD, pa, 1);
						} else if(type.equalsIgnoreCase("textarea")) {
							makeVisibleProperty(propLabel, type, true, ComponentType.TEXT_AREA, pa, 1);
						} else if(type.equalsIgnoreCase("combobox")) {
							makeVisibleProperty(propLabel, type, true, ComponentType.COMBO_BOX, pa, 1);
						} else if(type.equalsIgnoreCase("radiobutton")) {
							makeVisibleProperty(propLabel, type, true, ComponentType.RADIO_BUTTON, pa, 1);
						} else if(type.equalsIgnoreCase("checkbox")) {
							makeVisibleProperty(propLabel, type, true, ComponentType.CHECK_BOX, pa, 1);
						} else if(type.equalsIgnoreCase("report") || type.equalsIgnoreCase("transaction")) {
							makeVisibleProperty(propLabel, type, true, ComponentType.BUTTON, pa, 2);
						} else if(type.equalsIgnoreCase("link")) {
							makeVisibleProperty(propLabel, type, true, ComponentType.LINK, pa, 2);
						}
					}
					
					pa.update();
				}
			}
		}
		String poruka = tabIndex + " " + tabName;
		return poruka;
	}

	//*********************************************************************************
	// 	 												  METHODS THAT EXECUTE COMMANDS
	//*********************************************************************************	

	public void openProjectCommand(String command){
		OpenProjectAction open = new OpenProjectAction();		
		open.actionPerformed(null);
	}
	public static String makeProjectCommand(String command) {
		Scanner sc = new Scanner(command);
		Pattern pattern = Pattern.compile("[\"']([^\"']+)[\"']");
		String token = sc.findInLine(pattern);
		if(token != null) {
			String name = token.substring(1, token.length()-1);
			BussinesSubsystem pr = KrokiMockupToolApp.getInstance().findProject(name);
			if(pr != null) {
				return "Project with specified name already exists";
			}else {
				BussinesSubsystem bss = new BussinesSubsystem(name, true, ComponentType.MENU, null);
				KrokiMockupToolApp.getInstance().getWorkspace().addPackage(bss);
				KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
				return "Project " + token  + " created successfully";
			}
		}else {
			return "Error parsing command. Check your syntax!";
		}
	}

	public static String makePackageCommand(String command) {
		Pattern patt = Pattern.compile("[\"']([^\"']+)[\"'] in [\"']([^\"']+)[\"']");
		String project;
		String pack;
		Matcher matcher = patt.matcher(command);
		if(matcher.find()) {
			if(matcher.groupCount() > 0) {
				pack = matcher.group(1);
				project = matcher.group(2);
				
				BussinesSubsystem owner = getOwnerPackage(project);
				BussinesSubsystem pr = KrokiMockupToolApp.getInstance().checkPackage(pack, owner);
			
				if (pr != null) {
					return "Package with specified name already exists";
				} else {
					makePackage(owner, pack);
					return "Subsystem \"" + pack  + "\" created successfully in \"" + project + "\"";
				}
			}
		}
		return "Error parsing command. Check your syntax!";
	}

	public static String makeStdPanelCommand(String command) {
		Pattern patt = Pattern.compile("[\"']([^\"']+)[\"'] in [\"']([^\"']+)[\"'](?: \\{(.+?)\\})?");
		String panel;
		String pack;
		String components;
		Matcher matcher = patt.matcher(command);
		if(matcher.find()) {
			if (matcher.groupCount() > 0) {
				panel = matcher.group(1);
				pack = matcher.group(2);
				components = matcher.group(3);
				
				BussinesSubsystem owner = getOwnerPackage(pack);
				VisibleClass pa = KrokiMockupToolApp.getInstance().checkPanel(panel, owner);
				
				if (pa == null) {

					if(components != null) {
						String[] comps = components.split(";");
						makeStdPanel(owner, panel, comps);
					}else {
						makeStdPanel(owner, panel, null);
					}
	
					return "Standard panel \"" + panel  + "\" created successfully in \"" + pack + "\"";
				} else {
					return "Standard panel with specified name already exists";
				}
			}
		}
		return "Error parsing command. Check your syntax!";
	}
	
	public String makeParentChildPanelCommand(String command){
		Pattern patt = Pattern.compile("[\"']([^\"']+)[\"'] in [\"']([^\"']+)[\"'](?: \\{(.+?)\\})?");
		String panel;
		String pack;
		String components;
		Matcher matcher = patt.matcher(command);
		if (matcher.find()) {
			if (matcher.groupCount() > 0) {
				panel = matcher.group(1);
				pack = matcher.group(2);				
				components = matcher.group(3);
				
				BussinesSubsystem owner = getOwnerPackage(pack);
				VisibleClass pa = KrokiMockupToolApp.getInstance().checkPanel(panel, owner);
				
				if (pa == null) {
					if (components != null) {
						String[] comps = components.split(";");
						makeParentChildPanel(owner, panel, comps);
					} else {
						makeParentChildPanel(owner, panel, null);
					}
					
					return "Parent child panel \"" + panel + "\" created successfully in \"" + pack +  "\"";
				} else {
					return "Panel with specified name already exists";
				}
			}
		}
		
		return "Error parsing command. Check your syntax!";
		
	}

	public String renameElement(String command){
		Pattern patt = Pattern.compile("[\"']([^\"']+)[\"'](?: \\{(.+?)\\})? to [\"']([^\"']+)[\"']");
		
		return null;
	}
	
	private void redoKrokiMockupToolAction() {
		RedoAction redo = new RedoAction();
		redo.actionPerformed(null);
	}

	private void undoKrokiMockupToolAction() {
		UndoAction undo = new UndoAction();
		undo.actionPerformed(null);
	}

	public void aboutKrokiMockupTool(String command) {
		KrokiMockupToolAboutDialog about = new KrokiMockupToolAboutDialog();
		about.setVisible(true);
	}
	
	public String displayHelp(String command) {
		
		commandTabHelpOptionIndex = 0;
		
		HelpText helptext = new HelpText();
		
		return helptext.getHelpText(command);
	}

	//*********************************************************************************
	//																  	   UTIL METHODS
	//*********************************************************************************
	/**
	 * Finds package or project based on name
	 * @param projectName project or package name that we are looking for
	 * @param recursion if true, searches trough all levels of package hierarchy,
	 * else searches only projects (imediate childern of workspace node)
	 * @return found package or null if package with given name dones not exist
	 */
	public static BussinesSubsystem findNode(String projectName, Boolean recursion) {
		BussinesSubsystem project = null;
		Workspace workspace = KrokiMockupToolApp.getInstance().getWorkspace();

		for(int i=0; i<workspace.getPackageCount(); i++) {
			BussinesSubsystem system = (BussinesSubsystem) workspace.getPackageAt(i);
			if(system.getLabel().trim().equals(projectName)) {
				project = system;
			}else {
				if(recursion) {
					BussinesSubsystem node = findChild(projectName, system);
					if(node != null) {
						project = node;
					}
				}
			}
		}
		return project;
	}

	/**
	 * Finds package inside owner package based on given name
	 * @param childName name of the package that we are looking for
	 * @param owner packet which childern are beig searched
	 * @return found package or null if package with given name dones not exist
	 */
	public static BussinesSubsystem findChild(String childName, BussinesSubsystem owner) {
		BussinesSubsystem found = null;
		for(int i=0; i<owner.ownedElementCount(); i++) {
			if(owner.getOwnedElementAt(i) instanceof BussinesSubsystem) {
				BussinesSubsystem subsys = (BussinesSubsystem) owner.getOwnedElementAt(i);
				if(subsys.getLabel().trim().equals(childName.trim())) {
					found = subsys;
				}else {
					if (subsys.ownedElementCount() > 0) {
						BussinesSubsystem child = findChild(childName, subsys);
						if(child != null) {
							found = child;
						}
					}
				}
			}
		}
		return found;
	}

	/**
	 * Creates package with given name inside workspace
	 * @param projectName name of the new project
	 * @return created project
	 */
	public static BussinesSubsystem makeProject(String projectName) {
		BussinesSubsystem project = new BussinesSubsystem(projectName, true, ComponentType.MENU, null);
		KrokiMockupToolApp.getInstance().getWorkspace().addPackage(project);
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
		return project;
	}

	/**
	 * Creates package with given name inside provided owner package
	 * @param owner package or project inside which new package is created
	 * @param packageName name of the new package
	 * @return created package
	 */
	public static BussinesSubsystem makePackage(BussinesSubsystem owner, String packageName) {
		BussinesSubsystem pack = new BussinesSubsystem(owner);
		pack.setLabel(packageName);
		owner.addNestedPackage(pack);
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
		return pack;
	}

	/**
	 * Creates standard panel inside given package
	 * @param owner package or project inside which panel is created
	 * @param label panel nale
	 * @param components list of GUI components to be created on panel
	 * @return created panel
	 */
	public static VisibleClass makeStdPanel(BussinesSubsystem owner, String label, String[] components) {
		VisibleClass panel = new StandardPanel();
		StandardPanelUtil.defaultGuiSettings((StandardPanel) panel);
		NamingUtil cc = new NamingUtil();
		panel.setLabel(label);
		panel.getComponent().setName(label);
		StandardPanel sp = (StandardPanel) panel;
		sp.getPersistentClass().setName(cc.toCamelCase(panel.getLabel(), false));
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(1);
		((Composite) gr.getComponent()).setLayoutManager(new VerticalLayoutManager());
		((Composite) gr.getComponent()).layout();
		gr.update();
		StyleToolbar st = (StyleToolbar) KrokiMockupToolApp.getInstance().getGuiManager().getStyleToolbar();
		st.updateAllToggles(gr);
		panel.update();
		owner.addOwnedType(panel);

		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
		KrokiMockupToolApp.getInstance().getTabbedPaneController().openTab(panel);
		
		if(components != null) {
			for(int i=0; i<components.length; i++) { // select
				String c = components[i];
				String[] data = c.trim().split("-");

				String propLabel = data[1];
				String type = data[0];
				if(type.equalsIgnoreCase("textfield")) {
					makeVisibleProperty(propLabel, type, true, ComponentType.TEXT_FIELD, panel, 1);
				}else if(type.equalsIgnoreCase("textarea")) {
					makeVisibleProperty(propLabel, type, true, ComponentType.TEXT_AREA, panel, 1);
				}else if(type.equalsIgnoreCase("combobox")) {
					makeVisibleProperty(propLabel, type, true, ComponentType.COMBO_BOX, panel, 1);
				}else if(type.equalsIgnoreCase("radiobutton")) {
					makeVisibleProperty(propLabel, type, true, ComponentType.RADIO_BUTTON, panel, 1);
				}else if(type.equalsIgnoreCase("checkbox")) {
					makeVisibleProperty(propLabel, type, true, ComponentType.CHECK_BOX, panel, 1);
				}else if(type.equalsIgnoreCase("report") || type.equalsIgnoreCase("transaction")) {
					makeVisibleProperty(propLabel, type, true, ComponentType.BUTTON, panel, 2);
				}else if(type.equalsIgnoreCase("link")) {
					makeVisibleProperty(propLabel, type, true, ComponentType.LINK, panel, 2);
				}
			}
		}

		return panel;
	}
	

	
	public VisibleClass makeParentChildPanel(BussinesSubsystem owner, String label, String[] componets){
		VisibleClass panel = new ParentChild();
		ParentChildUtil.defaultGuiSettings((ParentChild) panel);
		//NamingUtil cc = new NamingUtil();
		panel.setLabel(label);
		panel.getComponent().setName(label);		
		ElementsGroup eg = (ElementsGroup) panel.getVisibleElementList().get(1);
		((Composite) eg.getComponent()).setLayoutManager(new VerticalLayoutManager());
		((Composite) eg.getComponent()).layout();
		eg.update();
		panel.update();
		owner.addOwnedType(panel);
		
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
		KrokiMockupToolApp.getInstance().getTabbedPaneController().openTab(panel);
		
		if (componets != null){
			for (int i=0; i < componets.length; i++) {
				String c = componets[i];
				String[] data = c.trim().split("-");
				
				String probLabel = data[1];
				String type = data[0];
				
				/**
				 * 
				 * ovo nije potrebno, samo hierarhija treba //hierarchy // mandatory
				 */
				
				if (type.equalsIgnoreCase("hierachy")) {
				//makeVisibleParentChildElemet(probLabel, type, true, ComponentType., panel, 0);
				} else if (type.equalsIgnoreCase("textarea")) {
					makeVisibleProperty(probLabel, type, true, ComponentType.TEXT_AREA, panel, 0);
				}			
			}
		}
		
		
		return panel;
		
	}
	
	public static Boolean checkArrayElement(ArrayList<String> panelElements, String label){
		
		for (int j=0;j<panelElements.size(); j++) {
			if (panelElements.get(j).equals(label)) {

				return true;
			}
		}
		
		return false;
		
	}

	/**
	 * creates GUI element
	 * @param label element label
	 * @param visible specifies element visibility on panel
	 * @param type component type
	 * @param panel panel on which component is to be created
	 * @param group group inside panel in which created component is to be put (0-toolbar, 1-Properties, 2-Operations)
	 */
	public static void makeVisibleProperty(String label, String typeElement, boolean visible, ComponentType type, VisibleClass panel, int group) {
		
		List<VisibleElement> visiblePanelElements = panel.getVisibleElementList();
		ArrayList<String> panelElements = new ArrayList<String>();
		
		for(int i=0;i<visiblePanelElements.size(); i++){
			panelElements.add(visiblePanelElements.get(i).getLabel().toString());
			
		}
		
		if(checkArrayElement(panelElements, label)){
			allLabelValues.add(label);
			Boolean elementUnique = false;
			int elementCount = 1;
			do{
				if (elementCount > 1) {
					elementCount = elementCount-1;
					int endIndex = label.lastIndexOf("_"+elementCount);
					if (endIndex != -1)  
				    {
						label = label.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
				    }
				}
				if(!checkArrayElement(panelElements, label+"_"+elementCount)){
					label = label + "_"  + elementCount;
					elementUnique = true;
					break;
					
				} else {
					elementCount++;
					elementUnique = false;
				}
				
			} while(elementUnique);
		}
		
		NamingUtil namer = new NamingUtil();
		Report report = new Report(label, visible, type);
		Transaction transaction = new Transaction(label,visible,type);
		Next link = new Next(label);
		VisibleProperty property = new VisibleProperty(label, visible, type);
		// Change mode
		if(type == ComponentType.TEXT_FIELD || type == ComponentType.TEXT_AREA 
				|| type == ComponentType.COMBO_BOX || type == ComponentType.CHECK_BOX) {
			
			String[] data = label.trim().split(",");
			ArrayList<String> allElemetsDataType = new ArrayList<String>();
			ArrayList<String> allElemetsPersistentType = new ArrayList<String>();
			
			for (int i=0;i<data.length;i++) {
				String[] dataSecond = data[i].trim().split(":");
				if (true) {
					allElemetsDataType.add(dataSecond[0].trim());
					allElemetsPersistentType.add(dataSecond[0].trim());
					/**
					 * 
					 * ovo za labelu i column name jos proveriti, nije dobro odradjeno
					 */
					if (dataSecond[0].equalsIgnoreCase("Label")) {
						
						if (in_array(dataSecond[1], allLabelValues)){
							Boolean uniqueLabel = false;
							int count = 1;
							do{
								if(!in_array(dataSecond[1].trim()+"_"+count, allLabelValues)){
									property.setLabel(dataSecond[1].trim()+"_"+count);
									property.setColumnLabel(namer.toDatabaseFormat(panel.getLabel(), dataSecond[1].trim()+"_"+count));
									allLabelValues.add(dataSecond[1].trim()+"_"+count);
									uniqueLabel=true;
									break;
								}
								count++;
							} while(uniqueLabel);
						} else {
							allLabelValues.add(dataSecond[1].trim());
							property.setLabel(dataSecond[1].trim());
							property.setColumnLabel(namer.toDatabaseFormat(panel.getLabel(), dataSecond[1].trim()));
						}
						
					}
					
					if (dataSecond[0].equalsIgnoreCase("Visible")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							property.setVisible(false);
						} else {
							property.setVisible(true);
						}
						
					}
					
					if(dataSecond[0].equalsIgnoreCase("DataType") && type == ComponentType.TEXT_AREA){
						property.setDataType(dataSecond[1]);
					}
					
					if (dataSecond[0].equalsIgnoreCase("DisplayFormat")) {
						property.setDisplayFormat(dataSecond[1].trim());
					}
					
					//ovde bi trebalo da bude za mandatory, ali nema poziva
					if (dataSecond[0].equalsIgnoreCase("Mandatory")) {
						if (dataSecond[1].equalsIgnoreCase("true")) {
						//	property.
						}
						
					}

					if (dataSecond[0].equalsIgnoreCase("Representative")) {
						if (dataSecond[1].equalsIgnoreCase("true")) {
							property.setRepresentative(true);
						} else {
							property.setRepresentative(false);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("AutoGo")) {
						if (dataSecond[1].equalsIgnoreCase("true")) {
							property.setAutoGo(true);
						} else {
							property.setAutoGo(false);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("Disabled")) {
						if (dataSecond[1].equalsIgnoreCase("true")) {
							property.setDisabled(true);
						} else {
							property.setDisabled(false);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("DefaultValue")) {
						property.setDefaultValue(dataSecond[1].trim());
					}
					
					if (dataSecond[0].equalsIgnoreCase("LabelToCode")) {
						if (dataSecond[1].equalsIgnoreCase("true")) {
							property.setLabelToCode(true);
						} else {
							property.setLabelToCode(false);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("PersistentType")) {
						property.setPersistentType(dataSecond[1].trim());
					}
					
					if (dataSecond[0].equalsIgnoreCase("Length")){
						property.setLength(Integer.parseInt(dataSecond[1]));
					}
				}
			}
				
			if (!in_array("DataType", allElemetsDataType) && type == ComponentType.TEXT_AREA) {
				property.setDataType("String");
			}
			
			if (!in_array("PersistentType", allElemetsPersistentType)) {
				property.setPersistentType("Varchar");
			}
		} else if (type == ComponentType.PANEL) {
			String[] data = label.trim().split(",");
			ArrayList<String> allElemetsDataType = new ArrayList<String>();
			ArrayList<String> allElemetsPersistentType = new ArrayList<String>();
			
			for (int i=0;i<data.length;i++) {
				String[] dataSecond = data[i].trim().split(":");
				if (true) {
					allElemetsDataType.add(dataSecond[0].trim());
					allElemetsPersistentType.add(dataSecond[0].trim());
					/**
					 * 
					 * ovo za labelu i column name jos proveriti, nije dobro odradjeno
					 */
					if (dataSecond[0].equalsIgnoreCase("Label")) {
						
						if (in_array(dataSecond[1], allLabelValues)){
							Boolean uniqueLabel = false;
							int count = 1;
							do{
								if(!in_array(dataSecond[1].trim()+"_"+count, allLabelValues)){
									property.setLabel(dataSecond[1].trim()+"_"+count);
									property.setColumnLabel(namer.toDatabaseFormat(panel.getLabel(), dataSecond[1].trim()+"_"+count));
									allLabelValues.add(dataSecond[1].trim()+"_"+count);
									uniqueLabel=true;
									break;
								}
								count++;
							} while(uniqueLabel);
						} else {
							allLabelValues.add(dataSecond[1].trim());
							property.setLabel(dataSecond[1].trim());
							property.setColumnLabel(namer.toDatabaseFormat(panel.getLabel(), dataSecond[1].trim()));
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("Visible")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							property.setVisible(false);
						} else {
							property.setVisible(true);
						}
						
					}	
				}
			}
		} else if (type == ComponentType.RADIO_BUTTON) { 
			/**
			 * 
			 * videti koja je ovo komponenta
			 */
		} else if (type == ComponentType.BUTTON) { //Transaction
			
			String[] data = label.trim().split(",");
			ArrayList<String> allElemetsDataType = new ArrayList<String>();
			ArrayList<String> allElemetsPersistentType = new ArrayList<String>();
			
			for (int i=0;i<data.length;i++) {
				String[] dataSecond = data[i].trim().split(":");
				if (true) {
					allElemetsDataType.add(dataSecond[0].trim());
					allElemetsPersistentType.add(dataSecond[0].trim());
					
					if (typeElement.equalsIgnoreCase("report")) {
						
						/**
						 * 
						 * ovo za labelu i column name jos proveriti, nije dobro odradjeno
						 */
						if (dataSecond[0].equalsIgnoreCase("Label")) {
							
							if (in_array(dataSecond[1], allLabelValues)){
								Boolean uniqueLabel = false;
								int count = 1;
								do{
									if(!in_array(dataSecond[1].trim()+"_"+count, allLabelValues)){
										report.setLabel(dataSecond[1].trim()+"_"+count);
										allLabelValues.add(dataSecond[1].trim()+"_"+count);
										uniqueLabel=true;
										break;
									}
									count++;
								} while(uniqueLabel);
							} else {
								allLabelValues.add(dataSecond[1].trim());
								report.setLabel(dataSecond[1].trim());
							}
						}
						
						if (dataSecond[0].equalsIgnoreCase("Visible")) {
							if (dataSecond[1].equalsIgnoreCase("false")){
								report.setVisible(false);
							} else {
								report.setVisible(true);
							}
						}
						
						if (dataSecond[0].equalsIgnoreCase("HasParametersForm")) {
							if (dataSecond[1].equalsIgnoreCase("false")){
								report.setVisible(false);
							} else {
								report.setVisible(true);
							}						
						}
						
						if (dataSecond[0].equalsIgnoreCase("FilterdByKey")) {
							if (dataSecond[1].equalsIgnoreCase("false")){
								report.setVisible(false);
							} else {
								report.setVisible(true);
							}						
						}
						
						
						//treba da bude imprelentirana
						if (dataSecond[0].equalsIgnoreCase("PersistentOperation")) {
							
							//report.setPersistentOperation(persistentOperation);
						}
						
						if (dataSecond[0].equalsIgnoreCase("ReportName")) {
							report.setReportName(dataSecond[1]);
						}
						
						if (dataSecond[0].equalsIgnoreCase("DataFilter")) {
							report.setDataFilter(dataSecond[1]);
						}
						
						if (dataSecond[0].equalsIgnoreCase("SortBy")) {
							report.setSortBy(dataSecond[1]);
						}
					} else if (typeElement.equalsIgnoreCase("transaction")) {
						
						if (dataSecond[0].equalsIgnoreCase("Label")) {
							
							if (in_array(dataSecond[1], allLabelValues)){
								Boolean uniqueLabel = false;
								int count = 1;
								do{
									if(!in_array(dataSecond[1].trim()+"_"+count, allLabelValues)){
										transaction.setLabel(dataSecond[1].trim()+"_"+count);
										allLabelValues.add(dataSecond[1].trim()+"_"+count);
										uniqueLabel=true;
										break;
									}
									count++;
								} while(uniqueLabel);
							} else {
								allLabelValues.add(dataSecond[1].trim());
								transaction.setLabel(dataSecond[1].trim());
							}
							
						}
						
						if (dataSecond[0].equalsIgnoreCase("Visible")) {
							if (dataSecond[1].equalsIgnoreCase("false")){
								transaction.setVisible(false);
							} else {
								transaction.setVisible(true);
							}
						}
						
						if (dataSecond[0].equalsIgnoreCase("HasParametersForm")) {
							if (dataSecond[1].equalsIgnoreCase("false")){
								transaction.setVisible(false);
							} else {
								transaction.setVisible(true);
							}						
						}
						
						if (dataSecond[0].equalsIgnoreCase("FilterdByKey")) {
							if (dataSecond[1].equalsIgnoreCase("false")){
								transaction.setVisible(false);
							} else {
								transaction.setVisible(true);
							}						
						}
						
						
						//treba da bude imprelentirana
						if (dataSecond[0].equalsIgnoreCase("PersistentOperation")) {
							
							//report.setPersistentOperation(persistentOperation);
						}
						
						if (dataSecond[0].equalsIgnoreCase("RefreshRow")){
							if (dataSecond[1].equalsIgnoreCase("false")){
								transaction.setRefreshRow(false);
							} else {
								transaction.setRefreshRow(true);
							}
						}
						
						if (dataSecond[0].equalsIgnoreCase("RefreshAll")){
							if (dataSecond[1].equalsIgnoreCase("false")){
								transaction.setRefreshAll(false);
							} else {
								transaction.setRefreshAll(true);
							}
						}
						
						if (dataSecond[0].equalsIgnoreCase("AskConfirmation")) {
							if (dataSecond[1].equalsIgnoreCase("false")){
								transaction.setAskConfirmation(false);
							} else {
								transaction.setAskConfirmation(true);
							}
						}
						
						if (dataSecond[0].equalsIgnoreCase("ShowErrors")) {
							if (dataSecond[1].equalsIgnoreCase("false")){
								transaction.setShowErrors(false);
							} else {
								transaction.setShowErrors(true);
							}
						}
						
						if (dataSecond[0].equalsIgnoreCase("ConfirmationMessage")) {
							transaction.setConfirmationMessage(dataSecond[1]);
						}
					}
				}
			}
		} else if (type == ComponentType.LINK){
			
			String[] data = label.trim().split(",");
			ArrayList<String> allElemetsDataType = new ArrayList<String>();
			ArrayList<String> allElemetsPersistentType = new ArrayList<String>();
			
			for (int i=0;i<data.length;i++) {
				String[] dataSecond = data[i].trim().split(":");
				if (true) {
					allElemetsDataType.add(dataSecond[0].trim());
					allElemetsPersistentType.add(dataSecond[0].trim());
					/**
					 * 
					 * ovo za labelu i column name jos proveriti, nije dobro odradjeno
					 */
					if (dataSecond[0].equalsIgnoreCase("Label")) {
						
						if (in_array(dataSecond[1], allLabelValues)){
							Boolean uniqueLabel = false;
							int count = 1;
							do{
								if(!in_array(dataSecond[1].trim()+"_"+count, allLabelValues)){
									link.setLabel(dataSecond[1].trim()+"_"+count);
									allLabelValues.add(dataSecond[1].trim()+"_"+count);
									uniqueLabel=true;
									break;
								}
								count++;
							} while(uniqueLabel);
						} else {
							allLabelValues.add(dataSecond[1].trim());
							link.setLabel(dataSecond[1].trim());
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("Visible")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.setVisible(false);
						} else {
							link.setVisible(true);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("Add")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.setAdd(false);
						} else {
							link.setAdd(true);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("Update")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.setUpdate(false);
						} else {
							link.setUpdate(true);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("Copy")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.setCopy(false);
						} else {
							link.setCopy(true);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("Delete")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.setCopy(false);
						} else {
							link.setCopy(true);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("Search")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.setSearch(false);
						} else {
							link.setSearch(true);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("ChangeMode")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.setChangeMode(false);
						} else {
							link.setChangeMode(true);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("DataNavigation")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.setDataNavigation(false);
						} else {
							link.setDataNavigation(true);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("Mandatory")) {
//						if (dataSecond[1].equalsIgnoreCase("false")){
//							link.set(false);
//						} else {
//							link.setDataNavigation(true);
//						} Target zoom
					}
					
					if (dataSecond[0].equalsIgnoreCase("DefaultViewMode")) {
						//ovo treba jos videti
						link.getStdPanelSettings().setDefaultViewMode((ViewMode) (Object)dataSecond[1]);
					}
					
					if (dataSecond[0].equalsIgnoreCase("DefaultOperationMode")) {
						//ovo treba jos videti
						link.getStdPanelSettings().setDefaultOperationMode((OperationMode) (Object)dataSecond[1]);
					} // treba proveravati neke od ponudjenih uslova, kao sto je u aplikaciji
					
					if (dataSecond[0].equalsIgnoreCase("ConfirmDelete")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.getStdPanelSettings().setConfirmDelete(false);
						} else {
							link.getStdPanelSettings().setConfirmDelete(true);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("StayInAddMode")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.getStdPanelSettings().setStayInAddMode(false);
						} else {
							link.getStdPanelSettings().setStayInAddMode(true);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("GoToLastAdded")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.getStdPanelSettings().setGoToLastAdded(false);
						} else {
							link.getStdPanelSettings().setGoToLastAdded(true);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("DataFilter")) {
						link.getStdDataSettings().setDataFilter(dataSecond[1]);
					}
					
					if (dataSecond[0].equalsIgnoreCase("SortBy")) {
						//link.getStdDataSettings().setSortBy(sortBy);
					}
					
					if (dataSecond[0].equalsIgnoreCase("ActivationPanel")) {
						//TODO
					}
					
					if (dataSecond[0].equalsIgnoreCase("TargetPanel")) {
						/* link.setTargetPanel(panel);
						 * proveriti ovo
						 */
//						owner
//						KrokiMockupToolApp.getInstance().findPanel(dataSecond[1], owner)
//						link.setTargetPanel((VisibleClass) dataSecond[1]);
					}
					
					if (dataSecond[0].equalsIgnoreCase("TargetZoom")) {
						//TODO
					}
					
					if (dataSecond[0].equalsIgnoreCase("Position")) {
						//TODO
					}
					
					if (dataSecond[0].equalsIgnoreCase("AutoActivate")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.setAutoActivate(false);
						} else {
							link.setAutoActivate(true);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("DisplayRepresentative")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.setDisplayRepresentative(false);
						} else {
							link.setDisplayRepresentative(true);
						}
					}
					
					if (dataSecond[0].equalsIgnoreCase("DisplayRepresentative")) {
						if (dataSecond[1].equalsIgnoreCase("false")){
							link.setDisplayIdentifier(false);
						} else {
							link.setDisplayIdentifier(true);
						}
					}
					
				}
			}
		}
		
		if (typeElement.equalsIgnoreCase("report")) {
			UIPropertyUtil.addVisibleElement(panel, report);
		} else if (typeElement.equalsIgnoreCase("transaction")) {
			UIPropertyUtil.addVisibleElement(panel, transaction);
		} else if (type == ComponentType.LINK) {
			link.setActivationPanel(panel);
			UIPropertyUtil.addVisibleElement(panel, link);
		} else {
			UIPropertyUtil.addVisibleElement(panel, property);
		}
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(group);
		
		if (typeElement.equalsIgnoreCase("report")) {
			ElementsGroupUtil.addVisibleElement(gr, report);
		} else if (typeElement.equalsIgnoreCase("transaction")) {
			ElementsGroupUtil.addVisibleElement(gr, transaction); //button
		} else if (type == ComponentType.LINK) {
			ElementsGroupUtil.addVisibleElement(gr, link);
		}else {
			ElementsGroupUtil.addVisibleElement(gr, property);
		}
		
		//TODO Vidi sta se ovde buni
		//property.getComponent().setAbsolutePosition(gr.getComponent().getAbsolutePosition());
		gr.update();
		panel.update();
		
	}
	
	
	public static void makeVisibleParentChildElemet(String label, String typeElement, boolean visible, ComponentType type, VisibleClass panel, int group) {
		
		

		Hierarchy hierarchy = new Hierarchy(label, visible, type);
		
	}

	/**
	 * Finds (or creates) package hierarchy based on provided path and returns last package in hierarchy
	 * Used for commands 'make package' and 'make std-panel'
	 * @param path path in the hierarchy.
	 *        path is specified as 'Project/Package/Package/...'
	 * @return found or created package
	 */
	public static BussinesSubsystem getOwnerPackage(String path) {
		BussinesSubsystem owner = null;

		String[] paths = path.split("/");
		String projectName = paths[0];

		BussinesSubsystem root = findNode(projectName, false);
		if(root == null) {
			root = makeProject(projectName);
		}
		if(paths.length > 1) {
			String firstAfterRootName = paths[1];
			BussinesSubsystem firstAfterRoot = findChild(firstAfterRootName, root);
			if(firstAfterRoot == null) {
				firstAfterRoot = makePackage(root, firstAfterRootName);
			}
			owner = firstAfterRoot;
			for(int i=2; i<paths.length; i++) {
				String packName = paths[i];
				String ownerName = paths[i-1];
				BussinesSubsystem pacOwner = findNode(ownerName, true);
				BussinesSubsystem pac = findChild(packName, pacOwner);
				if(pac == null) {
					pac = makePackage(pacOwner, packName);
				}
				owner = pac;
			}
		}else {
			owner = root;
		}

		return owner;
	}
	
	public void makeTabPackageOption(){
		
		//
		if(!postoji){
			tabPackageOption = new ArrayList<String>();
			for (int i=0;i<projectList.size();i++){
				if(currentLine.getText().endsWith(projectList.get(i).toString() + "/")){
					BussinesSubsystem projekat = KrokiMockupToolApp.getInstance().findProject(projectList.get(i).toString());
					//projekat = projectList.get(i).toString();
					
					//ovo srediti jos da smanjim kod
					if (!currentLine.getText().startsWith("make component")) {
						for(int k=0;k<projekat.ownedElementCount();k++){
							if(projekat.getOwnedElementAt(k) instanceof BussinesSubsystem) {
								tabPackageOption.add(projekat.getOwnedElementAt(k).toString());
							}
						}
					} else {
						for(int k=0;k<projekat.ownedElementCount();k++){
							tabPackageOption.add(projekat.getOwnedElementAt(k).toString());
						}
					}
					postoji = true;
					break;
				}
			}
		}
		
		if (postoji && tabPackageOption.size() > 0) {
			String sad = currentLine.getText();
			String segments[] = sad.split("/");
			tempTabPackageOption = new ArrayList<String>();
			tempTabPackageOption = tabPackageOption;

			componentPackageTabOptionIndex = 0;
			
			if (segments.length == 2) {
				tabPackageOption = new ArrayList<String>();
				String packageName = segments[segments.length-1];
				String projectName = projekat.toString();
				
				BussinesSubsystem projectOwner = KrokiMockupToolApp.getInstance().findProject(projectName);
				BussinesSubsystem pak = KrokiMockupToolApp.getInstance().checkPackage(packageName, projectOwner);
				
				if (pak != null && pak.ownedElementCount() > 0 ) {
					for (int j=0;j<pak.ownedElementCount(); j++) {
						if (pak.getOwnedElementAt(j) instanceof BussinesSubsystem) {
							tabPackageOption.add(pak.getOwnedElementAt(j).toString());
						}
					}
				}
			} else if (segments.length > 2) {

				tabPackageOption = new ArrayList<String>();
				String packString = segments[segments.length-1];
				String projectName = segments[segments.length-2];
				
				BussinesSubsystem projectOwner = findNode(projectName, true);
				BussinesSubsystem pack = KrokiMockupToolApp.getInstance().checkPackage(packString, projectOwner);
				
				if(pack != null && pack.ownedElementCount() > 0){								
					
					for (int j=0;j<pack.ownedElementCount(); j++) {
						if(pack.getOwnedElementAt(j) instanceof BussinesSubsystem) {
							tabPackageOption.add(pack.getOwnedElementAt(j).getLabel());
						}
					}
				}
				
				
			}
			
			if (tabPackageOption.size() > 0){
				notEmptyTabPackageOption = true;
			} else {
				notEmptyTabPackageOption = false;
				tabPackageOption = tempTabPackageOption;
			}
		}		
	}

	/**
	 * Display text in cammand panel text pane
	 * @param text text that needs to be displayed
	 * @param type indicates message type 
	 */
	public void displayText(String text, int type) {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy  H:mm:ss");
		String d = formatter.format(now);
		//previousLines.append(text);
		StyledDocument document = previousLines.getStyledDocument();
		SimpleAttributeSet set = new SimpleAttributeSet();
		previousLines.setCharacterAttributes(set, true);
		String prefix = "[KROKI] ";
		switch (type) {
			case 0:
				StyleConstants.setForeground(set, Color.blue);
				prefix = "[" + d + "] ";
				break;
			case 1:
				StyleConstants.setForeground(set, Color.black);
				prefix = ">> ";
				break;
			case 2:
				StyleConstants.setForeground(set, Color.blue);
				prefix = "[" + d + "] " + "WARNING: ";
				break;
			case 3:
				StyleConstants.setForeground(set, Color.red);
				prefix =  "[" + d + "] " + "ERROR: ";
				break;
			case 4:
				StyleConstants.setForeground(set, new Color(44,99,49));
				prefix = "[" + d + "] ";
				break;
		}
		try {
			document.insertString(document.getLength(), prefix + text + "\n", set);
			previousLines.setCaretPosition(previousLines.getDocument().getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public int getNumberOfCommas(String text){
		int counter = 0;
		for( int i=0; i<text.length(); i++ ) {
		    if( text.charAt(i) == ',' ) {
		        counter++;
		    } 
		}
		
		return counter;
	}
	
	public static Boolean in_array(String element, ArrayList<String> elementArray){
		
		for(int i=0;i<elementArray.size();i++){
			if(elementArray.get(i).equalsIgnoreCase(element)){
				return true;
			}
		}
		
		return false;
	}
	
	public void exitKrokiMockupToolApp(){
		/*try {
		Calculator4Lexer lexer = new Calculator4Lexer(new ANTLRFileStream("D:/Master/test1.calc"));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
	    Calculator4Parser p = new Calculator4Parser(tokens);
	    p.addParseListener(new CalculatorListener());
	    ParserRuleContext t = p.program();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	*//**
	*
	* ovo je test, ovde mozemo opet da pozivamo file za parsiranje */
	try {
		KrokiMockupToolCommandSyntaxLexer lexer = new KrokiMockupToolCommandSyntaxLexer(new ANTLRFileStream("D:/Master/test1.calc"));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
	    KrokiMockupToolCommandSyntaxParser p = new KrokiMockupToolCommandSyntaxParser(tokens);
	    p.addParseListener(new KrokiMockupParserListener());
	    p.makeCommand();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		/*
		 * da viidmo listing za listory
		 */
	    
//		System.out.println(listory);
//		for (int i=0;i<listory.size();i++) {
//			
//			System.out.println(listory.get(i));
//			
//		}
	}
	
//	public static void getTabCurrnetIndex(int index){
//		tabIndex = index;
//	}
//	
//	public static void getTabCurrentName(String name){
//		tabName = name;
//	}
	
	public void saveKrokiHistory (){

		String theFilepath = null;
		boolean acceptable = false;
		
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Parse Files(parse)", "parse", "parse");
		fileChooser.setFileFilter(filter);
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			theFilepath = fileChooser.getSelectedFile().getAbsolutePath();
			File myFile = fileChooser.getSelectedFile();
			
			if (myFile.exists()) {
				int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?",
                        "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                	try(PrintWriter writer = new PrintWriter(myFile))
    		        {
    		            for (Object file : listory)
    		            {
    		                writer.println(file.toString());
    		            }
    		        }
    		        catch(FileNotFoundException ex)
    		        {
    		            System.out.println(ex.toString());
    		        }
                }
			} else {

            	if(!FilenameUtils.getExtension(myFile.getName()).equalsIgnoreCase("parse")) {
					myFile = new File(myFile.toString() + ".parse"); 
					myFile = new File(myFile.getParentFile(), FilenameUtils.getBaseName(myFile.getName())+".parse"); // ALTERNATIVELY: remove the extension (if any) and replace it with ".xml"
				}
            	
            	try(PrintWriter writer = new PrintWriter(myFile))
		        {
		            for (Object file : listory)
		            {
		                writer.println(file.toString());
		            }
		        }
		        catch(FileNotFoundException ex)
		        {
		            System.out.println(ex.toString());
		        }
			}
		} // Default view mode
	}
	
	public String chooseKrokiHistory(ActionEvent e) {
	    JFileChooser fileChooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Parse Files", "parse", "parse");
	    fileChooser.setFileFilter(filter);
	    int returnValue = fileChooser.showOpenDialog(null);
	    if (returnValue == JFileChooser.APPROVE_OPTION)
	    {
	        File selectedFile = fileChooser.getSelectedFile();
	        return selectedFile.getAbsolutePath();
	    } 
		return null;
	}
	
	public void parseSelectedFile(String pathToFile){
		
		if (pathToFile != null) {
			String path = pathToFile;
			try {
				KrokiMockupToolCommandSyntaxLexer lexer = new KrokiMockupToolCommandSyntaxLexer(new ANTLRFileStream(path));
				CommonTokenStream tokens = new CommonTokenStream(lexer);
			    KrokiMockupToolCommandSyntaxParser p = new KrokiMockupToolCommandSyntaxParser(tokens);
			    p.addParseListener(new KrokiMockupParserListener());
			    p.makeCommand();
			    displayText("Imported file " + pathToFile, OutputPanel.KROKI_RESPONSE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class MyTextField extends JTextField {
		MyTextField(int len) {
			super(len);
			addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent evt) {
					int key = evt.getKeyCode();
					if (key == KeyEvent.VK_SPACE)
						transferFocus();
				}
			});
		}
	}
	
	
	//check if label fo sertan element is unique, if not aubfix is added
//	public String labelInArray(String label, ArrayList<String> allLabels){
//		
//		int count = 1;
//		for (int i=0;i<allLabels.size();i++){
//			if (!allLabels.get(i).equalsIgnoreCase(label)){
//				String newLabel = label + count;
//				return newLabel;
//			} 
//			count++;
//		}
//		
//		return "true";
//		
//	}
}