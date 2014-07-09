package com.panelcomposer.core;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import util.PersistenceHelper;
import util.SchemaGenerator;
import util.xml_readers.EntityReader;
import util.xml_readers.EnumerationReader;
import util.xml_readers.PanelReader;
import util.xml_readers.TypeComponentReader;

import com.panelcomposer.elements.SMainForm;

import ejb.User;

public class MainApp {

	public static void main(String[] args) {
		PersistenceHelper.createFactory("hotelsoap");

		TypeComponentReader.loadMappings();
		EnumerationReader.loadMappings();
		//TypeMapping.loadMappings();
		EntityReader.loadMappings();
		PanelReader.loadMappings();

		//SCHEMA EXPORT
		//AnnotationConfigurationWithWildcard cfg = new AnnotationConfigurationWithWildcard();
		//cfg.configure();
		//new SchemaExport(cfg).create(true, true);
		try {
			SchemaGenerator gen = new SchemaGenerator("ejb");
			gen.generate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		User u = new User();
		u.setUsername("admin");
		u.setPassword("admin");

		EntityManager em = PersistenceHelper.createEntityManager();
		em.getTransaction().begin();
		em.persist(u);
		em.getTransaction().commit();

		SMainForm sm = new SMainForm();
		sm.setVisible(true);
	}
}
