package kroki.app.generators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kroki.app.generators.utils.Enumeration;
import kroki.app.generators.utils.XMLWriter;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * Generates enumerations
 * @author Kroki Team
 */
import kroki.app.KrokiMockupToolApp;

public class EnumerationGenerator {

	private XMLWriter writer;
	private DocumentBuilderFactory factory;
	private Boolean swing;
	private File outputFile;
	private String appPath;

	public EnumerationGenerator(Boolean swing) {
		this.writer = new XMLWriter();
		this.factory = DocumentBuilderFactory.newInstance();
		this.swing = swing;
		File f = new File(".");
		appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
		this.outputFile = getOutputFile();
	}

	public void generateXMLFiles(ArrayList<Enumeration> enmumerations) {
		try {
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			Document document = docBuilder.newDocument();

			//root <enums> tag
			Element rootElement = document.createElement("enums");
			document.appendChild(rootElement);

			//for each enumeration, <enum> tag is generated
			for (Enumeration enumeration : enmumerations) {

				Element enumElement = document.createElement("enum");

				//name attribute
				Attr nameAttr = document.createAttribute("name");
				nameAttr.setValue(enumeration.getName());
				enumElement.setAttributeNode(nameAttr);

				//label attribute
				Attr labelAttr = document.createAttribute("label");
				labelAttr.setValue(enumeration.getLabel());
				enumElement.setAttributeNode(labelAttr);

				//<value> tag for every enumeration value
				for(int i=0; i<enumeration.getValues().length; i++) {
					Element valueElement = document.createElement("value");
					valueElement.setTextContent(enumeration.getValues()[i]);
					enumElement.appendChild(valueElement);
				}

				rootElement.appendChild(enumElement);

			}

			writer.write(document, "enumerations-generated", swing);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void generateEnumFiles(ArrayList<Enumeration> enumerations) {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy  H:mm:ss");
		String d = formatter.format(now);
		KrokiMockupToolApp.getInstance().displayTextOutput("[" + d + "]" + " generating enumeration files...", 0);
		deleteFiles(getOutputFile());
		
		for (Enumeration enumeration : enumerations) {
			Configuration cfg = new Configuration();
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			FileTemplateLoader templateLoader;
			Template tpl = null;
			try {
				templateLoader = new FileTemplateLoader(new File(appPath + "src" + File.separator + "kroki" + File.separator + "app" + File.separator + "generators" + File.separator + "templates"));
				cfg.setTemplateLoader(templateLoader);
				tpl = cfg.getTemplate("enumeration.ftl");
				
			} catch (IOException e) {
				KrokiMockupToolApp.getInstance().displayTextOutput("[ENUM GENERATOR] Templates directory not found. Trying the alternative one...", 0);
				try {
					templateLoader = new FileTemplateLoader(new File(appPath + "templates"));
					cfg.setTemplateLoader(templateLoader);
					tpl = cfg.getTemplate("EJBClass.ftl");
					KrokiMockupToolApp.getInstance().displayTextOutput("[ENUM GENERATOR] Templates loaded ok.", 0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			try {
				File outFile = new File(getOutputFile().getAbsolutePath() + File.separator + enumeration.getName() + ".java");
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outFile));
				
				Map model = new TreeMap();
				String doc = "   /** " +
						"\n   File generated using Kroki EnumGenerator " +
						"\n   @Author KROKIteam " +
						"\n   **/";
				
				model.put("enum", enumeration);
				model.put("doc", doc);
				
				tpl.process(model, writer);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public File getOutputFile() {
		File out = null;
		if(swing) {
			out = new File(appPath.substring(0, appPath.length()-16) +  "SwingApp" + File.separator + "src" + File.separator + "com" + File.separator + "panelcomposer" + File.separator + "enumerations");
		}else {
			out = new File(appPath.substring(0, appPath.length()-16) +  "ApplicationRepository" + File.separator + "generated" + File.separator +  "model" + File.separator + "enumerations_generated");
		}
		return out;
	}

	public boolean deleteFiles(File directory) {
		boolean success = false;
		
		if (!directory.exists()) {
			return false;
		}
		if (!directory.canWrite()) {
			return false;
		}
		
		File[] files = directory.listFiles();
		for(int i=0; i<files.length; i++) {
			File file = files[i];
			if(!file.delete()) {
				success = false;
			}
		}
		return success;
	}
	
}
