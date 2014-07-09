package util.xml_readers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.panelcomposer.core.AppCache;

public class TypeMapping {
	
	 protected static final String TYPE = "type";
	 protected static final String MODEL_TYPE = "model-type";
	 protected static final String LANGUAGE_TYPE = "language-type";
	 protected static final String MAPPING_TYPES_FILE = "props/mapping-types.xml";
	 
	 public static void loadMappings() {
			try {
				Document doc = XMLUtil.getDocumentFromXML(MAPPING_TYPES_FILE, null);
				NodeList nodeLst = doc.getElementsByTagName(TYPE);
				for (int i = 0; i < nodeLst.getLength(); i++) {
					Element elem = (Element) nodeLst.item(i);
					String modelType = elem.getAttribute(MODEL_TYPE);
					String languageType = elem.getAttribute(LANGUAGE_TYPE);
					AppCache.getInstance().addToTypeMaping(modelType, languageType);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
