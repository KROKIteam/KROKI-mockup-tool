package graphedit.properties;

import graphedit.app.ApplicationMode;
import graphedit.app.MainFrame;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;

public class ApplicationModeProperties implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String UI_PATH = "/properties/UI.properties";
	private static final String UI_MIXED_PATH= "/properties/UI_mixed.properties";
	private static final String UI_PERSISTENT_PATH = "/properties/UI_persistent.properties";
	private static final String PERSISTENT_PATH = "/properties/persistent.properties";
	

	private Properties properties;
	private HashMap<String, Object> propertiesMap = new HashMap<String, Object>();

	private static ApplicationModeProperties applicationModeProperties;

	public ApplicationModeProperties(){

		properties = new Properties();
		loadProperties();
		
	}
	
	
	public void loadProperties(){
		propertiesMap.clear();
		try {
			if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE)
				properties.load(this.getClass().getResourceAsStream(UI_PATH));
			else if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE_MIXED)
				properties.load(this.getClass().getResourceAsStream(UI_MIXED_PATH));
			else if (MainFrame.getInstance().getAppMode() == ApplicationMode.USER_INTERFACE_PERSISTENT)
				properties.load(this.getClass().getResourceAsStream(UI_PERSISTENT_PATH));
			else
				properties.load(this.getClass().getResourceAsStream(PERSISTENT_PATH));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean hasProperty(String key){
		return properties.getProperty(key)!=null;
	}

	public Object getPropertyValue(String key){
		if (propertiesMap.containsKey(key))
			return propertiesMap.get(key);

		if (properties.getProperty(key) != null){
			String value = properties.getProperty(key).trim();
			if (value.equals("true") || value.equals("false")){
				Boolean boolValue = Boolean.valueOf(value);
				propertiesMap.put(key, boolValue);
				return boolValue;
			}
			propertiesMap.put(key, value);
			return value;
		}
		else {
			System.out.println(key);
			return "";
		}
	}

	public static ApplicationModeProperties getInstance(){
		if (applicationModeProperties == null)
			applicationModeProperties = new ApplicationModeProperties();
		return applicationModeProperties;
	}
}
