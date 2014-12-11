package adapt.aspects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import adapt.core.AppCache;
import adapt.model.menu.AdaptMenu;
import adapt.model.menu.AdaptSubMenu;
import adapt.resources.HomeResource;
import adapt.util.ejb.PersisenceHelper;
import ejb.Role;
import ejb.User;
import ejb.UserRoles;

/**
 * Aspect that takes care of user rights in the application
 * @author Milorad Filipovic
 */
public aspect AdministrationAspect {

	public pointcut setMenu(HomeResource homeResource) :
		call(public void HomeResource.prepareContent()) &&
		this(homeResource);

	/**
	 * When displaying home page, create the main menu according to user's view rights
	 */
	after (HomeResource homeResource) : setMenu(homeResource) {
		// 1. Get the current user from session ascpect:
		User user = SessionAspect.getCurrentUser();
		
		// 2. Filter menu list according to current user
		if (user == null)
			return;
		
		ArrayList<AdaptMenu> menus = AppCache.getInstance().getMenuList();
		
		//DB data		
		EntityManager em = PersisenceHelper.createEntityManager();

		List<UserRoles> roles = em.createQuery("SELECT r FROM UserRoles r").getResultList();
		if (roles == null || roles.size() == 0) {
			//If there are no UserRoles defined, use default menu 
			AdaptSubMenu adapt = AppCache.getInstance().getDefaultMenu();
			homeResource.addToDataModel("menu", AppCache.getInstance().getDefaultMenu());
		} else {
			AdaptSubMenu rootMenu = null;
			Role userRole = (Role)em.createQuery("SELECT ur.role FROM UserRoles ur WHERE ur.user.id =:uid").setParameter("uid", user.getId()).getSingleResult();
			for (AdaptMenu menu : menus) {
				AdaptSubMenu subMenu = (AdaptSubMenu)menu;
				for (String s : subMenu.getRoles()) {
					if (s.equals(userRole.getName())) {
						rootMenu = subMenu;
					}
				}
			}

			//If there is no user specified menu defined
			//add default
			if (rootMenu ==  null) {
				homeResource.addToDataModel("menu", AppCache.getInstance().getDefaultMenu());
			} else {
				homeResource.addToDataModel("menu", rootMenu);
			}
		}
		em.close();	
	}
	
}
