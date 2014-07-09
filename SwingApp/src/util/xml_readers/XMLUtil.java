package util.xml_readers;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLUtil {

	public static String getTagValue(String tag, Element elem) {
		String retVal = "";
		NodeList fstNmElmntLst = elem.getElementsByTagName(tag);
		Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		if (fstNmElmnt == null)
			return "";
		NodeList fstNm = fstNmElmnt.getChildNodes();
		if (fstNm == null || fstNm.item(0) == null)
			return "";
		retVal = fstNm.item(0).getNodeValue();
		return retVal;
	}
	
	public static boolean validateXML(String schemaPath, String xmlPath) {
		try {
			String schemaLang = "http://www.w3.org/2001/XMLSchema";
			SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
			// create schema by reading it from an XSD file:
			Schema schema = factory.newSchema(new StreamSource(schemaPath));
			Validator validator = schema.newValidator();
			// at last perform validation:
			validator.validate(new StreamSource(xmlPath));
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	public static Document getDocumentFromXML(String mappingFile, String schemaFile) {
		try {
			if(schemaFile != null) {
				if(validateXML(schemaFile, mappingFile) == false) {
					return null;
				}
			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(mappingFile);
			doc.getDocumentElement().normalize();
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
