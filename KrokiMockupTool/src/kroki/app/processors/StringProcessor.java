package kroki.app.processors;

import kroki.app.bodyElements.Operation;


public class StringProcessor extends Processor{
	
	public String process(int i){
		Operation o = null;
		String result = null;
		o = (Operation) caller.getElementList().get(i);
		if (o.getValue().equals("concat")) {
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				result = caller.makeStatement(i + 1) + ".concat";
			else {
				result = caller.getElementList().get(i + 1).getValue() + ".concat";
				caller.setLastOperand(i + 1);
			}
			if (caller.getElementList().get(caller.getLastOperand() + 1).getType().equals("OperationCallExpImpl"))
				result += caller.makeStatement(caller.getLastOperand() + 1);
			else {
				result += "("+caller.getElementList().get(caller.getLastOperand() + 1).getValue() + ")";
				caller.setLastOperand(i + 1);
			}
			removeUsedElements(i,result,"StringLiteralExpImpl", "String");				
		} 
			
		else if (o.getValue().equals("length")) {
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				result = caller.makeStatement(i + 1) + ".length()";
			else {
				result = caller.getElementList().get(i + 1).getValue() + ".length()";
				caller.setLastOperand(i + 1);
			}
			removeUsedElements(i,result,"IntegerLiteralExpImpl", "Integer");	
			} 
		else if (o.getValue().equals("toLowerCase") || o.getValue().equals("toUpperCase")) {
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				result = caller.makeStatement(i + 1) + "." + o.getValue()+"()";
			else {
				result = caller.getElementList().get(i + 1).getValue() + "." + o.getValue()+"()";
				caller.setLastOperand(i + 1);
			}
			removeUsedElements(i,result,"StringLiteralExpImpl", "String");
		}
		else if (o.getValue().equals("substring")) {
			if (caller.getElementList().get(i + 1).getType().equals("OperationCallExpImpl"))
				result = caller.makeStatement(i + 1) + "." + "substring(";
			else {
				result = caller.getElementList().get(i + 1).getValue() + "." + "substring(";
				caller.setLastOperand(i + 1);
			}
			// prvi parametar
			if (caller.getElementList().get(caller.getLastOperand() + 1).getType().equals("OperationCallExpImpl"))
				result += caller.makeStatement(caller.getLastOperand() + 1) + ", " ;
			else {
				result += caller.getElementList().get(caller.getLastOperand() + 1).getValue() + ", ";
				caller.setLastOperand(caller.getLastOperand() + 1);
			}
			// drugi parametar
			if (caller.getElementList().get(caller.getLastOperand() + 1).getType().equals("OperationCallExpImpl"))
				result += caller.makeStatement(caller.getLastOperand() + 1) + ")" ;
			else {
				result += caller.getElementList().get(caller.getLastOperand() + 1).getValue() + ")";
				caller.setLastOperand(caller.getLastOperand() + 1);
			}
			removeUsedElements(i,result,"StringLiteralExpImpl", "String");
		}
	return result;
	}

}
