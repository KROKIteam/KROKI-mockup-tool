package kroki.generators;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class XMLWriter {
	
	public void write(Document doc, String fileName, Boolean swing) {
		
		try {
			File f = new File(".");
			String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
			
			File fout = new File(appPath.substring(0, appPath.length()-16) + "SwingApp" + File.separator + "model" + File.separator +  fileName + ".xml");
			if(!swing) {
				//F:\workspace\github\KROKI-mockup-tool\ApplicationRepository\generated\model
				fout = new File(appPath.substring(0, appPath.length()-16) +  "ApplicationRepository" + File.separator + "generated" + File.separator + 
																		      File.separator + "model" + File.separator + fileName + ".xml");
			}
			if (!fout.getParentFile().exists()) 
				if (!fout.getParentFile().mkdirs()) {
					throw new IOException("Greska pri kreiranju izlaznog direktorijuma ");
			}
			
			System.out.println("XML Writer writing " + fout.getAbsolutePath());
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(fout);
 
			transformer.transform(source, result);
			System.out.println("[XML WRITER] " + fileName + ".xml datoteka generisana");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
	}

}
