package kroki.app.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import kroki.api.panel.VisibleClassUtil;
import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.ImageResource;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.subsystem.BussinesSubsystem;

import org.apache.commons.collections.CollectionUtils;

import framework.MainFrame;
import gui.menudesigner.model.Submenu;

/**
 * 
 * @author Bane - Administration Subsystem
 *
 */
public class AdministrationSubsytemAction extends AbstractAction{

	private Map<String,String> panelType = new HashMap<String,String>();
	private NamingUtil cc;
	
	public AdministrationSubsytemAction() {
		cc = new NamingUtil();
		putValue(NAME, "Show administration subsystem");
		putValue(SHORT_DESCRIPTION, "Show administration subsystem");
		ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.administrationSubsystem.icon"));
		putValue(SMALL_ICON, smallIcon);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//Get selected project from the workspace
		BussinesSubsystem proj = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getCurrentProject();
		MainFrame mainFrame = MainFrame.getNewInstance(true);
		if (proj != null) {
			if (proj.getMenu() != null) {
				gui.menudesigner.MenuSketchDialog.menus = (List<Submenu>) proj.getMenu();
			} else {
				gui.menudesigner.MenuSketchDialog.menus = new ArrayList<Submenu>();
				proj.setMenu(gui.menudesigner.MenuSketchDialog.menus);
			}
			//Formiranje dela podsistema za administraciju podacima koji su unapred poznati
			dao.administration.ResourceHibernateDao rDao = new dao.administration.ResourceHibernateDao();
			List<String> sResources = new ArrayList<String>();
			checkForExistingAdministrationSubsystem(sResources,proj);
			loadFormDataType(proj);
			
			if (compareResources(sResources)) {
				if(rDao.findAll().isEmpty()) {
					findAndPersistAllFormsAsResources(proj);
					addInitialData();
				}
				
				mainFrame.setPanelType(panelType);
				mainFrame.setVisible(true);
			} else {
				KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				Object dugme[] = {"Create new","Add to existing"};
				int selektovano = JOptionPane.showOptionDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "Do you wish to recreate Resources or add new to existing (at own risk) resources?", 
						"Resources modified!", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, dugme, dugme[0]);
				
				if (selektovano == JOptionPane.YES_NO_OPTION) {
					rDao.deleteAll();
					findAndPersistAllFormsAsResources(proj);
					mainFrame.setPanelType(panelType);
					mainFrame.setVisible(true);
				} else if (selektovano == JOptionPane.NO_OPTION){
					findAndPersistAllFormsAsResources(proj);
					mainFrame.setPanelType(panelType);
					mainFrame.setVisible(true);
				}
			}
		}else {
			//if no project is selected, inform user to select one
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!", "Administration Subsytem", JOptionPane.WARNING_MESSAGE);
			KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		}
	}

	private void addInitialData() {
		dao.administration.UserHibernateDao uDao = new dao.administration.UserHibernateDao();
		ejb.administration.User admin = new ejb.administration.User();
		admin.setUsername("admin");
		admin.setPassword("12345");
		uDao.save(admin);
		
		dao.administration.RoleHibernateDao rDao = new dao.administration.RoleHibernateDao();
		ejb.administration.Role adminRole = new ejb.administration.Role();
		adminRole.setName("admins");
		rDao.save(adminRole);
		
		dao.administration.OperationHibernateDao oDao = new dao.administration.OperationHibernateDao();
		ejb.administration.Operation addOperation = new ejb.administration.Operation();
		addOperation.setName("add");
		
		ejb.administration.Operation removeOperation = new ejb.administration.Operation();
		removeOperation.setName("remove");
		
		ejb.administration.Operation modifyOperation = new ejb.administration.Operation();
		modifyOperation.setName("modify");
		
		oDao.save(addOperation);
		oDao.save(removeOperation);
		oDao.save(modifyOperation);
		
		
		dao.administration.UserRolesHibernateDao urDao = new dao.administration.UserRolesHibernateDao();
		ejb.administration.UserRoles adminUserRole = new ejb.administration.UserRoles();
		adminUserRole.setRole(adminRole);
		adminUserRole.setUser(admin);
		urDao.save(adminUserRole);	
	}

	private boolean compareResources(List<String> sResources) {
		dao.administration.ResourceHibernateDao rDao = new dao.administration.ResourceHibernateDao();
		List<ejb.administration.Resource> resources = rDao.findAll();
		List<String> resourcesList = new ArrayList<String>();
		if(resources.isEmpty())
			return true;
		
		if (sResources.size() != resources.size()) {
			return false;
		}
		
		for (ejb.administration.Resource r : resources) {
			resourcesList.add(r.getName());
		}
		
		Collection commonList = CollectionUtils.retainAll(sResources, resourcesList);
		if(commonList.size() == sResources.size() && commonList.size() == resourcesList.size())
			return true;
		
		return false;
	}

	/**
	 * Find all forms from the selected project.
	 * Each form is a resource in administration subsytem.
	 * @param pack
	 */
	private void findAndPersistAllFormsAsResources(BussinesSubsystem pack) {
		VisibleElement element;
		dao.administration.ResourceHibernateDao rDao = new dao.administration.ResourceHibernateDao();
		ejb.administration.Resource resource = null;
		String activate = null;
		for (int i = 0; i < pack.ownedElementCount(); i++) {
			element = pack.getOwnedElementAt(i);
			if (element instanceof VisibleClass) {
				resource = new ejb.administration.Resource();
				resource.setName(element.name());
				
				String panelTypeTemp = panelType.get(element.name());
				if (panelTypeTemp.contains("standard-panel")) {
					StandardPanel sp = (StandardPanel)element;
					activate = sp.getPersistentClass().name().toLowerCase() + "_st";
					//activate = element.name().toLowerCase() + "_st";
					resource.setPaneltype("standard-panel");
				} else if (panelTypeTemp.contains("parent-child")) {
					ParentChild pcPanel = (ParentChild)element;
					activate =cc.toCamelCase(pcPanel.name(), false) + "_pc"; 
					//activate = cc.toCamelCase(element.name(), false) + "_pc";
					resource.setPaneltype("parent-child");
				}
				resource.setLink(activate);
				rDao.save(resource);
			} else if (element instanceof BussinesSubsystem) {
				findAndPersistAllFormsAsResources((BussinesSubsystem) element);
			}
		}
	}
	
	/**
	 * Meotda vrsi mapiranje tipova formi, potrebnih 
	 * prilikom skiciranja menija
	 * @param pack
	 */
	private void loadFormDataType(BussinesSubsystem pack) {
		VisibleElement element;
		
		for (int i = 0; i < pack.ownedElementCount(); i++) {
			element = pack.getOwnedElementAt(i);
			if (element instanceof VisibleClass) {
				if(element instanceof StandardPanel) {
					panelType.put(element.name(), "standard-panel");
				}else if (element instanceof ParentChild) {
					ParentChild pcPanel = (ParentChild)element;
					String panel_type = "parent-child";
					
					//add list to contained panels enclosed in square brackets
					panel_type += "[";
					for(Hierarchy hierarchy: VisibleClassUtil.containedHierarchies(pcPanel)) {
						panel_type += cc.toCamelCase(hierarchy.getTargetPanel().getComponent().getName(), false) + ":";
					}
					panel_type = panel_type.substring(0, panel_type.length()-1) + "]";
					panelType.put(element.name(), panel_type);
				}
			} else if (element instanceof BussinesSubsystem) {
				loadFormDataType((BussinesSubsystem) element);
			}
		}
	}
	
	/**
	 * Kupljenje trenutnih formi koji se nalaze u krokiju za njihovo poredjenje sa onima u bazi
	 */
	private void checkForExistingAdministrationSubsystem(List<String> sResources, BussinesSubsystem pack) {		
		VisibleElement element;
		for (int i = 0; i < pack.ownedElementCount(); i++) {
			element = pack.getOwnedElementAt(i);
			if (element instanceof VisibleClass) {
				sResources.add(element.name());
			} else if (element instanceof BussinesSubsystem) {
				checkForExistingAdministrationSubsystem(sResources,(BussinesSubsystem) element);
			}
		}
	}
	
}
