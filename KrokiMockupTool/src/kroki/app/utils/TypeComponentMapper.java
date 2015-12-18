package kroki.app.utils;

import java.io.File;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TypeComponentMapper {
	
	private HashMap<String, String> mapping;
	
	public void getMappings() {
		mapping = new HashMap<String, String>();
		try {
			File f = new File(".");
			String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
			Document typeDoc = XMLParser.parseXml(appPath.substring(0, appPath.length()-16) + "SwingApp" + File.separator + "model" + File.separator + "type-component-mappings.xml");
			NodeList typeNodes = typeDoc.getElementsByTagName("property");
			for(int i=0; i<typeNodes.getLength(); i++) {
				Element el = (Element) typeNodes.item(i);
				mapping.put(el.getAttribute("component-type"), el.getAttribute("language-type"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String getDataType(String componentName) {
		if(mapping.get(componentName) != null) {
			return mapping.get(componentName);
		}else {
			return "String";
		}
	}
	
}
