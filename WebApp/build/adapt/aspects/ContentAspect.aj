package adapt.aspects;

import java.util.ArrayList;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import adapt.entities.usersAndRights.Resource;
import adapt.entities.usersAndRights.User;
import adapt.entities.usersAndRights.UserRights;
import adapt.utils.Control;


public aspect ContentAspect {
	
	public pointcut prepareContent(Map<String, Object> model, EntityManager e, int n) : 
		call (public void *.prepareContent(..)) && 
		args(model, e, n);

	@SuppressWarnings("unchecked")
	before (Map<String, Object> model, EntityManager e, int n) : prepareContent(model, e, n){
		User curr = SessionAspect.getCurrentUser();
		ArrayList<Resource> resources = new ArrayList<Resource>();
		ArrayList<Control> controls = new ArrayList<Control>();
		ArrayList<Control> modifies = new ArrayList<Control>();
		
		if(curr != null) {
			EntityTransaction tx = e.getTransaction();
			tx.begin();
			
			//---------------PRIPREMANJE MENIJA-----------------------
			//pokupim sva prava u kojima  je trenutnom korisniku pravo view doyvoljeno
			ArrayList<UserRights> rights = (ArrayList<UserRights>) e.createQuery
			("FROM UserRights ur WHERE ur.user.id =:uid AND ur.action = 1 AND ur.allowed = 1")
			.setParameter("uid", curr.getId()).getResultList();
			
			//sad odatle pokupim sve resurse i stavim u listu koja ide u meni
			for(int i=0; i<rights.size(); i++) {
				Resource r = (Resource)e.createQuery
				("FROM Resource r WHERE r.id =:rid").
				setParameter("rid", rights.get(i).getResource().getId()).
				getSingleResult();
				resources.add(r);
			}
			
			
			if(n!=1) { //ne treba za naslovnu stranu
				//-----------PRIPREMANJE KONTROLA-------------------------------------
				//pokupim prava koja korisnik ima na trazeni resurs
				Query q = e.createQuery("FROM UserRights ur WHERE ur.user.id =:uid  AND ur.resource.id =:res AND ur.allowed = 1");
				q.setParameter("uid", curr.getId());
				Integer nj = n;
				q.setParameter("res", nj.longValue());
				ArrayList<UserRights> r = (ArrayList<UserRights>) q.getResultList();
				
				for (int i = 0; i < r.size(); i++) {
					UserRights ur = r.get(i);
					//ako ima pravo da dodaje, generise se dugme iznad tabele
					if(ur.getAction().getName().equalsIgnoreCase("add")) {
						String name = ur.getAction().getName();
						String icon = "/css/add.png";
						String link = ur.getResource().getLink().substring(1) + "Dodavanje";
						Control c = new Control(name, link, icon);
						controls.add(c);
					}
				}
				
				Control up = new Control("Up",  "javascript:goUp()", "/css/up.png");
				Control down = new Control("Down", "javascript:goDown()", "/css/down.png");
				
				controls.add(up);
				controls.add(down);
				
			}
			
			
			tx.commit();
		}
		
		model.put("user", curr);
		model.put("menu", resources);
		model.put("controls", controls);
	}
}
