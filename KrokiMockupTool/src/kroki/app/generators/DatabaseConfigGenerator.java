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
		geneateHibernateConfigXML();
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
		if(web){
			fout = new File(appPath.substring(0, appPath.length()-16) +  "WebApp" + File.separator + "etc" + File.separator + "META-INF" + File.separator + "persistence.xml");
			tpl = prepareTemplate("persistenceWeb.ftl");
		}
//		JOptionPane.showMessageDialog(null, "DB CONFIG GENERATOR: generisem u " + fout.getAbsolutePath());
		
		OutputStreamWriter writer;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(fout));
			Map model = new TreeMap();
			
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
		} catch (FileNotFoundException e) {
//			JOptionPane.showMessageDialog(null, "DB CONFIG GENERATOR: FileNotFoundException");
			e.printStackTrace();
		} catch (TemplateException e) {
//			JOptionPane.showMessageDialog(null, "DB CONFIG GENERATOR: TemplateException");
			e.printStackTrace();
		} catch (IOException e) {
//			JOptionPane.showMessageDialog(null, "DB CONFIG GENERATOR: IOException");
			e.printStackTrace();
		}
	}
	
	  /***********************************************/
	 /*       hibernate.cfg.xml generation          */
	/***********************************************/
	public void geneateHibernateConfigXML() {
		System.out.println("[" + date + "]" + " generating hibernate.cfg.xml file...");
		File f = new File(".");
		appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
		try {
			File fout = new File(appPath.substring(0, appPath.length()-16) + "SwingApp" + File.separator + "src"  + File.separator + "hibernate.cfg.xml");
			Template tpl = prepareTemplate("hibernate.cfg.ftl");
			
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fout));
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
			
			tpl.process(model, writer);
		}catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (TemplateException te) {
			te.printStackTrace();
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
			e.printStackTrace();
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
