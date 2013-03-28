package kroki.app.generators;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Dictionary;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import kroki.app.generators.utils.Menu;
import kroki.app.generators.utils.Submenu;
import kroki.app.generators.utils.XMLWriter;

import org.apache.tools.ant.SubBuildListener;
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
	
	public void generateWEBMenu(ArrayList<Menu> menus) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			//<div> kontejner za ostale elemente
			Element divRoot = doc.createElement("div");
			//element <div> - zaglavlje navigacije
			Element navigationDiv = doc.createElement("div");
			//element <ul> koji predstavlja koren meni liste
			Element menuRoot = doc.createElement("ul");
			//<script> tag za inicijalizaciju javascripta
			Element scriptRoot = doc.createElement("script");
			
			Attr rootIdAttr = doc.createAttribute("id");
			rootIdAttr.setValue("menuDiv");
			divRoot.setAttributeNode(rootIdAttr);
			
			doc.appendChild(divRoot);
			divRoot.appendChild(navigationDiv);
			divRoot.appendChild(menuRoot);
			divRoot.appendChild(scriptRoot);
			
			//--------------Sadrzaj <div> taga----------------
			//id atribut
			Attr divIdAttr = doc.createAttribute("id");
			divIdAttr.setValue("menuTitle");
			navigationDiv.setAttributeNode(divIdAttr);
			navigationDiv.setTextContent("Navigation");
			
			//-------------Sadrzaj <ul> taga-----------------
			//id atribut
			Attr ulIdAttr = doc.createAttribute("id");
			ulIdAttr.setValue("treemenu1");
			menuRoot.setAttributeNode(ulIdAttr);
			//class atribut
			Attr ulClassAttr = doc.createAttribute("class");
			ulClassAttr.setValue("treeview");
			menuRoot.setAttributeNode(ulClassAttr);
			
			for (Menu menu : menus) {
				printWEBMenu(menu, doc, menuRoot);
			}
			
			//-------------Sadrzaj <script> taga-----------------
			//type attribut
			Attr scriptTypeAttr = doc.createAttribute("type");
			scriptTypeAttr.setValue("text/javascript");
			scriptRoot.setAttributeNode(scriptTypeAttr);
			//tekstualni sadrzaj div-a
			scriptRoot.setTextContent("ddtreemenu.createTree(\"treemenu1\", true)");
			
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
			printSWINGMenu(m, doc, menuEl);
		}
	}

	public void printWEBMenu(Menu menu, Document doc, Element root) {
		//za svaki meni ide <li> tag sa labelom
		Element menuLiElement = doc.createElement("li");
		menuLiElement.setTextContent(menu.getLabel());
		root.appendChild(menuLiElement);
		//unutar njega ide <ul> tag koji sadrzi listu podmenija
		Element menuUlElemnet = doc.createElement("ul");
		menuLiElement.appendChild(menuUlElemnet);
		//za svaki podmeni unutar se generise <li> tag sa linkom unutar <ul> elementa
		for (Submenu submenu : menu.getSubmenus()) {
			Element submenuLiElement = doc.createElement("li");
			Element submenuAElement = doc.createElement("a");
			Attr hrefAttr = doc.createAttribute("href");
			hrefAttr.setValue(submenu.getActivate());
			submenuAElement.setAttributeNode(hrefAttr);
			submenuAElement.setTextContent(submenu.getLabel());
			submenuLiElement.appendChild(submenuAElement);
			menuUlElemnet.appendChild(submenuLiElement);
		}
		for (Menu men : menu.getMenus()) {
			printWEBMenu(men, doc, menuLiElement);
		}
	}
	
	public void writeDocument(Document doc) {
		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
		File fout = new File(appPath.substring(0, appPath.length()-16) +  "WebApp" + File.separator + "src" + File.separator + "adapt" + File.separator +  "templates" + File.separator + "treemenuGenerated.html");
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
}
