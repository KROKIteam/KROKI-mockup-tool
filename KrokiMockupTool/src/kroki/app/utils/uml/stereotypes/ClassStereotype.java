package kroki.app.utils.uml.stereotypes;

import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;

import kroki.app.utils.uml.ProgressWorker;
import kroki.profil.panel.StandardPanel;

/**
 * Used to check if the stereotypes StandardPanel and ParentChild
 * are set for the UML Class element, and to retrieve values set for the corresponding stereotypes.
 * 
 * @author Zeljko Ivkovic (zekljo89ps@gmail.com)
 *
 */
public class ClassStereotype {

	/**
	 * Name of the StandardPanel stereotype that can be applied to a UML Class element.
	 */
	public static final String STEREOTYPE_STANDARD_PANEL_NAME="StandardPanel";
	
	/**
	 * Name of the ParentChild stereotype that can be applied to a UML Class element.
	 */
	public static final String STEREOTYPE_PARENT_CHILD_NAME="ParentChild";
    
	/**
	 * Checks if StandardPanel stereotype is applied for the received UML Class element.
	 * @param object     UML Class element for which to check if StandardPanel stereotype is applied
	 * @return           <code>true</code> if StandardPanel stereotype is applied, <code>false</code> otherwise
	 */
	public static boolean isStandardPanelStereotypeApplied(org.eclipse.uml2.uml.Class object){
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+STEREOTYPE_STANDARD_PANEL_NAME);
		
		if(stereotypeObject!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * For a UML Class element retrieves all the property values of the StandardPanel stereotype, if it is applied
	 * to the UML Class element and sets the retrieved values to the corresponding attributes of the
	 * Kroki StandardPanel object.    
	 * @param object        UML Class element for which to retrieve the property values of the StandardPanel stereotype
	 * @param krokiObject   Kroki StandardPanel object for which to set the retrieved values
	 * @param thread        background worker thread, implementing the import functionality, used to output messages
	 * of the current progress of the values that are being retrieved 
	 */
	public static void stereotypeStandardPanelImport(org.eclipse.uml2.uml.Class object,StandardPanel krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_STANDARD_PANEL_NAME;
		thread.publishText("Checking "+stereotypeName+" stereotype");
		thread.addIndentation();
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+stereotypeName);
		
		if(stereotypeObject!=null)
		{
			thread.publishText(stereotypeName+" stereotype applied");
			Object value;
			boolean stereotypeButtons;
			if((value=getProperty(object, stereotypeObject, "add", thread))!=null)
			{
				stereotypeButtons=(boolean)value;
				if(stereotypeButtons!=krokiObject.isAdd())
					krokiObject.setAdd((boolean)value);
			}else
				krokiObject.setAdd(false);
			if((value=getProperty(object, stereotypeObject, "update", thread))!=null)
			{
				stereotypeButtons=(boolean)value;
				if(stereotypeButtons!=krokiObject.isUpdate())
					krokiObject.setUpdate((boolean)value);
			}else
				krokiObject.setUpdate(false);
			if((value=getProperty(object, stereotypeObject, "copy", thread))!=null)
			{
				stereotypeButtons=(boolean)value;
				if(stereotypeButtons!=krokiObject.isCopy())
					krokiObject.setCopy((boolean)value);
			}else
				krokiObject.setCopy(false);
			if((value=getProperty(object, stereotypeObject, "delete", thread))!=null)
			{
				stereotypeButtons=(boolean)value;
				if(stereotypeButtons!=krokiObject.isDelete())
					krokiObject.setDelete((boolean)value);
			}else
				krokiObject.setDelete(false);
			if((value=getProperty(object, stereotypeObject, "search", thread))!=null)
			{
				stereotypeButtons=(boolean)value;
				if(stereotypeButtons!=krokiObject.isSearch())
					krokiObject.setSearch((boolean)value);
			}else
				krokiObject.setSearch(false);
			if((value=getProperty(object, stereotypeObject, "changeMode", thread))!=null)
			{
				stereotypeButtons=(boolean)value;
				if(stereotypeButtons!=krokiObject.isChangeMode())
					krokiObject.setChangeMode((boolean)value);
			}else
				krokiObject.setChangeMode(false);
			if((value=getProperty(object, stereotypeObject, "dataNavigation", thread))!=null)
			{
				stereotypeButtons=(boolean)value;
				if(stereotypeButtons!=krokiObject.isDataNavigation())
					krokiObject.setDataNavigation((boolean)value);
			}else
				krokiObject.setDataNavigation(false);
			
			if((value=getProperty(object, stereotypeObject, "modal", thread))!=null)
			{
				krokiObject.setModal((boolean)value);
			}else
				krokiObject.setModal(false);
			
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
	 * For a Kroki StandardPanel object retrieves all the values corresponding to the property values of the StandardPanel
	 * stereotype and sets them to the UML Class element.
	 * @param profile      UML profile that defines a StandardPanel stereotype
	 * @param object       UML Class element for which to set the property values of the StandardPanel stereotype corresponding  
	 * to the attribute values of the Kroki StandardPanel object
	 * @param krokiObject  Kroki StandardPanel object from which to retrieve the attribute values to be set to the
	 * stereotype property values of the UML Class element
	 * @param thread       background worker thread, implementing the export functionality, used to output messages of
	 * the current progress of the property values that are being set
	 */
	public static void stereotypeStandardPanelExport(Profile profile,org.eclipse.uml2.uml.Class object,StandardPanel krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_STANDARD_PANEL_NAME;
		
		Stereotype stereotypeObject=profile.getOwnedStereotype(stereotypeName);
		if(stereotypeObject!=null)
		{
			thread.publishText("Applaying stereotype "+stereotypeName);
			thread.addIndentation();
			object.applyStereotype(stereotypeObject);
			
			setProperty(object,stereotypeObject,"add",krokiObject.isAdd(),thread);
			
			setProperty(object,stereotypeObject,"update",krokiObject.isUpdate(),thread);
			
			setProperty(object,stereotypeObject,"copy",krokiObject.isCopy(),thread);

			setProperty(object,stereotypeObject,"delete",krokiObject.isDelete(),thread);

			setProperty(object,stereotypeObject,"search",krokiObject.isSearch(),thread);
			
			setProperty(object,stereotypeObject,"changeMode",krokiObject.isChangeMode(),thread);
			
			setProperty(object,stereotypeObject,"dataNavigation",krokiObject.isDataNavigation(),thread);
			
			//VisibleClass attribute
			setProperty(object,stereotypeObject,"modal",krokiObject.isModal(),thread);
			
			//VisibleElemente attributes
            setProperty(object,stereotypeObject,"label",krokiObject.getLabel(),thread);
			
			setProperty(object,stereotypeObject,"visible",krokiObject.isVisible(),thread);
			
			
			thread.removeIndentation(1);
		}
		else
			thread.publishText(stereotypeName+" stereotype could not be applied");
	}
	
	/**
	 * Used to check if a property value of a stereotype is set for the UML Class element and to retrieve its value. 
	 * @param object            UML Class element for which to check and retrieve the value
	 * @param stereotypeObject  UML stereotype object for which the value should be retrieved 
	 * @param propertyName      name of the stereotype property for which the value should be retrieved
	 * @param thread            background worker thread, implementing the import functionality, used to
	 * output messages if the value has been set
	 * @return                  value retrieved for the corresponding stereotype property
	 */
	public static Object getProperty(org.eclipse.uml2.uml.Class object,Stereotype stereotypeObject,String propertyName,ProgressWorker thread){
		if(object.hasValue(stereotypeObject, propertyName))
		{
			thread.publishText(stereotypeObject.getName()+" "+propertyName+" property is set");
			return object.getValue(stereotypeObject, propertyName);
		}else
			thread.publishText(stereotypeObject.getName()+" "+propertyName+" property not set");
		return null;
	}
	
	/**
	 * Used to set the property value of the stereotype for the UML Class element.
	 * @param object            UML Class element for which to set the stereotype property value
	 * @param stereotypeObject  UML stereotype object for which to set the property value 
	 * @param propertyName      name of the stereotype property to set
	 * @param value             value to set for the stereotype property
	 * @param thread            background worker thread, implementing the export functionality, that is used to output
	 * messages of which property has been set
	 */
	public static void setProperty(org.eclipse.uml2.uml.Class object,Stereotype stereotypeObject,String propertyName,Object value,ProgressWorker thread){
		object.setValue(stereotypeObject, propertyName, value);
		StereotypeUtil.outputMessage(thread, propertyName);
	}
	
	/**
	 * Checks if ParentChild stereotype is applied for the received UML Class element.
	 * @param object     UML Class element for which to check if ParentChild stereotype is applied
	 * @return           <code>true</code> if ParentChild stereotype is applied, <code>false</code> otherwise
	 */
	public static boolean isParenChildStereotypeApplied(org.eclipse.uml2.uml.Class object){
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+STEREOTYPE_PARENT_CHILD_NAME);
		
		if(stereotypeObject!=null)
			return true;
		else
			return false;
	}
}
