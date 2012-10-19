package adapt.aspects;

import adapt.entities.usersAndRights.User;
import adapt.resources.HomeResource;
import adapt.resources.IndexResource;

public aspect SessionAspect { 
	
	private static User currentUser;
	public pointcut login() :
					call (* HomeResource.findUser(..));
	public pointcut logout() :
					set(* IndexResource.dataModel);
	
	
	public static  User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		SessionAspect.currentUser = currentUser;
	}
	
	after() returning(User u) : login() {
		setCurrentUser(u);
	}
	
	after() : logout() {
		setCurrentUser(null);
	}
}
