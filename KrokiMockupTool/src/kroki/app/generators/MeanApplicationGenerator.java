package kroki.app.generators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import kroki.app.KrokiMockupToolApp;
import kroki.app.generators.utils.EJBClass;
import kroki.app.generators.utils.Enumeration;
import kroki.app.generators.utils.Menu;
import kroki.app.menu.Submenu;
import kroki.profil.VisibleElement;
import kroki.profil.utils.DatabaseProps;

public class MeanApplicationGenerator {

	private DatabaseProps  parameters;
	private String date;
	private String appPath = "home/student2014/PRAKSA_KROKI/meanApp";
	
	public MeanApplicationGenerator() {
		
	}
	
	public void generate(ArrayList<EJBClass> classes, ArrayList<Menu> menus, ArrayList<VisibleElement> elements, ArrayList<Enumeration> enumerations, Submenu rootMenu) {
		resolveTemplate("mean_public_controller.ftl");
	}
	
	public String parseName(String name) {
		String[] splits = name.split("_");
		StringBuilder ret = new StringBuilder();
		if (splits==null || splits.length<2 || !splits[0].equals("mean")) {
			return null;
		}
		for (int i=1; i<splits.length; i++) {
			ret.append(splits[i]);
			if (i<splits.length-2) {
				ret.append(File.separator);
			} else {
				ret.append("." + splits[splits.length-1]);
				break;
			}
		}
		return ret.toString();
	}
	
	public void resolveTemplate(String fileName) {
		KrokiMockupToolApp.getInstance().displayTextOutput("[MEAN APP GENERATOR] generating view package...", 0);
		File f = new File(appPath);
		File fout = null;
		OutputStreamWriter writer = null;
		String filePath = parseName(fileName);
		try {
			fout = new File(appPath + File.separator + filePath);
			writer = new OutputStreamWriter(new FileOutputStream(fout));
		}catch (IOException ioe) {
			try {
				fout = new File(appPath + filePath);
				writer = new OutputStreamWriter(new FileOutputStream(fout));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				KrokiMockupToolApp.getInstance().displayTextOutput(e.getMessage(), 3);
			}
		}
		Template tpl = prepareTemplate(fileName);
		Map model = new TreeMap();

		try {
			tpl.process(model, writer);
		} catch (TemplateException | IOException e) {
			e.printStackTrace();
			KrokiMockupToolApp.getInstance().displayTextOutput(e.getMessage(), 3);
		}
	}
	
	public Template prepareTemplate(String templateFile) {
		Template template = null;
		
		Configuration cfg = new Configuration();
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		FileTemplateLoader templateLoader;
		
		try {
			templateLoader = new FileTemplateLoader(new File(appPath + "src/kroki/app/generators/templates"));
			cfg.setTemplateLoader(templateLoader);
			template = cfg.getTemplate(templateFile);
		} catch (IOException e) {
			//e.printStackTrace();
//			KrokiMockupToolApp.getInstance().displayTextOnConsole("[DB CONFIG GENERATOR] Templates directory not found. Trying the alternative one...", 0);
			KrokiMockupToolApp.getInstance().setBinaryRun(true);
			try {
				templateLoader = new FileTemplateLoader(new File(appPath + "mean/templates"));
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
