package bp.model.data;

import bp.details.TimerActivityEventDetails;
import bp.model.graphic.ActivityEventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;
import bp.view.Strokes;

public class TimerActivityEvent extends ActivityEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private String timeFormat;
    private Boolean stopActivity;

    public TimerActivityEvent(final String uniqueName) {
        super(uniqueName);
    }

    public TimerActivityEvent() {
		super();
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public void setStopActivity(Boolean stopActivity) {
		this.stopActivity = stopActivity;
	}

	public String getTimeFormat() {
        return this.timeFormat;
    }

    public void updateTimeFormat(final String timeFormat, final Controller source) {
        this.timeFormat = timeFormat;
        fireAttributeChanged(BPKeyWords.TIME_FORMAT, this.timeFormat, source);
    }

    public Boolean getStopActivity() {
        return this.stopActivity;
    }

    public void updateStopActivity(final Boolean stopActivity, final Controller source) {
        this.stopActivity = stopActivity;
        fireAttributeChanged(BPKeyWords.STOP_ACTIVITY, this.stopActivity, source);
    }

    @Override
    protected void initializeComponent() {
        this.component = new ActivityEventComponent(this, ImageRes.TIMER, Strokes.getDashedLine(Strokes.THIN_LINE));
        this.component.setzIndex(201);
    }

    @Override
    protected void initializeDetails() {
        this.details = new TimerActivityEventDetails(this);
    }

}
