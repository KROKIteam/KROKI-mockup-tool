package kroki.app.utils.uml.stereotypes;

import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;

import kroki.app.utils.uml.ProgressWorker;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Used to check if the stereotype BusinessSubsystem
 * is set for the UML Package element, and to retrieve values set for the corresponding stereotypes.
 * 
 * @author Zeljko Ivkovic (ivkovicszeljko@gmail.com)
 *
 */
public class PackageStereotype {

	/**
	 * Name of the BusinessSubsystem stereotype that can be applied to a UML Package element.
	 */
	public static final String STEREOTYPE_BUSINESS_SUBSYSTEM_NAME="BusinessSubsystem";
	
	/**
	 * Used to check if a property value of a stereotype is set for the UML Package element and to retrieve its value. 
	 * @param object            UML Package element for which to check and retrieve the value
	 * @param stereotypeObject  UML stereotype object for which the value should be retrieved
	 * @param propertyName      name of the stereotype property for which the value should be retrieved 
	 * @param thread            background worker thread, implementing the import functionality, used to
	 * output messages if the value has been set
	 * @return                  value retrieved for the corresponding stereotype property
	 */
	public static Object getProperty(Package object,Stereotype stereotypeObject,String propertyName,ProgressWorker thread){
		if(object.hasValue(stereotypeObject, propertyName))
		{
			thread.publishText(stereotypeObject.getName()+" "+propertyName+" property is set");
			return object.getValue(stereotypeObject, propertyName);
		}else
			thread.publishText(stereotypeObject.getName()+" "+propertyName+" property not set");
		return null;
	}
	
	/**
	 * Used to set the property value of the stereotype for the UML Package element.
	 * @param object            UML Package element for which to set the stereotype property value
	 * @param stereotypeObject  UML stereotype object for which to set the property value 
	 * @param propertyName      name of the stereotype property to set
	 * @param value             value to set for the stereotype property
	 * @param thread            background worker thread, implementing the export functionality, that is used to output
	 * messages of which property has been set
	 */
	public static void setProperty(Package object,Stereotype stereotypeObject,String propertyName,Object value,ProgressWorker thread){
		object.setValue(stereotypeObject, propertyName, value);
		StereotypeUtil.outputMessage(thread, propertyName);
	}
	
	/**
	 * For a UML Package element retrieves all the property values of the BusinessSubsystem stereotype, if it is applied
	 * to the UML Package element and sets the retrieved values to the corresponding attributes of the
	 * Kroki BussinesSubsystem object.    
	 * @param object        UML Package element for which to retrieve the property values of the BusinessSubsystem stereotype
	 * @param krokiObject   Kroki BussinesSubsystem object for which to set the retrieved values
	 * @param thread        background worker thread, implementing the import functionality, used to output messages
	 * of the current progress of the values that are being retrieved 
	 */
	public static void stereotypeBusinessSubsystemImport(Package object,BussinesSubsystem krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_BUSINESS_SUBSYSTEM_NAME;
		thread.publishText("Checking "+stereotypeName+" stereotype");
		thread.addIndentation();
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+stereotypeName);
		
		if(stereotypeObject!=null)
		{
			thread.publishText(stereotypeName+" stereotype applied");
			Object value;
			if((value=getProperty(object, stereotypeObject, "label", thread))!=null)
			{
				krokiObject.setLabel((String)value);
			}
			if((value=getProperty(object, stereotypeObject, "visible", thread))!=null)
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
	 * For a Kroki BussinesSubsystem object retrieves all the values corresponding to the property values of the BussinesSubsystem
	 * stereotype and sets them to the UML Package element.
	 * @param profile      UML profile that defines a StandardPanel stereotype
	 * @param object       UML Package element for which to set the property values of the BusinessSubsystem stereotype corresponding  
	 * to the attribute values of the Kroki BussinesSubsystem object
	 * @param krokiObject  Kroki BussinesSubsystem object from which to retrieve the attribute values to be set to the
	 * stereotype property values of the UML Package element
	 * @param thread       background worker thread, implementing the export functionality, used to output messages of
	 * the current progress of the property values that are being set
	 */
	public static void stereotypeBusinessSbsystemExport(Profile profile,Package object,BussinesSubsystem krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_BUSINESS_SUBSYSTEM_NAME;
		
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
}
