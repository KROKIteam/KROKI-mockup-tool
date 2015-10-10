package kroki.app.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JARMaker {

	
	public void makeJAR(File inputDir, File outputDir, String fileName) throws FileNotFoundException, IOException {
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, "com.panelcomposer.core.MainApp");
		System.out.println("Generating JAR: " + outputDir.getAbsolutePath() + fileName);
		JarOutputStream target = new JarOutputStream(new FileOutputStream(outputDir.getAbsolutePath() + File.separator + fileName), manifest);
		add(inputDir, target, manifest);
		target.close();
	}

	private void add(File source, JarOutputStream target, Manifest manifest) throws IOException {
		String classpath = "";
		BufferedInputStream in = null;
		try {
			System.out.println("[SOURCE]" + source.getPath().replace("\\", "/"));
			//skipping source files
			if (!source.getName().equals("src")) {
				//lib entries to classpath
				if(source.getPath().contains("lib")) {
					String entryName = source.getPath().replace("\\", "/").substring(40);
					System.out.println("[LIB ENTRY]" + entryName + "   ");
					
					for (File nestedFile : source.listFiles()) {
						classpath += nestedFile.getName() + "   ";;
					}
					
					//add bin content to jar root
					if(source.getPath().contains("bin")) {
						if (source.isDirectory()) {
							String name = source.getPath().replace("\\", "/");
							if (!name.isEmpty()) {
								if (!name.endsWith("/")) {
									name += "/";
								}
								JarEntry entry = new JarEntry(name.substring(40));
								entry.setTime(source.lastModified());
								target.putNextEntry(entry);
								target.closeEntry();
							}
							for (File nestedFile : source.listFiles()) {
								add(nestedFile, target, manifest);
							}
							return;
						}
						String name = source.getPath().replace("\\", "/").substring(44);
						JarEntry entry = new JarEntry(name);
						entry.setTime(source.lastModified());
						target.putNextEntry(entry);
						in = new BufferedInputStream(new FileInputStream(source));
						byte[] buffer = new byte[1024];
						while (true) {
							int count = in.read(buffer);
							if (count == -1)
								break;
							target.write(buffer, 0, count);
						}
						target.closeEntry();
					//other entries
					}else {
						if (source.isDirectory()) {
							String name = source.getPath().replace("\\", "/");
							if (!name.isEmpty()) {
								if (!name.endsWith("/")) {
									name += "/";
								}
								//System.out.println("name: " + name.substring(40));
								JarEntry entry = new JarEntry(name.substring(40));
								entry.setTime(source.lastModified());
								target.putNextEntry(entry);
								target.closeEntry();
							}
							for (File nestedFile : source.listFiles()) {
								add(nestedFile, target, manifest);
							}
							return;
						}
						String name = source.getPath().replace("\\", "/").substring(40);
						JarEntry entry = new JarEntry(name);
						entry.setTime(source.lastModified());
						target.putNextEntry(entry);
						in = new BufferedInputStream(new FileInputStream(source));
						byte[] buffer = new byte[1024];
						while (true) {
							int count = in.read(buffer);
							if (count == -1)
								break;
							target.write(buffer, 0, count);
						}
						target.closeEntry();
					}
				}

			}
		}
		finally {
			if (in != null)
				in.close();
		}
		manifest.getMainAttributes().put(Attributes.Name.CLASS_PATH, classpath);
	}

}
