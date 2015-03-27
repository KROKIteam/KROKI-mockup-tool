package kroki.app.generators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import kroki.app.KrokiMockupToolApp;
import kroki.profil.utils.DatabaseProps;

public class DatabaseConfigGenerator {
	
	private DatabaseProps  parameters;
	private String date;
	private String appPath;
	
	public DatabaseConfigGenerator(DatabaseProps props) {
		this.parameters = props;
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy  H:mm");
		date = formatter.format(now);
	}

	public void generateFilesForDesktopApp() {
		geneateHibernateConfigXML(null);
		generatePersistenceXMl(false);
	}
			
	  /***********************************************/
	 /*        persistence.xml generation           */
	/***********************************************/
	public void generatePersistenceXMl(boolean web) {
		System.out.println("[" + date + "]" + " generating persistence.xml file...");
		File f = new File(".");
		appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
		
		File fout = new File(appPath.substring(0, appPath.length()-16) +  "SwingApp" + File.separator + "props" + File.separator + "META-INF" + File.separator + "persistence.xml");
		Template tpl = prepareTemplate("persistenceSwing.ftl");
		Map model = new TreeMap();
		OutputStreamWriter writer = null;
		if(web){
			fout = new File(appPath.substring(0, appPath.length()-16) +  "WebApp" + File.separator + "etc" + File.separator + "META-INF" + File.separator + "persistence.xml");
			tpl = prepareTemplate("persistenceWeb.ftl");
		}
		try {
			writer = new OutputStreamWriter(new FileOutputStream(fout));
		} catch (FileNotFoundException e) {
			if(web) {
				fout = new File(appPath +  "WebApp" + File.separator + "etc" + File.separator + "META-INF" + File.separator + "persistence.xml");
				tpl = prepareTemplate("persistenceWeb.ftl");
			}else {
				fout = new File(appPath +  "SwingApp" + File.separator + "props" + File.separator + "META-INF" + File.separator + "persistence.xml");
				tpl = prepareTemplate("persistenceSwing.ftl");
			}
			try {
				writer = new OutputStreamWriter(new FileOutputStream(fout));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		
		try {
			String driver = parameters.getDriverClass();
			String protocol = getProtocol(parameters.getProfile());
			String url = "jdbc:" + protocol + "://" + parameters.getHost()  + ":" + parameters.getPort() + "/" + parameters.getSchema();
			//if test profile is selected, generate test url
			if(parameters.getProfile() == 5) {
				url = "jdbc:h2:mem:test";
			}
			String username = parameters.getUsername();
			String password = parameters.getPassword();
			String dialect = parameters.getDialect();
			
			model.put("doc", prepareComment());
			model.put("driver", driver);
			model.put("url", url);
			model.put("dialect", dialect);
			model.put("username", username);
			model.put("password", password);
			tpl.process(model, writer);
		} catch (TemplateException | IOException e) {
			e.printStackTrace();
		}
	}
	
	  /***********************************************/
	 /*       hibernate.cfg.xml generation          */
	/***********************************************/
	public void geneateHibernateConfigXML(String path) {
		System.out.println("[" + date + "]" + " generating hibernate.cfg.xml file...");
		File f = new File(".");
		appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
		File fout = null;
		OutputStreamWriter writer = null;
		if(path == null) {
			path = "SwingApp" + File.separator + "src"  + File.separator + "hibernate.cfg.xml";
		}
		try {
			fout = new File(appPath.substring(0, appPath.length()-16) + path);
			writer = new OutputStreamWriter(new FileOutputStream(fout));
		}catch (IOException ioe) {
			try {
				fout = new File(appPath + path);
				writer = new OutputStreamWriter(new FileOutputStream(fout));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		Template tpl = prepareTemplate("hibernate.cfg.ftl");
		Map model = new TreeMap();
		
		String protocol = getProtocol(parameters.getProfile());
		
		String url = "jdbc:" + protocol + "://" + parameters.getHost()  + ":" + parameters.getPort() + "/" + parameters.getSchema();
		if(parameters.getProfile() == 5) {
			url = "jdbc:h2:mem:test";
		}
		String username = parameters.getUsername();
		String password = parameters.getPassword();
		String driver = parameters.getDriverClass();
		String dialect = parameters.getDialect();
		
		model.put("doc", prepareComment());
		model.put("url", url);
		model.put("username", username);
		model.put("password", password);
		model.put("driver", driver);
		model.put("dialect", dialect);
		
		try {
			tpl.process(model, writer);
		} catch (TemplateException | IOException e) {
			e.printStackTrace();
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
			System.out.println("[DB CONFIG GENERATOR] Templates directory not found. Trying the alternative one...");
			KrokiMockupToolApp.getInstance().setBinaryRun(true);
			try {
				templateLoader = new FileTemplateLoader(new File(appPath + "templates"));
				cfg.setTemplateLoader(templateLoader);
				template = cfg.getTemplate(templateFile);
				System.out.println("[DB CONFIG GENERATOR] Templates loaded ok.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		return template;
	}
	
	public String prepareComment() {
		return "<!-- File generated using KROKI DatabaseConfigGenerator class -->" + 
			   "\n<!-- Creation time and date: " + date + " -->";
	}
	
	public String getProtocol(int profile) {
		switch (profile) {
		case 0:
			return "mysql";
		case 1:
			return "postgresql";
		case 2:
			return "jtds:sqlserver";
		case 3:
			return "microsoft:sqlserver";
		case 4:
			return "h2";
		default:
			return "mysql";
		}
	}
	
}
