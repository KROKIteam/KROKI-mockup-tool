package bp.model.data;

import bp.details.MessageStartEventDetails;
import bp.model.graphic.EventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;

public class MessageStartEvent extends StartEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dataFormat;

    public MessageStartEvent(final String uniqueName) {
        super(uniqueName);
    }
    
    public MessageStartEvent() { }
    
    public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getDataFormat() {
        return this.dataFormat;
    }

    public void updateDataFormat(final String dataFormat, final Controller source) {
        this.dataFormat = dataFormat;
        fireAttributeChanged(BPKeyWords.DATA_FORMAT, this.dataFormat, source);
    }

    @Override
    protected void initializeComponent() {
        this.component = new EventComponent(this, ImageRes.MESSAGE, null);
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new MessageStartEventDetails(this);
    };

}
