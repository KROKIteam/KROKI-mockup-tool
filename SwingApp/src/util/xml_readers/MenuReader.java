package util.xml_readers;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.text.html.HTML.Tag;

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
	protected static String modelDir = ReadersPathConst.MODEL_DIR_PATH;
	protected static String usersDirName = modelDir + ReadersPathConst.USERS_FILE_NAME + File.separator;

	/**
	 * Loads the menu configuration from XML
	 */
	public static void load() {
		try {
			File f = new File(".");
			String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
			System.out.println("MENU READER FILE: " + appPath + modelDir + File.separator + menuFile);
			Document doc = XMLUtil.getDocumentFromXML(appPath + modelDir + File.separator + menuFile, null);
			Element elementMenuMap = (Element) doc.getElementsByTagName(Tags.MENU_MAP).item(0);

			NodeList list = elementMenuMap.getChildNodes();
			for(int i=0;i<list.getLength(); i++) {
				Node n = list.item(i);
				if(n.getNodeType() == Node.ELEMENT_NODE) {
					AppCache.getInstance().addToCache(createMenu(n));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static MyMenu createMenu(Node node) {
		Element elementMenu = (Element) node;

		MyMenu mmenu = new MyMenu();
		mmenu.setLabel(elementMenu.getAttribute(Tags.LABEL));
		
		NodeList childern = node.getChildNodes();
		for(int i=0; i<childern.getLength(); i++) {
			Node n = childern.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				//System.out.println("CHILD " + i + " OF " + elementMenu.getAttribute(Tags.LABEL) + " IS " + n.getNodeName());
				if(n.getNodeName().equals("menu")) {
					MyMenu menu = createMenu(n);
					if(menu != null) {
						mmenu.addMenu(menu);
					}
				}else if (n.getNodeName().equals("submenu")) {
					try {
						MySubMenu submenu = createSubMenu(n);
						if(submenu != null) {
							mmenu.addSubmenu(submenu);
						}
					} catch (PanelTypeParsingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return mmenu;
	}

	private static MySubMenu createSubMenu(Node node)throws PanelTypeParsingException {
		Element elementSubmenu = (Element) node;
		MySubMenu submenu = new MySubMenu();
		submenu.setActivate(elementSubmenu.getAttribute(Tags.ACTIVATE));
		submenu.setLabel(elementSubmenu.getAttribute(Tags.LABEL));
		String pType = elementSubmenu.getAttribute(Tags.PANEL_TYPE);
		submenu.setPanelType(PanelTypeResolver.getType(pType));
		return submenu;
	}

}
