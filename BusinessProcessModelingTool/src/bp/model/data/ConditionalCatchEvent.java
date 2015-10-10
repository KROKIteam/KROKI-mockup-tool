package bp.model.data;

import bp.details.ConditionalCatchEventDetails;
import bp.model.graphic.EventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;
import bp.view.Strokes;

public class ConditionalCatchEvent extends CatchEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String condition;

    public ConditionalCatchEvent(final String uniqueName) {
        super(uniqueName);
    }

    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition) {
		this.condition = condition;
	}

	public void updateCondition(final String condition, final Controller source) {
        this.condition = condition;
        fireAttributeChanged(BPKeyWords.CONDITION, this.condition, source);
    }

    @Override
    protected void initializeComponent() {
        this.component = new EventComponent(this, ImageRes.CONDITION, Strokes.getDashedLine(Strokes.THIN_LINE));
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new ConditionalCatchEventDetails(this);
    }

}
