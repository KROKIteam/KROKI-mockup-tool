package kroki.app.utils;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;

public class XMLParser {

	public static Document parseXml(String fileName) {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.parse(fileName);
		}catch(ParserConfigurationException pce) {
			//pce.printStackTrace();
			return null;
		}catch(SAXException se) {
			//se.printStackTrace();
			return null;
		}catch(IOException ioe) {
			//ioe.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns string value of the xml document
	 */
	public static String getCharacterDataFromElement(org.w3c.dom.Element line) {
		   Node child = ((Node) line).getFirstChild();
		   if (child instanceof CharacterData) {
		     CharacterData cd = (CharacterData) child;
		       return cd.getData();
		     }
		   return "?";
		 }
	
}
