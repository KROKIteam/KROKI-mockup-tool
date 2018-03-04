package kroki.app.generators.utils;

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

import kroki.app.KrokiMockupToolApp;

public class XMLWriter {
	
	public void write(Document doc, String fileName, Boolean swing) {
		
		try {
			File f = new File(".");
			String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
			String appRepoPath = appPath.substring(0, appPath.length()-16) +  "ApplicationRepository";
			if(KrokiMockupToolApp.getInstance().isBinaryRun()) {
				appRepoPath = appPath +  "ApplicationRepository";
			}
			
			File fout = new File(appPath.substring(0, appPath.length()-16) + "SwingApp" + File.separator + "model" + File.separator +  fileName + ".xml");
			if(KrokiMockupToolApp.getInstance().isBinaryRun()) {
				fout = new File(appPath + "SwingApp" + File.separator + "model" + File.separator +  fileName + ".xml");
			}
			if(!swing) {
				fout = new File(appRepoPath + File.separator + "generated" + File.separator + 
																		      File.separator + "model" + File.separator + fileName + ".xml");
			}
			if (!fout.getParentFile().exists()) 
				if (!fout.getParentFile().mkdirs()) {
					throw new IOException("Error occured while generating output directory!");
			}
			
			System.out.println("XML Writer writing " + fout.getAbsolutePath());
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(fout);
 
			transformer.transform(source, result);
			KrokiMockupToolApp.getInstance().displayTextOutput("[XML WRITER] " + fileName + ".xml file generated successfully.", 0);
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
