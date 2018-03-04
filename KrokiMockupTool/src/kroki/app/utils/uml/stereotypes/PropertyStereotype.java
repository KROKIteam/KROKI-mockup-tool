package kroki.app.utils.uml.stereotypes;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

import kroki.app.utils.uml.ProgressWorker;
import kroki.profil.association.Next;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.VisibleProperty;

/**
 * Used to check if the stereotypes VisibleAssociationEnd, Zoom, Next, Hierarchy, ElementsGroupProperty and
 * VisibleProperty are set for the UML Property element, and to retrieve values set for the corresponding stereotypes.
 * 
 * @author Zeljko Ivkovic (ivkovicszeljko@gmail.com)
 *
 */
public class PropertyStereotype {

	/**
	 * Name of the Zoom stereotype that can be applied to a UML Property element.
	 */
	public static final String STEREOTYPE_ZOOM_NAME="Zoom";
	/**
	 * Name of the Next stereotype that can be applied to a UML Property element.
	 */
	public static final String STEREOTYPE_NEXT_NAME="Next";
	/**
	 * Name of the Hierarchy stereotype that can be applied to a UML Property element.
	 */
	public static final String STEREOTYPE_HIERARCHY_NAME="Hierarchy";
	/**
	 * Name of the ElementsGroupProperty stereotype that can be applied to a UML Property element.
	 */
	public static final String STEREOTYPE_ELEMENTS_GROUP_PROPERTY_NAME="ElementsGroupProperty";
	/**
	 * Name of the VisibleProperty stereotype that can be applied to a UML Property element.
	 */
	public static final String STEREOTYPE_VISIBLE_PROPERTY_NAME="VisibleProperty";	
	
	/**
	 * Checks if VisibleProperty stereotype is applied for the received UML Property element.
	 * @param object     UML Property element for which to check if VisibleProperty stereotype is applied
	 * @return           <code>true</code> if VisibleProperty stereotype is applied, <code>false</code> otherwise
	 */
	public static boolean isVisiblePropertyStereotypeApplied(Property object){
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+STEREOTYPE_VISIBLE_PROPERTY_NAME);
		
		if(stereotypeObject!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if ElementsGroupProperty stereotype is applied for the received UML Property element.
	 * @param object     UML Property element for which to check if ElementsGroupProperty stereotype is applied
	 * @return           <code>true</code> if ElementsGroupProperty stereotype is applied, <code>false</code> otherwise
	 */
	public static boolean isElementsGroupPropertyStereotypeApplied(Property object){
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+STEREOTYPE_ELEMENTS_GROUP_PROPERTY_NAME);
		
		if(stereotypeObject!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * Gets the value of the property nestedElements of the ElementsGroupProperty stereotype.
	 * @param object  UML Property element for which to retrieve the value of the nestedElements property
	 * @param thread  background worker thread, implementing the import functionality, used to output messages
	 * of the current progress if the value has been set 
	 * @return        nesteElements property value if it has been set, <code>null</code> otherwise
	 */
	public static EList<Property> getStereotypeElementsGroupPropertyNestedElements(Property object,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_ELEMENTS_GROUP_PROPERTY_NAME;
		String stereotypeProperty="nestedElements";
		thread.publishText("Retreiving property "+stereotypeProperty+" for stereotype "+stereotypeName+" stereotype");
		thread.addIndentation();
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+stereotypeName);
		
		if(stereotypeObject!=null)
		{
			Object value;
			if((value=getProperty(object, stereotypeObject,stereotypeName, stereotypeProperty, thread))!=null)
			{
				thread.removeIndentation(1);
				return (EList<Property>)value;
			}else
			{
				thread.publishText("Property "+stereotypeProperty+" for stereotype "+stereotypeName+" stereotype not set");
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
	 * Applies ElementsGroupProperty stereotype to the UML Property element and sets the value of 
	 * the nestedElements property value for the ElementsGroupOperation stereotype.
	 * @param profile         UML profile that defines the ElementsGroupProperty stereotype
	 * @param object          UML Property element for which to apply ElementsGroupProperty stereotype and
	 * set the nestedElements property value 
	 * @param nestedElements  value to set for the nestedElements property value of the ElementsGroupProperty stereotype
	 * @param thread          background worker thread, implementing the export functionality, used to output messages of
	 * the current progress that the value for the nestedElements property is being set
	 */
	public static void setStereotypeElementsGroupPropertyNestedElements(Profile profile,Property object,List<Property> nestedElements,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_ELEMENTS_GROUP_PROPERTY_NAME;
		String stereotypeProperty="nestedElements";
		
		Stereotype stereotypeObject=profile.getOwnedStereotype(stereotypeName);
		
		
		if(stereotypeObject!=null)
		{
			//thread.publishText("Applaying stereotype "+stereotypeName);
			//object.applyStereotype(stereotypeObject);
			thread.addIndentation();
            
            thread.publishText("Setting property "+stereotypeProperty+" for "+stereotypeName+" stereotype");
			object.setValue(stereotypeObject, stereotypeProperty, nestedElements);
			StereotypeUtil.outputMessage(thread, stereotypeName);
			thread.removeIndentation(1);
		}
		else
			thread.publishText(stereotypeName+" stereotype not applied");
		
	}
	
	/**
	 * For a UML Property element retrieves all the property values of the ElementsGroupProperty stereotype, if it is applied
	 * to the UML Property element and sets the retrieved values to the corresponding attributes of the
	 * Kroki ElementsGroup object.    
	 * @param object        UML Property element for which to retrieve the property values of the ElementsGroupProperty stereotype
	 * @param krokiObject   Kroki ElementsGroup object for which to set the retrieved values
	 * @param thread        background worker thread, implementing the import functionality, used to output messages
	 * of the current progress of the values that are being retrieved 
	 */
	public static void stereotypeElementsGroupOperationImport(Property object,ElementsGroup krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_ELEMENTS_GROUP_PROPERTY_NAME;
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
	 * For a Kroki ElementsGroup object retrieves all the values corresponding to the property values of the ElementsGroupProperty
	 * stereotype and sets them to the UML Property element.
	 * @param profile      UML profile that defines a ElementsGroupProperty stereotype
	 * @param object       UML Property element for which to set the property values of the ElementsGroupProperty stereotype corresponding  
	 * to the attribute values of the Kroki ElementsGroup object
	 * @param krokiObject  Kroki ElementsGroup object from which to retrieve the attribute values to be set to the
	 * stereotype property values of the UML Property element
	 * @param thread       background worker thread, implementing the export functionality, used to output messages of
	 * the current progress of the property values that are being set
	 */
	public static void stereotypeElementsGroupOperationExport(Profile profile,Property object,ElementsGroup krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_ELEMENTS_GROUP_PROPERTY_NAME;
		
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
	 * Checks if Hierarchy stereotype is applied for the received UML Property element.
	 * @param object     UML Property element for which to check if Hierarchy stereotype is applied
	 * @return           <code>true</code> if Hierarchy stereotype is applied, <code>false</code> otherwise
	 */
	public static boolean isHierarchyStereotypeApplied(Property object){
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+STEREOTYPE_HIERARCHY_NAME);
		
		if(stereotypeObject!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * Gets the value of the property targetPanel of the Hierarchy stereotype.
	 * @param object  UML Property element for which to retrieve the value of the targetPanel property
	 * @param thread  background worker thread, implementing the import functionality, used to output messages
	 * of the current progress if the value has been set 
	 * @return        targetPanel property value if it has been set, <code>null</code> otherwise
	 */
	public static VisibleClass getStereotypeHierarchyTargetPanel(Property object,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_HIERARCHY_NAME;
		String stereotypeProperty="targetPanel";
		thread.publishText("Retreiving property "+stereotypeProperty+" for stereotype "+stereotypeName+" stereotype");
		thread.addIndentation();
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+stereotypeName);
		
		if(stereotypeObject!=null)
		{
			Object value;
			if((value=getProperty(object, stereotypeObject,stereotypeName, stereotypeProperty, thread))!=null)
			{
				thread.removeIndentation(1);
				return (VisibleClass)value;
			}else
			{
				thread.publishText("Property "+stereotypeProperty+" for stereotype "+stereotypeName+" stereotype not set");
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
	 * Applies Hierarchy stereotype to the UML Property element and sets the value of 
	 * the targetPanel property value for the Hierarchy stereotype.
	 * @param profile         UML profile that defines the Hierarchy stereotype
	 * @param object          UML Property element for which to apply Hierarchy stereotype and
	 * set the targetPanel property value 
	 * @param targetPanel     value to set for the targetPanel property value of the Hierarchy stereotype
	 * @param thread          background worker thread, implementing the export functionality, used to output messages of
	 * the current progress that the value for the targetPanel property is being set
	 */
	public static void setStereotypeHierarchyTargetPanel(Profile profile,Property object,VisibleClass targetPanel,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_HIERARCHY_NAME;
		String stereotypeProperty="targetPanel";
		
		Stereotype stereotypeObject=profile.getOwnedStereotype(stereotypeName);
		
		
		if(stereotypeObject!=null)
		{
			thread.publishText("Applaying stereotype "+stereotypeName);
			thread.addIndentation();
            object.applyStereotype(stereotypeObject);
            thread.publishText("Setting property "+stereotypeProperty+" for "+stereotypeName+" stereotype");
			object.setValue(stereotypeObject, stereotypeProperty, targetPanel);
			StereotypeUtil.outputMessage(thread, stereotypeName);
			thread.removeIndentation(1);
		}
		else
			thread.publishText(stereotypeName+" stereotype not applied");
		
	}
	
	/**
	 * Gets the value of the property viaAssociationEnd of the Hierarchy stereotype.
	 * @param object  UML Property element for which to retrieve the value of the viaAssociationEnd property
	 * @param thread  background worker thread, implementing the import functionality, used to output messages
	 * of the current progress if the value has been set 
	 * @return        viaAssociationEnd property value if it has been set, <code>null</code> otherwise
	 */
	public static VisibleAssociationEnd getStereotypeHierarchyViaAssociationEnd(Property object,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_HIERARCHY_NAME;
		String stereotypeProperty="viaAssociationEnd";
		thread.publishText("Retreiving property "+stereotypeProperty+" for stereotype "+stereotypeName+" stereotype");
		thread.addIndentation();
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+stereotypeName);
		
		if(stereotypeObject!=null)
		{
			Object value;
			if((value=getProperty(object, stereotypeObject,stereotypeName, stereotypeProperty, thread))!=null)
			{
				thread.removeIndentation(1);
				return (VisibleAssociationEnd)value;
			}else
			{
				thread.publishText("Property "+stereotypeProperty+" for stereotype "+stereotypeName+" stereotype not set");
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
	 * Applies Hierarchy stereotype to the UML Property element and sets the value of 
	 * the viaAssociationEnd property value for the Hierarchy stereotype.
	 * @param profile            UML profile that defines the Hierarchy stereotype
	 * @param object             UML Property element for which to apply Hierarchy stereotype and
	 * set the viaAssociationEnd property value 
	 * @param viaAssociationEnd  value to set for the viaAssociationEnd property value of the Hierarchy stereotype
	 * @param thread             background worker thread, implementing the export functionality, used to output messages of
	 * the current progress that the value for the viaAssociationEnd property is being set
	 */
	public static void setStereotypeHierarchyViaAssociationEnd(Profile profile,Property object,VisibleAssociationEnd viaAssociationEnd,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_HIERARCHY_NAME;
		String stereotypeProperty="viaAssociationEnd";
		
		Stereotype stereotypeObject=profile.getOwnedStereotype(stereotypeName);
		
		
		if(stereotypeObject!=null)
		{
			thread.publishText("Applaying stereotype "+stereotypeName);
			thread.addIndentation();
            object.applyStereotype(stereotypeObject);
            thread.publishText("Setting property "+stereotypeProperty+" for "+stereotypeName+" stereotype");
			object.setValue(stereotypeObject, stereotypeProperty, viaAssociationEnd);
			StereotypeUtil.outputMessage(thread, stereotypeName);
			thread.removeIndentation(1);
		}
		else
			thread.publishText(stereotypeName+" stereotype not applied");
		
	}
	
	/**
	 * Used to check if a property value of a stereotype is set for the UML Property element and to retrieve its value. 
	 * @param object            UML Property element for which to check and retrieve the value
	 * @param stereotypeObject  UML stereotype object for which the value should be retrieved 
	 * @param propertyName      name of the stereotype property for which the value should be retrieved
	 * @param thread            background worker thread, implementing the import functionality, used to
	 * output messages if the value has been set
	 * @return                  value retrieved for the corresponding stereotype property
	 */
	public static Object getProperty(Property object,Stereotype stereotypeObject,String stereotypeName,String propertyName,ProgressWorker thread){
		if(object.hasValue(stereotypeObject, propertyName))
		{
			thread.publishText(stereotypeName+" "+propertyName+" property is set");
			return object.getValue(stereotypeObject, propertyName);
		}else
			thread.publishText(stereotypeName+" "+propertyName+" property not set");
		return null;
	}
	
	/**
	 * Used to set the property value of the stereotype for the UML Property element.
	 * @param object            UML Property element for which to set the stereotype property value
	 * @param stereotypeObject  UML stereotype object for which to set the property value 
	 * @param propertyName      name of the stereotype property to set
	 * @param value             value to set for the stereotype property
	 * @param thread            background worker thread, implementing the export functionality, that is used to output
	 * messages of which property has been set
	 */
	public static void setProperty(Property object,Stereotype stereotypeObject,String propertyName,Object value,ProgressWorker thread){
		object.setValue(stereotypeObject, propertyName, value);
		StereotypeUtil.outputMessage(thread, propertyName);
	}
	

	/**
	 * For a UML Property element retrieves all the property values of the VisibleProperty stereotype, if it is applied
	 * to the UML Property element and sets the retrieved values to the corresponding attributes of the
	 * Kroki VisibleProperty object.    
	 * @param object        UML Property element for which to retrieve the property values of the VisibleProperty stereotype
	 * @param krokiObject   Kroki VisibleProperty object for which to set the retrieved values
	 * @param thread        background worker thread, implementing the import functionality, used to output messages
	 * of the current progress of the values that are being retrieved 
	 */
	public static void stereotypeVisiblePropertyImport(Property object,VisibleProperty krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_VISIBLE_PROPERTY_NAME;
		thread.publishText("Checking "+stereotypeName+" stereotype");
		thread.addIndentation();
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+stereotypeName);
		
		if(stereotypeObject!=null)
		{
			thread.publishText(stereotypeName+" stereotype applied");
			Object value;
			if((value=getProperty(object, stereotypeObject,stereotypeName, "columnLabel", thread))!=null)
			{
				krokiObject.setColumnLabel((String)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "displayFormat", thread))!=null)
			{
				krokiObject.setDisplayFormat((String)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "representative", thread))!=null)
			{
				krokiObject.setRepresentative((boolean)value);
			}else
				krokiObject.setRepresentative(false);
			if((value=getProperty(object, stereotypeObject,stereotypeName, "autoGo", thread))!=null)
			{
				krokiObject.setAutoGo((boolean)value);
			}else
				krokiObject.setAutoGo(false);
			if((value=getProperty(object, stereotypeObject,stereotypeName, "disabled", thread))!=null)
			{
				krokiObject.setDisabled((boolean)value);
			}else
				krokiObject.setDisabled(false);
			
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
	 * For a Kroki VisibleProperty object retrieves all the values corresponding to the property values 
	 * of the VisibleProperty stereotype and sets them to the UML Property element.
	 * @param profile      UML profile that defines a VisibleProperty stereotype
	 * @param object       UML Property element for which to set the property values of the VisibleProperty
	 * stereotype corresponding to the attribute values of the Kroki VisibleProperty object
	 * @param krokiObject  Kroki VisibleProperty object from which to retrieve the attribute values to be set to the
	 * stereotype property values of the UML Property element
	 * @param thread       background worker thread, implementing the export functionality, used to output messages of
	 * the current progress of the property values that are being set
	 */
	public static void stereotypeVisiblePropertyExport(Profile profile,Property object,VisibleProperty krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_VISIBLE_PROPERTY_NAME;
		
		Stereotype stereotypeObject=profile.getOwnedStereotype(stereotypeName);
		if(stereotypeObject!=null)
		{
			thread.publishText("Applaying stereotype "+stereotypeName);
			thread.addIndentation();
			object.applyStereotype(stereotypeObject);
			
			setProperty(object,stereotypeObject,"columnLabel",krokiObject.getColumnLabel(),thread);
			
			setProperty(object,stereotypeObject,"displayFormat",krokiObject.getDisplayFormat(),thread);
			
			setProperty(object,stereotypeObject,"representative",krokiObject.isRepresentative(),thread);
			
			setProperty(object,stereotypeObject,"autoGo",krokiObject.isAutoGo(),thread);
			
			setProperty(object,stereotypeObject,"disabled",krokiObject.isDisabled(),thread);
			
			//VisibleElement attributes
            setProperty(object,stereotypeObject,"label",krokiObject.getLabel(),thread);
			
			setProperty(object,stereotypeObject,"visible",krokiObject.isVisible(),thread);
			thread.removeIndentation(1);
		}
		else
			thread.publishText(stereotypeName+" stereotype could not be applied");
	}
	
	/**
	 * For a UML Property element retrieves all the property values of the Next stereotype, if it is applied
	 * to the UML Property element and sets the retrieved values to the corresponding attributes of the
	 * Kroki Next object.    
	 * @param object        UML Property element for which to retrieve the property values of the Next stereotype
	 * @param krokiObject   Kroki Next object for which to set the retrieved values
	 * @param thread        background worker thread, implementing the import functionality, used to output messages
	 * of the current progress of the values that are being retrieved 
	 */
	public static void stereotypeNextImport(Property object,Next krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_NEXT_NAME;
		thread.publishText("Checking "+stereotypeName+" stereotype");
		thread.addIndentation();
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+stereotypeName);
		
		if(stereotypeObject!=null)
		{
			thread.publishText(stereotypeName+" stereotype applied");
			Object value;
			if((value=getProperty(object, stereotypeObject,stereotypeName, "autoActivate", thread))!=null)
			{
				krokiObject.setAutoActivate((boolean)value);
			}else
				krokiObject.setAutoActivate(false);
			if((value=getProperty(object, stereotypeObject,stereotypeName, "displayIdentifier", thread))!=null)
			{
				krokiObject.setDisplayIdentifier((boolean)value);
			}else
				krokiObject.setDisplayIdentifier(false);
			if((value=getProperty(object, stereotypeObject,stereotypeName, "displayRepresentative", thread))!=null)
			{
				krokiObject.setDisplayRepresentative((boolean)value);
			}else
				krokiObject.setDisplayRepresentative(false);
			
			//VisibleAssociationEnd attributes
			if((value=getProperty(object, stereotypeObject,stereotypeName, "add", thread))!=null)
			{
				krokiObject.setAdd((boolean)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "update", thread))!=null)
			{
				krokiObject.setUpdate((boolean)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "copy", thread))!=null)
			{
				krokiObject.setCopy((boolean)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "delete", thread))!=null)
			{
				krokiObject.setDelete((boolean)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "search", thread))!=null)
			{
				krokiObject.setSearch((boolean)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "changeMode", thread))!=null)
			{
				krokiObject.setChangeMode((boolean)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "dataNavigation", thread))!=null)
			{
				krokiObject.setDataNavigation((boolean)value);
			}
			
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
	 * For a Kroki Next object retrieves all the values corresponding to the property values 
	 * of the Next stereotype and sets them to the UML Property element.
	 * @param profile      UML profile that defines a Next stereotype
	 * @param object       UML Property element for which to set the property values of the Next
	 * stereotype corresponding to the attribute values of the Kroki Next object
	 * @param krokiObject  Kroki Next object from which to retrieve the attribute values to be set to the
	 * stereotype property values of the UML Property element
	 * @param thread       background worker thread, implementing the export functionality, used to output messages of
	 * the current progress of the property values that are being set
	 */
	public static void stereotypeNextExport(Profile profile,Property object,Next krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_NEXT_NAME;
		
		Stereotype stereotypeObject=profile.getOwnedStereotype(stereotypeName);
		if(stereotypeObject!=null)
		{
			thread.publishText("Applaying stereotype "+stereotypeName);
			thread.addIndentation();
			object.applyStereotype(stereotypeObject);
			
			setProperty(object,stereotypeObject,"autoActivate",krokiObject.isAutoActivate(),thread);
			
			setProperty(object,stereotypeObject,"displayIdentifier",krokiObject.isDisplayIdentifier(),thread);
			
			setProperty(object,stereotypeObject,"displayRepresentative",krokiObject.isDisplayRepresentative(),thread);
			
			//VisibleAssociationEnd attributes
            setProperty(object,stereotypeObject,"add",krokiObject.isAdd(),thread);
			
			setProperty(object,stereotypeObject,"update",krokiObject.isUpdate(),thread);
			
			setProperty(object,stereotypeObject,"copy",krokiObject.isCopy(),thread);
			
			setProperty(object,stereotypeObject,"delete",krokiObject.isDelete(),thread);
			
			setProperty(object,stereotypeObject,"search",krokiObject.isSearch(),thread);
			
			setProperty(object,stereotypeObject,"changeMode",krokiObject.isChangeMode(),thread);
			
			setProperty(object,stereotypeObject,"dataNavigation",krokiObject.isDataNavigation(),thread);
			
			//VisibleElement attributes
            setProperty(object,stereotypeObject,"label",krokiObject.getLabel(),thread);
			
			setProperty(object,stereotypeObject,"visible",krokiObject.isVisible(),thread);
			thread.removeIndentation(1);
		}
		else
			thread.publishText(stereotypeName+" stereotype could not be applied");
	}
	
	/**
	 * For a UML Property element retrieves all the property values of the Zoom stereotype, if it is applied
	 * to the UML Property element and sets the retrieved values to the corresponding attributes of the
	 * Kroki Zoom object.    
	 * @param object        UML Property element for which to retrieve the property values of the Zoom stereotype
	 * @param krokiObject   Kroki Zoom object for which to set the retrieved values
	 * @param thread        background worker thread, implementing the import functionality, used to output messages
	 * of the current progress of the values that are being retrieved 
	 */
	public static void stereotypeZoomImport(Property object,Zoom krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_ZOOM_NAME;
		thread.publishText("Checking "+stereotypeName+" stereotype");
		thread.addIndentation();
		Stereotype stereotypeObject=object.getAppliedStereotype(StereotypeUtil.EUIS_DSL_PROFILE+stereotypeName);
		
		if(stereotypeObject!=null)
		{
			thread.publishText(stereotypeName+" stereotype applied");
			Object value;
			if((value=getProperty(object, stereotypeObject,stereotypeName, "combozoom", thread))!=null)
			{
				krokiObject.setCombozoom((boolean)value);
			}else
				krokiObject.setCombozoom(false);
			
			//VisibleAssociationEnd attributes
			if((value=getProperty(object, stereotypeObject,stereotypeName, "add", thread))!=null)
			{
				krokiObject.setAdd((boolean)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "update", thread))!=null)
			{
				krokiObject.setUpdate((boolean)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "copy", thread))!=null)
			{
				krokiObject.setCopy((boolean)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "delete", thread))!=null)
			{
				krokiObject.setDelete((boolean)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "search", thread))!=null)
			{
				krokiObject.setSearch((boolean)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "changeMode", thread))!=null)
			{
				krokiObject.setChangeMode((boolean)value);
			}
			if((value=getProperty(object, stereotypeObject,stereotypeName, "dataNavigation", thread))!=null)
			{
				krokiObject.setDataNavigation((boolean)value);
			}
			
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
	 * For a Kroki Zoom object retrieves all the values corresponding to the property values 
	 * of the Zoom stereotype and sets them to the UML Property element.
	 * @param profile      UML profile that defines a Zoom stereotype
	 * @param object       UML Property element for which to set the property values of the Zoom
	 * stereotype corresponding to the attribute values of the Kroki Zoom object
	 * @param krokiObject  Kroki Zoom object from which to retrieve the attribute values to be set to the
	 * stereotype property values of the UML Property element
	 * @param thread       background worker thread, implementing the export functionality, used to output messages of
	 * the current progress of the property values that are being set
	 */
	public static void stereotypeZoomExport(Profile profile,Property object,Zoom krokiObject,ProgressWorker thread){
		String stereotypeName=STEREOTYPE_ZOOM_NAME;
		
		Stereotype stereotypeObject=profile.getOwnedStereotype(stereotypeName);
		if(stereotypeObject!=null)
		{
			thread.publishText("Applaying stereotype "+stereotypeName);
			thread.addIndentation();
			object.applyStereotype(stereotypeObject);
			
			setProperty(object,stereotypeObject,"combozoom",krokiObject.isCombozoom(),thread);
			
			//VisibleAssociationEnd attributes
            setProperty(object,stereotypeObject,"add",krokiObject.isAdd(),thread);
			
			setProperty(object,stereotypeObject,"update",krokiObject.isUpdate(),thread);
			
			setProperty(object,stereotypeObject,"copy",krokiObject.isCopy(),thread);
			
			setProperty(object,stereotypeObject,"delete",krokiObject.isDelete(),thread);
			
			setProperty(object,stereotypeObject,"search",krokiObject.isSearch(),thread);
			
			setProperty(object,stereotypeObject,"changeMode",krokiObject.isChangeMode(),thread);
			
			setProperty(object,stereotypeObject,"dataNavigation",krokiObject.isDataNavigation(),thread);
			
			//VisibleElement attributes
            setProperty(object,stereotypeObject,"label",krokiObject.getLabel(),thread);
			
			setProperty(object,stereotypeObject,"visible",krokiObject.isVisible(),thread);
			thread.removeIndentation(1);
		}
		else
			thread.publishText(stereotypeName+" stereotype could not be applied");
	}
}
