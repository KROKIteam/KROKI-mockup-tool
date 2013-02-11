package com.panelcomposer.core;


import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.tool.hbm2ddl.SchemaExport;

import util.AnnotationConfigurationWithWildcard;
import util.PersistenceHelper;
import util.SchemaGenerator;
import util.xml_readers.EntityReader;
import util.xml_readers.EnumerationReader;
import util.xml_readers.PanelReader;
import util.xml_readers.TypeComponentReader;

import com.mysql.jdbc.ResultSet;
import com.panelcomposer.elements.SMainForm;

import ejb.User;

public class MainApp {

	public static void main(String[] args) {
		//Logger.getLogger("").setLevel(Level.OFF);

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
		u.setUsername("mrd");
		u.setPassword("mrd");

		EntityManager em = PersistenceHelper.createEntityManager();
		em.getTransaction().begin();
		em.persist(u);
		em.getTransaction().commit();

		SMainForm sm = new SMainForm();
		sm.setVisible(true);
	}
}
