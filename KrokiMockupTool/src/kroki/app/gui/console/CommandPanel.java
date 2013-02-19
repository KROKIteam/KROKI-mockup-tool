package kroki.app.gui.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLEditorKit;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.toolbar.StyleToolbar;
import kroki.app.model.Workspace;
import kroki.commons.camelcase.NamingUtil;
import kroki.mockup.model.Composite;
import kroki.mockup.model.layout.VerticalLayoutManager;
import kroki.profil.ComponentType;
import kroki.profil.association.Next;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlType;

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

	public CommandPanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		listory = new ArrayList<String>();

		initGUI();
	}

	public void initGUI() {

		previousLines = new JTextPane();
		previousLines.setFont(new Font("Monospaced",Font.PLAIN,15));
		previousLines.setEditable(false);

		consoleScroll = new JScrollPane(previousLines);

		currentLine = new JTextField();
		currentLine.setFont(new Font("Monospaced",Font.PLAIN,15));


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

		currentLine.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				//if ENTER key is pressed inside text box, command is read and parsed
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!currentLine.getText().equals("")) {
						displayText(currentLine.getText(), 1);
						displayText(parseCommand(currentLine.getText()), 0);
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
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});

		add(consoleScroll, BorderLayout.CENTER);
		add(currentLine, BorderLayout.SOUTH);
	}


	//*********************************************************************************
	//               									        COMMAND PARSING METHODS
	//*********************************************************************************
	public String parseCommand(String command) {
		String ret = "KROKI does not understand '" + command +"'";

		if(command.startsWith("help")) {
			ret = displayHelp(command);
		}else if(command.startsWith("make project")) {
			ret = makeProjectCommand(command);
		}else if (command.startsWith("make package")) {
			ret = makePackageCommand(command);
		}
		else if(command.startsWith("make std-panel")) {
			ret = makeStdPanelCommand(command);
		}else if(command.equalsIgnoreCase("clear")) {
			previousLines.setText("");
			ret = "";
		}
		return ret;
	}

	//*********************************************************************************
	// 	 												  METHODS THAT EXECUTE COMMANDS
	//*********************************************************************************	

	public String makeProjectCommand(String command) {
		Scanner sc = new Scanner(command);
		Pattern pattern = Pattern.compile("[\"']([^\"']+)[\"']");
		String token = sc.findInLine(pattern);
		if(token != null) {
			String name = token.substring(1, token.length()-1);
			BussinesSubsystem bss = new BussinesSubsystem(name, true, ComponentType.MENU, null);
			KrokiMockupToolApp.getInstance().getWorkspace().addPackage(bss);
			KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
			return "Project " + token  + " created successfully";
		}else {
			return "Error parsing command. Check your syntax!";
		}
	}

	public String makePackageCommand(String command) {
		Pattern patt = Pattern.compile("[\"']([^\"']+)[\"'] in [\"']([^\"']+)[\"']");
		String project;
		String pack;

		Matcher matcher = patt.matcher(command);
		if(matcher.find()) {
			if(matcher.groupCount() > 0) {
				pack = matcher.group(1);
				project = matcher.group(2);

				BussinesSubsystem owner = getOwnerPackage(project);
				makePackage(owner, pack);

				return "Subsystem \"" + pack  + "\" created successfully in \"" + project + "\"";
			}
		}
		return "Error parsing command. Check your syntax!";
	}

	public String makeStdPanelCommand(String command) {
		Pattern patt = Pattern.compile("[\"']([^\"']+)[\"'] in [\"']([^\"']+)[\"'](?: \\{(.+?)\\})?");
		String panel;
		String pack;
		String components;
		Matcher matcher = patt.matcher(command);
		if(matcher.find()) {
			if(matcher.groupCount() > 0) {
				panel = matcher.group(1);
				pack = matcher.group(2);
				components = matcher.group(3);

				if(components != null) {
					String[] comps = components.split(",");
					BussinesSubsystem owner = getOwnerPackage(pack);
					makeStdPanel(owner, panel, comps);
				}else {
					BussinesSubsystem owner = getOwnerPackage(pack);
					makeStdPanel(owner, panel, null);
				}

				return "Standard panel \"" + panel  + "\" created successfully in \"" + pack + "\"";
			}
		}
		return "Error parsing command. Check your syntax!";
	}

	public String displayHelp(String command) {
		String help = "";
		if(command.equals("help")) {
			help = "Available commands:" +
					"\n\t\t1. make project" +
					"\n\t\t2. make package" +
					"\n\t\t3. make std-panel"+
					"\n\t\t4. clear" +
					"\nFor help on specific command, type \"help command name\" (i.e. help make project)";
		}else if(command.equals("help make project")) {
			help = "\n[KROKI] make project command" +
					"\n\tSyntax: make project \"Project name\"" +
					"\n\tDescription: Creates new project with specified name in workspace." +
					"\n\tExample:" + 
					"\n\t\tmake project \"Resources\"" + 
					"\n\t\tCreates new project named \"Resources\" in workspace" +
					"\n\t\tNOTE: The project name can be written in sigle or double quotes.\n";
		}else if(command.equals("help make package")) {
			help = "\n[KROKI] make package command" +
					"\n\tSyntax: make package \"Package name\" in \"Path\"" +
					"\n\tDescription: Creates new package with specified name in specified path." +
					"\n\t\t     Path specifies hierarchial order of parent packages starting from project in workspace" +
					"\n\t\t     and ending with immediate parent package, separated by /." +
					"\n\tExample:" + 
					"\n\t\tmake package \"Workers\" in \"Resources/Human Resources\"" + 
					"\n\t\tCreates new package named \"Workers\" in package named \"Human Resources\" in project named \"Resources\"." +
					"\n\t\tNOTE: If any project or package in path is not found, it will be created." +
					"\n\t\t      The package name and path can be written in sigle or double quotes.\n";
		}else if(command.equals("help make std-panel")) {
			help = "\n[KROKI] make std-panel command" +
					"\n\tSyntax: make std-panel \"Panel name\" in \"Path\" {components}" +
					"\n\tDescription: Creates standard panel with specified name in specified path with enlisted GUI components." +
					"\n\t\t     Path specifies hierarchial order of parent packages starting from project in workspace" +
					"\n\t\t     and ending with immediate parent package, separated by /." +
					"\n\t\t     Components specify list of GUI elements to be drawn on panel, components list is enclosed in curly brackets," +
					"\n\t\t     each component in list is specified by pair type-name, separated by comma." +
					"\n\t\t     Available component types are: textfield, textarea, combobox, radiobutton, checkbox, report, transaction, link." +
					"\n\t\t     NOTE: When specifying component type, case is ignored, so textfield is same as textField or TextField." +
					"\n\tExample:" + 
					"\n\t\tmake std-panel \"Workers\" in \"Resources/Human resources\" {textfield-First name, textfield-Last name, textarea-Address, checkbox-Married}" +
					"\n\t\tNOTE: The panel name and path can be written in sigle or double quotes.\n";
		}else {
			help = "No help for command \"" + command.substring(5) + "\"";
		}
		return help;
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
	public BussinesSubsystem findNode(String projectName, Boolean recursion) {
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
	public BussinesSubsystem findChild(String childName, BussinesSubsystem owner) {
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
	public BussinesSubsystem makeProject(String projectName) {
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
	public BussinesSubsystem makePackage(BussinesSubsystem owner, String packageName) {
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
	public VisibleClass makeStdPanel(BussinesSubsystem owner, String label, String[] components) {
		VisibleClass panel = new StandardPanel();
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
			for(int i=0; i<components.length; i++) {
				String c = components[i];
				String[] data = c.trim().split("-");

				String propLabel = data[1];
				String type = data[0];
				if(type.equalsIgnoreCase("textfield")) {
					makeVisibleProperty(propLabel, true, ComponentType.TEXT_FIELD, panel, 1);
				}else if(type.equalsIgnoreCase("textarea")) {
					makeVisibleProperty(propLabel, true, ComponentType.TEXT_AREA, panel, 1);
				}else if(type.equalsIgnoreCase("combobox")) {
					makeVisibleProperty(propLabel, true, ComponentType.COMBO_BOX, panel, 1);
				}else if(type.equalsIgnoreCase("radiobutton")) {
					makeVisibleProperty(propLabel, true, ComponentType.RADIO_BUTTON, panel, 1);
				}else if(type.equalsIgnoreCase("checkbox")) {
					makeVisibleProperty(propLabel, true, ComponentType.CHECK_BOX, panel, 1);
				}else if(type.equalsIgnoreCase("report") || type.equalsIgnoreCase("transaction")) {
					makeVisibleProperty(propLabel, true, ComponentType.BUTTON, panel, 2);
				}else if(type.equalsIgnoreCase("link")) {
					Next next = new Next(propLabel);
					next.setActivationPanel(panel);
					panel.addVisibleElement(next);
					ElementsGroup group = (ElementsGroup) panel.getVisibleElementList().get(2);
					group.addVisibleElement(next);
					group.update();
					panel.update();
				}
			}
		}

		return panel;
	}

	/**
	 * creates GUI element
	 * @param label element label
	 * @param visible specifies element visibility on panel
	 * @param type component type
	 * @param panel panel on which component is to be created
	 * @param group group inside panel in which created component is to be put (0-toolbar, 1-Properties, 2-Operations)
	 */
	public void makeVisibleProperty(String label, boolean visible, ComponentType type, VisibleClass panel, int group) {
		NamingUtil namer = new NamingUtil();
		VisibleProperty property = new VisibleProperty(label, visible, type);
		if(type == ComponentType.TEXT_FIELD) {
			property.setDataType("String");
		}
		property.setColumnLabel(namer.toDatabaseFormat(panel.getLabel(), label));
		panel.addVisibleElement(property);
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(group);
		gr.addVisibleElement(property);
		//TODO Vidi sta se ovde buni
		//property.getComponent().setAbsolutePosition(gr.getComponent().getAbsolutePosition());
		gr.update();
		panel.update();
	}

	/**
	 * Finds (or creates) package hierarchy based on provided path and returns last package in hierarchy
	 * Used for commands 'make package' and 'make std-panel'
	 * @param path path in the hierarchy.
	 *        path is specified as 'Project/Package/Package/...'
	 * @return found or created package
	 */
	public BussinesSubsystem getOwnerPackage(String path) {
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

	/**
	 * Display text in cammand panel text pane
	 * @param text text that needs to be displayed
	 * @param type indicates message type (0 - KROKI response, 1 - user echo, 2 - KROKI warning, 3 - KROKI error)
	 */
	public void displayText(String text, int type) {
		//previousLines.append(text);
		StyledDocument document = previousLines.getStyledDocument();
		SimpleAttributeSet set = new SimpleAttributeSet();
		previousLines.setCharacterAttributes(set, true);
		String prefix = "[KROKI] ";
		switch (type) {
		case 0:
			StyleConstants.setForeground(set, Color.blue);
			prefix = "[KROKI] ";
			break;
		case 1:
			StyleConstants.setForeground(set, Color.black);
			prefix = ">> ";
			break;
		case 2:
			StyleConstants.setForeground(set, Color.blue);
			prefix = "[WARNING] ";
			break;
		case 3:
			StyleConstants.setForeground(set, Color.red);
			prefix = "[ERROR] ";
			break;
		}
		try {
			document.insertString(document.getLength(), prefix + text + "\n", set);
			previousLines.setCaretPosition(previousLines.getDocument().getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

}