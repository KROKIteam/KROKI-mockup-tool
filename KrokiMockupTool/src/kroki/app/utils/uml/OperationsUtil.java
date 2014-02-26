package kroki.app.utils.uml;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import kroki.app.KrokiMockupToolApp;
import kroki.profil.VisibleElement;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.visitor.ContainingPanels;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;

public class OperationsUtil {

	
	public static void delete(VisibleElement visibleElement){
		
		if (visibleElement instanceof UmlPackage) {
			UmlPackage subsystem = (UmlPackage) visibleElement;

			ContainingPanels visitor = new ContainingPanels();
			visitor.visit(subsystem);
			List<Object> visibleClassList = visitor.getObjectList();
			updateAssociationEnds(visibleClassList);
			for (int i = 0; i < visibleClassList.size(); i++) {
				VisibleClass visibleClass = (VisibleClass) visibleClassList.get(i);
				deletePanel(visibleClass);
			}

			if (subsystem.nestingPackage() != null) {
				subsystem.nestingPackage().removeNestedPackage(subsystem);
			} else {
				//izbrisi ga iz workspace-a
				KrokiMockupToolApp.getInstance().getWorkspace().removePackage(subsystem);
			}

		} else if (visibleElement instanceof UmlType) {
			UmlType umlType = (UmlType) visibleElement;

			deletePanel((VisibleClass) umlType);
			updateAssociationEnds((VisibleClass) umlType);
			umlType.umlPackage().removeOwnedType(umlType);
		}
		KrokiMockupToolApp.getInstance().getProjectHierarchyController().getTree().updateUI();
	}

	public static UmlPackage findCurrentProject(){
		TreePath path =KrokiMockupToolApp.getInstance().getProjectHierarchyController().getTree().getSelectionPath();
		Object node = path.getLastPathComponent();
		UmlPackage proj = null;
		if(node instanceof BussinesSubsystem) {
			BussinesSubsystem subsys = (BussinesSubsystem) node;
			proj = KrokiMockupToolApp.getInstance().findProject(subsys);
		}else if(node instanceof VisibleClass) {
			JTree tree = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree();
			Object parent = tree.getSelectionPath().getParentPath().getLastPathComponent();
			if(parent instanceof BussinesSubsystem) 
				proj = KrokiMockupToolApp.getInstance().findProject((BussinesSubsystem)parent);
		}
		return proj;
	}


	private static void deletePanel(VisibleClass panel){
		int index = KrokiMockupToolApp.getInstance().getTabbedPaneController().getTabIndex((VisibleClass) panel);
		if (index != -1) 
			KrokiMockupToolApp.getInstance().getTabbedPaneController().closeTab(index);
	}


	private static void updateAssociationEnds(VisibleClass panel){

		for (VisibleAssociationEnd end: allAssociationEnds()){
			if (panel == end.getTargetPanel()){
				end.setTargetPanel(null);
				end.setOpposite(null);
			}
		}
	}

	private static void updateAssociationEnds(List<Object> panels){

		for (VisibleAssociationEnd end: allAssociationEnds()){
			if (panels.contains(end.getTargetPanel())){
				end.setTargetPanel(null);
				end.setOpposite(null);
			}
		}
	}

	private static List<VisibleAssociationEnd> allAssociationEnds(){
		BussinesSubsystem project = (BussinesSubsystem) findCurrentProject();
		List<VisibleAssociationEnd> ret = new ArrayList<VisibleAssociationEnd>();
		project.allAssociationEnds(ret);
		return ret;
	}
	
}
