package adapt.utils;

import java.util.ResourceBundle;

public class Settings {

	public static String APP_TITLE = read("app.title");
	public static String APP_DESC = read("app.description");

	public static String read(String name) {
		return ResourceBundle.getBundle("app").getString(name);
	}

}
