package bp.model.data;

import bp.details.MessageActivityEventDetails;
import bp.model.graphic.ActivityEventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;
import bp.view.Strokes;

public class MessageActivityEvent extends ActivityEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dataFormat;
    private Boolean stopActivity;

    public MessageActivityEvent(final String uniqueName) {
        super(uniqueName);
    }

    public MessageActivityEvent() { }
    
    public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public void setStopActivity(Boolean stopActivity) {
		this.stopActivity = stopActivity;
	}

	public String getDataFormat() {
        return this.dataFormat;
    }

    public void updateDataFormat(final String dataFormat, final Controller source) {
        this.dataFormat = dataFormat;
        fireAttributeChanged(BPKeyWords.DATA_FORMAT, this.dataFormat, source);
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
        this.component = new ActivityEventComponent(this, ImageRes.MESSAGE, Strokes.getDashedLine(Strokes.THIN_LINE));
        this.component.setzIndex(201);
    }

    @Override
    protected void initializeDetails() {
        this.details = new MessageActivityEventDetails(this);
    }
}
