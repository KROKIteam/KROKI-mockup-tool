package adapt.aspects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import adapt.application.AdaptApplication;
import adapt.utils.XMLResource;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public aspect EJBGenerator {

	public pointcut generate() :
		call (* AdaptApplication.getXMLResources());
	
	@SuppressWarnings("rawtypes")
	after() returning(ArrayList<XMLResource> XMLResources) : generate() {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy  H:mm:ss:SSS");
		String d = formatter.format(now);
		System.out.println("[" + d + "]" + " generating JPA Entity classes...");
		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
		
		for(int i=0; i<XMLResources.size(); i++) {
			XMLResource res = XMLResources.get(i);
		
			Configuration cfg = new Configuration();
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			FileTemplateLoader templateLoader;
			try {
				templateLoader = new FileTemplateLoader(new File(appPath + "src/adapt/templates"));
				cfg.setTemplateLoader(templateLoader);
				Template tpl = cfg.getTemplate("EJBClass.ftl");
				
				File fout = new File(appPath + "src" + File.separator + "adapt" + File.separator + "entities" + File.separator + res.getName() + ".java");
				if (!fout.getParentFile().exists()) 
					if (!fout.getParentFile().mkdirs()) {
						throw new IOException("Greska pri kreiranju izlaznog direktorijuma ");
				}
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fout));
				
				Map model = new TreeMap();
				String doc = "   /** " +
						     "\n   Class generated using EJBGeneratorAspect " +
						     "\n   @Author KROKI team " +
						     "\n   Creation date: " + d + "h" +
						     "\n   **/";
						
				model.put("resource", res);
				model.put("doc", doc);
				
				tpl.process(model, writer);
			} catch (IOException e) {
				System.out.println("puko1");
				e.printStackTrace();
			} catch (TemplateException e) {
				System.out.println("puko2");
				e.printStackTrace();
			} 
			
		}
		System.out.println("[" + d + "] " + XMLResources.size() + " JPA classes successfully generated.");
	}
}