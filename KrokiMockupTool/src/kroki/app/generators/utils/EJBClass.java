package kroki.app.generators.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class is used to define mapping of persistent classes of UI profile to a class
 * inside a freemarker template. Helper class to make generation of persistent layer easier and
 * is not a part of the UI profile.
 * @author Milorad Filipović
 */
public class EJBClass {
	
	private String classPackage;
	private String subsystem;
	private String name;
	private String tableName;
	private String label;
	private ArrayList<EJBAttribute> attributes;
	private ArrayList<Constraint> constraints = new ArrayList<Constraint>();
	private List<String> importedPackages = new ArrayList<String>();
	private ArrayList<Operation> operations = new ArrayList<Operation>();
//	private ArrayList<Attribute> attributes;
//	private ArrayList<ManyToOneAttribute> manyToOneAttributes;
//	private ArrayList<OneToManyAttribute> oneToManyAttributes;
//	
//	public EJBClass(String classPackage, String subsys, String name, String tableName, String label,
//			ArrayList<Attribute> attributes,
//			ArrayList<ManyToOneAttribute> manyToOneAttributes,
//			ArrayList<OneToManyAttribute> oneToManyAttributes) {
//		super();
//		this.classPackage = classPackage;
//		this.subsystem = subsys;
//		this.name = name;
//		this.tableName = tableName;
//		this.label = label;
//		this.attributes = attributes;
//		this.manyToOneAttributes = manyToOneAttributes;
//		this.oneToManyAttributes = oneToManyAttributes;
//	}
	
	public EJBClass(String classPackage, String subsystem, String name, String tableName, String label, ArrayList<EJBAttribute> attributes) {
		this.classPackage = classPackage;
		this.subsystem = subsystem;
		this.name = name;
		this.tableName = tableName;
		this.label = label;
		this.attributes = attributes;
	}

	
	public EJBClass(String classPackage, String subsystem, String name, String tableName, String label, ArrayList<EJBAttribute> attributes, ArrayList<Constraint> constraints) {
		this.classPackage = classPackage;
		this.subsystem = subsystem;
		this.name = name;
		this.tableName = tableName;
		this.label = label;
		this.attributes = attributes;
		this.constraints = constraints;
	}

	
	public String getClassPackage() {
		return classPackage;
	}

	public void setClassPackage(String classPackage) {
		this.classPackage = classPackage;
	}

	public String getSubsystem() {
		return subsystem;
	}

	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}

	public String getName() {
		return name;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	public ArrayList<EJBAttribute> getAttributes() {
		return attributes;
	}


	public void setAttributes(ArrayList<EJBAttribute> attributes) {
		this.attributes = attributes;
	}


	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}


	public void setConstraints(ArrayList<Constraint> constraints) {
		this.constraints = constraints;
	}


	public List<String> getImportedPackages() {
		return importedPackages;
	}


	public void setImportedPackages(List<String> importedPackages) {
		this.importedPackages = importedPackages;
	}


	public ArrayList<Operation> getOperations() {
		return operations;
	}


	public void setOperations(ArrayList<Operation> operations) {
		this.operations = operations;
	}
	
}