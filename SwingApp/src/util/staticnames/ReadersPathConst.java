package util.staticnames;

import java.io.File;
import java.util.ResourceBundle;

public class ReadersPathConst {

	public static final String XML_MAPPINGS_FILE = read("entity.xmlmappings.name");
	public static final String MODEL_DIR_PATH = read("model.dir");

	public static final String ENUM_FILE_NAME = read("enum.file.name");

	public static final String MENU_FILE_NAME = read("menu.file.name");

	public static final String PANELS_DIR = read("panels.dir");
	public static final String PANELS_FILE_NAME = read("panels.file.name");
	public static final String PANELS_MAP_FILE_NAME = read("panels.map.file.name");

	public static final String USERS_FILE_NAME = read("users.file.name");

	public static final String TYPE_COMPONENT_FILE_NAME = read("type.component.file.name");
	
	//TODO:
	//transform into relative paths
	public static final String XSD_ENTITY = "D:" + File.separator + "workspace" + File.separator + "kroki-integracija-clone" + File.separator + "SwingApp" + File.separator + "xsd" + File.separator + "entity.xsd";
	public static final String XSD_ENUM = "D:" + File.separator + "workspace" + File.separator + "kroki-integracija-clone" + File.separator + "SwingApp" + File.separator + "xsd" + File.separator + "enumerations.xsd";
	public static final String XSD_MENU = "D:" + File.separator + "workspace" + File.separator + "kroki-integracija-clone" + File.separator + "SwingApp" + File.separator + "xsd" + File.separator + "menu.xsd";
	public static final String XSD_PANEL_MAP = "D:" + File.separator + "workspace" + File.separator + "kroki-integracija-clone" + File.separator + "SwingApp" + File.separator + "xsd" + File.separator + "panel-map.xsd";
	public static final String XSD_PANELS = "D:" + File.separator + "workspace" + File.separator + "kroki-integracija-clone" + File.separator + "SwingApp" + File.separator + "xsd" + File.separator + "panels.xsd";
	public static final String XSD_XML_MAP = "D:" + File.separator + "workspace" + File.separator + "kroki-integracija-clone" + File.separator + "SwingApp" + File.separator + "xsd" + File.separator + "xml-map.xsd";

	public static String read(String name) {
		return ResourceBundle.getBundle("props.xmlpaths").getString(name);
	}
}