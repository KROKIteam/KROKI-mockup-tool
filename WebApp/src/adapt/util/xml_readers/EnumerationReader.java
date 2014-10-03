package adapt.util.xml_readers;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import adapt.core.AppCache;
import adapt.model.enumeration.Enumeration;
import adapt.util.repository_utils.RepositoryPathsUtil;
import adapt.util.staticnames.Tags;
import adapt.util.xml_utils.XMLParserUtils;

public class EnumerationReader {

	protected static String generatedModelPath 	= RepositoryPathsUtil.getGeneratedModelPath();
	protected static String staticModelPath		= RepositoryPathsUtil.getStaticModelPath();
	protected static String staticFileName	 	= "enumerations.xml";
	protected static String generatedFileName	= "enumerations-generated.xml";

	private static String logPrefix = "ENUMERATION READER: ";

	public static void loadEnumerations() {
		AppCache.displayTextOnMainFrame(logPrefix + "Reading mapping file: " + staticFileName, 0);
		load(staticModelPath + File.separator + staticFileName);
		AppCache.displayTextOnMainFrame(logPrefix + "Reading mapping file: " + generatedFileName, 0);
		load(generatedModelPath + File.separator + generatedFileName);
	}

	private static void load(String filePath) {
		try {
			Document document = XMLParserUtils.parseXml(filePath);
			NodeList enumNodes = document.getElementsByTagName(Tags.ENUM);
			for(int i=0; i<enumNodes.getLength(); i++) {
				Node enumNode = enumNodes.item(i);
				Element enumElement = (Element)enumNode;
				String name = enumElement.getAttribute(Tags.NAME);
				String label = enumElement.getAttribute(Tags.LABEL);
				Enumeration enumeration = new Enumeration(name, label);
				NodeList valueNodes = enumElement.getElementsByTagName(Tags.VALUE);
				for(int j=0; j<valueNodes.getLength(); j++) {
					String value = valueNodes.item(j).getTextContent();
					if(value != null) {
						enumeration.add(value);
					}
				}
				AppCache.getInstance().addToCache(name, enumeration);
				AppCache.displayTextOnMainFrame(logPrefix + "Enumeration '" + name + "' parsed successfully.", 0);
			}
		} catch (Exception e) {
			AppCache.displayTextOnMainFrame(logPrefix + "Enumeration file not found. ", 2);
		}
	}
}
