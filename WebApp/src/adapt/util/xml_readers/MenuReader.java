package adapt.util.xml_readers;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import adapt.core.AppCache;
import adapt.exceptions.PanelTypeParsingException;
import adapt.model.menu.AdaptMenu;
import adapt.model.menu.AdaptSubMenu;
import adapt.util.repository_utils.RepositoryPathsUtil;
import adapt.util.resolvers.PanelTypeResolver;
import adapt.util.staticnames.Tags;
import adapt.util.xml_utils.XMLParserUtils;

/**
 * Util class that reads menu structure specification from application repository
 * @author Milorad Filipovic
 */
public class MenuReader {

	protected static String generatedRepoPath 	= RepositoryPathsUtil.getGeneratedModelPath();
	protected static String generatedModelPath 	= RepositoryPathsUtil.getGeneratedModelPath();
	protected static String xmlFileName		 	= "menu.xml";
	
	private static String logPrefix = "MENU READER: ";

	/**
	 * Loads menu.xml file form application repository and creates object model representation of menus
	 */
	public static void load() {
		try {
			AppCache.displayTextOnMainFrame(logPrefix + "Reading menu structure from XML specification...", 0);
			Document document = XMLParserUtils.parseXml(generatedModelPath + File.separator + xmlFileName); 
			Element elementMenuMap = (Element)document.getElementsByTagName(Tags.MENU_MAP).item(0);
			
			NodeList list = elementMenuMap.getChildNodes();
			for(int i=0; i<list.getLength(); i++) {
				Node node = list.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					AppCache.getInstance().addToCache(createMenu(node));
				}
			}
			AppCache.displayTextOnMainFrame(logPrefix + "Menu structure obtained successfully!", 0);
		} catch (Exception e) {
			AppCache.displayTextOnMainFrame("Error reading menu.xml", 1);
			AppCache.displayStackTraceOnMainFrame(e);
		}
	}
	
	private static AdaptMenu createMenu(Node node) {
		Element menuElement = (Element)node;
		AdaptMenu menu = new AdaptMenu();
		menu.setLabel(menuElement.getAttribute(Tags.LABEL));
		
		NodeList menuChildern = node.getChildNodes();
		for(int i=0; i<menuChildern.getLength(); i++) {
			Node n = menuChildern.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				if(n.getNodeName().equals(Tags.MENU)) {
					AdaptMenu menuu = createMenu(n);
					if(menuu != null) {
						menu.addMenu(menuu);
					}
				}else if(n.getNodeName().equals(Tags.SUBMENU)) {
					try {
						AdaptSubMenu submenu = createSubmenu(n);
						if(submenu != null) {
							menu.addSubMenu(submenu);
						}
					}catch(PanelTypeParsingException e) {
						AppCache.displayStackTraceOnMainFrame(e);
					}
				}
			}
		}
		return menu;
	}
	
	/**
	 * Creates {@code AdaptSubMenu} object from XML element
	 * @param node
	 * @return
	 * @throws PanelTypeParsingException
	 */
	private static AdaptSubMenu createSubmenu(Node node) throws PanelTypeParsingException {
		Element submenuElement = (Element)node;
		AdaptSubMenu submenu = new AdaptSubMenu();
		submenu.setActivate(submenuElement.getAttribute(Tags.ACTIVATE));
		submenu.setLabel(submenuElement.getAttribute(Tags.LABEL));
		String panelType = submenuElement.getAttribute(Tags.PANEL_TYPE);
		submenu.setPanelType(PanelTypeResolver.getType(panelType));
		return submenu;
	}
}
