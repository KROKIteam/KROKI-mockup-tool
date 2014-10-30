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
		// TODO:
		// 1. Get the current user from session acpect:
		User user = SessionAspect.getCurrentUser();
		// 2. Filter menu list according to current user
		
		if (user == null)
			return;
		
		ArrayList<AdaptMenu> menus = AppCache.getInstance().getMenuList();
		
		//Podaci iz baze		
		EntityManager em = PersisenceHelper.createEntityManager();

		List<UserRoles> roles = em.createQuery("SELECT r FROM UserRoles r").getResultList();
		AppCache.displayTextOnMainFrame(roles.size()+ " ", 0);
		for(UserRoles ur : roles) {
			AppCache.displayTextOnMainFrame(ur.getUser().getUsername() + ", " + ur.getRole().getName(), 0);
		}
		if (roles == null || roles.size() == 0) {
			//NE POSTOJE ROLE ODRADI DEFAULT OPERACIJE
			//DEFAULTNI MENI
			homeResource.addToDataModel("menu", (AdaptSubMenu)AppCache.getInstance().getDefaultMenu());
		} else {
			AdaptSubMenu rootMenu = null;
			//POSTOJE ROLE
			Role userRole = (Role)em.createQuery("SELECT ur.role FROM UserRoles ur WHERE ur.user.id =:uid").setParameter("uid", user.getId()).getSingleResult();
			for (AdaptMenu menu : menus) {
				AdaptSubMenu subMenu = (AdaptSubMenu)menu;
				for (String s : subMenu.getRoles()) {
					if (s.equals(userRole.getName())) {
						rootMenu = subMenu;
					}
				}
			}
			homeResource.addToDataModel("menu", rootMenu);
		}
		em.close();	
	}
	
}
