package bp.model.data;

import bp.details.ConditionalEdgeDetails;
import bp.model.graphic.BPEdge;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;

/**
 * Connects Vertices under some condition
 * 
 * @author Sholy
 * 
 */
public class ConditionalEdge extends Edge {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String condition;

    public String getCondition() {
        return this.condition;
    }

    public void updateCondition(final String condition, final Controller source) {
        this.condition = condition;
        fireAttributeChanged(BPKeyWords.CONDITION, this.condition, source);
    }

    public ConditionalEdge(final String uniqueName) {
        super(uniqueName);
    }

    public ConditionalEdge() { }
    
    public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
    protected void initializeComponent() {
        this.component = new BPEdge(BPEdge.CONDITIONAL_EDGE);
        this.component.setzIndex(301);
    }

    @Override
    protected void initializeDetails() {
        this.details = new ConditionalEdgeDetails(this);
    }

}
