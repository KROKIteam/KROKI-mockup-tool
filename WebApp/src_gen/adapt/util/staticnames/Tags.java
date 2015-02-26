package adapt.util.staticnames;

public class Tags {

	public static final String XML_FILE = read("xml_file");
	public static final String PROPERTY = read("property");

	public static final String EJB = read("ejb");
	public static final String CLASS_TAG = read("class_tag");
	public static final String ATTRIBUTES = read("attributes");
	public static final String COLUMN_ATTRIBUTE = read("column_attribute");
	public static final String JOIN_COLUMN_ATTRIBUTE = read("zoom_attribute");

	public static final String NAME = read("name");
	public static final String LABEL = read("label");

	public static final String FIELD_NAME = read("field_name");
	public static final String DATA_TYPE = read("data_type");
	public static final String LENGTH = read("length");
	public static final String PRECISION = read("precision");
	public static final String SCALE = read("scale");
	public static final String UNIQUE = read("unique");
	public static final String NULLABLE = read("nullable");
	
	public static final String KEY = read("key");

	public static final String ZOOMED_BY = read("zoomed_by");
	public static final String CLASS_NAME = read("class_name");
	public static final String COLUMN_REF = read("column_reference");

	// panel tags:
	public static final String VISUAL = read("visual");
	public static final String STANDARD_PANELS = read("standard_panels");
	public static final String STANDARD_PANEL = read("standard_panel");
	public static final String PARENT_CHILD_PANELS = read("parent_child_panels");
	public static final String PARENT_CHILD = read("parent_child");
	public static final String PARENT_PANEL = read("parent_panel");
	public static final String CHILD_PANEL = read("child_panel");
	public static final String PANEL_REF = read("panel_reference");
	public static final String MANY_TO_MANY = read("many_to_many");
	public static final String OPPOSITE = read("opposite");
	
	public static final String PANEL = read("panel");
	
	public static final String SETTINGS = read("settings");
	public static final String OPERATION = read("operation");
	public static final String OPERATION_REFERENCE = read("operation_ref");
	public static final String DATA = read("data");
	
	public static final String ID = read("id");
	public static final String EJB_REF = read("ejb_ref");
	public static final String LEVEL = read("level");
	public static final String ASSOCIATION_END = read("association_end");
	public static final String OPERATION_GROUP = read("operation_group");
	public static final String ELEMENT_GROUP = read("element_group");
	
	public static final String ADD = read("add");
	public static final String DELETE = read("delete");
	public static final String UPDATE = read("update");
	public static final String COPY = read("copy");
	public static final String CHANGE_MODE = read("change_mode");
	public static final String NAVIGATION = read("navigation");
	public static final String VIEW_MODE = read("view_mode");
	public static final String STATE_MODE = read("state_mode");
	public static final String OPENED_AS = read("opened_as");
	
	public static final String PARAMETER = read("parameter");
	public static final String DEFAULT = read("default");
	
	public static final String NEXT = read("next");
	
	public static final String MENU_MAP = read("menu_map");
	public static final String MENU = read("menu");
	public static final String SUBMENU = read("submenu");
	//public static final String MENUITEM = read("menuitem");
	public static final String ACTIVATE = read("activate");
	public static final String PANEL_TYPE = read("panel_type");
	
	public static final String COMPONENT = read("component");
	public static final String COMPONENT_TYPE = read("component_type");
	public static final String LANGUAGE_TYPE = read("language_type");
	public static final String COMPONENT_ID = read("component_id");
	public static final String TEMPLATE_FILE = read("template_file");
	
	public static final String ENUM = read("enum");
	public static final String VALUE = read("value");
	
	public static final String HIDDEN = read("hidden");
	public static final String DISABLED = read("disabled");
	public static final String TARGET = read("target");
	public static final String ALLOWED = read("allowed");
	
	public static final String EDITABLE = read("editable");
	public static final String DERIVED = read("derived");
	public static final String FORMULA = read("formula");
	public static final String PARAMETER_TYPE = read("parameter_type");
	public static final String NEXT_PANEL = read("next_panel");
	public static final String ZOOM = read("zoom");
	
	public static String read(String propertyName) {
		return PropertiesReader.readStaticProp("tagutil", propertyName);
	}
}
