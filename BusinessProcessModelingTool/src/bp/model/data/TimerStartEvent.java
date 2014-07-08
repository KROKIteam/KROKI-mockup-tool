package bp.model.data;

import bp.details.TimerStartEventDetails;
import bp.model.graphic.EventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;

public class TimerStartEvent extends StartEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private String timeFormat;

    public TimerStartEvent(final String uniqueName) {
        super(uniqueName);
    }

    public TimerStartEvent() {
		super();
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public String getTimeFormat() {
        return this.timeFormat;
    }

    public void updateTimeFormat(final String timeFormat, final Controller source) {
        this.timeFormat = timeFormat;
        fireAttributeChanged(BPKeyWords.TIME_FORMAT, this.timeFormat, source);
    }

    @Override
    protected void initializeComponent() {
        this.component = new EventComponent(this, ImageRes.TIMER, null);
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new TimerStartEventDetails(this);
    }

}
