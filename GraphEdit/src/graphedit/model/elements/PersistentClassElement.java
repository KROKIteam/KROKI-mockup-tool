package graphedit.model.elements;

import graphedit.model.components.Attribute;
import graphedit.model.components.GraphElement;
import graphedit.model.components.Link;
import graphedit.model.components.Method;
import graphedit.model.components.Parameter;
import graphedit.model.properties.PropertyEnums.LinkProperties;
import kroki.profil.persistent.PersistentClass;
import kroki.uml_core_basic.UmlClass;
import kroki.uml_core_basic.UmlNamedElement;
import kroki.uml_core_basic.UmlOperation;
import kroki.uml_core_basic.UmlProperty;

public class PersistentClassElement extends ClassElement{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public PersistentClassElement(GraphElement element){
		this.element = element;
		this.umlClass = new PersistentClass();
	}
	

	@Override
	public void addAttribute(Attribute attribute, int ...args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMethod(Method method, int ...args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAttribute(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeMethod(int index) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void renameAttribute(Attribute attribute, String newName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeAttributeType(Attribute attribute, String newType,
			int... args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOldProperty(Attribute attribute, UmlProperty oldProperty,
			int... args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOldOperation(Method method, UmlOperation oldOperation,
			int... args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeOperationStereotype(Method method, String newStereotype,
			int... args) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void changeLinkProperty(Link link, LinkProperties property, Object newValue, Object...args) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void addParameter(Method m, Parameter parameter) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removeParameter(Method m, Parameter parameter) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void changeClassStereotype(String stereotype) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setOldClass(UmlClass oldClass) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setOldLink(Link link, Object... args) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public UmlNamedElement getUmlElement() {
		return umlClass;
	}


	@Override
	public void setUmlElement(UmlNamedElement umlElement) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setName(String newName) {
		umlClass.setName(newName);
		
	}


	@Override
	public void renameMathod(Method method, String newName) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void link(Link link, Object... args) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void unlink(Link link, Object... args) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void moveElementUp(int... args) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void moveElementDown(int... args) {
		// TODO Auto-generated method stub
		
	}



	

}
