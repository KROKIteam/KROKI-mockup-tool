package util.staticnames;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Settings {

	/**
	 * Maximum char size when creating swing components
	 * Everything bigger than this number will be reduced to it.
	 */
	public static final int MAX_CHAR_SIZE = 30;
	
	/**
	 * Directory that is containing icons for buttons
	 */
	public static final String iconsDirectory = read("icons.dir");
	
	public static final String TABLE = "TABLE";
	public static final String INPUT = "INPUT";

	public static final String MAIN_FORM = read("main.form.name");

	public static final String USERNAME = read("login.username");
	public static final String PASSWORD = read("login.password");

	public static final String BTN_EXECUTE = read("btn.execute");
	public static final String BTN_CANCEL = read("btn.cancel");
	public static final String BTN_OK = read("btn.ok");

	public static final String LOGIN = read("login");

	public static String read(String name) {
		try {
			return ResourceBundle.getBundle("props.main").getString(name);
		} catch (MissingResourceException e) {
			return ResourceBundle.getBundle("props.main-generated").getString(name);
		}
	}
}
