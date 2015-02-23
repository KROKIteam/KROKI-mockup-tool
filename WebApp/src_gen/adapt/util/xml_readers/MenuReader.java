package adapt.util.xml_readers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import adapt.core.AppCache;
import adapt.enumerations.PanelType;
import adapt.exceptions.GeneratedMenuMissingException;
import adapt.exceptions.PanelTypeParsingException;
import adapt.model.menu.AdaptMenuItem;
import adapt.model.menu.AdaptSubMenu;
import adapt.util.repository_utils.RepositoryPathsUtil;
import adapt.util.resolvers.PanelTypeResolver;
import adapt.util.xml_utils.XMLParserUtils;

/**
 * Util class that reads menu structure specification from application
 * repository
 * 
 * @author Sebastijan Kaplar
 */
public class MenuReader {

	protected static String generatedRepoPath = RepositoryPathsUtil
			.getGeneratedModelPath();
	protected static String generatedModelPath = RepositoryPathsUtil
			.getGeneratedModelPath();
	
	protected static String xmlFileName = "menu-generated.xml";
	protected static String xmlFileNameDefault = "menu-generated-default.xml";
	protected static String logPrefix = "MENU READER: ";
	protected static ArrayList<AdaptSubMenu> rootMenus = new ArrayList<AdaptSubMenu>();
	protected static ArrayList<HashMap<String, AdaptSubMenu>> menuMaps = new ArrayList<HashMap<String, AdaptSubMenu>>();

	public static final int DEFAULT_MENU = 1;
	public static final int GENERATED_MENUS = 2;
	public static final String SUBMENU = "submenu";
	public static final String MENU_ITEM_MAIN = "menu_item_main";
	public static final String ROLES = "roles";
	/**
	 * Loads menu.xml file form application repository and creates object model
	 * representation of menus
	 */
	public static void load() {
		try {
			AppCache.displayTextOnMainFrame(logPrefix
					+ "Reading menu structure from XML specification...", 0);
			createMenus(xmlFileNameDefault, DEFAULT_MENU);
			rootMenus.clear();
			menuMaps.clear();
			createMenus(xmlFileName, GENERATED_MENUS);
			AppCache.displayTextOnMainFrame(logPrefix
					+ "Menu structure obtained successfully!", 0);
		} catch (GeneratedMenuMissingException e) {
			AppCache.displayTextOnMainFrame("Error reading " + e.getMessage(), 2);
		} catch (Exception e) {
			AppCache.displayTextOnMainFrame("Error reading " + xmlFileName, 1);
			AppCache.displayStackTraceOnMainFrame(e);
		}

	}


	public static void createMenu(String file) {
	}
	
	public static void createMenus(String file, int menu_type) throws GeneratedMenuMissingException {
		Document resDoc = XMLParserUtils.parseXml(generatedModelPath
				+ File.separator + file);
		
		if (resDoc == null && file.equals(xmlFileName)) {
			throw new GeneratedMenuMissingException(file + ", Generated menu missing, proceeding with default");
		}
		
		// Ekstrakcija menija
		NodeList resNodes = resDoc.getElementsByTagName("menu");

		for (int i = 0; i < resNodes.getLength(); i++) {
			AdaptSubMenu rootMenu = new AdaptSubMenu();
			rootMenu.setName("Menu");
			rootMenus.add(rootMenu);

			HashMap<String, AdaptSubMenu> menuMap = new HashMap<String, AdaptSubMenu>();
			menuMap.put("Menu", rootMenu);
			menuMaps.add(menuMap);

			Element menuElement = (Element) resNodes.item(i);
			NodeList menuChildren = menuElement.getChildNodes();

			for (int j = 0; j < menuChildren.getLength(); j++) {
				Node n = (Node) menuChildren.item(j);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					if (n.getNodeName().equals(SUBMENU)) {
						createSubmenu((Element) n, i);
					} else if (n.getNodeName().equals(MENU_ITEM_MAIN)) {
						createMenuItemMain((Element) n, i);
					} else if (n.getNodeName().equals(ROLES)) {
						createRoles((Element) n, i);
					}
				}
			}

		}
		
		switch (menu_type) {
			case DEFAULT_MENU:
				AdaptSubMenu temp = rootMenus.get(0);
				AppCache.getInstance().addToCacheDefaultMenu(temp);
				break;
			case GENERATED_MENUS:
				AppCache.getInstance().addToCache(rootMenus);
				break;
		}
	}

	/**
	 * Roles connected to the menu structure.
	 * @param n
	 * @param i
	 */
	private static void createRoles(Element n, int i) {
		NodeList nroles = n.getElementsByTagName("role");

		for (int j = 0; j < nroles.getLength(); j++) {
			Element element1 = (Element) nroles.item(j);
			String roleName = XMLParserUtils
					.getCharacterDataFromElement(element1);
			rootMenus.get(i).getRoles().add(roleName);
		}
	}

	
	/**
	 * Creates menu item on the main menu bar
	 * @param n
	 * @param i
	 */
	private static void createMenuItemMain(Element n, int i) {

		// Menuitemi u rootu menija
		NodeList nmenuname = n.getElementsByTagName("menu_name");
		NodeList nformname = n.getElementsByTagName("form_name");
		NodeList nactivate = n.getElementsByTagName("activate");
		NodeList npaneltype = n.getElementsByTagName("panel_type");
		
		String menuName = XMLParserUtils
				.getCharacterDataFromElement((Element) nmenuname.item(0));
		String formName = XMLParserUtils
				.getCharacterDataFromElement((Element) nformname.item(0));
		String activate = XMLParserUtils
				.getCharacterDataFromElement((Element) nactivate.item(0));
		String panelType = XMLParserUtils
				.getCharacterDataFromElement((Element) npaneltype.item(0));
		
		

		AdaptMenuItem tMenuItem;
		tMenuItem = new AdaptMenuItem();
		tMenuItem.setMenuName(menuName);
		tMenuItem.setFormName(formName);
		tMenuItem.setActivate(activate);
		PanelType ptr = null;
		try {
			ptr = PanelTypeResolver.getType(panelType);
		} catch (PanelTypeParsingException e) {
			e.printStackTrace();
		}
		tMenuItem.setPanelType(ptr.name().toString());
		tMenuItem.setParent(rootMenus.get(i));
		rootMenus.get(i).getChildren().add(tMenuItem);

	}

	/**
	 * Creates Submenu.
	 * @param n
	 * @param i
	 */
	private static void createSubmenu(Element n, int i) {
		// hijerarhija se odredjuje na osnovu polja parent
		NodeList nsubmenuparent = n.getElementsByTagName("submenu_parent");
		NodeList nsubmenuname = n.getElementsByTagName("submenu_name");
		NodeList ntmenuitem = n.getElementsByTagName("menu_item");

		String submenuParent = XMLParserUtils
				.getCharacterDataFromElement((Element) nsubmenuparent.item(0));
		String submenuName = XMLParserUtils
				.getCharacterDataFromElement((Element) nsubmenuname.item(0));

		AdaptSubMenu parentMenu = menuMaps.get(i).get(submenuParent);
		AdaptSubMenu tempSubmenu = new AdaptSubMenu(submenuName);
		if (parentMenu != null) {
			tempSubmenu.setParent(parentMenu);
			parentMenu.getChildren().add(tempSubmenu);
		}
		menuMaps.get(i).put(submenuName, tempSubmenu);

		for (int k = 0; k < ntmenuitem.getLength(); k++) {
			Element element2 = (Element) ntmenuitem.item(k);
			NodeList nmenuname = element2.getElementsByTagName("menu_name");
			NodeList nformname = element2.getElementsByTagName("form_name");
			NodeList nactivate = element2.getElementsByTagName("activate");
			NodeList npaneltype = element2.getElementsByTagName("panel_type");

			String menuName = XMLParserUtils
					.getCharacterDataFromElement((Element) nmenuname.item(0));
			String formName = XMLParserUtils
					.getCharacterDataFromElement((Element) nformname.item(0));
			String activate = XMLParserUtils
					.getCharacterDataFromElement((Element) nactivate.item(0));
			String panelType = XMLParserUtils
					.getCharacterDataFromElement((Element) npaneltype.item(0));

			AdaptMenuItem tMenuItem;
			tMenuItem = new AdaptMenuItem();
			tMenuItem.setMenuName(menuName);
			tMenuItem.setFormName(formName);
			tMenuItem.setActivate(activate);
			PanelType ptr = null;
			try {
				ptr = PanelTypeResolver.getType(panelType);
			} catch (PanelTypeParsingException e) {
				e.printStackTrace();
			}
			tMenuItem.setPanelType(ptr.name().toString());
			tMenuItem.setParent(tempSubmenu);
			tempSubmenu.getChildren().add(tMenuItem);
		}

	}

}
