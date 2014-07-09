package com.panelcomposer.business.report.jasper;

import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import com.panelcomposer.business.report.AbstractReportView;
import com.panelcomposer.exceptions.ReportException;

/***
 * JasperReportView is class that extends AbstractReportView and implements it's
 * method execute for calling Jasper Reports
 * 
 * @author Darko
 * 
 */
public class JasperReportView extends AbstractReportView {

	/***
	 * JasperReportView constructor
	 * @param parameterMap
	 * @param location
	 */
	public JasperReportView(Map<String, Object> parameterMap, String location) {
		super(parameterMap, location);
	}

	@Override
	public void execute() throws ReportException {
		try {
			JasperReportUtil.viewReport(parameterMap, location);
		} catch (JRException e) {
			throw new ReportException("");
		}
	}

}
