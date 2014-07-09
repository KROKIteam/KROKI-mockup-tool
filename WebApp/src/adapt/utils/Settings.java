package adapt.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Settings {

	public static String APP_TITLE = read("app.title");
	public static String APP_DESC = read("app.description");

	public static String read(String name) {
		try {
			//when running exported jar, properies files have 'props' prefix
			return ResourceBundle.getBundle("props.app").getString(name);
		}catch(MissingResourceException mre) {
			return ResourceBundle.getBundle("app").getString(name);
		}
	}

}
