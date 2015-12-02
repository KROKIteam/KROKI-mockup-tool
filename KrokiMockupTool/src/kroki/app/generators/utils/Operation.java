package kroki.app.generators.utils;

import java.util.ArrayList;

public class Operation extends Element{
	
	private String type;
	private String visibility;
	private ArrayList<Parameter> parametri=new ArrayList<Parameter>();
	private String header;	// naziv metode sa svim parametrima
	private String body;
	private String forParam;	// atribut kroz koji se iterira
	private String ifCondition;	// atrubut u if uslovu
	private String returnValue="";	// povratna vrednost
	private String propertyInSet="";	//  vrednost iza set atributa
	private String iterType="";	//  tip elementa kolekcije kroz koju se iterira
	

	public String getIterType() {
		return iterType;
	}

	public void setIterType(String iterType) {
		this.iterType = iterType;
	}

	public String getPropertyInSet() {
		return propertyInSet;
	}

	public void setPropertyInSet(String propertyInSet) {
		this.propertyInSet = propertyInSet;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public String getForParam() {
		return forParam;
	}

	public void setForParam(String forParam) {
		this.forParam = forParam;
	}

	public String getIfCondition() {
		return ifCondition;
	}

	public void setIfCondition(String ifCondition) {
		this.ifCondition = ifCondition;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public ArrayList<Parameter> getParametri() {
		return parametri;
	}

	public void setParametri(ArrayList<Parameter> parametri) {
		this.parametri = parametri;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Operation(String name, String type) {
		super(name);
		this.type = type;
	}
	
	public void makeOperationHeader() {
		String header=getName()+"(";
		if(getParametri().size()>0){
			for(int i=0;i<getParametri().size();i++){
				header+=getParametri().get(i).getType()+" "+getParametri().get(i).getName();
				int poz=i;
				if(poz!=getParametri().size()-1)
					header+=", ";
			}	
		}
		header+=")";
		this.header = header;
	}
	
	public String getHeaderWithoutTypes(){
		String header=getName()+"(";
		if(getParametri().size()>0){
			for(int i=0;i<getParametri().size();i++){
				header+=getParametri().get(i).getName();
				int poz=i;
				if(poz!=getParametri().size()-1)
					header+=", ";
			}	
		}
		header+=")";
		return header;
	}
	
	public String getHeaderWithoutName(){
		String header="(";
		if(getParametri().size()>0){
			for(int i=0;i<getParametri().size();i++){
				header+=getParametri().get(i).getType()+" "+getParametri().get(i).getName();
				int poz=i;
				if(poz!=getParametri().size()-1)
					header+=", ";
			}	
		}
		header+=")";
		return header;
		/*int big=header.indexOf("(");
		int end=header.indexOf(")");
		return header.substring(big,end+1);*/
	}
	
	public String getHeaderWithoutNameAndTypes(){
		String hwt=getHeaderWithoutTypes();
		int big=hwt.indexOf("(");
		int end=hwt.indexOf(")");
		return hwt.substring(big,end+1);
	}

	public Operation(){}

}
