/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.subsystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.association.VisibleAssociationEnd;
import kroki.profil.association.Zoom;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.utils.DatabaseProps;
import kroki.profil.utils.visitor.AllPosibleHierarchyPanels;
import kroki.profil.utils.visitor.AllPosibleNextPanels;
import kroki.profil.utils.visitor.AllPosibleNexts;
import kroki.profil.utils.visitor.AllPosibleZoomPanels;
import kroki.profil.utils.visitor.ContainingPanels;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;

/**
 * Klasa koja se koristi za definisanje
 * poslovnih podsistema i referata, na nacin definisan HCI standardom.
 * @author Vladan Marsenic (vladan.marsenic@gmail.com)
 */
public class BussinesSubsystem extends VisibleElement implements UmlPackage {
	private UmlPackage nestingPackage;
	private List<VisibleElement> ownedElement = new ArrayList<VisibleElement>();
	private List<UmlPackage> nestedPackage = new ArrayList<UmlPackage>();
	private List<UmlType> ownedType = new ArrayList<UmlType>();
	private DatabaseProps DBConnectionProps = new DatabaseProps();
	private File file;
	private Object graphPackage;

	public BussinesSubsystem(BussinesSubsystem owner) {
		super();
		this.nestingPackage = owner;
	}

	public BussinesSubsystem(String label, boolean visible, ComponentType componentType, BussinesSubsystem owner) {
		super(label, visible, componentType);
		this.nestingPackage = owner;
	}

	/**************/
	/*JAVNE METODE*/
	/**************/
	public VisibleElement getOwnedElementAt(int index) {
		if (index >= 0 && index < ownedElement.size()) {
			return ownedElement.get(index);
		} else {
			return null;
		}
	}

	public int ownedElementCount() {
		return ownedElement.size();
	}

	public int indexOf(VisibleElement visibleElement) {
		return ownedElement.indexOf(visibleElement);
	}

	public void addOwnedType(UmlType umlType) {
		if (!ownedType.contains(umlType)) {
			ownedType.add(umlType);
			umlType.setUmlPackage(this);
			ownedElement.add((VisibleElement) umlType);
		}
	}

	public void removeOwnedType(UmlType umlType) {
		if (ownedType.contains(umlType)) {
			ownedType.remove(umlType);
			ownedElement.remove((VisibleElement) umlType);
		}
	}

	public void addNestedPackage(UmlPackage umlPackage) {
		if (!nestedPackage.contains(umlPackage)) {
			nestedPackage.add(umlPackage);
			umlPackage.setNestingPackage(this);
			ownedElement.add((VisibleElement) umlPackage);
		}
	}

	public void removeNestedPackage(UmlPackage umlPackage) {
		if (nestedPackage.contains(umlPackage)) {
			nestedPackage.remove(umlPackage);
			ownedElement.remove((VisibleElement) umlPackage);
		}
	}

	public List<UmlPackage> nestedPackage() {
		return nestedPackage;
	}

	public UmlPackage nestingPackage() {
		return nestingPackage;
	}

	public List<UmlType> ownedType() {
		return ownedType;
	}

	public void setNestingPackage(UmlPackage nestingPackage) {
		this.nestingPackage = nestingPackage;
	}

	public DatabaseProps getDBConnectionProps() {
		return DBConnectionProps;
	}

	public void setDBConnectionProps(DatabaseProps dBConnectionProps) {
		DBConnectionProps = dBConnectionProps;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}



	public List<VisibleAssociationEnd> allAssociationEnds(){
		ArrayList<VisibleAssociationEnd> ret = new ArrayList<VisibleAssociationEnd>();
		allAssociationEnds(this, ret);
		return ret;
	}
	

	public List<VisibleClass> allPanels(){
		ArrayList<VisibleClass> ret = new ArrayList<VisibleClass>();
		allPanels(this, ret);
		return ret;
	}
	

	/**
	 * Finds all visible association ends which are contained by the subsystem and its nested packages
	 * @param pack
	 * @param ret
	 */
	 protected void allAssociationEnds(BussinesSubsystem pack, List<VisibleAssociationEnd> ret){
		for (UmlType ownedType : pack.ownedType()){
			if (!(ownedType instanceof VisibleClass))
				continue;
			VisibleClass visibleClass = (VisibleClass)ownedType;
			for (VisibleAssociationEnd end : visibleClass.containedAssociationEnds())
				ret.add(end);
		}
		for (UmlPackage ownedPackage : pack.nestedPackage())
			allAssociationEnds((BussinesSubsystem) ownedPackage, ret);
	 }

	 protected void allPanels(BussinesSubsystem pack, List<VisibleClass> ret){
		for (UmlType ownedType : pack.ownedType()){
			if ((ownedType instanceof VisibleClass))
				ret.add((VisibleClass) ownedType);
		}
		for (UmlPackage ownedPackage : pack.nestedPackage())
			allPanels((BussinesSubsystem) ownedPackage, ret);
	 }

	 

	 /****************************************************/
	 /*IMPLEMENTIRANE METODE INTERFEJSA VisitingSubsystem*/
	 /****************************************************/
	 public void accept(AllPosibleZoomPanels visitor) {
		 visitor.addAllObjects(ownedType);
		 for (int i = 0; i < nestedPackage.size(); i++) {
			 BussinesSubsystem subsystem = (BussinesSubsystem) nestedPackage.get(i);
			 subsystem.accept(visitor);
		 }
	 }

	 public void accept(AllPosibleNextPanels visitor) {
		 visitor.addAllObjects(ownedType);
		 for (int i = 0; i < nestedPackage.size(); i++) {
			 BussinesSubsystem subsystem = (BussinesSubsystem) nestedPackage.get(i);
			 subsystem.accept(visitor);
		 }
	 }

	 public void accept(AllPosibleNexts visitor) {
		 for (UmlType owned : ownedType)
			 if (owned instanceof StandardPanel)
				 visitor.addAllObjects(((StandardPanel)owned).containedZooms());

		 for (int i = 0; i < nestedPackage.size(); i++) {
			 BussinesSubsystem subsystem = (BussinesSubsystem) nestedPackage.get(i);
			 subsystem.accept(visitor);
		 }
	 }

	 public void accept(AllPosibleHierarchyPanels visitor) {
		 visitor.addAllObjects(ownedType);
		 for (int i = 0; i < nestedPackage.size(); i++) {
			 BussinesSubsystem subsystem = (BussinesSubsystem) nestedPackage.get(i);
			 subsystem.accept(visitor);
		 }
	 }

	 public void accept(ContainingPanels visitor) {
		 visitor.addAllObjects(ownedType);
		 for (int i = 0; i < nestedPackage.size(); i++) {
			 BussinesSubsystem subsystem = (BussinesSubsystem) nestedPackage.get(i);
			 subsystem.accept(visitor);
		 }
	 }

	 @Override
	 public String toString() {
		 return label;
	 }

	public Object getGraphPackage() {
		return graphPackage;
	}

	public void setGraphPackage(Object graphPackage) {
		this.graphPackage = graphPackage;
	}


}
