package kroki.app.processors;

import kroki.app.bodyElements.Operation;


public class MathProcessor extends Processor{
	
public String process(int i){
	Operation o=(Operation) caller.getElementList().get(i);
	String result = null;
	if(o.getValue().equals("abs")){
		result="Math.abs(";
		if(caller.getElementList().get(i+1).getType().equals("OperationCallExpImpl"))
			result="Math.abs"+caller.makeStatement(i+1);						
		else{
			result+=caller.getElementList().get(i+1).getValue()+")";
			caller.setLastOperand(i+1);					
		}		
		removeUsedElements(i,result,"IntegerLiteralExpImpl", "Integer");
	}else if(o.getValue().equals("max") || o.getValue().equals("min")){
		String firstOp;
		String secondOp;
		// odredjivanje prvog operanda
		if(caller.getElementList().get(i+1).getType().equals("OperationCallExpImpl"))
			firstOp=caller.makeStatement(i+1);
		else{
			firstOp=caller.getElementList().get(i+1).getValue();
			caller.setLastOperand(i+1);	
		}			
		// odredjivanje dtugog operanda
		if(caller.getElementList().get(caller.getLastOperand()+1).getType().equals("OperationCallExpImpl"))
			secondOp=caller.makeStatement(caller.getLastOperand()+1);
		else{
			secondOp=caller.getElementList().get(caller.getLastOperand()+1).getValue();
			caller.setLastOperand(i+1);	
		}				
		result="Math."+o.getValue().substring(0, 4)+firstOp+", "+secondOp+")";
		removeUsedElements(i,result,"IntegerLiteralExpImpl", "Integer");
	}
	else if(o.getValue().equals("round") || o.getValue().equals("floor")){
		result="Math."+o.getValue()+"(";
		if(caller.getElementList().get(i+1).getType().equals("OperationCallExpImpl"))
			result="Math."+o.getValue()+caller.makeStatement(i+1);
		else{
			result+=caller.getElementList().get(i+1).getValue()+")";
			caller.setLastOperand(i+1);	
		}
		removeUsedElements(i,result,"IntegerLiteralExpImpl", "Integer");
	}
	return result;
	}

}
