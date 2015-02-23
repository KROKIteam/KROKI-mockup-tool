/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

import org.apache.commons.io.FileDeleteStrategy;

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
import kroki.app.gui.settings.SettingsFactory;
import kroki.app.model.Workspace;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import kroki.app.utils.uml.OperationsUtil;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;

/**
 *
 * @author Vladan Marsenic (vladan.marsenic@gmail.com)
 */
public class KrokiMockupToolFrame extends JFrame {

	GuiManager guiManager;
	/*******************/
	/*GLAVNE KOMPONENTE*/
	/*******************/
	private JPanel topPanel;
	/**Komponenta koja deli sadrzaj glavnog panela na dva dela.*/
	private JSplitPane mainSplitPane;
	/**Glavni meni*/
	private JMenuBar mainMenuBar;
	/**Panel za ispisivanje trenutnog statusa*/
	private JPanel statusBar;
	/**splitPane sa canvasom i konzolom**/
	private JSplitPane canvasSplitPane;
	/***************************************/
	/*KOMPONENTE SADRZANE U GLAVNOM PANEL-u*/
	/***************************************/
	private JToolBar mainToolbar;
	/**Komponenta koja predstavlja tabulatorski panel u kojem ce se naci hijerarhijski uredjena struktura projekata. Levi deo mainSplitPane-a*/
	private JTabbedPane treeTabbedPane;
	/**Komponenta koja predstavlja tabulatorski panel u kojem ce se naci kanvas. Desni deo mainSplitPane-a*/
	private JTabbedPane canvasTabbedPane;
	/**Stablo kojim se prikazuje hijerarhija otvorenih projekata u datoj aplikaciji*/
	private JTree tree;

	private CommandPanel console;

	private JSplitPane leftSplitPane;
	/***********************************/
	/*KOMPONENTE SADRZANE U MAIN MENU-u*/
	/***********************************/
	private CutAction cutAction;
	private CopyAction copyAction;
	private PasteAction pasteAction;
	/************************************/
	/*KOMPONENTE SADRZANE U STATUS BAR-u*/
	/************************************/
	private JLabel statusMessage;

	private SettingsFactory settingFactory = new SettingsFactory();

	/**Konstruise {@code KrokiMockupToolFrame} bez parametara*/
	public KrokiMockupToolFrame(GuiManager guiManager) {
		super();
		this.guiManager = guiManager;
		initMainComponents();
		Image headerIcon = ImageResource.getImageResource("app.logo32x32");
		setIconImage(headerIcon);
		setTitle(StringResource.getStringResource("app.header"));
	}

	/************************************/
	/*PRIVATNE METODE ZA INICIJALIZACIJU*/
	/************************************/
	/**
	 * Inicijalizuje osnovne sastavne komponente
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
			}
		});
	}

	/**
	 * Inicijalizuje glavni panel.
	 */
	private void initMainPanel() {
		//debljina i pozicija dividera
		mainSplitPane.setDividerSize(2);
		mainSplitPane.setResizeWeight(0.2f);

		//tabbed pane sa tree
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
		//tabbed pane sa canvasom i panelom za podesavanja

		console = new CommandPanel();
		JLabel consoleLbl = new JLabel("Command Window");

		canvasSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		canvasSplitPane.setDividerSize(2);
		canvasSplitPane.setResizeWeight(0.8d);

		canvasTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		canvasTabbedPane.setName("canvasTabbedPane");

		leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		leftSplitPane.setResizeWeight(0.5d);
		leftSplitPane.setDividerSize(5);
		leftSplitPane.setLeftComponent(treeTabbedPane);

		JPanel consolePanel = new JPanel();
		consolePanel.setLayout(new BorderLayout());
		consolePanel.add(consoleLbl, BorderLayout.NORTH);
		consolePanel.add(console, BorderLayout.CENTER);

		canvasSplitPane.setTopComponent(canvasTabbedPane);
		canvasSplitPane.setBottomComponent(consolePanel);

		mainSplitPane.setLeftComponent(leftSplitPane);
		mainSplitPane.setRightComponent(canvasSplitPane);
		}

		/**
		 * Inicijalizuje glavni meni
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
			//Dodavanje dela za import i export
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
			//Kraj dela za dodavanje import i export

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
		 * Inicijalizuje toolbar
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
		 * Inicijalizuje status bar.
		 */
		private void initStatusBar() {
			statusMessage = new JLabel();
			statusMessage.setName("Status message");
			//TODO: staviti iz resource bunde-a
			statusMessage.setText("Status message");
			statusBar.setLayout(new FlowLayout(FlowLayout.LEFT));
			statusBar.add(statusMessage);
		}

		/**
		 * Dodaje komponentu na mesto glavnog panela.
		 * @param c glavni panel
		 */
		private void addContent(JComponent c) {
			this.add(c, BorderLayout.CENTER);
		}

		/**
		 * Dodaje komponentu na mesto glavnog menia
		 * @param c glavni meni
		 */
		private void addMenuBar(JComponent c) {
			this.add(c, BorderLayout.NORTH);
		}

		/**
		 * Dodaje komponentu na mesto status bara.
		 * @param c status bar.
		 */
		private void addStatusBar(JComponent c) {
			this.add(c, BorderLayout.SOUTH);
		}

		/**
		 * Returns currently selected project from workspace with regard to these rules:
		 * 	- If a project is selected, returns it
		 * 	- If package or panel is selected, finds containing project and returns it
		 *  - If nothing is selected and only one project exists in workspace, return that project
		 * @return
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
		/*GETERI I SETERI*/
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

		public CommandPanel getConsole() {
			return console;
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

	}
