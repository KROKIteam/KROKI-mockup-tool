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
import kroki.profil.panel.VisibleClass;
import kroki.profil.utils.DatabaseProps;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;

public class BussinesSubsystem extends VisibleElement implements UmlPackage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UmlPackage nestingPackage;
	private List<VisibleElement> ownedElement = new ArrayList<VisibleElement>();
	private List<UmlPackage> nestedPackage = new ArrayList<UmlPackage>();
	private List<UmlType> ownedType = new ArrayList<UmlType>();
	private DatabaseProps DBConnectionProps = new DatabaseProps();
	private File file;
	private Object graphPackage;
	private Object menu;
	private boolean labelToCode = true;

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
			sortedInsert((VisibleElement) umlType);
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
			sortedInsert((VisibleElement) umlPackage);
		}
	}

	public void removeNestedPackage(UmlPackage umlPackage) {
		if (nestedPackage.contains(umlPackage)) {
			nestedPackage.remove(umlPackage);
			ownedElement.remove((VisibleElement) umlPackage);
		}
	}

	private void sortedInsert(VisibleElement element){
		for (int i = 0; i < ownedElementCount(); i++){
			VisibleElement el = ownedElement.get(i);
			if (element.getClass() != el.getClass()){
				if (element instanceof UmlPackage){
					ownedElement.add(i, element);
					return;
				}
				continue;
			}
			if (el.getLabel().toLowerCase().compareTo(element.getLabel().toLowerCase()) > 0){
				ownedElement.add(i, element);
				return;
			}
		}
		ownedElement.add(element);
	}
	
	 @Override
	 public void setLabel(String label) {
		 super.setLabel(label);
		 BussinesSubsystem pack = (BussinesSubsystem) nestingPackage;
		 
		 if (pack != null &&  pack.ownedType().contains(this)){
			 pack.removeNestedPackage(this);
			 pack.addNestedPackage(this);
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


	public Object getMenu() {
		return menu;
	}

	public void setMenu(Object menu) {
		this.menu = menu;
	}
	
	public boolean isLabelToCode() {
		return labelToCode;
	}

	public void setLabelToCode(boolean labelToCode) {
		this.labelToCode = labelToCode;
	}


}
