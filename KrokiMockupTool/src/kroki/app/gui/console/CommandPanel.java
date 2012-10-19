package kroki.app.gui.console;

import java.awt.BorderLayout;
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
import javax.swing.border.BevelBorder;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.toolbar.StyleToolbar;
import kroki.app.model.Workspace;
import kroki.commons.camelcase.CamelCaser;
import kroki.mockup.model.Composite;
import kroki.mockup.model.layout.VerticalLayoutManager;
import kroki.profil.ComponentType;
import kroki.profil.association.Next;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Simulacija konzole pomocu TextField i TextArea komponenti
 * @author mrd
 *
 */
public class CommandPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	//JTextArea koja sadrzi prethodno unete linije
	//i poruke od strane aplikacije
	private JTextArea previousLines;
	private JScrollPane consoleScroll;
	//JTextField u koji se unose komande
	private JTextField currentLine;
	//lista koja cuva istoriju naredbi
	private ArrayList<String> listory;
	//indeks trenutne naredbe u listi
	private int commandInex;
	
	public CommandPanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		listory = new ArrayList<String>();
		
		initGUI();
	}
	
	public void initGUI() {
		
		previousLines = new JTextArea();
		previousLines.setFont(new Font("Monospaced",Font.PLAIN,15));
		previousLines.setEditable(false);
		
		consoleScroll = new JScrollPane(previousLines);
		
		currentLine = new JTextField();
		currentLine.setFont(new Font("Monospaced",Font.PLAIN,15));
		
		
		//*********************************************************************************
		//                                         								  LISTENERI
		//*********************************************************************************
		previousLines.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				//kada se klikne na JTextArea,
				//JTextField dobije fokus
				currentLine.requestFocusInWindow();
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
			}
		});
		
		currentLine.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				//Kada se stisne ENTER, parsira se unesena komanda
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!currentLine.getText().equals("")) {
						previousLines.append(">> " + currentLine.getText());
						previousLines.append(parseCommand(currentLine.getText()));
						previousLines.setCaretPosition(previousLines.getDocument().getLength());
						listory.add(currentLine.getText());
						commandInex = listory.size();
						currentLine.setText("");
					}
					//strelicama GORE i DOLE se krecemo kroz istoriju naredbi
				}else if(arg0.getKeyCode() == KeyEvent.VK_UP) {
					if(!listory.isEmpty()) {
						if(commandInex > 0) {
							commandInex--;
						}
						currentLine.setText(listory.get(commandInex));
					}
				}else if(arg0.getKeyCode() == KeyEvent.VK_DOWN) {
					if(!listory.isEmpty()) {
						if(commandInex < listory.size()-1) {
							commandInex++;
							currentLine.setText(listory.get(commandInex));
						}else if (commandInex == listory.size()-1) {
							//ako je dosao do kraja liste naredbi kada stisne DOLE, ispise se prazan red
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
	//               									   METODA ZA PARSIRANJE KOMANDI
	//*********************************************************************************
	public String parseCommand(String command) {
		String ret = "\n[KROKI] Error parsing command!\n";
		
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
	// 	 															  METODE ZA KOMANDE
	//*********************************************************************************	
	
	public String makeProjectCommand(String command) {
		Scanner sc = new Scanner(command);
		Pattern pattern = Pattern.compile("\"([^\"]+)\"");
	    String token = sc.findInLine(pattern);
	    if(token != null) {
	    	String name = token.substring(1, token.length()-1);
	    	BussinesSubsystem bss = new BussinesSubsystem(name, true, ComponentType.MENU, null);
	    	KrokiMockupToolApp.getInstance().getWorkspace().addPackage(bss);
            KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
	    	return "\n[KROKI] Project " + token  + " created successfully\n";
	    }else {
	    	return "\n[KROKI] Error parsing command!\n";
	    }
	}
	
	public String makePackageCommand(String command) {
		Pattern patt = Pattern.compile("\"([^\"]*)\" in \"([^\"]*)\"");
		String project;
		String pack;
		
		Matcher matcher = patt.matcher(command);
		if(matcher.find()) {
			if(matcher.groupCount() > 0) {
				pack = matcher.group(1);
				project = matcher.group(2);
				
				BussinesSubsystem owner = getOwnerPackage(project);
				makePackage(owner, pack);
				
				return "\n[KROKI] Subsystem \"" + pack  + "\" created successfully in \"" + project + "\"\n";
			}
		}
		return "\n[KROKI] Error parsing command!\n";
	}
	
	public String makeStdPanelCommand(String command) {
		Pattern patt = Pattern.compile("\"([^\"]+)\" in \"([^\"]+)\"(?: \\{(.+?)\\})?");
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
				
				return "\n[KROKI] Standard panel \"" + panel  + "\" created successfully in \"" + pack + "\"\n";
			}
		}
		return "\n[KROKI] Error parsing command!\n";
	}
	
	public String displayHelp(String command) {
		String help = "";
		if(command.equals("help")) {
			help = "\n[KROKI] Available commands:" +
					"\n\t\t1. make project" +
					"\n\t\t2. make package" +
					"\n\t\t3. make std-panel"+
					"\n\t\t4. clear" +
					"\nFor help on specific command, type \"help command name\" (i.e. help make project)\n";
		}else if(command.equals("help make project")) {
			help = "\n[KROKI] make project command" +
					"\n\tSyntax: make project \"Project name\"" +
					"\n\tDescription: Creates new project with specified name in workspace." +
					"\n\tExample:" + 
					"\n\t\tmake project \"Resources\"" + 
					"\n\t\tCreates new project named \"Resources\" in workspace\n";
		}else if(command.equals("help make package")) {
			help = "\n[KROKI] make package command" +
					"\n\tSyntax: make package \"Package name\" in \"Path\"" +
					"\n\tDescription: Creates new package with specified name in specified path." +
					"\n\t\t     Path specifies hierarchial order of parent packages starting from project in workspace" +
					"\n\t\t     and ending with immediate parent package, separated by /." +
					"\n\tExample:" + 
					"\n\t\tmake package \"Workers\" in \"Resources/Human Resources\"" + 
					"\n\t\tCreates new package named \"Workers\" in package named \"Human Resources\" in project named \"Resources\"." +
					"\n\t\tNOTE: If any project or package in path is not found, it will be created.\n";
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
					"\n\t\tmake std-panel \"Workers\" in \"Resources/Human resources\" {textfield-First name, textfield-Last name, textarea-Address, checkbox-Married}\n";
		}else {
			help = "\n[KROKI] no help for command \"" + command.substring(5) + "\"\n";
		}
		return help;
	}
	
	//*********************************************************************************
	//																  	 POMOCNE METODE
	//*********************************************************************************
	/**
	 * Pronalazi paket ili projekat na osnovu imena
	 * @param projectName ime projekta ili paketa na osnovu kojeg se vrsi pretraga
	 * @param recursion ako je true, pretrazuje sve pakete u hijerarhiju, ako je false
	 * pretrazuje samo projekte (prvi nivo hijerarhije)
	 * @return pronadjeni paket ili null ako nema paketa sa prosledjenim imenom
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
	 * Pronalazi paket unutar prosledjenog paketa na osnovu imena
	 * @param childName ime paketa na osnovu kojeg se vrsi pretraga
	 * @param owner paket unutar kojeg se vrsi pretraga
	 * @return pronadjeni paket ili null ako nema paketa sa prosledjenim imenom
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
	 * Kreira projekat sa zeljenim nazivom unutar workspacea
	 * @param projectName ime novog projekta
	 * @return kreirani projekat
	 */
	public BussinesSubsystem makeProject(String projectName) {
		BussinesSubsystem project = new BussinesSubsystem(projectName, true, ComponentType.MENU, null);
    	KrokiMockupToolApp.getInstance().getWorkspace().addPackage(project);
        KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
        return project;
	}
	
	/**
	 * Kreira paket unutar prosledjenog paketa ili projekta
	 * @param owner paket ili projekat unutar kojega ce se kreirati novi paket
	 * @param packageName ime novog paketa
	 * @return kreirani paket
	 */
	public BussinesSubsystem makePackage(BussinesSubsystem owner, String packageName) {
		BussinesSubsystem pack = new BussinesSubsystem(owner);
		pack.setLabel(packageName);
		owner.addNestedPackage(pack);
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
		return pack;
	}
	
	/**
	 * Kreira standardni panel unutar prosledjenog paketa
	 * @param owner paket u kojem se kreira panel
	 * @param label naziv panela
	 * @param components spisak gui komponenti koje ce biti na kreiranom panelu
	 * @return kreirani panel
	 */
	public VisibleClass makeStdPanel(BussinesSubsystem owner, String label, String[] components) {
		VisibleClass panel = new StandardPanel();
		CamelCaser cc = new CamelCaser();
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
	 * Kreiranje GUI elemenata
	 * @param label labela uz element
	 * @param visible da li ce element biti vidljiv na panelu
	 * @param type tip komponente koji se kreira
	 * @param panel panel na kojem se kreira komponenta
	 * @param group grupa unutar panela unutar koje se kreira komponenta (0-toolbar, 1-Properties, 2-Operations)
	 */
	public void makeVisibleProperty(String label, boolean visible, ComponentType type, VisibleClass panel, int group) {
		VisibleProperty property = new VisibleProperty(label, visible, type);
		panel.addVisibleElement(property);
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(group);
		gr.addVisibleElement(property);
		//TODO Vidi sta se ovde buni
		//property.getComponent().setAbsolutePosition(gr.getComponent().getAbsolutePosition());
		gr.update();
		panel.update();
	}
	
	/**
	 * Na osnovu putanje nalazi (ili kreira ako ne postoji) hijerarhiju paketa i vraca poslendnji paket u putanji.
	 * Koristi se za komande 'make package' i 'make std-panel'
	 * @param path putanja u hijerarhiji na kojoj se nalazi paket.
	 *        putanje su oblika 'Projekat/Paket/Paket/...'
	 * @return pronadjeni ili kreirani poslednji paket u navedenoj putanji
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
	
}