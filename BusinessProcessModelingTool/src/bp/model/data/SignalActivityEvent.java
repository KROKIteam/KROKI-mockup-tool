package bp.model.data;

import bp.details.SignalActivityEventDetails;
import bp.model.graphic.ActivityEventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;
import bp.view.Strokes;

public class SignalActivityEvent extends ActivityEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dataFormat;
    private String signalName;
    private Boolean stopActivity;

    public SignalActivityEvent(final String uniqueName) {
        super(uniqueName);
    }

    public SignalActivityEvent() { }
    
    public String getDataFormat() {
        return this.dataFormat;
    }

    public void updateDataFormat(final String dataFormat, final Controller source) {
        this.dataFormat = dataFormat;
        fireAttributeChanged(BPKeyWords.DATA_FORMAT, this.dataFormat, source);
    }

    public String getSignalName() {
        return this.signalName;
    }

    public void updateSignalName(final String signalName, final Controller source) {
        this.signalName = signalName;
        fireAttributeChanged(BPKeyWords.SIGNAL_NAME, this.signalName, source);
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
        this.component = new ActivityEventComponent(this, ImageRes.SIGNAL, Strokes.getDashedLine(Strokes.THIN_LINE));
        this.component.setzIndex(201);
    }

    @Override
    protected void initializeDetails() {
        this.details = new SignalActivityEventDetails(this);
    }

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}

	public void setStopActivity(Boolean stopActivity) {
		this.stopActivity = stopActivity;
	}

}
