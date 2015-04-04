package adapt.util.repository_utils;

import java.io.File;

public class RepositoryPathsUtil {

	public static String getRepositoryRootPath() {
		File f = new File(".");
		return f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + "ApplicationRepository";
	}
	
	public static String getAppRootPath() {
		File f = new File(".");
		return f.getAbsolutePath().substring(0, f.getAbsolutePath().length()-9);
	}
	
	public static String getStaticRepositoryPath() {
		return getRepositoryRootPath() + File.separator + "static";
	}
	
	public static String getGeneratedRepositoryPath() {
		return getRepositoryRootPath() + File.separator + "generated";
	}
	
	public static String getGeneratedModelPath() {
		return getGeneratedRepositoryPath() + File.separator + "model";
	}
	
	public static String getStaticModelPath() {
		return getStaticRepositoryPath() + File.separator + "model";
	}
}