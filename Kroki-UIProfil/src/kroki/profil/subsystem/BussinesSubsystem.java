package kroki.profil.subsystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kroki.profil.ComponentType;
import kroki.profil.VisibleElement;
import kroki.profil.utils.DatabaseProps;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlType;

/**
 * Class represents a business subsystem i.e. a package
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class BussinesSubsystem extends VisibleElement implements UmlPackage {

	private static final long serialVersionUID = 1L;
	
	/**Parent package which contains it*/
	private UmlPackage nestingPackage;
	/**Contained visible elements*/
	private List<VisibleElement> ownedElement = new ArrayList<VisibleElement>();
	/**Contained packages*/
	private List<UmlPackage> nestedPackage = new ArrayList<UmlPackage>();
	/**Contained elements*/
	private List<UmlType> ownedType = new ArrayList<UmlType>();
	/**Database properties*/
	private DatabaseProps DBConnectionProps = new DatabaseProps();
	/**File where the project is saved. Used if the project is at the top of the hierarchy*/
	private File file;
	/**Graph edit project connected to the project (if the project is at the top of the hierarchy*/
	private Object graphPackage;
	/**Path to the exported Eclipse project*/
	private File eclipseProjectPath; 	
	/**Menu connected to the project*/
	private Object menu;
	/**Indicates if the label is used to set the code*/
	private boolean labelToCode = true;
	private String projectDescription;

	public BussinesSubsystem(BussinesSubsystem owner) {
		super();
		this.nestingPackage = owner;
	}

	public BussinesSubsystem(String label, boolean visible, ComponentType componentType, BussinesSubsystem owner) {
		super(label, visible, componentType);
		this.nestingPackage = owner;
		this.projectDescription = "This application is a prototype generated from KROKI specification. Please log in to continue.";
	}

	/**************/
	/*PUBLIC METHODS*/
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

	/**Returns of the index of the visible element within the package*/
	public int indexOf(VisibleElement visibleElement) {
		return ownedElement.indexOf(visibleElement);
	}

	/**Adds new element*/
	public void addOwnedType(UmlType umlType) {
		if (!ownedType.contains(umlType)) {
			ownedType.add(umlType);
			umlType.setUmlPackage(this);
			sortedInsert((VisibleElement) umlType);
		}
	}

	/**Removes the element*/
	public void removeOwnedType(UmlType umlType) {
		if (ownedType.contains(umlType)) {
			ownedType.remove(umlType);
			ownedElement.remove((VisibleElement) umlType);
		}
	}

	/**Adds a new package*/
	public void addNestedPackage(UmlPackage umlPackage) {
		if (!nestedPackage.contains(umlPackage)) {
			nestedPackage.add(umlPackage);
			umlPackage.setNestingPackage(this);
			sortedInsert((VisibleElement) umlPackage);
		}
	}

	/**Removes a contained package*/
	public void removeNestedPackage(UmlPackage umlPackage) {
		if (nestedPackage.contains(umlPackage)) {
			nestedPackage.remove(umlPackage);
			ownedElement.remove((VisibleElement) umlPackage);
		}
	}

	/**Inserts a new element and retains the sorted order*/
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

	public File getEclipseProjectPath() {
		return eclipseProjectPath;
	}

	public void setEclipseProjectPath(File eclipseProjectPath) {
		this.eclipseProjectPath = eclipseProjectPath;
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
	

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
}
