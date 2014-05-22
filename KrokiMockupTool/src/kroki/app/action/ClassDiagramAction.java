package kroki.app.action;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;
import graphedit.model.elements.GraphEditPackage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.ImageResource;
import kroki.app.view.Canvas;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;

public class ClassDiagramAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClassDiagramAction(){
		putValue(NAME, "Show class diagram");
		putValue(SHORT_DESCRIPTION, "Show class diagram");
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.classDiagramAction.icon"));
		putValue(SMALL_ICON, smallIcon);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		BussinesSubsystem proj = null;
		if (KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath()!=null){
			String selectedNoded = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().getSelectionPath().getLastPathComponent().toString();
			for(int j=0; j<KrokiMockupToolApp.getInstance().getWorkspace().getPackageCount(); j++) {
				BussinesSubsystem pack = (BussinesSubsystem)KrokiMockupToolApp.getInstance().getWorkspace().getPackageAt(j);
				if(pack.getLabel().equals(selectedNoded)) {
					proj = pack;
				}
			}
		}
		
		MainFrame.getInstance().setAppMode(ApplicationMode.USER_INTERFACE);

		List<UmlPackage> workspaceList = KrokiMockupToolApp.getInstance().getWorkspace().getPackageList();
		if (proj != null)
			graphedit.model.GraphEditWorkspace.getInstance().setProject(proj);
		else
			graphedit.model.GraphEditWorkspace.getInstance().setPackageList(workspaceList);

		MainFrame.getInstance().setVisible(true);

		//graphedit.model.GraphEditWorkspace.getInstance().getMainFrame().setVisible(true);
		List<GraphEditPackage> packageList = graphedit.model.GraphEditWorkspace.getInstance().getpackageList();
		UmlPackage umlPackage;
		BussinesSubsystem businessSub;
		for (GraphEditPackage pack: packageList){
			umlPackage = pack.getUmlPackage();
			if (!workspaceList.contains(umlPackage))
				workspaceList.add(umlPackage);
			updatePanels(umlPackage);
			if (umlPackage instanceof BussinesSubsystem){
				businessSub = (BussinesSubsystem)umlPackage;
				try {
					if ((businessSub.getDiagramFile() == null && pack.getFile() != null) || (businessSub.getDiagramFile() != null && pack.getFile() != null &&
							!businessSub.getDiagramFile().getCanonicalPath().equals(pack.getFile().getCanonicalPath())))
							businessSub.setDiagramFile(pack.getFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}

		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getCanvasTabbedPane().updateUI();
	}

	private void updatePanels(UmlPackage pack){
		StandardPanel panel;
		for (UmlType ownedType : pack.ownedType()){
			if (ownedType instanceof StandardPanel){
				panel = (StandardPanel) ownedType;
				ElementsGroup gr1 = (ElementsGroup) panel.getVisibleElementList().get(1);
				ElementsGroup gr2 = (ElementsGroup) panel.getVisibleElementList().get(2);
				for (VisibleElement el : gr1.getVisibleElementList())
					el.update();
				for (VisibleElement el : gr2.getVisibleElementList())
					el.update();
				gr1.update();
				gr2.update();
				/*NamingUtil cc = new NamingUtil();
				System.out.println(panel.getPersistentClass().name());
				panel.getPersistentClass().setName(cc.toCamelCase(panel.getLabel(), false));
				System.out.println(panel.getPersistentClass().name());
				panel.getComponent().setName(panel.getLabel());
				StyleToolbar st = (StyleToolbar) KrokiMockupToolApp.getInstance().getGuiManager().getStyleToolbar();
				st.updateAllToggles(((ElementsGroup) panel.getVisibleElementList().get(1)));
				st.updateAllToggles(((ElementsGroup) panel.getVisibleElementList().get(2)));*/
			}
			((VisibleClass)ownedType).update();
		}
		for (UmlPackage nestedPack : pack.nestedPackage())
			updatePanels(nestedPack);

	}

}
