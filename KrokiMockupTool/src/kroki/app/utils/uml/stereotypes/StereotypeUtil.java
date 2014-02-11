package kroki.app.utils.uml.stereotypes;

import kroki.app.utils.uml.ProgressWorker;

/**
 * Contains a constant used while retrieving stereotypes from a UML profile and a method
 * to output current progress messages of the property name for which the value has been set.  
 * 
 * @author Zeljko Ivkovic (zekljo89ps@gmail.com)
 *
 */
public class StereotypeUtil {

	/**
	 * Part of the stereotype name with which to retrieve stereotypes from a UML profile where 
	 * they have been defined. 
	 */
	public static final String EUIS_DSL_PROFILE="EUISDSLProfile::";
	
	/**
	 * Shows message of the property value, for a stereotype added to a UML element, that has been set.
	 * @param thread        background worker thread, implementing the export functionality, used to output messages of
	 * the current progress of a value that has been set for a stereotype property
	 * @param propertyName  name of the stereotype property for which a value has been set
	 */
	public static void outputMessage(ProgressWorker thread,String propertyName){
		thread.publishText("Value set for "+propertyName+" property");
	}
}
