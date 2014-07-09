package com.panelcomposer.business.report;

import java.util.Map;

import com.panelcomposer.exceptions.ReportException;

/***
 * AbstractReportView is an abstract class that provides interface for report
 * subsystem implementations.
 * 
 * @author Darko
 * 
 */
public abstract class AbstractReportView {
	/***
	 * Map of the parameters for report
	 */
	protected Map<String, Object> parameterMap;
	/***
	 * Report file location (path)
	 */
	protected String location;

	/***
	 * AbstractReportView constructor
	 * @param parameterMap
	 * @param location
	 */
	public AbstractReportView(Map<String, Object> parameterMap, String location) {
		this.parameterMap = parameterMap;
		this.location = location;
	}

	/***
	 * Execution of the code which calls reports. Left abstract so it is
	 * implemented in concrete implementations.
	 * 
	 * @throws ReportException
	 *             If something went wrong while calling report, the method will
	 *             throw this exception.
	 */
	public abstract void execute() throws ReportException;
}
