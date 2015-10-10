package bp.model.data;

import bp.details.MessageEndEventDetails;
import bp.model.graphic.EventComponent;
import bp.model.util.BPKeyWords;
import bp.model.util.Controller;
import bp.util.ImageRes;
import bp.view.Strokes;

public class MessageEndEvent extends SingleEndEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;

    public MessageEndEvent(final String uniqueName) {
        super(uniqueName);
    }
    
    public MessageEndEvent() { }

    public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
        return this.message;
    }

    public void updateMessage(final String message, final Controller source) {
        this.message = message;
        fireAttributeChanged(BPKeyWords.MESSAGE, this.message, source);
    }

    @Override
    protected void initializeComponent() {
        this.component = new EventComponent(this, ImageRes.MESSAGE, Strokes.getLine(Strokes.THICK_LINE));
        this.component.setzIndex(101);
    }

    @Override
    protected void initializeDetails() {
        this.details = new MessageEndEventDetails(this);
    }


}
