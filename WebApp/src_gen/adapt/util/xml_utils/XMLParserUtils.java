package adapt.util.xml_utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLParserUtils {

	/**
	 * Creates W3C DOM Document object from XML file
	 * @param filePath path to xml file including file name and extension
	 */
	public static Document parseXml(String filePath) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(filePath);
			doc.getDocumentElement().normalize();
			return doc;
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
			return null;
		}catch(SAXException se) {
			se.printStackTrace();
			return null;
		}catch(IOException ioe) {
			//ioe.printStackTrace();
			String toRemove = File.separator + "WebApp" + File.separator;
			Document doc;
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				doc = db.parse(filePath.replace(toRemove, File.separator));
				doc.getDocumentElement().normalize();
				return doc;
			} catch (SAXException saxex) {
				saxex.printStackTrace();
				return null;
			} catch (ParserConfigurationException pcex) {
				pcex.printStackTrace();
				return null;
			} catch (IOException ioex) {
				ioex.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * Gets String representation of XML tag value
	 */
	public static String getCharacterDataFromElement(Element line) {
		Node child = ((Node) line).getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "?";
	}
}
