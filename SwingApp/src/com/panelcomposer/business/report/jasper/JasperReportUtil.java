package com.panelcomposer.business.report.jasper;

import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import org.hibernate.ejb.HibernateEntityManager;

import util.PersistenceHelper;

public class JasperReportUtil {

	/**
	 * Prikazuje izvestaj.
	 * 
	 * @param parameters
	 *            Parametri za jasper report
	 * @param location
	 *            Relativna putanja .jasper fajla u projektu
	 */

	@SuppressWarnings("deprecation")
	public static void viewReport(Map<String, Object> parameters,
			String location) throws JRException {

		JasperPrint jasperPrint = null;	
		
		jasperPrint = JasperFillManager.fillReport(location, parameters,
				((HibernateEntityManager) PersistenceHelper.createEntityManager())
					.getSession().connection());
		Iterator<Object> it = parameters.values().iterator();
		System.out.println("JasperReportUtil.viewReport()");
		while (it.hasNext()) {
			Object obj = it.next();
		}
		 
		
		
		JasperViewer.viewReport(jasperPrint);

	}
}
