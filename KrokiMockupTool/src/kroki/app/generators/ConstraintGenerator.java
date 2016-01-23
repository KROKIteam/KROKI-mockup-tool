package kroki.app.generators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kroki.app.KrokiMockupToolApp;
import kroki.app.generators.utils.Attribute;
import kroki.app.generators.utils.EJBAttribute;
import kroki.app.generators.utils.EJBClass;
import kroki.app.generators.utils.Enumeration;
import kroki.app.generators.utils.ManyToOneAttribute;
import kroki.app.generators.utils.XMLWriter;
import kroki.commons.camelcase.NamingUtil;

import org.apache.commons.io.FileDeleteStrategy;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ConstraintGenerator {

	NamingUtil cc;
	XMLWriter writer = new XMLWriter();
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

	/***********************************************/
	/*           EJB CLASS GENERATION              */
	/***********************************************/
	public void generateConstraints(ArrayList<EJBClass> classes, Boolean swing) {
		cc = new NamingUtil();
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy  H:mm:ss");
		String d = formatter.format(now);
		KrokiMockupToolApp.getInstance().displayTextOutput("[CONSTRAINT GENERATOR] generating Constraint classes...", 0);
		File f = new File(".");
		String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
		if(!KrokiMockupToolApp.getInstance().isBinaryRun()) {
			appPath = appPath.substring(0, appPath.length()-16);
		}
		
		File dir = new File(appPath +  "SwingApp" + File.separator + "src" + File.separator + "ejb");
		if(!swing) {
			dir = new File(appPath +  "WebApp" + File.separator + "src_gen" + File.separator + "ejb_generated");
		}
		//deleteFiles(dir);

		for(int i=0; i<classes.size(); i++) {
			EJBClass cl = classes.get(i);
			Configuration cfg = new Configuration();
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			FileTemplateLoader templateLoader;
			Template tpl = null;
			try {
				templateLoader = new FileTemplateLoader(new File(appPath + "KrokiMockupTool/src/kroki/app/generators/templates"));
				cfg.setTemplateLoader(templateLoader);
				tpl = cfg.getTemplate("constraint.ftl");
			}catch (IOException ioe) {
				//				JOptionPane.showMessageDialog(null, "EJB GENERATOR: IOException");
				//e.printStackTrace();
				System.out.println("[CONSTRAINT GENERATOR] " + ioe.getMessage());
				System.out.println("[CONSTRAINT GENERATOR] Templates directory not found. Trying the alternative one...");
				try {
					templateLoader = new FileTemplateLoader(new File(appPath + "templates"));
					cfg.setTemplateLoader(templateLoader);
					tpl = cfg.getTemplate("constraint.ftl");
					System.out.println("[CONSTRAINT GENERATOR] Templates loaded ok.");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			File fout = new File(appPath +  "SwingApp" + File.separator + "src" + File.separator + "ejb" + File.separator + cl.getName() + "Constraints.java");
			//ako je swing false onda se generisu ejb klase u web projekat
			if(!swing) {
				fout = new File(appPath +  "WebApp" +  File.separator + "src_gen" + File.separator + "ejb_generated" + File.separator + cl.getName() + "Constraints.java");
			}

			//JOptionPane.showMessageDialog(null, "EJB GENERATOR: generisem u " + fout.getAbsolutePath());

			try {
				if (!fout.getParentFile().exists()) 
					if (!fout.getParentFile().mkdirs()) {
						throw new IOException("Greska pri kreiranju izlaznog direktorijuma ");
					}
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fout));

				Map model = new TreeMap();
				String doc = "   /** " +
						"\n   Class generated using Kroki ConstraintGenerator " +
						"\n   @Author KROKI Team " +
						"\n   Creation date: " + d + "h" +
						"\n   **/"; 

			    model.put("class", cl);
				model.put("doc", doc);
				
				tpl.process(model, writer);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				KrokiMockupToolApp.getInstance().displayTextOutput(e.getMessage(), 3);
			} catch (IOException e) {
				e.printStackTrace();
				KrokiMockupToolApp.getInstance().displayTextOutput(e.getMessage(), 3);
			} catch (TemplateException e) {
				e.printStackTrace();
				KrokiMockupToolApp.getInstance().displayTextOutput(e.getMessage(), 3);
			}
		}
		KrokiMockupToolApp.getInstance().displayTextOutput(classes.size() + " cCnstraint classes successfully generated.", 0);
	}

	public boolean deleteFiles(File directory) {
		boolean success = false;

		if (!directory.exists()) {
			return false;
		}
		if (!directory.canWrite()) {
			return false;
		}

		File[] files = directory.listFiles();
		for(int i=0; i<files.length; i++) {
			File file = files[i];
			if(file.isDirectory()) {
				deleteFiles(file);
			}
			try {
				FileDeleteStrategy.FORCE.delete(file);
				success =  !file.delete();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return success;
	}		
	
}