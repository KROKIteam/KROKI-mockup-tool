package kroki.app.processors;

import kroki.app.bodyElements.*;
import kroki.app.generators.utils.Parameter;

public class CollectionProcessor extends Processor{

	@Override
	public String process(int i) {
		String javaResult = null;
		Operation o=(Operation) caller.getElementList().get(i);
		
		if(caller.getElementList().get(i+1).getType().equals("CollectionLiteralExpImpl")){
			Parameter par=new Parameter(caller.getElementList().get(i+1).getValue(), caller.getElementList().get(i+1).getValueType());
			caller.getFmCon().getOperation().getParametri().add(par);
		}
		
		if (o.getValue().equals("including") || o.getValue().equals("excluding") || o.getValue().equals("append") || o.getValue().equals("prepend")){
			String set;
			String setType="";
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult += caller.makeStatement(i + 1);
			else {
				javaResult = caller.getElementList().get(i+1).getValue();
				setType=caller.getElementList().get(i+1).getValueType();
				caller.setLastOperand(i+1);
			}
			set = javaResult;
			if(o.getValue().equals("excluding"))
				javaResult+=".remove(";
			else
				javaResult+=".add(";
			if(o.getValue().equals("prepend"))
				javaResult+="0, ";
			if (caller.getElementList().get(i + 2).getType().equals("OperationCallExpImpl"))
				javaResult += caller.makeStatement(i + 2) + ")";
			else {
				javaResult += caller.getElementList().get(i + 2).getValue() + ")";
				caller.setLastOperand( i + 2 );
			}
					
			caller.getFmCon().getDeclarations().add(javaResult+";");
			javaResult=set;
			removeUsedElements(i, set, "PropertyCallExpImpl", setType);
				
		}else if (o.getValue().equals("size")) {
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult = caller.makeStatement(i + 1) + ".size()";
			else {
				javaResult = caller.getElementList().get(i+1).getValue()+".size()";
				caller.setLastOperand(i + 1);
			}
			removeUsedElements(i, javaResult, "PropertyCallExpImpl", caller.getElementList().get(i+1).getValueType());
		
		}else if (o.getValue().equals("excludes") || o.getValue().equals("includes")) {
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult += caller.makeStatement(i + 1) + ".contains(";
			else {
				javaResult = caller.getElementList().get(i+1).getValue()+".contains(";
				caller.setLastOperand(i + 1);
			}		
					
			if (caller.getElementList().get(i + 2).getType().equals("OperationCallExpImpl"))
				javaResult += caller.makeStatement(i + 2) + ")";
			else {
				javaResult += caller.getElementList().get(i + 2).getValue() + ")";
				caller.setLastOperand(i + 2);
			}					
			if(caller.getElementList().get(i).getValue().equals("excludes"))
				javaResult="!("+javaResult+")";
			removeUsedElements(i, javaResult, "BooleanLiteralExpImpl", "Boolean");
					
		}else if (o.getValue().equals("isEmpty") || o.getValue().equals("notEmpty")){
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult=caller.makeStatement(i + 1)+".isEmpty()";
			else		
				javaResult = caller.getElementList().get(i+1).getValue()+".isEmpty()";		
			if(o.getValue().equals("notEmpty"))
				javaResult="!("+javaResult+")";
			removeUsedElements(i, javaResult, "BooleanLiteralExpImpl", "Boolean");

		}else if (o.getValue().equals("count")){
			String header="count(";
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult="count("+caller.makeStatement(i + 1)+", ";
			else		
				javaResult="count("+caller.getElementList().get(i+1).getValue()+", ";
			header+=caller.getElementList().get(i+1).getValueType()+" collection, ";
				
			if (caller.getElementList().get(i + 2).getType().equals("OperationCallExpImpl"))
				javaResult += caller.makeStatement(i + 2) + ")";
			else {
				javaResult += caller.getElementList().get(i + 2).getValue() + ")";
				caller.setLastOperand(i + 2);
			}
			header+=caller.getElementList().get(i+2).getValueType()+" element)";
			String iterType=caller.getElementList().get(i+2).getValueType();		
			removeUsedElements(i, javaResult, "IntegerLiteralExpImpl", "Integer");
			kroki.app.generators.utils.Operation count=new kroki.app.generators.utils.Operation();
			count.setHeader(header);
			count.setIterType(iterType);
			count.setName("count");
			caller.getFmCon().getSetOperations().add(count);
			//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addImportedPackage("import java.util.Iterator;");
		}else if (o.getValue().equals("excludesAll") || o.getValue().equals("includesAll")){
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult=caller.makeStatement(i+1)+".containsAll";
			else		
				javaResult=caller.getElementList().get(i+1).getValue()+".containsAll";
					
			if (caller.getElementList().get(i + 2).getType().equals("OperationCallExpImpl"))
				javaResult+=caller.makeStatement(i+2);
			else{
				javaResult+="("+caller.getElementList().get(i+2).getValue()+")";
				caller.setLastOperand(i+2);
			}	
					
			if(o.getValue().equals("excludesAll"))
				javaResult="!("+javaResult+")";
			removeUsedElements(i, javaResult, "BooleanLiteralExpImpl", "Boolean");
		}
		else if (o.getValue().equals("union") || o.getValue().equals("intersection")){
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult=caller.makeStatement(i+1);
			else		
				javaResult=caller.getElementList().get(i+1).getValue();
			String valueInList=javaResult;
			if(o.getValue().equals("union"))
				javaResult+=".addAll";
			else
				javaResult+=".retainAll";
			
			if (caller.getElementList().get(i + 2).getType().equals("OperationCallExpImpl"))
				javaResult+=caller.makeStatement(i+2);
			else{
				javaResult+="("+caller.getElementList().get(i+2).getValue()+")";
				caller.setLastOperand(i+2);
			}
				
			caller.getFmCon().getDeclarations().add(javaResult+";");
			javaResult=valueInList;
			removeUsedElements(i, javaResult, "PropertyCallExpImpl", caller.getElementList().get(i+1).getValueType());
		}
		else if (o.getValue().equals("last") || o.getValue().equals("first")){
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult=caller.makeStatement(i+1);
			else{
				javaResult=caller.getElementList().get(i+1).getValue();
				caller.setLastOperand(i+1);
			}
			if(o.getValue().equals("last"))
				javaResult+=".get("+caller.getElementList().get(i+1).getValue()+".size()-1)";
			else
				javaResult+=".get(0)";
			
			removeUsedElements(i, javaResult, "PropertyCallExpImpl", caller.getElementList().get(i+1).getValueType());
		}
		else if (o.getValue().equals("at") || o.getValue().equals("indexOf")){
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult=caller.makeStatement(i+1);
			else{
				javaResult=caller.getElementList().get(i+1).getValue();
			}
			if(o.getValue().equals("at"))
				javaResult+=".get";
			else
				javaResult+=".indexOf";
			
			if (caller.getElementList().get(i + 2).getType().equals("OperationCallExpImpl"))
				javaResult+=caller.makeStatement(i+2);
			else{
				javaResult+="("+caller.getElementList().get(i+2).getValue()+")";
				caller.setLastOperand(i+2);
			}
			
			removeUsedElements(i, javaResult, "PropertyCallExpImpl", caller.getElementList().get(i+1).getValueType());
		}
		else if (o.getValue().equals("insertAt")){
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult=caller.makeStatement(i+1);
			else{
				javaResult=caller.getElementList().get(i+1).getValue();
			}
			String valueInList=javaResult;
			javaResult+=".add(";
			if (caller.getElementList().get(i + 2).getType().equals("OperationCallExpImpl"))
				javaResult+=caller.makeStatement(i+2);
			else{
				javaResult+=caller.getElementList().get(i+2).getValue();
				caller.setLastOperand(i+2);
			}
			javaResult+=" ,";
			if (caller.getElementList().get(caller.getLastOperand()+1).getType().equals("OperationCallExpImpl"))
				javaResult+=caller.makeStatement(caller.getLastOperand()+1);
			else{
				javaResult+=caller.getElementList().get(caller.getLastOperand()+1).getValue();
				caller.setLastOperand(caller.getLastOperand()+1);
			}
			javaResult+=")";
			caller.getFmCon().getDeclarations().add(javaResult+";");
			javaResult=valueInList;
			removeUsedElements(i, javaResult, "PropertyCallExpImpl", caller.getElementList().get(i+1).getValueType());
		}
		else if (o.getValue().equals("subSequence") || o.getValue().equals("subOrderedSet")){
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult=caller.makeStatement(i+1);
			else{
				javaResult=caller.getElementList().get(i+1).getValue();
			}
			javaResult+=".subList(";
			if (caller.getElementList().get(i + 2).getType().equals("OperationCallExpImpl"))
				javaResult+=caller.makeStatement(i+2);
			else{
				javaResult+=caller.getElementList().get(i+2).getValue();
				caller.setLastOperand(i+2);
			}
			javaResult+=" ,";
			if (caller.getElementList().get(caller.getLastOperand()+1).getType().equals("OperationCallExpImpl"))
				javaResult+=caller.makeStatement(caller.getLastOperand()+1);
			else{
				javaResult+=caller.getElementList().get(caller.getLastOperand()+1).getValue();
				caller.setLastOperand(caller.getLastOperand()+1);
			}
			javaResult+=")";
			removeUsedElements(i, javaResult, "PropertyCallExpImpl", caller.getElementList().get(i+1).getValueType());
		}
		else if (o.getValue().equals("asSequence") || o.getValue().equals("asBag") || o.getValue().equals("asOrderedSet") || o.getValue().equals("asSet")){
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult=caller.makeStatement(i+1);
			else{
				javaResult=caller.getElementList().get(i+1).getValue();
				caller.setLastOperand(i+1);
			}
			kroki.app.generators.utils.Operation oper=new kroki.app.generators.utils.Operation();
			if(o.getValue().equals("asSet")){
				oper.setName("convertToSet");
				javaResult="convertToSet("+javaResult+")";
			}
			else{
				oper.setName("convertToList");
				javaResult="convertToList("+javaResult+")";
			}
			oper.setType(caller.getElementList().get(i+1).getValueType());
			oper.setIterType(getSetElemType(caller.getElementList().get(i+1).getValueType()));
			caller.getFmCon().getSetOperations().add(oper);
			//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addImportedPackage("import java.util.Iterator;");
			if(o.getValue().equals("asSet")){
				//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addImportedPackage("import java.util.Set;");
				//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addImportedPackage("import java.util.HashSet;");
			}else{
				//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addImportedPackage("import java.util.List;");
				//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addImportedPackage("import java.util.ArrayList;");
			}
			removeUsedElements(i, javaResult, "PropertyCallExpImpl", caller.getElementList().get(i+1).getValueType());
		}
		else if (o.getValue().equals("symmetricDifference")){
			javaResult="symmetricDifference(";
			kroki.app.generators.utils.Operation oper=new kroki.app.generators.utils.Operation();
			oper.setName("symmetricDifference");
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult+=caller.makeStatement(i+1);
			else{
				javaResult+=caller.getElementList().get(i+1).getValue();
			}
			oper.setIterType(getSetElemType(caller.getElementList().get(i+1).getValueType()));
			javaResult+=", ";
			
			if (caller.getElementList().get(i + 2).getType().equals("OperationCallExpImpl"))
				javaResult+=caller.makeStatement(i+2);
			else{
				javaResult+=caller.getElementList().get(i+2).getValue();
				caller.setLastOperand(i+2);
			}
			javaResult+=")";
			caller.getFmCon().getSetOperations().add(oper);
			
			removeUsedElements(i, javaResult, "PropertyCallExpImpl", caller.getElementList().get(i+1).getValueType());
		}
		else if (o.getValue().equals("flatten")){
			javaResult="flatten(";
			kroki.app.generators.utils.Operation oper=new kroki.app.generators.utils.Operation();
			oper.setName("flatten");
			
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				javaResult+=caller.makeStatement(i+1);
			else{
				javaResult+=caller.getElementList().get(i+1).getValue();
				caller.setLastOperand(i+1);
			}
			Parameter param=new Parameter("collection", caller.getElementList().get(i+1).getValueType());
			oper.getParametri().add(param);
			oper.makeOperationHeader();
		//	oper.setType(getSetElemType(caller.getElementList().get(i+1).getValueType()));
			oper.setIterType(getSetElemType(caller.getElementList().get(i+1).getValueType()));
			oper.setForParam(getSetElemType(getSetElemType(caller.getElementList().get(i+1).getValueType())));
			String tip=caller.getElementList().get(i+1).getValueType();
			int index=tip.indexOf("<");
			oper.setType(tip.substring(0, index)+"<"+oper.getForParam()+">");
			javaResult+=")";
			caller.getFmCon().getSetOperations().add(oper);
			
			removeUsedElements(i, javaResult, "PropertyCallExpImpl", caller.getElementList().get(i+1).getValueType());
		}
		return javaResult;
	}

}
