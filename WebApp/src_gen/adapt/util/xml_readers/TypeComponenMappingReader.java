package adapt.util.xml_readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import adapt.core.AppCache;
import adapt.util.repository_utils.RepositoryPathsUtil;
import adapt.util.staticnames.Tags;
import adapt.util.xml_utils.XMLParserUtils;

/**
 * Reads mapping of language types to UI components
 * Each data type is specified with java fully qualified class name in mapping file
 * Each UI component is specified with it's id in mapping file and corresponding freemarker template in 'components-web.xml'
 * @author Milorad Filipovic
 */
public class TypeComponenMappingReader {

	protected static String staticModelPath		= RepositoryPathsUtil.getStaticModelPath();
	protected static String staticRepoPath		= RepositoryPathsUtil.getStaticRepositoryPath();
	protected static String componentsFile		= "components-web.xml";
	protected static String mappingFile			= "type-component-mappings.xml";

	private static String logPrefix = "TYPE-COMPONENT MAPPING READER: ";

	private static Map<String, String> mappings = new HashMap<String, String>();
	private static Map<String, String> components = new HashMap<String, String>();

	public static void mapTypesToComponents() {
		loadMappings();
		loadComponents();
		for (String languageType : mappings.keySet()) {
			String componentId = mappings.get(languageType);
			String templateFile = components.get(componentId);
			AppCache.getInstance().addTypeComponentMapping(languageType, templateFile);
			AppCache.displayTextOnMainFrame(logPrefix + "Mapping " + languageType + " --> " + templateFile, 0);
		}
	}

	/**
	 * Loads template path for specified component name
	 * @param id Unique component name
	 */
	public static String loadComponent(String id) {
		String templatePath = "/components/" + id;
				/*	staticRepoPath + File.separator + 
					"gui" + File.separator + 
					"web" + File.separator + 
					"component-templates" + File.separator + id;*/
		return templatePath;
	}

	//Parse mappings from mappings file and load data types-to-component id mappings
	private static void loadMappings() {
		try {
			AppCache.displayTextOnMainFrame(logPrefix + "Reading mapping file: " + mappingFile, 0);
			Document document = XMLParserUtils.parseXml(staticModelPath + File.separator + mappingFile);
			NodeList propertyNodes = document.getElementsByTagName(Tags.PROPERTY);
			for(int i=0; i<propertyNodes.getLength(); i++) {
				Element propertyElement = (Element)propertyNodes.item(i);
				String languageType = propertyElement.getAttribute(Tags.LANGUAGE_TYPE);
				String componentId  = propertyElement.getAttribute(Tags.COMPONENT_ID);
				System.out.println(logPrefix + "READING MAPPING " + languageType + " : " + componentId);
				mappings.put(languageType, componentId);
			}
		} catch (Exception e) {
			if(e instanceof FileNotFoundException) {
				AppCache.displayTextOnMainFrame(logPrefix + "Mapping file not found!",  1);
			}else {
				AppCache.displayTextOnMainFrame(logPrefix + "Error reading mapping file.", 1);
				AppCache.displayStackTraceOnMainFrame(e);
			}
		}
	}

	//Parse components file and load component id-to-freemarker template mappings
	private static void loadComponents() {
		try {
			AppCache.displayTextOnMainFrame(logPrefix + "Reading components file: " + componentsFile, 0);
			Document document = XMLParserUtils.parseXml(staticModelPath + File.separator + componentsFile);
			NodeList componentNodes = document.getElementsByTagName(Tags.COMPONENT);
			for(int i=0; i<componentNodes.getLength(); i++) {
				Element componentElement = (Element)componentNodes.item(i);
				String componentId = componentElement.getAttribute(Tags.ID);
				String templateFile = componentElement.getAttribute(Tags.TEMPLATE_FILE);
				components.put(componentId, templateFile);
			}
		} catch (Exception e) {
			AppCache.displayTextOnMainFrame("Error reading components file", 1);
			AppCache.displayStackTraceOnMainFrame(e);
		}
	}
}