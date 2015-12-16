package kroki.app.bodyElements;

import java.util.ArrayList;
import java.util.List;

public class Operation extends BodyElement{

	private String tip; // da li je unarni, binarni i td.
	private String firstOperand;
	private String secondOperand;
	private int lowerBound;
	private int upperBound;
	
	private List<BodyElement> elementList=new ArrayList<BodyElement>();
	
	public int getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}

	public int getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}

	public List<BodyElement> getElementList() {
		return elementList;
	}

	public void setElementList(List<BodyElement> elementList) {
		this.elementList = elementList;
	}

	public String getTip() {
		return tip;
	}

	public String getFirstOperand() {
		return firstOperand;
	}

	public void setFirstOperand(String firstOperand) {
		this.firstOperand = firstOperand;
	}

	public String getSecondOperand() {
		return secondOperand;
	}

	public void setSecondOperand(String secondOperand) {
		this.secondOperand = secondOperand;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public Operation(String v, String t) {
		super(v, t);
		// TODO Auto-generated constructor stub
	}
	public Operation(){}
	
	public void correctValue(String v){
		if(v.equals("="))
			value="==";
		else if(v.equals("<>"))
			value="!=";
		else if(v.equals("and"))
			value="&&";
		else if(v.equals("or"))
			value="||";
		else if(v.equals("xor"))
			value="^";
		else if(v.equals("mod"))
			value="%";
		else if(v.equals("div"))
			value="/";
	}
	
	public String determineTip(String tip){
		if(binarniOperatori.contains(tip)){
			return"binarni";		
		}			
		else if(unarniOperatori.contains(tip))
			return "unarni";
		else if(setOperatori.contains(tip))
			return "set";
		else if(stringOperatori.contains(tip))
			return "string";
		else if(mathOperatori.contains(tip))
			return "math";
		else if(loopOperations.contains(tip))
			return "loop";
		else if(unsupported.contains(tip))
			return "unsupported";
		else 
			return "other";
	}
	
	public static List<String> getBinarniOperatori() {
		return binarniOperatori;
	}
	public static List<String> getSetOperatori() {
		return setOperatori;
	}
	
	private static List<String> binarniOperatori;
	static{
		binarniOperatori=new ArrayList<String>();
		binarniOperatori.add("+");
		binarniOperatori.add("-");
		binarniOperatori.add("*");
		binarniOperatori.add("/");
		binarniOperatori.add("==");
		binarniOperatori.add(">");
		binarniOperatori.add("<");
		binarniOperatori.add(">=");
		binarniOperatori.add("<=");
		binarniOperatori.add("!=");
		binarniOperatori.add("&&");
		binarniOperatori.add("||");
		binarniOperatori.add("^");
		binarniOperatori.add("implies");
		binarniOperatori.add("%");
	}
	private static List<String> setOperatori;
	

	static{
		setOperatori=new ArrayList<String>();
		setOperatori.add("including");
		setOperatori.add("excluding");
		setOperatori.add("size");
		setOperatori.add("excludes");
		setOperatori.add("includes");
		setOperatori.add("includesAll");
		setOperatori.add("excludesAll");
		setOperatori.add("isEmpty");
		setOperatori.add("notEmpty");
		setOperatori.add("count");
		setOperatori.add("union");
		setOperatori.add("intersection"); 
		setOperatori.add("last");
		setOperatori.add("first");
		setOperatori.add("at");
		setOperatori.add("indexOf");
		setOperatori.add("insertAt");
		setOperatori.add("subSequence");
		setOperatori.add("subOrderedSet");
		setOperatori.add("append");
		setOperatori.add("prepend");
		setOperatori.add("asSequence");
		setOperatori.add("asBag");
		setOperatori.add("asOrderedSet");
		setOperatori.add("asSet");
		setOperatori.add("symmetricDifference");
		setOperatori.add("flatten");
	}
	static List<String> loopOperations;
	static{
		loopOperations=new ArrayList<String>();
		loopOperations.add("forAll");
		loopOperations.add("exists");
		loopOperations.add("select");
		loopOperations.add("reject");
		loopOperations.add("sum");
		loopOperations.add("one");
		loopOperations.add("isUnique");
		loopOperations.add("any");
		loopOperations.add("collect");
	}
	static List<String> unarniOperatori;
	static{
		unarniOperatori=new ArrayList<String>();
		unarniOperatori.add("oclIsUndefined");
		unarniOperatori.add("oclIsKindOf");
		unarniOperatori.add("oclIsTypeOf");
		unarniOperatori.add("oclAsType");
	}
	static List<String> stringOperatori;
	static{
		stringOperatori=new ArrayList<String>();
		stringOperatori.add("concat");
		stringOperatori.add("length");
		stringOperatori.add("toLowerCase");
		stringOperatori.add("toUpperCase");
		stringOperatori.add("substring");
	}
	static List<String> mathOperatori;
	static{
		mathOperatori=new ArrayList<String>();
		mathOperatori.add("floor");
		mathOperatori.add("mod");
		mathOperatori.add("div");
		mathOperatori.add("abs");
		mathOperatori.add("round");
		mathOperatori.add("max");
		mathOperatori.add("min");
	}
	static List<String> unsupported;
	static{
		unsupported=new ArrayList<String>();
	//	unsupported.add("flatten");
		unsupported.add("sortedBy");
		unsupported.add("isSignalSent");
		unsupported.add("isOperationCall");
		unsupported.add("hasReturned");
		unsupported.add("result");
		unsupported.add("oclIsNew");
		unsupported.add("oclInState");
		unsupported.add("allInstances");
		unsupported.add("^^");
	}

}
