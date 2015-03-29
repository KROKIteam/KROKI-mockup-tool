package kroki.app.importt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import kroki.app.KrokiMockupToolApp;
import kroki.app.export.ExportProjectToEclipseUML;
import kroki.app.utils.uml.OperationsTypeDialog;
import kroki.app.utils.uml.ProgressWorker;
import kroki.app.utils.uml.TextToRemove;
import kroki.app.utils.uml.UMLElementsEnum;
import kroki.app.utils.uml.UMLResourcesUtil;
import kroki.app.utils.uml.stereotypes.ClassStereotype;
import kroki.app.utils.uml.stereotypes.OperationStereotype;
import kroki.app.utils.uml.stereotypes.PackageStereotype;
import kroki.app.utils.uml.stereotypes.PropertyStereotype;
import kroki.commons.camelcase.NamingUtil;
import kroki.mockup.model.Composite;
import kroki.mockup.model.border.TitledBorder;
import kroki.mockup.model.layout.LayoutManager;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Next;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.group.GroupOrientation;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.persistent.PersistentClass;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.ElementsGroupUtil;
import kroki.profil.utils.StandardPanelUtil;
import kroki.profil.utils.UIPropertyUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.impl.AssociationImpl;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;

/**
 * Class that implements import functionality for importing files with Eclipse UML model to Kroki project. 
 * 
 *  @author Zeljko Ivkovic (zekljo89ps@gmail.com)
 *
 */
public class ImportEclipseUMLToProject extends ProgressWorker{
	/**
	 * File from which to load Eclipse UML model for importing to Kroki project.
	 */
	private File file;
	/**
	 * Attribute containing the reference to a class that has methods for transforming
	 * names from camel case notation to notation where words in sentences are separated
	 * using empty spaces.
	 */
	private NamingUtil namingUtil;
	
	/**
	 * Prefix or suffix text to be removed from the names of
	 * the package, class, property or operation elements that
	 * are being imported from UML diagram.
	 */
	private List<TextToRemove> textsToBeRemoved;
	/**
	 * Constructor that creates an object for importing 
	 * files with Eclipse UML model to Kroki project.
	 * Receives a file object that represents the file that should be imported.
	 * @param file  file to be imported.
	 */
	public ImportEclipseUMLToProject(File file,List<TextToRemove> textsToBeRemoved){
		super();
		this.file=file;
		this.textsToBeRemoved=textsToBeRemoved;
		namingUtil = new NamingUtil();
		execute();
	}
	
	/**
	 * List of Eclipse UML {@link Association} elements for which to create zoom fields in Kroki.
	 */
	private List<Association> associations;
	/**
	 * List of Eclipse UML {@link Operation} elements for which to check which type of business operation to create.
	 */
	private List<Operation> operationsToCheck;
	/**
	 * Saves all the Kroki StandardPanel elements created for the corresponding Eclipse UML {@link ClassImpl} elements.
	 */
	private Map<ClassImpl, VisibleClass> classMap;
	/**
	 * Value used as a key value in a {@link #propertiesOperations} LinkedHashMap.
	 */
	private final String PROPERTY="Poperty";
	/**
	 * Value used as a key value in a {@link #propertiesOperations} LinkedHashMap.
	 */
	private final String OPERATION="Operation";
	/**
	 * LinkedHashMap that saves all the VisibleProperty, Zoom, VisibleOperation
	 * and ElementsGroup elements created for the corresponding UMl
	 * Property and Operation elements contained in the UML Class element
	 * represented as the key value <code>ClassImpl</code>
	 */
	private Map<ClassImpl, Map<String, Map<Object, Object>>> propertiesOperations;
	/**
	 * Contains the Kroki project element and its sub elements, that will be created during the import functionality.
	 */
	private BussinesSubsystem project;
	
	protected String removeFileExtension(String fileName){
		int pos=fileName.lastIndexOf(".");
		if(pos==-1)
			return fileName;
		else
			return fileName.substring(0,pos);
	}

	private HashMap<String, String> labels;
	private boolean extraLabelFile=false;
	protected void extractLabelFile(File file){
		if(!file.exists())
		{
			publishWarning("Label file "+file.getAbsolutePath()+" does not exist");
			publishWarning("UML element names will be used for persistent name and labels");
			extraLabelFile=false;
			return;
		}
		else
		{
			publishText("Loading labels from "+file.getAbsolutePath());
			BufferedReader reader=null;
			try {
				labels=new HashMap<String, String>();
				reader=new BufferedReader(new FileReader(file));
				String line,key,value;
				int commaPosition;
				while((line=reader.readLine())!=null)
				{
					commaPosition=line.indexOf(',');
					key=line.substring(0, commaPosition);
					
					value=line.substring(++commaPosition);
					//System.out.println(key+"|"+value);
					labels.put(key, value);
				}
				extraLabelFile=true;
				publishText("Finished loading labels from "+file.getAbsolutePath());
			} catch (FileNotFoundException e) {
			
			} catch (IOException e) {
				publishWarning("Error while loading label file "+file.getAbsolutePath());
				publishWarning("UML element names will be used for persistent name and labels");
			}finally{
				if(reader!=null)
					try {
						reader.close();
					} catch (IOException e) {
						
					}
			}
			
		}
	}
	
	/**
	 * Imports file with the Eclipse UML diagram that was received in the constructor.
	 * @throws Exception  if Eclipse UML model can not be loaded from the file and if the Kroki project files can not be created
	 * or forwarding an Exception thrown from the {@link #extractModel} method
	 */
	@Override
	protected Void doInBackground() throws Exception {
		publishText("Importing Eclipse UML diagram from file "+file.getAbsolutePath()+".");
		publishText("Loading Eclipse UML model from file.");
        
		addIndentation();
	    Model model=loadFile(file.getAbsolutePath());
	    String fileName=removeFileExtension(file.getName());
	    
	    File labelFile=new File(file.getParentFile().getAbsolutePath()+File.separatorChar+fileName+".txt");
	    extractLabelFile(labelFile);
	    removeIndentation(1);
	    
	    if(model!=null)
	    {
	    	publishText("Eclipse UML model loaded successfully.");
	    	publishText("Creating project for Eclipse UML model.");
		    project=null;
		    associations=new ArrayList<Association>();
		    operationsToCheck=new ArrayList<Operation>();
		    classMap=new HashMap<ClassImpl, VisibleClass>();
		    propertiesOperations=new LinkedHashMap<ClassImpl, Map<String,Map<Object,Object>>>();
			extractModel(model);
			if(!isCancelled())
			{
				if(project!=null)
				{
					//project.setFile(file);
					KrokiMockupToolApp.getInstance().getWorkspace().addPackage(project);
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
					publishText("Project for Eclipse UML model created successfully");
					publishText("Importing Eclipse UML diagram finished successfully.");
				}else{
					publishText("Error occurred while creating project from Eclipse UML model.");
					throw new Exception("Error while creating project from Eclipse UML model.");
				}
			}
	    }else
	    {
	    	throw new Exception("File you are trying to import is in unrecognizable format.");
	    }
		return null;
	}
	
	/**
	 * If the background process was interrupted or there was an error while
	 * importing a Eclipse UML diagram, a corresponding message will be displayed
	 * and the <code>done</code> method from the inherited class is called.
	 */
	@Override
	public void done(){
		try {
			get();
			JOptionPane.showMessageDialog(getFrame(), "Importing Eclipse UML diagram finished successfully.");
		} catch (InterruptedException | ExecutionException e) {
			showErrorMessage(e);
		} catch(CancellationException e){
			//When called cancel method
			//e.printStackTrace();
			JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), 
					"Importing Eclipse UML diagram has been canceled");
			publishErrorText("Import has been canceled");
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
		publishErrorText("Error happened while importing");
		publishErrorText(exceptionMessage(e));
		showError(exceptionMessage(e), "Error while importing");
		
		//e.printStackTrace();
	}
	/**
	 * Creates an Eclipse UML model elements from a file that contains a Eclipse UML diagram.
	 *  
	 * @param filePath    file that contains the Eclipse UML model
	 * @return            Eclipse UML Model with corresponding sub elements that are created from the file 
	 * @throws Exception  if the Eclipse UML diagram saved in the file contains unrecognizable elements
	 */
	protected Model loadFile(String filePath) throws Exception{
        URI fileUri = URI.createFileURI(filePath);
		
        ResourceSet resourceSet = new ResourceSetImpl();
        UMLResourcesUtil.init(resourceSet);
        try{
		    Resource resource = resourceSet.getResource(fileUri, true);
		    return (Model) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.MODEL);
        }catch(Exception e){
			e.printStackTrace();
			throw new Exception("Version of the Eclipse UML model is not recognized. Suported versions Eclipse UML 2.1,3.0 and 4.0.");
        }
	}
	

	/**
	 * Starts the process of creating the Kroki project for the loaded Eclipse UML model 
	 * that is sent to the method. 
	 * @param model       Eclipse UML model from which to create a Kroki project
	 * @throws Exception  forwards any Exception that is thrown from the methods 
	 * {@link #extractPackage}, {@link #placePropertiesAndOperations} or 
	 * {@link #determineOperationTypes()}
	 */
	private  void extractModel(Model model) throws Exception{
		if(model!=null)
		{
			project=new BussinesSubsystem(createHumanReadableLabel(model.getName()), true, ComponentType.MENU, null);
			project.setLabelToCode(!extraLabelFile);
			addIndentation();
			publishText("Project with name "+project.getLabel()+" created");
			addIndentation();
			publishText("Extracting project contents");
			extractPackage(model, project);
			removeIndentation(1);
			createAssociations();
			determineOperationTypes();
			placePropertiesAndOperations();
			removeIndentation(1);
		}else
		{
			publishText("File is in unrecognizable format.");
			publishText("Importing Eclipse UML diagram failed");
		}
	}

	/** 
	 * For the given Eclipse UML Package element iterates through Package, Class and Association sub elements that it  contains.
	 * For the Package sub elements this method creates Kroki BussinesSubsystem elements by calling the {@link #createBussinesSubsystem}
	 * method, for the Class elements it will create StandardPanel elements by calling {@link #createStandardPanel} method and 
	 * Association elements will be added to the list that will later be used for creating zoom fields.
	 * For every Class element methods to check and retrieve values of the corresponding stereotypes are called.
	 * Created elements are added to the received subsystemOwner Kroki BussinesSubsystem element that was created for the received 
	 * Package element.
	 * @param packageElem     Eclipse UML Package element to be imported
	 * @param subsystemOwner  Kroki BussinesSubsystem element that corresponds to the received Package element
	 * @throws Exception      forwards any Exception that is thrown from method {@link #extractClass}
	 */
	private void extractPackage(Package packageElem,BussinesSubsystem subsystemOwner) throws Exception{
		
		PackageStereotype.stereotypeBusinessSubsystemImport(packageElem, subsystemOwner, this);
		
		EList<Package> packages=packageElem.getNestedPackages();
		for(Package packageHelp:packages)
		{
			BussinesSubsystem pack = createBussinesSubsystem(packageHelp.getName(),subsystemOwner);
			publishText("Created "+BussinesSubsystem.class.getSimpleName()+" "+pack.getLabel());
			addIndentation();
			extractPackage(packageHelp,pack);
			removeIndentation(1);
		}
		
		Association association;
		ClassImpl classHelper;
		EList<Type> types= packageElem.getOwnedTypes();
		for(Type type:types)
		{
			if(type instanceof ClassImpl)
			{
				classHelper=(ClassImpl)type;
				if(isParentChildClass(classHelper))
				{
					//TODO:Namestiti da se ubacuje hierarchy 
				}else
				{
					VisibleClass panel = createStandardPanel(classHelper.getName(),subsystemOwner);
					addIndentation();
					classMap.put(classHelper, panel);
					extractClass(classHelper,panel);
					removeIndentation(1);
				}
			}else if(type instanceof AssociationImpl)
			{
				association=(Association)type;
				if(!associations.contains(association))
				{
					associations.add(association);
				}
			}
		}
		
	}

	/**
	 * Checks if the UML Class element has a stereotype 
	 * applied that will determine that this Class is
	 * supposed to be a ParentChild element or if it is
	 * supposed to be a StandardPanel element. If no
	 * stereotype is applied for the Class element, 
	 * then it checks if any of the UML Property
	 * sub elements have a Hierarchy stereotype applied
	 * that will mean this Class should be a ParentChild
	 * element.
	 * @param object  Eclipse UML Class element to check
	 * if it is supposed to be a Kroki ParentChild element
	 * @return        <code>true</code> if it is supposed
	 * to be a ParentChild element, <code>false</code> otherwise
	 */
	public boolean isParentChildClass(ClassImpl object){
		if(ClassStereotype.isParenChildStereotypeApplied(object))
			return true;
		else
		{
			for(Property property:object.getAttributes())
				if(PropertyStereotype.isHierarchyStereotypeApplied(property))
					return true;
		}
		return false;
	}
	/**
	 * Creates a Kroki StandardPanel element with the received name and adds it
	 * as a sub element to the classOwner BussinesSubsystem element. Name is transfered from a
	 * camel case notation to the notation that uses empty space for separating words.
	 * @param name         name of the StandardPanel element to be created
	 * @param classOwner   BussinesSubsystem element to which the created StandradPanel element will be added
	 * @return             created StandardPanel element
	 */
	public VisibleClass createStandardPanel(String name,BussinesSubsystem classOwner){
		StandardPanel panel = new StandardPanel();
		StandardPanelUtil.defaultGuiSettings(panel);
		String newName=null;
		boolean labelToCode=false;
		if(extraLabelFile)
		{
			newName=labels.get(name);
		}
		
		if(!extraLabelFile||newName==null)
		{
			newName=removePrefixSuffix(name, UMLElementsEnum.CLASS, true);
			newName=createHumanReadableLabel(newName);
			newName=removePrefixSuffix(newName, UMLElementsEnum.CLASS, false);
			if(extraLabelFile)
				publishWarning("Label for class "+name+" not found in label file");
			publishText("Label "+newName+" set for class "+name);
			labelToCode=true;
		}else
		{
			publishText("Label "+newName+" retreived from label file");
		}
		panel.setLabel(newName);
		
		//panel.getComponent().setName(newName);
		PersistentClass persistent=panel.getPersistentClass();
		persistent.setLabelToCode(!extraLabelFile);
		/*
		if(labelToCode)
		{
			panel.getComponent().setName(namingUtil.toCamelCase(panel.getLabel(), false));
			persistent.setName(namingUtil.toCamelCase(panel.getLabel(), false));
		}
		else
		*/
		{
			panel.getComponent().setName(name);
			persistent.setName(firstUpper(name));
			persistent.setTableName(name);
		}
		
		/*
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(1);
		((Composite) gr.getComponent()).setLayoutManager(new VerticalLayoutManager());
		((Composite) gr.getComponent()).layout();
		gr.update();
		*/
		
		classOwner.addOwnedType(panel);
		panel.setUmlPackage(classOwner);
		panel.update();
		publishText("Created "+StandardPanel.class.getSimpleName()+" "+panel.getLabel());
		return panel;
	}
	
	protected String firstUpper(String name){
		String newName=name;
		if(!name.isEmpty())
			newName=newName.substring(0, 1).toUpperCase()+newName.substring(1);
		return newName;
	}
	
	/**
	 * Creates a BussinesSubsystem element with the received name and adds it as a 
	 * sub element to the received BussinesSubsystem element.
	 * @param name            name of the BussinesSubsystem element to be created
	 * @param subsystemOwner  BussinesSubsystem element to which the created BussinesSubsystem element 
	 * will be added
	 * @return                created BussinesSubsystem element
	 */
	public BussinesSubsystem createBussinesSubsystem(String name, BussinesSubsystem subsystemOwner){
		BussinesSubsystem pack = new BussinesSubsystem(subsystemOwner);
		String newName=removePrefixSuffix(name, UMLElementsEnum.PACKAGE, true);
		newName=createHumanReadableLabel(newName);
		newName=removePrefixSuffix(newName, UMLElementsEnum.PACKAGE, false);
		pack.setLabel(newName);
		pack.setLabelToCode(!extraLabelFile);
		subsystemOwner.addNestedPackage(pack);
		return pack;
	}
	
	/**
	 * For the received Eclipse UML Class element iterates through the Operation, Property and Association sub elements.
	 * For an Operation element it checks if it has a stereotype applied that determines if a Report, Transaction or
	 * ElementsGroup element is supposed to be created, if it can not determine which type of element to create it adds
	 * the Operation element to the {@link #operationsToCheck} list, for the user to choose if a Report or a Transaction
	 * element should be created for thie Operation element. For the Property element it checks what stereotype is
	 * applied to determine if it should create a VisibleProperty or a ElementsGroup element. Association elements are
	 * added to the list that will be used to create zoom fields. For every Property and Operation element methods that
	 * check and retrieve stereotypes values are called. Created elements are added to the received panel
	 * element of type VisibleClass.   
	 * @param classObject    Eclipse UML Class element to be imported
	 * @param panel          Kroki VisibleClass element that corresponds to the StandardPanel element to which the
	 * created elements will be added
	 * @throws Exception     if a UML Property that is an end of a Association element has VisibleProperty or 
	 * ElementsGroup stereotype applied and if one UML Property has both the VisibleProperty and the ElementsGroup
	 * stereotype applied 
	 */
	private void extractClass(ClassImpl classObject,VisibleClass panel) throws Exception {
		
		ClassStereotype.stereotypeStandardPanelImport(classObject, (StandardPanel) panel,this);

		int group;//(0-toolbar, 1-Properties, 2-Operations)
		group=2;
		boolean fieldsCreated,operationsCreated;
		Object createdObject;
		EList<Operation> operations=classObject.getOwnedOperations();
		publishText("Creating business operations and groups:");
		addIndentation();
		Map<Object, Object> objectsMap=new LinkedHashMap<Object, Object>();
		operationsCreated=false;
		for(Operation operation:operations)
		{
			createdObject=null;
			if(OperationStereotype.isReportStereotypeApplied(operation))
			{
				createdObject=createReportOnly(operation.getName(),panel);
				addIndentation();
				OperationStereotype.stereotypeReportImport(operation, (Report) createdObject, this);
				removeIndentation(1);
			}else if(OperationStereotype.isTransactionStereotypeApplied(operation))
			{
				createdObject=createTransactionOnly(operation.getName(),panel);
				addIndentation();
				OperationStereotype.stereotypeTransactionImport(operation, (Transaction) createdObject, this);
				removeIndentation(1);
			}else if(OperationStereotype.isElementsGroupOperationStereotypeApplied(operation))
			{
				createdObject=createElementsGroupOnly(operation.getName(),true,panel,false);
				publishText("Created group for UML Operation "+operation.getName());
				addIndentation();
				OperationStereotype.stereotypeElementsGroupOperationImport(operation, (ElementsGroup)createdObject, this);
				removeIndentation(1);
			}else
			{
				operationsToCheck.add(operation);
				publishText("UML Operation "+operation.getName()+" has to be decided by user what type of business operation to create");
			}
			objectsMap.put(operation, createdObject);
			operationsCreated=true;
		}
		if(!operationsCreated)
			publishText("No UML Operation elements for this Class");
		Map<String, Map<Object, Object>> propertiesOperationsPartMap=new LinkedHashMap<String, Map<Object,Object>>();
		propertiesOperationsPartMap.put(OPERATION, objectsMap);
		removeIndentation(1);
		
		group=1;
		Type dataType=null;
		Enumeration enumObject;
		VisibleProperty property;
		StringBuilder enumerationLiterals;
		EList<Property> properties=classObject.getAttributes();
		boolean elementsGroup;
		boolean visibleProperty;
		objectsMap=new LinkedHashMap<Object, Object>();
		publishText("Creating fields and groups:");
		addIndentation();
		fieldsCreated=false;
		for(Property attribute:properties)
		{
			createdObject=null;
			elementsGroup=PropertyStereotype.isElementsGroupPropertyStereotypeApplied(attribute);
			visibleProperty=PropertyStereotype.isVisiblePropertyStereotypeApplied(attribute);
			Association association=attribute.getAssociation();
			if(association!=null)
			{
				if(visibleProperty || elementsGroup)
				{
					throw new Exception("In class "+classObject.getName()+" check stereotype for property "+attribute.getName());
				}
				else
				{	
					if(!associations.contains(association))
					{
						associations.add(association);
					}
					continue;
				}
			}
			if(visibleProperty && elementsGroup)
			{
				throw new Exception("Property "+attribute.getName()+" for class "
			             +classObject.getName()+" cannot have "
						 +PropertyStereotype.STEREOTYPE_ELEMENTS_GROUP_PROPERTY_NAME
						 +" and "+PropertyStereotype.STEREOTYPE_VISIBLE_PROPERTY_NAME+" stereotypes set");
			}else if(!visibleProperty && elementsGroup)
			{
				createdObject=createElementsGroupOnly(attribute.getName(), true,panel,true);
				publishText("Created group for UML Property "+attribute.getName());
				addIndentation();
				PropertyStereotype.stereotypeElementsGroupOperationImport(attribute, (ElementsGroup) createdObject, this);
				removeIndentation(1);
			}else
			{
				dataType=attribute.getType();
				if(dataType==null)
				{
					/*
					if(PropertyStereotype.isComboBoxComponentStereotypeApplied(attribute))
						createdObject=createVisibleProperty(attribute.getName(), true, ComponentType.COMBO_BOX,"", panel,true);
					else
					*/
						createdObject=createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"String", panel,true,classObject.getName());
				}else if(dataType instanceof Enumeration)
				{
					enumObject=(Enumeration)dataType;
					property=createVisibleProperty(attribute.getName(), true, ComponentType.COMBO_BOX,"", panel,true,classObject.getName());
					enumerationLiterals=new StringBuilder();
					for(EnumerationLiteral literal:enumObject.getOwnedLiterals())
						enumerationLiterals.append(literal.getName()+";");
					property.setEnumeration(enumerationLiterals.toString());
					createdObject=property;
				}else
				{
					if(dataType.getName()==null)
					{
						throw new Exception("Data type for property "+attribute.getName()+" can not be determined.\n"
								+"File beeing imported is missing profile files. Try exporting again and then importing to KROKI.");
					}else
					if(dataType.getName().toLowerCase().contains(ExportProjectToEclipseUML.ENUMERATION_ELEMENT_TYPE.toLowerCase()))
					{
						createdObject=createVisibleProperty(attribute.getName(), true, ComponentType.COMBO_BOX,"", panel,true,classObject.getName());
					}else
					if(dataType.getName().toLowerCase().contains("boolean"))
					{
						createdObject=createVisibleProperty(attribute.getName(), true, ComponentType.CHECK_BOX,"", panel ,true,classObject.getName());
					}
					else if(dataType.getName().toLowerCase().contains("bigdecimal")||
							dataType.getName().toLowerCase().contains("double")||
							dataType.getName().toLowerCase().contains("float"))
					{
						createdObject=createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"BigDecimal", panel,true,classObject.getName());
					}
					else if(dataType.getName().toLowerCase().contains("string"))
					{
						if(attribute.getUpper()>50)
							createdObject=createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_AREA,"", panel,true,classObject.getName());
						else
							createdObject=createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"String", panel,true,classObject.getName());
					}
					else if(dataType.getName().toLowerCase().contains("int"))
						createdObject=createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"Integer", panel,true,classObject.getName());
					else if(dataType.getName().toLowerCase().contains("long"))
						createdObject=createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"Long", panel,true,classObject.getName());
					else if(dataType.getName().toLowerCase().contains("date"))
						createdObject=createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"Date", panel,true,classObject.getName());
					else
						createdObject=createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"String", panel,true,classObject.getName());
					attribute.getLabel();
				}
				if(createdObject!=null)
				{
					addIndentation();
					PropertyStereotype.stereotypeVisiblePropertyImport(attribute, (VisibleProperty) createdObject, this);
					removeIndentation(1);
				}
			}
			objectsMap.put(attribute, createdObject);
			fieldsCreated=true;
		}
		if(!fieldsCreated)
			publishText("No UML Property elements for this Class");
		removeIndentation(1);
		propertiesOperationsPartMap.put(PROPERTY, objectsMap);
		propertiesOperations.put(classObject, propertiesOperationsPartMap);
		EList<Association> associationsForClass=classObject.getAssociations();
		for(Association association:associationsForClass)
		{
		
			if(!associations.contains(association))
			{
				associations.add(association);
			}
		}
		
		EList<Generalization> generalizations=classObject.getGeneralizations();
		for(Generalization generalization:generalizations)
		{
			//System.out.println(generalization+"General:\""+generalization.getGeneral()+"\"General:\""+generalization.getOwner()+"\"");
		}
	}
	
	/**
	 * Creates a ElementsGroup element to be used for creating hierarchies of input field and business operations.
	 * @param name             name of the ElementsGroup element
	 * @param visible          if the created ElementsGroup should be visible
	 * @param panel            panel representing the root element that contains all the elements on the window 
	 * @param visibleProperty  <code>true</code> if this ElementsGroup will contain input fields or <code>false</code>
	 * if this ElementsGroup element will contain business operations
	 * @return                 created ElementsGroup element
	 */
	private ElementsGroup createElementsGroupOnly(String name,boolean visible,VisibleClass panel,boolean visibleProperty) {
		ElementsGroup elementsGroup = new ElementsGroup(createHumanReadableLabel(name), visible, ComponentType.PANEL);
        elementsGroup.setGroupOrientation(GroupOrientation.vertical);
        /*
        if(visibleProperty)
        {
        	LayoutManager propertiesLayout = new VerticalLayoutManager(10, 10, VerticalLayoutManager.LEFT);
        	((Composite) elementsGroup.getComponent()).setLayoutManager(propertiesLayout);
        }
        else
        {
        	elementsGroup.setGroupAlignment(GroupAlignment.center);
        	LayoutManager operationsLayout = new VerticalLayoutManager();
            operationsLayout.setAlign(LayoutManager.CENTER);
            ((Composite) elementsGroup.getComponent()).setLayoutManager(operationsLayout);
        }
        */
        ((Composite) elementsGroup.getComponent()).setBorder(new TitledBorder());
        elementsGroup.setUmlClass(panel);
		return elementsGroup;
	}
	
	
	/**
	 * Creates a Report element with the received name and adds it as a sub element to the
	 * received VisibleClass element. VisibleClass element is represented by the
	 * StandardPanel element.
	 * @param name   name of the Report element to be created
	 * @param panel  StandardPanel element to which the created Report element will be added
	 * @return       created Report element
	 */
	protected VisibleOperation createVisibleOperation(String name,VisibleClass panel){
		int group;//(0-toolbar, 1-Properties, 2-Operations)
		group=2;
		
		VisibleOperation visibleOperation = new Report(createHumanReadableLabel(name), true, ComponentType.BUTTON);
		UIPropertyUtil.addVisibleElement(panel, visibleOperation);
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(group);
		ElementsGroupUtil.addVisibleElement(gr, visibleOperation);
		visibleOperation.setParentGroup(gr);
		visibleOperation.setUmlClass(panel);
		return visibleOperation;
	}
	
	/**
	 * Creates a Report element with the received name and sets its parent panel that represents the
	 * window where the created Report will be shown, but does not add it to the elements to be shown
	 * on the window.
	 * @param name   name of the Report element to be created
	 * @param panel  parent panel that will contain this created Report element
	 * @return       created Report element
	 */
	protected Report createReportOnly(String name,VisibleClass panel){
		String humanReadable=removePrefixSuffix(name, UMLElementsEnum.OPERATION, true);
		humanReadable=createHumanReadableLabel(humanReadable);
		humanReadable=removePrefixSuffix(humanReadable, UMLElementsEnum.OPERATION, false);
		Report report= new Report(humanReadable, true, ComponentType.BUTTON);
		report.setUmlClass(panel);
		publishText("Created Report operation for UML Operation "+name);
		return report;
	}
	
	/**
	 * Creates a Transaction element with the received name and sets its parent panel that represents the
	 * window where the created Transaction will be shown, but does not add it to the elements to be shown
	 * on the window.
	 * @param name   name of the Transaction element to be created
	 * @param panel  parent panel that will contain this created Transaction element
	 * @return       created Transaction element
	 */
	protected Transaction createTransactionOnly(String name,VisibleClass panel){
		String humanReadable=removePrefixSuffix(name, UMLElementsEnum.OPERATION, true);
		humanReadable=createHumanReadableLabel(humanReadable);
		humanReadable=removePrefixSuffix(humanReadable, UMLElementsEnum.OPERATION, false);
		Transaction transaction= new Transaction(humanReadable, true, ComponentType.BUTTON);
		transaction.setUmlClass(panel);
		publishText("Created Transaction operation for UML Operation "+name);
		return transaction;
	}
	
	/**
	 * Creates a VisibleProperty element with the properties received and adds it as a sub element
	 * of the parent panel representing the window on which this created VisibleProperty element
	 * will be shown. 
	 * @param label     name of the VisibleProperty to be created
	 * @param visible   if the VisibleProperty created should be visible
	 * @param type      type of the VisibleProperty element
	 * @param dataType  extra property for determining the type of the input fields that allow input
	 * of characters
	 * @param panel     parent window to which to add the created VisibleProperty element
	 * @return          created VisibleProperty element
	 */
	private VisibleProperty createVisibleProperty(String label, boolean visible, ComponentType type,String dataType, VisibleClass panel,boolean visiblePropertyOnly,String umlClassName){
		int group=1;
		String humanReadable=null;
		boolean labelToCode=false;
		if(extraLabelFile)
		{
			humanReadable=labels.get(umlClassName+"."+label);
		}
		
		if(!extraLabelFile||humanReadable==null)
		{
			humanReadable=removePrefixSuffix(label, UMLElementsEnum.CLASS, true);
			humanReadable=createHumanReadableLabel(humanReadable);
			humanReadable=removePrefixSuffix(humanReadable, UMLElementsEnum.CLASS, false);
			if(extraLabelFile)
				publishWarning("Label for property "+umlClassName+"."+label+" not found in label file");
			publishText("Label "+humanReadable+" set for property "+label);
			labelToCode=true;
		}else
		{
			publishText("Label "+humanReadable+" retreived from label file");
		}
		/*
		String humanReadable=removePrefixSuffix(label, UMLElementsEnum.PROPERTY, true);
		humanReadable=createHumanReadableLabel(humanReadable);
		humanReadable=removePrefixSuffix(humanReadable, UMLElementsEnum.PROPERTY, false);
		*/
		VisibleProperty property = new VisibleProperty(humanReadable, visible, type);
		property.setLabel(humanReadable);
		if(type == ComponentType.TEXT_FIELD) {
			property.setDataType(dataType);
		}
		
		property.setLabelToCode(!extraLabelFile);
		/*
		if(labelToCode)
			property.setColumnLabel(namingUtil.toDatabaseFormat(panel.getLabel(), label));
		else*/
			property.setColumnLabel(label);
		
		if(!visiblePropertyOnly)
		{
			UIPropertyUtil.addVisibleElement(panel, property);
			ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(group);
			ElementsGroupUtil.addVisibleElement(gr, property);
			gr.update();
			panel.update();
			property.setParentGroup(gr);
		}
		property.setUmlClass(panel);
		publishText("Created input field for UML Property "+label);
		return property;
	}
	
	/**
	 * Creates zoom fields and next links using the list of
	 * Association elements. For every Association that has cardinality
	 * of one of the ends set to 0..* and the other set to 0..1. 
	 * Corresponding zoom filed and next link are created.
	 */
	private void createAssociations(){
		Property first,second;
		publishText("Creating associations");
		addIndentation();
		for(Association association:associations){
			first=association.getAllAttributes().get(0);
			second=association.getAllAttributes().get(1);
			if(first.getUpper()==LiteralUnlimitedNatural.UNLIMITED)
			{
				if(second.getUpper()==LiteralUnlimitedNatural.UNLIMITED)
				{
					publishText("Many-to-Many association between Classes "
				             +first.getType().getName() +" and "
				             +second.getType().getName());
					publishText("Many-to-Many association is not supported");
					//System.out.println("Many-to-Many not supported");
				}
				else{
					createZoom(second,first);
				}
			}
			else{
				if(second.getUpper()==LiteralUnlimitedNatural.UNLIMITED)
				{
					createZoom(first,second);
				}
				else
				{
					//supported association
					publishText("One-to-One association between Classes "
					             +first.getType().getName() +" and "
					             +second.getType().getName());
					publishText("One-to-One association is not supported");
					//System.out.println("One-to-One not supported");
				}
			}
		}
		removeIndentation(1);
		publishText("Associations created");
	}
	
	/**
	 * Creates a Kroki Zoom element using the Eclipse UML Property
	 * elements that represent the ends of the Eclipse UML 
	 * Association element for which a Zoom element is being 
	 * created. For the corresponding Property element calls
	 * methods that check and retrieve the values of the 
	 * corresponding stereotypes. Zoom element corresponds to the
	 * zoom field.
	 * @param first   Property element that has cardinality set to 0..1
	 * @param second  Property element that has cardinality set to 0..*
	 */
	private void createZoom(Property first,Property second){
		ClassImpl firstClass=(ClassImpl) first.getType();
		ClassImpl secondClass=(ClassImpl) second.getType();
		VisibleClass firstVisibleClass=classMap.get(secondClass);
		VisibleClass secondVisibleClass=classMap.get(firstClass);
		
		String zoomName=first.getName();
		//If name of the association end is not set, set the name of the Class
		boolean nameNotSet=false;
		if(zoomName==null)
			nameNotSet=true;
		else if(zoomName.isEmpty())
			nameNotSet=true;
		
		if(nameNotSet)
		{
			String base=secondVisibleClass.getLabel();
			int[] sufix=null;
			zoomName=base;
			boolean provera=false;
			Map<Object,Object> properties= propertiesOperations.get(secondClass).get(PROPERTY);
			VisibleElement element;
			/* Do-while loop needed because there is a case when the suffix that is added to the
			 * nextName is equal to a name of another element, but because that element was before
			 * this point the new suffix is not checked with the name of that element. New suffix 
			 * needs to be checked with the elements that where already passed. 
			 */
			do{
				provera=false;
				for(Entry<Object, Object> value:properties.entrySet())
				{
					element=(VisibleElement)value.getValue();
					if(zoomName!=null && element.getLabel()!=null)
						while(zoomName.equalsIgnoreCase(element.getLabel()))
						{
							sufix=updateSufix(sufix);
							zoomName=createText(base, sufix);
							provera=true;
						}
				}
			}while(provera);
			publishWarning("Creating zoom for association end that has no name set");
			publishWarning("Name for association end set to be "+zoomName);
			zoomName=createHumanReadableLabel(zoomName);
		}
		else
		{
			publishText("Creating zoom for property "+zoomName);
			zoomName=removePrefixSuffix(zoomName, UMLElementsEnum.PROPERTY, true);
			zoomName=createHumanReadableLabel(zoomName);
			zoomName=removePrefixSuffix(zoomName, UMLElementsEnum.PROPERTY, false);
			publishText("After removing prefix and suffix name for next property is "+zoomName);
		}
		
		addIndentation();
		
		
		
		VisibleProperty visibleProperty=createVisibleProperty(zoomName, true, ComponentType.COMBO_BOX, "", firstVisibleClass,true,secondClass.getName());
		/*
		ElementsGroup elg = (ElementsGroup) firstVisibleClass.getVisibleElementList().get(group);
        if (elg != null) {
            int position = elg.indexOf(visibleProperty);
            elg.removeVisibleElement(visibleProperty);
            firstVisibleClass.removeVisibleElement(visibleProperty);
            */
            Zoom zoom = new Zoom(visibleProperty);
            zoom.setActivationPanel(firstVisibleClass);
            /*
            elg.addVisibleElement(position, zoom);
            firstVisibleClass.addVisibleElement(zoom);

            elg.update();
            firstVisibleClass.update();
            */
            zoom.setTargetPanel(secondVisibleClass);
            //zoom.setParentGroup(elg);
            zoom.setUmlClass(firstVisibleClass);
            publishText("Created Zoom field with name "+zoomName+" in Class "+secondClass.getName());            
            if(second.isNavigable())
            {
            	publishText("Navigable property is set to true for property with maximum cardinality * Next element is beeing created");
            	Next next=createNext(second, first);
            	zoom.setOpposite(next);
            	next.setOpposite(zoom);
            }else
            {
            	publishText("Navigable property is not set for property with maximum cardinality * Next element will not be created");
            }
            addIndentation();
            PropertyStereotype.stereotypeZoomImport(first, zoom, this);
            removeIndentation(1);
        //}
            //Has to go secondClass because it coresponds to the firstVisibleClass StandardPanel where the zoom is located
        propertiesOperations.get(secondClass).get(PROPERTY).put(first, zoom);
        removeIndentation(1);
	}
	
	/**
	 * Method that receives a string and checks if it contains all upper case letters
	 * or if it is in camel case notation and does the corresponding formating of the
	 * string.
	 * @param text string to be formated into human readable text.
	 * @return human readable text
	 */
	protected String createHumanReadableLabel(String text){
		if(text!=null)
			if(!text.isEmpty())
			{
				String label=text.replaceAll("_", " "); 
				boolean maloSlovo=false;
				for(int i=0;i<label.length();i++)
				{
					if(Character.isLowerCase(label.charAt(i)))
					{
						maloSlovo=true;
						break;
					}
				}
				
				if(maloSlovo)
				{
					label= namingUtil.fromCamelCase(label);
				}
				else
				{
					StringBuilder builder=new StringBuilder();
					for(int i=0;i<label.length();i++)
					{
						if(i==0)
						{
							builder.append(Character.toUpperCase(label.charAt(i)));
						}else
							builder.append(Character.toLowerCase(label.charAt(i)));
					}
					label= builder.toString();
				}
				
				StringBuilder builder=new StringBuilder();
				char c;
				for(int i=0;i<label.length();i++)
				{
					c=label.charAt(i);
					if(Character.isAlphabetic(c)||Character.isWhitespace(c)||Character.isDigit(c))
						builder.append(c);
				}
				label=builder.toString();
				label=label.replaceAll("[ ]+", " ");
				label=label.replaceAll("[\t]+", " ");
				label=label.replaceAll("[\n]+", " ");
				return label.trim();
				
			}
		return "";
	}
	
	/**
	 * Method that receives a set of characters represented as integers. And increments characters to get
	 * the next combination. It iterates through characters starting from 'a' to 'z' and when it gets to 'z'
	 * it adds a new character to the end of the set if it iterated through the whole set or if it has more
	 * characters in the set it changes the next character.
	 * Calling this method continuously with the sets returned by this method will create sets with characters
	 * shown in the example: a,b,c,d,....,z,aa,ba,ca,da,....,zz,aaa etc.
	 * @param sufix Set of characters to be updated by changing the characters or by adding new characters to
	 * the set. Initially this parameter should have a <code>null</code> value.
	 * @return a set with characters that is different than the set the method received.
	 */
	protected static int[] updateSufix(int[] sufix){
		if(sufix==null)
		{
			sufix=new int[]{'a'};
		}else
		{
			int i=0;
			sufix[i]++;
			while(sufix[i]>'z')
			{
				sufix[i]='a';
				if(i==sufix.length-1)
				{
					int[] pomSufix=new int[sufix.length+1];
					pomSufix[pomSufix.length-1]='a';
					for(int j=0;j<sufix.length;j++)
						pomSufix[j]=sufix[j];
					sufix=pomSufix;
				}else
				{
					i++;
					sufix[i]++;
				}
			}
		}
		return sufix;
	}
	
	/**
	 * Method that takes a base string and a set of characters represented by integers
	 * and creates a new string starting with the base string and appending all the
	 * characters represented by integer values in the set.
	 * @param base starting string
	 * @param sufix set of integers that will be turned into characters and appended to the
	 * starting string
	 * @return  string created by uniting the starting string with the characters in the set
	 */
	protected static String createText(String base,int [] sufix){
		StringBuilder builder=new StringBuilder(base);
		for(int i=0;i<sufix.length;i++)
			builder.append(Character.toChars(sufix[i]));
		return builder.toString();
	}
	
	/**
	 * Creates a Kroki Next element using the Eclipse UML Property
	 * elements that represent the ends of the Eclipse UML
	 * Association element for which a Next element is being 
	 * created. Next element corresponds to the next link.
	 * @param first  Property element that has cardinality set to 0..*
	 * @param second Property element that has cardinality set to 0..1
	 */
	private Next createNext(Property first,Property second){
		ClassImpl firstClass=(ClassImpl) first.getType();
		ClassImpl secondClass=(ClassImpl) second.getType();
		VisibleClass firstVisibleClass=classMap.get(secondClass);
		VisibleClass secondVisibleClass=classMap.get(firstClass);
		
		addIndentation();
		
		String nextName=first.getName();
		//If name of the association end is not set, set the name of the Class
		boolean nameNotSet=false;
		if(nextName==null)
			nameNotSet=true;
		else if(nextName.isEmpty())
			nameNotSet=true;
		
		if(nameNotSet)
		{
			String base=secondVisibleClass.getLabel();
			int[] sufix=null;
			nextName=base;
			boolean provera=false;
			Map<Object,Object> properties= propertiesOperations.get(secondClass).get(PROPERTY);
			VisibleElement element;
			/* Do-while loop needed because there is a case when the suffix that is added to the
			 * nextName is equal to a name of another element, but because that element was before
			 * this point the new suffix is not checked with the name of that element. New suffix 
			 * needs to be checked with the elements that where already passed. 
			 */
			do{
				provera=false;
				for(Entry<Object, Object> value:properties.entrySet())
				{
					element=(VisibleElement)value.getValue();
					if(nextName!=null && element.getLabel()!=null)
						while(nextName.equalsIgnoreCase(element.getLabel()))
						{
							sufix=updateSufix(sufix);
							nextName=createText(base, sufix);
							provera=true;
						}
				}
			}while(provera);
			properties= propertiesOperations.get(secondClass).get(OPERATION);
			
			do{
				provera=false;
				for(Entry<Object, Object> value:properties.entrySet())
				{
					element=(VisibleElement)value.getValue();
					if(nextName!=null && element.getLabel()!=null)
						while(nextName.equalsIgnoreCase(element.getLabel()))
						{
							sufix=updateSufix(sufix);
							nextName=createText(base, sufix);
							provera=true;
						}
				}
			}while(provera);
			publishWarning("Creating next for association end that has no name set");
			publishWarning("Name for association end set to be "+nextName);
			nextName=createHumanReadableLabel(nextName);
		}
		else
		{
			publishText("Creating next for property "+nextName);
			nextName=removePrefixSuffix(nextName, UMLElementsEnum.PROPERTY, true);
			nextName=createHumanReadableLabel(nextName);
			nextName=removePrefixSuffix(nextName, UMLElementsEnum.PROPERTY, false);
			publishText("After removing prefix and suffix name for next property is "+nextName);
		}
			
		
		Next next = new Next(nextName);
		next.setActivationPanel(firstVisibleClass);
		
		next.setTargetPanel(secondVisibleClass);
		
		propertiesOperations.get(secondClass).get(OPERATION).put(first, next);
		removeIndentation(1);
		return next;
	}
	
	/**
	 * Creates a dialog in which the user determines which 
	 * type of business operation, Report or Transaction,
	 * should be created for every UML Operation element
	 * that did not have a stereotype applied to determine
	 * which type of element to create Report, Transaction
	 * or ElementsGroup. Operation elements for which the
	 * user has to decide which elements to create were saved
	 * to a {@link #operationsToCheck} list during the import
	 * functionality. 
	 * @throws Exception   if the user exited the
	 * dialog with out confirming the selection of the
	 * elements to be created for the UML Operation elements
	 */
	private void determineOperationTypes() throws Exception{
		if(!operationsToCheck.isEmpty())
		{
			publishText("Determine types of operations");
			OperationsTypeDialog dialog=new OperationsTypeDialog(this.getFrame(), operationsToCheck);
			dialog.setVisible(true);
			if(dialog.isOK())
			{
				Operation operation;
				Object createdObject;
				VisibleClass classObject;
				for(Entry<Operation, Class<?>> entry:dialog.getOperationTypes().entrySet())
				{
					operation=entry.getKey();
					classObject=classMap.get(operation.getOwner());
					if(entry.getValue().equals(Report.class))
						createdObject=createReportOnly(operation.getName(), classObject);
					else //if(entry.getValue().toLowerCase().contains("transaction"))
						createdObject=createTransactionOnly(operation.getName(), classObject);
					propertiesOperations.get(operation.getOwner()).get(OPERATION).put(operation, createdObject);
				}
				publishText("Types of operations determined");
			}
			else
			{
				publishText("Import aborted because the user exited the dialog for choosing elements to be"
						+ " created for the UML Operation element with out confirmation.");
				throw new Exception("Import aborted because the user exited the dialog for choosing elements "
						+ "to be created for the UML Operation element with out confirmation.");
			}
		}
	}
	
	/**
	 * Method that for all the created VisibleProperty, Zoom,
	 * Report, Transaction and ElementsGroup elements organizes 
	 * them into hierarchies of ElementsGroup elements.
	 * Which element is a sub element of which ElementsGroup is
	 * saved as the value of the nestedElements property for the
	 * ElementsGroupProperty or the ElementsGroupOperation
	 * stereotype that was applied to the UML Property or
	 * a UML Operation element respectively.
	 * All the elements that need to be organized are retrieved 
	 * from the {@link #propertiesOperations} map during the
	 * import functionality.
	 * @throws Exception   if there is a recursive loop between the 
	 * UML Property or Operation elements that have applied 
	 * ElementsGroupProperty or ElementsGroupOperation stereotypes
	 * respectively, if a UML Property or Operation element is
	 * referenced by two others UML Property or Operation 
	 * elements that have applied ElementsGroupProperty or 
	 * ElementsGroupOperation stereotype, if a UML Property or 
	 * Operation element that has a ElementsGroupProperty or 
	 * ElementsGroupOperation stereotype applied references a
	 * UML Property or Operation from a different Class element
	 * and if every UML Property or Operation element is referenced
	 * by some other UML Property or Operation and there is no 
	 * UML Property and Operation elements to be the root elements
	 * that contain all the other elements
	 */
	private void placePropertiesAndOperations() throws Exception {
		publishText("Organizing input fields, zoom fields, business operations into groups");
				
		StandardPanel standardPanel;
		Map<Object,Object> propertiesMap;
		List<Object> rootValues;
		/*
		 * used to check if a UML Property or a UML Operation is
		 * referenced in two different nestedElements properties
		 * of the  ElementsGroupProperty or the ElementsGroupOperation
		 * stereotypes.
		 */
		List<Object> referencedValues;
		/*
		 * Used to check if there are any UML Property and Operation
		 * elements to be added to the StandardPanel element, because
		 * if there are not any elements then the check if any elements
		 * after the hierarchy organization are left to be added directly
		 * to the StandardPanel element should not be checked.
		 */
		int numberOfElements;
		ElementsGroup group;
		Property mapProperty;
		Object value;
		VisibleElement visibleElement;
		
		Operation mapOpeartion;
		boolean propertyRefrences,operationRefrences;
		ClassImpl classForStandardPanel; 
		LayoutManager layout = null;
		addIndentation();
		for(Map.Entry<ClassImpl, Map<String, Map<Object,Object>>> propertiesOperationsEntry:propertiesOperations.entrySet())
		{
			classForStandardPanel=propertiesOperationsEntry.getKey();
			standardPanel=(StandardPanel) classMap.get(classForStandardPanel);
			
			propertiesMap=propertiesOperationsEntry.getValue().get(PROPERTY);
			numberOfElements=propertiesMap.size();
			rootValues=new ArrayList<Object>(propertiesMap.values());
			referencedValues=new ArrayList<Object>();
			publishText("Placing input fields, zoom fields and groups for the "+standardPanel.getLabel()+" Standard panel window");
			addIndentation();
			
			for(Entry<Object,Object> entry:propertiesMap.entrySet())
			{
				mapProperty=(Property) entry.getKey();
				value=entry.getValue();
				if(value instanceof ElementsGroup)
				{
					group=(ElementsGroup) value;
					publishText("Placing input fields, zoom fields and groups into the "+group.getLabel()+" group");
					addIndentation();
					EList<Property> elements=PropertyStereotype.getStereotypeElementsGroupPropertyNestedElements(mapProperty, this);
					if(elements!=null)
						for(Property property:elements)
						{
							if(referencedValues.contains(property))
								throw new Exception("Property "+property.getLabel()+" can not be refrenced in"
										+ " two different nestedElements properties of the "
										+PropertyStereotype.STEREOTYPE_ELEMENTS_GROUP_PROPERTY_NAME
										+" stereotype in the "+classForStandardPanel.getLabel()+" Class element");
							else
							{
								referencedValues.add(property);
								visibleElement=(VisibleElement) propertiesMap.get(property);
								if(visibleElement==null)
									throw new Exception("Property element "+property.getLabel()+" is not in the same "
											+ " Class as the Property element "+mapProperty.getLabel()+" and so it can not "
											+ " be refrenced in the nestedElements property of the "+PropertyStereotype.STEREOTYPE_ELEMENTS_GROUP_PROPERTY_NAME
											+ " stereotype in the "+classForStandardPanel.getLabel()+" Class element");
								else
								{
									rootValues.remove(visibleElement);
									if(visibleElement instanceof ElementsGroup)
										layout=((Composite)visibleElement.getComponent()).getLayoutManager();
									UIPropertyUtil.addVisibleElement(standardPanel, visibleElement);
									ElementsGroupUtil.addVisibleElement(group, visibleElement);
									if(checkContainsElement(group.getVisibleElementList().toArray(), group))
										throw new Exception("Property element "+mapProperty.getLabel()+" is in a loop after"
												+ " adding the Property element "+property.getLabel()+" that is"
												+ " refrenced in the nestedElements property of the "+PropertyStereotype.STEREOTYPE_ELEMENTS_GROUP_PROPERTY_NAME
												+ " stereotype in the "+classForStandardPanel.getLabel()+" Class element");
									if(visibleElement instanceof ElementsGroup)
									{
										((Composite)visibleElement.getComponent()).setLayoutManager(layout);
										publishText("Added "+visibleElement.getLabel()+" group");
									}
									else
										publishText("Added "+visibleElement.getLabel()+" field");
									//visibleElement.setParentGroup(group);
									
									group.update();
									//standardPanel.update();
								}
							}
						}
					removeIndentation(1);
				}
			}
			if(numberOfElements>0)
				if(rootValues.size()==0)
					throw new Exception("Not all the Property elements should be refrenced by the"
							+ " nesteElements property of the "+PropertyStereotype.STEREOTYPE_ELEMENTS_GROUP_PROPERTY_NAME
							+" stereotype in the "+classForStandardPanel.getLabel()+" Class element");
			if(numberOfElements!=checkNumberOfElements(rootValues.toArray(),0))
				throw new Exception("There is a Property element that is refrenced by two "
						+ " nestedElements properties of the "+PropertyStereotype.STEREOTYPE_ELEMENTS_GROUP_PROPERTY_NAME
						+ " stereotype in the "+classForStandardPanel.getLabel()+" Class element");
			ElementsGroup elg = (ElementsGroup) standardPanel.getVisibleElementList().get(1);
			publishText("Placing input fields, zoom fields and groups onto the "+standardPanel.getLabel()+" Standard panel window");
			addIndentation();
			propertyRefrences=false;
			for(Object object:rootValues)
			{
				visibleElement=(VisibleElement) object;
				if(visibleElement instanceof ElementsGroup)
					layout=((Composite)visibleElement.getComponent()).getLayoutManager();
				UIPropertyUtil.addVisibleElement(standardPanel, visibleElement);
				ElementsGroupUtil.addVisibleElement(elg, visibleElement);
				if(visibleElement instanceof ElementsGroup)
				{
					((Composite)visibleElement.getComponent()).setLayoutManager(layout);
					publishText("Added "+visibleElement.getLabel()+" group");
				}
				else
					publishText("Added "+visibleElement.getLabel()+" field");
				//visibleElement.setParentGroup(elg);
				
				elg.update();
				//standardPanel.update();
				propertyRefrences=true;
			}
			if(!propertyRefrences)
				publishText("There where not any input fields, zoom fields and groups to place onto the "+standardPanel.getLabel()+" Standard panel window");
			removeIndentation(2);
			
			propertiesMap=propertiesOperationsEntry.getValue().get(OPERATION);
			numberOfElements=propertiesMap.size();
			rootValues=new ArrayList<Object>(propertiesMap.values());
			referencedValues=new ArrayList<Object>();
			
			publishText("Placing business operations and groups for the "+standardPanel.getLabel()+" Standard panel window");
			addIndentation();
			for(Entry<Object,Object> entry:propertiesMap.entrySet())
				if(entry.getKey() instanceof Operation)
				{
					mapOpeartion=(Operation) entry.getKey();
					value=entry.getValue();
					if(value instanceof ElementsGroup)
					{
						
						group=(ElementsGroup) value;
						publishText("Placing business operations and groups into the "+group.getLabel()+" group");
						addIndentation();
						EList<Operation> elements=OperationStereotype.getStereotypeElementsGroupOperationNestedElements(mapOpeartion, this);
						if(elements!=null)
							for(Operation operation:elements)
							{
								if(referencedValues.contains(operation))
									throw new Exception("Operation "+operation.getLabel()+" can not be refrenced in"
											+ " two different nestedElements properties of the "
											+OperationStereotype.STEREOTYPE_ELEMENTS_GROUP_OPERATION_NAME
											+ " stereotype in the "+classForStandardPanel.getLabel()+" Class element");
								else
								{
									referencedValues.add(operation);
									visibleElement=(VisibleElement) propertiesMap.get(operation);
									if(visibleElement==null)
										throw new Exception("Operation element "+operation.getLabel()+" is not in the same "
												+ " class as the Operation element "+mapOpeartion.getLabel()+" and so it can not "
												+ " be refrenced in the nestedElements property of the "+OperationStereotype.STEREOTYPE_ELEMENTS_GROUP_OPERATION_NAME
												+ " stereotype in the "+classForStandardPanel.getLabel()+" Class element");
									else
									{
										rootValues.remove(visibleElement);
										
										if(visibleElement instanceof ElementsGroup)
											layout=((Composite)visibleElement.getComponent()).getLayoutManager();
										
										UIPropertyUtil.addVisibleElement(standardPanel, visibleElement);
										ElementsGroupUtil.addVisibleElement(group, visibleElement);
										if(checkContainsElement(group.getVisibleElementList().toArray(), group))
											throw new Exception("Operation element "+mapOpeartion.getLabel()+" is in a loop after"
													+ " adding the Operation element "+operation.getLabel()+" that is"
													+ " refrenced in the nestedElements property of the "+OperationStereotype.STEREOTYPE_ELEMENTS_GROUP_OPERATION_NAME
													+ " stereotype in the "+classForStandardPanel.getLabel()+" Class element");
										if(visibleElement instanceof ElementsGroup)
										{
											((Composite)visibleElement.getComponent()).setLayoutManager(layout);
											publishText("Added "+visibleElement.getLabel()+" group");
										}
										else
											publishText("Added "+visibleElement.getLabel()+" operation");
	
										//visibleElement.setParentGroup(group);
										
										group.update();
										//standardPanel.update();
									}
								}
							}
						removeIndentation(1);
					}
				}
			if(numberOfElements>0)
				if(rootValues.size()==0)
					throw new Exception("Not all the Operation elements should be refrenced by the"
							+ " nestedElements property of the "+OperationStereotype.STEREOTYPE_ELEMENTS_GROUP_OPERATION_NAME
							+ " stereotype in the "+classForStandardPanel.getLabel()+" Class element");
			if(numberOfElements!=checkNumberOfElements(rootValues.toArray(),0))
				throw new Exception("There is a Operation element that is refrenced by two "
						+ " nestedElements properties of the "+OperationStereotype.STEREOTYPE_ELEMENTS_GROUP_OPERATION_NAME
						+ " stereotype in the "+classForStandardPanel.getLabel()+" Class element");
			elg = (ElementsGroup) standardPanel.getVisibleElementList().get(2);
			publishText("Placing business operations and groups onto the "+standardPanel.getLabel()+" Standard panel window");
			addIndentation();
			operationRefrences=false;
			for(Object object:rootValues)
			{
				visibleElement=(VisibleElement) object;
				if(visibleElement instanceof ElementsGroup)
					layout=((Composite)visibleElement.getComponent()).getLayoutManager();
				UIPropertyUtil.addVisibleElement(standardPanel, visibleElement);
				ElementsGroupUtil.addVisibleElement(elg, visibleElement);
				if(visibleElement instanceof ElementsGroup)
				{
					((Composite)visibleElement.getComponent()).setLayoutManager(layout);
					publishText("Added "+visibleElement.getLabel()+" group");
				}
				else
					publishText("Added "+visibleElement.getLabel()+" operation");
				//visibleElement.setParentGroup(elg);
				
				elg.update();
				//standardPanel.update();
				operationRefrences=true;
			}
			
			standardPanel.update();
			if(!operationRefrences)
				publishText("There where not any business operations and groups to place onto the "+standardPanel.getLabel()+" Standard panel window");
			removeIndentation(2);
		}
		removeIndentation(1);
		//Organizing input fields, zoom fields, business operations into groups
		publishText("Organized input fields, zoom fields, business operations into groups");
	}

	/**
	 * Calculates the number of all the elements and sub
	 * elements and adds it to the received number. 
	 * Passes through all the received elements.
	 * If an element is of type ElementsGroup it recursively
	 * calls the same method and sends all the sub elements of
	 * the ElementsGroup element.
	 * @param elements  set of elements for which to calculate
	 * the number of all the elements and sub elements
	 * @param number    number from which to start calculating
	 * the number of elements
	 * @return          number of elements summed with with
	 * the number received
	 */
	private int checkNumberOfElements(Object[] elements,int number) {
		int numberOfElements=number;
		ElementsGroup group;
		for(Object object:elements)
			if(object instanceof ElementsGroup)
			{
				group=(ElementsGroup)object;
				numberOfElements++;
				numberOfElements=checkNumberOfElements(group.getVisibleElementList().toArray(),numberOfElements);
			}
			else
				numberOfElements++;
		return numberOfElements;
	}
	
	/**
	 * Checks if any of the elements in a set are equal to the
	 * received element. If any element in the set is of type
	 * ElementsGroup this method is called by send all the sub
	 * elements of the ElementsGroup as the set of elements to
	 * check if any of them is equal to the received element.
	 * @param elements  set of elements to check if any is
	 * equal to the received element
	 * @param element   element that should be checked if it is
	 * contained in the set of elements
	 * @return          <code>true</code> if the received element
	 * is equal to any of the elements, <code>false</code> otherwise
	 */
	private boolean checkContainsElement(Object[] elements,Object element) {
		ElementsGroup group;
		for(Object object:elements)
			if(object.equals(element))
				return true;
			else if(object instanceof ElementsGroup)
			{
				group=(ElementsGroup)object;
				return checkContainsElement(group.getVisibleElementList().toArray(),element);
			}
		return false;
	}
	
	/**
	 * Removes prefix and suffix texts the user has entered before import from 
	 * the names of the package, class, property and operation elements.
	 * @param name               name from which to remove prefix or suffix user has entered
	 * @param typeOfElement      type of element from which to remove text received
	 * @param beforeConversion   if name is before conversion from camel case to human readable
	 * format
	 * @return    name with out the prefix and suffix removed if the type of element
	 * was of the type user wanted to be removed from and if the name contained the text
	 * user entered to be removed.
	 */
	public String removePrefixSuffix(String name,UMLElementsEnum typeOfElement,boolean beforeConversion){
		String newName=name;
		boolean removed=true;
		for(TextToRemove textTR:textsToBeRemoved)
		{
			if(beforeConversion)
				textTR.setUsedAlready(false);
			if(!textTR.isUsedAlready())
				if((textTR.isFromPackageElement()&&typeOfElement.equals(UMLElementsEnum.PACKAGE))
						||(textTR.isFromClassElement()&&typeOfElement.equals(UMLElementsEnum.CLASS))
						||(textTR.isFromPropertyElement()&&typeOfElement.equals(UMLElementsEnum.PROPERTY))
						||(textTR.isFromOperationElement()&&typeOfElement.equals(UMLElementsEnum.OPERATION)))
				{
					removed=false;
					if(textTR.isPrefix())
					{
						if(newName.startsWith(textTR.getText()))
						{
							newName=newName.substring(textTR.getText().length());
							removed=true;
						}
					}
					if(textTR.isSuffix())
					{
						if(newName.endsWith(textTR.getText()))
						{
							newName=newName.substring(0,newName.length()-textTR.getText().length());
							removed=true;
						}
					}
					if(removed&&beforeConversion)
					{
						textTR.setUsedAlready(true);
					}	
					if(removed&&!beforeConversion)
					{
						newName=newName.trim();
						if(newName.length()==1)
							newName=newName.toUpperCase();
						else if(newName.length()>1)
							newName=Character.toUpperCase(newName.charAt(0))+newName.substring(1);
					}
				}
		}
		return newName;
	}
}
