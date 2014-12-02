/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.panel;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kroki.mockup.model.Composite;
import kroki.profil.BusinessProcessModelingSubject;
import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.Hierarchy;
import kroki.profil.association.Next;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.operation.Report;
import kroki.profil.operation.Transaction;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.property.Persistent;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlProperty;

/**
 * Označava klasu koja se preslikava na panel (obični ili tabulatorski) u okviru korisničkog interfejsa aplikacije.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class VisibleClass extends BusinessProcessModelingSubject implements UmlClass {

	private static final long serialVersionUID = 1L;
	
	/**Indikator modalnosti klase*/
	protected boolean modal = true;
	/**Lista svih elemenata koje poseduje klasa*/
	protected List<VisibleElement> visibleElementList = new ArrayList<VisibleElement>();
	/*OBELEÅ½JA METAKLASE CLASS*/
	protected boolean isAbstract;
	protected UmlPackage umlPackage;
	//Component counter map used for incremental component naming
	HashMap<ComponentType, Integer> componentCounts;

	/*****************/
	/*Konstruktori   */
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

	/**************/
	/*JAVNE METODE*/
	/**************/
	



	 /**
	  * OsveÅ¾ava stanje svojih grafiÄ�kih komponenti.
	  */
	 @Override
	 public void update() {
		 super.update();
	 }

	 /**
	  * Gets number of components with specified type
	  */
	 public Integer getComponentCount(ComponentType type) {
		 Integer count = 0;
		 if(componentCounts.get(type) != null) {
			 count = componentCounts.get(type);
		 }
		 return count;
	 }

	 /**
	  * Increments number of components with specfied type and returns incremented count
	  * If that components type is not found in count map, it is added with count set to 1
	  */
	 public Integer incrementCount(ComponentType type) {
		 Integer count = componentCounts.get(type);
		 if(count != null) {
			 count++;
			 componentCounts.put(type, count);
			 return count;
		 }else {
			 componentCounts.put(type, 1);
			 return 1;
		 }
	 }

	 public void decrementCount(ComponentType type) {
		 Integer count = componentCounts.get(type);
		 if(count != null && count > 0) {
			 count--;
			 componentCounts.put(type, count);
		 }
	 }
	 /*******************************************/
	 /*Implementirane metode interfejsa UmlClass*/
	 /*******************************************/
	 public boolean isAbstract() {
		 return isAbstract;
	 }

	 /**VraÄ‡a listu svih atrubuta klase koji implmentiraju interfejs UmlProperty*/
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

	 /**VraÄ‡a listu svih operacija klase koji implmentiraju interfejs UmlOperation*/
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
	 /*GETERI I SETERI*/
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
}