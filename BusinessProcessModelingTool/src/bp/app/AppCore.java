package bp.app;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import bp.action.ExitAction;
import bp.details.AbstractDetails;
import bp.gui.BPPanel;
import bp.gui.MainMenu;
import bp.gui.PropertiesView;
import bp.gui.SideToolbar;
import bp.model.data.Process;

//public final class AppCore extends JFrame {
public final class AppCore extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6272641537629757811L;
	
	public static final String APP_TITLE = "Business Process Modeling Tool v1.1";
	
	private static AppCore instance;
	
	private File projectFile;
	
	private boolean runningStandalone = false;
	
	private boolean saveActionInvoked = false;
	
    private MainMenu mainMenu;
    private SideToolbar sideToolbar;
    private PropertiesView propertiesView;
    private JScrollPane propertiesScroll;
    private JPanel rightPanel;
    // private BPGraphicPanel bpPanel;
    private BPPanel bpPanel;
    
	public static AppCore getInstance() {
		if (instance == null) {
			instance = new AppCore();
		}
		return instance;
	}

	public static AppCore getABrandNewInstance() {
		instance = new AppCore();
		return instance;
	}
	
	private AppCore() {
		initialize();
	}
	
	private void initialize() {

        setSize(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize());
        setMinimumSize(new Dimension(1024, 576));
		setLocationRelativeTo(null);
		setTitle(APP_TITLE);
		
		setModal(true);
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        mainMenu = new MainMenu();
        setJMenuBar(mainMenu);

        sideToolbar = new SideToolbar();
        rightPanel = new JPanel(new BorderLayout());
        propertiesView = new PropertiesView();
        propertiesScroll = new JScrollPane(propertiesView);

        initializeView();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new ExitAction().actionPerformed(null);
			}
		});
	}

    private void initializeView() {
        setLayout(new BorderLayout());
        JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        splitter.add(propertiesScroll);
        splitter.add(rightPanel);
        splitter.setDividerLocation(270);

        rightPanel.add(sideToolbar, BorderLayout.EAST);

        add(splitter, BorderLayout.CENTER);

        validate();
    }

    public void updateDetails(AbstractDetails details) {
        propertiesView.removeAll();
        propertiesView.add(details);
        propertiesScroll.validate();
        propertiesScroll.repaint();
    }

    public void createBPPanel() {
    	createBPPanel("untitledProcess");
    }
    
    public void createBPPanel(String uniqueName) {
        if (bpPanel == null) {
            bpPanel = new BPPanel(uniqueName);
            rightPanel.add(bpPanel, BorderLayout.CENTER);
            rightPanel.validate();
        }
    }

    public void loadProcess(Process process) {
    	bpPanel = new BPPanel(process);
        rightPanel.add(bpPanel, BorderLayout.CENTER);
        rightPanel.validate();
    }

    public BPPanel getBpPanel() {
        return bpPanel;
    }

	public File getProjectFile() {
		return projectFile;
	}

	public void setProjectFile(File projectFile) {
		this.projectFile = projectFile;
	}

	public boolean isRunningStandalone() {
		return runningStandalone;
	}

	public void setRunningStandalone(boolean runningStandalone) {
		this.runningStandalone = runningStandalone;
	}

	public boolean isSaveActionInvoked() {
		return saveActionInvoked;
	}

	public void setSaveActionInvoked(boolean saveActionInvoked) {
		this.saveActionInvoked = saveActionInvoked;
	}
}
