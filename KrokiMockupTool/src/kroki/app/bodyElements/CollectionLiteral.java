package kroki.app.bodyElements;

import java.util.ArrayList;
import java.util.List;


public class CollectionLiteral extends BodyElement{
	
	private int numberOfLiterals;
	private List<BodyElement> contents=new ArrayList<BodyElement>();
	
	public CollectionLiteral(String value, String type){
		super(value,type);
	}
	
	public List<BodyElement> getContents() {
		return contents;
	}

	public void setContents(List<BodyElement> contents) {
		this.contents = contents;
	}

	public int getNumberOfLiterals() {
		return numberOfLiterals;
	}

	public void setNumberOfLiterals(int numberOfLiterals) {
		this.numberOfLiterals = numberOfLiterals;
	}

}
