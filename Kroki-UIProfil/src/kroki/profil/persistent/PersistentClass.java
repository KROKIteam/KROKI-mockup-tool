package kroki.profil.persistent;

import java.io.Serializable;
import java.util.List;

import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlPackage;
import kroki.uml_core_basic.UmlProperty;

/**
 * Contains properties of a class directly mapped to certain database concepts.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class PersistentClass implements UmlClass, Serializable {
	
	private static final long serialVersionUID = 1L;

	
	/**Name of the table in the database*/
	protected String tableName;
	/**Indicates if element's label should be transformed to code*/
	protected boolean labelToCode;
    /**Element's name*/
    protected String name;
    /**Element's qualified name*/
    protected String qualifiedName;
    /**Indicates if the class is abstract*/
    protected boolean isAbstract;
    /**Package which contains the class*/
    protected UmlPackage umlPackage;
    /**Class' contained properties*/
    protected List<UmlProperty> ownedAttribute;
    /**Class' contained operations*/
    protected List<UmlOperation> ownedOperation;
    /**Class' super classes*/
    protected List<UmlClass> superClass;
    
    
    public PersistentClass(){
    	
    }
    
    public PersistentClass(boolean labelToCode){
    	this.labelToCode = labelToCode;
    }
    	
    public PersistentClass(String tableName){
    	this.tableName = tableName;
    }

    /*UmlClass interfae methods*/
    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public void setUmlPackage(UmlPackage umlPackage) {
        this.umlPackage = umlPackage;
    }

    public UmlPackage umlPackage() {
        return umlPackage;
    }

    public List<UmlProperty> ownedAttribute() {
        return ownedAttribute;
    }

    public List<UmlOperation> ownedOperation() {
        return ownedOperation;
    }

    public List<UmlClass> superClass() {
        return superClass;
    }

    /*JAVNE METODE KLASE PerisistentClass*/
    public void addAttribute(UmlProperty umlProperty) {
        ownedAttribute.add(umlProperty);
    }

    public void removeAttribute(UmlProperty umlProperty) {
        ownedAttribute.remove(umlProperty);
    }

    public void addOperation(UmlOperation umlOperation) {
        ownedOperation.add(umlOperation);
    }

    public void removeOperation(UmlOperation umlOperation) {
        ownedOperation.remove(umlOperation);
    }

    public String name() {
        return name;
    }

    public String qualifiedName() {
        return qualifiedName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean isLabelToCode() {
		return labelToCode;
	}

	public void setLabelToCode(boolean labelToCode) {
		this.labelToCode = labelToCode;
	}
}
