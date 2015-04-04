package adapt.util.staticnames;

public class Settings {

	public static final String APP_TITLE = PropertiesReader.readGeneratedProp("main", "app.title");
	public static final String APP_DESCRIPTION = PropertiesReader.readGeneratedProp("main", "app.description");
	
	public static final String BOOL_TRUE = PropertiesReader.readStaticProp("main", "boolean.true");
	public static final String BOOL_FALSE = PropertiesReader.readStaticProp("main", "boolean.false");
	public static final String NULL_VALUE = PropertiesReader.readStaticProp("main", "null");
	
	public static final String LOGIN_USERNAME = PropertiesReader.readStaticProp("main", "login.username");
	public static final String LOGIN_PASSWORD = PropertiesReader.readStaticProp("main", "login.password");
	
	public static final String BTN_OK = PropertiesReader.readStaticProp("main", "btn.ok");
	public static final String BTN_CANCEL = PropertiesReader.readStaticProp("main", "btn.cancel");
	public static final String BTN_EXECUTE = PropertiesReader.readStaticProp("main", "btn.execute");
	
	public static final String DATE_TIME_SECONDS_FORMAT = "MMM d  H:mm:ss";
	public static final String DATE_TIME_MINUTES_FORMAT = "MMM d  H:mm:ss";
	public static final String FULL_DATE_FORMAT = "dd.MM.yyyy";
	public static final String FULL_DATE_TIME_FORMAT = "dd.MM.yyyy  H:mm:ss:SSS";
	public static final String CONDENSED_DATE_FORMAT = "ddMMyyyHmmss";
	
	public static final String WEB_THEME = PropertiesReader.readStaticProp("main", "web.theme");
	public static final String ICONS_DIR = PropertiesReader.readStaticProp("main", "icons.dir");
	public static final String WINDOW_ICON = PropertiesReader.readStaticProp("main", "windows.icon");
	public static final String WEB_MAINFRAME_ICON =  PropertiesReader.readStaticProp("main", "web.main.icon");
	public static final String DEPLOY = PropertiesReader.readStaticProp("main", "deploy");
}