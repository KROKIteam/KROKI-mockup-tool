package kroki.app.utils;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TypeComponentMapper {
	
	HashMap<String, String> mapping;
	
	public void getMappings() {
		mapping = new HashMap<String, String>();
		try {
			Document typeDoc = XMLParser.parseXml("D:/workspace/kroki-integracija-clone/SwingApp/model/type-component-mappings.xml");
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
