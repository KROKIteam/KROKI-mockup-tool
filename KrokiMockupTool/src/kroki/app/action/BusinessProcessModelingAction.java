package kroki.app.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.TreePath;

import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.ImageResource;
import kroki.app.view.Canvas;
import kroki.profil.BusinessProcessModelingSubject;
import kroki.profil.VisibleElement;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;
import bp.app.AppCore;
import bp.util.WorkspaceUtility;
import bp.model.data.Process;

public class BusinessProcessModelingAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BussinesSubsystem project;
	
	private List<VisibleElement> choiceList;
	
	private VisibleElement initiallySelected;
	
	public BusinessProcessModelingAction() {
		putValue(NAME, "Business Process Modeling Tool");
		putValue(SHORT_DESCRIPTION, "Bring up the business process modeling tool");
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.businessProcessModelingAction.icon"));
		putValue(SMALL_ICON, smallIcon);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
		
        AppCore appCore = AppCore.getABrandNewInstance();
        
        /* Set the enclosing project file path... */
        project = getSelectedProject();
        File projectFile = project instanceof BussinesSubsystem ? project.getFile() : null; 
        appCore.setProjectFile(projectFile);
        
        if (projectFile != null) {
        	if (choiceList.size() > 0) {
        		Object choice = selectVisibleElement();
        		
        		/* User has made a choice */ 
        		if (choice != null) {
	        		BusinessProcessModelingSubject modelingSubject = (BusinessProcessModelingSubject) choice;
	        		
	        		System.out.println("Modeling subject: " + modelingSubject);
	        		System.out.println("Enclosing project file path: " + projectFile.getAbsolutePath());
	
	        		/* Unless a deep copy is used, user won't be able NOT (to choose) to save the changes */
	        		if (modelingSubject.getProcess() == null) {
	        			
	        			String uniqueName = modelingSubject.toString();
	        			Process process = WorkspaceUtility.load(appCore.getProjectFile(), uniqueName);
	        			
	        			if (process instanceof Process)      				
	        				appCore.loadProcess(process);
	        			else 
	        				appCore.createBPPanel(uniqueName);
	        		}
	        		//else appCore.loadProcess((Process) DeepCopy.copy(modelingSubject.getProcess()));
	        		else appCore.loadProcess(modelingSubject.getProcess());
		        	
	        		appCore.setVisible(true);
	        		
	        		/* User has opted to save the changes */
	        		if (appCore.isSaveActionInvoked()) {
	        			System.out.println("Changes are being saved... " + modelingSubject);
	        			modelingSubject.setProcess(appCore.getBpPanel().getProcess());
	        		} 
	        			
        		}
        		
        	} else {
        		JOptionPane.showMessageDialog(appCore, "Make sure that you have at least one form in the project!", TITLE, JOptionPane.WARNING_MESSAGE);
        	}
		} else {
			JOptionPane.showMessageDialog(appCore, "Make sure to select the project first!", TITLE, JOptionPane.WARNING_MESSAGE);
		}
	}
	
    /*
	private File findEnclosingProject() {
    	
    	//get selected item from jtree and find its project
    	BussinesSubsystem proj = null;
    	try {
	    	TreePath path =  KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath();
			Object node = path.getLastPathComponent();
			if(node != null) {
				//if package is selected, find parent project
				if(node instanceof BussinesSubsystem) {
					BussinesSubsystem subsys = (BussinesSubsystem) node;
					proj = KrokiMockupToolApp.getInstance().findProject(subsys);
				}else if(node instanceof VisibleClass) {
					//if panel is selected, get parent node from tree and find project
					JTree tree = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree();
					Object parent = tree.getSelectionPath().getParentPath().getLastPathComponent();
					if(parent instanceof BussinesSubsystem) {
						proj = KrokiMockupToolApp.getInstance().findProject((BussinesSubsystem)parent);
					}
				}
			}
    	} catch (Exception e) {
    		return null;
    	}
		return (proj instanceof BussinesSubsystem ? proj.getFile() : null);
    }
	*/
	
	private BussinesSubsystem getSelectedProject() {

		BussinesSubsystem selectedProject = null;
		VisibleClass selectedForm = null;
		VisibleElement selectedElement = null;
		choiceList = new ArrayList<>();
		initiallySelected = null;
		
		try {
	    	TreePath path =  KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath();
			Object node = path.getLastPathComponent();
			
			if (node instanceof BussinesSubsystem) {
				
				System.out.println("BusinessSubsystem (project) is selected.");
				selectedProject = KrokiMockupToolApp.getInstance().findProject((BussinesSubsystem) node);
				
				/* Find all forms in the project */
				VisibleElement element;
				
				for (int i = 0; i < selectedProject.ownedElementCount(); i++) {
					element = selectedProject.getOwnedElementAt(i);
					if (element instanceof VisibleClass)
						choiceList.add(element);
				}
				
			} else if (node instanceof VisibleClass) {
				
				System.out.println("VisibleClass (form) is selected.");
				selectedForm = (VisibleClass) node;
				
				System.out.println("Locating the BusinessSubsystem (project)...");
				JTree tree = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree();
				Object parent = tree.getSelectionPath().getParentPath().getLastPathComponent();
				if(parent instanceof BussinesSubsystem) {
					selectedProject = KrokiMockupToolApp.getInstance().findProject((BussinesSubsystem)parent);
				}			
				
				Canvas canvas = KrokiMockupToolApp.getInstance().getTabbedPaneController().getCurrentTabContent();

				/* Create a choice list */
				choiceList.add(selectedForm);
				for (VisibleElement element : selectedForm.getVisibleElementList())
					if (element instanceof VisibleOperation) 
						choiceList.add(element);

				/* Set the initially selected element */
				if (canvas.getSelectionModel().getVisibleElementList().size() > 0) { 
					
					selectedElement = canvas.getSelectionModel().getVisibleElementList().get(0);
					
					if (selectedElement instanceof VisibleOperation) {
						System.out.println(selectedElement + " is selected.");
						initiallySelected = selectedElement;
					} else {
						System.out.println("First element of the choice list is selected.");
						initiallySelected = choiceList.get(0);
					}
				} 
			}
		} catch (Exception e) {
			return null;
		}
		return selectedProject;			
    }
	
	private Object selectVisibleElement() {
		Object[] choiceArray = choiceList.toArray();
		return JOptionPane.showInputDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), 
		        "Choose a business process modeling subject: ",
		        TITLE,
		        JOptionPane.QUESTION_MESSAGE, 
		        null, 
		        choiceArray, 
		        initiallySelected);
		
	}
	
	public static final String TITLE = "Business Process Modeling Tool";
}
