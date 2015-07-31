package kroki.app.generators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import kroki.app.KrokiMockupToolApp;
import kroki.app.generators.utils.EJBClass;
import kroki.app.generators.utils.Enumeration;
import kroki.app.generators.utils.Menu;
import kroki.app.menu.Submenu;
import kroki.profil.VisibleElement;
import kroki.profil.utils.DatabaseProps;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MeanApplicationGenerator {

	//docs/models generisati kao fajl
	
	private String date;
	private static String appPath = null;
	private static String currPackagePath = null;
	private static String currFilePath = null;
	private static String resourcePath="src" + File.separator + "kroki" + File.separator + "app" + File.separator + "generators" + File.separator + "templates";
	private static Map<String,EJBClass> model = new HashMap<String,EJBClass>();
	public MeanApplicationGenerator() {
		
	}
	
	public static void generate(ArrayList<EJBClass> classes, ArrayList<Menu> menus, ArrayList<VisibleElement> elements, ArrayList<Enumeration> enumerations, Submenu rootMenu) {
		model.put("class",classes.get(0));
		File f1 = new File(".");
		File f = new File(f1.getAbsolutePath().substring(0,f1.getAbsolutePath().length()-1)+resourcePath);
		
	    FilenameFilter viewFilter = new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return (name.toLowerCase().startsWith("mean") && name.toLowerCase().endsWith("html.ftl"));
	        }
	    };
	    
	    FilenameFilter configFilter = new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return ((name.toLowerCase().startsWith("mean") && (name.toLowerCase().split("_").length<=3)) || name.toLowerCase().startsWith("mean_docs"));
	        }
	    };
	    
	    FilenameFilter classFilter = new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return (name.toLowerCase().startsWith("mean") && name.toLowerCase().split("_").length>3  && !name.toLowerCase().endsWith("sjs.ftl") && !name.toLowerCase().endsWith("html.ftl") && !name.toLowerCase().startsWith("mean_docs"));
	        }
	    };
	    
	    FilenameFilter pluralClassFilter = new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return (name.toLowerCase().startsWith("mean") && name.toLowerCase().split("_").length>3 && !name.toLowerCase().endsWith("html.ftl") && name.toLowerCase().endsWith("sjs.ftl") && !name.toLowerCase().startsWith("mean_docs"));
	        }
	    };
	    
		appPath = f1.getAbsolutePath().substring(0,f1.getAbsolutePath().length()-1);
		appPath = appPath.substring(0, appPath.length()-16) + "MeanApp" + File.separator + "packages" + File.separator + "custom" + File.separator;
		
		
		for (EJBClass klasa : classes) {

			model.put("class",klasa);
			File currPackageDir = new File(appPath+klasa.getLabel()+ "s" + File.separator);
			currPackageDir.mkdir();
			currPackagePath = currPackageDir.getAbsolutePath()+ File.separator;

		    File[] classFiles = f.listFiles(classFilter);
		    for (File file : classFiles) {
		        if (!file.isDirectory()) {
					currFilePath = parseNameWithClass(file.getName());
		        	resolveTemplate(file.getName());
		        } 
		    }
		    
		    File[] pluralClassFiles = f.listFiles(pluralClassFilter);
		    for (File file : pluralClassFiles) {
		        if (!file.isDirectory()) {
					currFilePath = parseNameWithPluralClass(file.getName());
		        	resolveTemplate(file.getName());
		        } 
		    }
		    
		    File[] viewFiles = f.listFiles(viewFilter);
		    for (File file : viewFiles) {
		        if (!file.isDirectory()) {
					currFilePath = parseNameWithoutClass(file.getName());
		        	resolveTemplate(file.getName());
		        } 
		    }
		    
		    File[] configFiles = f.listFiles(configFilter);
		    for (File file : configFiles) {
		        if (!file.isDirectory()) {
					currFilePath = parseNameWithoutClass(file.getName());
		        	resolveTemplate(file.getName());
		        } 
		    }
		}
	}
	
	public static String parseNameWithClass(String name) {
		String[] splits = name.split("_");
		StringBuilder ret = new StringBuilder();
		if (splits==null || splits.length<2 || !splits[0].equals("mean")) {
			return null;
		}
		for (int i=1; i<splits.length; i++) {
			if (i<=splits.length-2) {
				ret.append(splits[i]);
				File dir = new File(currPackagePath + ret.toString());
				dir.mkdir();
				ret.append(File.separator);
			} else {
				String exten = splits[splits.length-1];
				exten = exten.substring(0, exten.length()-4);
				ret.append(model.get("class").getLabel());
				ret.append("." + exten);
				break;
			}
		}
		return ret.toString();
	}
	
	public static String parseNameWithPluralClass(String name) {
		String[] splits = name.split("_");
		StringBuilder ret = new StringBuilder();
		if (splits==null || splits.length<2 || !splits[0].equals("mean")) {
			return null;
		}
		for (int i=1; i<splits.length; i++) {
			if (i<=splits.length-2) {
				ret.append(splits[i]);
				File dir = new File(currPackagePath + ret.toString());
				dir.mkdir();
				ret.append(File.separator);
			} else {
				ret.append(model.get("class").getLabel());
				ret.append("s.js");
				break;
			}
		}
		return ret.toString();
	}
	
	public static String parseNameWithoutClass(String name) {
		String[] splits = name.split("_");
		StringBuilder ret = new StringBuilder();
		if (splits==null || splits.length<2 || !splits[0].equals("mean")) {
			return null;
		}
		for (int i=1; i<splits.length; i++) {
			if (i<=splits.length-3) {
				ret.append(splits[i]);
				File dir = new File(currPackagePath + ret.toString());
				dir.mkdir();
				ret.append(File.separator);
			} else {
				String exten = splits[splits.length-1];
				exten = exten.substring(0, exten.length()-4);
				ret.append(splits[i]);
				ret.append("." + exten);
				break;
			}
		}
		return ret.toString();
	}
	
	public static void resolveTemplate(String fileName) {
		KrokiMockupToolApp.getInstance().displayTextOutput("[MEAN APP GENERATOR] generating view package...", 0);
		File fout = null;
		OutputStreamWriter writer = null;

		try {
			fout = new File(currPackagePath + currFilePath);
			writer = new OutputStreamWriter(new FileOutputStream(fout));
		}catch (Exception e) {
			e.printStackTrace();
			KrokiMockupToolApp.getInstance().displayTextOutput(e.getMessage(), 3);
		}
		Template tpl = prepareTemplate(fileName);

		try {
			tpl.process(model,writer);
		} catch (TemplateException | IOException e) {
			e.printStackTrace();
			KrokiMockupToolApp.getInstance().displayTextOutput(e.getMessage(), 3);
		}
	}
	
	public static Template prepareTemplate(String templateFile) {
		Template template = null;
		File f = new File(".");
		Configuration cfg = new Configuration();
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		FileTemplateLoader templateLoader;
		
		try {
			templateLoader = new FileTemplateLoader(new File(f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1)+resourcePath));
			cfg.setTemplateLoader(templateLoader);
			template = cfg.getTemplate(templateFile);
		} catch (IOException e) {
			//e.printStackTrace();
//			KrokiMockupToolApp.getInstance().displayTextOnConsole("[DB CONFIG GENERATOR] Templates directory not found. Trying the alternative one...", 0);
			KrokiMockupToolApp.getInstance().setBinaryRun(true);
			try {
				templateLoader = new FileTemplateLoader(new File(resourcePath));
				cfg.setTemplateLoader(templateLoader);
				template = cfg.getTemplate(templateFile);
				KrokiMockupToolApp.getInstance().displayTextOutput("[MEAN APP GENERATOR] Templates loaded ok.", 0);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return template;
	}
	
	public String prepareComment() {
		return "<!-- File generated using KROKI MeanApplicationGenerator class -->" + 
			   "\n<!-- Creation time and date: " + date + " -->";
	}
 
}
