package graphedit.app;

import graphedit.actions.ActionController;
import graphedit.actions.edit.CopyAction;
import graphedit.actions.edit.CopyHereAction;
import graphedit.actions.edit.CreateShortcutHereAction;
import graphedit.actions.edit.CutAction;
import graphedit.actions.edit.PasteAction;
import graphedit.actions.edit.PrepareShortcutAction;
import graphedit.actions.edit.RedoAction;
import graphedit.actions.edit.SelectAllAction;
import graphedit.actions.edit.SelectInverseAction;
import graphedit.actions.edit.ShortcutAction;
import graphedit.actions.edit.UndoAction;
import graphedit.actions.file.CloseAllDiagramsAction;
import graphedit.actions.file.CloseDiagramAction;
import graphedit.actions.file.ExitAction;
import graphedit.actions.file.ExportAction;
import graphedit.actions.file.NewProjectAction;
import graphedit.actions.file.SaveProjectAction;
import graphedit.actions.help.AboutAction;
import graphedit.actions.help.ContentsAction;
import graphedit.actions.help.IndexAction;
import graphedit.actions.pallete.AggregationLinkButtonAction;
import graphedit.actions.pallete.AssociationLinkButtonAction;
import graphedit.actions.pallete.ClassButtonAction;
import graphedit.actions.pallete.CompositionLinkButtonAction;
import graphedit.actions.pallete.DependencyLinkButtonAction;
import graphedit.actions.pallete.GeneralizationLinkButtonAction;
import graphedit.actions.pallete.InnerLinkButtonAction;
import graphedit.actions.pallete.InterfaceButtonAction;
import graphedit.actions.pallete.PackageButtonAction;
import graphedit.actions.pallete.RealizationLinkButtonAction;
import graphedit.actions.pallete.RequireLinkButtonAction;
import graphedit.actions.pallete.SelectButtonAction;
import graphedit.actions.popup.ElementPopupMenu;
import graphedit.actions.popup.LinkPopupMenu;
import graphedit.actions.popup.MainToolBarPopupMenu;
import graphedit.actions.popup.MoveElementPopup;
import graphedit.actions.popup.PackagePopupMenu;
import graphedit.actions.popup.PopupListener;
import graphedit.actions.popup.ProjectPopupMenu;
import graphedit.actions.popup.ViewPopupMenu;
import graphedit.actions.popup.WorkspacePopupMenu;
import graphedit.actions.view.BestFitZoomAction;
import graphedit.actions.view.FullScreenAction;
import graphedit.actions.view.LassoZoomAction;
import graphedit.actions.view.PreferencesAction;
import graphedit.actions.view.ShowGridAction;
import graphedit.actions.view.StandardToolbarAction;
import graphedit.command.CommandManager;
import graphedit.gui.listeners.ZoomSliderListener;
import graphedit.gui.table.PropertiesButtonMouseListener;
import graphedit.gui.table.PropertiesTable;
import graphedit.gui.table.PropertiesTableModel;
import graphedit.gui.table.PropertiesTableRenderer;
import graphedit.gui.tree.WorkspaceTree;
import graphedit.model.ClipboardManager;
import graphedit.model.GraphEditWorkspace;
import graphedit.model.components.AggregationLink;
import graphedit.model.components.AssociationLink;
import graphedit.model.components.Class;
import graphedit.model.components.CompositionLink;
import graphedit.model.components.DependencyLink;
import graphedit.model.components.GeneralizationLink;
import graphedit.model.components.GraphElement;
import graphedit.model.components.InnerLink;
import graphedit.model.components.Interface;
import graphedit.model.components.Link;
import graphedit.model.components.Package;
import graphedit.model.components.RealizationLink;
import graphedit.model.components.RequireLink;
import graphedit.model.diagram.GraphEditModel;
import graphedit.model.elements.GraphEditPackage;
import graphedit.properties.ApplicationModeProperties;
import graphedit.properties.Preferences;
import graphedit.util.WorkspaceUtility;
import graphedit.view.AggregationLinkPainter;
import graphedit.view.AssociationLinkPainter;
import graphedit.view.ClassPainter;
import graphedit.view.CompositionLinkPainter;
import graphedit.view.ContainerPanel;
import graphedit.view.DependencyLinkPainter;
import graphedit.view.ElementPainter;
import graphedit.view.GeneralizationLinkPainter;
import graphedit.view.GraphEditView;
import graphedit.view.GraphEditViewMini;
import graphedit.view.InnerLinkPainter;
import graphedit.view.InterfacePainter;
import graphedit.view.PackagePainter;
import graphedit.view.RealizationLinkPainter;
import graphedit.view.RequireLinkPainter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Observer;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreePath;

import net.miginfocom.swing.MigLayout;


public class MainFrame extends JDialog{

	private static final long serialVersionUID = 1L;
	private JMenuBar mainMenu;
	private JToolBar mainToolBar, auxiliaryToolBar;
	private JToggleButton aggregationLinkButton,associationLinkButton,compositionLinkButton,
	dependencyLinkButton,generalizationLinkButton,innerLinkButton,realizationLinkButton,requireLinkButton;
	private JToggleButton interfaceButton, classButton, selectButton, lassoZoomButton, packageButton;
	private JMenu fileMenu, editMenu, viewMenu, helpMenu;
	private ViewPopupMenu viewPopupMenu;
	private LinkPopupMenu linkPopupMenu;

	private WorkspacePopupMenu workspacePopupMenu;
	private ProjectPopupMenu projectPopupMenu;
	private PackagePopupMenu packagePopupMenu; 
	//private DiagramPopupMenu diagramPopupMenu;
	private ElementPopupMenu elementPopupMenu;
	private MoveElementPopup moveElementPopup;

	private MainToolBarPopupMenu mainToolBarPopupMenu;
	private JCheckBoxMenuItem standardToolBar;
	private JCheckBoxMenuItem showGridMenuItem;
	private JLabel positionTrack, statusLabel, statusTrack;
	private JSplitPane mainSplitPane, leftSplitPane;
	private JScrollPane mainTreeScrollPane, propertiesScrollPane;
	private PropertiesTable propertiesTable;
	private JTree mainTree;
	private JTabbedPane mainTabbedPane;
	private JPanel statusBar, southPanel, statusPanel;
	private JSlider zoomSlider;
	private static MainFrame singletonMain;	
	// File Actions
	private NewProjectAction newProjectAction; 
	private SaveProjectAction saveProjectAction; 
	private ExportAction exportAction;
	private CloseDiagramAction closeDiagramAction;
	private CloseAllDiagramsAction closeAllDiagramsAction;
	private ExitAction exitDiagramAction;
	// Edit Actions
	private CutAction cutDiagramAction; 
	private CopyAction copyDiagramAction; 
	private PasteAction pasteDiagramAction;
	private SelectAllAction selectAllAction;
	private SelectInverseAction selectInverseAction;
	private UndoAction undoAction;
	private RedoAction redoAction;
	private PrepareShortcutAction prepareShortcutAction;
	private ShortcutAction shortcutAction;
	private CopyHereAction copyHereAction;
	private CreateShortcutHereAction createShortcutHere;
	// View Actions
	private StandardToolbarAction standardToolbarDiagramAction;
	private BestFitZoomAction bestFitZoomAction;
	private ShowGridAction showGridAction;
	private PreferencesAction preferencesAction;
	private FullScreenAction fullScreenAction;
	// Help Actions
	private IndexAction indexDiagramAction;
	private ContentsAction contentsDiagramAction;
	private AboutAction aboutDiagramAction;

	// Action Controller
	private ActionController actionController;

	// Prefs singleton instance
	private Preferences preferences; 
	private ApplicationModeProperties properties;

	private ApplicationMode appMode = ApplicationMode.USER_INTERFACE;


	//********************************* NOVO ***************************************
	private JSplitPane rightSplitPane;
	private JSplitPane elementsSplitPane; 

	private JPanel palletePanel;
	private GraphEditViewMini miniView;
	private JScrollPane viewScrollPane;

	private Dimension buttonSize = new Dimension(150,27);

	//******************************************************************************



	// Graphical interface related enums
	public static enum ToolSelected { INTERFACE_SELECTED, CLASS_SELECTED, PACKAGE_SELECTED, SELECTION, AGGREGATION,ASSOCIATION,COMPOSITION,
		DEPENDENCY,GENERALIZATION,INNERLINK,REALIZATION,REQUIRE };
		public static enum EventSource { TREE_VIEW, GRAPHICAL_VIEW, MAIN_TOOLBAR, AUXILIARY_TOOLBAR };

		private static final int SLIDER_SCALE_FACTOR = 100;

		// Zoom Slider parameters
		public static final int ZOOM_MIN = 20;
		public static final int ZOOM_MAX = 500;
		public static final int ZOOM_INIT = 100;
		public static final String MAINFRAME_TITLE = "Graph Edit v1.2";

		public MainFrame() {
			setSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize());
			this.setTitle(MAINFRAME_TITLE);
			setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			this.preferences = Preferences.getInstance();
			setModal(true);
		}


		public void setAppMode(ApplicationMode appMode){
			this.appMode = appMode;
			properties = ApplicationModeProperties.getInstance();
			guiInit();
		}


		private void guiInit() {
			actionController = new ActionController();
			// File Actions
			newProjectAction = new NewProjectAction();		
			saveProjectAction = new SaveProjectAction();
			exportAction = new ExportAction();
			closeDiagramAction = new CloseDiagramAction();
			closeAllDiagramsAction = new CloseAllDiagramsAction();
			exitDiagramAction = new ExitAction();	
			// Edit Actions
			cutDiagramAction = new CutAction();		
			copyDiagramAction = new CopyAction();		
			pasteDiagramAction = new PasteAction();
			selectAllAction = new SelectAllAction();
			selectInverseAction = new SelectInverseAction();
			undoAction = new UndoAction();
			redoAction = new RedoAction();
			prepareShortcutAction = new PrepareShortcutAction();
			shortcutAction = new ShortcutAction();
			copyHereAction = new CopyHereAction();
			createShortcutHere = new CreateShortcutHereAction(); 
			// View Actions
			standardToolbarDiagramAction = new StandardToolbarAction();
			bestFitZoomAction = new BestFitZoomAction();
			preferencesAction = new PreferencesAction();
			fullScreenAction = new FullScreenAction();
			showGridAction = new ShowGridAction();

			showGridMenuItem = new JCheckBoxMenuItem(showGridAction);

			// Help Actions
			indexDiagramAction = new IndexAction();
			contentsDiagramAction = new ContentsAction();
			aboutDiagramAction = new AboutAction();
			// Initialize Menu Components
			mainMenu = new JMenuBar();
			mainToolBar = new JToolBar();
			auxiliaryToolBar = new JToolBar();

			rightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			rightSplitPane.setOneTouchExpandable(true);
			mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			mainSplitPane.setOneTouchExpandable(true);
			leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			leftSplitPane.setOneTouchExpandable(true);

			// Initialize Zoom Slider
			zoomSlider = new JSlider(JSlider.HORIZONTAL, 
					(int) Math.round(Double.parseDouble(preferences.getProperty(Preferences.MIN_ZOOM))), 
					(int) Math.round(Double.parseDouble(preferences.getProperty(Preferences.MAX_ZOOM)) * SLIDER_SCALE_FACTOR), 
					(int) Math.round(SLIDER_SCALE_FACTOR));
			zoomSlider.setEnabled(false);

			mainMenuInit();
			mainToolBarInit();
			auxiliaryToolBarInit();
			mainTreeInit();
			mainTabbedPaneInit();
			statusBarInit();
			initZoomSlider();
			initElementsPanel();
			// Lay Components Out
			this.setJMenuBar(mainMenu);
			this.setLayout(new BorderLayout());
			this.add(mainToolBar, BorderLayout.NORTH);
			this.add(southPanel, BorderLayout.SOUTH);
			this.add(mainSplitPane, BorderLayout.CENTER);
			// Fill The SplitPane
			mainSplitPane.add(leftSplitPane);
			mainSplitPane.add(rightSplitPane);
			rightSplitPane.add(mainTabbedPane);
			rightSplitPane.add(elementsSplitPane);


			mainSplitPane.setDividerLocation(220);
			mainSplitPane.setFocusable(true);
			mainTabbedPane.setFocusable(true);
			// Initialize PopUp Menus
			workspacePopupMenu = new WorkspacePopupMenu();
			projectPopupMenu = new ProjectPopupMenu();
			packagePopupMenu = new PackagePopupMenu();
			elementPopupMenu = new ElementPopupMenu();
			linkPopupMenu = new LinkPopupMenu();

			viewPopupMenu = new ViewPopupMenu(cutDiagramAction, copyDiagramAction, pasteDiagramAction, prepareShortcutAction, shortcutAction, showGridAction, undoAction, redoAction);
			moveElementPopup = new MoveElementPopup(copyHereAction, createShortcutHere);
			mainToolBarPopupMenu = new MainToolBarPopupMenu();
			// Subscribe Listeners For PopUp Events
			mainTree.addMouseListener(new PopupListener(EventSource.TREE_VIEW));
			mainToolBar.addMouseListener(new PopupListener(EventSource.MAIN_TOOLBAR));
			auxiliaryToolBar.addMouseListener(new PopupListener(EventSource.AUXILIARY_TOOLBAR));

			this.addWindowListener(new WindowAdapter() {

				public void windowClosing(WindowEvent winEvt) {
					new ExitAction().actionPerformed(null);
					singletonMain = null;
				}
			});
		}

		public JSplitPane getMainSplitPane() {
			return mainSplitPane;
		}

		public void setMainSplitPane(JSplitPane mainSplitPane) {
			this.mainSplitPane = mainSplitPane;
		}


		public static MainFrame getInstance() {
			//return GraphEditWorkspace.getInstance().getMainFrame();
			return ((singletonMain instanceof MainFrame)) ?  singletonMain : (singletonMain = new MainFrame());
		}

		public void setParametrizedTitle(String title) {
			this.setTitle(MAINFRAME_TITLE + " - " + title);
		}


		public void initElementsPanel(){
			palletePanel = new JPanel();
			viewScrollPane = new JScrollPane();
			elementsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, viewScrollPane, palletePanel);
			palletePanel.setLayout(new MigLayout("wrap 1, center")); 
			palleteInit();
			elementsSplitPane.setDividerLocation(220);
			rightSplitPane.setDividerLocation(getWidth()-430);
		}

		public void initZoomSlider() {
			zoomSlider.addChangeListener(new ZoomSliderListener());
			zoomSlider.setMajorTickSpacing(100);
			zoomSlider.setMinorTickSpacing(1);
			zoomSlider.setSize(200, 15);
			zoomSlider.setPaintTicks(false);
			zoomSlider.setPaintLabels(false);

		}

		private void statusBarInit() {
			southPanel = new JPanel(new BorderLayout());
			statusPanel = new JPanel();
			statusBar = new JPanel(new BorderLayout());
			statusBar.add(positionTrack, BorderLayout.EAST);
			statusPanel.add(zoomSlider);
			statusPanel.add(statusLabel);
			statusPanel.add(statusTrack);
			statusBar.add(statusPanel, BorderLayout.WEST);
			//statusBar.add(zoomSlider, BorderLayout.CENTER);
			southPanel.add(auxiliaryToolBar, BorderLayout.NORTH);
			southPanel.add(statusBar, BorderLayout.SOUTH);
		}

		private void mainMenuInit() {
			fileMenu = new JMenu("File");
			fileMenu.setMnemonic(KeyEvent.VK_F);
			fileMenu.add(newProjectAction);
			fileMenu.addSeparator();
			fileMenu.add(saveProjectAction);
			fileMenu.addSeparator();
			fileMenu.add(exportAction);
			fileMenu.addSeparator();
			fileMenu.add(closeDiagramAction);
			fileMenu.add(closeAllDiagramsAction);
			fileMenu.addSeparator();
			fileMenu.add(exitDiagramAction);
			editMenu = new JMenu("Edit");
			editMenu.setMnemonic(KeyEvent.VK_E);
			editMenu.add(undoAction);
			editMenu.add(redoAction);
			editMenu.addSeparator();
			editMenu.add(selectAllAction);
			editMenu.add(selectInverseAction);
			editMenu.addSeparator();
			editMenu.add(cutDiagramAction);
			editMenu.add(copyDiagramAction);
			editMenu.add(pasteDiagramAction);
			editMenu.add(prepareShortcutAction);
			editMenu.add(shortcutAction);
			viewMenu = new JMenu("View");
			viewMenu.setMnemonic(KeyEvent.VK_V);
			standardToolBar = new JCheckBoxMenuItem(standardToolbarDiagramAction);
			standardToolBar.setSelected(true);
			viewMenu.add(bestFitZoomAction);
			viewMenu.add(fullScreenAction);
			viewMenu.addSeparator();
			viewMenu.add(standardToolBar);
			viewMenu.add(showGridMenuItem);
			viewMenu.addSeparator();
			viewMenu.add(preferencesAction);
			showGridMenuItem.setEnabled(false);
			showGridMenuItem.setSelected(true);
			helpMenu = new JMenu("Help");
			helpMenu.setMnemonic(KeyEvent.VK_H);
			helpMenu.add(indexDiagramAction);
			helpMenu.add(contentsDiagramAction);
			helpMenu.add(aboutDiagramAction);
			mainMenu.add(fileMenu);
			mainMenu.add(editMenu);
			mainMenu.add(viewMenu);
			mainMenu.add(helpMenu);
		}
		private void mainToolBarInit() {
			mainToolBar.setFloatable(false);
			mainToolBar.add(newProjectAction);
			mainToolBar.addSeparator();
			mainToolBar.add(saveProjectAction);
			mainToolBar.addSeparator();
			mainToolBar.add(exportAction);
			mainToolBar.addSeparator();
			mainToolBar.add(closeDiagramAction);
			mainToolBar.add(closeAllDiagramsAction);
			mainToolBar.addSeparator();
			mainToolBar.add(selectAllAction);
			mainToolBar.add(selectInverseAction);
			mainToolBar.addSeparator();
			mainToolBar.add(undoAction);
			mainToolBar.add(redoAction);
			mainToolBar.addSeparator();
			mainToolBar.add(cutDiagramAction);
			mainToolBar.add(copyDiagramAction);
			mainToolBar.add(pasteDiagramAction);
			mainToolBar.addSeparator();
			mainToolBar.add(prepareShortcutAction);
			mainToolBar.add(shortcutAction);
			mainToolBar.addSeparator();
			mainToolBar.add(bestFitZoomAction);
			mainToolBar.addSeparator();
			mainToolBar.add(fullScreenAction);
			//mainToolBar.addSeparator();
			//mainToolBar.add(switchWorkspaceAction);
		}
		private void auxiliaryToolBarInit() {
			auxiliaryToolBar.addSeparator();
			positionTrack = new JLabel("(0.0, 0.0)");

			statusTrack = new JLabel("Ready");
			statusTrack.setForeground(Color.BLUE);
			statusLabel = new JLabel("Current state: ");
			auxiliaryToolBar.add(positionTrack);

		}

		private void palleteInit(){


			ButtonGroup abstractGroup = new ButtonGroup();

			interfaceButton = new JToggleButton(new InterfaceButtonAction());
			classButton = new JToggleButton(new ClassButtonAction());
			packageButton = new JToggleButton(new PackageButtonAction());
			selectButton = new JToggleButton(new SelectButtonAction());
			aggregationLinkButton = new JToggleButton(new AggregationLinkButtonAction());
			associationLinkButton = new JToggleButton(new AssociationLinkButtonAction());
			compositionLinkButton = new JToggleButton(new CompositionLinkButtonAction());
			dependencyLinkButton = new JToggleButton(new DependencyLinkButtonAction());
			generalizationLinkButton = new JToggleButton(new GeneralizationLinkButtonAction());
			innerLinkButton = new JToggleButton(new InnerLinkButtonAction());
			realizationLinkButton = new JToggleButton(new RealizationLinkButtonAction());
			requireLinkButton = new JToggleButton(new RequireLinkButtonAction());
			lassoZoomButton = new JToggleButton(new LassoZoomAction());

			abstractGroup.add(interfaceButton);
			abstractGroup.add(classButton);
			abstractGroup.add(packageButton);
			abstractGroup.add(selectButton);
			abstractGroup.add(aggregationLinkButton);
			abstractGroup.add(associationLinkButton);
			abstractGroup.add(compositionLinkButton);
			abstractGroup.add(dependencyLinkButton);
			abstractGroup.add(generalizationLinkButton);
			abstractGroup.add(innerLinkButton);
			abstractGroup.add(realizationLinkButton);
			abstractGroup.add(requireLinkButton);

			abstractGroup.add(lassoZoomButton);

			Enumeration<AbstractButton> tmpE = abstractGroup.getElements();
			while(tmpE.hasMoreElements()) {
				AbstractButton curBtn = tmpE.nextElement();
				JToggleButton button = (JToggleButton) curBtn;
				button.setPreferredSize(buttonSize);
				button.setMinimumSize(buttonSize);
				button.setAlignmentX(LEFT_ALIGNMENT);
				button.setHorizontalAlignment(SwingConstants.LEFT);
			}


			JLabel label = new JLabel("Palette");
			Font font = label.getFont();
			Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
			label.setFont(boldFont);

			palletePanel.add(label);
			if ((Boolean) properties.getPropertyValue("showInterfaceButton")){
				palletePanel.add(interfaceButton);
			}
			if ((Boolean) properties.getPropertyValue("showClassButton")){
				palletePanel.add(classButton);
			}
			if ((Boolean) properties.getPropertyValue("showPackageButton")){
				palletePanel.add(packageButton);
			}
			palletePanel.add(lassoZoomButton);
			palletePanel.add(selectButton);

			if ((Boolean) properties.getPropertyValue("showAssociationButton")){
				palletePanel.add(associationLinkButton);
			}
			if ((Boolean) properties.getPropertyValue("showAggregationButton")){
				palletePanel.add(aggregationLinkButton);
			}
			if ((Boolean) properties.getPropertyValue("showCompositionButton")){
				palletePanel.add(compositionLinkButton);
			}

			if ((Boolean) properties.getPropertyValue("showGeneralizationButton")){
				palletePanel.add(generalizationLinkButton);
			}
			if ((Boolean) properties.getPropertyValue("showDependencyButton")){
				palletePanel.add(dependencyLinkButton);
			}
			if ((Boolean) properties.getPropertyValue("showInnerButton")){
				palletePanel.add(innerLinkButton);
			}
			if ((Boolean) properties.getPropertyValue("showRealizationButton")){
				palletePanel.add(realizationLinkButton);
			}
			if ((Boolean) properties.getPropertyValue("showRequireButton")){
				palletePanel.add(requireLinkButton);
			}
			selectButton.setSelected(true);
		}

		private void mainTreeInit() {
			// kreiranje pocetne workspace hijerarhije
			GraphEditWorkspace workspace = GraphEditWorkspace.getInstance();
			// kreiranje stabla i povezivanje sa modelom
			mainTree = new WorkspaceTree(workspace);
			mainTree.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					TreePath selPath = mainTree.getPathForLocation(e.getX(), e.getY());
					if (selPath != null) {
						Object source = selPath.getLastPathComponent();
						//show diagram if clicked twice on a package
						if (e.getClickCount() == 2) {
							if (source instanceof GraphEditPackage) {
								GraphEditPackage pack = (GraphEditPackage) source;
								showDiagram(pack.getDiagram());
							}
						} else {

							// do the same for new package
							if (source instanceof GraphEditPackage) {
								packagePopupMenu.getNewPackageAction().setEnabled(true);
								newProjectAction.setEnabled(true);
							} else {
								packagePopupMenu.getNewPackageAction().setEnabled(false);
							}

							if (source instanceof GraphEditWorkspace)
								newProjectAction.setEnabled(true);
							else
								newProjectAction.setEnabled(false);

						}
					}
				}
			});

			// inicijalno otvaranje putanje stabla
			//Object[] pathToDiagram = new Object[3];
			Object[] pathToDiagram = new Object[1];
			pathToDiagram[0] = workspace;
			/*pathToDiagram[1] = project;
		pathToDiagram[2] = diagram;*/
			mainTree.makeVisible(new TreePath(pathToDiagram));

			mainTreeScrollPane = new JScrollPane(mainTree);
			leftSplitPane.add(mainTreeScrollPane);
			propertiesTable = new PropertiesTable(new PropertiesTableModel());
			propertiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			propertiesTable.setDefaultRenderer(Object.class, new PropertiesTableRenderer());
			propertiesTable.addMouseListener(new PropertiesButtonMouseListener(propertiesTable));
			//propertiesTable.setDefaultRenderer

			//propertiesTable.setDefaultRenderer(Color.class, new ColorCellRenderer());
			propertiesScrollPane = new JScrollPane(propertiesTable);
			propertiesTable.setFillsViewportHeight(true);
			leftSplitPane.add(propertiesScrollPane);
			leftSplitPane.setResizeWeight(0.75);
		}

		public ProjectPopupMenu getProjectPopupMenu() {
			return projectPopupMenu;
		}

		public void setProjectPopupMenu(ProjectPopupMenu projectPopupMenu) {
			this.projectPopupMenu = projectPopupMenu;
		}

		private void mainTabbedPaneInit() {		
			mainTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

			mainTabbedPane.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					GraphEditView view = getCurrentView();
					if (view != null) {
						if (view.getSelectedTool() == ToolSelected.SELECTION) selectButton.setSelected(true);
						else if (view.getSelectedTool() == ToolSelected.INTERFACE_SELECTED) interfaceButton.setSelected(true);
						else classButton.setSelected(true);

						miniView = view.getMiniView();
						viewScrollPane.setViewportView(miniView);
						viewScrollPane.revalidate();
						viewScrollPane.repaint();

					}

					// enable switching to another model
					if (getCurrentView() != null && getCurrentView().getModel() instanceof GraphEditModel) {
						getCurrentView().getSelectionModel().fireUpdates();
						((PropertiesTableModel)propertiesTable.getModel()).reassignSelectionModel();
						actionController.setSelectionModel(getCurrentView().getSelectionModel());
						actionController.setModel(getCurrentView().getModel());
					}

					if (mainTabbedPane.getTabCount() > 0) {
						zoomSlider.setEnabled(true);
						bestFitZoomAction.setEnabled(true);
						fullScreenAction.setEnabled(true);
						exportAction.setEnabled(true);
						selectAllAction.setEnabled(true);
						selectInverseAction.setEnabled(true);
					}
					else {
						zoomSlider.setEnabled(false);
						bestFitZoomAction.setEnabled(false);
						fullScreenAction.setEnabled(false);
						exportAction.setEnabled(false);
						selectAllAction.setEnabled(false);
						selectInverseAction.setEnabled(false);
					}

					// enable or disable, considering the diagram state
					boolean closeable = isCloseable();
					saveProjectAction.setEnabled(isMarkedWithAsterisk());
					closeAllDiagramsAction.setEnabled(closeable);
					closeDiagramAction.setEnabled(closeable);
					showGridMenuItem.setEnabled(closeable);
				}
			});
		}

		public JTabbedPane getMainTabbedPane() {
			return this.mainTabbedPane;
		}

		public JToolBar getMainToolBar() {
			return this.mainToolBar;
		}

		public JToolBar getAuxiliaryToolBar() {
			return auxiliaryToolBar;
		}

		public JCheckBoxMenuItem getStandardToolBar() {
			return standardToolBar;
		}

		public ViewPopupMenu getViewPopupMenu() {
			return viewPopupMenu;
		}
		public MoveElementPopup getMoveElementPopup(){
			return moveElementPopup;
		}
		public ElementPopupMenu getElementPopupMenu() {
			return elementPopupMenu;
		}

		public JPopupMenu getMainPopupMenu(Object node) {
			if (node instanceof GraphEditWorkspace) {
				return workspacePopupMenu;
			}
			else if (node instanceof GraphEditPackage){
				return packagePopupMenu;
			} else if (node instanceof GraphElement) {
				return elementPopupMenu;
			} else {
				return null;
			}
		}

		public MainToolBarPopupMenu getMainToolBarPopupMenu() {
			return mainToolBarPopupMenu;
		}

		public ProjectPopupMenu getPopupMenu() {
			return projectPopupMenu;
		}

		public void setPositionTrack(int x, int y) {
			if (positionTrack != null)
				positionTrack.setText("(" + x + ", " + y + ")");
		}

		public void setStatusTrack(String state) {
			statusTrack.setText(state);
		}

		/**
		 * Ako je neki dijagram selektovan vraca njegov CommandManager, inace vraca mainCommandManager.
		 * @return
		 */
		public CommandManager getCommandManager() {
			GraphEditView view = this.getCurrentView();
			return (view != null ? view.getModel().getCommandManager() : null);
		}

		public GraphEditView getCurrentView() {
			if (mainTabbedPane.getComponentCount() == 0) {
				return null;
			}

			if (mainTabbedPane.getSelectedComponent() == null)
				return null;

			return ((ContainerPanel) mainTabbedPane.getSelectedComponent()).getView();
		}

		public GraphEditView getViewContaining(GraphElement element){
			GraphEditView view;
			for (int i = 0; i<mainTabbedPane.getComponentCount(); i++){
				if (mainTabbedPane.getComponent(i) instanceof ContainerPanel){
					view = ((ContainerPanel) mainTabbedPane.getComponent(i)).getView();
					if (view.getModel().getDiagramElements().contains(element))
						return view;
				}
			}
			return null;
		}

		public JToggleButton getUsecaseButton() {
			return interfaceButton;
		}

		public JToggleButton getClassButton() {
			return classButton;
		}

		public JToggleButton getSelectButton() {
			return selectButton;
		}

		public JTree getMainTree() {
			return mainTree;
		}

		/**
		 * This method is invoked whenever color preferences change.
		 * Only open views are to be changed, however all views are affected.
		 * @author specijalac
		 */
		public void updateColorsForAllOpenViews() {
			Preferences p = Preferences.getInstance();

			Color classColor1 = p.parseColor(Preferences.CLASS_COLOR_1);
			Color classColor2 = p.parseColor(Preferences.CLASS_COLOR_2);
			Color interfaceColor1 = p.parseColor(Preferences.INTERFACE_COLOR_1);
			Color interfaceColor2 = p.parseColor(Preferences.INTERFACE_COLOR_2);
			GraphEditView view = null;
			for (Component c : mainTabbedPane.getComponents()) {
				if (c instanceof GraphEditView) {
					view = (GraphEditView) c;
					for (ElementPainter e : view.getElementPainters()) {
						if (e instanceof InterfacePainter) {
							e.setFillColor1(interfaceColor1);
							e.setFillColor2(interfaceColor2);
						} else if (e instanceof ClassPainter) {
							e.setFillColor1(classColor1);					
							e.setFillColor2(classColor2);					
						}
					}
					view.repaint();
				}
			}
		}

		public void showDiagram(GraphEditModel diagram) {
			GraphEditView view = null;
			ContainerPanel container = null;
			for (Component c : mainTabbedPane.getComponents()) {
				if (c instanceof ContainerPanel) {
					if (((ContainerPanel) c).getView().getModel().equals(diagram)) {
						view = ((ContainerPanel) c).getView();
						container = ((ContainerPanel) c);
					}
				}
			}

			if (view == null) {
				view = new GraphEditView(diagram);
				container = new ContainerPanel(view);

				// diagram is just deserialized, generete its painters on the fly
				if (diagram.getDiagramElements().size() != 0 && view.getElementPainters().size() == 0)
					generatePainters(view);

				view.generatePackagePainters();
				mainTabbedPane.addTab(diagram.toString(), null, container, null);
				diagram.addObserver((Observer) mainTree);



			}


			miniView = view.getMiniView();
			viewScrollPane.setViewportView(miniView);
			viewScrollPane.revalidate();
			viewScrollPane.repaint();



			mainTabbedPane.setSelectedComponent(container);

			// dodaj selection model kako bi ga table model observe-ovao
			((PropertiesTableModel)propertiesTable.getModel()).reassignSelectionModel();
			actionController.setSelectionModel(getCurrentView().getSelectionModel());
			actionController.setModel(getCurrentView().getModel());
			container.requestFocusInWindow();

			view.repaint();



		}

		public void closeDiagram(GraphEditModel diagram, boolean restore) {
			List<GraphEditModel> diagrams = new ArrayList<GraphEditModel>();

			// restore diagram's state
			if (restore) diagram.getCommandManager().restoreDiagram();

			diagrams.add(diagram);
			closeDiagrams(diagrams);
		}

		public void closeAllDiagrams(boolean save, boolean restore) {
			Component c;
			for (int i = 0; i < mainTabbedPane.getTabCount(); i++) {
				c = mainTabbedPane.getComponentAt(i);

				if (save)  
					if (isMarkedWithAsterisk(i)) WorkspaceUtility.save(((GraphEditView)c).getModel());
				if (restore)
					(((GraphEditView)c).getModel()).getCommandManager().restoreDiagram();

			}
			mainTabbedPane.removeAll();

			if (mainTabbedPane.getTabCount() > 0) {
				mainTabbedPane.setSelectedIndex(0);
				mainTabbedPane.getComponentAt(0).requestFocusInWindow();
			}

			zoomSlider.setEnabled(false);
			bestFitZoomAction.setEnabled(false);
			exportAction.setEnabled(false);
			closeAllDiagramsAction.setEnabled(false);
			closeDiagramAction.setEnabled(false);
			showGridMenuItem.setEnabled(false);
			selectAllAction.setEnabled(false);
			selectInverseAction.setEnabled(false);
		}

		public void closeDiagrams(List<GraphEditModel> diagrams) {
			boolean removed = false;
			GraphEditModel diagram;

			// Sinhronizacija zbog StatusRefresher-a
			synchronized (this) {
				for (Component c : mainTabbedPane.getComponents()) {
					if (c instanceof ContainerPanel) {
						diagram = ((ContainerPanel) c).getView().getModel();

						if (diagrams.contains(diagram)) {
							mainTabbedPane.remove(c);
							removed = true;
						}
					}
				}

				if (removed && mainTabbedPane.getTabCount() > 0) {
					mainTabbedPane.setSelectedIndex(0);
					mainTabbedPane.getComponentAt(0).requestFocusInWindow();
				}
			}
		}


		public GraphEditView getOpenDiagram(GraphEditModel model){
			for (Component c : getMainTabbedPane().getComponents()){
				if (c instanceof ContainerPanel) {
					if (((ContainerPanel) c).getView().getModel() == model)
						return ((ContainerPanel) c).getView();
				}
			}
			return null;
		}

		private void generatePainters(GraphEditView view) {
			for (GraphElement element : view.getModel().getDiagramElements()) {
				ElementPainter painter;
				if (element instanceof Interface) { 
					painter = new InterfacePainter(element);
					view.addElementPainter(painter);
					((InterfacePainter)painter).updateShape();
				} else if (element instanceof Class) {
					painter = new ClassPainter(element);
					view.addElementPainter(painter);
					((ClassPainter)painter).updateShape();
				}
				else if (element instanceof Package){
					painter = new PackagePainter(element);
					view.addElementPainter(painter);
				}
			}
			for (Link link : view.getModel().getLinks()) {
				if (link instanceof CompositionLink) {
					view.addLinkPainter(new CompositionLinkPainter(link));
				} else if (link instanceof AggregationLink) {
					view.addLinkPainter(new AggregationLinkPainter(link));
				} else if (link instanceof AssociationLink) {
					view.addLinkPainter(new AssociationLinkPainter(link));
				} else if (link instanceof DependencyLink) {
					view.addLinkPainter(new DependencyLinkPainter(link));
				} else if (link instanceof GeneralizationLink) {
					view.addLinkPainter(new GeneralizationLinkPainter(link));
				} else if (link instanceof InnerLink) {
					view.addLinkPainter(new InnerLinkPainter(link));
				} else if (link instanceof RealizationLink) {
					view.addLinkPainter(new RealizationLinkPainter(link));
				} else if (link instanceof RequireLink) {
					view.addLinkPainter(new RequireLinkPainter(link));
				}
			}
		}


		public void prepareTable(boolean clazz){
			if (appMode == ApplicationMode.USER_INTERFACE && clazz){
				propertiesTable.setElementSelected(true);
			}
			else
				propertiesTable.setElementSelected(false);
		}


		/**
		 * This method acquires singleton instance of <code>ClipboardManager</code>
		 * @return clipboardManager
		 * @author specijalac
		 */
		public ClipboardManager getClipboardManager() {
			if (getCurrentView() instanceof GraphEditView)
				ClipboardManager.getInstance().setView(getCurrentView());
			return ClipboardManager.getInstance();
		}



		public int incrementClassCounter() {
			if(getCurrentView() == null){
				return 0;
			}
			int result = getCurrentView().getModel().getClassCounter();
			getCurrentView().getModel().setClassCounter(++result);
			return result;
		}

		public int incrementInterfaceCounter() {
			if(getCurrentView() == null){
				return 0;
			}
			int result = getCurrentView().getModel().getInterfaceCounter();
			getCurrentView().getModel().setInterfaceCounter(++result);
			return result;
		}



		public int incrementPackageCounter(){
			if(getCurrentView() == null){
				return 0;
			}
			int result = getCurrentView().getModel().getPackageCounter();
			getCurrentView().getModel().setPackageCounter(++result);
			return result;
		}

		public int incrementLinkCounter() {
			if(getCurrentView() == null){
				return 0;
			}
			int result = getCurrentView().getModel().getLinkCounter();
			getCurrentView().getModel().setLinkCounter(++result);
			return result;
		}

		public int getClassCounter() {
			if(getCurrentView() == null){
				return 0;
			}
			int result = getCurrentView().getModel().getClassCounter();
			return result;
		}

		public int getClassInterface() {
			if(getCurrentView() == null){
				return 0;
			}
			int result = getCurrentView().getModel().getInterfaceCounter();
			return result;
		}

		public int getLinkCounter() {
			if(getCurrentView() == null){
				return 0;
			}
			int result = getCurrentView().getModel().getLinkCounter();
			return result;
		}

		public CutAction getCutDiagramAction() {
			return cutDiagramAction;
		}

		public CopyAction getCopyDiagramAction() {
			return copyDiagramAction;
		}

		public PasteAction getPasteDiagramAction() {
			return pasteDiagramAction;
		}

		public SelectAllAction getSelectAllAction() {
			return selectAllAction;
		}

		public SelectInverseAction getSelectInverseAction() {
			return selectInverseAction;
		}

		public UndoAction getUndoAction() {
			return undoAction;
		}

		public RedoAction getRedoAction() {
			return redoAction;
		}

		public BestFitZoomAction getBestFitZoomAction() {
			return bestFitZoomAction;
		}

		public FullScreenAction getFullScreenAction() {
			return fullScreenAction;
		}

		public void updateZoomSlider(double factor) {
			zoomSlider.setValue((int) Math.round(factor * SLIDER_SCALE_FACTOR));
		}


		public SaveProjectAction getSaveProjectAction() {
			return saveProjectAction;
		}

		public CloseAllDiagramsAction getCloseAllDiagramsAction() {
			return closeAllDiagramsAction;
		}

		public CloseDiagramAction getCloseDiagramAction() {
			return closeDiagramAction;
		}

		public JCheckBoxMenuItem getShowGridMenuItem() {
			return showGridMenuItem;
		}

		/**
		 * This method marks currently active tab with asterisk
		 * meaning that its corresponding model has been changed.
		 * @author specijalac
		 */
		public void markTabWithAsterisk() {
			int selectedIndex = mainTabbedPane.getSelectedIndex();
			if (selectedIndex != -1) {
				mainTabbedPane.setTitleAt(selectedIndex, "*" + mainTabbedPane.getTitleAt(selectedIndex));

			}
		}

		/**
		 * Asterisk is not a valid character for naming diagrams.
		 * So it makes a perfect use for marking changed models.
		 * @author specijalac
		 */
		public void removeAsteriskFromTab() {
			int selectedIndex = mainTabbedPane.getSelectedIndex();
			if (selectedIndex != -1) {
				if (mainTabbedPane.getTitleAt(selectedIndex).startsWith("*"))
					mainTabbedPane.setTitleAt(selectedIndex, mainTabbedPane.getTitleAt(selectedIndex).substring(1));

			}
		}

		/**
		 * Iterates over the open tabs and removes existing asterisks.
		 * @author specijalac
		 */
		public void removeAsteriskFromAllTabs() {
			for (int i = 0; i < mainTabbedPane.getTabCount(); i++)
				if (mainTabbedPane.getTitleAt(i).startsWith("*"))
					mainTabbedPane.setTitleAt(i, mainTabbedPane.getTitleAt(i).substring(1));
		}

		/**
		 * Removes asterisk from all tabs showing diagrams from project 
		 * @param project
		 */
		public void removeAsteriskFromAllTabsContainingToProject(GraphEditPackage project){
			List<GraphEditModel> diagrams = WorkspaceUtility.allDiagramsInProject(project);
			int j = 0;
			for (int i = 0; i < mainTabbedPane.getComponentCount(); i++){
				if (mainTabbedPane.getComponent(i) instanceof ContainerPanel){
					GraphEditView view = ((ContainerPanel) mainTabbedPane.getComponent(i)).getView();
					if (diagrams.contains(view.getModel())){
						if (mainTabbedPane.getTitleAt(i - j).startsWith("*"))
							mainTabbedPane.setTitleAt(i - j, mainTabbedPane.getTitleAt(i - j).substring(1));
					}
				}
				else
					j++;
			}
		}

		/**
		 * This method check whether the selected tab is marked with an
		 * asterisk. 
		 * @return whether the selected tab corresponds to diagram 
		 * <code>GraphEditModel</code> whose state hasn't been saved yet.
		 * @author specijalac
		 */
		public boolean isMarkedWithAsterisk() {
			int selectedIndex = mainTabbedPane.getSelectedIndex();
			if (selectedIndex != -1)
				if (mainTabbedPane.getTitleAt(selectedIndex).startsWith("*"))
					return true;
			return false;
		}

		public boolean isMarkedWithAsterisk(int index) {
			if (mainTabbedPane.getTitleAt(index).startsWith("*"))
				return true;
			return false;
		}

		public boolean isCloseable() {
			return mainTabbedPane.getTabCount() > 0;
		}

		public ApplicationMode getAppMode() {
			return appMode;
		}


		public PrepareShortcutAction getPrepareShortcutAction() {
			return prepareShortcutAction;
		}


		public ShortcutAction getShortcutAction() {
			return shortcutAction;
		}


		public CopyHereAction getCopyHereAction() {
			return copyHereAction;
		}


		public CreateShortcutHereAction getCreateShortcutHere() {
			return createShortcutHere;
		}


		public LinkPopupMenu getLinkPopupMenu() {
			return linkPopupMenu;
		}


}
