package kroki.app.utils.uml.stereotypes;

import kroki.app.utils.uml.ProgressWorker;
import kroki.profil.VisibleElement;

import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;

/**
 * Used to check if the stereotype VisibleElement is set for the UML NamedElement element,
 * and to retrieve values set for the corresponding stereotype.
 * 
 * @author Zeljko Ivkovic (zekljo89ps@gmail.com)
 *
 */
public class NamedElementStereotype {

	/**
	 * Name of the VisibleElement stereotype that can be applied to a UML NamedElement element.
	 */
	public static final String STEREOTYPE_VISIBLE_ELEMENT_NAME="VisibleElement";
	
	/**
	 * Used to check if a property value of a stereotype is set for the UML NamedElement element and to retrieve its value. 
	 * @param object            UML NamedElement element for which to check and retrieve the value
	 * @param stereotypeObject  UML stereotype object for which the value should be retrieved
	 * @param propertyName      name of the stereotype property for which the value should be retrieved 
	 * @param thread            background worker thread, implementing the import functionality, used to
	 * output messages if the value has been set
	 * @return                  value retrieved for the corresponding stereotype property
	 */
	public static Object getProperty(org.eclipse.uml2.uml.NamedElement object,Stereotype stereotypeObject,String propertyName,ProgressWorker thread){
		if(object.hasValue(stereotypeObject, propertyName))
		{
			thread.publishText(stereotypeObject.getName()+" "+propertyName+" property is set");
			return object.getValue(stereotypeObject, propertyName);
		}else
			thread.publishText(stereotypeObject.getName()+" "+propertyName+" property not set");
		return null;
	}
	
	/**
	 * Used to set the property value of the stereotype for the UML NamedElement element.
	 * @param object            UML NamedElement element for which to set the stereotype property value
	 * @param stereotypeObject  UML stereotype object for which to set the property value 
	 * @param propertyName      name of the stereotype property to set
	 * @param value             value to set for the stereotype property
	 * @param thread            background worker thread, implementing the export functionality, that is used to output
	 * messages of which property has been set
	 */
	public static void setProperty(org.eclipse.uml2.uml.NamedElement object,Stereotype stereotypeObject,String propertyName,Object value,ProgressWorker thread){
		object.setValue(stereotypeObject, propertyName, value);
		StereotypeUtil.outputMessage(thread, propertyName);
	}
	
	/**
	 * For a UML NamedElement element retrieves all the property values of the VisibleElement stereotype, if it is applied
	 * to the UML NamedElement element and sets the retrieved values to the corresponding attributes of the
	 * Kroki VisibleElement object.    
	 * @param object        UML NamedElement element for which to retrieve the property values of the VisibleElement stereotype
	 * @param krokiObject   Kroki VisibleElement object for which to set the retrieved values
	 * @param thread        background worker thread, implementing the import functionality, used to output messages
	 * of the current progress of the values that are being retrieved 
	 */
	public static void stereotypeVisibleElementImport(org.eclipse.uml2.uml.NamedElement object,VisibleElement krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_VISIBLE_ELEMENT_NAME;
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
	 * For a Kroki VisibleElement object retrieves all the values corresponding to the property values of the VisibleElement
	 * stereotype and sets them to the UML NamedElement element.
	 * @param profile      UML profile that defines a StandardPanel stereotype
	 * @param object       UML NamedElement element for which to set the property values of the VisibleElement stereotype corresponding  
	 * to the attribute values of the Kroki VisibleElement object
	 * @param krokiObject  Kroki VisibleElement object from which to retrieve the attribute values to be set to the
	 * stereotype property values of the UML NamedElement element
	 * @param thread       background worker thread, implementing the export functionality, used to output messages of
	 * the current progress of the property values that are being set
	 */
	public static void stereotypeVisibleElementExport(Profile profile,org.eclipse.uml2.uml.NamedElement object,VisibleElement krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_VISIBLE_ELEMENT_NAME;
		
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
