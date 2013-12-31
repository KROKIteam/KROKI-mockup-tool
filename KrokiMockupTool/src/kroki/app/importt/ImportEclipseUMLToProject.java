package kroki.app.importt;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.uml.UMLResourcesUtil;
import kroki.commons.camelcase.NamingUtil;
import kroki.mockup.model.Composite;
import kroki.mockup.model.layout.VerticalLayoutManager;
import kroki.profil.ComponentType;
import kroki.profil.association.Next;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.Report;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ContentHandler;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.DataType;
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
import org.eclipse.uml2.uml.internal.impl.ModelImpl;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * Class that implements import functionality for importing files with Eclipse UML model to Kroki project. 
 * @author Zeljko Ivkovic
 *
 */
public class ImportEclipseUMLToProject {
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
	 * Constructor that creates an object for importing 
	 * files with Eclipse UML model to Kroki project.
	 * Receives a file object that represents the file that should be imported.
	 * @param file  file to be imported.
	 */
	public ImportEclipseUMLToProject(File file){
		this.file=file;
	}
	
	/**
	 * List of Eclipse UML {@link Association} elements for which to create zoom fields in Kroki.
	 */
	private List<Association> associations;
	/**
	 * Saves all the Kroki StandardPanel elements created for the corresponding Eclipse UML {@link ClassImpl} elements.
	 */
	private Map<ClassImpl, VisibleClass> classMap;
	/**
	 * Contains the Kroki project element and its sub elements, that will be created during the import functionality.
	 */
	private BussinesSubsystem project;
	
	/**
	 * Imports file with the Eclipse UML diagram that was received in the constructor.
	 * @throws Exception  if Eclipse UML model can not be loaded from the file and if the Kroki project files can not be created.
	 */
	public void executeImport() throws Exception{
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Loading Eclipse UML model from file.", 0);
	    try{
		    /**/
		    Model model=loadFile(file.getAbsolutePath());
		    
		    if(model!=null)
		    {
		    	KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Eclipse UML model loaded successfully.", 0);
			    KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Creating project for Eclipse UML model.", 0);
			    project=null;
			    namingUtil = new NamingUtil();
			    associations=new ArrayList<Association>();
			    classMap=new HashMap<ClassImpl, VisibleClass>();
				extractModel(model);
				if(project!=null)
				{
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Project for Eclipse UML model created successfully",0);
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Importing Eclipse UML diagram finished successfully.", 0);
					KrokiMockupToolApp.getInstance().getWorkspace().addPackage(project);
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getTree().updateUI();
				}else{
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Error occurred while creating project from Eclipse UML model.", 3);
					throw new Exception("Error while creating project from Eclipse UML model.");
				}
		    }
	    }catch(Exception e)
	    {
	    	//Version of the Eclipse UML model is not recognized
	    	KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText(e.getMessage(), 3);
	    	
	    }
		    
	}
	
	/**
	 * Creates an Eclipse UML model elements from a file that contains a Eclipse UML diagram.
	 *  
	 * @param filePath  file that contains Eclipse UML model.
	 * @return  Eclipse UML model with elements that is created from the file. 
	 * @throws Exception  if the Eclipse UML diagram saved in the file contains unrecognizable elements.
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
	 * @param model  Eclipse UML model from which to create a Kroki project.
	 */
	private  void extractModel(Model model){
		if(model!=null)
		{
			project=new BussinesSubsystem(namingUtil.fromCamelCase(model.getName()), true, ComponentType.MENU, null);
			extractPackage(model, project);
			createAssociations();
		}else
		{
			KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("File is in unrecognizable format.", 3);
			KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getConsole().displayText("Importing Eclipse UML diagram failed", 3);
		}
	}
		
	/** 
	 * For the given Eclipse UML Package element iterates through Package, Class and Association sub elements that it  contains.
	 * For the Package sub elements this method creates Kroki BussinesSubsystem elements, for the Class elements it will create
	 * StandardPanel elements and Association elements will be added to the list that will later be used for creating zoom fields.
	 * Created elements are added to the received subsystemOwner Kroki BussinesSubsystem element that was created for the received 
	 * Package element.
	 * @param packageElem  Eclipse UML Package element to be imported.
	 * @param subsystemOwner  Kroki BussinesSubsystem element that corresponds to the received Package element. 
	 */
	private void extractPackage(Package packageElem,BussinesSubsystem subsystemOwner){
		EList<Package> packages=packageElem.getNestedPackages();
		for(Package packageHelp:packages)
		{
			BussinesSubsystem pack = createBussinesSubsystem(packageHelp.getName(),subsystemOwner);
			extractPackage(packageHelp,pack);
		}
		
		Association association;
		ClassImpl classHelper;
		EList<Type> types= packageElem.getOwnedTypes();
		for(Type type:types)
		{
			if(type instanceof ClassImpl)
			{
				classHelper=(ClassImpl)type;
				VisibleClass panel = createVisibleClass(classHelper.getName(),subsystemOwner);
				classMap.put(classHelper, panel);
				extractClass(classHelper,panel);
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
	 * Creates a Kroki StandardPanel element with the received name and adds it
	 * as a sub element to the classOwner BussinesSubsystem element. Name is transfered from a
	 * camel case notation to the notation that uses empty space for separating words.
	 * @param name  name of the StandardPanel element to be created.
	 * @param classOwner  BussinesSubsystem element to which the created StandradPanel element will be added.
	 * @return  created StandardPanel element.
	 */
	public VisibleClass createVisibleClass(String name,BussinesSubsystem classOwner){
		StandardPanel panel = new StandardPanel();
		panel.setLabel(namingUtil.fromCamelCase(name));
		panel.getComponent().setName(namingUtil.fromCamelCase(name));
		panel.getPersistentClass().setName(namingUtil.toCamelCase(panel.getLabel(), false));
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(1);
		((Composite) gr.getComponent()).setLayoutManager(new VerticalLayoutManager());
		((Composite) gr.getComponent()).layout();
		gr.update();
		panel.update();
		classOwner.addOwnedType(panel);
		panel.setUmlPackage(classOwner);
		return panel;
	}
	
	/**
	 * Creates a BussinesSubsystem element with the received name and adds it as a 
	 * sub element to the received BussinesSubsystem element.
	 * @param name  name of the BussinesSubsystem element to be created.
	 * @param subsystemOwner  BussinesSubsystem element to which the created BussinesSubsystem element 
	 * will be added.
	 * @return  created BussinesSubsystem element.
	 */
	public BussinesSubsystem createBussinesSubsystem(String name, BussinesSubsystem subsystemOwner){
		BussinesSubsystem pack = new BussinesSubsystem(subsystemOwner);
		pack.setLabel(namingUtil.fromCamelCase(name));
		subsystemOwner.addNestedPackage(pack);
		return pack;
	}
	
	/**
	 * For the received Eclipse UML Class element iterates through the Operation, Attribute and Association sub elements.
	 * For Operation elements a corresponding Report elements are created, for the Attribute element a corresponding
	 * VisibleProperty is created and Association elements are added to the list that will be used to create zoom fields.
	 * Created elements are added to the received panel element of type VisibleClass. 
	 * @param classObject  Eclipse UML Class element to be imported.
	 * @param panel  Kroki VisibleClass element that corresponds to the StandardPanel element to which the
	 * created elements will be added.
	 */
	private void extractClass(org.eclipse.uml2.uml.Class classObject,VisibleClass panel) {
		int group;//(0-toolbar, 1-Properties, 2-Operations)
		group=2;
		VisibleOperation visibleOperation;
		ElementsGroup gr;
		EList<Operation> operations=classObject.getOwnedOperations();
		for(Operation operation:operations)
		{
			visibleOperation=createVisibleOperation(operation.getName(), panel);
		}
		
		group=1;
		Type dataType=null;
		Enumeration enumObject;
		VisibleProperty property;
		StringBuilder enumerationLiterals;
		EList<Property> properties=classObject.getAttributes();
		for(Property attribute:properties)
		{
			Association association=attribute.getAssociation();
			if(association!=null)
			{
				if(!associations.contains(association))
				{
					associations.add(association);
				}
				continue;
			}
			dataType=attribute.getType();
			if(dataType==null)
			{
				createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"String", panel);
			}else if(dataType instanceof Enumeration)
			{
				enumObject=(Enumeration)dataType;
				property=createVisibleProperty(attribute.getName(), true, ComponentType.COMBO_BOX,"", panel);
				enumerationLiterals=new StringBuilder();
				for(EnumerationLiteral literal:enumObject.getOwnedLiterals())
					enumerationLiterals.append(literal.getName()+";");
				property.setEnumeration(enumerationLiterals.toString());
			}else
			{
				if(dataType.getName().toLowerCase().contains("boolean"))
					createVisibleProperty(attribute.getName(), true, ComponentType.CHECK_BOX,"", panel);
				else if(dataType.getName().toLowerCase().contains("bigdecimal")||
						dataType.getName().toLowerCase().contains("double")||
						dataType.getName().toLowerCase().contains("float"))
					createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"BigDecimal", panel);
				else if(dataType.getName().toLowerCase().contains("string"))
				{
					if(attribute.getUpper()>50)
						createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_AREA,"", panel);
					else
						createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"String", panel);
				}
				else if(dataType.getName().toLowerCase().contains("int"))
					createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"Integer", panel);
				else if(dataType.getName().toLowerCase().contains("long"))
					createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"Long", panel);
				else if(dataType.getName().toLowerCase().contains("date"))
					createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"Date", panel);
				else
					createVisibleProperty(attribute.getName(), true, ComponentType.TEXT_FIELD,"String", panel);
			}
		}
		
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
	 * Creates a Report element with the received name and adds it as a sub element to the
	 * received VisibleClass element. VisibleClass element is represented by the
	 * StandardPanel element.
	 * @param name  name of the Report element to be created.
	 * @param panel  StandardPanel element to which the created Report element will be added.
	 * @return  created Report element.
	 */
	protected VisibleOperation createVisibleOperation(String name,VisibleClass panel){
		int group;//(0-toolbar, 1-Properties, 2-Operations)
		group=2;
		VisibleOperation visibleOperation = new Report(namingUtil.fromCamelCase(name), true, ComponentType.BUTTON);
		panel.addVisibleElement(visibleOperation);
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(group);
		gr.addVisibleElement(visibleOperation);
		visibleOperation.setParentGroup(gr);
		visibleOperation.setUmlClass(panel);
		return visibleOperation;
	}
	
	/**
	 * Creates a VisibleProperty element with the properties received and adds it as a sub element
	 * of the StandardPanel element. 
	 * @param label  name of the VisibleProperty to be created.
	 * @param visible  if the VisibleProperty created should be visible.
	 * @param type  type of the VisibleProperty element.
	 * @param dataType  extra property for determining type of the input fields that allow input
	 * of characters.
	 * @param panel  StandardPanel element to which to add the created VisibleProperty element.
	 * @param lower  lower cardinality of the created VisibleProperty element.
	 * @param upper  upper cardinality of the created VisibleProperty element.
	 * @return  created VisibleProperty element.
	 */
	private VisibleProperty createVisibleProperty(String label, boolean visible, ComponentType type,String dataType, VisibleClass panel,int lower, int upper){
		VisibleProperty property = createVisibleProperty(label, visible, type, dataType, panel);
		if(lower>=1)
		property.setLower(lower);
		if(upper>1)
		property.setUpper(upper);
		return property;
	}
	
	/**
	 * Creates a VisibleProperty element with the properties received and adds it as a sub element
	 * of the StandardPanel element. 
	 * @param label  name of the VisibleProperty to be created.
	 * @param visible  if the VisibleProperty created should be visible.
	 * @param type  type of the VisibleProperty element.
	 * @param dataType  extra property for determining type of the input fields that allow input
	 * of characters.
	 * @param panel  StandardPanel element to which to add the created VisibleProperty element.
	 * @return  created VisibleProperty element.
	 */
	private VisibleProperty createVisibleProperty(String label, boolean visible, ComponentType type,String dataType, VisibleClass panel){
		int group=1;
		VisibleProperty property = new VisibleProperty(namingUtil.fromCamelCase(label), visible, type);
		if(type == ComponentType.TEXT_FIELD) {
			property.setDataType(dataType);
		}
		property.setColumnLabel(namingUtil.toDatabaseFormat(panel.getLabel(), label));
		panel.addVisibleElement(property);
		ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(group);
		gr.addVisibleElement(property);
		gr.update();
		panel.update();
		property.setParentGroup(gr);
		property.setUmlClass(panel);
		return property;
	}
	
	/**
	 * Creates zoom fields and next links using the list of
	 * Association elements. For every Association that has cardinality
	 * of one of the ends set to 0..* and the other set to 0..1. a 
	 * corresponding zoom filed and next link are created.
	 */
	private void createAssociations(){
		Property first,second;
		for(Association association:associations){
			first=association.getAllAttributes().get(0);
			second=association.getAllAttributes().get(1);
			if(first.getUpper()==LiteralUnlimitedNatural.UNLIMITED)
			{
				if(second.getUpper()==LiteralUnlimitedNatural.UNLIMITED)
					System.out.println("Many-to-Many not suported");
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
					System.out.println("One-to-One not suported");
			}
		}
	}
	
	/**
	 * Creates a Kroki Zoom element using the Eclipse UML Property
	 * elements that represent the ends of the Eclipse UML 
	 * Association element for which a Zoom element is being 
	 * created. Zoom element corresponds to the zoom field.
	 * @param first  Property element that has cardinality set to 0..*.
	 * @param second  Property element that has cardinality set to 0..1.
	 */
	private void createZoom(Property first,Property second){
		int group=1;
		ClassImpl firstClass=(ClassImpl) first.getType();
		ClassImpl secondClass=(ClassImpl) second.getType();
		VisibleClass firstVisibleClass=classMap.get(secondClass);
		VisibleClass secondVisibleClass=classMap.get(firstClass);
		
		VisibleProperty visibleProperty=createVisibleProperty(namingUtil.fromCamelCase(first.getName()), true, ComponentType.COMBO_BOX, "", firstVisibleClass);
		ElementsGroup elg = (ElementsGroup) firstVisibleClass.getVisibleElementList().get(group);
        if (elg != null) {
            int position = elg.indexOf(visibleProperty);
            elg.removeVisibleElement(visibleProperty);
            firstVisibleClass.removeVisibleElement(visibleProperty);

            Zoom zoom = new Zoom(visibleProperty);
            zoom.setActivationPanel(firstVisibleClass);
            elg.addVisibleElement(position, zoom);
            firstVisibleClass.addVisibleElement(zoom);

            elg.update();
            firstVisibleClass.update();
            
            zoom.setTargetPanel(secondVisibleClass);
            zoom.setParentGroup(elg);
            zoom.setUmlClass(firstVisibleClass);
        }
	}
	
	/**
	 * Creates a Kroki Next element using the Eclipse UML Property
	 * elements that represent the ends of the Eclipse UML
	 * Association element for which a Next element is being 
	 * created. Next element corresponds to the next link.
	 * @param first  Property element that has cardinality set to 0..1.
	 * @param second Property element that has cardinality set to 0..*.
	 */
	private void createNext(Property first,Property second){
		ClassImpl firstClass=(ClassImpl) first.getType();
		ClassImpl secondClass=(ClassImpl) second.getType();
		VisibleClass firstVisibleClass=classMap.get(secondClass);
		VisibleClass secondVisibleClass=classMap.get(firstClass);
		
		ElementsGroup group = (ElementsGroup) firstVisibleClass.getVisibleElementList().get(2);
		String name=first.getName();
		if(name!=null)
			if(name.isEmpty())
				name="Next"+group.getVisibleElementList().size();
			
		Next next = new Next(namingUtil.fromCamelCase(name));		
		next.setActivationPanel(firstVisibleClass);
		firstVisibleClass.addVisibleElement(next);
		
		group.addVisibleElement(next);
		group.update();
		firstVisibleClass.update();
		
		next.setTargetPanel(secondVisibleClass);
	}

}
