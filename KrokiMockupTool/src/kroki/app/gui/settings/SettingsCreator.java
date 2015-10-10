package kroki.app.gui.settings;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface SettingsCreator {
	/** Method which should be activated once the settings have been changed. Container which 
	 * contains the settings panels should refresh its components*/
    public void settingsPreformed();
	/** Method which should be activated once the settings have been changed. Container which 
	 * contains the settings panels should refresh its components, including the tree*/
    public void settingsPreformedIncludeTree();
}
