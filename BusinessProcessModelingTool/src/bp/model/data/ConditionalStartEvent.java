package bp.model.data;

import bp.details.ConditionalStartEventDetails;
import bp.model.graphic.EventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;

public class ConditionalStartEvent extends StartEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String condition;

    public ConditionalStartEvent(final String uniqueName) {
        super(uniqueName);
    }
    
    public ConditionalStartEvent() { }

    public String getCondition() {
        return this.condition;
    }

    public void updateCondition(final String condition, final Controller source) {
        this.condition = condition;
        fireAttributeChanged(BPKeyWords.CONDITION, this.condition, source);
    }

    public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
    protected void initializeComponent() {
        this.component = new EventComponent(this, ImageRes.CONDITION, null);
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new ConditionalStartEventDetails(this);
    }
}
