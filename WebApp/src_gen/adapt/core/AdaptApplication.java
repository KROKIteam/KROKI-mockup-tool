package adapt.core;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.restlet.Application;
import org.restlet.Directory;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.LocalReference;

import adapt.resources.AddResource;
import adapt.resources.DeleteResource;
import adapt.resources.GetZoomsResource;
import adapt.resources.HomeResource;
import adapt.resources.IndexResource;
import adapt.resources.ModifyResource;
import adapt.resources.ParentChildInfoResource;
import adapt.resources.PrintResource;
import adapt.resources.ViewResource;
import adapt.util.repository_utils.RepositoryPathsUtil;
import adapt.util.staticnames.Settings;
import freemarker.template.Configuration;

/**
 * Main Restlet application class for KROKI Generic WEB Application
 * @author Milorad Filipovic
 *
 */
public class AdaptApplication extends Application {

	private static AdaptApplication instance = null;

	String theme = Settings.WEB_THEME;
	String repositoryPath = RepositoryPathsUtil.getRepositoryRootPath();
	Configuration fmc;
	AdaptMainFrame mainFrame;
	boolean alteredPaths = false;

	SimpleDateFormat formatter = new SimpleDateFormat(Settings.FULL_DATE_TIME_FORMAT);
	Date today = new Date();

	/**
	 * Singleton constructor
	 * @param mainFrame reference to application main framed
	 */
	public static AdaptApplication getInstance() {
		if(instance != null) {
			return instance;
		}else {
			instance = new AdaptApplication();
			AppCache.setApplication(instance);
			return instance;
		}
	}

	private AdaptApplication() {
		super();

		//Set up freemarker configuration
		// /static/gui/web/templates/pages
		// /static/gui/web/templates/component-templates
		fmc = new Configuration();
		try {
			// Running as exporter runnable jar file
			fmc.setDirectoryForTemplateLoading(
					new File(repositoryPath + File.separator + 
							"static" + File.separator + 
							"gui" + File.separator + 
							"web" + File.separator + 
							"templates"));
		} catch (IOException e) {
			System.out.println("[ADAPT APPLICATION] Cannot find default templates path, trying the optional one...");
			alteredPaths = true;
			try {
				// Running as na eclipse project
				String toRemove = File.separator + "WebApp" + File.separator;
				fmc.setDirectoryForTemplateLoading(
						new File(repositoryPath.replace(toRemove, File.separator) + File.separator + 
								"static" + File.separator + 
								"gui" + File.separator + 
								"web" + File.separator + 
								"templates"));
				System.out.println("[ADAPT APPLICATION] Freemarker templates path configured successfully.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public Restlet createRoot() {
		Router router = new Router(getContext());

		File templatesFile =  new File(repositoryPath + File.separator + 
				"static" + File.separator + 
				"gui" + File.separator + 
				"web" + File.separator + 
				"themes" +File.separator + 
				theme);
		
		if(alteredPaths) {
			String toRemove = File.separator + "WebApp" + File.separator;
			templatesFile =  new File(repositoryPath.replace(toRemove, File.separator) + File.separator + 
					"static" + File.separator + 
					"gui" + File.separator + 
					"web" + File.separator + 
					"themes" +File.separator + 
					theme);
		}
		
		LocalReference filesRef = LocalReference.createFileReference(templatesFile);
		LocalReference staticRef = LocalReference.createFileReference(repositoryPath + File.separator + "static_files");
		Directory themeDirectory = new Directory(getContext(), filesRef);
		Directory staticFilesDir = new Directory(getContext(), staticRef);

		router.attach("/files", themeDirectory);
		router.attach("/static", staticFilesDir);
		router.attach("/", IndexResource.class);
		router.attach("/homepage", HomeResource.class);
		router.attach("/show/{activate}", ViewResource.class);
		router.attach("/showChildren/{childPanelName}/{associationEnd}/{pid}", ViewResource.class);
		router.attach("/add/{entityName}", AddResource.class);
		router.attach("/delete/{panelName}/{delid}", DeleteResource.class);
		router.attach("/edit/{panelName}/{mid}/{pid}", ModifyResource.class); // ModifyResource just prepares edit form
		router.attach("/edited/{entityName}/{modid}", AddResource.class); // AddResource does the actual modifications
		router.attach("/getInfo/{pcPanel}", ParentChildInfoResource.class);
		router.attach("/getZooms/{panelName}/{zoomName}/{zid}", GetZoomsResource.class);
		router.attach("/printForm", PrintResource.class);
		
		return router;
	}

	public AdaptMainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(AdaptMainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public Configuration getFmc() {
		return fmc;
	}

	public void setFmc(Configuration fmc) {
		this.fmc = fmc;
	}
}
