package kroki.app.analyzer;

import java.util.Iterator;
import java.util.List;

import kroki.app.generators.utils.*;
import tudresden.ocl20.pivot.essentialocl.expressions.Variable;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.ExpressionInOclImpl;
import tudresden.ocl20.pivot.pivotmodel.ConstrainableElement;
import tudresden.ocl20.pivot.pivotmodel.Constraint;


public class ConstraintAnalyzer {
	
	private Constraint constraint;
	private List<String> importedPackages;
	
	public ConstraintAnalyzer(Constraint con, List<String> toImport){
		constraint=con;
		importedPackages = toImport; 
	}

	public void prepareModel() {
		//Model.getInstance().getConstraints().clear();
		process();
	}

	public kroki.app.generators.utils.Constraint process() {
		@SuppressWarnings("rawtypes")
		//Iterator it=cons.iterator();
		int counter=0;
		//while(it.hasNext()){
			Constraint c= (Constraint) constraint;	
			ExpressionInOclImpl e1=(ExpressionInOclImpl) c.getSpecification();
			kroki.app.generators.utils.Constraint con=new kroki.app.generators.utils.Constraint();
			con.setKind(c.getKind().toString());
			con.setContext(e1.getContext().getType().getName());
			String body=e1.getBody();
			con.setBody(body.substring(body.indexOf(":")+1).trim());
			con.setReturnType(e1.getBodyExpression().getType().getName());			
			
			
			@SuppressWarnings("rawtypes")
			Iterator iter=e1.getParameter().iterator();
			Operation oper=new Operation();
		
			// odredjivanje parametara
			while(iter.hasNext()){
				Variable v=(Variable) iter.next();
				Parameter param=new Parameter(v.getName(), v.getType().getName());
				oper.getParametri().add(param);
			}
			// odredjivanje parametara ako je u pitanju definicija
			if(con.getKind().equals("definition")){
				int pocetak=con.getBody().indexOf("(");
				int kraj=con.getBody().indexOf(")");
				String params=con.getBody().substring(pocetak+1, kraj);
				if(params.length()>0){
					String[] parts=params.split(",");
					for(int i=0;i<parts.length;i++){
						String[] param=parts[i].split(":");
						String name=param[0].trim();
						String type=param[1].trim();
						Parameter p=new Parameter(name, type);
						oper.getParametri().add(p);
					}
				}
			}
		
			String operationName="check"+con.getContext()+"Invariant"+counter;			
			if(c.getDefinedFeature()!=null)
				operationName=c.getDefinedFeature().getName();
			else{
				for(int ce=0;ce<c.getConstrainedElement().size();ce++){
					ConstrainableElement elem=c.getConstrainedElement().get(ce);
					String s=elem.toString();
					int index=s.indexOf("OperationImpl[name=");
					int zarez=s.indexOf(",");
					if(index==0){
						s=s.substring(19, zarez);
						operationName=s;
					}
				}
			}
			if(con.getReturnType().startsWith("Set("))
				con.setReturnType("Set<"+con.getReturnType().substring(4, con.getReturnType().length()-1)+">");
			
			oper.setName(operationName);
			oper.setType(con.getReturnType());
			
			oper.makeOperationHeader();
			con.setOperation(oper);
			
			//FMClass conClass=new FMClass(con.getContext(),"ejb", "public");
			//FMClass tempClass=FMModel.getInstance().findClassByName(con.getContext());
			//conClass.setConstructorParams(tempClass.getConstructorParams());
			//FMModel.getInstance().addConstraintClass(conClass);
			// provera da li je neki od parametara tipa Date, ako jeste importuje se
			for(int i=0;i<oper.getParametri().size();i++){
				if(oper.getParametri().get(i).getType().equals("Date"))
					importedPackages.add("import java.util.Date;");
			}
			
			
			ConstraintBodyAnalyzer cba=new ConstraintBodyAnalyzer(c,con);
			cba.setElementList(cba.getContentsData(c.eAllContents()));
			cba.processBody();
			
			//FMModel.getInstance().getConstraints().add(con);
			//FMModel.getInstance().findConstraintClassByName(con.getContext()).getConstraints().add(con);
			
			counter++;
			return con;
		//}
		/*for(int i=0;i<FMModel.getInstance().getConstraints().size();i++){
			System.out.println(FMModel.getInstance().getConstraints().get(i));
			System.out.println("");
			System.out.println("");
		}*/
	}

	

}
