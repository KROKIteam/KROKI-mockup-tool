package util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.h2.constant.SysProperties;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * @author john.thompson
 *
 */
public class SchemaGenerator
{
	private Configuration cfg;

	public SchemaGenerator(String packageName) throws Exception {
		cfg = new Configuration();
		cfg.setProperty("hibernate.hbm2ddl.auto","update");

		ArrayList<Class> classes = (ArrayList<Class>) getClasses(packageName);
		for(int i=0; i<classes.size(); i++) {
			Class clazz = classes.get(i);
			cfg.addAnnotatedClass(clazz);
		}
	}

	/**
	 * Method that actually creates the file.  
	 * @param dbDialect to use
	 */
	public void generate() {
		cfg.configure();
		SchemaExport export = new SchemaExport(cfg);
		export.setDelimiter(";");
		export.setFormat(true);
		export.setOutputFile("ddl_createDatabase.sql"); 
		//export.execute(true, true, true, true);
		export.create(false, true);
	}

	/**
	 * Utility method used to fetch Class list based on a package name.
	 * @param packageName (should be the package containing your annotated beans.
	 */
	private List getClasses(String packageName) throws Exception {
		final String classExt = ".class";
		ArrayList<String> files = listFilesInPackage(packageName);
		ArrayList<Class> classes = new ArrayList<Class>(files.size());

		for (String file : files) {
			if (file.endsWith(classExt)) {
				// removes the .class extension, and replaces '/' with '.'
				String className = (file.substring(0, file.length() - classExt.length())).replace(File.separatorChar, '.');
				classes.add(Class.forName(className));
			}
		}

		return classes;
	}

	public static ArrayList<String> listFilesInPackage(String packageName) throws ClassNotFoundException, IOException {
		ArrayList<String> classNames = new ArrayList<String>();
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = packageName.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(packageName + " (" + directory
					+ ") does not appear to be a valid package");
		}
		if (directory.exists()) { // Deal with file-system case
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				classNames.add(packageName.replace('.', File.separatorChar) + '.' + files[i]);
			}
		}
		else { // Deal with case where files are within a JAR
			final String[] parts = directory.getPath().split(".jar!\\\\");
			if (parts.length == 2) {
				String jarFilename = parts[0].substring(6) + ".jar";
				String relativePath = parts[1].replace(File.separatorChar, '/');
				JarFile jarFile = new JarFile(jarFilename);
				final Enumeration entries = jarFile.entries();
				while (entries.hasMoreElements()) {
					final JarEntry entry = (JarEntry) entries.nextElement();
					final String entryName = entry.getName();
					if ((entryName.length() > relativePath.length()) && entryName.startsWith(relativePath)) {
						classNames.add(entryName.replace('/', File.separatorChar));
					}
				}
			}
			else {
				throw new ClassNotFoundException(packageName
						+ " is not a valid package");
			}
		}

		return classNames;
	}
}