package util.xml_readers;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.resolvers.PanelTypeResolver;
import util.staticnames.ReadersPathConst;
import util.staticnames.Tags;

import com.panelcomposer.core.AppCache;
import com.panelcomposer.exceptions.PanelTypeParsingException;
import com.panelcomposer.model.menu.MyMenu;
import com.panelcomposer.model.menu.MySubMenu;

public class MenuReader {

	protected static String menuFile = ReadersPathConst.MENU_FILE_NAME;
	protected static String usersDirName = ReadersPathConst.USERS_FILE_NAME;

	/**
	 * Loads the menu configuration from XML
	 */
	public static void load() {
		try {
			File f = new File(".");
			String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
			System.out.println("MENU READER 1: " + appPath + menuFile);
			Document doc = XMLUtil.getDocumentFromXML(appPath + menuFile, 
					ReadersPathConst.XSD_MENU);
			Element elementMenuMap = (Element) doc.getElementsByTagName(
					Tags.MENU_MAP).item(0);
			NodeList nodeListMenus = elementMenuMap
					.getElementsByTagName(Tags.MENU);
			System.out.println("MENUS: " + doc.getDocumentURI());
			for (int i = 0; i < nodeListMenus.getLength(); i++) {
				AppCache.getInstance().addToCache(
						createMenu(nodeListMenus.item(i)));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static MyMenu createMenu(Node node) {
		Element elementMenu = (Element) node;
		MyMenu mmenu = new MyMenu();
		mmenu.setLabel(elementMenu.getAttribute(Tags.LABEL));
		NodeList nodeListSubmenus = elementMenu.getElementsByTagName(Tags.SUBMENU);
		for (int i = 0; i < nodeListSubmenus.getLength(); i++) {
			try {
				MySubMenu msm = createSubMenu(nodeListSubmenus.item(i));
				if(msm != null) {
					mmenu.add(msm);
				}
			} catch (PanelTypeParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mmenu;
	}

	private static MySubMenu createSubMenu(Node node)
			throws PanelTypeParsingException {
		
		Element elementSubmenu = (Element) node;
		MySubMenu submenu = new MySubMenu();
		submenu.setActivate(elementSubmenu.getAttribute(Tags.ACTIVATE));
		submenu.setLabel(elementSubmenu.getAttribute(Tags.LABEL));
		String pType = elementSubmenu.getAttribute(Tags.PANEL_TYPE);
		submenu.setPanelType(PanelTypeResolver.getType(pType));
		return submenu;
	}

}
