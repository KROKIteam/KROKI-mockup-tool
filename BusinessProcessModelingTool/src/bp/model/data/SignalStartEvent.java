package bp.model.data;

import bp.details.SignalStartEventDetails;
import bp.model.graphic.EventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;

public class SignalStartEvent extends StartEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private String dataFormat;
    private String signalName;

    public SignalStartEvent(final String uniqueName) {
        super(uniqueName);
    }

    public SignalStartEvent() {
		super();
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}

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

    @Override
    protected void initializeComponent() {
        this.component = new EventComponent(this, ImageRes.SIGNAL, null);
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new SignalStartEventDetails(this);
    }

}
