package graphedit.properties;

import java.awt.Color;
import java.io.FileOutputStream;
import java.util.Properties;


/**
 * Provides couple of utility, convenience methods for 
 * configuration data storage. 
 * @author specijalac
 *
 */
public class Preferences {
	
	public static final String PROPERTIES_FILE_PATH = "./GraphEdit/workspace.properties";

	public static final String MIN_ZOOM = "minZoomFactor";
	
	public static final String MAX_ZOOM = "maxZoomFactor";

	public static final String BESTFIT_FACTOR = "bestFitFactor";

	public static final String RIGHT_ANGLE = "rightAngle";

	public static final String CLASS_WIDTH = "defaultClassWidth";
	
	public static final String CLASS_HEIGHT = "defaultClassHeight";

	public static final String CLASS_COLOR_1 = "classColor1";
	
	public static final String CLASS_COLOR_2 = "classColor2";

	public static final String INTERFACE_COLOR_1 = "interfaceColor1";
	
	public static final String INTERFACE_COLOR_2 = "interfaceColor2";
	
	public static final String WORKSPACE_PATH_KEY = "path";
	
	public static final String PACKAGE_COLOR_1 = "packageColor1";
	
	public static final String PACKAGE_COLOR_2 = "packageColor2";
	
	/**
	 * Used as prefix for loading defaults.
	 */
	public static final String DEFAULT = "default.";
	
	private static Preferences preferences;
	
	private Properties properties;
	
	private Preferences() {
		properties = new Properties();
		loadProperties();
	}
	
	/**
	 * This method obtains a unique, singleton instance of its own class.
	 * @return singleton instance of <code>Preferences</code>.
	 * @author specijalac
	 */
	public static Preferences getInstance() {
		if (preferences == null)
			preferences = new Preferences();
		return preferences;
	}
	
	/**
	 * This method gets the property determined by its key value.
	 * @param key determines the property.
	 * @return a corresponding value.
	 * @author specijalac
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * This method sets the property determined by its key value.
	 * @param key determines the property.
	 * @param value represents a new value.
	 * @author specijalac
	 */
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	/**
	 * This method loads the key, value pairs from a specified 
	 * <code>properties</code> file.
	 * @author specijalac
	 */
	private void loadProperties() {
		properties = new Properties();
		try {
			properties.load(this.getClass().getResourceAsStream( "/properties/workspace.properties" ));
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * This method stores the key, value pairs into a <code>properties</code> file.
	 * @author specijalac
	 */
	public void saveProperties() {
		try {
			properties.store(new FileOutputStream(PROPERTIES_FILE_PATH), null);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * This method parses a color string determined by its the key.
	 * @param key determines the color property
	 * @return an instance of <code>Color</code> class.
	 * @author specijalac
	 */
	public Color parseColor(String key) {
		String[] split = properties.getProperty(key).split(",");
		return new Color(Integer.parseInt(split[0]),
				Integer.parseInt(split[1]),
				Integer.parseInt(split[2]));
	}

	/**
	 * This method corresponds to {@link #parseColor(String key)}
	 * @param value represents a <code>Color</code> which is to be unparsed.
	 * @return string representation, suitable for properties file.
	 * @author specijalac
	 */
	public String unparseColor(Color value) {
		return value.getRed() + "," + value.getGreen() + "," + value.getBlue();
	}

}
