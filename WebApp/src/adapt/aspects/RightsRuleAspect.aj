package adapt.aspects;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import adapt.application.AdaptApplication;
import adapt.entities.Action;
import adapt.entities.Resource;
import adapt.entities.User;
import adapt.entities.UserRights;
import adapt.exception.RightAlreadyDefinedException;
import adapt.resources.ViewResource;

public aspect RightsRuleAspect {
	
	public pointcut checkAdd(ViewResource vr, ArrayList<Object> values) : 
		call (public * ViewResource.modify(..)) && 
		this(vr) &&
		args(values, Long);
	
	public pointcut removeModify(ViewResource vr) : 
		call (public void ViewResource.prepareContent(..)) && 
		this(vr);
	
	@SuppressWarnings("unchecked")
	after(ViewResource vr) : removeModify(vr) {
		if(vr.getResource().getName().equals("UserRights")) {
			ArrayList<Action> actions = (ArrayList<Action>) vr.getDataModel().get("resourceActions");
			if(actions != null) {
				for(int i=0; i<actions.size(); i++) {
					if(actions.get(i).getName().equals("remove")) {
						actions.remove(i);
					}
				}
			}
		}
	}
	
	before(ViewResource vr, ArrayList<Object> values) throws RightAlreadyDefinedException : checkAdd(vr, values) {
		if(vr.getResource().getName().equals("UserRights")) {
			User user = (User) values.get(1);
			Action action = (Action)values.get(2);
			Resource resource = (Resource) values.get(3);
			
			AdaptApplication app = (AdaptApplication) vr.getApplication();
			EntityManager em = app.getEmf().createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			
			UserRights ur = null;
			String q = "FROM UserRights ur WHERE ur.user.id=:uid AND ur.action.id=:aid AND ur.resource.id=:rid";
			Query query = em.createQuery(q);
			query.setParameter("uid", user.getId());
			query.setParameter("aid", action.getId());
			query.setParameter("rid", resource.getId());
			try {
				ur = (UserRights) query.getSingleResult();
			} catch (Exception e) {
			}
			if(ur != null) {
				vr.getDataModel().put("msg", "Pravo koje poku&#353;avate uneti ve&#263; postoji. Mogu&#263;e ga je jedino izmeniti!");
				throw new RightAlreadyDefinedException();
			}
			tx.commit();
			em.close();
		}
	}
}
