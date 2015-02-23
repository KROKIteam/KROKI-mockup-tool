package adapt.util.staticnames;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

import adapt.util.repository_utils.RepositoryPathsUtil;

/**
 * Class that reads property values from application repository
 * @author Milorad Filipovic
 *
 */
public class PropertiesReader {

	public static String readStaticProp(String bundleName, String propertyName) {
		String repoPath = RepositoryPathsUtil.getStaticRepositoryPath() + File.separator + "props";
		String prop = readProps(bundleName, propertyName, repoPath);
		if(prop == null) {
			repoPath = "ApplicationRepository" + File.separator + "static" + File.separator + "props";
			prop = readProps(bundleName, propertyName, repoPath);
		}
		return prop;

	}

	public static String readGeneratedProp(String bundleName, String propertyName) {
		String repoPath = RepositoryPathsUtil.getGeneratedRepositoryPath() + File.separator + "props";
		String prop = readProps(bundleName, propertyName, repoPath);
		if(prop == null) {
			repoPath = "ApplicationRepository" + File.separator + "generated" + File.separator + "props";
			prop = readProps(bundleName, propertyName, repoPath);
		}
		return prop;
	}

	private static String readProps(String bundleName, String propertyName, String path) {
		File propFolder = new File(path);
		try {
			// Try to read properties files from the build path
			// This will work if the framework is running as an eclipse project
			URL[] urls = new URL[]{propFolder.toURI().toURL()};
			ClassLoader loader = new URLClassLoader(urls);
			ResourceBundle bundle = ResourceBundle.getBundle(bundleName, Locale.ENGLISH, loader);
			return bundle.getString(propertyName);
		} catch (Exception e) {
			try {
				// If the framework is exported and ran as executable jar file,
				// properties files need to be accessed by specifying the full path
				propFolder = new File(RepositoryPathsUtil.getAppRootPath() + File.separator +  path);
				URL[] urls = new URL[] { propFolder.toURI().toURL() };
				ClassLoader loader = new URLClassLoader(urls);
				ResourceBundle bundle = ResourceBundle.getBundle(bundleName, Locale.ENGLISH, loader);
				return bundle.getString(propertyName);
			} catch (Exception e2) {
				//e2.printStackTrace();
				return null;
			}
		}
	}
}
