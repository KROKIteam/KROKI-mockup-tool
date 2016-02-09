package kroki.app.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.uml.IOutputMessage;
import kroki.app.utils.uml.ProgressWorker;
import kroki.app.utils.uml.UMLResourcesUtil;
import kroki.app.utils.uml.stereotypes.ClassStereotype;
import kroki.app.utils.uml.stereotypes.OperationStereotype;
import kroki.app.utils.uml.stereotypes.PackageStereotype;
import kroki.app.utils.uml.stereotypes.PropertyStereotype;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Next;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UML302UMLResource;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * Class that implements export functionality for exporting Kroki project to Eclipse UML diagram files. 
 * @author Zeljko Ivkovic
 *
 */
public class ExportProjectToEclipseUML extends ProgressWorker implements IOutputMessage{
 
	/**
	 * Name of the PrimitiveType element that is created as a type
	 * to be set for a UML Property element that corresponds to a 
	 * input field that represents a combo box but does not have
	 * any defined values that can be chosen in the combo box.
	 */
	public static final String ENUMERATION_ELEMENT_TYPE="EnumerationElementType";
	
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
	 * UML profile where all the UML defined types are saved.
	 */
	private Model primitiveTypesUML;
	/**
	 * UML profile containing stereotypes used during export.
	 */
	private Profile stereotypeProfile;
	/**
	 * Contains created Eclipse UML Class elements that correspond to the StandardPanel elements in the Kroki project.
	 */
	private Map<VisibleClass, Class> classesMap;
	/**
	 * Value used as a key value in a {@link #propertiesOperations} HashMap.
	 */
	private final String PROPERTY="Poperty";
	/**
	 * Value used as a key value in a {@link #propertiesOperations} HashMap.
	 */
	private final String OPERATION="Operation";
	/**
	 * HashMap that saves all the UML Property and Operation elements created for
	 * the corresponding VisibleProperty, Zoom, VisibleOperation and ElementsGroup
	 * elements in a StandardPanel element that corresponds to the 
	 * UML Class element that is used as the key value. Values are added during the
	 * export functionality and are used in the {@link #createElementGroupRefrences}
	 */
	private Map<Class, Map<String, Map<Object, Object>>> propertiesOperations;
	/**
	 * Names of all the ElementsGroup in the hierarchy that contain the corresponding Zoom field. Zoom fields that do not belong to any
	 * ElementsGroup but are directly on a StandardPanel should not be added to this map.
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
	 * Value that determines if the stereotypes should be applied to
	 * the corresponding UML elements during export or not.
	 */
	private boolean withStereotypes;
	/**
	 * Determines if the Eclipse UMl diagram should be in version 4.0 or
	 * version 3.0
	 */
	private boolean version4;
	
	/**
	 * Implementation that determines where the messages of the progress of the
	 * export are to be shown.
	 */
	private IOutputMessage outputMessage;
	
	public final static int MESSAGES_FOR_CLASS=1;
	public final static int MESSAGES_FOR_PROPERTY=2;
	public final static int MESSAGES_FOR_OPERATION=4;
	public final static int MESSAGES_FOR_ASSOCIATION=8;
	public final static int MESSAGES_FOR_ALL=MESSAGES_FOR_ASSOCIATION|MESSAGES_FOR_OPERATION|MESSAGES_FOR_PROPERTY|MESSAGES_FOR_CLASS;
	
	/**
	 * Which messages to show to the user about the progress
	 * of the export. Will be compared to the static attributes
	 * that start with MESSAGES_FOR.
	 */
	private int showMessagesFor;
	
	/**
	 * If true shows the dialogs with a corresponding message of the success of the export.
	 */
	private boolean showDialogs;
	
	/**
	 * Constructor that creates an object for exporting
	 * Kroki project to a file with an Eclipse UML model.
	 * Receives a file object that represents the file where to export Kroki project and
	 * a Kroki project that should be exported.
	 * @param file             file where to export Kroki project
	 * @param project          Kroki project to be exported
	 * @param withStereotypes  Kroki project to be exported with stereotypes if <code>true</code> or only
	 * the elementary UML diagram elements if <code>false</code>
	 * @param version4         if <code>true</code> export to version 4.0 Eclipse
	 * UML diagram, if <code>false</code> exports to version 3.0 Eclipse UML diagram  
	 */
	public ExportProjectToEclipseUML(File file,BussinesSubsystem project,boolean withStereotypes,boolean version4) {
		super();
		this.version4=version4;
		//synchronizationObject.wait();
		this.file=file;
		this.project=project;
		this.withStereotypes=withStereotypes;
	}
	
	/**
	 * Executes the {@link #exportToUMLDiagram} method.
	 * @throws Exception throws the same Exception of the {@link #exportToUMLDiagram} method.   
	 */
	@Override
	protected Void doInBackground() throws Exception {
		exportToUMLDiagram(this,MESSAGES_FOR_ALL,true);
		return null;
	}	

	/**
	 * Called to export Kroki project to a file that was received in the constructor.
	 *  
	 * @param outputMessages implementation that determines where the messages of the current progress
	 * will be shown.
	 * @param showMessagesFor which of the messages should be shown. Use the static attributes MESSAGES_FOR to
	 * determine which messages to show.
	 * @param showDialogs determine if a dialog should be shown with a corresponding message of the export success  
	 * 
	 * @throws Exception  if the file can not be created and if the Eclipse UML model can not be saved to a file
	 */
	public void exportToUMLDiagram(IOutputMessage outputMessages,int showMessagesFor,boolean showDialogs) throws Exception{
		this.showMessagesFor=showMessagesFor;
		this.outputMessage=outputMessages;
		this.showDialogs=showDialogs;
		publishText("Exporting project to file "+file.getAbsolutePath());
		model = UMLFactory.eINSTANCE.createModel();
        model.setName(project.getLabel());
        
        ResourceSet resSet = new ResourceSetImpl();
		
		UMLResourcesUtil.init(resSet);
	    Resource resource = resSet.createResource(URI.createFileURI(file.getAbsolutePath()),"");
	    
	    if(resource==null)throw new Exception("Resource can not be created for given file.");
	    resource.getContents().add(model);
	    
        primitiveTypes=new ArrayList<PrimitiveType>();
        primitiveTypesUML=this.<Model>loadPackage(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI),false);
        if(withStereotypes)
        {
        	File f = new File(".");
            String appPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
            if(!KrokiMockupToolApp.getInstance().isBinaryRun()) {
                appPath = appPath.substring(0, appPath.length()-16);
            }
	        String stereotypeFilePath=  appPath + "KrokiMockupTool" + File.separator + "libECore" + File.separator + "EUISDSLProfile" + 
	        		File.separator + "EUISDSLProfile.profile.uml";
	        System.out.println("EUISDSL MODEL: " + stereotypeFilePath);
	        //currentPath+"\\libECore\\EUISDSLProfile\\EUISDSLProfile.profile.uml";
	        File euisDSLprofile=new File(stereotypeFilePath);
	        Path euisDSLTargetPath=file.toPath().getParent().resolve(euisDSLprofile.toPath().getFileName());

	        Files.copy(euisDSLprofile.toPath(), euisDSLTargetPath,StandardCopyOption.REPLACE_EXISTING);
	        stereotypeProfile=this.<Profile>loadPackage(URI.createFileURI(euisDSLTargetPath.toString()), true);
	        //stereotypeProfile.define();
	        model.applyProfile(stereotypeProfile);
        }
        
        classesMap=new HashMap<VisibleClass, Class>();
        propertiesOperations=new HashMap<Class, Map<String,Map<Object,Object>>>();
        zoomFieldsNames=new HashMap<Zoom,String>();
        zooms=new ArrayList<Zoom>();
        cc=new NamingUtil();
        publishText("Creating Eclipse UML model from project.");
        extractBussinesSubsystem(project, model);
        createAssociations();
        createElementGroupRefrences();
        publishText("Eclipse UML model created successfully.");
        
	    // Create a resource
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				throw e1;
			}
	    
		publishText("Saving Eclipse UML model to file.");
		try{
			resource.save(null);
			if(!version4)
				changeVersion(file);
			publishText("Exporting Eclipse UML diagram finished successfully.");
			if(showDialogs)
				JOptionPane.showMessageDialog(getFrame(), "Exporting Eclipse UML diagram finished successfully.");
		}catch(Exception e)
	    {
	    	throw e;
	    }
	}
	
	
	/**
	 * If the background process was interrupted or there was an error while
	 * exporting a Kroki project, a corresponding message will be displayed
	 * and the <code>done</code> method from the inherited class is called.
	 */
	@Override
	public void done(){
		try {
			get();
		} catch (InterruptedException | ExecutionException e) {
			showErrorMessage(e);
		} catch (CancellationException e) {
			//When cancel method is called
			//e.printStackTrace();
			if(showDialogs)
				JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), 
					"Exporting Kroki project has been canceled");
			publishErrorText("Export has been canceled");
		} catch (IllegalArgumentException e) {
			publishErrorText("Internal error ocured contact developer");
		} catch(NullPointerException e) {
			publishErrorText("Internal error ocured contact developer");
		} catch (Exception e) {
			showErrorMessage(e);
		}
		super.done();
	}
	
	/**
	 * Shows an error message by calling the
	 * methods {@link #publishText} and
	 * {@link #showError}.
	 * @param e  Exception that contains the error
	 * message
	 */
	private void showErrorMessage(Exception e){
		//Correct the error then try again
		publishErrorText("Error happened while exporting");
		publishErrorText(exceptionMessage(e));
		if(showDialogs)
			showError(exceptionMessage(e), "Error while exporting");
		e.printStackTrace();
	}
	
	@Override
	public void publishText(String text) {
		if(outputMessage!=null)
			outputMessage.publishInfoMessage(text);
	}
	
	@Override
	public void publishErrorText(String text) {
		if(outputMessage!=null)
			outputMessage.publishErrorMessage(text);
	}
	
	@Override
	public void publishWarning(String text) {
		if(outputMessage!=null)
			outputMessage.publishWarningMessage(text);
	}
	
	@Override
	public void publishInfoMessage(String text) {
		super.publishText(text);
	}

	
	@Override
	public void publishErrorMessage(String text) {
		super.publishErrorText(text);
	}

	@Override
	public void publishWarningMessage(String text) {
		super.publishWarning(text);
	}
	
	/**
	 * Used for loading a UML package that contains the defined UML types
	 * and the UML profile that defines the stereotypes used during
	 * export if the {@link #withStereotypes} is set to <code>true</code>.
	 * @param uri         {@link URI} object that represents the path to the
	 * UML files to load
	 * @param profile     <code>true</code> if the UML file that will be
	 * loaded contains  {@link Profile} as the root element, otherwise it
	 * will be loaded as a {@link Model}. Depending on the type of the
	 * root element generic type representing the type of the object that
	 * will be returned should also be set accordingly
	 * @return             root object of the UML file to load. Type of the return
	 * object depends of the value set for the generic parameter and should 
	 * be set accordingly to the profile parameter
	 * 
	 * @throws Exception   if the file with the received path could not be loaded 
	 */
	protected <T extends Object> T loadPackage(URI uri,boolean profile) throws Exception{
        //URI fileUri = URI.createFileURI(filePath);
		
        ResourceSet resourceSet = new ResourceSetImpl();
        UMLResourcesUtil.init(resourceSet);
        try{
		    Resource resource = resourceSet.getResource(uri, true);
		    if(profile)
		    	return (T) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PROFILE);
		    else
		    	return (T) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.MODEL);
        }catch(Exception e){
			e.printStackTrace();
			throw new Exception("Error while loading necessary files for export.");
        }
	}
	
	/**
	 * For the given Kroki element BussinesSubsystem, iterates through BussinesSubsystem sub elements 
	 * and StandardPanel sub elements that it contains. For the BussinesSubsystem sub elements this
	 * method creates Eclipse UML Package elements and for StandardPanel sub elements this method
	 * creates Eclipse UML Class elements. Created elements are added to the received packageObject
	 * Eclipse UML Package element that was created for the received subsystem BussinesSubsystem element.
	 * If the parameter {@link #withStereotypes} is set to <code>true</code> it also applies corresponding stereotypes
	 * to the created UML Class elements.
	 * @param subsystem      Kroki element BussinesSubsystem to be exported
	 * @param packageObject  Eclipse UML model Package element that corresponds to the BussinesSubsystem element
	 */
	protected void extractBussinesSubsystem(BussinesSubsystem subsystem,Package packageObject){
		addIndentation();
		publishText("Extracting "+BussinesSubsystem.class.getSimpleName()+" "+packageObject.getName());
		List<UmlPackage> subsystems=subsystem.nestedPackage();
		Package nestedPaskage;
		for(UmlPackage nestedSubsystem:subsystems)
		{
			if(nestedSubsystem instanceof BussinesSubsystem)
			{
				nestedPaskage=packageObject.createNestedPackage(cc.toCamelCaseIE(((BussinesSubsystem) nestedSubsystem).getLabel(),true));
				if(withStereotypes){
					PackageStereotype.stereotypeBusinessSbsystemExport(stereotypeProfile, nestedPaskage, (BussinesSubsystem) nestedSubsystem, this);
				}
				publishText("Created UML Package "+nestedPaskage.getName());
				extractBussinesSubsystem((BussinesSubsystem) nestedSubsystem, nestedPaskage);
			}
		}
		boolean isAbstract=false;
		Class nestedClass;
		List<UmlType> nestedTypes=subsystem.ownedType();
		Map<Object, Object> objectsMap;
		Map<String, Map<Object, Object>> propertiesOperationsPartMap;
		
		for(UmlType nestedType:nestedTypes)
		{
			if(nestedType instanceof StandardPanel)
			{				
				addIndentation();
				nestedClass=packageObject.createOwnedClass(cc.toCamelCaseIE(((StandardPanel) nestedType).getLabel(),true), isAbstract);
				if((showMessagesFor&MESSAGES_FOR_CLASS)>0)
					publishText("Created UML Class "+nestedClass.getName());
				if(withStereotypes)
				{					
					ClassStereotype.stereotypeStandardPanelExport(stereotypeProfile, nestedClass, (StandardPanel) nestedType, this);
				}
				classesMap.put((VisibleClass) nestedType, nestedClass);
				
				objectsMap=new HashMap<Object, Object>();
				propertiesOperationsPartMap=new HashMap<String, Map<Object,Object>>();
				propertiesOperationsPartMap.put(PROPERTY, objectsMap);
				objectsMap=new HashMap<Object, Object>();
				propertiesOperationsPartMap.put(OPERATION, objectsMap);
				propertiesOperations.put(nestedClass,propertiesOperationsPartMap);
				
				extractStandardPanel((StandardPanel) nestedType, nestedClass);
				removeIndentation(1);
			}
		}
		removeIndentation(1);
	}
	
	/**
	 * For the given Kroki element StandardPanel iterates through VisibleElement sub elements that it contains.
	 * Element VisibleElement can be of type Zoom, VisibleProperty, ElementsGroup and VisibleOperation. For the Zoom 
	 * element this method adds them to the list of zoom elements for which are later created Eclipse UML Association 
	 * elements, for a VisibleProperty element it creates an Eclipse UML Property element, for a VisibleOperation 
	 * element this method creates an Eclipse UML Operation element and for a ElementsGroup element it calls a 
	 * {@link #extractElementsGroup} method if it is in the list of VisibleProperty elements or it calls 
	 * {@link #extractOperationsElementsGroup} if it is in the group of VisibleOperation elements. Created elements are
	 * added to the received classObject Eclipse UML Class element that corresponds to the received panel StandardPanel 
	 * element.</br>
	 * If the parameter {@link #withStereotypes} is set to <code>true</code> it also applies corresponding stereotypes
	 * to the created UML Property elements and UML Operation elements.
	 * @param panel        Kroki StandardPanel element to be exported
	 * @param classObject  Eclipse UML Class element that corresponds to the received StandardPanel element
	 */
	protected void extractStandardPanel(StandardPanel panel,Class classObject){
		VisibleProperty propertyObject;
		//(0-toolbar, 1-Properties, 2-Operations)
		ElementsGroup elements=(ElementsGroup) panel.getVisibleElementList().get(1);
		Property createdProperty;
		for(VisibleElement visibleElement:elements.getVisibleElementList())
		{
			if(visibleElement instanceof Zoom)
			{
				zooms.add((Zoom)visibleElement);
				if(withStereotypes)
					propertiesOperations.get(classObject).get(PROPERTY).put(visibleElement,null);
			}else if(visibleElement instanceof VisibleProperty)
			{
				propertyObject=(VisibleProperty)visibleElement;
				createdProperty=exportVisibleProperty(classObject,propertyObject,"");
				if(withStereotypes)
					propertiesOperations.get(classObject).get(PROPERTY).put(visibleElement,createdProperty);
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
				createOperation(classObject, "",(VisibleOperation) visibleElement);
			}else if(visibleElement instanceof ElementsGroup)
			{
				extractOperationsElementsGroup(classObject,(ElementsGroup) visibleElement,"");
			}
		}
	}
	
	/**
	 * For the given Kroki ElementsGroup element iterates through VisibleElement sub elements that it contains. ElementsGroup 
	 * element is used for creating a hierarchy of extra operations on a StandardPanel element. Element VisibleElement can be of type
	 * ElementsGroup and VisibleOperation. For ElementsGroup element it again calls this method <code>extractOperationsElementsGroup</code>
	 * and for a VisibleOperation element it calls the {@link #createOperation} method that will create an Eclipse UML Operation element.
	 * </br>
	 * If the attribute {@link #withStereotypes} is set to <code>true</code> for the ElementsGroup it creates a UML Operation element,
	 * that will have a ElementsGroupOperation stereotype applied, and adds it to the {@link #propertiesOperations} hash map.
	 * @param classObject   Eclipse UML Class element that corresponds to the StandardPanel element that contains the ElemenetsGroup that
	 * is being exported
	 * @param group         Kroki ElementsGroup element to be exported
	 * @param prefix        names of all the other ElementsGroup elements that contain the ElementsGroup element that is being exported
	 */
	protected void extractOperationsElementsGroup(Class classObject,ElementsGroup group,String prefix) {
		String groupName=(prefix.isEmpty())?group.getLabel():prefix+" "+group.getLabel();
		Operation createdOperation;
		addIndentation();
		for(VisibleElement visibleElement:group.getVisibleElementList())
		{
			if(visibleElement instanceof VisibleOperation)
			{
				createOperation(classObject, groupName,(VisibleOperation) visibleElement);
			}else if(visibleElement instanceof ElementsGroup)
			{
				extractOperationsElementsGroup(classObject,(ElementsGroup) visibleElement,groupName);
			}
		}
		if(withStereotypes)
		{
			
			createdOperation=classObject.createOwnedOperation(cc.toCamelCaseIE(group.getLabel(),false), null, null);
			if((showMessagesFor&MESSAGES_FOR_OPERATION)>0)
				publishText("Created UML Operation for group "+group.getLabel());
			
			OperationStereotype.stereotypeElementsGroupOperationExport(stereotypeProfile, createdOperation, group, this);
			
			propertiesOperations.get(classObject).get(OPERATION).put(group,createdOperation);
		}
		removeIndentation(1);
	}
	
	/**
	 * Creates a UML Operation element, for the received VisibleOperation element, and sets its name so that it contains names of
	 * all the ElementsGroup elements in the hierarchy that contain the received VisibleOperation element.
	 * </br>
	 * If the parameter {@link #withStereotypes} is set to <code>true</code> it also applies corresponding stereotypes
	 * to the created UML Operation element, adds it to the {@link #propertiesOperations} hash map and the name of the created UML Operation element only contains the label of the
	 * VisibleOperation element, not the names of the ElementsGroup elements in the hierarchy that contain the received VisibleOperation
	 * element.
	 * @param classObject  Eclipse UML Class element to which the created Operation element should be added
	 * @param prefix       names of the ElementsGroup elements in the hierarchy that contain this VisibleOperation element
	 * @param krokiObject  VisibleOperation element for which to create an Eclipse UML Operation element
	 * @return             created Eclipse UML Operation element for the received VisibleOperation element
	 */
	protected Operation createOperation(Class classObject, String prefix,VisibleOperation krokiObject){
		String operationName;
		if(withStereotypes)
			operationName=krokiObject.getLabel();
		else
			operationName=(prefix.isEmpty())?krokiObject.getLabel():prefix+" "+krokiObject.getLabel();
		Operation object=classObject.createOwnedOperation(cc.toCamelCaseIE(operationName,false), null, null);
		if((showMessagesFor&MESSAGES_FOR_OPERATION)>0)
			publishText("Created UML Operation for "+krokiObject.getLabel());
		addIndentation();
		if(withStereotypes)
			if(krokiObject instanceof Report)
			{
				OperationStereotype.stereotypeReportExport(stereotypeProfile, object, (Report) krokiObject, this);
			}else
			{
				OperationStereotype.stereotypeTransactionExport(stereotypeProfile, object, (Transaction) krokiObject, this);
			}
		if(withStereotypes)
		{
			propertiesOperations.get(classObject).get(OPERATION).put(krokiObject,object);
		}
		removeIndentation(1);
		return object;
	}
	/**
	 * For the given Kroki element ElementsGroup iterates through VisibleElement sub elements that it contains. ElementsGroup 
	 * element is used for creating hierarchy of input fields on a StandardPanel element. Element VisibleElement can be of type
	 * Zoom, VisibleProperty and ElementsGroup. For the Zoom element this method adds it to the list of zoom elements that is 
	 * later used for creating Eclipse UML Association elements, for VisibleProperty element it calls the {@link #exportVisibleProperty}
	 * methodcreates and for ElementsGroup it again calls this method <code>extractElementsGroup</code>. For a Zoom element it adds the
	 * name of all the ElementsGroup elements in the hierarchy, that contain the Zoom element, to a map {@link #zoomFieldsNames}. 
	 * </br>
	 * If the attribute {@link #withStereotypes} is set to <code>true</code> for the ElementsGroup element it creates a UML Property element,
	 * that will have a ElementsGroupProperty stereotype applied, and adds it to the {@link #propertiesOperations} hash map.
	 * @param classObject    Eclipse UML Class element that corresponds to the StandardPanel element that contains the ElemenetsGroup element
	 * that is being exported
	 * @param group          Kroki ElementsGroup element to be exported
	 * @param prefix         names of all the other ElementsGroup elements that contain the ElementsGroup element that is being exported  
	 */
	protected void extractElementsGroup(Class classObject,ElementsGroup group,String prefix) {
		String groupName=(prefix.isEmpty())?group.getLabel():prefix+" "+group.getLabel();
		VisibleProperty propertyObject;
		Property createdProperty;
		addIndentation();
		for(VisibleElement visibleElement:group.getVisibleElementList())
		{
			createdProperty=null;
			if(visibleElement instanceof Zoom)
			{
				zoomFieldsNames.put((Zoom)visibleElement, groupName);
				zooms.add((Zoom)visibleElement);
				if(withStereotypes)
					propertiesOperations.get(classObject).get(PROPERTY).put(visibleElement,null);
			}else if(visibleElement instanceof VisibleProperty)
			{
				propertyObject=(VisibleProperty)visibleElement;
				createdProperty = exportVisibleProperty(classObject,propertyObject,groupName);
				if(withStereotypes)
					propertiesOperations.get(classObject).get(PROPERTY).put(visibleElement,createdProperty);
			}else if(visibleElement instanceof ElementsGroup)
			{
				extractElementsGroup(classObject,(ElementsGroup)visibleElement, groupName);
			}				
		}
		if(withStereotypes)
		{
			createdProperty=classObject.createOwnedAttribute(cc.toCamelCaseIE(group.getLabel(),false), null);
			if((showMessagesFor&MESSAGES_FOR_PROPERTY)>0)
				publishText("Created UML Property for group "+group.getLabel());
			PropertyStereotype.stereotypeElementsGroupOperationExport(stereotypeProfile, createdProperty, group, this);
			
			propertiesOperations.get(classObject).get(PROPERTY).put(group,createdProperty);			
		}
		removeIndentation(1);
	}
	
	/**
	 * For the received VisibleProperty element creates an Eclipse UML Property element by calling
	 * {@link #createAttribute} method.
	 * Created Property element will be added to the received classObject element that corresponds to
	 * the StandardPanel element that contains the VisibleProperty element.
	 * For creating a object that will be set as the type of the created Property element it calls the 
	 * {@link #getPrimitiveType} method or the {@link #createEnumeration} method depending on what kind
	 * of an input field does the VisibleProperty element represent.
	 * If the {@link #createEnumeration} method returns <code>null</code> the {@link #getPrimitiveType} method will
	 * be called to create a PrimitiveType, with name that is saved in the attribute {@link #ENUMERATION_ELEMENT_TYPE},
	 * that will be set as the type of the UML Property element.
	 * Name of the UML Property element will contain the names of all the ElementsGroup elements in
	 * the hierarchy that contain the received VisibleOperation element.
	 * </br>
	 * If the attribute {@link #withStereotypes} is set to <code>true</code> sets the corresponding stereotypes to the
	 * created UML Property element and the name of the created Property element is set only to the label of the VisibleProperty
	 * element and will not contain names of all the ElementsGroup elements in the hierarchy that contain the received
	 * VisibleOperation element.
	 * @param classObject   Eclipse UML Class element that corresponds to the StandardPanel element that contains this
	 * VisibleProperty element
	 * @param property      Kroki VisibleProperty element to be exported
	 * @param prefix        names of the ElementsGroup elements in the hierarchy that contain this VisibleProperty element
	 * @return              created UML Property element  
	 */
	protected Property exportVisibleProperty(Class classObject,VisibleProperty property,String prefix){
		Property propertyUML;
		String propertyName;
		
		if(withStereotypes)
			propertyName=property.getLabel();
		else
			propertyName=(prefix.isEmpty())?property.getLabel():prefix+" "+property.getLabel();
		if(property.getComponentType().equals(ComponentType.TEXT_FIELD))
		{
			PrimitiveType primitiveType=getPrimitiveType(property.getDataType());
			if(property.getDataType().equals("String"))
				propertyUML = createAttribute(classObject, propertyName, primitiveType, 0, 50);
			else
				propertyUML = createAttribute(classObject, propertyName, primitiveType, null, null);
		}else if(property.getComponentType().equals(ComponentType.COMBO_BOX))
		{
			Enumeration enumeration=createEnumeration(property);
			if(enumeration==null)
			{
				PrimitiveType enumType=getPrimitiveType(ENUMERATION_ELEMENT_TYPE);
				propertyUML = createAttribute(classObject, propertyName, enumType, null, null);
			}else
			{
				propertyUML = createAttribute(classObject, propertyName, enumeration, null, null);
			}
			/*
			if(withStereotypes)
				PropertyStereotype.stereotypeComboBoxComponent(stereotypeProfile, propertyUML, this);
				*/
		}else if(property.getComponentType().equals(ComponentType.CHECK_BOX))
		{
			PrimitiveType primitiveType=getPrimitiveType("Boolean");
			propertyUML = createAttribute(classObject, propertyName, primitiveType, null, null);
		}else if(property.getComponentType().equals(ComponentType.TEXT_AREA))
		{
			PrimitiveType primitiveType=getPrimitiveType("String");
			propertyUML = createAttribute(classObject, propertyName, primitiveType, 0, 300);
		}else 
		{
			PrimitiveType primitiveType=getPrimitiveType("String");
			propertyUML = createAttribute(classObject, propertyName, primitiveType, null, null);
		}
		if((showMessagesFor&MESSAGES_FOR_PROPERTY)>0)
			publishText("Created UML Property for "+property.getLabel());
		addIndentation();
		if(withStereotypes)
		{
			PropertyStereotype.stereotypeVisiblePropertyExport(stereotypeProfile, propertyUML, property, this);
		}
		removeIndentation(1);
		return propertyUML;
	}
	
	/**
	 * Used for creating Eclipse UML Enumeration element for the
	 * VisibleProperty that represents a list input field with defined values.
	 * Created Enumeration element can be used to set the type of the created
	 * Eclipse UML Property element. If the received VisibleProperty element does
	 * not contain any defined values this method will return <code>null</code>.
	 * @param property   Kroki VisibleProperty element for which to create Enumeration element
	 * @return           created Enumeration Eclipse UML element or <code>null</code> if the 
	 * VisibleProperty does not contain any defined values.
	 */
	protected Enumeration createEnumeration(VisibleProperty property){
		String enumName = property.getLabel();
		enumName += ((StandardPanel)property.umlClass()).getLabel();
		enumName=cc.toCamelCaseIE(enumName, false) + "Enum";
		//String enumClass = property.umlClass().name();
		//String enumProp = cc.toCamelCaseIE(property.getLabel(), true);
		if(property.getEnumeration()!=null)
		{
			String[] enumValues = property.getEnumeration().split(";");
			
			Enumeration enumeration=model.createOwnedEnumeration(enumName);
			for(String literal:enumValues)
				enumeration.createOwnedLiteral(literal);
			return enumeration;
		}
		return null;
	}
	
	/**
	 * Used for getting Eclipse UML PrimitiveType element that is used
	 * to set the type of the Eclipse UML Property element.
	 * It first checks if there is a defined UML type with the
	 * name received if there is not it creates the PrimitiveType element
	 * with the name received.
	 * @param primitiveTypeName   name of PrmitiveType that is needed
	 * @return                    UML defined PrimitiveType or a created
	 * Eclipse UML PrimitiveType element if there is not a PrimitiveType
	 * defined with a received name
	 */
	protected PrimitiveType getPrimitiveType(String primitiveTypeName){
		for(PrimitiveType helper:primitiveTypes)
		{
			if(helper.getName().equals(primitiveTypeName))
				return helper;
		}
		//PrimitiveType primitiveType=model.createOwnedPrimitiveType(primitiveTypeName);
		PrimitiveType primitiveType=(PrimitiveType)primitiveTypesUML.getOwnedType(primitiveTypeName);
		if(primitiveType!=null)
			model.createElementImport(primitiveType);
		else
			primitiveType=model.createOwnedPrimitiveType(primitiveTypeName);
		primitiveTypes.add(primitiveType);
		return primitiveType;
	}
	
	/**
	 * Creates an Eclipse UML Property element with the name, type, lower and upper bounds received.
	 * Created Property element is added to the received classObject element. 
	 * @param classObject  Eclipse UML Class element to which the created Property element will be added
	 * @param name         name of the Property element that will be created
	 * @param type         type of the Property element that will be created
	 * @param lowerBound   lower bound of the Property element that will be created
	 * @param upperBound   upper bound of the Property element that will be created
	 * @return             created Eclipse UML Property element
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
	 * </br>
	 * If the attribute {@link #withStereotypes} is set to <code>true</code> the UML Property
	 * element that represents the end of the Association element and that has 0..1 cardinality
	 * set also gets the corresponding stereotypes set by calling the {@link #setStereotypeForZoom}
	 * method.
	 */
	protected void createAssociations() {
		VisibleClass activationPanel;
		VisibleClass targetPanel;
		Class activationClass;
		Class targetClass;
		String zoomFieldName;
		if((showMessagesFor&MESSAGES_FOR_ASSOCIATION)>0)
			publishText("Generating associations:");
		addIndentation();
		for(Zoom zoom:zooms)
		{
			if((showMessagesFor&MESSAGES_FOR_ASSOCIATION)>0)
				publishText("Zoom "+zoom.getLabel());
			if(withStereotypes)
				zoomFieldName=zoom.getLabel();
			else
			{
				zoomFieldName=zoomFieldsNames.get(zoom);
				if(zoomFieldName==null)
					zoomFieldName=zoom.getLabel();
				else
					zoomFieldName+=" "+zoom.getLabel();
			}
			//zoomFieldName=cc.toCamelCaseIE(zoomFieldName, false);
			activationPanel=zoom.getActivationPanel();
			targetPanel=zoom.getTargetPanel();
			activationClass=classesMap.get(activationPanel);
			targetClass=classesMap.get(targetPanel);
			Next next=(Next)zoom.opposite();
			String nextName=activationClass.getLabel()+zoomFieldName;
			boolean nextNavigable=false;
			if(next!=null)
			{
				nextName=next.getLabel();
				nextNavigable=true;
			}
			Association association=activationClass.createAssociation(true,AggregationKind.NONE_LITERAL, cc.toCamelCaseIE(zoomFieldName,false), 0,1, targetClass,
					nextNavigable, AggregationKind.NONE_LITERAL, cc.toCamelCaseIE(nextName,false), 0, LiteralUnlimitedNatural.UNLIMITED);
                    //false, AggregationKind.NONE_LITERAL, cc.toCamelCaseIE("nextTo"+activationClass.getLabel(),false), 0, LiteralUnlimitedNatural.UNLIMITED);
			if(withStereotypes)
			{
				Property first=association.getAllAttributes().get(0);
				Property second=association.getAllAttributes().get(1);
				if(first.getUpper()==LiteralUnlimitedNatural.UNLIMITED)
				{
					if(second.getUpper()==LiteralUnlimitedNatural.UNLIMITED)
						System.out.println("Many-to-Many not suported");
					else{
						setStereotypeForZoom(activationClass,second,zoom);
						if(next!=null)
							setStereotypeForNext(targetClass, first, next);
					}
				}
				else{
					if(second.getUpper()==LiteralUnlimitedNatural.UNLIMITED)
					{
						setStereotypeForZoom(activationClass,first,zoom);
						if(next!=null)
							setStereotypeForNext(targetClass, second, next);
					}
					else
						System.out.println("One-to-One not suported");
				}
			}
		}
		removeIndentation(1);
		if((showMessagesFor&MESSAGES_FOR_ASSOCIATION)>0)
			publishText("Finished generating associations");
	}
	
	/**
	 * Sets the stereotypes for the received UML Property that 
	 * represents the end of an UML Association element created
	 * for a zoom field and adds the received UML Property element
	 * to the {@link #propertiesOperations} hash map.
	 * @param classObject   UML Class element that was created for the
	 * StandardPanel element that contains the zoom field for which the
	 * UML Association element was created that has the received Property
	 * as one of the ends
	 * @param object       UML Property for which the stereotypes are to
	 * be set
	 * @param krokiObject  zoom field object for which the UML Association
	 * was created that has the received Property element as one of the ends
	 */
	public void setStereotypeForZoom(Class classObject,Property object,Zoom krokiObject){
		PropertyStereotype.stereotypeZoomExport(stereotypeProfile, object, krokiObject, this);
		propertiesOperations.get(classObject).get(PROPERTY).put(krokiObject,object);
	}
	
	
	/**
	 * Sets the stereotypes for the received UML Property that 
	 * represents the end of an UML Association element created
	 * for a next element.
	 * @param classObject   UML Class element that was created for the
	 * StandardPanel element that contains the next element that corresponds
	 * to the received Property that is one of the ends of the UML Association
	 * element
	 * @param object       UML Property for which the stereotypes are to
	 * be set
	 * @param krokiObject  next element object that corresponds
	 * to the received Property that is one of the ends of the UML Association
	 * element
	 */
	public void setStereotypeForNext(Class classObject,Property object,Next krokiObject){
		PropertyStereotype.stereotypeNextExport(stereotypeProfile, object, krokiObject, this);
	}
	/**
	 * For every UML Property and Operation element that was created for
	 * a Kroki ElementsGroup element sets the value of the nestedElements
	 * property.
	 * Property nestedElements that is a part of the
	 * ElementsGroupProperty stereotype saves all the UML Property
	 * elements that where created for the VisibleProperty, Zoom
	 * and ElementsGroup elements that are contained in a ElementGroup
	 * element for which a UML Property was created and the value
	 * of the nestedElements property is being set.
	 * Property nestedElements that is a part of the
	 * ElementsGroupOperation stereotype saves all the UML Operation
	 * elements that correspond to the VisibleOperation and
	 * ElementsGroup elements that are contained in a ElementGroup
	 * element for which a UML Operation was created and the
	 * value of the nestedElements property is being set.
	 * All the UML Property and Operation elements needed to
	 * create values to be saved to the nestedElements 
	 * property are in the {@link #propertiesOperations}
	 * map.
	 */
	private void createElementGroupRefrences() {
		if((showMessagesFor&(MESSAGES_FOR_PROPERTY|MESSAGES_FOR_OPERATION))>0)
			publishText("Setting element group references");
		
		Map<Object,Object> propertiesMap;
		List<Property> refrencedProperties;
		List<Operation> refrencedOperations;
		
		Property mapProperty;
		VisibleElement visibleElement;
		List<VisibleElement> groupElements;
		Operation mapOperation;
		addIndentation();
		boolean propertyRefrences;
		boolean operationRefrences;
		for(Map.Entry<Class, Map<String, Map<Object,Object>>> propertiesOperationsEntry:propertiesOperations.entrySet())
		{
			
			propertiesMap=propertiesOperationsEntry.getValue().get(PROPERTY);
			if((showMessagesFor&(MESSAGES_FOR_PROPERTY|MESSAGES_FOR_OPERATION))>0)
				publishText("Setting group references for Property and Operation elements in the "+((Class)propertiesOperationsEntry.getKey()).getName()+" Class element");
			addIndentation();
			propertyRefrences=false;
			for(Entry<Object,Object> entry:propertiesMap.entrySet())
			{
				visibleElement=(VisibleElement) entry.getKey();
				mapProperty=(Property)entry.getValue();
				if(visibleElement instanceof ElementsGroup)
				{
					if((showMessagesFor&MESSAGES_FOR_PROPERTY)>0)
						publishText("Setting group references for "+mapProperty.getName()+" Property");
					addIndentation();
					refrencedProperties=new ArrayList<Property>();
					groupElements=((ElementsGroup)visibleElement).getVisibleElementList();
					for(VisibleElement groupElement:groupElements)
					{
						refrencedProperties.add((Property) propertiesMap.get(groupElement));
					}
					PropertyStereotype.setStereotypeElementsGroupPropertyNestedElements(stereotypeProfile, mapProperty, refrencedProperties, this);
					removeIndentation(1);
					propertyRefrences=true;
				}
			}		
			if(!propertyRefrences)
				if((showMessagesFor&MESSAGES_FOR_PROPERTY)>0)
					publishText("There where no Property elements that represent groups");
			propertiesMap=propertiesOperationsEntry.getValue().get(OPERATION);
			
			operationRefrences=false;
			for(Entry<Object,Object> entry:propertiesMap.entrySet())
			{
				
				visibleElement=(VisibleElement) entry.getKey();
				mapOperation=(Operation)entry.getValue();
				if(visibleElement instanceof ElementsGroup)
				{
					if((showMessagesFor&MESSAGES_FOR_OPERATION)>0)
						publishText("Setting group references for "+mapOperation.getName()+" Operation");
					addIndentation();
					refrencedOperations=new ArrayList<Operation>();
					groupElements=((ElementsGroup)visibleElement).getVisibleElementList();
					for(VisibleElement groupElement:groupElements)
					{
						refrencedOperations.add((Operation) propertiesMap.get(groupElement));
					}
					OperationStereotype.setStereotypeElementsGroupOperationNestedElements(stereotypeProfile, mapOperation, refrencedOperations, this);
					removeIndentation(1);
					operationRefrences=true;
				}
			}
			if(!operationRefrences)
				if((showMessagesFor&MESSAGES_FOR_OPERATION)>0)
					publishText("There where no Operation elements that represent groups");
			removeIndentation(1);
		}
		removeIndentation(1);
		if((showMessagesFor&(MESSAGES_FOR_PROPERTY|MESSAGES_FOR_OPERATION))>0)
			publishText("Element group references are set");
	}
	
	/**
	 * Change version of the exported uml diagram 4.0 file to version 3.0.
	 * @param file File with uml diagram for which to change version of uml
	 * diagram
	 */
	private void changeVersion(File file){
		try{
		BufferedReader reader = new BufferedReader( new FileReader (file));
		String         line = null;
		StringBuilder  stringBuilder = new StringBuilder();
		String         ls = System.getProperty("line.separator");
	
		while( ( line = reader.readLine() ) != null ) {
			stringBuilder.append( line );
		    stringBuilder.append( ls );
		}

		reader.close();
		
	    String fileAsString=stringBuilder.toString();
	    
	    fileAsString = fileAsString.replace(UMLPackage.eNS_URI, UML302UMLResource.UML_METAMODEL_NS_URI);
	    
	    PrintWriter printWriter=new PrintWriter(file);
	    
	    printWriter.write(fileAsString);
	    printWriter.flush();
	    printWriter.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
