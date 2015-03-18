package kroki.profil.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kroki.profil.BusinessProcessModelingSubject;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlProperty;

/**
 * Represents a panel within user interface of the application 
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class VisibleClass extends BusinessProcessModelingSubject implements UmlClass {

	private static final long serialVersionUID = 1L;
	
	/**Indicates if the class is modal*/
	protected boolean modal = true;
	/**List of all elements contained by the class*/
	protected List<VisibleElement> visibleElementList = new ArrayList<VisibleElement>();
	/*METACLASS CLASS properties*/
	protected boolean isAbstract;
	protected UmlPackage umlPackage;
	//Component counter map used for incremental component naming
	protected HashMap<ComponentType, Integer> componentCounts;

	/*****************/
	/*Constructors   */
	/*****************/
	public VisibleClass(boolean modal) {
		super();
		this.modal = modal;
		componentCounts = new HashMap<ComponentType, Integer>();
	}

	public VisibleClass(String label, boolean visible, ComponentType componentType, boolean modal) {
		super(label, visible, componentType);
		this.modal = modal;
		componentCounts = new HashMap<ComponentType, Integer>();
	}

	public VisibleClass() {
		super();
		this.modal = true;
		componentCounts = new HashMap<ComponentType, Integer>();
	}


	 /**
	  * Refreshes states of its graphical components
	  */
	 @Override
	 public void update() {
		 super.update();
	 }

	
	 /*******************************************/
	 /*UmlClass interface methods*/
	 /*******************************************/
	 public boolean isAbstract() {
		 return isAbstract;
	 }

	 /**Returns a list of all attributes of the class which implement <code>UmlProperty</code> interface*/
	 public List<UmlProperty> ownedAttribute() {
		 List<UmlProperty> umlPropertyList = new ArrayList<UmlProperty>();
		 for (int i = 0; i < visibleElementList.size(); i++) {
			 VisibleElement visibleElement = visibleElementList.get(i);
			 if (visibleElement instanceof UmlProperty) {
				 umlPropertyList.add((UmlProperty) visibleElement);
			 }
		 }
		 return umlPropertyList;
	 }

	 
	 /**Returns a list of all attributes of the class which implement <code>UmlOperation</code> interface*/
	 public List<UmlOperation> ownedOperation() {
		 List<UmlOperation> umlOperationList = new ArrayList<UmlOperation>();
		 for (int i = 0; i < visibleElementList.size(); i++) {
			 VisibleElement visibleElement = visibleElementList.get(i);
			 if (visibleElement instanceof UmlOperation) {
				 umlOperationList.add((UmlOperation) visibleElement);
			 }
		 }
		 return umlOperationList;
	 }


	 @Override
	 public void setLabel(String label) {
		 super.setLabel(label);
		 BussinesSubsystem pack = (BussinesSubsystem) umlPackage;
		 
		 if (pack != null &&  pack.ownedType().contains(this)){
			 pack.removeOwnedType(this);
			 pack.addOwnedType(this);
		 }
	 }

	 public void setAbstract(boolean isAbstract) {
		 this.isAbstract = isAbstract;
	 }

	 public List<UmlClass> superClass() {
		 return null;
	 }

	 public UmlPackage umlPackage() {
		 return umlPackage;
	 }

	 public void setUmlPackage(UmlPackage umlPackage) {
		 this.umlPackage = umlPackage;
	 }

	 /*****************/
	 /*GETTERS AND SETTERS*/
	 /*****************/
	 public boolean isModal() {
		 return modal;
	 }

	 public void setModal(boolean modal) {
		 this.modal = modal;
	 }

	 public List<VisibleElement> getVisibleElementList() {
		 return visibleElementList;
	 }

	 public void setVisibleElementList(List<VisibleElement> visibleElementList) {
		 this.visibleElementList = visibleElementList;
	 }

	 @Override
	 public String toString() {
		 return label;
	 }

	public HashMap<ComponentType, Integer> getComponentCounts() {
		return componentCounts;
	}
}