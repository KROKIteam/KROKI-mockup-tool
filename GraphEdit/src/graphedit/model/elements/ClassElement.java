package graphedit.model.elements;

import java.io.Serializable;

import graphedit.model.components.Attribute;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Method;
import graphedit.model.components.Parameter;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlProperty;
import kroki.uml_core_basic.UmlType;

public abstract class ClassElement implements Serializable, GraphEditElement{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected UmlClass umlClass;
	protected GraphElement element;

	public UmlType getUmlType(){
		return umlClass;
	}

	
	@Override
	public GraphElement element() {
		return element;
	}

	@Override
	public void setElement(GraphElement element) {
		this.element = element;
	}
	
	public abstract void addAttribute(Attribute attribute, int ...args);
	public abstract void moveElementUp(int ...args);
	public abstract void moveElementDown( int ...args);
	public abstract void addMethod(Method method, int ...args);
	public abstract void removeAttribute(int index);
	public abstract void removeMethod(int index);
	public abstract void changeAttributeType(Attribute attribute, String newType, int ...args);
	public abstract void renameAttribute(Attribute attribute, String newName);
	public abstract void setOldProperty(Attribute attribute, UmlProperty oldProperty, int ...args);
	public abstract void setOldOperation(Method method, UmlOperation oldOperation, int ...args);
	public abstract void changeOperationStereotype(Method method, String newStereotype, int ...args);
	public abstract void addParameter(Method m, Parameter parameter);
	public abstract void removeParameter(Method m, Parameter parameter);
	public abstract void changeClassStereotype(String stereotype);
	public abstract void setOldClass(UmlClass oldClass);
	public abstract void renameMathod(Method method, String newName);
	
	


}
