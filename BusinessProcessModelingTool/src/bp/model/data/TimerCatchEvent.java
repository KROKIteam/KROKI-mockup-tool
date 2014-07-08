package bp.model.data;

import bp.details.TimerCatchEventDetails;
import bp.model.graphic.EventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;
import bp.view.Strokes;

public class TimerCatchEvent extends CatchEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private String timeFormat;

    public TimerCatchEvent(final String uniqueName) {
        super(uniqueName);
    }

    public TimerCatchEvent() {
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
        this.component = new EventComponent(this, ImageRes.TIMER, Strokes.getDashedLine(Strokes.THIN_LINE));
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new TimerCatchEventDetails(this);
    }


}
