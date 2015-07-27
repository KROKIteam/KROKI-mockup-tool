package kroki.app.generators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

	private DatabaseProps  parameters;
	private String date;
	private static String appPath = "/home/student2014/kroki/meanApp";
	private static String resourcePath="/home/student2014/kroki/KROKI-mockup-tool/KrokiMockupTool/src/kroki/app/generators/templates/";
	private static Map<String,EJBClass> model = new HashMap<String,EJBClass>();
	public MeanApplicationGenerator() {
		
	}
	
	public static void generate(ArrayList<EJBClass> classes, ArrayList<Menu> menus, ArrayList<VisibleElement> elements, ArrayList<Enumeration> enumerations, Submenu rootMenu) {
		model.put("klasa",classes.get(0));
		resolveTemplate("mean_public_controllers_js.ftl");
	}
	
	public static String parseName(String name) {
		String[] splits = name.split("_");
		StringBuilder ret = new StringBuilder();
		if (splits==null || splits.length<2 || !splits[0].equals("mean")) {
			return null;
		}
		for (int i=1; i<splits.length; i++) {
			if (i<=splits.length-2) {
				ret.append(splits[i]);
				ret.append(File.separator);
			} else {
				String exten = splits[splits.length-1];
				exten = exten.substring(0, exten.length()-4);
				ret.append(model.get("klasa").getLabel());
				ret.append("." + exten);
				break;
			}
		}
		return ret.toString();
	}
	
	public static void resolveTemplate(String fileName) {
		KrokiMockupToolApp.getInstance().displayTextOutput("[MEAN APP GENERATOR] generating view package...", 0);
		File f = new File(resourcePath);
		File fout = null;
		OutputStreamWriter writer = null;
		String filePath = parseName(fileName);
		try {
			fout = new File(resourcePath + File.separator + filePath);
			writer = new OutputStreamWriter(new FileOutputStream(fout));
		}catch (IOException ioe) {
			try {
				fout = new File(resourcePath + filePath);
				writer = new OutputStreamWriter(new FileOutputStream(fout));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				KrokiMockupToolApp.getInstance().displayTextOutput(e.getMessage(), 3);
			}
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
		
		Configuration cfg = new Configuration();
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		FileTemplateLoader templateLoader;
		
		try {
			templateLoader = new FileTemplateLoader(new File(resourcePath));
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
