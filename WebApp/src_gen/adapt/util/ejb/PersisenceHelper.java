package adapt.util.ejb;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersisenceHelper {

	private static EntityManagerFactory factory;
	
	public static EntityManager createEntityManager() {
		return factory.createEntityManager();
	}
	
	public static EntityManagerFactory getEntityManagerFactory() {
		return factory;
	}
	
	public static void createFactory(String name) {
		PersisenceHelper.factory = Persistence.createEntityManagerFactory(name);
	}
}
