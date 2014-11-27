package adapt.util.staticnames;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

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
			repoPath = RepositoryPathsUtil.getAppRootPath() + File.separator + "ApplicationRepository" + File.separator + "static" + File.separator + "props";
			prop = readProps(bundleName, propertyName, repoPath);
		}
		return prop;

	}

	public static String readGeneratedProp(String bundleName, String propertyName) {
		String repoPath = RepositoryPathsUtil.getGeneratedRepositoryPath() + File.separator + "props";
		String prop = readProps(bundleName, propertyName, repoPath);
		if(prop == null) {
			repoPath = RepositoryPathsUtil.getAppRootPath() + File.separator + "ApplicationRepository" + File.separator + "generated" + File.separator + "props";
			prop = readProps(bundleName, propertyName, repoPath);
		}
		return prop;
	}

	private static String readProps(String bundleName, String propertyName, String path) {
		File propFolder = new File(path);
		try {
			URL[] urls = new URL[]{propFolder.toURI().toURL()};
			ClassLoader loader = new URLClassLoader(urls);
			ResourceBundle bundle = ResourceBundle.getBundle(bundleName, Locale.ENGLISH, loader);
			return bundle.getString(propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
