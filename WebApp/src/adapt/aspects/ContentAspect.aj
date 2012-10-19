package adapt.aspects;

import java.util.ArrayList;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import adapt.application.AdaptApplication;
import adapt.entities.Action;
import adapt.entities.MyResource;
import adapt.entities.Resource;
import adapt.entities.User;
import adapt.entities.UserRights;
import adapt.resources.ViewResource;
import adapt.utils.XMLResource;


public aspect ContentAspect {
	
	public pointcut prepareContent(ViewResource vr) : 
		call (public void ViewResource.prepareContent(..)) && 
		this(vr);
	
	public pointcut prepareMenu(Map<String, Object> model, EntityManager e) : 
		call (public void *.prepareContent(..)) && 
		args(model, e);
	
	public pointcut prepareChildernControls(ViewResource vr, String cresName) :
		call(public void ViewResource.prepareChildern(..)) &&
		args(AdaptApplication, String, String, cresName) &&
		this(vr);
	
	public pointcut prepareMTMChildernControls(ViewResource vr, String child) :
		call(public void ViewResource.prepareMTMChildern(..)) &&
		args(AdaptApplication, String, String, child) &&
		this(vr);
	
	@SuppressWarnings("unchecked")
	after (ViewResource vr) : prepareContent(vr){
		User curr = SessionAspect.getCurrentUser();
		XMLResource resource = vr.getResource();
		if(curr != null && resource != null) {
			Map<String, Object> dataModel = vr.getDataModel();
			AdaptApplication application = (AdaptApplication) vr.getApplication();
			EntityManagerFactory emf = application.getEmf();
			EntityManager em = emf.createEntityManager();
			prepareMenu(dataModel, em, curr);
			prepareControls(dataModel, em, curr, resource.getLabel(), application);
			prepareChildern(vr, curr);
		}
	}
	
	@SuppressWarnings("unchecked")
	before (Map<String, Object> model, EntityManager e) : prepareMenu(model, e){
		User curr = SessionAspect.getCurrentUser();
		if(curr != null && e != null) {
			prepareMenu(model, e, curr);
		}
	}

	after(ViewResource vr, String child) : prepareMTMChildernControls(vr, child) {
		ArrayList<Action> controls = new ArrayList<Action>();
		ArrayList<Action> actions = new ArrayList<Action>();
		User curr = SessionAspect.getCurrentUser();
		
		AdaptApplication app = (AdaptApplication) vr.getApplication();
		XMLResource childRes = app.getXMLResource(child);
		EntityManager e = app.getEmf().createEntityManager();
		EntityTransaction tx = e.getTransaction();
		tx.begin();
		
		//pokupim prava koja korisnik ima na trazeni resurs
		Query q = e.createQuery("FROM UserRights ur WHERE ur.user.id =:uid  AND ur.resource.name =:res AND ur.allowed = TRUE");
		q.setParameter("uid", curr.getId());
		q.setParameter("res", childRes.getLabel());
		ArrayList<UserRights> r = (ArrayList<UserRights>) q.getResultList();
		for (int i = 0; i < r.size(); i++) {
			UserRights ur = r.get(i);
			//za svako dozvoljeno pravo generise se akcija
			Action action = app.getAction(ur.getAction().getName());
			//akcija se smesta u odgovarajucu listu u zavisnosti od tipa
			if(action.getType().equals("control")) {
				if(action.getName().equals("add")) {
					Action act = app.getAction("mtmadd");
					controls.add(act);
				}
			}else if (action.getType().equals("action")) {
				if(action.getName().equals("remove")) {
					Action act = app.getAction("mtmremove");
					actions.add(act);
				}
			}
		}
		
		tx.commit();
		e.close();
		
		vr.getDataModel().put("childControls", controls);
		vr.getDataModel().put("childActions", actions);
	}
	
	//priprema kontrola za child tabelu
	after(ViewResource vr, String cresName) : prepareChildernControls(vr, cresName) {
		ArrayList<Action> controls = new ArrayList<Action>();
		ArrayList<Action> actions = new ArrayList<Action>();
		User curr = SessionAspect.getCurrentUser();
		
		AdaptApplication app = (AdaptApplication) vr.getApplication();
		XMLResource childRes = app.getXMLResource(cresName);
		EntityManager e = app.getEmf().createEntityManager();
		EntityTransaction tx = e.getTransaction();
		tx.begin();
		
		//pokupim prava koja korisnik ima na trazeni resurs
		Query q = e.createQuery("FROM UserRights ur WHERE ur.user.id =:uid  AND ur.resource.name =:res AND ur.allowed = TRUE");
		q.setParameter("uid", curr.getId());
		q.setParameter("res", childRes.getLabel());
		ArrayList<UserRights> r = (ArrayList<UserRights>) q.getResultList();
		for (int i = 0; i < r.size(); i++) {
			UserRights ur = r.get(i);
			//za svako dozvoljeno pravo generise se akcija
			Action action = app.getAction(ur.getAction().getName());
			//akcija se smesta u odgovarajucu listu u zavisnosti od tipa
			if(action.getType().equals("control")) {
				controls.add(action);
			}else if (action.getType().equals("action")) {
				actions.add(action);
			}
		}
		
		tx.commit();
		e.close();
		
		vr.getDataModel().put("childControls", controls);
		vr.getDataModel().put("childActions", actions);
	}
	
//------------------------------------------METODE-------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public void prepareMenu(Map<String, Object> model, EntityManager e, User c) {
		ArrayList<Resource> resources = new ArrayList<Resource>();
		EntityTransaction tx = e.getTransaction();
		tx.begin();
		
		//pokupim sva prava u kojima  je trenutnom korisniku pravo view dozvoljeno
		ArrayList<UserRights> rights = (ArrayList<UserRights>) e.createQuery
		("FROM UserRights ur WHERE ur.user.id =:uid AND ur.action = 1 AND ur.allowed = 1")
		.setParameter("uid", c.getId()).getResultList();
		
		//sad odatle pokupim sve resurse i stavim u listu koja ide u meni
		for(int i=0; i<rights.size(); i++) {
			Resource r = (Resource)e.createQuery
			("FROM Resource r WHERE r.id =:rid").
			setParameter("rid", rights.get(i).getResource().getId()).
			getSingleResult();
			resources.add(r);
		}
		
		tx.commit();
		
		ArrayList<MyResource> myResources = (ArrayList<MyResource>) SessionAspect.getMyResources();
		
		if(myResources.size() > 5) {
			ArrayList<MyResource> latest = new ArrayList<MyResource>();
			for(int i=0; i<5; i++) {
				latest.add(myResources.get(i));
			}
			model.put("myResources", latest);
		}else {
			model.put("myResources", myResources);
		}
		model.put("allMyResources", myResources);
		model.put("menu", resources);
		model.put("user", c);
	}
	
	
	@SuppressWarnings("unchecked")
	public void prepareControls(Map<String, Object> model, EntityManager e,User c, String rname, AdaptApplication app) {
		ArrayList<Action> controls = new ArrayList<Action>();
		ArrayList<Action> actions = new ArrayList<Action>();
		
		EntityTransaction tx = e.getTransaction();
		tx.begin();
		
		//pokupim prava koja korisnik ima na trazeni resurs
		Query q = e.createQuery("FROM UserRights ur WHERE ur.user.id =:uid  AND ur.resource.name =:res AND ur.allowed = TRUE");
		q.setParameter("uid", c.getId());
		q.setParameter("res", rname);
		ArrayList<UserRights> r = (ArrayList<UserRights>) q.getResultList();
		for (int i = 0; i < r.size(); i++) {
			UserRights ur = r.get(i);
			//za svako dozvoljeno pravo generise se akcija
			Action action = app.getAction(ur.getAction().getName());
			//akcija se smesta u odgovarajucu listu u zavisnosti od tipa
			if(action.getType().equals("control")) {
				controls.add(action);
			}else if (action.getType().equals("action")) {
				actions.add(action);
			}
		}
		
		tx.commit();
		e.close();
		
		model.put("resourceControls", controls);
		model.put("resourceActions", actions);
	}
	
	public void prepareChildern(ViewResource resource, User user) {
		AdaptApplication app = (AdaptApplication) resource.getApplication();
		ArrayList<XMLResource> childern = app.getResourceChildern(resource.getResource());
		ArrayList<XMLResource> allowedChildern = new ArrayList<XMLResource>();
		ArrayList<XMLResource> MTMChildern = app.getManyToManyChildern(resource.getResource());
		ArrayList<XMLResource> allowedMTMChildern = new ArrayList<XMLResource>();
		
		EntityManager em = app.getEmf().createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		//child lista za many-to-one formu
		for(int i=0; i<childern.size(); i++) {
			XMLResource ress = childern.get(i);
			Query q = em.createQuery("FROM UserRights ur WHERE ur.user.id =:uid AND ur.action.id = 1 AND ur.allowed = 1 AND ur.resource.link =:rname");
			q.setParameter("uid", user.getId());
			q.setParameter("rname", ress.getLink());
			UserRights uu = null;
			try {
				uu = (UserRights) q.getSingleResult();
				allowedChildern.add(ress);
			} catch (javax.persistence.NoResultException e1) {
				System.out.println("neam prava");
			}
		}
		//child lista za many-to-many formu
		for(int j=0; j<MTMChildern.size(); j++) {
			XMLResource re = MTMChildern.get(j);
			Query q = em.createQuery("FROM UserRights ur WHERE ur.user.id =:uid AND ur.action.id = 1 AND ur.allowed = 1 AND ur.resource.link =:rname");
			q.setParameter("uid", user.getId());
			q.setParameter("rname", re.getLink());
			UserRights uu = null;
			try {
				uu = (UserRights) q.getSingleResult();
				allowedChildern.add(re);
				allowedMTMChildern.add(re);
			} catch (javax.persistence.NoResultException e1) {
				System.out.println("neam prava");
			}
		}
		tx.commit();
		em.close();
		resource.getDataModel().put("childList", allowedChildern);
		resource.getDataModel().put("MTMChildList", allowedMTMChildern);
	}
}
