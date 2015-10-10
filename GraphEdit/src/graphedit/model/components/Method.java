package graphedit.model.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kroki.profil.operation.Report;
import kroki.uml_core_basic.UmlOperation;

public class Method implements Serializable {

	private static final long serialVersionUID = 1L;

	private static int instanceCounter = 1;
	
	private String name;
	
	private String returnType;
	
	private String stereotype;
	
	private boolean constructorMethod = false;
	
	private boolean staticMethod = false;
	
	private boolean finalMethod = false;
	
	private boolean abstractMethod = false;
	
	private Modifier modifier = Modifier.PUBLIC;
	
	private boolean visible = true;
	
	private List<Parameter> parameters;
	
	private UmlOperation umlOperation;

	public Method() { 
		this("method" + instanceCounter++, "void", "Report");
		umlOperation = new Report();
	}
	
	public Method(String name, String type, String stereotype) {
		this.name = name;
		this.returnType = type;
		this.stereotype = stereotype;
		this.parameters = new ArrayList<Parameter>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public boolean isConstructorMethod() {
		return constructorMethod;
	}

	public void setConstructorMethod(boolean constructorMethod) {
		this.constructorMethod = constructorMethod;
	}

	public boolean isStaticMethod() {
		return staticMethod;
	}

	public void setStaticMethod(boolean staticMethod) {
		this.staticMethod = staticMethod;
	}

	public boolean isFinalMethod() {
		return finalMethod;
	}

	public void setFinalMethod(boolean finalMethod) {
		this.finalMethod = finalMethod;
	}

	public boolean isAbstractMethod() {
		return abstractMethod;
	}

	public void setAbstractMethod(boolean abstractMethod) {
		this.abstractMethod = abstractMethod;
	}

	public Modifier getModifier() {
		return modifier;
	}

	public void setModifier(Modifier modifier) {
		this.modifier = modifier;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	public void addParameter(Parameter a) {
		this.parameters.add(a);
	}

	public void addParameters(List<Parameter> a) {
		this.parameters.addAll(a);
	}
	
	public void removeParameter(Parameter parameter) {
		this.parameters.remove(parameter);
	}

	public void removeParameters(List<Attribute> a) {
		this.parameters.removeAll(a);
	}
	
	public void removeAllParameters() {
		this.parameters.clear();	
	}
	
	public String getStereotype() {
		return stereotype;
	}

	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(modifier + " " + name + "(");
		for (int i = 0; i < parameters.size(); i++)
			if (i == parameters.size() - 1) builder.append(parameters.get(i).getType());
			else builder.append(parameters.get(i).getType() + ", ");
		builder.append(") : " + returnType);
		builder.append(" <<" + stereotype + ">>");
		return  builder.substring(0);
	}

	public UmlOperation getUmlOperation() {
		return umlOperation;
	}

	public void setUmlOperation(UmlOperation umlOperation) {
		this.umlOperation = umlOperation;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void addParameter(int index, Parameter parameter) {
		parameters.add(index, parameter);
	}
	
	public void removeParameter(int index){
		parameters.remove(index);
	}

}
