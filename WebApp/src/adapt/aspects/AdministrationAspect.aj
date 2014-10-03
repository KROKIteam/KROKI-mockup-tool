package adapt.aspects;

import java.util.ArrayList;

import adapt.core.AppCache;
import adapt.model.menu.AdaptMenu;
import adapt.resources.HomeResource;

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
		// 		User user = SessionAspect.getCurrentUser();
		// 2. Filter menu list according to current user
		
		// Right now, just add menus from application model to freemarker data model
		ArrayList<AdaptMenu> menus = AppCache.getInstance().getMenuList();
		homeResource.addToDataModel("main_menu", menus);
	}
	
}
