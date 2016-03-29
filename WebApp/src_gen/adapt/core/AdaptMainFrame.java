package adapt.core;

import java.util.List;

import javax.persistence.EntityManager;

import org.restlet.Component;
import org.restlet.data.Protocol;

import adapt.util.ejb.PersisenceHelper;
import adapt.util.ejb.SchemaGenerator;
import adapt.util.staticnames.Settings;
import adapt.util.xml_readers.AdministrationSubsystemReader;
import adapt.util.xml_readers.EntityReader;
import adapt.util.xml_readers.EnumerationReader;
import adapt.util.xml_readers.MenuReader;
import adapt.util.xml_readers.PanelReader;
import adapt.util.xml_readers.TypeComponenMappingReader;
import ejb.AdaptUser;


public class AdaptMainFrame {

	public static String DEPLOY_SERVER = "server";
	public static String DEPLOY_DEVELOP = "develop";
	Component component;
	AdaptMainFrameDevelop amfd = null;
	public AdaptMainFrame() {
		component = new Component();
		if (Settings.DEPLOY.equals(DEPLOY_DEVELOP)) {
			amfd = new AdaptMainFrameDevelop(component);
			amfd.setVisible(true);
		}
		try {
			runApp();
			displayText("Server running...", 0);
			if(amfd != null) {
				amfd.navigate();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			displayStackTrace(e1);
		}
	}

	public void runApp() throws Exception {
		component.getServers().add(Protocol.HTTP, 8182);
		component.getClients().add(Protocol.FILE);
		component.getClients().add(Protocol.HTTP);
		AdaptApplication app = AdaptApplication.getInstance();
		app.setMainFrame(AdaptMainFrame.this);
		PersisenceHelper.createFactory("adapt");

		// Read XML specifications
		loadMappings();
		// Export ejb classes to database tables
		try {
			SchemaGenerator schemaGen = new SchemaGenerator("ejb");
			schemaGen.generate();
			EntityManager em = PersisenceHelper.createEntityManager();

			loadAdministrationSubsytem(em);

			List<AdaptUser> users = em.createQuery("SELECT u FROM AdaptUser u").getResultList();
			if (users == null || users.isEmpty()) {
				//If no users defined
				//persist test user
				AdaptUser u = new AdaptUser();
				u.setUsername("admin");
				u.setPassword("12345");

				em.getTransaction().begin();
				em.persist(u);
				em.getTransaction().commit();
			}
			em.close();
		}catch(Exception e) {
			displayStackTrace(e);
			e.printStackTrace();
		}

		component.getDefaultHost().attach(app);
		component.start();
		displayText("Starting internal server on port 8182", 0);
	}

	/**
	 * Invokes XML readers' parsing functions
	 */
	private void loadMappings() {
		EntityReader.loadMappings();
		PanelReader.loadMappings();
		TypeComponenMappingReader.mapTypesToComponents();;
		MenuReader.load();
		EnumerationReader.loadEnumerations();
	}

	private void loadAdministrationSubsytem(EntityManager entityManager) {
		AdministrationSubsystemReader.load(entityManager);
	}

	/**
	 * Displays text for logging purposes.
	 * @param text Text to be shown in frame
	 * @param type Message type: 0 - info, 1 - error, 2 - warning
	 */
	public void displayText(String text, int type) {
		if (Settings.DEPLOY.equals(DEPLOY_SERVER)) {
			System.out.println(text);
			return;
		} else if (Settings.DEPLOY.equals(DEPLOY_DEVELOP)){
			amfd.displayText(text, type);
		}
	}

	/**
	 * Displays exception stack trace as error message
	 * @param e
	 */
	public void displayStackTrace(Exception e) {
		if (Settings.DEPLOY.equals(DEPLOY_SERVER)) {
			e.printStackTrace();
			return;
		} else if (Settings.DEPLOY.equals(DEPLOY_DEVELOP)){
			amfd.displayStackTrace(e);
		}
	}
}