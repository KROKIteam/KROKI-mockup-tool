package adapt.aspects;

import java.util.ArrayList;
import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import adapt.entities.MyResource;
import adapt.entities.User;
import adapt.resources.HomeResource;
import adapt.resources.IndexResource;

public aspect SessionAspect { 
	
	private static User currentUser;
	private static ArrayList<MyResource> myResources = new ArrayList<MyResource>();
	
	public pointcut login(EntityManager e) :
					call (* HomeResource.findUser(..)) &&
					args(String, String, e);
	public pointcut logout() :
					set(* IndexResource.dataModel);
	public pointcut modifyUser(String username, String pass) :
					call (* HomeResource.modify(..)) &&
					args(Long, username, pass, EntityManager);
	
	
	public static  User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		SessionAspect.currentUser = currentUser;
	}
	
	public static ArrayList<MyResource> getMyResources() {
		return myResources;
	}
	public static void setMyResources(ArrayList<MyResource> myResources) {
		SessionAspect.myResources = myResources;
	}
	
	@SuppressWarnings("unchecked")
	after(EntityManager e) returning(User u) : login(e) {
		setCurrentUser(u);
		if(u != null) {
			EntityTransaction tx = e.getTransaction();
			tx.begin();
			try {
				 
				ArrayList<MyResource> myres = (ArrayList<MyResource>) e.createQuery("FROM MyResource mr WHERE mr.user.id =:uid").setParameter("uid", u.getId()).getResultList();
				Collections.reverse(myres);
				myResources.addAll(myres);
				
			} catch (javax.persistence.NoResultException e1) {
				myResources = null;
			}
		}
	}
	
	after() : logout() {
		setCurrentUser(null);
		myResources.clear();
	}
	
	after(String username, String pass) : modifyUser(username, pass) {
		currentUser.setUsername(username);
		currentUser.setPassword(pass);
	}
}
