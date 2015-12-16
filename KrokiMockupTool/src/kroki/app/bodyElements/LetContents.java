package kroki.app.bodyElements;

import java.util.ArrayList;
import java.util.List;

public class LetContents extends BodyElement{
	
	private List<BodyElement> letVariableContents=new ArrayList<BodyElement>();
	private int lowerBound;
	private int upperBound;
	
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

	

	public List<BodyElement> getLetVariableContents() {
		return letVariableContents;
	}

	public void setLetVariableContents(List<BodyElement> letVariable) {
		this.letVariableContents = letVariable;
	}

	public LetContents(String v, String t) {
		super(v, t);
	}
	
	/**
	 * Trazi se gde se u listi nalazi sadrzaj koji se odnosi na let.
	 */
	public void determineBounds(List<BodyElement> oList){
		int brojac=0;
		int oListBrojac=0;
		while(brojac<letVariableContents.size()){
			if(!(letVariableContents.get(brojac).getValue().equals(oList.get(oListBrojac).getValue())))
				oListBrojac++;
			else{
				brojac++;
				oListBrojac++;
			}
		}
		lowerBound=oListBrojac-brojac;
		upperBound=oListBrojac;
	}
}
