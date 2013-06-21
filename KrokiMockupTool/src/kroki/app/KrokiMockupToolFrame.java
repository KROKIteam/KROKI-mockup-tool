/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;

import kroki.app.action.AboutAction;
import kroki.app.action.DBConneectionSettingsAction;
import kroki.app.action.ExitAction;
import kroki.app.action.ExportSwingAction;
import kroki.app.action.ExportWebAction;
import kroki.app.action.HelpAction;
import kroki.app.action.NewFileAction;
import kroki.app.action.NewProjectAction;
import kroki.app.action.OpenFileAction;
import kroki.app.action.OpenProjectAction;
import kroki.app.action.RedoAction;
import kroki.app.action.RunSwingAction;
import kroki.app.action.RunWebAction;
import kroki.app.action.SaveAction;
import kroki.app.action.SaveAllAction;
import kroki.app.action.SaveAsAction;
import kroki.app.action.UndoAction;
import kroki.app.gui.GuiManager;
import kroki.app.gui.console.CommandPanel;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;

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
    /************************************/
    /*KOMPONENTE SADRZANE U STATUS BAR-u*/
    /************************************/
    private JLabel statusMessage;
    
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
        file.add(new ExitAction());

        JMenu edit = new JMenu();
        edit.setName("edit");
        edit.setText(StringResource.getStringResource("menu.edit.name"));

        edit.add(new UndoAction());
        edit.add(new RedoAction());
        edit.addSeparator();

        JMenu export = new JMenu();
        export.setName("export");
        export.setText("Export...");
        export.add(new ExportSwingAction());
        export.add(new ExportWebAction());
        
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
}
