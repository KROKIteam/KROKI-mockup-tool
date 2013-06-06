package adapt.resources;

import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import adapt.application.AdaptApplication;
import adapt.entities.User;
import adapt.utils.Settings;

public class HomeResource extends BaseResource {
	
	public HomeResource(Context context, Request request, Response response) {
		super(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_HTML));
	}
	
	@Override
	public void handleGet() {
		AdaptApplication app = (AdaptApplication) getApplication();
		EntityManager em = app.getEmf().createEntityManager();
		Form form = getRequest().getEntityAsForm();
		String username = (String)form.getFirstValue("username");
		String password = (String)form.getFirstValue("password");
		String usernameIzmena = form.getFirstValue("usernameIzmena");
		String passwordIzmena = form.getFirstValue("passwordIzmena");
		String idIzmena = form.getFirstValue("idIzmena");
		
		if(username!=null && password!=null) {//login
			findUser(username, password, em);
		}
		
		if(usernameIzmena != null && passwordIzmena != null) {//izmena
			try {
				Long id = Long.parseLong(idIzmena);
				modify(id, usernameIzmena, passwordIzmena, em);
			} catch (Exception e) {
				System.out.println("[HomeResource] id za izmenu nije long!");
			}
		}
		
		super.handleGet();
	}

	public User findUser(String uname, String pass, EntityManager em) {
		User u = null;
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			u = (User) em.createQuery("from User u where u.username =:usrname").setParameter("usrname", uname).getSingleResult();
			if(u != null) {
				AdaptApplication app = (AdaptApplication) getApplication();
				app.getMainFrame().displayText("User " + u.getUsername() + " logged in from " + getRequest().getClientInfo().getAddress(), 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();
		//em.close();
		
		return u;
	}
	
	public void modify(Long id, String username, String password, EntityManager e) {
		EntityTransaction tx = e.getTransaction();
		tx.begin();
		try {
			User u = (User) e.createQuery("FROM User u WHERE u.id =:uid").setParameter("uid", id).getSingleResult();
			tx.commit();
			u.setUsername(username);
			u.setPassword(password);
			e.flush();
		} catch (javax.persistence.NoResultException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
    public Representation represent(Variant variant) throws ResourceException {
        final Map<String, Object> dataModel = new TreeMap<String, Object>();
        AdaptApplication app = (AdaptApplication) getApplication();
		EntityManager em = app.getEmf().createEntityManager();
        prepareContent(dataModel, em);
        dataModel.put("title", Settings.APP_TITLE);
        return getHTMLTemplateRepresentation("homepage.html", dataModel);
    }
    
	@Override
	public void prepareContent(Map<String, Object> model, EntityManager em) {
		super.prepareContent(model, em);
	}
	
    @Override
	public void handlePost() {
		handleGet();
	}
}
