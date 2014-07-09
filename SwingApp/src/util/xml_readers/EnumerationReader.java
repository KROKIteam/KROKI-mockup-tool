package util.xml_readers;

import java.io.File;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.staticnames.ReadersPathConst;
import util.staticnames.Tags;

import com.panelcomposer.core.AppCache;
import com.panelcomposer.model.enumeration.Enumeration;

public class EnumerationReader {

	protected static String fileName = ReadersPathConst.ENUM_FILE_NAME;
	protected static String modelDir = ReadersPathConst.MODEL_DIR_PATH;

	public static void loadMappings() {
		try {
			File f = new File(".");
			String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
			System.out.println("ENUM READER 1: " + appPath + modelDir  + File.separator + fileName);
			Document doc = XMLUtil.getDocumentFromXML(appPath + modelDir  + File.separator + fileName, null);
			NodeList nodeLst = doc.getElementsByTagName(Tags.ENUM);
			System.out.println("ENUM READER 2: " + fileName + " DOC: " + doc.getDocumentURI());
			for (int i = 0; i < nodeLst.getLength(); i++) {
				Node nodeEnum = nodeLst.item(i);
				Element elemEnum = (Element) nodeEnum; 
				String name = elemEnum.getAttribute(Tags.NAME);
				String label = elemEnum.getAttribute(Tags.LABEL);
				Enumeration enumeration = new Enumeration(name, label);
				NodeList nodeListValues = elemEnum.getElementsByTagName(Tags.VALUE);
				for (int j = 0; j < nodeListValues.getLength(); j++) {
					String value = nodeListValues.item(j).getTextContent();
					if(value != null) {
						enumeration.add(value);
					}
				}
				AppCache.getInstance().addToCacheTypes("javax.swing.JComboBox", "com.panelcomposer.enumerations." + enumeration.getName());
				AppCache.getInstance().addToCache(name, enumeration);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
