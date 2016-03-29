package adapt.aspects;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.restlet.data.Form;

import adapt.core.AppCache;
import adapt.resources.HomeResource;
import adapt.resources.IndexResource;
import adapt.util.ejb.PersisenceHelper;
import ejb.AdaptUser;

/**
 * Aspect that intercepts login and logout links and stores info about currently logged user
 * @author Milorad Filipovic
 */
public aspect SessionAspect {

	private static AdaptUser currentUser;
	/**
	 * Map used to store user data during login time
	 */
	private static Map<Object, Object>  session = new HashMap<Object, Object>();
	
	// Intercept the prepareContent() method and use the HomeResource instance to access it's data in advice
	public pointcut login(HomeResource homeResource) : 
		call (public void HomeResource.prepareContent()) && 
		this(homeResource);
	
	// Intercept the IndexResource(Login page) visit
	// Currently, every visit to this page is considered as log out action, since the only way
	// to get to this page using UI is via 'Log out' link
	public pointcut logout() : call (public void IndexResource.prepareContent());
	
	/**
	 * When home page is visited, check user credentials and log in
	 */
	before(HomeResource homeResource): login(homeResource) {
		if(currentUser == null) {
			Form loginForm = homeResource.getRequest().getEntityAsForm();
			EntityManager em = PersisenceHelper.createEntityManager();
			
			// Get username and password from login form
			String username = (String)loginForm.getFirstValue("korki-username");
			String password = (String)loginForm.getFirstValue("korki-password");
			
			if(username != null && password != null) {
				EntityTransaction tx = em.getTransaction();
				tx.begin();
				try {
					// Find the user with specified credentials in database
					AdaptUser u = (AdaptUser)em.createQuery("FROM AdaptUser u WHERE u.username =:uname and u.password =:pword").
							setParameter("uname", username).
							setParameter("pword", password).
							getSingleResult();
					if(u != null) {
						// Set the found user as currentUser, so all other aspects that need to administer user rights
						// can call getCurrent user and get currently logged user 
						currentUser = u;
						homeResource.addToDataModel("user", u);
						AppCache.displayTextOnMainFrame("User " + u.getUsername() + " logged in from " + homeResource.getRequest().getClientInfo().getAddress(), 0);
						System.out.println("[SESSION ASPECT] User logged in.");
					}
				} catch (Exception e) {
				}
			}
		}else {
			homeResource.addToDataModel("user", currentUser);
		}
	}
	
	/**
	 * Logout by setting currentUser to null
	 */
	before() : logout() {
		currentUser = null;
	}
	//---------------------------------------------------------------------------|| UTIL METHODS
	public static AdaptUser getCurrentUser() {
		return currentUser;
	}
	
	public static void addToSession(Object key, Object value) {
		session.put(key, value);
	}
	
	public static Object getFromSession(Object key) {
		return session.get(key);
	}
	
	public static void removeFromSession(Object key) {
		session.remove(key);
	}
}
