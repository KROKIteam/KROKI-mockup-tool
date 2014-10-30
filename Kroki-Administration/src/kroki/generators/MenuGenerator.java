package kroki.generators;

import gui.menudesigner.model.Menu;
import gui.menudesigner.model.MenuItem;
import gui.menudesigner.model.Submenu;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MenuGenerator {
	
	private Submenu root;
	private List roles;
	private List<Submenu> menus;
	public MenuGenerator(List<Submenu> menus) {
		this.menus = menus;
	}
	
	public void generate() {
		XMLWriter writer = new XMLWriter();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		
		try {
			docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			// korenski tag <classes>
			Element resourcesRoot = doc.createElement("menus");
			doc.appendChild(resourcesRoot);
			
			generateMenu(resourcesRoot, doc);
			
			writer.write(doc, "menu-generated", false);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void generateMenu(Element resourcesRoot, Document doc) {
		for (Submenu menu : menus) {
			//<menu>
			Element menuTag = doc.createElement("menu");
			resourcesRoot.appendChild(menuTag);
			
			Element rolesTag = doc.createElement("roles");
			menuTag.appendChild(rolesTag);
			
			for (Object o : menu.getRoles()) {
				Element roleTag = doc.createElement("role");
				roleTag.setTextContent(o.toString());
				rolesTag.appendChild(roleTag);
			}
			
			if(!menu.getChildren().isEmpty())
				extractChildren(menuTag, menu.getChildren(), doc);
		}

	}
	
	private void extractChildren(Element parentTag, ArrayList<Menu> children, Document doc) {
		
		for (Menu m : children) {
			if (m instanceof MenuItem) {
				MenuItem temp = (MenuItem)m;
				String tempMenuItemName = null;
				if (temp.getParent().equals(root))
					tempMenuItemName = "menu_item_main";
				else
					tempMenuItemName = "menu_item";
				Element tempMenuItemTag = doc.createElement(tempMenuItemName);
				parentTag.appendChild(tempMenuItemTag);
				
				Element menuNameTag = doc.createElement("menu_name");
				menuNameTag.setTextContent(temp.getMenuName());
				tempMenuItemTag.appendChild(menuNameTag);
				
				Element formNameTag = doc.createElement("form_name");
				formNameTag.setTextContent(temp.getFormName() != null ? temp.getFormName() : "");
				tempMenuItemTag.appendChild(formNameTag);
				
				Element activateTag = doc.createElement("activate");
				activateTag.setTextContent(temp.getActivate() != null ? temp.getActivate() : "");
				tempMenuItemTag.appendChild(activateTag);
				
				Element panelTypeTag = doc.createElement("panel_type");
				panelTypeTag.setTextContent(temp.getPanelType() != null ? temp.getPanelType() : "");
				tempMenuItemTag.appendChild(panelTypeTag);
			} else if (m instanceof Submenu) {
				Submenu temp = (Submenu)m;
				Element tempSubmenuTag = doc.createElement("submenu");
				parentTag.appendChild(tempSubmenuTag);
				
				Submenu parentSubmenu = (Submenu)temp.getParent();
				Element submenuParentTag = doc.createElement("submenu_parent");
				submenuParentTag.setTextContent(parentSubmenu != null ? parentSubmenu.getName() : "");
				tempSubmenuTag.appendChild(submenuParentTag);
				
				Element tempSubmenuNameTag = doc.createElement("submenu_name");
				tempSubmenuNameTag.setTextContent(temp.getName());
				tempSubmenuTag.appendChild(tempSubmenuNameTag);
				if (!temp.getChildren().isEmpty()) {
					extractChildren(tempSubmenuTag, temp.getChildren(), doc);
				}
			}
		}
	}

	private void generateRolePermissions(Element resourcesRoot, Document doc) {
		//<role_permissions> tag
		Element rolePermissionsTag = doc.createElement("role_permissions");
		resourcesRoot.appendChild(rolePermissionsTag);
			
		dao.administration.RolePermissionHibernateDao pDao = new dao.administration.RolePermissionHibernateDao();
		List<ejb.administration.RolePermission> permissions = pDao.findAll();
		
		for (ejb.administration.RolePermission rp : permissions) {
			Element rolePermissionTag = doc.createElement("role_permission");
			rolePermissionsTag.appendChild(rolePermissionTag);
			
			Element idTag = doc.createElement("id");
			idTag.setTextContent(rp.getId().toString());
			rolePermissionTag.appendChild(idTag);
			
			Element roleTag = doc.createElement("role_name");
			roleTag.setTextContent(rp.getRole().getName().toString());
			rolePermissionTag.appendChild(roleTag);
			
			Element permissionTag = doc.createElement("permission_name");
			permissionTag.setTextContent(rp.getPermission().getName());
			rolePermissionTag.appendChild(permissionTag);
			
		}
		
	}

}
