package kroki.app.export;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.uml.UMLResourcesUtil;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.LiteralBoolean;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UML302UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.UML302UMLResource;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resource.XMI2UMLResource;

/**
 * Class that implements export functionality for exporting Kroki project to Eclipse UML diagram files. 
 * @author Zeljko Ivkovic
 *
 */
public class ExportProjectToEclipseUML {

	/**
	 * File in which Eclipse UML model is to be saved.
	 */
	private File file;
	/**
	 * Kroki project that is to be exported.
	 */
	private BussinesSubsystem project;
	/**
	 * Contains the Eclipse UML model that is to be saved to a file. 
	 */
	private Model model;
	/**
	 * List of primitive types to be assigned as types for the Eclipse UML Property sub elements of the Eclipse UML Class element.
	 */
	private List<PrimitiveType> primitiveTypes;
	/**
	 * Contains created Eclipse UML Class elements that correspond to the StandardPanel elements in the Kroki project.
	 */
	private Map<VisibleClass, Class> classesMap;
	/**
	 * Names for the zoom fields. Used to save names for zoom fields that are in a hierarchy of elements groups.
	 */
	private Map<Zoom, String> zoomFieldsNames;
	/**
	 * Saves zoom fields for which Association elements in Eclipse UML model will be created.
	 */
	private List<Zoom> zooms;
	/**
	 * Utility class used for changing names of Kroki elements to camel case notation of Eclipse UML elements.
	 */
	private NamingUtil cc;
	
	/**
	 * Constructor that creates an object for exporting
	 * Kroki project to a file with an Eclipse UML model.
	 * Receives a file object that represents the file where to export Kroki project and
	 * a Kroki project that should be exported.
	 * @param file  file where to export Kroki project
	 * @param project  Kroki project to be exported
	 */
	public ExportProjectToEclipseUML(File file,BussinesSubsystem project){
		this.file=file;
		this.project=project;
		
	}
	
	/**
	 * Called to export Kroki project to file that was received in the constructor.
	 * @throws Exception  if the file can not be created and if Eclipse UML model can not be saved to a file.
	 */
	public void executeExport() throws Exception{
		model = UMLFactory.eINSTANCE.createModel();
        model.setName(project.getLabel());
        primitiveTypes=new ArrayList<PrimitiveType>();
        classesMap=new HashMap<VisibleClass, Class>();
        zoomFieldsNames=new HashMap<Zoom,String>();
        zooms=new ArrayList<Zoom>();
        cc=new NamingUtil();
        KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Creating Eclipse UML model from project.", 0);
        extractBussinesSubsystem(project, model);
        createAssociations();
        KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Eclipse UML model created successfully.", 0);
        
	    // Create a resource
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				throw e1;
			}
	    
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Saving Eclipse UML model to file.", 0);
		try{
			saveFile(file.getAbsolutePath(), model);
	    	KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Exporting Eclipse UML diagram finished successfully.", 0);
		}catch(Exception e)
	    {
	    	KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText(e.getMessage(), 3);
	    	throw e;
	    }
	    
	}	
		
	/**
	 * Saves the Eclipse UML model to a file that is specified with the file path received.
	 * @param filePath  file path of the file where to save Eclipse UML model
	 * @param model  Eclipse UML model to be saved to a file
	 * @throws Exception  if resource for saving Eclipse UML model can not be created or if saving Eclipse UML model can not be
	 * finished successfully.
	 */
	protected void saveFile(String filePath,Model model) throws Exception{
		ResourceSet resSet = new ResourceSetImpl();
		
		UMLResourcesUtil.init(resSet);
	    Resource resource = resSet.createResource(URI.createFileURI(filePath),"");
	    
	    if(resource==null)throw new Exception("Resource can not be created for given file.");
	    resource.getContents().add(model);
	    try {
	      resource.save(Collections.EMPTY_MAP);
	    } catch (IOException e) {
	      e.printStackTrace();
	      throw new Exception("Error occurred while saving Eclipse UML model to file.");
	    }
	}
	
	/**
	 * For the given Kroki element BussinesSubsystem, iterates through BussinesSubsystem sub elements 
	 * and StandardPanel sub elements that it contains. For the BussinesSubsystem sub elements this
	 * method creates Eclipse UML Package elements and for StandardPanel sub elements this method
	 * creates Eclipse UML Class elements. Created elements are added to the received packageObject
	 * Eclipse UML Package element that was created for the received subsystem BussinesSubsystem element.
	 * @param subsystem  Kroki element BussinesSubsystem to be exported.
	 * @param packageObject  Eclipse UML model Package element that corresponds to the BussinesSubsystem element. 
	 */
	protected void extractBussinesSubsystem(BussinesSubsystem subsystem,Package packageObject){
		List<UmlPackage> subsystems=subsystem.nestedPackage();
		Package nestedPaskage;
		for(UmlPackage nestedSubsystem:subsystems)
		{
			if(nestedSubsystem instanceof BussinesSubsystem)
			{
				nestedPaskage=packageObject.createNestedPackage(cc.toCamelCaseIE(((BussinesSubsystem) nestedSubsystem).getLabel(),true));
				extractBussinesSubsystem((BussinesSubsystem) nestedSubsystem, nestedPaskage);
			}
		}
		boolean isAbstract=false;
		Class nestedClass;
		List<UmlType> nestedTypes=subsystem.ownedType();
		for(UmlType nestedType:nestedTypes)
		{
			if(nestedType instanceof StandardPanel)
			{				
				nestedClass=packageObject.createOwnedClass(cc.toCamelCaseIE(((StandardPanel) nestedType).getLabel(),true), isAbstract);
				classesMap.put((VisibleClass) nestedType, nestedClass);
				extractStandardPanel((StandardPanel) nestedType, nestedClass);
			}
		}
	}
	
	/**
	 * For the given Kroki element StandardPanel iterates through VisibleElement sub elements that it contains.
	 * Element VisibleElement can be of type Zoom, VisibleProperty, ElementsGroup and VisibleOperation. For the Zoom element this
	 * method adds them to the list of zoom elements for which are later created Eclipse UML Association elements, for
	 * VisibleProperty element it creates an Eclipse UML Property element, for ElementsGroup it iterates trough its sub elements and 
	 * for VisibleOperation sub elements this method creates Eclipse UML Operation elements. Created elements are added to the received
	 * classObject Eclipse UML Class element that corresponds to the received panel StandardPanel element. 
	 * @param panel  Kroki StandardPanel element to be exported.
	 * @param classObject  Eclipse UML Class element that corresponds to the received StandardPanel element.
	 */
	protected void extractStandardPanel(StandardPanel panel,Class classObject){
		VisibleProperty propertyObject;
		//(0-toolbar, 1-Properties, 2-Operations)
		ElementsGroup elements=(ElementsGroup) panel.getVisibleElementList().get(1);
		for(VisibleElement visibleElement:elements.getVisibleElementList())
		{
			if(visibleElement instanceof Zoom)
			{
				zooms.add((Zoom)visibleElement);
			}else if(visibleElement instanceof VisibleProperty)
			{
				propertyObject=(VisibleProperty)visibleElement;
				exportVisibleProperty(classObject,propertyObject,"");
			}else if(visibleElement instanceof ElementsGroup)
			{
				extractElementsGroup(classObject,(ElementsGroup)visibleElement, "");
			}
		}
		
		elements=(ElementsGroup) panel.getVisibleElementList().get(2);
		for(VisibleElement visibleElement:elements.getVisibleElementList())
		{
			if(visibleElement instanceof VisibleOperation)
			{
				 classObject.createOwnedOperation(cc.toCamelCaseIE(visibleElement.getLabel(),false), null, null);
			}else if(visibleElement instanceof ElementsGroup)
			{
				extractOperationsElementsGroup(classObject,(ElementsGroup) visibleElement,"");
			}
		}
	}
	
	/**
	 * For the given Kroki ElementsGroup element iterates through VisibleElement sub elements that it contains. ElementsGroup 
	 * element is used for creating a hierarchy of extra operations on a StandardPanel element. Element VisibleElement can be of type
	 * ElementsGroup and VisibleOperation. For ElementsGroup it iterates trough its sub elements and for VisibleOperation sub elements
	 * this method creates Eclipse UML Operation elements. Created elements are added to the received classObject Eclipse UML Class
	 * element that corresponds to the StandardPanel element that contains the received ElementsGroup element. Names of the created
	 * Operation elements contain a prefix that is sent to the method. 
	 * @param classObject  Eclipse UML Class element that corresponds to the StandardPanel element that contains this ElemenetsGroup element. 
	 * @param group  Kroki ElementsGroup element to be exported.
	 * @param prefix  prefix that will be added to the name of the created Operation elements for the VisibleOperation elements that the
	 * received ElementsGroup element contains.
	 */
	protected void extractOperationsElementsGroup(Class classObject,ElementsGroup group,String prefix) {
		String groupName=prefix+group.getLabel();
		for(VisibleElement visibleElement:group.getVisibleElementList())
		{
			if(visibleElement instanceof VisibleOperation)
			{
				 classObject.createOwnedOperation(cc.toCamelCaseIE(groupName+visibleElement.getLabel(),false), null, null);
				 //System.out.print("Operation:"+visibleElement.getLabel());
			}else if(visibleElement instanceof ElementsGroup)
			{
				extractOperationsElementsGroup(classObject,(ElementsGroup) visibleElement,groupName);
			}
		}
	}
	
	/**
	 * For the given Kroki element ElementsGroup iterates through VisibleElement sub elements that it contains. ElementsGroup 
	 * element is used for creating hierarchy of input fields on a StandardPanel element. Element VisibleElement can be of type
	 * Zoom, VisibleProperty and ElementsGroup. For the Zoom elements this method adds them to the list of zoom elements that is 
	 * later used for creating Eclipse UML Association elements, for VisibleProperty element it creates an Eclipse UML Property element and 
	 * for ElementsGroup it iterates trough its sub elements. Created elements are added to the received classObject Eclipse UML Class
	 * element that was created for the StandardPanel element that contains the ElementsGroup element. Names of the created Property elements
	 * contain a prefix that is sent to the method. Received prefix is added to the names of the Zoom elements and the created names are added to
	 * a map which will later be used for setting names of created Association elements.
	 * @param classObject  Eclipse UML Class element that corresponds to the StandardPanel element that contains this ElemenetsGroup element. 
	 * @param group  Kroki ElementsGroup element to be exported.
	 * @param prefix  prefix that will be added to the name of the created Property and Association elements for the elements that the
	 * received ElementsGroup element contains.  
	 */
	protected void extractElementsGroup(Class classObject,ElementsGroup group,String prefix) {
		String groupName=prefix+group.getLabel();
		VisibleProperty propertyObject;
		for(VisibleElement visibleElement:group.getVisibleElementList())
		{
			if(visibleElement instanceof Zoom)
			{
				zoomFieldsNames.put((Zoom)visibleElement, groupName+visibleElement.getLabel());
				zooms.add((Zoom)visibleElement);
			}else if(visibleElement instanceof VisibleProperty)
			{
				propertyObject=(VisibleProperty)visibleElement;
				exportVisibleProperty(classObject,propertyObject,groupName);
			}else if(visibleElement instanceof ElementsGroup)
			{
				extractElementsGroup(classObject,(ElementsGroup)visibleElement, groupName);
			}
		}
	}
	
	/**
	 * For the received VisibleProperty element creates an Eclipse UML Property element.
	 * Created Property element will be added to the received classObject element that corresponds to
	 * the StandardPanel element that contains the VisibleProperty element.
	 * For setting a type of the created Property element it creates a PrimitiveType element or a 
	 * Enumeration element depending on the type of the VisibleProperty element. Name of the created 
	 * Property element contains the received prefix.  
	 * @param classObject  Eclipse UML Class element that corresponds to the StandardPanel element that contains this ElemenetsGroup element. 
	 * @param property  Kroki VisibleProperty element to be exported.
	 * @param prefix  prefix that will be added to the name of the created Property and Association elements for the elements that the
	 * received ElementsGroup element contains.  
	 */
	protected void exportVisibleProperty(Class classObject,VisibleProperty property,String prefix){
		String propertyName=prefix+property.getLabel();
		if(property.getComponentType().equals(ComponentType.TEXT_FIELD))
		{
			PrimitiveType primitiveType=getPrimitiveType(property.getDataType());
			if(property.getDataType().equals("String"))
				createAttribute(classObject, propertyName, primitiveType, 0, 50);
			else
				createAttribute(classObject, propertyName, primitiveType, null, null);
		}else if(property.getComponentType().equals(ComponentType.COMBO_BOX))
		{
			Enumeration enumeration=createEnumeration(property);
			createAttribute(classObject, propertyName, enumeration, null, null);
		}else if(property.getComponentType().equals(ComponentType.CHECK_BOX))
		{
			PrimitiveType primitiveType=getPrimitiveType("Boolean");
			createAttribute(classObject, propertyName, primitiveType, null, null);
		}else if(property.getComponentType().equals(ComponentType.TEXT_AREA))
		{
			PrimitiveType primitiveType=getPrimitiveType("String");
			createAttribute(classObject, propertyName, primitiveType, 0, 300);
		}else 
		{
			PrimitiveType primitiveType=getPrimitiveType("String");
			createAttribute(classObject, propertyName, primitiveType, null, null);
		}
	}
	
	/**
	 * Used for creating Eclipse UML Enumeration element for the
	 * VisibleProperty that represents a list input field with defined values.
	 * Created Enumeration element can be used to set the type of the created
	 * Eclipse UML Property element.
	 * @param property  Kroki VisibleProperty element for which to create Enumeration element
	 * @return  created Enumeration Eclipse UML element
	 */
	protected Enumeration createEnumeration(VisibleProperty property){
		String enumName = property.getLabel();
		enumName += ((StandardPanel)property.umlClass()).getLabel();
		enumName=cc.toCamelCaseIE(enumName, false) + "Enum";
		//String enumClass = property.umlClass().name();
		//String enumProp = cc.toCamelCaseIE(property.getLabel(), true);
		String[] enumValues = property.getEnumeration().split(";");
		
		Enumeration enumeration=model.createOwnedEnumeration(enumName);
		for(String literal:enumValues)
			enumeration.createOwnedLiteral(literal);
		return enumeration;
	}
	
	/**
	 * Used for creating Eclipse UML PrimitiveType element that is used
	 * to set the type of the Eclipse UML Property element.
	 * It creates the PrimitiveType element with the name received.
	 * @param primitiveTypeName name of the PrimitiveType to be created
	 * @return created Eclipse UML PrimitiveType element.
	 */
	protected PrimitiveType getPrimitiveType(String primitiveTypeName){
		for(PrimitiveType helper:primitiveTypes)
		{
			if(helper.getName().equals(primitiveTypeName))
				return helper;
		}
		PrimitiveType primitiveType=model.createOwnedPrimitiveType(primitiveTypeName);
		primitiveTypes.add(primitiveType);
		return primitiveType;
	}
	
	/**
	 * Creates an Eclipse UML Property element with the name, type, lower and upper bounds received.
	 * Created Property element is added to the received classObject element. 
	 * @param classObject  Eclipse UML Class element to which the created Property element will be added.
	 * @param name  name of the Property element that will be created.
	 * @param type  type of the Property element that will be created.
	 * @param lowerBound  lower bound of the Property element that will be created.
	 * @param upperBound  upper bound of the Property element that will be created.
	 * @return  created Eclipse UML Property element.
	 */
	protected Property createAttribute(Class classObject,String name, Type type, Integer lowerBound, Integer upperBound) {
		Property attribute;
		if(lowerBound!=null || upperBound!=null)
            attribute = classObject.createOwnedAttribute(cc.toCamelCaseIE(name,false), type, lowerBound, upperBound);
		else
			attribute = classObject.createOwnedAttribute(cc.toCamelCaseIE(name,false), type);
            return attribute;
	}
	
	/**
	 * Creates Eclipse UML Association elements using the list of zoom fields.
	 * For every zoom field an Association element is created that connects Class elements
	 * that correspond to the Kroki StandardPanel elements that contain the zoom field and that 
	 * the zoom field references. Association element has cardinality of one end set to 0..* 
	 * and the other end has the cardinality set to 0..1.   
	 */
	protected void createAssociations() {
		VisibleClass activationPanel;
		VisibleClass targetPanel;
		Class activationClass;
		Class targetClass;
		String zoomFieldName;
		for(Zoom zoom:zooms)
		{
			zoomFieldName=zoomFieldsNames.get(zoom);
			if(zoomFieldName==null)
				zoomFieldName=zoom.getLabel();
			//zoomFieldName=cc.toCamelCaseIE(zoomFieldName, false);
			activationPanel=zoom.getActivationPanel();
			targetPanel=zoom.getTargetPanel();
			activationClass=classesMap.get(activationPanel);
			targetClass=classesMap.get(targetPanel);
			activationClass.createAssociation(true,AggregationKind.NONE_LITERAL, cc.toCamelCaseIE(zoomFieldName,false), 0,1, targetClass,
					false, AggregationKind.SHARED_LITERAL, cc.toCamelCaseIE(activationClass.getLabel()+zoomFieldName,false), 0, LiteralUnlimitedNatural.UNLIMITED);
                    //false, AggregationKind.NONE_LITERAL, cc.toCamelCaseIE("nextTo"+activationClass.getLabel(),false), 0, LiteralUnlimitedNatural.UNLIMITED);
			
		}
	}
}
