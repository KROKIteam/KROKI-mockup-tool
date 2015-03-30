/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app;



import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import kroki.app.controller.TabbedPaneController;
import kroki.app.controller.TreeController;
import kroki.app.gui.GuiManager;
import kroki.app.model.ClipboardManager;
import kroki.app.model.ProjectHierarchyModel;
import kroki.app.model.Workspace;
import kroki.app.utils.ImageResource;
import kroki.app.utils.KrokiLookAndFeel;
import kroki.app.utils.StringResource;
import kroki.app.utils.TypeComponentMapper;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 *
 * @author Vladan Marsenic (vladan.marsenic@gmail.com)
 * ovo je sad izmenjeno
 */
public class KrokiMockupToolApp {

	/**
	 *
	 */
	private static KrokiMockupToolApp krokiMockupApp;
	private KrokiMockupToolFrame krokiMockupToolFrame;
	private TabbedPaneController tabbedPaneController;
	private TreeController projectHierarchyController;
	private ProjectHierarchyModel projectHierarchyModel;
	private Workspace workspace;
	private GuiManager guiManager;
	private static KrokiMockupToolSplashScreen splash = new KrokiMockupToolSplashScreen();;
	private boolean binaryRun = false;
	
	public KrokiMockupToolApp() {
		KrokiLookAndFeel.setLookAndFeel();
		guiManager = new GuiManager();
		krokiMockupToolFrame = new KrokiMockupToolFrame(guiManager);
		tabbedPaneController = new TabbedPaneController(krokiMockupToolFrame.getCanvasTabbedPane());
		workspace = new Workspace();

		JTree projectHierarchy = krokiMockupToolFrame.getTree();

		projectHierarchyController = new TreeController(projectHierarchy, workspace);
		projectHierarchyModel = new ProjectHierarchyModel(projectHierarchy, workspace);

		projectHierarchy.addMouseListener(projectHierarchyController);
		projectHierarchy.addKeyListener(projectHierarchyController);
		projectHierarchy.addTreeSelectionListener(projectHierarchyController);
		projectHierarchyModel.addTreeModelListener(projectHierarchyController);
		projectHierarchy.setModel(projectHierarchyModel);

		//JTree icons
		ImageIcon leafIcon = new ImageIcon(ImageResource.getImageResource("tree.leaf.icon"));
		ImageIcon openIcon = new ImageIcon(ImageResource.getImageResource("tree.open.icon"));
		ImageIcon closedIcon = new ImageIcon(ImageResource.getImageResource("tree.closed.icon"));

		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				ImageIcon rootIcon = new ImageIcon(ImageResource.getImageResource("tree.root.icon"));
				ImageIcon projectIcon = new ImageIcon(ImageResource.getImageResource("tree.project.icon"));
				//setting workspace icon
				if(value instanceof Workspace) {
					setIcon(rootIcon);
				}
				//project icons
				if(value instanceof BussinesSubsystem) {
					BussinesSubsystem proj = (BussinesSubsystem) value;
					Workspace workspace = KrokiMockupToolApp.getInstance().getWorkspace();
					//since both projects and packages are BussinesSubsystem instances
					//we need to change icons only for imediate childern of workspace (projects)
					if(krokiMockupApp.isProject(proj)) {
						setIcon(projectIcon);
					}
				}
				return this;
			}
		};

		renderer.setLeafIcon(leafIcon);
		renderer.setClosedIcon(closedIcon);
		renderer.setOpenIcon(openIcon);
		projectHierarchy.setCellRenderer(renderer);
		projectHierarchy.setEditable(true);
	}

	public static KrokiMockupToolApp getInstance() {
		if (krokiMockupApp == null) {
			krokiMockupApp = new KrokiMockupToolApp();
		}
		return krokiMockupApp;
	}

	public void launch() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				krokiMockupToolFrame.pack();
				GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
				krokiMockupToolFrame.setMaximizedBounds(e.getMaximumWindowBounds());
				krokiMockupToolFrame.setMinimumSize(new Dimension(600, 400));
				krokiMockupToolFrame.setSize(getPreferredSize());
				krokiMockupToolFrame.setExtendedState(krokiMockupToolFrame.getExtendedState() | JFrame.NORMAL);
				krokiMockupToolFrame.setLocationRelativeTo(null);
				krokiMockupToolFrame.toFront();
				krokiMockupToolFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				krokiMockupToolFrame.getStatusMessage().setText(StringResource.getStringResource("app.state.select"));
				splash.turnOffSplash();
				krokiMockupToolFrame.setVisible(true);
			}
		});
	}

	public void stop() {
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		KrokiMockupToolApp.getInstance().launch();
		TypeComponentMapper tcm = new TypeComponentMapper();
		tcm.getMappings();
	}

	public void displayTextOnConsole(String message, int type) {
		getKrokiMockupToolFrame().getConsole().displayText(message, type);
	}
	
	/*
	 * Returns dimension that is 80% of the screen size
	 */
	public Dimension getPreferredSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();

		int w = (int)Math.floor(width*0.8);
		int h = (int)Math.floor(height*0.8);
		return new Dimension(w, h);
	}
	

	//*****************************************[ SEARCH METHODS ]***************************************************

	//Finds project with specified label
	public BussinesSubsystem findProject(String label) {
		BussinesSubsystem project = null;
		for (int i=0; i<workspace.getPackageCount(); i++) {
			BussinesSubsystem proj = (BussinesSubsystem) workspace.getPackageAt(i);
			if(proj.getLabel().equals(label)) {
				return proj;
			}
		}
		return project;
	}
	
	//Finds project with specified file
		public BussinesSubsystem findProject(File file) {
			BussinesSubsystem project = null;
			for (int i=0; i<workspace.getPackageCount(); i++) {
				BussinesSubsystem proj = (BussinesSubsystem) workspace.getPackageAt(i);
				if(proj.getFile() != null && proj.getFile().getPath().equals(file.getPath())) {
					return proj;
				}
			}
			return project;
		}

	//Finds package with specified label inside specified project or package
	public BussinesSubsystem findPackage(String label, BussinesSubsystem owner) {
		//System.out.println("\tTRAZIM " + label + " UNUTAR " + owner.getLabel());
		for(int i=0; i<owner.ownedElementCount(); i++) {
			if(owner.getOwnedElementAt(i) instanceof BussinesSubsystem) {
				BussinesSubsystem p = (BussinesSubsystem) owner.getOwnedElementAt(i);
				if(p.getLabel().equalsIgnoreCase(label)) {
					//System.out.println("\tNASAO " + p.getLabel() + " UNUTAR " + owner.getLabel());
					return p;
				}else {
					//System.out.println("\tNISAM NASAO IDEM U " + p.getLabel());
					return findPackage(label, p);
				}
			}
		}
		return null;
	}


	//Finds owner project for specified package
	public BussinesSubsystem findProject(BussinesSubsystem pack) {
		if(isProject(pack)) {
			//if passed subsystem is project, return it
			return pack;
		}else {
			BussinesSubsystem owner = (BussinesSubsystem) pack.nestingPackage();
			if(isProject(owner)) {
				//if imediate parent is project, return it
				return owner;
			}else {
				//else, go one level up, and check owner's parents until one of them is project
				return findProject(owner);
			}
		}
	}

	//checks if business subsystem is project (returns false if it's package)
	public boolean isProject(BussinesSubsystem sub) {
		if(sub.nestingPackage() == null) {
			return true;
		}else {
			return false;
		}
	}

	public boolean isProject(String label) {
		if(findProject(label) != null) {
			return true;
		}else {
			return false;
		}
	}
	
	//***************************************************************************************************************

	
	public KrokiMockupToolFrame getKrokiMockupToolFrame() {
		return krokiMockupToolFrame;
	}

	public TabbedPaneController getTabbedPaneController() {
		return tabbedPaneController;
	}

	public void setTabbedPaneController(TabbedPaneController tabbedPaneController) {
		this.tabbedPaneController = tabbedPaneController;
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public TreeController getProjectHierarchyController() {
		return projectHierarchyController;
	}

	public void setProjectHierarchyController(TreeController projectHierarchyController) {
		this.projectHierarchyController = projectHierarchyController;
	}

	public ProjectHierarchyModel getProjectHierarchyModel() {
		return projectHierarchyModel;
	}

	public void setProjectHierarchyModel(ProjectHierarchyModel projectHierarchyModel) {
		this.projectHierarchyModel = projectHierarchyModel;
	}

	public GuiManager getGuiManager() {
		return guiManager;
	}

	public void setGuiManager(GuiManager guiManager) {
		this.guiManager = guiManager;
	}
	
	public boolean isBinaryRun() {
		return binaryRun;
	}

	public void setBinaryRun(boolean binaryRun) {
		if(!this.binaryRun) {
			this.binaryRun = binaryRun;
			System.out.println("[KROKI APP] Setting the binary run flag up.");
		}
	}

	public ClipboardManager getClipboardManager() {
		ClipboardManager.getInstance().setCanvas(getTabbedPaneController().getCurrentTabContent());
		return ClipboardManager.getInstance();
	}
}
