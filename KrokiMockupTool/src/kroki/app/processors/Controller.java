package kroki.app.processors;

import kroki.app.analyzer.ConstraintBodyAnalyzer;
import kroki.app.bodyElements.BodyElement;
import kroki.app.bodyElements.Operation;
import kroki.app.processors.*;


public class Controller {
	
	private Processor processor;

	public Processor getProcessor() {
		return processor;
	}

	public void setProcessor(Processor processor) {
		this.processor = processor;
	}
	
	public void chooseProcessor(BodyElement op){
		if(op instanceof Operation){
			Operation o=(Operation) op;
			if(o.getTip().equals("string"))
				setProcessor(new StringProcessor());
			else if(o.getTip().equals("math"))
				setProcessor(new MathProcessor());	
			else if(o.getTip().equals("set"))
				setProcessor(new CollectionProcessor());
			else if(o.getTip().equals("loop"))
				setProcessor(new LoopProcessor());
			else if(o.getTip().equals("unarni"))
				setProcessor(new UnarProcessor());
		}	
	}
	
	public String process(int i){
		String res=processor.process(i);
		setProcessor(null);
		return res;
	}

	public void setCaller(ConstraintBodyAnalyzer constraintBodyAnalyzer) {
		processor.setCaller(constraintBodyAnalyzer);
	}

}
