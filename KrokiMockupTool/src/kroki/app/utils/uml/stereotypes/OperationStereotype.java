package kroki.app.utils.uml.stereotypes;

import java.util.List;

import kroki.app.utils.uml.ProgressWorker;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.BussinessOperation;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;

/**
 * Used to check if the stereotypes Report, Transaction and ElementsGroupOperation
 * are set for the UML Operation element, and to retrieve values set for the corresponding stereotypes.
 * 
 * @author Zeljko Ivkovic (zekljo89ps@gmail.com)
 *
 */
public class OperationStereotype {

	/**
	 * Name of the Report stereotype that can be applied to a UML Operation element.
	 */
	public static final String STEREOTYPE_REPORT_NAME="Report";
	/**
	 * Name of the Transaction stereotype that can be applied to a UML Operation element.
	 */
	public static final String STEREOTYPE_TRANSACTION_NAME="Transaction";
	/**
	 * Name of the ElementsGroupOperation stereotype that can be applied to a UML Operation element.
	 */
	public static final String STEREOTYPE_ELEMENTS_GROUP_OPERATION_NAME="ElementsGroupOperation";                       
	
	/**
	 * Checks if ElementsGroupOperation stereotype is applied for the received UML Operation element.
	 * @param object     UML Operation element for which to check if ElementsGroupOperation stereotype is applied
	 * @return           <code>true</code> if ElementsGroupOperation stereotype is applied, <code>false</code> otherwise
	 */
	public static boolean isElementsGroupOperationStereotypeApplied(Operation object){
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+STEREOTYPE_ELEMENTS_GROUP_OPERATION_NAME);
		
		if(stereotypeObject!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * Gets the value of the property nestedElements of the ElementsGroupOperation stereotype.
	 * @param object  UML Operation element for which to retrieve the value of the nestedElements property
	 * @param thread  background worker thread, implementing the import functionality, used to output messages
	 * of the current progress if the value has been set 
	 * @return        nesteElements property value if it has been set, <code>null</code> otherwise
	 */
	public static EList<Operation> getStereotypeElementsGroupOperationNestedElements(Operation object,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_ELEMENTS_GROUP_OPERATION_NAME;
		String stereotypeProperty="nestedElements";
		thread.publishText("Retreiving property "+stereotypeProperty+" for "+stereotypeName+" stereotype");
		thread.addIndentation();
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+stereotypeName);
		
		if(stereotypeObject!=null)
		{
			Object value;
			if((value=getProperty(object, stereotypeObject,stereotypeName, stereotypeProperty, thread))!=null)
			{
				thread.removeIndentation(1);
				return (EList<Operation>)value;
			}else
			{
				thread.publishText("Property "+stereotypeProperty+" for "+stereotypeName+" stereotype not set");
				thread.removeIndentation(1);
				return null;
			}
		}
		else
			thread.publishText(stereotypeName+" stereotype not applied");
		thread.removeIndentation(1);
		return null;
	}
	
	/**
	 * Applies ElementsGroupOperation stereotype to the UML Operation element and sets the value of 
	 * the nestedElements property value for the ElementsGroupOperation stereotype.
	 * @param profile         UML profile that defines the ElementsGroupOperation stereotype
	 * @param object          UML Operation element for which to apply ElementsGroupOperation stereotype and
	 * set the nestedElements property value 
	 * @param nestedElements  value to set for the nestedElements property value of the ElementsGroupOperation stereotype
	 * @param thread          background worker thread, implementing the export functionality, used to output messages of
	 * the current progress that the value for the nestedElements property is being set
	 */
	public static void setStereotypeElementsGroupOperationNestedElements(Profile profile,Operation object,List<Operation> nestedElements,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_ELEMENTS_GROUP_OPERATION_NAME;
		String stereotypeProperty="nestedElements";
		thread.addIndentation();
		Stereotype stereotypeObject=profile.getOwnedStereotype(stereotypeName);
		
		
		if(stereotypeObject!=null)
		{
			thread.publishText("Applaying stereotype "+stereotypeName);
            object.applyStereotype(stereotypeObject);
            thread.publishText("Setting property "+stereotypeProperty+" for "+stereotypeName+" stereotype");
			object.setValue(stereotypeObject, stereotypeProperty, nestedElements);
			StereotypeUtil.outputMessage(thread, stereotypeName);
		}
		else
			thread.publishText(stereotypeName+" stereotype not applied");
		thread.removeIndentation(1);
	}
	
	/**
	 * For a UML Operation element retrieves all the property values of the ElementsGroupOperation stereotype, if it is applied
	 * to the UML Operation element and sets the retrieved values to the corresponding attributes of the
	 * Kroki ElementsGroup object.    
	 * @param object        UML Operation element for which to retrieve the property values of the ElementsGroupOperation stereotype
	 * @param krokiObject   Kroki ElementsGroup object for which to set the retrieved values
	 * @param thread        background worker thread, implementing the import functionality, used to output messages
	 * of the current progress of the values that are being retrieved 
	 */
	public static void stereotypeElementsGroupOperationImport(Operation object,ElementsGroup krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_ELEMENTS_GROUP_OPERATION_NAME;
		thread.publishText("Checking "+stereotypeName+" stereotype");
		thread.addIndentation();
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+stereotypeName);
		
		if(stereotypeObject!=null)
		{
			thread.publishText(stereotypeName+" stereotype applied");
			Object value;
			if((value=getProperty(object, stereotypeObject,stereotypeName, "label", thread))!=null)
			{
				krokiObject.setLabel((String)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "visible", thread))!=null)
			{
				krokiObject.setVisible((boolean)value);
			}else
				krokiObject.setVisible(false);
		}
		else
			thread.publishText(stereotypeName+" stereotype not applied");
		thread.removeIndentation(1);
	}
	
	/**
	 * For a Kroki ElementsGroup object retrieves all the values corresponding to the property values of the ElementsGroupOperation
	 * stereotype and sets them to the UML Operation element.
	 * @param profile      UML profile that defines a ElementsGroupOperation stereotype
	 * @param object       UML Operation element for which to set the property values of the ElementsGroupOperation stereotype corresponding  
	 * to the attribute values of the Kroki ElementsGroup object
	 * @param krokiObject  Kroki ElementsGroup object from which to retrieve the attribute values to be set to the
	 * stereotype property values of the UML Operation element
	 * @param thread       background worker thread, implementing the export functionality, used to output messages of
	 * the current progress of the property values that are being set
	 */
	public static void stereotypeElementsGroupOperationExport(Profile profile,Operation object,ElementsGroup krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_ELEMENTS_GROUP_OPERATION_NAME;
		
		Stereotype stereotypeObject=profile.getOwnedStereotype(stereotypeName);
		if(stereotypeObject!=null)
		{
			thread.publishText("Applaying stereotype "+stereotypeName);
			thread.addIndentation();
			object.applyStereotype(stereotypeObject);
			
			setProperty(object,stereotypeObject,"label",krokiObject.getLabel(),thread);
			
			setProperty(object,stereotypeObject,"visible",krokiObject.isVisible(),thread);
			thread.removeIndentation(1);
		}
		else
			thread.publishText(stereotypeName+" stereotype could not be applied");
	}
	
	/**
	 * Checks if Report stereotype is applied for the received UML Operation element.
	 * @param object     UML Operation element for which to check if Report stereotype is applied
	 * @return           <code>true</code> if Report stereotype is applied, <code>false</code> otherwise
	 */
	public static boolean isReportStereotypeApplied(Operation object){
		Stereotype standardPanel=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+STEREOTYPE_REPORT_NAME);
		
		if(standardPanel!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * For a UML Operation element retrieves all the property values of the Report stereotype, if it is applied
	 * to the UML Operation element and sets the retrieved values to the corresponding attributes of the
	 * Kroki Report object.    
	 * @param object        UML Operation element for which to retrieve the property values of the Report stereotype
	 * @param krokiObject   Kroki Report object for which to set the retrieved values
	 * @param thread        background worker thread, implementing the import functionality, used to output messages
	 * of the current progress of the values that are being retrieved 
	 */
	public static void stereotypeReportImport(Operation object,Report krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_REPORT_NAME;
		thread.publishText("Checking "+stereotypeName+" stereotype");
		thread.addIndentation();
		
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+stereotypeName);
		
		if(stereotypeObject!=null)
		{
			thread.publishText(stereotypeName+" stereotype applied");
			Object value;
			if((value=getProperty(object, stereotypeObject,stereotypeName, "reportName", thread))!=null)
			{
				krokiObject.setReportName((String)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "dataFilter", thread))!=null)
			{
				krokiObject.setDataFilter((String)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "sortBy", thread))!=null)
			{
				krokiObject.setSortBy((String)value);
			}
			//BusinessOperation attributes
			if((value=getProperty(object, stereotypeObject,stereotypeName, "hasParametersForm", thread))!=null)
			{
				krokiObject.setHasParametersForm((boolean)value);
			}else
				krokiObject.setHasParametersForm(false);
			if((value=getProperty(object, stereotypeObject,stereotypeName, "filteredBy", thread))!=null)
			{
				krokiObject.setFilteredByKey((boolean)value);
			}else
				krokiObject.setFilteredByKey(false);
			
			//VisibleElement attributes
			if((value=getProperty(object, stereotypeObject,stereotypeName, "label", thread))!=null)
			{
				krokiObject.setLabel((String)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "visible", thread))!=null)
			{
				krokiObject.setVisible((boolean)value);
			}else
				krokiObject.setVisible(false);
		}
		else
			thread.publishText(stereotypeName+" stereotype not applied");
		thread.removeIndentation(1);
	}
	
	/**
	 * For a Kroki Report object retrieves all the values corresponding to the property values of the Report
	 * stereotype and sets them to the UML Operation element.
	 * @param profile      UML profile that defines a Report stereotype
	 * @param object       UML Operation element for which to set the property values of the Report stereotype corresponding  
	 * to the attribute values of the Kroki Report object
	 * @param krokiObject  Kroki Report object from which to retrieve the attribute values to be set to the
	 * stereotype property values of the UML Operation element
	 * @param thread       background worker thread, implementing the export functionality, used to output messages of
	 * the current progress of the property values that are being set
	 */
	public static void stereotypeReportExport(Profile profile,Operation object,Report krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_REPORT_NAME;
		
		Stereotype stereotypeObject=profile.getOwnedStereotype(stereotypeName);
		if(stereotypeObject!=null)
		{
			thread.publishText("Applaying stereotype "+stereotypeName);
			thread.addIndentation();
			object.applyStereotype(stereotypeObject);
			
			setProperty(object,stereotypeObject,"reportName",krokiObject.getReportName(),thread);
			
			setProperty(object,stereotypeObject,"dataFilter",krokiObject.getDataFilter(),thread);
			
			setProperty(object,stereotypeObject,"sortBy",krokiObject.getSortBy(),thread);
			
			//BusinessOperation attributes
            setProperty(object,stereotypeObject,"hasParametersForm",krokiObject.isHasParametersForm(),thread);
			
			setProperty(object,stereotypeObject,"filteredBy",krokiObject.isFilteredByKey(),thread);
			
			//VisibleElemente attributes
            setProperty(object,stereotypeObject,"label",krokiObject.getLabel(),thread);
			
			setProperty(object,stereotypeObject,"visible",krokiObject.isVisible(),thread);
			thread.removeIndentation(1);
		}
		else
			thread.publishText(stereotypeName+" stereotype could not be applied");
	}
	
	/**
	 * Used to check if a property value of a stereotype is set for the UML Operation element and to retrieve its value. 
	 * @param object            UML Operation element for which to check and retrieve the value
	 * @param stereotypeObject  UML stereotype object for which the value should be retrieved 
	 * @param propertyName      name of the stereotype property for which the value should be retrieved
	 * @param thread            background worker thread, implementing the import functionality, used to
	 * output messages if the value has been set
	 * @return                  value retrieved for the corresponding stereotype property
	 */
	public static Object getProperty(Operation object,Stereotype stereotypeObject,String stereotypeName,String propertyName,ProgressWorker thread){
		if(object.hasValue(stereotypeObject, propertyName))
		{
			thread.publishText(stereotypeName+" "+propertyName+" property is set");
			return object.getValue(stereotypeObject, propertyName);
		}else
			thread.publishText(stereotypeName+" "+propertyName+" property not set");
		return null;
	}
	
	/**
	 * Used to set the property value of the stereotype for the UML Operation element.
	 * @param object            UML Operation element for which to set the stereotype property value
	 * @param stereotypeObject  UML stereotype object for which to set the property value 
	 * @param propertyName      name of the stereotype property to set
	 * @param value             value to set for the stereotype property
	 * @param thread            background worker thread, implementing the export functionality, that is used to output
	 * messages of which property has been set
	 */
	public static void setProperty(Operation object,Stereotype stereotypeObject,String propertyName,Object value,ProgressWorker thread){
		object.setValue(stereotypeObject, propertyName, value);
		StereotypeUtil.outputMessage(thread, propertyName);
	}
	
	/**
	 * Checks if Transaction stereotype is applied for the received UML Operation element.
	 * @param object     UML Operation element for which to check if Transaction stereotype is applied
	 * @return           <code>true</code> if Transaction stereotype is applied, <code>false</code> otherwise
	 */
	public static boolean isTransactionStereotypeApplied(Operation object){
		Stereotype standardPanel=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+STEREOTYPE_TRANSACTION_NAME);
		
		if(standardPanel!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * For a UML Operation element retrieves all the property values of the Transaction stereotype, if it is applied
	 * to the UML Operation element and sets the retrieved values to the corresponding attributes of the
	 * Kroki Transaction object.    
	 * @param object        UML Operation element for which to retrieve the property values of the Transaction stereotype
	 * @param krokiObject   Kroki Transaction object for which to set the retrieved values
	 * @param thread        background worker thread, implementing the import functionality, used to output messages
	 * of the current progress of the values that are being retrieved 
	 */
	public static void stereotypeTransactionImport(Operation object,Transaction krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_TRANSACTION_NAME;
		thread.publishText("Checking "+stereotypeName+" stereotype");
		thread.addIndentation();
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+stereotypeName);
		
		
		if(stereotypeObject!=null)
		{
			thread.publishText(stereotypeName+" stereotype applied");
			Object value;
			if((value=getProperty(object, stereotypeObject,stereotypeName, "refreshRow", thread))!=null)
			{
				krokiObject.setRefreshRow((boolean)value);
			}else
				krokiObject.setRefreshRow(false);
			if((value=getProperty(object, stereotypeObject,stereotypeName, "refreshAll", thread))!=null)
			{
				krokiObject.setRefreshAll((boolean)value);
			}else
				krokiObject.setRefreshAll(false);
			if((value=getProperty(object, stereotypeObject,stereotypeName, "askConfirmation", thread))!=null)
			{
				krokiObject.setAskConfirmation((boolean)value);
			}else
				krokiObject.setAskConfirmation(false);
			if((value=getProperty(object, stereotypeObject,stereotypeName, "confirmationMessage", thread))!=null)
			{
				krokiObject.setConfirmationMessage((String)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "showErrors", thread))!=null)
			{
				krokiObject.setShowErrors((boolean)value);
			}else
				krokiObject.setShowErrors(false);
			
			//BusinessOperation attributes
			if((value=getProperty(object, stereotypeObject,stereotypeName, "hasParametersForm", thread))!=null)
			{
				krokiObject.setHasParametersForm((boolean)value);
			}else
				krokiObject.setHasParametersForm(false);
			if((value=getProperty(object, stereotypeObject,stereotypeName, "filteredBy", thread))!=null)
			{
				krokiObject.setFilteredByKey((boolean)value);
			}else
				krokiObject.setFilteredByKey(false);
			//VisibleElement attributes
			if((value=getProperty(object, stereotypeObject,stereotypeName, "label", thread))!=null)
			{
				krokiObject.setLabel((String)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "visible", thread))!=null)
			{
				krokiObject.setVisible((boolean)value);
			}else
				krokiObject.setVisible(false);
		}
		else
			thread.publishText(stereotypeName+" stereotype not applied");
		thread.removeIndentation(1);
	}
	
	/**
	 * For a Kroki Transaction object retrieves all the values corresponding to the property values of the Transaction
	 * stereotype and sets them to the UML Operation element.
	 * @param profile      UML profile that defines a Transaction stereotype
	 * @param object       UML Operation element for which to set the property values of the Transaction stereotype corresponding  
	 * to the attribute values of the Kroki Transaction object
	 * @param krokiObject  Kroki Transaction object from which to retrieve the attribute values to be set to the
	 * stereotype property values of the UML Operation element
	 * @param thread       background worker thread, implementing the export functionality, used to output messages of
	 * the current progress of the property values that are being set
	 */
	public static void stereotypeTransactionExport(Profile profile,Operation object,Transaction krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_TRANSACTION_NAME;
				
		Stereotype stereotypeObject=profile.getOwnedStereotype(stereotypeName);
		if(stereotypeObject!=null)
		{
			thread.publishText("Applaying stereotype "+stereotypeName);
			thread.addIndentation();
			object.applyStereotype(stereotypeObject);
			
			setProperty(object,stereotypeObject,"refreshRow",krokiObject.isRefreshRow(),thread);
			
			setProperty(object,stereotypeObject,"refreshAll",krokiObject.isRefreshAll(),thread);
			
			setProperty(object,stereotypeObject,"askConfirmation",krokiObject.isAskConfirmation(),thread);
			
			setProperty(object,stereotypeObject,"confirmationMessage",krokiObject.getConfirmationMessage(),thread);
			
			setProperty(object,stereotypeObject,"showErrors",krokiObject.isShowErrors(),thread);
			
			//BusinessOperation attributes
            setProperty(object,stereotypeObject,"hasParametersForm",krokiObject.isHasParametersForm(),thread);
			
			setProperty(object,stereotypeObject,"filteredBy",krokiObject.isFilteredByKey(),thread);
			
			//VisibleElemente attributes
            setProperty(object,stereotypeObject,"label",krokiObject.getLabel(),thread);
			
			setProperty(object,stereotypeObject,"visible",krokiObject.isVisible(),thread);
			thread.removeIndentation(1);
		}
		else
			thread.publishText(stereotypeName+" stereotype could not be applied");
	}
}
