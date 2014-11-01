package kroki.app.generators;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import kroki.app.generators.utils.Menu;
import kroki.app.generators.utils.Submenu;
import kroki.app.generators.utils.XMLWriter;
import kroki.app.menu.MenuItem;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MenuGenerator {
	
	//generates menu xml file for swing application based on menu list
	public void generateSWINGMenu(ArrayList<Menu> menus) {
		XMLWriter writer = new XMLWriter();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			//korenski element <menus>
			Element root = doc.createElement("menus");
			doc.appendChild(root);
			
			for(int i=0; i<menus.size(); i++) {
				Menu menu = menus.get(i);
				printSWINGMenu(menu, doc, root);
			}
			
			writer.write(doc, "menu", true);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void generateMenu(ArrayList<Menu> menus) {
		XMLWriter writer = new XMLWriter();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			//korenski element <menus>
			Element root = doc.createElement("menus");
			doc.appendChild(root);
			
			for(int i=0; i<menus.size(); i++) {
				Menu menu = menus.get(i);
				printSWINGMenu(menu, doc, root);
			}
			
			writer.write(doc, "menu", false);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	//generates menu.html file for web application
	public void generateWEBMenu(ArrayList<Menu> menus) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			// <ul> element that contains menu items
			Element ulRoot = doc.createElement("ul");
			Attr ulRootIdAttr = doc.createAttribute("id");
			ulRootIdAttr.setValue("mainMenu");
			ulRoot.setAttributeNode(ulRootIdAttr);
			doc.appendChild(ulRoot);

			for (Menu menu : menus) {
				printWEBMenu(menu, doc, ulRoot);
			}
			
			//writer.write(doc, "menu", true);
			writeDocument(doc);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	
	
	//prints menu tags in menu xml file for swing application
	public void printSWINGMenu(Menu menu, Document doc, Element root) {
		//za svaki meni ide <menu> tag
		Element menuEl = doc.createElement("menu");
		
		//atribut "name"
		Attr nameAttr = doc.createAttribute("name");
		nameAttr.setValue(menu.getName());
		menuEl.setAttributeNode(nameAttr);
		
		//atribut "label"
		Attr labelAttr = doc.createAttribute("label");
		labelAttr.setValue(menu.getLabel());
		menuEl.setAttributeNode(labelAttr);
		
		root.appendChild(menuEl);
		
		if(!menu.getSubmenus().isEmpty()) {
			for(int i=0; i<menu.getSubmenus().size(); i++) {
				Submenu sub = menu.getSubmenus().get(i);
			
				//za svaki podmeni ide <submenu> tag
				Element subEl = doc.createElement("submenu");
				
				//atribut "activate"
				Attr activateAttr = doc.createAttribute("activate");
				activateAttr.setValue(sub.getActivate());
				subEl.setAttributeNode(activateAttr);
				
				//atribut "label"
				Attr sLabelAttr = doc.createAttribute("label");
				sLabelAttr.setValue(sub.getLabel());
				subEl.setAttributeNode(sLabelAttr);
				
				//atribut "panel_type"
				Attr panelTypeAttr = doc.createAttribute("panel-type");
				panelTypeAttr.setValue(sub.getPanel_type());
				subEl.setAttributeNode(panelTypeAttr);
				
				menuEl.appendChild(subEl);
			}
			
		}
		if(!menu.getMenus().isEmpty()) {
			for(int j=0; j<menu.getMenus().size(); j++) {
				Menu m = menu.getMenus().get(j);
				printSWINGMenu(m, doc, menuEl);
			}
		}
	}

	public void printWEBMenu(Menu menu, Document doc, Element root) {
		//<li.mainMenuItems> for every first level menu
		Element mainMenuItemLi = doc.createElement("li");
		Attr mainMenuItemClass = doc.createAttribute("class");
		mainMenuItemClass.setValue("mainMenuItems");
		mainMenuItemLi.setAttributeNode(mainMenuItemClass);
		
		//each menu list item contains <div.menu>
		Element menuDiv = doc.createElement("div");
		Attr menuDivClass = doc.createAttribute("class");
		menuDivClass.setValue("menu");
		
		//each menu div item contains two elements:
		//	- <div.mainMenuText> with label text
		//	- <div.arrow-down> which represnts arrow pointing down
		Element mainMenuTextDiv =  doc.createElement("div");
		Attr mainMenuTextDivClass =  doc.createAttribute("class");
		mainMenuTextDivClass.setValue("mainMenuText");
		mainMenuTextDiv.setAttributeNode(mainMenuTextDivClass);
		mainMenuTextDiv.setTextContent(menu.getLabel());
		
		Element arrowDownDiv = doc.createElement("div");
		Attr arrowDownDivClass =  doc.createAttribute("class");
		arrowDownDivClass.setValue("arrow-down");
		arrowDownDiv.setAttributeNode(arrowDownDivClass);
		//add dummy text content to arrow div so the self-closing div doesn't get generated
		//this text is deleted in javascript on page load
		arrowDownDiv.setTextContent("/");
		
		menuDiv.setAttributeNode(menuDivClass);
		menuDiv.appendChild(mainMenuTextDiv);
		menuDiv.appendChild(arrowDownDiv);
		
		mainMenuItemLi.appendChild(menuDiv);
		
		//list of submenu items for 1st level submenus
		//each item is represented as <li.submenuItem>
		Element l1SubmenuUl = doc.createElement("ul");
		Attr l1SubmnenuUlClass = doc.createAttribute("class");
		l1SubmnenuUlClass.setValue("L1SubMenu");
		l1SubmenuUl.setAttributeNode(l1SubmnenuUlClass);
		for (Submenu submenu : menu.getSubmenus()) {
			Element l1SubmenuLi = doc.createElement("li");
			Attr l1SubmenuLiClass = doc.createAttribute("class");
			l1SubmenuLiClass.setValue("subMenuItem");
			l1SubmenuLi.setAttributeNode(l1SubmenuLiClass);
			l1SubmenuLi.setTextContent(submenu.getLabel());
			l1SubmenuLi.setAttribute("data-activate", submenu.getActivate());
			l1SubmenuLi.setAttribute("data-panelType", submenu.getPanel_type());
			l1SubmenuUl.appendChild(l1SubmenuLi);
		}
		
		//each submenu is represented as <li.submenuItem submenuLink>
		for (Menu men : menu.getMenus()) {
			//printWEBMenu(men, doc, mainMenuItemLi);
			Element l1SubmenuLink = doc.createElement("li");
			l1SubmenuLink.setAttribute("class", "subMenuItem subMenuLink");
			l1SubmenuLink.setTextContent(men.getLabel());
			Element arrowRightDiv = doc.createElement("div");
			arrowRightDiv.setAttribute("class", "arrow-right");
			arrowRightDiv.setTextContent("/");
			l1SubmenuLink.appendChild(arrowRightDiv);
			
			l1SubmenuUl.appendChild(l1SubmenuLink);
			//generate actual list of subenu items
			printWEBSubmenus(doc, l1SubmenuLink, men);
		}
		
		if(l1SubmenuUl.getChildNodes().getLength() > 0) {
			mainMenuItemLi.appendChild(l1SubmenuUl);
		}
		root.appendChild(mainMenuItemLi);
	}
	
	//generates <ul.L2SubMenu> for submenus below level 1 recursively
	public void printWEBSubmenus(Document doc, Element submenuLink, Menu menu) {
		Element submenuUl = doc.createElement("ul");
		submenuUl.setAttribute("class", "L2SubMenu");
		
		for (Submenu submenu : menu.getSubmenus()) {
			Element submenuItemLi = doc.createElement("li");
			submenuItemLi.setAttribute("class", "subMenuItem");
			submenuItemLi.setTextContent(submenu.getLabel());
			submenuItemLi.setAttribute("data-activate", submenu.getActivate());
			submenuItemLi.setAttribute("data-panelType", submenu.getPanel_type());
			submenuUl.appendChild(submenuItemLi);
		}
		
		for (Menu men : menu.getMenus()) {
			Element subMenuLinkLi = doc.createElement("li");
			subMenuLinkLi.setAttribute("class", "subMenuItem subMenuLink");
			subMenuLinkLi.setTextContent(men.getLabel());
			Element arrowRightDiv = doc.createElement("div");
			arrowRightDiv.setAttribute("class", "arrow-right");
			arrowRightDiv.setTextContent("/");
			subMenuLinkLi.appendChild(arrowRightDiv);
			
			printWEBSubmenus(doc, subMenuLinkLi, men);
			submenuUl.appendChild(subMenuLinkLi);
		}
		
		if(submenuUl.getChildNodes().getLength() > 0) {
			submenuLink.appendChild(submenuUl);
		}
	}
	
	public void writeDocument(Document doc) {
		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
		File fout = new File(appPath.substring(0, appPath.length()-16) +  "WebApp" + File.separator + "src" + File.separator + "adapt" + File.separator +  "templates" + File.separator + "menu.html");
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(fout);

			transformer.transform(source, result);
			System.out.println("[MENU GENERATOR] menu.html file generated");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * NEW DEFAULT MENU
	 */
	private kroki.app.menu.Submenu root;
	public void generateNewMenu(kroki.app.menu.Submenu rootMenu) {
		this.root = rootMenu;
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
			
			writer.write(doc, "menu-generated-default", false);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void generateMenu(Element resourcesRoot, Document doc) {
		//<menu>
		Element menuTag = doc.createElement("menu");
		resourcesRoot.appendChild(menuTag);
		
		Element rolesTag = doc.createElement("roles");
		menuTag.appendChild(rolesTag);
		
		/*
		for (Object o : roles) {
			Element roleTag = doc.createElement("role");
			roleTag.setTextContent(o.toString());
			rolesTag.appendChild(roleTag);
		}*/
		
		if(!root.getChildren().isEmpty())
			extractChildren(menuTag, root.getChildren(), doc);

	}
	
	private void extractChildren(Element parentTag, ArrayList<kroki.app.menu.Menu> children, Document doc) {
		
		for (kroki.app.menu.Menu m : children) {
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
			} else if (m instanceof kroki.app.menu.Submenu) {
				kroki.app.menu.Submenu temp = (kroki.app.menu.Submenu)m;
				Element tempSubmenuTag = doc.createElement("submenu");
				parentTag.appendChild(tempSubmenuTag);
				
				kroki.app.menu.Submenu parentSubmenu = (kroki.app.menu.Submenu)temp.getParent();
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
}
