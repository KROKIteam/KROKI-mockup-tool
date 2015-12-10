package kroki.app;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import kroki.app.action.AboutAction;
import kroki.app.action.CopyAction;
import kroki.app.action.CutAction;
import kroki.app.action.DBConneectionSettingsAction;
import kroki.app.action.ExitAction;
import kroki.app.action.ExportEclipseProjectAction;
import kroki.app.action.ExportEclipseUMLDiagramAction;
import kroki.app.action.ExportSwingAction;
import kroki.app.action.ExportWebAction;
import kroki.app.action.HelpAction;
import kroki.app.action.ImportEclipseUMLDiagramAction;
import kroki.app.action.NewFileAction;
import kroki.app.action.NewProjectAction;
import kroki.app.action.OpenFileAction;
import kroki.app.action.OpenProjectAction;
import kroki.app.action.PasteAction;
import kroki.app.action.RedoAction;
import kroki.app.action.RunSwingAction;
import kroki.app.action.RunWebAction;
import kroki.app.action.SaveAction;
import kroki.app.action.SaveAllAction;
import kroki.app.action.SaveAsAction;
import kroki.app.action.UndoAction;
import kroki.app.gui.GuiManager;
import kroki.app.gui.console.CommandPanel;
import kroki.app.gui.console.OutputPanel;
import kroki.app.gui.settings.SettingsFactory;
import kroki.app.model.Workspace;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.app.utils.uml.OperationsUtil;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;

import org.apache.commons.io.FileDeleteStrategy;

/**
 * Kroki mockup tool main frame
 * @author Kroki Team
 */
public class KrokiMockupToolFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GuiManager guiManager;
	/*******************/
	/*MAIN COMPONENTS*/
	/*******************/
	private JPanel topPanel;
	/**Component which splits the content of the main panel into two parts*/ 
	private JSplitPane mainSplitPane;
	/**Main menu*/
	private JMenuBar mainMenuBar;
	/**Status bar*/
	private JPanel statusBar;
	/**Component which splits the central part of the frame into two parts - drawing canvas and command console*/
	private JSplitPane canvasSplitPane;
	/***************************************/
	/*Components contained by the main panel*/
	/***************************************/
	/**Main toolbar*/
	private JToolBar mainToolbar;
	/**Component which represents the tabular panel which will contain the hierarchically organize structure of the project.
	 * It is placed on the left side of the {@link mainSplitPane}*/
	private JTabbedPane treeTabbedPane;
	/**Component represents tabular panel which will contain the canvas. 
	 * It is placed on the right side of the {@link mainSplitPane}*/
	private JTabbedPane canvasTabbedPane;
	/**Tree which shows the hierarchical structured of the opened projects*/
	private JTree tree;
	/**Command console and output window*/
	private JTabbedPane consoleTabbedPane;
	private CommandPanel console;
	private OutputPanel outputPanel;
	/**Components which splits the left part of the {@link mainSplitPane} into two parts*/
	private JSplitPane leftSplitPane;
	/***********************************/
	/*Components contained by the main menu*/
	/***********************************/
	private CutAction cutAction;
	private CopyAction copyAction;
	private PasteAction pasteAction;
	/************************************/
	/*Status bar components*/
	/************************************/
	private JLabel statusMessage;

	private SettingsFactory settingFactory = new SettingsFactory();

	/**Constructor of the {@code KrokiMockupToolFrame} class*/
	public KrokiMockupToolFrame(GuiManager guiManager) {
		super();
		this.guiManager = guiManager;
		initMainComponents();
		Image headerIcon = ImageResource.getImageResource("app.logo32x32");
		setIconImage(headerIcon);
		setTitle(StringResource.getStringResource("app.header"));
	}

	/**
	 * Initializes main components
	 */
	private void initMainComponents() {
		topPanel = new JPanel();
		topPanel.setName("topPanel");
		topPanel.setLayout(new BorderLayout());
		initMainMenu();
		initToolbars();
		addMenuBar(topPanel);

		statusBar = new JPanel();
		statusBar.setName("statusBar");
		initStatusBar();
		addStatusBar(statusBar);

		mainSplitPane = new JSplitPane();
		mainSplitPane.setName("mainSplitPane");
		initMainPanel();
		addContent(mainSplitPane);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				//get temporary location in KROKI directory
				File f = new File(".");
				String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1) + "Temp";
				File tempDir = new File(appPath);
				deleteFiles(tempDir);

				//save projects
				if (KrokiMockupToolApp.getInstance().getWorkspace().getPackageCount() == 0)
		    		System.exit(1);
		    	
		    	int answer = JOptionPane.showConfirmDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(),
		    			"Save changes before closing the application?", "?", JOptionPane.YES_NO_CANCEL_OPTION);

		    	if (answer == JOptionPane.NO_OPTION)
		    		System.exit(1);
		    	else if (answer == JOptionPane.YES_OPTION){
		    		KrokiMockupToolApp.getInstance().getWorkspace().saveAllProjects();
		    		System.exit(1);
		    	}
		    	else {} //canceled
			}
		});
	}

	/**
	 * Initializes the main panel.
	 */
	private void initMainPanel() {
		mainSplitPane.setDividerSize(2);
		mainSplitPane.setResizeWeight(0.3f);

		treeTabbedPane = new JTabbedPane();
		treeTabbedPane.setName("hierarchyTabbedPane");

		tree = new JTree();
		tree.addKeyListener(new KeyAdapter() {


			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_DELETE){
					int result = JOptionPane.showConfirmDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(),
							StringResource.getStringResource("action.delete.message"),StringResource.getStringResource("action.delete.title"),
							JOptionPane.YES_NO_OPTION);

					if (result == JOptionPane.YES_OPTION) {
						for (TreePath path : tree.getSelectionModel().getSelectionPaths()){
							Object node = path.getLastPathComponent();
							if (node instanceof VisibleElement)
								OperationsUtil.delete((VisibleElement) node);
						}
					}

				}

			}
		});



		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {

						Object node = tree.getLastSelectedPathComponent();
						if (node == null) 
							return;
						if (node instanceof VisibleElement)
							settingFactory.treeUpdatePerformed((VisibleElement) node);

					}
				});
			}
			});
		
		treeTabbedPane.addTab(StringResource.getStringResource("app.tab.hierarchy.label"), new ImageIcon(ImageResource.getImageResource("app.tab.hierarchy.icon")), new JScrollPane(tree));
		consoleTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		
		console = new CommandPanel();
		outputPanel = new OutputPanel();
		consoleTabbedPane.addTab("Command window", new ImageIcon(ImageResource.getImageResource("app.tab.console.icon")), console);
		consoleTabbedPane.addTab("Message log", new ImageIcon(ImageResource.getImageResource("app.tab.output.icon")), outputPanel);
		
		canvasSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		canvasSplitPane.setDividerSize(2);
		canvasSplitPane.setResizeWeight(0.8d);

		canvasTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		canvasTabbedPane.setName("canvasTabbedPane");

		leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		leftSplitPane.setResizeWeight(0.5d);
		leftSplitPane.setDividerSize(5);
		leftSplitPane.setLeftComponent(treeTabbedPane);

		canvasSplitPane.setTopComponent(canvasTabbedPane);
		canvasSplitPane.setBottomComponent(consoleTabbedPane);

		mainSplitPane.setLeftComponent(leftSplitPane);
		mainSplitPane.setRightComponent(canvasSplitPane);
		}

		/**
		 * Initializes the main menu.
		 */
		private void initMainMenu() {
			mainMenuBar = new JMenuBar();
			mainMenuBar.setName("menuBar");

			cutAction = new CutAction();
			pasteAction = new PasteAction();
			copyAction = new CopyAction();

			JMenu file = new JMenu();
			file.setName("file");
			file.setText(StringResource.getStringResource("menu.file.name"));

			file.add(new NewProjectAction());
			file.add(new NewFileAction());
			file.addSeparator();
			file.add(new OpenProjectAction());
			file.add(new OpenFileAction());
			file.addSeparator();
			file.add(new SaveAction());
			file.add(new SaveAsAction());
			file.add(new SaveAllAction());
			file.addSeparator();
			
			JMenu importFileMenu=new JMenu();
			importFileMenu.setName("import");
			importFileMenu.setText(StringResource.getStringResource("menu.file.submenu.import"));
			file.add(importFileMenu);

			importFileMenu.add(new ImportEclipseUMLDiagramAction());

			JMenu exportFileMenu=new JMenu();
			exportFileMenu.setName("export");
			exportFileMenu.setText(StringResource.getStringResource("menu.file.submenu.export"));
			file.add(exportFileMenu);

			exportFileMenu.add(new ExportEclipseUMLDiagramAction(true,true));
			exportFileMenu.add(new ExportEclipseUMLDiagramAction(true,false));

			exportFileMenu.add(new ExportEclipseUMLDiagramAction(false,true));
			exportFileMenu.add(new ExportEclipseUMLDiagramAction(false,false));


			file.addSeparator();

			file.add(new ExitAction());

			JMenu edit = new JMenu();
			edit.setName("edit");
			edit.setText(StringResource.getStringResource("menu.edit.name"));

			edit.add(new UndoAction());
			edit.add(new RedoAction());
			edit.addSeparator();

			edit.add(cutAction);
			edit.add(copyAction);
			edit.add(pasteAction);

			JMenu export = new JMenu();
			export.setName("export");
			export.setText("Export...");
			export.add(new ExportSwingAction());
			export.add(new ExportWebAction());
			export.addSeparator();
			export.add(new ExportEclipseProjectAction());

			JMenu run = new JMenu();
			run.setName("run");
			run.setText("Run...");
			run.add(new RunSwingAction());
			run.add(new RunWebAction());

			JMenu project = new JMenu();
			project.setName("project");
			project.setText(StringResource.getStringResource("menu.project.name"));
			project.add(export);
			project.addSeparator();
			project.add(run);
			project.addSeparator();
			project.add(new DBConneectionSettingsAction());

			JMenu help = new JMenu();
			help.setName("help");
			help.setText(StringResource.getStringResource("menu.help.name"));

			help.add(new HelpAction());
			help.add(new AboutAction());

			mainMenuBar.add(file);
			mainMenuBar.add(edit);
			mainMenuBar.add(project);
			mainMenuBar.add(help);
			topPanel.add(mainMenuBar, BorderLayout.NORTH);
		}

		/**
		 * Initializes the toolbars.
		 */
		private void initToolbars() {
			mainToolbar = new JToolBar(JToolBar.HORIZONTAL);
			mainToolbar.setRollover(false);
			mainToolbar.setFloatable(false);


			JToolBar runToolbar = new JToolBar(JToolBar.HORIZONTAL);
			runToolbar.add(new RunSwingAction());
			runToolbar.add(new RunWebAction());

			mainToolbar.add(guiManager.getMainToolbar());
			mainToolbar.add(runToolbar);
			mainToolbar.add(guiManager.getStyleToolbar());
			topPanel.add(mainToolbar, BorderLayout.CENTER);

			this.add(guiManager.getPallete(), BorderLayout.EAST);
		}

		/**
		 * Initializes the status bar
		 */
		private void initStatusBar() {
			statusMessage = new JLabel();
			statusMessage.setName(StringResource.getStringResource("status.message.name"));
			statusMessage.setText(StringResource.getStringResource("status.message.text"));
			statusBar.setLayout(new FlowLayout(FlowLayout.LEFT));
			statusBar.add(statusMessage);
		}

		/**
		 * Adds the main panel to the frame
		 * @param c Main panel
		 */
		private void addContent(JComponent c) {
			this.add(c, BorderLayout.CENTER);
		}

		/**
		 * Adds the main menu
		 * @param c Man menu
		 */
		private void addMenuBar(JComponent c) {
			this.add(c, BorderLayout.NORTH);
		}

		/**
		 * Adds the status bar
		 * @param c Status bar
		 */
		private void addStatusBar(JComponent c) {
			this.add(c, BorderLayout.SOUTH);
		}

		/**
		 * Returns currently selected project from workspace with regard to these rules:
		 * 	- If a project is selected, returns it
		 * 	- If package or panel is selected, finds containing project and returns it
		 *  - If nothing is selected and only one project exists in workspace, return that project
		 * @return Currently selected project
		 */
		public BussinesSubsystem getCurrentProject() {
			BussinesSubsystem proj = null;

			TreePath path = getTree().getSelectionPath();
			if(path != null) {
				Object node = path.getLastPathComponent();
				//if package is selected, find parent project
				if(node instanceof BussinesSubsystem) {
					BussinesSubsystem subsys = (BussinesSubsystem) node;
					proj = KrokiMockupToolApp.getInstance().findProject(subsys);
				}else if(node instanceof VisibleClass) {
					//if panel is selected, get parent node from tree and find project
					Object parent = getTree().getSelectionPath().getParentPath().getLastPathComponent();
					if(parent instanceof BussinesSubsystem) {
						proj = KrokiMockupToolApp.getInstance().findProject((BussinesSubsystem)parent);
					}
				}
			}else {
				Workspace workspace = KrokiMockupToolApp.getInstance().getWorkspace();
				if(workspace.getPackageCount() == 1) {
					UmlPackage pack = workspace.getPackageAt(0);
					if(pack instanceof BussinesSubsystem) {
						proj = (BussinesSubsystem) pack;
					}
				}
			}

			return proj;
		}
	
	// Delete contents of temp directory
	public static boolean deleteFiles(File directory) {
		boolean success = false;

		if (!directory.exists()) {
			return false;
		}
		if (!directory.canWrite()) {
			return false;
		}

		File[] files = directory.listFiles();
		for(int i=0; i<files.length; i++) {
			File file = files[i];
			if(file.isDirectory()) {
				deleteFiles(file);
			}
			try {
				FileDeleteStrategy.FORCE.delete(file);
				success =  !file.delete();
			} catch (IOException e) {
				System.err.println(e.getMessage());
				//e.printStackTrace();
			} 
		}
		return success;
	}
	

		/*****************/
		/*GETTERS AND SETTERS*/
		/*****************/
		public JTabbedPane getCanvasTabbedPane() {
			return canvasTabbedPane;
		}

		public void setCanvasTabbedPane(JTabbedPane canvasTabbedPane) {
			this.canvasTabbedPane = canvasTabbedPane;
		}

		public JTabbedPane getTreeTabbedPane() {
			return treeTabbedPane;
		}

		public void setTreeTabbedPane(JTabbedPane hierarchyTabbedPane) {
			this.treeTabbedPane = hierarchyTabbedPane;
		}

		public JSplitPane getMainSplitPane() {
			return mainSplitPane;
		}

		public void setMainSplitPane(JSplitPane mainSplitPane) {
			this.mainSplitPane = mainSplitPane;
		}

		public JToolBar getMainToolbar() {
			return mainToolbar;
		}

		public void setMainToolbar(JToolBar mainToolbar) {
			this.mainToolbar = mainToolbar;
		}

		public JMenuBar getMainMenuBar() {
			return mainMenuBar;
		}

		public void setMainMenuBar(JMenuBar mainMenuBar) {
			this.mainMenuBar = mainMenuBar;
		}

		public JTree getTree() {
			return tree;
		}

		public void setTree(JTree projectHierarchy) {
			this.tree = projectHierarchy;
		}

		public JPanel getStatusBar() {
			return statusBar;
		}

		public void setStatusBar(JPanel statusBar) {
			this.statusBar = statusBar;
		}

		public JLabel getStatusMessage() {
			return statusMessage;
		}

		public void setStatusMessage(JLabel statusMessage) {
			this.statusMessage = statusMessage;
		}

		public JPanel getTopPanel() {
			return topPanel;
		}

		public void setTopPanel(JPanel topPanel) {
			this.topPanel = topPanel;
		}

		public JSplitPane getLeftSplitPane() {
			return leftSplitPane;
		}

		public void setLeftSplitPane(JSplitPane leftSplitPane) {
			this.leftSplitPane = leftSplitPane;
		}

		public void setConsole(CommandPanel console) {
			this.console = console;
		}

		public CutAction getCutAction() {
			return cutAction;
		}

		public void setCutAction(CutAction cutAction) {
			this.cutAction = cutAction;
		}

		public PasteAction getPasteAction() {
			return pasteAction;
		}

		public void setPasteAction(PasteAction pasteAction) {
			this.pasteAction = pasteAction;
		}

		public OutputPanel getOutputPanel() {
			return outputPanel;
		}

		public JTabbedPane getConsoleTabbedPane() {
			return consoleTabbedPane;
		}
	}
