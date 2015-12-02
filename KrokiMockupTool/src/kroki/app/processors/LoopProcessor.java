package kroki.app.processors;

import java.util.ArrayList;
import java.util.List;

import kroki.app.bodyElements.*;
import kroki.app.bodyElements.Operation;
import kroki.app.generators.utils.*;

public class LoopProcessor extends Processor{

	@Override
	public String process(int i) {
		Operation iterData=(Operation) caller.getElementList().get(i);
		List<BodyElement> operList=caller.linkProperties(iterData.getElementList());
		int operListSize=operList.size();
		kroki.app.generators.utils.Operation setOper=new kroki.app.generators.utils.Operation();
		Operation o = null;
		o=  (Operation) caller.getElementList().get(i);
		String name;
		
		int lop=1;	// lastOperandPosition
		for(int x=0;x<operList.size();x++)
			if(operList.get(x).getType().equals("CollectionLiteralExpImpl")){
				CollectionLiteral col=(CollectionLiteral) operList.get(x);
				List<BodyElement> colList=caller.linkProperties(col.getContents());
				operListSize-=colList.size();
				lop=0;
			}
	
		if(caller.getElementList().get(i+1).getType().equals("OperationCallExpImpl")){
			Operation nextOper=(Operation) caller.getElementList().get(i+1);
			List<BodyElement> nextOperList=caller.linkProperties(nextOper.getElementList());
			operListSize-=nextOperList.size();
			name=o.getValue()+caller.upperFirst(caller.makeStatement(i+1));
		}
		else{
			name=o.getValue()+caller.upperFirst(caller.getElementList().get(i+1).getValue());
		}
		
		name="";
				//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).correctOperName(name);
		
		int p=0;	//prvi property je kolekcija i treba da se predkoci
		for(int j=i+1;j <= i+operListSize;j++)
			if(caller.getElementList().get(j).getType().equals("PropertyCallExpImpl")){
				if(p==1){
					String value=caller.getElementList().get(j).getValue();
					if(value.startsWith("get") && value.endsWith("()"))
						caller.getElementList().get(j).setValue("x."+value);
					else
						caller.getElementList().get(j).setValue("x.get"+caller.upperFirst(value)+"()");
				}
				p=1;					
			}else if(caller.getElementList().get(j).getType().equals("CollectionLiteralExpImpl")){
				Parameter par=new Parameter(caller.getElementList().get(j).getValue(), caller.getElementList().get(j).getValueType());
				caller.getFmCon().getOperation().getParametri().add(par);
				lop=0;
			}
		
		
		int index=caller.getFmCon().getBody().indexOf(caller.getElementList().get(i).getValue());
		String body=caller.getFmCon().getBody().substring(index);
		index=body.indexOf(")");
		body=body.substring(0, index);
		ArrayList<Integer> indeksiZaBrisanje=new ArrayList<Integer>();
		if(body.indexOf("|")!=-1)
			for(int j=i+1;j <= i+operListSize;j++)
				if(caller.getElementList().get(j).getType().equals("VariableExpImpl"))
					for(int y=0;y<iterData.getElementList().size();y++)
						if(iterData.getElementList().get(y).getType().equals("VariableImpl"))
							if(iterData.getElementList().get(y).getValue().equals(caller.getElementList().get(j).getValue())){
								String variableImplValue=iterData.getElementList().get(y).getValue();
								caller.getElementList().get(j).setValue("x");
								for(int x=j+1;x<=i+operListSize;x++)
									if(caller.getElementList().get(x).getType().equals("VariableImpl") && caller.getElementList().get(x).getValue().equals(variableImplValue))
										indeksiZaBrisanje.add(x);
							}	
		for(int y=indeksiZaBrisanje.size();y>0;y--){
			int ind=indeksiZaBrisanje.get(y-1);
			caller.getElementList().remove(ind);
		}
				
		
		setOper.setForParam(caller.getElementList().get(i+1).getValue());
		setOper.setIterType(getSetElemType(caller.getElementList().get(i+1).getValueType()));
		if(caller.getElementList().get(i+2).getType().equals("OperationCallExpImpl"))
			setOper.setIfCondition(caller.makeStatement(i+2));
		else
			setOper.setIfCondition(caller.getElementList().get(i+2).getValue());
		// kasnije se u ime metode doda i tip parametara, a treba da se vrati bez. Zato se cuva u result
		String result=name;
		if(!(name.endsWith(")"))){
			result=name+caller.getFmCon().getOperation().getHeaderWithoutNameAndTypes();	
		}
		setOper.setName(result);
		
		
		if(o.getValue().equals("exists") || o.getValue().equals("forAll") || o.getValue().equals("one")){
			removeUsedElements(i, setOper.getName(), "BooleanLiteralExpImpl", "Boolean");
		}		
		else if(o.getValue().equals("select") || o.getValue().equals("reject")){
			if(o.getValue().equals("reject"))
				setOper.setIfCondition("!("+setOper.getIfCondition()+")");
			setOper.setType(caller.getElementList().get(i+1).getValueType());
			removeUsedElements(i, setOper.getName(), "PropertyCallExpImpl", caller.getElementList().get(i+1).getValueType());
			//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addImportedPackage("import java.util.Set;");
			//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addImportedPackage("import java.util.HashSet;");	
		}					
		else if(o.getValue().equals("sum")){
			if(lop==0)
				setOper.setPropertyInSet("x");
			else
				setOper.setPropertyInSet(caller.getElementList().get(i+2).getValue());
			caller.setLastOperand(i+1+lop);
			removeUsedElements(i, setOper.getName(), "IntegerLiteralExpImpl", "Integer");
		}
		else if(o.getValue().equals("isUnique")){
			removeUsedElements(i, setOper.getName(), "BooleanLiteralExpImpl", "Boolean");
			//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addImportedPackage("import java.util.ArrayList;");	
		}	
		else if(o.getValue().equals("any")){
			removeUsedElements(i, setOper.getName(), "PropertyCallExpImpl", setOper.getIterType());
			//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addImportedPackage("import java.util.Random;");	
			//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addImportedPackage("import java.util.ArrayList;");	
			caller.setElementList(caller.linkProperties(caller.getElementList()));
		}
		else if(o.getValue().equals("collect")){
			setOper.setType(caller.getElementList().get(i+2).getValueType());
			caller.setLastOperand(i+1+lop);
			removeUsedElements(i, setOper.getName(), "OperationCallExpImpl", "Integer");
			//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addImportedPackage("import java.util.ArrayList;");	
		}
		setOper.setName(deleteHeader(name)+caller.getFmCon().getOperation().getHeaderWithoutName());
		//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addOperation(setOper);
		//FMModel.getInstance().findConstraintClassByName(caller.getFmCon().getContext()).addImportedPackage("import java.util.Iterator;");
		return result;
	}
	
	
	
	@Override
	public void removeUsedElements(int i, String result, String newElementClass, String newElementType){
		for (int bound = i + 1; bound <= caller.getLastOperand(); bound++)
			caller.getElementList().remove(i + 1);
		caller.setLastOperand(i);	
		BodyElement b=new BodyElement(result,newElementClass);
		b.setValueType(newElementType);
		caller.getElementList().set(i, b);
	}

	
	public String deleteHeader(String s) {
		String res=s;
		int index = s.indexOf("(");
		if(index!=-1)
			res=s.substring(0, index);
		return res;
	}

}
