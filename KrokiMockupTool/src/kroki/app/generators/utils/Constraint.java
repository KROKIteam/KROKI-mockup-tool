package kroki.app.generators.utils;

import java.util.ArrayList;
import java.util.List;

public class Constraint {
	
	private String kind;	// inv,def,post,pre
	private String context; //Klasa na koju se odnosi ogranicenje
	private String body; //izraz
	private String returnType; //tip koji se vraca
	private Operation operation; // naziv metode
	private String condition=""; 	// uslov u if iskazu
	private String thenPart;
	private String elsePart;
	private List<Operation> setOperations=new ArrayList<Operation>();
	private List<String> declarations=new ArrayList<String>(); // deklaracije promenljivih i poziva meoda;
	


	public String getThenPart() {
		return thenPart;
	}

	public void setThenPart(String thenPart) {
		this.thenPart = thenPart;
	}

	public String getElsePart() {
		return elsePart;
	}

	public void setElsePart(String elsePart) {
		this.elsePart = elsePart;
	}

	public List<Operation> getSetOperations() {
		return setOperations;
	}

	public List<String> getDeclarations() {
		return declarations;
	}

	public void setDeclarations(List<String> declarations) {
		this.declarations = declarations;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getContext() {
		return context;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setContext(String typeName) {
		this.context = typeName;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}
	
	public String toString(){
		String s;
		s="contextTypeName: "+context+"\n";
		s+="kind: "+kind+"\n";
		s+="body: "+body+"\n";
		s+="returnType: "+returnType+"\n";
		s+="operationName: "+operation+"\n";
		if(operation!=null)
			for(int i=0;i<operation.getParametri().size();i++)
				s+="param"+i+":" +operation.getParametri().get(i).getType()+"\n";
		return s;
	}

}
