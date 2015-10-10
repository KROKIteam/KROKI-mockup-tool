package bp.model.data;

import bp.details.ConditionalActivityEventDetails;
import bp.model.graphic.ActivityEventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;
import bp.view.Strokes;

public class ConditionalActivityEvent extends ActivityEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String condition;
    private Boolean stopActivity;

    public ConditionalActivityEvent(final String uniqueName) {
        super(uniqueName);
    }

    public ConditionalActivityEvent() { }
    
    public String getCondition() {
        return this.condition;
    }

    public void updateCondition(final String condition, final Controller source) {
        this.condition = condition;
        fireAttributeChanged(BPKeyWords.CONDITION, this.condition, source);
    }

    public Boolean getStopActivity() {
        return this.stopActivity;
    }

    public void updateStopActivity(final Boolean stopActivity, final Controller source) {
        this.stopActivity = stopActivity;
        fireAttributeChanged(BPKeyWords.STOP_ACTIVITY, this.stopActivity, source);
    }

    public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setStopActivity(Boolean stopActivity) {
		this.stopActivity = stopActivity;
	}

	@Override
    protected void initializeComponent() {
        this.component = new ActivityEventComponent(this, ImageRes.CONDITION, Strokes.getDashedLine(Strokes.THIN_LINE));
        this.component.setzIndex(201);
    }

    @Override
    protected void initializeDetails() {
        this.details = new ConditionalActivityEventDetails(this);
    }
}
