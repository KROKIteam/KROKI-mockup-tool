package kroki.app.processors;

import kroki.app.bodyElements.Operation;


public class UnarProcessor extends Processor{

	@Override
	public String process(int i) {
		String result="";
		if(caller.getElementList().get(i).getValue().equals("oclIsUndefined")){
			result=caller.getElementList().get(i+1).getValue()+"==null";
			caller.setLastOperand(i);
		}
		else if(caller.getElementList().get(i).getValue().equals("oclIsKindOf")){
			if(caller.getElementList().get(i+1).getType().equals("TypeLiteralExpImpl")){
				result="this"+" instanceof "+caller.getElementList().get(i+1).getValue();
				caller.setLastOperand(i+1);
			}
			else{
				result=caller.getElementList().get(i+1).getValue()+" instanceof "+caller.getElementList().get(i+2).getValue();
				caller.setLastOperand(i+2);
			}	
		}
		else if(caller.getElementList().get(i).getValue().equals("oclIsTypeOf")){
			if(caller.getElementList().get(i+1).getType().equals("TypeLiteralExpImpl")){
				result="this.getClass().getName().equals(\""+caller.getElementList().get(i+1).getValue()+"\")";
				caller.setLastOperand(i+1);
			}
			else{
				result=caller.getElementList().get(i+1).getValue()+".getClass().getName().equals(\""+caller.getElementList().get(i+2).getValue()+"\")";
				caller.setLastOperand(i+2);
			}
		}
		else if(caller.getElementList().get(i).getValue().equals("oclAsType")){
			result="super."+caller.getElementList().get(i-1).getValue();
			caller.getElementList().remove(i+1);
			caller.getElementList().remove(i);
			Operation o=new Operation(result, "OperationCallExpImpl");
			o.setValueType(caller.getElementList().get(i-1).getValueType());
			o.setTip("other");
			caller.getElementList().set(i-1, o);
			caller.setLastOperand(i-1);
		}
		if(i>0)	// proverava se da li postoji not()
			if(caller.getElementList().get(i-1).getValue().equals("not()"))
				result="!("+result+")";
		return result;
	}

}
