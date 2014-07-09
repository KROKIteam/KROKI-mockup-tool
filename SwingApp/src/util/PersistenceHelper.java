package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceHelper {

  	public static EntityManager createEntityManager() {
    	return factory.createEntityManager();
  	}
  	public static EntityManagerFactory getEntityManagerFactory() {
    	return factory;
  	}
  	
  	public static void createFactory(String name) {
		PersistenceHelper.factory = Persistence.createEntityManagerFactory(name);  
	}
  	
	private static EntityManagerFactory factory;
	
}
