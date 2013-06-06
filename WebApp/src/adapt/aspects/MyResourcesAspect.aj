package adapt.aspects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import adapt.application.AdaptApplication;
import adapt.entities.MyResource;
import adapt.entities.User;
import adapt.resources.ViewResource;
import adapt.utils.XMLResource;

public aspect MyResourcesAspect {
	
	public pointcut addMyResource(ViewResource vr, Long id) :
		call (public * ViewResource.modify(..)) && 
		this(vr) &&
		args(ArrayList<Object>, id);
	
	after(ViewResource vr, Long id) returning(Object obj) : addMyResource(vr, id) {
		if(id == null) {
			AdaptApplication app = (AdaptApplication) vr.getApplication();
			XMLResource resource = vr.getResource();
			EntityManager em = app.getEmf().createEntityManager();
			EntityTransaction tx = em.getTransaction();
			User user = SessionAspect.getCurrentUser();
			
			MyResource myres = new MyResource();
			myres.setTable(resource.getName());
			myres.setTableLabel(resource.getLabel());
			myres.setResLink(resource.getLink());
			myres.setUser(user);
			
			Class oClass = obj.getClass();
			Method getName;
			Method getId;
			try {
				getName = oClass.getMethod("getName");
				getName.setAccessible(true);
				String name = (String) getName.invoke(obj);
				
				getId = oClass.getMethod("getId");
				getId.setAccessible(true);
				Long entid = (Long) getId.invoke(obj);
				
				myres.setEntityId(entid);
				myres.setEntityLabel(name);
				
				tx.begin();
				em.persist(myres);
				tx.commit();
				SessionAspect.getMyResources().add(0, myres);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				try {
					getId = oClass.getMethod("getId");
					getId = oClass.getMethod("getId");
					getId.setAccessible(true);
					Long entid = (Long) getId.invoke(obj);
					
					myres.setEntityId(entid);
					myres.setEntityLabel(entid.toString());
					
					tx.begin();
					em.persist(myres);
					tx.commit();
					SessionAspect.getMyResources().add(0, myres);
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e2) {
					e.printStackTrace();
				} catch (IllegalAccessException e2) {
					e.printStackTrace();
				} catch (InvocationTargetException e2) {
					e.printStackTrace();
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			em.close();
		}
	}
}
