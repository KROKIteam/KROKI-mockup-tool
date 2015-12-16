package kroki.app.processors;

import kroki.app.analyzer.*;

import kroki.app.bodyElements.*;

abstract public class Processor {
	protected ConstraintBodyAnalyzer caller;
	protected String result;
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public ConstraintBodyAnalyzer getCaller() {
		return caller;
	}

	public void setCaller(ConstraintBodyAnalyzer caller) {
		this.caller = caller;
	}

	abstract public String process(int i);
	
	protected void removeUsedElements(int i, String result, String newElementClass, String newElementType){
		for (int bound = i + 1; bound <= caller.getLastOperand(); bound++)
			caller.getElementList().remove(i + 1);
		caller.setLastOperand(i);	
		BodyElement b = new BodyElement(result,newElementClass);
		b.setValueType(newElementType);
		caller.getElementList().set(i, b);
	}
	
	protected String getSetElemType(String valueType) {
		if(valueType.startsWith("Set"))
			return valueType.substring(4,valueType.length()-1);
		else if(valueType.startsWith("List"))
			return valueType.substring(5,valueType.length()-1);
		return valueType;
	}
}
