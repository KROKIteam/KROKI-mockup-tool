package kroki.app.analyzer;

import java.util.ArrayList;


import java.util.List;

import kroki.app.generators.utils.*;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import kroki.app.bodyElements.*;
import kroki.app.bodyElements.Operation;
import kroki.app.processors.*;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.BooleanLiteralExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.CollectionLiteralExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.EnumLiteralExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.IfExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.IntegerLiteralExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.IteratorExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.LetExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.OperationCallExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.PropertyCallExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.RealLiteralExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.StringLiteralExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.TypeLiteralExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.VariableExpImpl;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.VariableImpl;
import tudresden.ocl20.pivot.pivotmodel.Constraint;

public class ConstraintBodyAnalyzer {
	private kroki.app.generators.utils.Constraint fmCon;
	private List<BodyElement> elementList=new ArrayList<BodyElement>();
	
	
	
	public ConstraintBodyAnalyzer(Constraint e,kroki.app.generators.utils.Constraint con){
		fmCon=con;
	}
	
	public BodyElement getElementData(EObject o){	
		if(o instanceof PropertyCallExpImpl)
			return getPropertyCallData((PropertyCallExpImpl) o);
		else if(o instanceof IntegerLiteralExpImpl)
			return getIntData((IntegerLiteralExpImpl)o);
		else if(o instanceof IteratorExpImpl){
			if(getIteratorData((IteratorExpImpl)o)!=null)
				return getIteratorData((IteratorExpImpl)o);
		}else if(o instanceof IfExpImpl)
			return getIfExpData((IfExpImpl)o);
		else if(o instanceof OperationCallExpImpl)
			return getOperationData((OperationCallExpImpl)o);		
		else if(o instanceof StringLiteralExpImpl)
			return getStringData((StringLiteralExpImpl)o);
		else if(o instanceof VariableImpl){
			if(getVariableData((VariableImpl)o)!=null)
				return getVariableData((VariableImpl)o);
		}else if(o instanceof EnumLiteralExpImpl)
			return getEnumData((EnumLiteralExpImpl)o);
		else if(o instanceof VariableExpImpl){
			if(getVariableExpData((VariableExpImpl)o)!=null)
				return getVariableExpData((VariableExpImpl)o);
		}else if(o instanceof LetExpImpl)
			return getLetData((LetExpImpl)o);
		else if(o instanceof BooleanLiteralExpImpl)
			return getBooleanData((BooleanLiteralExpImpl)o);
		else if(o instanceof RealLiteralExpImpl)
			return getRealData((RealLiteralExpImpl)o);
		else if(o instanceof TypeLiteralExpImpl)
			return getTypeLiteralData((TypeLiteralExpImpl)o);
		else if(o instanceof CollectionLiteralExpImpl)
			return getCollectionLiteralData((CollectionLiteralExpImpl)o);
		//System.out.println(o);
		return null;
	}


	

	public List<BodyElement> getContentsData(TreeIterator<EObject> tree) {
		List<BodyElement> result=new ArrayList<BodyElement>();
		while(tree.hasNext()){
			EObject o=tree.next();
			if(getElementData(o)!=null)
				result.add(getElementData(o));		
			//System.out.println(o);
		}	
		
		return result;
	}
	
	public List<BodyElement> getContentsData(EList<EObject> tree) {
		List<BodyElement> result=new ArrayList<BodyElement>();
		for(int i=0;i<tree.size();i++){
			EObject o=tree.get(i);
			if(getElementData(o)!=null)
				result.add(getElementData(o));		
		}	
		return result;
	}
	
	public void processBody() {
		for(int n=0;n<elementList.size();n++)
			System.out.println(elementList.get(n).getValue()+"\t"+elementList.get(n).getType()+"\t"+elementList.get(n).getValueType());
		
		if(supported()){
			prepareLet();
			checkSizeOperation();
		//	processCollectionLiteral();
			checkCollectionLiterals();
			elementList=linkProperties(elementList);	
			if(fmCon.getKind().equals("postcondition")){
				preparePost();		// bitan je redosled poziva metoda
			}else if(fmCon.getKind().equals("initial")){
				processInit();
			}
			processLet();
			processElementList();
			
			if(fmCon.getKind().equals("derived")){
				processDerived();
			}else if(fmCon.getKind().equals("precondition"))
				processPre();
		}
		
		List<BodyElement> lista=getElementList();
		for(int l=0;l<lista.size();l++){
			System.out.println(lista.get(l).getValue());
		}		
		System.out.println("");
		System.out.println("");
	}
	
	private BodyElement getCollectionLiteralData(CollectionLiteralExpImpl o){
		String value=o.getType().getName();
		value=correctPropertyType(value);
		CollectionLiteral b=new CollectionLiteral(value, "CollectionLiteralExpImpl");
		b.setValueType(value);
		b.setNumberOfLiterals(o.getPart().size());
		TreeIterator<EObject> tree=o.eAllContents();
		b.setContents(getContentsData(tree));		
		return b;
	}
	
	private BodyElement getTypeLiteralData(TypeLiteralExpImpl o) {
		String value=o.getReferredType().getName();
		if(value.startsWith("Sequence"))
			value="List"+value.substring(8);
		else if(value.startsWith("Bag"))
			value="List"+value.substring(3);
		else if(value.startsWith("OrderedSet"))
			value="List"+value.substring(10);
		else if(value.startsWith("Set"))
			value="Set"+value.substring(3);
		else if(value.equals("Real"))
			value="float";
		else if(value.equals("Boolean"))
			value="boolean";
		else if(value.equals("OclType"))
			value="Class";
		else if(value.equals("OclAny"))
			value="Object";
		BodyElement b=new BodyElement(value, "TypeLiteralExpImpl");
		return b;
	}

	private BodyElement getRealData(RealLiteralExpImpl o) {
		float f=o.getRealSymbol();
		String value=String.valueOf(f);
		BodyElement b=new BodyElement(value, "RealLiteralExpImpl");
		b.setValueType("float");
		return b;
	}

	private BodyElement getBooleanData(BooleanLiteralExpImpl o) {		
		String value=o.toString();
		int i=value.indexOf("true");
		if(i==-1)
			value="false";
		else
			value="true";
		BodyElement b=new BodyElement(value, "BooleanLiteralExpImpl");
		b.setValueType("boolean");
		return b;
	}
	
	private BodyElement getLetData(LetExpImpl o) {
		TreeIterator<EObject> tree=o.getVariable().eAllContents();
		List<BodyElement> variableContents=new ArrayList<BodyElement>();
		variableContents=getContentsData(tree);		
		String value=o.getVariable().getName();
		LetContents let=new LetContents(value, "LetExpImpl");
		let.setLetVariableContents(variableContents);
		String type=o.getVariable().getType().getName();
		type=correctPropertyType(type);
		let.setValueType(type);		
		return let;
	}

	private BodyElement getVariableExpData(VariableExpImpl o) {
		BodyElement b=null;
		String value=o.getReferredVariable().getName();
		if(!(value.equals("self")) && !(value.startsWith("$implicitVariable")) && !(value.startsWith("$implicitCollect"))){
			b=new BodyElement(value, "VariableExpImpl");
			String type=correctPropertyType(o.getReferredVariable().getType().getName());
			b.setValueType(type);
		}
		return b;
	}

	private BodyElement getEnumData(EnumLiteralExpImpl o) {
		String value=o.getType().getName()+"."+o.getReferredEnumLiteral().getName();
		BodyElement b=new BodyElement(value, "EnumLiteralExpImpl");
		return b;
	}

	private BodyElement getVariableData(VariableImpl o) {
		BodyElement b=null;
		String value=o.getName();
		if(!(value.equals("self")) && !(value.startsWith("$implicitVariable")) && !(value.startsWith("$implicitCollect")) && !(value.equals(""))){
			b=new BodyElement(value, "VariableImpl");
			elementList.add(b);
		}
		return b;
	}

	private BodyElement getStringData(StringLiteralExpImpl o) {
		String value='\"'+o.getStringSymbol()+'\"';
		BodyElement b=new BodyElement(value, "StringLiteralExpImpl");
		b.setValueType("String");
		return b;
	}

	private BodyElement getOperationData(OperationCallExpImpl o) {
		String value;
		if(o.getReferredOperation()!=null){
			value=o.getReferredOperation().getName();
		}else{
			value=o.getName();
		}	
		Operation b=new Operation(value, "OperationCallExpImpl");;
		b.correctValue(value);
		
		if(o.getReferredOperation()!=null)
			b.setValueType(o.getReferredOperation().getType().getName());
		
		b.setTip(b.determineTip(b.getValue()));	// odredjivanje tipa operatora
		
		if(!(b.getTip().equals("binarni")) && !(b.getTip().equals("set")) && !(b.getTip().equals("loop")) && 
				!(b.getTip().equals("math")) && !(b.getTip().equals("string"))&& !(b.getTip().equals("unarni")) && !(b.getTip().equals("unsupported")))
			b.setValue(b.getValue()+"()");
		
		if(b.getTip().equals("set")){
			EList<EObject> tree =o.eContents();
			List<BodyElement> absList=getContentsData(tree);
			b.setElementList(absList);
		}
		if(b.getValue().equals("sum")){
			TreeIterator<EObject> tree=o.eAllContents();
			List<BodyElement> absList=getContentsData(tree);
			b.setElementList(absList);
		}
		return b;
	}

	private BodyElement getIfExpData(IfExpImpl o) {
		String value="if";
		BodyElement b=new BodyElement(value, "IfExpImpl");
		b.setValueType("Boolean");
		return b;
	}

	private BodyElement getIteratorData(IteratorExpImpl o) {
		Operation b=null;
		String value=o.getName();
		if(!(value.equals("collect")) || fmCon.getBody().indexOf("collect")!=-1){
			TreeIterator<EObject> tree =o.eAllContents();
			List<BodyElement> iterList=getContentsData(tree);	
			b=new Operation(value, "OperationCallExpImpl");
			b.setTip("loop");
			b.setElementList(iterList);
			elementList.add(b);
		}
		return b;
	}

	private BodyElement getIntData(IntegerLiteralExpImpl o) {
		int value=o.getIntegerSymbol();
		BodyElement b=new BodyElement(Integer.toString(value), "IntegerLiteralExpImpl");
		b.setValueType("Integer");
		return b;
	}

	private BodyElement getPropertyCallData(PropertyCallExpImpl o) {
		String value=o.getReferredProperty().getName();
		BodyElement b=new BodyElement("ka_" + value, "PropertyCallExpImpl");
		String type=o.getType().getName();
		type=correctPropertyType(type);
		b.setValueType(type);
		return b;
	}
	
	private void processInit() {
	/*	InitialValue init=new InitialValue(fmCon.getOperation().getName(),elementList.get(0).getValue());
		List<FMClass> classes=FMModel.getInstance().getClasses();
		FMModel.getInstance().findConstraintClassByName(fmCon.getContext()).getInitialValues().add(init);
		for(int i=0;i<classes.size();i++)
			if(classes.get(i).getName().equals(fmCon.getContext())){
				FMModel.getInstance().getClasses().get(i).getInitialValues().add(init);
				for(int j=0;j<FMModel.getInstance().getClasses().get(i).getConstructorParams().size();j++)
					if(FMModel.getInstance().getClasses().get(i).getConstructorParams().get(j).getName().equals(fmCon.getOperation().getName()))
						FMModel.getInstance().getClasses().get(i).getConstructorParams().remove(j);
			}
		for(int j=0;j<FMModel.getInstance().findConstraintClassByName(fmCon.getContext()).getConstructorParams().size();j++)
			if(FMModel.getInstance().findConstraintClassByName(fmCon.getContext()).getConstructorParams().get(j).getName().equals(fmCon.getOperation().getName()))
				FMModel.getInstance().findConstraintClassByName(fmCon.getContext()).getConstructorParams().remove(j);
				
		*/
	}
	
	private void processPre() {
		int index=fmCon.getOperation().getHeader().indexOf("(");  // ubacivanje Pre u ime metode
		String operHeader=fmCon.getOperation().getHeader().substring(0, index)+"Pre"+fmCon.getOperation().getHeader().substring(index);
		fmCon.getOperation().setHeader(operHeader);
		String operName=fmCon.getOperation().getName().substring(0, index)+"Pre"+fmCon.getOperation().getName().substring(index);
		fmCon.getOperation().setName(operName);
		for(int i=0;i<elementList.size();i++)
			if(getElementList().get(i).getType().equals("OperationCallExpImpl") && !(getElementList().get(i).getValue().equals("not()"))){
				fmCon.setCondition(makeStatement(i));
				break;
			}
	}
	
	public void preparePost(){		
		int index=fmCon.getOperation().getHeader().indexOf("(");  // ubacivanje Post u ime metode
		String operHeader=fmCon.getOperation().getHeader().substring(0, index)+"Post"+fmCon.getOperation().getHeader().substring(index);
		fmCon.getOperation().setHeader(operHeader);	
		for(int i=0;i<elementList.size();i++) // zamena result sa pozivom operacije
			if(elementList.get(i).getValue().equals("result")){
				elementList.get(i).setValue(fmCon.getOperation().getHeaderWithoutTypes());
			}
		
		String javaResult="";
		String preType = "";
		String preValue = "";
		Boolean atPre=false; // oznacava da li postoji element sa operatorom @pre
		for(int i=0;i<elementList.size();i++)
			if(elementList.get(i).getValue().equals("atPre()")){  // pronadje se element na koji se odnosi operator @pre
				atPre=true;  // element na koji se odnosi operator @pre i sam operator se spajaju u jednu vrednost
				preType=elementList.get(i+1).getValueType();
				preValue=elementList.get(i+1).getValue();
				if(preValue.endsWith("()"))
					preValue=preValue.substring(0, preValue.length()-2);
				if(elementList.get(i+1).getType().equals("PropertyCallExpImpl"))
					javaResult="\t"+preType+" "+preValue+"Pre = "+preValue+";"; // deklaracija promenljive koja ce cuvati prethodnu vrednost atributa
				else if(elementList.get(i+1).getType().equals("OperationCallExpImpl"))
					javaResult="\t"+preType+" "+preValue+"Pre = "+preValue+"();"; // deklaracija promenljive koja ce cuvati prethodnu vrednost atributa
			
				elementList.get(i).setValue(preValue+"Pre");
				elementList.get(i).setValueType(preType);
				elementList.get(i).setType("PropertyCallExpImpl");
				fmCon.getDeclarations().add(javaResult);
				elementList.remove(i+1);	//element se brise iz liste, jer vise nije potreban
			}
		if(atPre){		
			javaResult="\t"+fmCon.getOperation().getHeaderWithoutTypes()+";"; // poziv metode
			fmCon.getDeclarations().add(javaResult);
		}
		String operName=fmCon.getOperation().getName().substring(0, index)+"Post"+fmCon.getOperation().getName().substring(index);
		fmCon.getOperation().setName(operName);
	}

	public void processElementList(){
		if(elementList.size()==1)
			fmCon.setCondition(elementList.get(0).getValue());
		for(int i=0;i<elementList.size();i++){
			if(elementList.get(i).getType().equals("OperationCallExpImpl")){
				if(fmCon.getCondition().length()==0)
					fmCon.setCondition(makeStatement(i));					
			}
			else if(elementList.get(i).getType().equals("IfExpImpl") && elementList.get(i).getValue().equals("if")){
				fmCon.setCondition(makeStatement(i+1));
				fmCon.setThenPart(makeStatement(pom+1));
				fmCon.setElsePart(makeStatement(pom+1));
			}  
			else if(elementList.get(i).getType().equals("BooleanLiteralExpImpl")){
				fmCon.setCondition(makeStatement(i));
			}
		}
	}
	
	private void processLet(){
		for(int i=0;i<elementList.size();i++){
			if(elementList.get(i).getType().equals("LetExpImpl")){
				String letJava=elementList.get(i).getValueType()+" "+elementList.get(i).getValue()+" = ";		
				letJava+=makeStatement(i+1)+";";
				for (int bound = i + 1; bound <= lastOperand; bound++)
					elementList.remove(i + 1);
				fmCon.getDeclarations().add(letJava);
			}  
		}
	}
	
	/**
	 * Sadrzaj koji se odnosi na let promenljivu se brise iz liste i prebacuje ispred naziva let promenljive
	 */
	private void prepareLet(){
		for(int i=0;i<elementList.size();i++){
			if(elementList.get(i).getType().equals("LetExpImpl")){
				LetContents let=(LetContents) elementList.get(i);
				let.determineBounds(elementList);
				for(int bound=let.getLowerBound();bound<let.getUpperBound();bound++)
					elementList.remove(let.getLowerBound());
				for(int j=let.getLetVariableContents().size();j>0;j--)
					elementList.add(i+1, let.getLetVariableContents().get(j-1));
				i+=let.getLetVariableContents().size();
			}
		}
	}
	
	public void checkCollectionLiterals(){
		for(int i=0;i<elementList.size();i++)
			if(elementList.get(i).getType().equals("CollectionLiteralExpImpl") && elementList.get(i).getValue().contains("<")){
				processCollectionLiteral(i);
			}
	}
	int index=0;
	public String processCollectionLiteral(int i){
				index++;
				elementList.get(i).setValue("list"+index);
				CollectionLiteral lit=(CollectionLiteral) elementList.get(i);
				if(lit.getValueType().startsWith("List"))
					fmCon.getDeclarations().add(lit.getValueType()+" "+lit.getValue()+"=new Array"+lit.getValueType()+"();");
				else if(lit.getValueType().startsWith("Set"))
					fmCon.getDeclarations().add(lit.getValueType()+" "+lit.getValue()+"=new Hash"+lit.getValueType()+"();");
				String literal;
				System.out.println(lit.getValue());
				for(int j=0;j<lit.getNumberOfLiterals();j++){
					if(elementList.get(i+1).getType().equals("OperationCallExpImpl")){
						literal=makeStatement(i+1);
						for(int b=i+1;b<=lastOperand;b++)
							elementList.remove(i+1);
					}
					else if(elementList.get(i+1).getType().equals("CollectionLiteralExpImpl")){
						literal=processCollectionLiteral(i+1);
						elementList.remove(i+1);
					}
					
					else{
						literal=elementList.get(i+1).getValue();
						elementList.remove(i+1);
					}
					fmCon.getDeclarations().add(lit.getValue()+".add("+literal+");");
				}
				return lit.getValue();
	}
	
	int pom=0; // cuva poziciju drugog operanda u situaciji kada nijedan od operanada nije operator
	private int lastOperand=0;	// pozicija poslennjeg clana u izrazu
	public int getLastOperand() {
		return lastOperand;
	}

	public void setLastOperand(int lastOperand) {
		this.lastOperand = lastOperand;
	}

	public String makeStatement(int i){
		String ifPart = "";	
			if(elementList.get(i).getType().equals("OperationCallExpImpl")){
				Operation operation=(Operation) elementList.get(i);
				if(operation.getTip().equals("binarni")){
					int not=0; // da li ispred uslova stoji negacija
					int poz=i;
					if(poz>0)
						if(elementList.get(i-1).getValue().equals("not()"))
							not=1;
					String secondOperandType=elementList.get(i+2).getType();
					String firstOperandValue=elementList.get(i+1).getValue();
					String secondOperandValue=elementList.get(i+2).getValue();
					
					Controller controller=new Controller();	
					controller.chooseProcessor(elementList.get(i+1));
					if(controller.getProcessor()!=null){
						controller.setCaller(this);
						firstOperandValue=controller.process(i+1);
						secondOperandValue=elementList.get(lastOperand+1).getValue();
					}
					controller.chooseProcessor(elementList.get(i+2));
					if(controller.getProcessor()!=null){
						controller.setCaller(this);
						secondOperandValue=controller.process(i+2);
					}
					 secondOperandType=elementList.get(i+2).getType();
					 firstOperandValue=elementList.get(i+1).getValue();
					 secondOperandValue=elementList.get(i+2).getValue();
					
					if(!(Operation.getBinarniOperatori().contains(firstOperandValue))){
						ifPart="( "+firstOperandValue;
						if(needEqual(elementList.get(i), elementList.get(i+1), elementList.get(i+2))){
							if(secondOperandType.equals("OperationCallExpImpl") && Operation.getBinarniOperatori().contains(secondOperandValue)) // drugi operand je operator
								ifPart+=".equals("+makeStatement(i+2)+"))";
							else{
								ifPart+=".equals("+secondOperandValue+"))";
								pom=i+2;
								lastOperand=pom;
							}
						}else{
							ifPart+=" "+elementList.get(i).getValue()+" ";
							if(secondOperandType.equals("OperationCallExpImpl") && Operation.getBinarniOperatori().contains(secondOperandValue)){
								ifPart+=makeStatement(i+2)+" )";
							}else{
								ifPart+=secondOperandValue+" )";
								pom=i+2;
								lastOperand=pom;
							}
						}
					}else if(Operation.getBinarniOperatori().contains(firstOperandValue)){  // prvi operand je operator
						ifPart+=makeStatement(i+1);
						BodyElement firstOper=new BodyElement(ifPart, "PropertyCallExpImpl");
						ifPart="( "+makeStatement(elementList.get(i),firstOper,elementList.get(pom+1),pom+1)+" )";
					}	
					if(operation.getValue().equals("implies")){
						((Operation)(elementList.get(i))).setFirstOperand(makeStatement(i+1));
						((Operation)(elementList.get(i))).setSecondOperand(makeStatement(pom+1));
						ifPart="implies("+((Operation)(elementList.get(i))).getFirstOperand()+", "+((Operation)(elementList.get(i))).getSecondOperand()+ ")";
						kroki.app.generators.utils.Operation oper=new kroki.app.generators.utils.Operation();
						oper.setName("implies");
						oper.setType(""); // da u sablonu ne bi pukao null pointer exception
						//****FMModel.getInstance().findConstraintClassByName(fmCon.getContext()).addOperation(oper);
					}
					
					if(not==1 || operation.getValue().equals("!=")){
						ifPart="!("+ifPart+")";
					}
				}
				else{
					Controller controller=new Controller();	
					controller.chooseProcessor(elementList.get(i));
					if(controller.getProcessor()!=null){
						controller.setCaller(this);
						ifPart=controller.process(i);
					}
				}
					
			}else if(elementList.get(i).getType().equals("PropertyCallExpImpl") && elementList.get(i).getValueType().equals("Boolean")){
				ifPart=elementList.get(i).getValue();
				pom=i;
				lastOperand=i;
			}else if(elementList.get(i).getType().equals("BooleanLiteralExpImpl")){
				ifPart=elementList.get(i).getValue();
				pom=i;
				lastOperand=i;
			}		
		return ifPart;
	}

	public String makeStatement(BodyElement znak, BodyElement op1, BodyElement op2, int op2Poz){
		String ifPart = "";
		//Operation o=new Operation();
				if(Operation.getBinarniOperatori().contains(znak.getValue())){
					String secondOperandType=op2.getType();
					String firstOperandValue=op1.getValue();
					String secondOperandValue=op2.getValue();
					
					Controller controller=new Controller();	
					controller.chooseProcessor(op2);
					if(controller.getProcessor()!=null){
						controller.setCaller(this);
						secondOperandValue=controller.process(op2Poz);
					}
					
					if(!(Operation.getBinarniOperatori().contains(firstOperandValue))){
						ifPart=firstOperandValue;
						if(needEqual(znak, op1, op2)){
							if(secondOperandType.equals("OperationCallExpImpl")&& Operation.getBinarniOperatori().contains(firstOperandValue))
								ifPart+=".equals("+makeStatement(op2Poz)+")";
							else{
								ifPart+=".equals("+secondOperandValue+")";
								pom=op2Poz;
								lastOperand=pom;
							}
						}else{
							ifPart+=" "+znak.getValue()+" ";
							if(secondOperandType.equals("OperationCallExpImpl") && Operation.getBinarniOperatori().contains(secondOperandValue)){
								ifPart+=makeStatement(op2Poz);
							}else{
								ifPart+=secondOperandValue;	
								pom=op2Poz;
								lastOperand=pom;
							}	
						}
					}else if(Operation.getBinarniOperatori().contains(firstOperandValue)){
						ifPart+=makeStatement(op2Poz-1);
						BodyElement fo=new BodyElement(ifPart, "PropertyCallExpImpl");
						ifPart+=makeStatement(elementList.get(op2Poz-2),fo,elementList.get(pom+1),pom+1);
					}					
				}
		return ifPart;
	}

	public List<BodyElement> linkProperties(List<BodyElement> list){
		/*String linkedValue="";
		int count=0;	// broji atribute u lancu
		int end;  // oznacava kraj lanca
		String resultType = null;
		for(int i=0;i<list.size();i++){
			end=1;
			if(list.get(i).getType().equals("PropertyCallExpImpl") || list.get(i).getType().equals("VariableExpImpl")){
				int poz=i;
				if(list.size()>poz+1){  // provera da li postoji naredni element da bi se izbegao null pointer exeption
					if(list.get(i+1).getType().equals("PropertyCallExpImpl") || list.get(i+1).getType().equals("VariableExpImpl")){
						String tip=list.get(i+1).getValueType();    // ime klase kojoj atribut pripada
						List<FMClass> classes = FMModel.getInstance().getClasses();
						for(int c=0;c<classes.size();c++){ 
							if(classes.get(c).getName().equals(tip)){  //poredi se naziv klase sa tipom atributa
								EJBClass cl=classes.get(c);
								for(int a=0;a<cl.getProperties().size();a++){
									if(cl.getProperties().get(a).getName().equals(list.get(i).getValue())){
										resultType=list.get(i+1).getType();
										end=0;
										String newPart;
										// ako je u pitanju poziv operacije koja vraca neki objekat, ne dodaje se get
										if(list.get(i+1).getValue().endsWith("()"))
											newPart=list.get(i+1).getValue();
										else
											newPart="get"+upperFirst(list.get(i+1).getValue())+"()";
										if(count==0){ // u pitanju je prvi krug
											linkedValue=newPart+".get"+upperFirst(list.get(i).getValue())+"()";
										}else{
											linkedValue=newPart+"."+linkedValue;											
										}
										count++;					
									}
								}
							}
						}
					}
				}			
			}
			if(end==1 && !(linkedValue.equals(""))){ // ubacuje se u listu dobijeni lanac i brisu se sastavni delovi
				if(resultType.equals("VariableExpImpl")){
					linkedValue=lowerFirst(linkedValue.substring(3, linkedValue.length())); // skinda se get sa pocetka
					int index=linkedValue.indexOf(".");
					linkedValue=linkedValue.substring(0, index-2)+linkedValue.substring(index); // skidanje zagrada sa prvog clana
				}
			
				BodyElement b=new BodyElement(linkedValue, resultType);
				b.setValueType(list.get(i-count).getValueType());
				list.add(i+1, b);
				linkedValue="";				
				for(int r=0;r<=count;r++){
					list.remove(i-r);
				}
				count=0;
				i--;	// prilikom brisanja, i se pomeri za jedno mesto u napred pa mora da se vrati
			}
		}*/
		return list;
	}
	
	private void processDerived() {
	/*	DerivedValue der=new DerivedValue(fmCon.getOperation().getName(),fmCon.getOperation().getType(),fmCon.getCondition());
		List<FMClass> classes=FMModel.getInstance().getClasses();
		for(int i=0;i<classes.size();i++)
			if(classes.get(i).getName().equals(fmCon.getContext())){
				FMModel.getInstance().getClasses().get(i).removeGetter(fmCon.getOperation().getName());
				FMModel.getInstance().getClasses().get(i).getDerivedValues().add(der);
			}*/
	}
	
	private Boolean needEqual(BodyElement znak, BodyElement op1, BodyElement op2){
		if(!(znak.getValue().equals("==")) && !(znak.getValue().equals("!=")))
			return false;
		int o1=1; // 0 - int, 1 - string
		int o2=1;
		if(op1.getValueType()!=null)
			if(op1.getValueType().equals("Integer"))
				o1=0;
		if(op1.getType().equals("OperationCallExpImpl") && op1.getValueType()!=null)
			if(op1.getValueType().equals("Integer"))
				o1=0;
		if(op2.getValueType()!=null)
			if(op2.getValueType().equals("Integer"))
				o2=0;
		if(op2.getType().equals("OperationCallExpImpl") && op2.getValueType()!=null)
			if(op2.getValueType().equals("Integer"))
				o2=0;
		if(o1==1 && o2==1)
			return true;
		else
			return false;
	}

	public String upperFirst(String linkedValue) {
		String s="";
		if(linkedValue.length()>0)
			s=linkedValue.substring(0, 1).toUpperCase()+linkedValue.substring(1);
		return s;
	}
	private String lowerFirst(String linkedValue) {
		String s=linkedValue.substring(0, 1).toLowerCase()+linkedValue.substring(1);
		return s;
	}
	private void checkSizeOperation(){
		for(int i=0;i<elementList.size();i++)
			if(elementList.get(i).getValue().equals("size")){
				if(elementList.get(i+1).getValueType()!=null)
					if(elementList.get(i+1).getValueType().length()>4)	// zbog substring da ne dodje do IndexOutOfBoundsException
						if(!(elementList.get(i+1).getValueType().substring(0, 3).equals("Set")) && !(elementList.get(i+1).getValueType().substring(0, 4).equals("List")))
							if(!(Operation.getSetOperatori().contains(elementList.get(i+1).getValue()))){
								elementList.get(i).setValue("length");
								((Operation)(elementList.get(i))).setTip("string");
							}
			}
	}
	
	private String correctPropertyType(String type) {
		String res=type;
		String typePart="";
		if(type.startsWith("Bag")){
			typePart=type.substring(4, type.length()-1);
			typePart=correctPropertyType(typePart);
			res="List<"+typePart+">";
		}	
		else if(type.startsWith("Sequence")){
			typePart=type.substring(9, type.length()-1);
			typePart=correctPropertyType(typePart);
			res="List<"+typePart+">";
		}	
		else if(type.startsWith("OrderedSet")){
			typePart=type.substring(11, type.length()-1);
			typePart=correctPropertyType(typePart);
			res="List<"+typePart+">";
		}	
		else if(type.substring(0, 3).equals("Set")){
			typePart=type.substring(4, type.length()-1);
			typePart=correctPropertyType(typePart);
			res="Set<"+typePart+">";
		}
		
		if(res.startsWith("List")){
			//FMModel.getInstance().findConstraintClassByName(fmCon.getContext()).addImportedPackage("import java.util.List;");
			//FMModel.getInstance().findConstraintClassByName(fmCon.getContext()).addImportedPackage("import java.util.ArrayList;");
		}		
		else if(res.startsWith("Set")){
			//FMModel.getInstance().findConstraintClassByName(fmCon.getContext()).addImportedPackage("import java.util.Set;");
			//FMModel.getInstance().findConstraintClassByName(fmCon.getContext()).addImportedPackage("import java.util.HashSet;");
		}
		return res;
	}
	
	private boolean supported() {
		for(int i=0;i<elementList.size();i++)
			if(elementList.get(i).getType().equals("OperationCallExpImpl")){
				Operation op=(Operation) elementList.get(i);
				if(op.getTip().equals("unsupported")){
					fmCon.setBody("Operacija "+op.getValue()+" nije podrzana. Ogranicenje se mora implementirati rucno.");
					fmCon.setKind("unsupported");
					return false;
				}
			}else if(!(elementList.get(i).getType().equals("VariableImpl")) && !(elementList.get(i).getType().equals("EnumLiteralExpImpl")))
				if(elementList.get(i).getValueType().startsWith("Tuple")){
					fmCon.setKind("unsupported");
					fmCon.setBody("Tip podataka Tuple nije podrzan. Ogranicenje se mora implementirati rucno.");
					return false;
				}
					
		return true;
	}
	
	public List<BodyElement> getElementList() {
		return elementList;
	}
	public void setElementList(List<BodyElement> objectList) {
		this.elementList = objectList;
	}
	public kroki.app.generators.utils.Constraint getFmCon() {
		return fmCon;
	}
}
