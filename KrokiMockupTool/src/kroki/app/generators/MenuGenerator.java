package kroki.app.generators;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kroki.app.generators.utils.Menu;
import kroki.app.generators.utils.Submenu;
import kroki.app.generators.utils.XMLWriter;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MenuGenerator {
	
	public void generate(ArrayList<Menu> menus) {
		
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
				printMenu(menu, doc, root);
			}
			
			writer.write(doc, "menu", true);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	
	public void printMenu(Menu menu, Document doc, Element root) {
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
		for(int j=0; j<menu.getMenus().size(); j++) {
			Menu m = menu.getMenus().get(j);
			printMenu(m, doc, menuEl);
		}
	}

}
