package bp.model.data;

import bp.details.SignalEndEventDetails;
import bp.model.graphic.EventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;
import bp.view.Strokes;

public class SignalEndEvent extends SingleEndEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private String signalData;
    private String signalName;

    public SignalEndEvent(final String uniqueName) {
        super(uniqueName);
    }

    public SignalEndEvent() {
		super();
	}

	public void setSignalData(String signalData) {
		this.signalData = signalData;
	}

	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}

	public String getSignalData() {
        return this.signalData;
    }

    public void updateSignalData(final String signalData, final Controller source) {
        this.signalData = signalData;
        fireAttributeChanged(BPKeyWords.SIGNAL_DATA, this.signalData, source);
    }

    public String getSignalName() {
        return this.signalName;
    }

    public void updateSignalName(final String signalName, final Controller source) {
        this.signalName = signalName;
        fireAttributeChanged(BPKeyWords.SIGNAL_NAME, this.signalName, source);
    }

    @Override
    protected void initializeComponent() {
        this.component = new EventComponent(this, ImageRes.SIGNAL, Strokes.getLine(Strokes.THICK_LINE));
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new SignalEndEventDetails(this);
    }

}
